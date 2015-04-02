package popsuite.xtend.annotation

import java.io.Serializable
import org.eclipse.xtend.core.XtendInjectorSingleton
import org.eclipse.xtend.core.compiler.batch.XtendCompilerTester
import org.eclipse.xtend.core.xtend.XtendFile
import org.eclipse.xtext.junit4.util.ParseHelper
import org.eclipse.xtext.junit4.validation.ValidationTestHelper
import org.junit.Test

import static org.hamcrest.MatcherAssert.assertThat
import static org.hamcrest.Matchers.*

import static extension java.lang.reflect.Modifier.*

class DefaultSerializableTest {
    
    static val SERIAL_VERSION_UID_LABEL = "serialVersionUID"
    static val SERIAL_VERSION_UID_VALUE = 1L

    extension XtendCompilerTester = XtendCompilerTester::newXtendCompilerTester(
        DefaultSerializable
    )
    extension ParseHelper<XtendFile> = XtendInjectorSingleton.INJECTOR.getInstance(ParseHelper)
    extension ValidationTestHelper = XtendInjectorSingleton.INJECTOR.getInstance(ValidationTestHelper)
    
    @Test
    def void smokeTest() {
        '''
        package foo
        import popsuite.xtend.annotation.DefaultSerializable
        @DefaultSerializable
        class Foo {
        }
        '''.parse.assertNoErrors
       
    }

    @Test
    def void test() {
        '''
        package foo
        import popsuite.xtend.annotation.DefaultSerializable
        @DefaultSerializable
        class Foo implements {
        }
        '''.compile [
            assertThat(compiledClass.interfaces, hasItemInArray(isA(typeof(Serializable))))
            assertThat(compiledClass.annotations, emptyArray)
            val serialVersionUIDField = compiledClass.getDeclaredField(SERIAL_VERSION_UID_LABEL)
            assertThat(serialVersionUIDField.type, equalTo(Long.TYPE))
            assertThat(serialVersionUIDField.modifiers.private, is(true))
            assertThat(serialVersionUIDField.modifiers.static, is(true))
            assertThat(serialVersionUIDField.modifiers.final, is(true))
            
            serialVersionUIDField.setAccessible(true)
            val value = serialVersionUIDField.get(compiledClass.newInstance)
            assertThat(value, equalTo(SERIAL_VERSION_UID_VALUE))
        ]
    }

}