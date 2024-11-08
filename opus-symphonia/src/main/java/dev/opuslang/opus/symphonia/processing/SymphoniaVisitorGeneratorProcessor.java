package dev.opuslang.opus.symphonia.processing;

import com.google.auto.service.AutoService;
import com.palantir.javapoet.*;
import dev.opuslang.opus.symphonia.annotation.Symphonia;

import javax.annotation.processing.*;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.PackageElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;
import java.io.IOException;
import java.util.*;

@AutoService(Processor.class)
@SupportedAnnotationTypes({
        "dev.opuslang.opus.symphonia.annotation.Symphonia.Visitor.Package",
        "dev.opuslang.opus.symphonia.annotation.Symphonia.Visitor.Visitable"
})
@SupportedSourceVersion(SourceVersion.RELEASE_21)
@SuppressWarnings("unused")
public class SymphoniaVisitorGeneratorProcessor  extends AbstractProcessor {

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
        if(!annotations.isEmpty()) this.messager.printNote("Generating Symphonia.Visitors...");

        Map<PackageElement, List<TypeElement>> classesByPackage = new HashMap<>();
        for (Element element : roundEnv.getElementsAnnotatedWith(Symphonia.Visitor.Package.class)) {
            if(element instanceof PackageElement packageElement){
                classesByPackage.putIfAbsent(packageElement, new ArrayList<>());
            }
        }

        for (Element element : roundEnv.getElementsAnnotatedWith(Symphonia.Visitor.Visitable.class)) {
            if(element instanceof TypeElement typeElement){
                PackageElement packageElement = this.elementUtils.getPackageOf(typeElement);

                if (classesByPackage.containsKey(packageElement)) {
                    classesByPackage.get(packageElement).add(typeElement);
                }
            }
        }

        for(Map.Entry<PackageElement, List<TypeElement>> entry : classesByPackage.entrySet()){
            try {
                entry.getValue().sort(Comparator.comparingInt(t -> t.getAnnotation(Symphonia.Visitor.Visitable.class).order()));
                this.generateVisitorClass(entry.getKey(), entry.getValue());
            } catch (IOException e) {
                this.messager.printError("Failed to generate Symphonia.Visitor class: " + e.getMessage());
            }
        }
        return true;
    }

    private void generateVisitorClass(PackageElement packageElement, List<TypeElement> visitableElements) throws IOException {
        Symphonia.Visitor.Package symphoniaVisitorPackageAnnotation = packageElement.getAnnotation(Symphonia.Visitor.Package.class);

        TypeSpec.Builder visitableBuilder = TypeSpec.interfaceBuilder(symphoniaVisitorPackageAnnotation.outputInterface())
                .addModifiers(Modifier.PUBLIC, Modifier.SEALED);

        ClassName visitableClassname = ClassName.get(packageElement.getQualifiedName().toString(), symphoniaVisitorPackageAnnotation.outputInterface());

        TypeVariableName t = TypeVariableName.get("T");

        TypeSpec.Builder visitorBuilder = TypeSpec.classBuilder(symphoniaVisitorPackageAnnotation.outputClass())
                .addModifiers(Modifier.PUBLIC, Modifier.ABSTRACT)
                .addTypeVariable(t);

        // Add permitted subclasses
        ClassName[] permittedSubclasses = visitableElements.stream()
                .map(ClassName::get)
                .toArray(ClassName[]::new);
        visitableBuilder.addPermittedSubclasses(List.of(permittedSubclasses));

        // Generate generic visit method:
        MethodSpec defaultVisitMethod = MethodSpec.methodBuilder("visit_default")
                .addModifiers(Modifier.PROTECTED, Modifier.ABSTRACT)
                .addParameter(visitableClassname, "visitable")
                .returns(t)
                .build();
        visitorBuilder.addMethod(defaultVisitMethod);

        for (TypeElement visitableClass : visitableElements) {
            String methodName = "visit_" + visitableClass.getSimpleName();
            MethodSpec abstractVisitMethod = MethodSpec.methodBuilder(methodName)
                    .addModifiers(Modifier.PROTECTED)
                    .addParameter(TypeName.get(visitableClass.asType()), "visitable")
                    .addStatement("return $L(visitable)", defaultVisitMethod.name())
                    .returns(t)
                    .build();
            visitorBuilder.addMethod(abstractVisitMethod);
        }

        CodeBlock.Builder switchBuilder = CodeBlock.builder().beginControlFlow("switch (visitable)");

        for (TypeElement visitableClass : visitableElements) {
            String methodName = "visit_" + visitableClass.getSimpleName();
            switchBuilder.add("case $T v -> $L(v);\n",
                    TypeName.get(visitableClass.asType()), methodName);
        }

        switchBuilder.endControlFlow();

        MethodSpec visitMethod = MethodSpec.methodBuilder("visit")
                .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                .addParameter(visitableClassname, "visitable")
                .addStatement("return " + switchBuilder.build())
                .returns(t)
                .build();

        visitorBuilder.addMethod(visitMethod);

        JavaFile visitorFile = JavaFile.builder(packageElement.getQualifiedName().toString(), visitorBuilder.build())
                .build();

        JavaFile visitableFile = JavaFile.builder(packageElement.getQualifiedName().toString(), visitableBuilder.build())
                .build();

        visitorFile.writeTo(filer);
        visitableFile.writeTo(filer);
    }
}
