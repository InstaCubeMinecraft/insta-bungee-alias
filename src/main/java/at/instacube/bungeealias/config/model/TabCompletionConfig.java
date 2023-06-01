package at.instacube.bungeealias.config.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TabCompletionConfig {
    private String commandStartsWith;
    private List<String> completions;
}
