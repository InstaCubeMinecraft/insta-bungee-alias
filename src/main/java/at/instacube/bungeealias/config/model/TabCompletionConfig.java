package at.instacube.bungeealias.config.model;

import lombok.Data;

import java.util.List;

@Data
public class TabCompletionConfig {
    private String commandStartsWith;
    private List<String> completions;
}
