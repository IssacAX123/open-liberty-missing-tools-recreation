package com.demo.tools;

import jakarta.enterprise.context.ApplicationScoped;
import org.mcpjava.server.tools.Tool;
import org.mcpjava.server.tools.ToolArg;

/**
 * Scenario 1: Basic Bug Replication (Zero-arg tool declared first).
 *
 * In this scenario, tool_a (zero arguments) is declared before tool_b (parameterized).
 * Expected Liberty behavior in 26.0.0.9:
 * - tools/list returns tool_a, but silently drops tool_b.
 * - tools/call can still invoke tool_b by name successfully.
 */
@ApplicationScoped
public class BuggyOrderTools {

    @Tool(description = "A zero-argument tool declared first")
    public String tool_a() {
        return "result-a";
    }

    @Tool(description = "A tool with one parameter declared after a zero-arg tool")
    public String tool_b(@ToolArg(name = "input") String input) {
        return "result-b: " + input;
    }
}
