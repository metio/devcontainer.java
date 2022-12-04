/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import io.soabase.recordbuilder.core.RecordBuilder;
import java.util.List;
import java.util.Map;

/**
 * Wrapper for the various ways to specify commands in a devcontainer file. Note that only one of the parameters is set
 * at most.
 *
 * @param string Goes through a shell (it needs to be parsed into command and arguments)
 * @param array  Passed to the OS for execution without going through a shell
 * @param object All lifecycle scripts have been extended to support object types to allow for parallel execution
 * @see <a href="https://containers.dev/implementors/json_reference/#formatting-string-vs-array-properties">schema
 * reference</a>
 */
@RecordBuilder
@RecordBuilder.Options(buildMethodName = "create", enableWither = false)
@JsonDeserialize(using = CommandDeserializer.class)
public record Command(
    String string,
    List<String> array,
    Map<String, Command> object) {

}
