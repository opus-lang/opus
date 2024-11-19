package dev.opuslang.opus.symphonia.processing;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;
import dev.opuslang.opus.symphonia.annotation.Symphonia;
import dev.opuslang.opus.symphonia.utils.SymphoniaUtils;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "dev.opuslang.opus.symphonia.annotation.Symphonia.Builder.Buildable",
        "dev.opuslang.opus.symphonia.annotation.Symphonia.Builder.Final",
        "dev.opuslang.opus.symphonia.annotation.Symphonia.Builder.Optional",
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SuppressWarnings("unused")
public class SymphoniaBuilderGeneratorProcessor extends AbstractProcessor {

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
        if(!annotations.isEmpty()) this.messager.printNote("Generating Symphonia.Builders...");

        for (Element annotatedElement : roundEnv.getElementsAnnotatedWith(Symphonia.Builder.Buildable.class)) {
            if(annotatedElement instanceof TypeElement typeElement){
                try {
                    this.generateAbstractBuilderClass(typeElement);
                } catch (IOException e) {
                    this.messager.printError("Failed to generate Symphonia.Builder class: " + e.getMessage());
                }
            }
        }
        return true;
    }

    private void generateAbstractBuilderClass(TypeElement typeElement) throws IOException {
        Symphonia.Builder.Buildable builderAnnotation = typeElement.getAnnotation(Symphonia.Builder.Buildable.class);

        String packageName = this.elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        String className = typeElement.getSimpleName().toString();
        String builderClassName = className+ "_Builder";

        List<ExecutableElement> abstractMethods = this.collectAbstractMethods(typeElement);
        TypeSpec implClass = this.generateImplClass(typeElement);

        MethodSpec.Builder builderClassConstructorBuilder = MethodSpec
                .constructorBuilder()
                .addModifiers(Modifier.PROTECTED);

        MethodSpec.Builder builderClassCopyConstructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC)
                .addParameter(TypeName.get(typeElement.asType()), "other");

        TypeVariableName builderReturnType = TypeVariableName.get("T");
        TypeSpec.Builder builderClassBuilder = TypeSpec.classBuilder(builderClassName)
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT, Modifier.SEALED)
                .addPermittedSubclass(SymphoniaUtils.getClassSafely(builderAnnotation::value).getTypeMirror())
                .addType(implClass);

        CodeBlock.Builder validationCodeBlockBuilder = CodeBlock.builder();

        for (ExecutableElement method : abstractMethods) {
            String fieldName = method.getSimpleName().toString();
            TypeName fieldType = TypeName.get(method.getReturnType());

            FieldSpec.Builder field = FieldSpec.builder(fieldType, fieldName, Modifier.PROTECTED);

            if(method.getAnnotation(Symphonia.Builder.Optional.class) == null){
                validationCodeBlockBuilder
                        .beginControlFlow("if ($L == null)", fieldName)
                        .addStatement("throw new $T()", IllegalStateException.class)
                        .endControlFlow();
            }

            boolean createSetter = true;
            if(method.getAnnotation(Symphonia.Builder.Final.class) != null){
                field.addModifiers(Modifier.FINAL);
                builderClassConstructorBuilder.addParameter(fieldType, fieldName);
                builderClassConstructorBuilder.addStatement("this.$N = $N", fieldName, fieldName);
                createSetter = false;
            }

            MethodSpec setter = MethodSpec.methodBuilder(fieldName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(TypeName.get(SymphoniaUtils.getClassSafely(builderAnnotation::value).getTypeMirror()))
                    .addParameter(fieldType, fieldName)
                    .addStatement("this.$N = $N", fieldName, fieldName)
                    .addStatement("return ($L)this", TypeName.get(SymphoniaUtils.getClassSafely(builderAnnotation::value).getTypeMirror()))
                    .build();

            MethodSpec getter = MethodSpec.methodBuilder(fieldName)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(fieldType)
                    .addStatement("return this.$N", fieldName)
                    .build();

            builderClassCopyConstructorBuilder.addStatement("this.$N = other.$N()", fieldName, fieldName);
            builderClassBuilder.addField(field.build());
            builderClassBuilder.addMethod(getter);
            if(createSetter) builderClassBuilder.addMethod(setter);
        }

        MethodSpec validateAndPrepareMethod = MethodSpec.methodBuilder("validateAndPrepare")
                .addModifiers(Modifier.PROTECTED)
                .addCode(validationCodeBlockBuilder.build())
                .addStatement("return true")
                .returns(TypeName.BOOLEAN)
                .build();

        builderClassBuilder.addMethod(validateAndPrepareMethod);

        MethodSpec buildMethod = MethodSpec.methodBuilder("build")
                .addModifiers(Modifier.PUBLIC)
                .beginControlFlow("if ($L())", validateAndPrepareMethod.name())
                .addStatement("return new $L($L)", implClass.name(), abstractMethods.stream().map(ExecutableElement::getSimpleName).map(Object::toString).map(s -> "this."+s).collect(Collectors.joining(", ")))
                .nextControlFlow("else")
                .addStatement("throw new $T()", IllegalStateException.class)
                .endControlFlow()
                .returns(ClassName.get(packageName, className))
                .build();

        builderClassBuilder.addMethod(buildMethod);
        builderClassBuilder.addMethod(builderClassConstructorBuilder.build());
        builderClassBuilder.addMethod(builderClassCopyConstructorBuilder.build());

        JavaFile javaFile = JavaFile.builder(packageName, builderClassBuilder.build()).build();
        javaFile.writeTo(this.processingEnv.getFiler());
    }

    private List<ExecutableElement> collectAbstractMethods(TypeElement typeElement) {
        Set<String> seenSignatures = new HashSet<>();
        List<ExecutableElement> methods = new ArrayList<>();

        collectAbstractMethodsRecursively(typeElement, seenSignatures, methods);
        return methods;
    }

    private void collectAbstractMethodsRecursively(TypeElement typeElement, Set<String> seenSignatures, List<ExecutableElement> methods) {
        for (Element e : typeElement.getEnclosedElements()) {
            if (e.getKind() == ElementKind.METHOD && e instanceof ExecutableElement method) {
                String signature = this.formMethodSignature(method);

                if (method.getModifiers().contains(Modifier.ABSTRACT) && !seenSignatures.contains(signature)) {
                    seenSignatures.add(signature);
                    methods.add(method);
                } else if (!method.getModifiers().contains(Modifier.ABSTRACT)) {
                    seenSignatures.add(signature);
                }
            }
        }

        TypeMirror superclass = typeElement.getSuperclass();
        if (superclass != null && !superclass.toString().equals("java.lang.Object")) {
            Element superElement = processingEnv.getTypeUtils().asElement(superclass);
            if (superElement instanceof TypeElement superTypeElement) {
                this.collectAbstractMethodsRecursively(superTypeElement, seenSignatures, methods);
            }
        }

        for (TypeMirror iface : typeElement.getInterfaces()) {
            Element ifaceElement = processingEnv.getTypeUtils().asElement(iface);
            if (ifaceElement instanceof TypeElement ifaceTypeElement) {
                this.collectAbstractMethodsRecursively(ifaceTypeElement, seenSignatures, methods);
            }
        }
    }

    private String formMethodSignature(ExecutableElement method) {
        return  method.getSimpleName() +
                "(" +
                String.join(",", method.getParameters().stream().map(VariableElement::asType).map(Object::toString).toList()) +
                ")";
    }

    private TypeSpec generateImplClass(TypeElement typeElement) {
        String packageName = this.elementUtils.getPackageOf(typeElement).getQualifiedName().toString();
        String className = typeElement.getSimpleName().toString();
        String generatedClassName = className + "_Impl";

        List<ExecutableElement> abstractMethods = this.collectAbstractMethods(typeElement);

        TypeSpec.Builder classBuilder = TypeSpec.classBuilder(generatedClassName)
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC, Modifier.FINAL);

        if (typeElement.getKind() == ElementKind.CLASS) {
            classBuilder.superclass(TypeName.get(typeElement.asType()));
        } else if (typeElement.getKind() == ElementKind.INTERFACE) {
            classBuilder.addSuperinterface(TypeName.get(typeElement.asType()));
        } else {
            throw new IllegalArgumentException("Unsupported element type for @Builder: " + typeElement.getKind());
        }

        for (ExecutableElement method : abstractMethods) {
            String fieldName = method.getSimpleName().toString();
            TypeName fieldType = TypeName.get(method.getReturnType());

            FieldSpec field = FieldSpec.builder(fieldType, fieldName, Modifier.PRIVATE, Modifier.FINAL).build();
            classBuilder.addField(field);
        }

        MethodSpec.Builder constructorBuilder = MethodSpec.constructorBuilder()
                .addModifiers(Modifier.PUBLIC);

        for (ExecutableElement method : abstractMethods) {
            String paramName = method.getSimpleName().toString();
            TypeName paramType = TypeName.get(method.getReturnType());

            constructorBuilder.addParameter(paramType, paramName);
            constructorBuilder.addStatement("this.$N = $N", paramName, paramName);
        }

        classBuilder.addMethod(constructorBuilder.build());

        for (ExecutableElement method : abstractMethods) {
            String methodName = method.getSimpleName().toString();
            TypeName returnType = TypeName.get(method.getReturnType());

            MethodSpec methodSpec = MethodSpec.methodBuilder(methodName)
                    .addAnnotation(Override.class)
                    .addModifiers(Modifier.PUBLIC)
                    .returns(returnType)
                    .addStatement("return $N", methodName)
                    .build();

            classBuilder.addMethod(methodSpec);
        }

        return classBuilder.build();
    }

}
