package com.demo.tools;

import jakarta.enterprise.context.ApplicationScoped;
import org.mcpjava.server.tools.Tool;
import org.mcpjava.server.tools.ToolArg;

/**
 * Scenario 4: Parameterized tool first followed by multiple (two) empty/zero-argument tools.
 *
 * In this scenario:
 * - A parameterized tool is declared first.
 * - Followed by first zero-arg tool.
 * - Followed by second zero-arg tool.
 *
 * Demonstrates how attempting the workaround with multiple zero-argument tools still breaks:
 * - tools/list returns tool_param and tool_zero_1.
 * - tools/list silently drops tool_zero_2 (because it follows tool_zero_1).
 * - All tools are invocable via tools/call.
 */
@ApplicationScoped
public class ParameterizedFirstTwoEmptyTools {

    @Tool(description = "Parameterized tool declared first")
    public String tool_param(@ToolArg(name = "msg") String msg) {
        return "param: " + msg;
    }

    @Tool(description = "First zero-argument tool")
    public String tool_zero_1() {
        return "zero-1-result";
    }

    @Tool(description = "Second zero-argument tool (silently dropped due to preceding zero-arg tool)")
    public String tool_zero_2() {
        return "zero-2-result";
    }
}
