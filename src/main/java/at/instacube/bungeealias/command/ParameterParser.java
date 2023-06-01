package at.instacube.bungeealias.command;

import at.instacube.bungeealias.Main;
import net.md_5.bungee.api.connection.ProxiedPlayer;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ParameterParser {

    public static List<String> parseParameters(List<String> baseText, String serverName, String playerName) {
        return baseText.stream()
                .map(text -> text.replace("%serverName%", serverName))
                .map(text -> text.replace("%playerName%", playerName))
                .flatMap(text -> {
                    if (text.contains("%localPlayers%")) {
                        List<String> texts = new ArrayList<>();
                        Iterator<String> iter = Arrays.asList(text.split("%localPlayers%")).iterator();
                        while (iter.hasNext()) {
                            String txt = iter.next();
                            texts.add(txt);
                            if (iter.hasNext()) {
                                texts.addAll(getLocalPlayers(serverName));
                            }
                        }
                        return texts.stream();
                    } else {
                        return Stream.of(text);
                    }
                })
                .flatMap(text -> {
                    if (text.contains("%players%")) {
                        List<String> texts = new ArrayList<>();
                        Iterator<String> iter = Arrays.asList(text.split("%players%")).iterator();
                        while (iter.hasNext()) {
                            String txt = iter.next();
                            texts.add(txt);
                            if (iter.hasNext()) {
                                texts.addAll(getAllPlayers());
                            }
                        }
                        return texts.stream();
                    } else {
                        return Stream.of(text);
                    }
                })
                .collect(Collectors.toList());
    }

    private static List<String> getLocalPlayers(String serverName) {
        return Main.getPlugin().getProxy()
                .getServerInfo(serverName)
                .getPlayers().stream()
                .map(ProxiedPlayer::getName)
                .collect(Collectors.toList());
    }

    private static List<String> getAllPlayers() {
        return Main.getPlugin().getProxy()
                .getPlayers().stream()
                .map(ProxiedPlayer::getName)
                .collect(Collectors.toList());
    }
}
