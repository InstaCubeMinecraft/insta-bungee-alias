package at.instacube.bungeealias.config;

import at.instacube.bungeealias.Main;
import at.instacube.bungeealias.config.model.AliasConfig;
import at.instacube.bungeealias.config.model.BaseConfig;
import at.instacube.bungeealias.config.model.TabCompletionConfig;
import com.google.common.io.ByteStreams;
import net.md_5.bungee.api.plugin.Plugin;
import org.yaml.snakeyaml.TypeDescription;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;
import org.yaml.snakeyaml.representer.Representer;

import java.io.*;

public class ConfigLoader {

    private static ConfigLoader configLoader;
    private BaseConfig baseConfig;

    private ConfigLoader() {

    }

    public static ConfigLoader getConfigLoader() {
        if (configLoader == null) {
            configLoader = new ConfigLoader();
        }
        return configLoader;
    }

    public BaseConfig reloadBaseConfig() throws FileNotFoundException {
        Representer representer = new Representer();
        representer.getPropertyUtils().setSkipMissingProperties(true);

        // Define the target object type
        Constructor constructor = new Constructor(BaseConfig.class);

        Yaml yaml = new Yaml(constructor, representer);
        File configFile = loadAndCreateResource(Main.getPlugin(), "config.yml");
        BaseConfig baseConfig = yaml.load(new FileInputStream(configFile));
        this.baseConfig = baseConfig;

        return this.baseConfig;
    }

    public BaseConfig getBaseConfig() {
        if (this.baseConfig == null) {
            try {
                return reloadBaseConfig();
            } catch (FileNotFoundException e) {
                return null;
            }
        }
        return baseConfig;
    }

    private File loadAndCreateResource(Plugin plugin, String resource) {
        File folder = plugin.getDataFolder();
        if (!folder.exists())
            folder.mkdir();
        File resourceFile = new File(folder, resource);
        try {
            if (!resourceFile.exists()) {
                resourceFile.createNewFile();
                try (InputStream in = plugin.getResourceAsStream(resource);
                     OutputStream out = new FileOutputStream(resourceFile)) {
                    ByteStreams.copy(in, out);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return resourceFile;
    }
}
