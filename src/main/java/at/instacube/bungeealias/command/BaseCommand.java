package at.instacube.bungeealias.command;

import at.instacube.bungeealias.Main;
import at.instacube.bungeealias.config.model.AliasConfig;
import at.instacube.bungeealias.config.model.ExecutionConfig;
import at.instacube.bungeealias.config.model.TabCompletionConfig;
import net.md_5.bungee.api.CommandSender;
import net.md_5.bungee.api.chat.TextComponent;
import net.md_5.bungee.api.connection.ProxiedPlayer;
import net.md_5.bungee.api.plugin.Command;
import net.md_5.bungee.api.plugin.TabExecutor;

import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class BaseCommand extends Command implements TabExecutor {

    AliasConfig aliasConfig;

    public BaseCommand(AliasConfig aliasConfig) {
        super(aliasConfig.getTargetCommand().strip(),
                aliasConfig.getPermission() == null && aliasConfig.isRequiresPermission()
                        ? "hfghgdowe"
                        : (aliasConfig.isRequiresPermission() ? aliasConfig.getPermission() : null)
        );
        this.aliasConfig = aliasConfig;
    }

    @Override
    public void execute(CommandSender commandSender, String[] strings) {
        String serverName = "CONSOLE";
        String playerName = "CONSOLE";

        if (commandSender instanceof ProxiedPlayer) {
            serverName = ((ProxiedPlayer) commandSender).getServer().getInfo().getName().toLowerCase();
            playerName = commandSender.getName();
        }

        if (aliasConfig.isRequiresPermission() && !commandSender.hasPermission(aliasConfig.getPermission())) {
            commandSender.sendMessage(new TextComponent(
                    TextComponent.fromLegacyText("§7[§e!§7] §cDu hast nicht die nötigen Rechte für diesen Befehl.")
            ));
            return;
        }

        if (!aliasConfig.isUsableOnAllServers() &&
                !getUsableServers().contains(serverName) &&
                !serverName.equalsIgnoreCase("CONSOLE")
        ) {
            return;
        }

        for (ExecutionConfig executionConfig : Optional.ofNullable(aliasConfig.getExecutions()).orElse(List.of())) {
            String command = Optional.ofNullable(executionConfig.getCommand()).orElse("");
            String chatMessage = Optional.ofNullable(executionConfig.getChatMessage()).orElse("");
            chatMessage.replace("&", "§");

            List<String> commands = ParameterParser.parseParameters(Arrays.asList(strings), serverName, playerName);
            commands.add(0, command.strip());
            String excuteCommand = String.join(" ", commands);

            if (executionConfig.isExecuteCommand()) {
                if (executionConfig.isExecuteAsConsole()) {
                    Main.getPlugin().getProxy().getPluginManager().dispatchCommand(Main.getPlugin().getProxy().getConsole(), excuteCommand);
                } else {
                    Main.getPlugin().getProxy().getPluginManager().dispatchCommand(commandSender, excuteCommand);
                }
            }

            if (executionConfig.isShowChatMessage()) {
                List<String> texts = ParameterParser.parseParameters(List.of(chatMessage), serverName, playerName);
                String text = String.join(" ", texts);
                commandSender.sendMessage(new TextComponent(
                        TextComponent.fromLegacyText(text)
                ));
            }
        }
    }

    @Override
    public Iterable<String> onTabComplete(CommandSender commandSender, String[] strings) {
        String command = aliasConfig.getTargetCommand().strip() + " " + String.join(" ", strings);
        String serverName = "CONSOLE";
        String playerName = "CONSOLE";

        if (commandSender instanceof ProxiedPlayer) {
            serverName = ((ProxiedPlayer) commandSender).getServer().getInfo().getName().toLowerCase();
            playerName = commandSender.getName();
        }

        List<TabCompletionConfig> tabCompletionConfigs = Optional.ofNullable(aliasConfig.getTabCompletions())
                .orElse(List.of()).stream()
                .filter(comp -> command.startsWith(comp.getCommandStartsWith().strip()))
                .sorted(Comparator.comparingInt((TabCompletionConfig a) -> a.getCommandStartsWith().length()).reversed())
                .collect(Collectors.toList());

        if (tabCompletionConfigs.isEmpty()) {
            return List.of();
        }

        TabCompletionConfig tabCompletionConfig = tabCompletionConfigs.get(0);
        return ParameterParser.parseParameters(tabCompletionConfig.getCompletions(), serverName, playerName);
    }

    private List<String> getUsableServers() {
        return Optional.ofNullable(aliasConfig.getUsableOnServers())
                .map(List::stream)
                .orElse(Stream.empty())
                .map(String::toLowerCase)
                .collect(Collectors.toList());
    }
}
