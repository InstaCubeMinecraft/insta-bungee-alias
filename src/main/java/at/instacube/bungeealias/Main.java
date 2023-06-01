package at.instacube.bungeealias;

import at.instacube.bungeealias.command.BaseCommand;
import at.instacube.bungeealias.config.ConfigLoader;
import at.instacube.bungeealias.config.model.BaseConfig;
import net.md_5.bungee.api.ProxyServer;
import net.md_5.bungee.api.plugin.Plugin;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class Main extends Plugin {

    private static Plugin PLUGIN;

    private List<BaseCommand> registeredCommands = new ArrayList<>();


    @Override
    public void onEnable() {
        Main.PLUGIN = this;
        ConfigLoader configLoader = ConfigLoader.getConfigLoader();
        configLoader.getBaseConfig();


    }

    public void registerCommands() {
        for (BaseCommand command : registeredCommands) {
            // TODO Unregister
        }

        BaseConfig config = ConfigLoader.getConfigLoader().getBaseConfig();
        this.registeredCommands = Optional.ofNullable(config.getAliases()).orElse(List.of())
                .stream()
                .map(BaseCommand::new)
                .collect(Collectors.toList());

        registeredCommands.forEach(
                reg -> getPlugin().getProxy().getPluginManager().registerCommand(this, reg)
        );
    }

    public static Plugin getPlugin() {
        return Main.PLUGIN;
    }
}
