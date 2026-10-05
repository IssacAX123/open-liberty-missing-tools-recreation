package com.demo.tools;

import jakarta.enterprise.context.ApplicationScoped;
import org.mcpjava.server.tools.Tool;
import org.mcpjava.server.tools.ToolArg;

/**
 * Scenario 3: Breaking Workaround (Multiple Zero-Argument Tools).
 *
 * In this scenario, there are multiple zero-arg methods in the same bean.
 * Even if one zero-arg tool is at the end, any tool (parameterized or zero-arg)
 * declared after the FIRST zero-arg method will be silently dropped from tools/list.
 *
 * Expected Liberty behavior in 26.0.0.9:
 * - tools/list returns tool_initial, tool_zero_first.
 * - tools/list silently drops tool_middle and tool_zero_second.
 * - All tools remain invocable via tools/call.
 */
@ApplicationScoped
public class BreakingWorkaroundTools {

    @Tool(description = "A parameterized tool declared before any zero-arg method")
    public String tool_initial(@ToolArg(name = "name") String name) {
        return "hello " + name;
    }

    @Tool(description = "First zero-argument tool")
    public String tool_zero_first() {
        return "zero-1";
    }

    @Tool(description = "Parameterized tool declared between two zero-arg tools (dropped)")
    public String tool_middle(@ToolArg(name = "value") String value) {
        return "middle: " + value;
    }

    @Tool(description = "Second zero-argument tool declared at the end (dropped)")
    public String tool_zero_second() {
        return "zero-2";
    }
}
