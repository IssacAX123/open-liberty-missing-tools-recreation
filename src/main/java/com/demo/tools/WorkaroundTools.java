package com.demo.tools;

import jakarta.enterprise.context.ApplicationScoped;
import org.mcpjava.server.tools.Tool;
import org.mcpjava.server.tools.ToolArg;

/**
 * Scenario 2: Successful Workaround (Zero-arg tool declared LAST).
 *
 * In this scenario, all parameterized tools are declared before the zero-arg tool.
 * Expected Liberty behavior in 26.0.0.9:
 * - tools/list correctly discovers both tool_param1, tool_param2, and the trailing tool_zero_last.
 */
@ApplicationScoped
public class WorkaroundTools {

    @Tool(description = "A parameterized tool declared first")
    public String tool_param1(@ToolArg(name = "message") String message) {
        return "param1: " + message;
    }

    @Tool(description = "Another parameterized tool")
    public String tool_param2(@ToolArg(name = "count") int count) {
        return "param2: " + count;
    }

    @Tool(description = "A zero-argument tool declared at the very end of the class (workaround)")
    public String tool_zero_last() {
        return "zero-last-result";
    }
}
