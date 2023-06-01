package at.instacube.bungeealias.config.model;

import lombok.Data;

import java.util.List;

@Data
public class AliasConfig {

    private String targetCommand;
    private boolean requiresPermission;
    private String permission;
    private boolean usableOnAllServers;
    private List<String> usableOnServers;

    private List<TabCompletionConfig> tabCompletions;
    private List<ExecutionConfig> executions;

}
