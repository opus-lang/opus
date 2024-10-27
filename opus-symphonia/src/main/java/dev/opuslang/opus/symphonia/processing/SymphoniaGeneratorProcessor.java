package dev.opuslang.opus.symphonia.processing;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.MirroredTypeException;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.*;
import java.util.function.Supplier;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "dev.opuslang.opus.symphonia.annotation.Symphonia.Package",
        "dev.opuslang.opus.symphonia.annotation.Symphonia.Component"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SuppressWarnings("unused")
public class SymphoniaGeneratorProcessor extends AbstractProcessor {

    private Messager messager;
    private Filer filer;
    private Elements elementUtils;
    private Types typeUtils;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnv) {
        super.init(processingEnv);
        this.messager = processingEnv.getMessager();
        this.filer = processingEnv.getFiler();
        this.elementUtils = processingEnv.getElementUtils();
        this.typeUtils = processingEnv.getTypeUtils();
    }

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        if(!annotations.isEmpty()) this.messager.printNote("Generating Symphonias...");

        Map<PackageElement, Set<TypeElement>> classesByPackage = new HashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(Symphonia.Package.class)) {
            if(element instanceof PackageElement packageElement){
                classesByPackage.putIfAbsent(packageElement, new HashSet<>());
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Symphonia.Component.class)) {
            if(element instanceof TypeElement typeElement){
                if(!hasNoArgConstructor(typeElement)){
                    this.messager.printError("Symphonia.Component must have a no-arg constructor.", typeElement);
                }
                PackageElement packageElement = this.elementUtils.getPackageOf(typeElement);

                if (classesByPackage.containsKey(packageElement)) {
                    classesByPackage.get(packageElement).add(typeElement);
                }
            }
        }

        for(Map.Entry<PackageElement, Set<TypeElement>> entry : classesByPackage.entrySet()){
            for(TypeElement componentElement : entry.getValue()) {
                checkRequiredBindings(entry.getKey().getAnnotation(Symphonia.Package.class), componentElement).ifPresent(absentInjection -> {
                    this.messager.printError(String.format("Symphonia.Component does not inject a required field '%s'.", absentInjection), componentElement);
                });
            }
            try {
                this.generateClass(entry.getKey(), entry.getValue());
            } catch (IOException e) {
                this.messager.printError("Failed to generate Symphonia class: " + e.getMessage());
            }
        }
        return true;
    }

    private void generateClass(PackageElement packageElement, Set<TypeElement> componentElements) throws IOException {
        Symphonia.Package symphoniaPackageAnnotation = packageElement.getAnnotation(Symphonia.Package.class);

        String setterClassName = symphoniaPackageAnnotation.outputClass();
        String annotatedPackageName = packageElement.getQualifiedName().toString();

        TypeSpec.Builder setterClassBuilder = TypeSpec.classBuilder(setterClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT);

        MethodSpec.Builder setterConstructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PROTECTED);

        for (Symphonia.Provider providerAnnotation : symphoniaPackageAnnotation.additionalProviders()) {
            TypeMirror type = getClassSafely(providerAnnotation::type).getTypeMirror();
            setterClassBuilder.addField(TypeName.get(type), providerAnnotation.name(), Modifier.PROTECTED, Modifier.FINAL);
            setterConstructorBuilder.addStatement("this.$L = $L", providerAnnotation.name(), providerAnnotation.name());
            setterConstructorBuilder.addParameter(TypeName.get(type), providerAnnotation.name());
        }

        CodeBlock.Builder injectionBlock = CodeBlock.builder();

        for (TypeElement componentElement : componentElements) {
            TypeName componentTypeName = TypeName.get(componentElement.asType());
            String componentFieldName = getComponentFieldName(componentElement);
            setterClassBuilder.addField(componentTypeName, componentFieldName, Modifier.PROTECTED, Modifier.FINAL);
            setterConstructorBuilder.addStatement("this.$L = new $T()", componentFieldName, componentElement.asType());

            injectorLoop: for(Element enclosedElement : componentElement.getEnclosedElements()){
                if(isInjectable(enclosedElement)){
                    VariableElement variableElement = (VariableElement) enclosedElement;
                    if(variableElement.getModifiers().contains(Modifier.PRIVATE))
                        this.messager.printError("Symphonia.Inject fields must be non-private.", variableElement);
                    if(variableElement.getModifiers().contains(Modifier.FINAL))
                        this.messager.printError("Symphonia.Inject fields must be non-final.", variableElement);

                    String fieldName = variableElement.getSimpleName().toString();
                    TypeMirror fieldType = variableElement.asType();

                    Optional<String> matchingField = this.findMatchingField(symphoniaPackageAnnotation, fieldType);
                    boolean foundField = false;
                    if(matchingField.isPresent()){
                        injectionBlock.addStatement("this.$L.$L = this.$L",
                                componentFieldName, fieldName, matchingField.get());
                        continue injectorLoop;
                    }else{
                        // If no matching Provider, attempt to search among other components.
                        for(TypeElement otherComponentElement : componentElements){
                            if (this.typeUtils.isSameType(otherComponentElement.asType(), fieldType)) {
                                String otherComponentFieldName = getComponentFieldName(otherComponentElement);
                                injectionBlock.addStatement("this.$L.$L = this.$L",
                                        componentFieldName, fieldName, otherComponentFieldName);
                                continue injectorLoop;
                            }
                        }
                    }
                    this.messager.printError("Trying to Symphonia.Inject a non-provided variable.", variableElement);
                }
            }
        }

        setterConstructorBuilder.addCode(injectionBlock.build());
        setterClassBuilder.addMethod(setterConstructorBuilder.build());

        JavaFile setterFile = JavaFile.builder(annotatedPackageName, setterClassBuilder.build())
                .build();
        setterFile.writeTo(filer);
    }

    private Optional<String> findMatchingField(Symphonia.Package symphoniaPackageAnnotation, TypeMirror fieldType) {
        for (Symphonia.Provider providerAnnotation : symphoniaPackageAnnotation.additionalProviders()) {
            return this.typeUtils.isSameType(fieldType, getClassSafely(providerAnnotation::type).getTypeMirror()) ? Optional.of(providerAnnotation.name()) : Optional.empty();
        }
        return Optional.empty();
    }

    private Optional<TypeMirror> checkRequiredBindings(Symphonia.Package symphoniaPackageAnnotation, TypeElement componentElement) {
        providerLoop: for(Symphonia.Provider providerAnnotation : symphoniaPackageAnnotation.additionalProviders()){
            if(providerAnnotation.required()){
                TypeMirror type = getClassSafely(providerAnnotation::type).getTypeMirror();
                for(Element enclosedElement : componentElement.getEnclosedElements()){
                    if(isInjectable(enclosedElement) && this.typeUtils.isSameType(type, enclosedElement.asType())){
                            continue providerLoop;
                    }
                }
                return Optional.of(type);
            }
        }
        return Optional.empty();
    }

    private static boolean isInjectable(Element element){
        return element.getKind() == ElementKind.FIELD && element.getAnnotation(Symphonia.Inject.class) != null;
    }

    private static boolean hasNoArgConstructor(TypeElement typeElement) {
        for (Element enclosed : typeElement.getEnclosedElements()) {
            if (enclosed.getKind() == ElementKind.CONSTRUCTOR) {
                ExecutableElement constructor = (ExecutableElement) enclosed;
                if (constructor.getParameters().isEmpty()) {
                    return true;
                }
            }
        }
        return false;
    }

    private static MirroredTypeException getClassSafely(Supplier<Class<?>> classSupplier) {
        try {
            classSupplier.get();
            assert false; // must never happen
        } catch (MirroredTypeException e) {
            return e;
        }
        assert false; // must never happen
        return new MirroredTypeException(null);
    }

    private static String getComponentFieldName(TypeElement componentElement){
        Symphonia.Component componentAnnotation = componentElement.getAnnotation(Symphonia.Component.class);
        if(componentAnnotation == null || componentAnnotation.name().isBlank()){
            return toCamelCase(componentElement.getSimpleName().toString());
        }else{
            return componentAnnotation.name();
        }
    }

    private static String toCamelCase(String str){
        if(str == null) return null;
        if(str.isBlank()) return str;
        String start = str.substring(0, 1).toLowerCase();
        if(str.length() == 1) return start;
        return start + str.substring(1);
    }

}
