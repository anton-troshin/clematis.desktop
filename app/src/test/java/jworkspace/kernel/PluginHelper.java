package jworkspace.kernel;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.jar.JarEntry;
import java.util.jar.JarOutputStream;
import java.util.jar.Manifest;

import com.hyperrealm.kiwi.io.StreamUtils;
import com.hyperrealm.kiwi.util.plugin.Plugin;
import com.hyperrealm.kiwi.util.plugin.PluginDTO;


/**
 * @author Anton Troshin
 */
class PluginHelper {

    static final String PLUGIN_JAR = "plugin.jar";
    static final String PLUGIN_JAR_2 = "plugin2.jar";

    static final String TEST_PLUGIN_CLASS_2 = "TestPlugin2.class";

    private static final String ANOTHER_PLUGIN_JAR = "another_plugin.jar";
    private static final String TEST_PLUGIN_CLASS_PACKAGE = "jworkspace.kernel.";
    private static final String TEST_PLUGIN_CLASS = "TestPlugin.class";
    private static final String TEST_PLUGIN = "jworkspace.kernel.TestPlugin";

    private static final String TEST_PLUGIN_NAME = "Test plugin";
    private static final String TEST_PLUGIN_DESCRIPTION = "Test plugin description";
    private static final String TEST_PLUGIN_VERSION = "1.0.0";
    private static final String TEST_PLUGIN_HELP_URL = "http://test.com";
    private static final String TEST_PLUGIN_ICON = "/dummy/path.png";

    private static final String TEST_PLUGIN_2 = "jworkspace.kernel.TestPlugin2";
    private static final String TEST_PLUGIN_NAME_2 = "Test plugin 2";
    private static final String TEST_PLUGIN_DESCRIPTION_2 = "Test plugin 2 description";
    private static final String TEST_PLUGIN_VERSION_2 = "2.0.0";
    private static final String TEST_PLUGIN_ICON_2 = "/dummy/path2.png";

    private PluginHelper() {
    }

    static void writeJarFile(File testPluginClassPath, String testPluginClass, Manifest manifest, String pluginJar)
            throws IOException {

        writeJarFile(testPluginClassPath, testPluginClass, manifest, testPluginClassPath, pluginJar);
    }

    private static void writeJarFile(File testPluginClassPath, String testPluginClass, Manifest manifest,
                                     File jarPath, String pluginJar)
            throws IOException {

        Files.createDirectories(jarPath.toPath());

        try (InputStream is = Files.newInputStream(Paths.get(testPluginClassPath.getAbsolutePath(), testPluginClass));
             OutputStream os = new FileOutputStream(getPluginFile(jarPath, pluginJar));
             JarOutputStream target = new JarOutputStream(os, manifest)) {

            JarEntry entry = new JarEntry(TEST_PLUGIN_CLASS_PACKAGE + testPluginClass);
            target.putNextEntry(entry);
            target.write(StreamUtils.readStreamToByteArray(is));
            target.closeEntry();
        }
    }

    static File getPluginFile(File folder, String file) {
        return new File(folder, file);
    }

    private static Manifest getManifest() {

        PluginDTO plugin = new PluginDTO(TEST_PLUGIN_CLASS_PACKAGE + TEST_PLUGIN_CLASS,
            TEST_PLUGIN_NAME,
            PluginDTO.PLUGIN_TYPE_ANY,
            TEST_PLUGIN_DESCRIPTION,
            TEST_PLUGIN_ICON,
            TEST_PLUGIN_VERSION,
            TEST_PLUGIN_HELP_URL);

        return PluginDTO.getManifest(plugin);
    }

    static Manifest getManifest2() {

        PluginDTO plugin = new PluginDTO(TEST_PLUGIN_CLASS_PACKAGE + TEST_PLUGIN_CLASS_2,
            TEST_PLUGIN_NAME_2,
            PluginDTO.PLUGIN_TYPE_ANY,
            TEST_PLUGIN_DESCRIPTION_2,
            TEST_PLUGIN_ICON_2,
            TEST_PLUGIN_VERSION_2,
            TEST_PLUGIN_HELP_URL);

        return PluginDTO.getManifest(plugin);
    }

    static void assertPluginEqualsManifest(Plugin testPlugin) {

        assert testPlugin.getName().equals(TEST_PLUGIN_NAME);
        assert testPlugin.getDescription().equals(TEST_PLUGIN_DESCRIPTION);
        assert testPlugin.getClassName().equals(TEST_PLUGIN);
        assert testPlugin.getVersion().equals(TEST_PLUGIN_VERSION);
        assert testPlugin.getHelpURL().toString().equals(TEST_PLUGIN_HELP_URL);
        assert testPlugin.getType().equals(PluginDTO.PLUGIN_TYPE_ANY);
    }

    static void preparePlugins(File folder) throws IOException {
        preparePlugins(folder, folder);
    }

    static void preparePlugins(File testPluginClassPath, File target) throws IOException {

        File sourceFile = getPluginFile(testPluginClassPath, TEST_PLUGIN_CLASS);
        Files.write(sourceFile.toPath(),
            StreamUtils.readStreamToByteArray(PluginTests.class.getResourceAsStream(TEST_PLUGIN_CLASS)));

        File sourceFile2 = getPluginFile(testPluginClassPath, TEST_PLUGIN_CLASS_2);
        Files.write(sourceFile2.toPath(),
            StreamUtils.readStreamToByteArray(PluginTests.class.getResourceAsStream(TEST_PLUGIN_CLASS_2)));

        Manifest manifest = getManifest();
        writeJarFile(testPluginClassPath, TEST_PLUGIN_CLASS, manifest, target, PLUGIN_JAR);
        writeJarFile(testPluginClassPath, TEST_PLUGIN_CLASS, manifest, target, ANOTHER_PLUGIN_JAR);

        Manifest manifest2 = getManifest2();
        writeJarFile(testPluginClassPath, TEST_PLUGIN_CLASS_2, manifest2, target, PLUGIN_JAR_2);
    }
}