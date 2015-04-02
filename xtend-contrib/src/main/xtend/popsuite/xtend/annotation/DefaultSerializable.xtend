package popsuite.xtend.annotation

import java.lang.annotation.ElementType
import java.lang.annotation.Target
import org.eclipse.xtend.lib.macro.AbstractClassProcessor
import org.eclipse.xtend.lib.macro.Active
import org.eclipse.xtend.lib.macro.TransformationContext
import org.eclipse.xtend.lib.macro.declaration.MutableClassDeclaration
import java.io.Serializable

@Target(ElementType.TYPE)
@Active(DefaultSerialVersionUIDProcessor)
annotation DefaultSerializable {
}

class DefaultSerialVersionUIDProcessor extends AbstractClassProcessor {

    static val SERIAL_VERSION_UID_LABEL = "serialVersionUID"
    static val SERIAL_VERSION_UID_EXPR = "1L"

    override doTransform(MutableClassDeclaration classType, extension TransformationContext context) {
        val defaultSerializableAnnotation = classType.annotations.findFirst[
            annotationTypeDeclaration == DefaultSerializable.newTypeReference.type]
        classType.removeAnnotation(defaultSerializableAnnotation)
        classType.addSerializableInterface(context)
        classType.addSerialVersionUID(context)
    }
    
    private def static addSerializableInterface(MutableClassDeclaration classType,
        extension TransformationContext context) {
        val serializableType = newTypeReference(typeof(Serializable))
        
        if (serializableType.type.isAssignableFrom(classType)) return;
        
        classType.implementedInterfaces = classType.implementedInterfaces + #[serializableType]
    }

    /*
     *  Adds a <code>private static final long serialVersionUID = 1L;</code> field
     */
    private def static addSerialVersionUID(MutableClassDeclaration classType,
        extension TransformationContext context) {
        
        if(classType.findDeclaredField(SERIAL_VERSION_UID_LABEL) != null) return;
        
        classType.addField(SERIAL_VERSION_UID_LABEL) [
            type = primitiveLong
            static = true
            final = true
            initializer = [SERIAL_VERSION_UID_EXPR]
        ]
    }
}
