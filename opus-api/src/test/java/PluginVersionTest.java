import dev.opuslang.opus.api.plugin.PluginVersion;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public final class PluginVersionTest {

    @Test
    public void givenNoFragments_whenValueOf_thenThrow(){
        final String version = "";
        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> PluginVersion.valueOf(version),
                "Invalid version string: ''."
        );
    }

    @Test
    public void givenPartialFragments_whenValueOf_thenReturnPluginVersion(){
        Assertions.assertEquals(
                new PluginVersion(1, 0, 0),
                PluginVersion.valueOf("1")
        );

        Assertions.assertEquals(
                new PluginVersion(1, 2, 0),
                PluginVersion.valueOf("1.2")
        );

        Assertions.assertEquals(
                new PluginVersion(1, 2, 3),
                PluginVersion.valueOf("1.2.3")
        );
    }

    @Test
    public void givenAllFragments_whenValueOf_thenReturnPluginVersion(){
        Assertions.assertEquals(
                new PluginVersion(1, 2, 3),
                PluginVersion.valueOf("1.2.3")
        );
    }

    @Test
    public void givenTrailingDot_whenValueOf_thenThrow(){
        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> PluginVersion.valueOf("1."),
                "Invalid version string: '1.'."
        );

        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> PluginVersion.valueOf("1.2."),
                "Invalid version string: '1.2.'."
        );

        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> PluginVersion.valueOf("1.2.3."),
                "Invalid version string: '1.2.3.'."
        );
    }


    @Test
    public void givenIllegalFragment_whenValueOf_thenThrow(){
        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> PluginVersion.valueOf("a.b.c"),
                "Invalid version string: 'a.b.c'."
        );

        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> PluginVersion.valueOf("1.b.c"),
                "Invalid version string: '1.b.c'."
        );

        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> PluginVersion.valueOf("1.2.c"),
                "Invalid version string: '1.2.c'."
        );

        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> PluginVersion.valueOf("1a.b.c"),
                "Invalid version string: '1a.b.c'."
        );
    }

    @Test
    public void givenNegativeFragment_whenConstructor_thenThrow(){
        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new PluginVersion(-1, -1, -1),
                "Invalid version: all values must be positive."
        );

        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new PluginVersion(1, -1, -1),
                "Invalid version: all values must be positive."
        );

        Assertions.assertThrowsExactly(
                IllegalArgumentException.class,
                () -> new PluginVersion(1, 2, -1),
                "Invalid version: all values must be positive."
        );
    }

    @Test
    public void givenTwoVersions_whenCompareTo_thenReturnDelta(){
        Assertions.assertEquals(
                0,
                new PluginVersion(1, 2, 3).compareTo(new PluginVersion(1, 2, 3))
        );

        Assertions.assertEquals(
                -1,
                new PluginVersion(1, 2, 3).compareTo(new PluginVersion(1, 2, 4))
        );

        Assertions.assertEquals(
                1,
                new PluginVersion(1, 3, 3).compareTo(new PluginVersion(1, 2, 3))
        );

        //TODO: FINISH!!!!!!!!!

    }


}
