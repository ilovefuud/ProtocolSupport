package protocolsupport.buildprocessor.processors;

import protocolsupport.buildprocessor.annotations.NeedsNoArgConstructor;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.annotation.processing.SupportedSourceVersion;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.tools.Diagnostic.Kind;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Set;

@SupportedAnnotationTypes({"*"})
@SupportedSourceVersion(SourceVersion.RELEASE_8)
public class HasNoArgConstructorProcessor extends AbstractProcessor {
    public HasNoArgConstructorProcessor() {
    }

    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        Iterator var4 = roundEnv.getRootElements().iterator();

        while(var4.hasNext()) {
            Element type = (Element)var4.next();
            if (type.getKind() == ElementKind.CLASS && !type.getModifiers().contains(Modifier.ABSTRACT) && this.hasNeedsNoArgsConstrAnnotationInHierarcy((TypeElement)type)) {
                this.checkForNoArgConstructor(type);
            }
        }

        return true;
    }

    private boolean hasNeedsNoArgsConstrAnnotationInHierarcy(TypeElement type) {
        ArrayList hierarcy = new ArrayList();

        while(true) {
            hierarcy.add(type.asType());
            hierarcy.addAll(type.getInterfaces());
            TypeMirror hierarcyElement = type.getSuperclass();
            if (hierarcyElement.getKind() == TypeKind.NONE || !(hierarcyElement instanceof DeclaredType)) {
                Iterator var4 = hierarcy.iterator();

                while(var4.hasNext()) {
                    hierarcyElement = (TypeMirror)var4.next();
                    if (hierarcyElement instanceof DeclaredType) {
                        DeclaredType decltype = (DeclaredType)hierarcyElement;
                        if (decltype.asElement().getAnnotation(NeedsNoArgConstructor.class) != null) {
                            return true;
                        }
                    }
                }

                return false;
            }

            type = (TypeElement)((DeclaredType)hierarcyElement).asElement();
        }
    }

    private void checkForNoArgConstructor(Element type) {
        Iterator var3 = type.getEnclosedElements().iterator();

        Element enclosed;
        do {
            if (!var3.hasNext()) {
                this.processingEnv.getMessager().printMessage(Kind.ERROR, "Missing a no-arg constructor", type);
                return;
            }

            enclosed = (Element)var3.next();
        } while(enclosed.getKind() != ElementKind.CONSTRUCTOR || !((ExecutableElement)enclosed).getParameters().isEmpty());

    }
}