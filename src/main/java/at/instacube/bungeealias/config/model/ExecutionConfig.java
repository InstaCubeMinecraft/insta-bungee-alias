package at.instacube.bungeealias.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExecutionConfig {
    private boolean executeAsConsole = false;
    private boolean executeCommand = false;
    private boolean showChatMessage = false;
    private String command;
    private String chatMessage;
}
