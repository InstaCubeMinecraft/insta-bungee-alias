package at.instacube.bungeealias.config.model;

import lombok.Data;

@Data
public class ExecutionConfig {
    private boolean executeAsConsole = false;
    private boolean executeCommand = false;
    private boolean showChatMessage = false;
    private String command;
    private String chatMessage;
}
