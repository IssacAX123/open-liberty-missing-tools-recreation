# Open Liberty MCP Tool Discovery Bug Replication Demo (Issue #35726)

This repository contains an Open Liberty demo application created to replicate GitHub issue **[#35726](https://github.com/OpenLiberty/open-liberty/issues/35726)**:
> *"Zero-argument `@Tool` method causes subsequent tools to be silently dropped from `tools/list`"*

---

## 1. Issue Summary

The reported issue claimed that in Open Liberty `26.0.0.9` with `mcp-1.0`:
- A zero-argument `@Tool` method declared before other `@Tool` methods causes all subsequent tools in the same CDI bean to be silently dropped from `tools/list`.
- The dropped tools were still invocable by name using `tools/call`.
- The user proposed a workaround of declaring zero-argument tools at the end of the class, but noted this breaks when multiple zero-argument tools exist in the same class.

---

## 2. Test Scenarios Implemented

Four `@ApplicationScoped` CDI beans are implemented in `src/main/java/com/demo/tools/`:

1. **`BuggyOrderTools.java` (Basic Bug Scenario)**
   - `tool_a()` (zero arguments, declared first)
   - `tool_b(@ToolArg String input)` (parameterized, declared second)
   - *Goal:* Test if `tool_b` is omitted from `tools/list`.

2. **`WorkaroundTools.java` (Workaround Scenario)**
   - `tool_param1(@ToolArg String message)`
   - `tool_param2(@ToolArg int count)`
   - `tool_zero_last()` (zero arguments, declared last)
   - *Goal:* Test if placing zero-arg method last allows all tools to be discovered.

3. **`BreakingWorkaroundTools.java` (Breaking Workaround with Multiple Zero-Arg Tools)**
   - `tool_initial(@ToolArg String name)` (parameterized)
   - `tool_zero_first()` (first zero-arg)
   - `tool_middle(@ToolArg String value)` (parameterized between zero-arg tools)
   - `tool_zero_second()` (second zero-arg at the end)
   - *Goal:* Test if tools declared after `tool_zero_first` are silently dropped.

4. **`ParameterizedFirstTwoEmptyTools.java` (Parameterized First Followed by Two Empty Tools)**
   - `tool_param(@ToolArg String msg)` (parameterized first)
   - `tool_zero_1()` (first zero-arg)
   - `tool_zero_2()` (second zero-arg)
   - *Goal:* Test if `tool_zero_2` is dropped because it follows another zero-argument tool.

---

## 3. Environment & Configuration

- **Open Liberty Version:** `26.0.0.9` (`wlp-1.0.117.cl260920260824-0859`)
- **Java Runtime:**
  ```text
  openjdk version "21.0.12.1" 2026-08-18 LTS
  OpenJDK Runtime Environment Temurin-21.0.12.1+1 (build 21.0.12.1+1-LTS)
  OpenJDK 64-Bit Server VM Temurin-21.0.12.1+1 (build 21.0.12.1+1-LTS, mixed mode, sharing)
  ```
- **Enabled Features (`server.xml`):**
  - `restfulWS-4.0`
  - `mcp-1.0`
- **Dependencies (`pom.xml`):**
  - `jakarta.platform:jakarta.jakartaee-api:10.0.0` (provided)
  - `org.eclipse.microprofile:microprofile:7.1` (provided)
  - `org.mcpjava:mcp-server-api:1.0.0` (provided)

---

## 5. Replication Results

| Class | Declared Methods | Expected Under Reported Bug | Actual Result in `tools/list` | Status |
|---|---|---|---|---|
| `BuggyOrderTools` | `tool_a`, `tool_b` | `tool_b` omitted | Both `tool_a` & `tool_b` present | **Cannot Reproduce** |
| `WorkaroundTools` | `tool_param1`, `tool_param2`, `tool_zero_last` | All present | All 3 tools present | **Pass** |
| `BreakingWorkaroundTools` | `tool_initial`, `tool_zero_first`, `tool_middle`, `tool_zero_second` | `tool_middle` & `tool_zero_second` omitted | All 4 tools present | **Cannot Reproduce** |
| `ParameterizedFirstTwoEmptyTools` | `tool_param`, `tool_zero_1`, `tool_zero_2` | `tool_zero_2` omitted | All 3 tools present | **Cannot Reproduce** |

### Conclusion
**All 12 of 12 tools across all 4 CDI beans are discovered and listed in `tools/list`.** The reported issue could not be reproduced on Open Liberty `26.0.0.9` with Java 21 Temurin. IBM Semeru Runtimes (Java 17 and Java 21) were also trialed, as well as Weld 5 and 6, yielding the same non-reproducible outcome.
