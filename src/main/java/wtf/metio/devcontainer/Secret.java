/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * Metadata for a recommended secret, keyed by the environment variable name under {@code secrets}.
 *
 * @param description      A description of the secret.
 * @param documentationUrl A URL to documentation about the secret.
 * @see <a href="https://containers.dev/implementors/json_reference/#general-properties">schema reference</a>
 */
@RecordBuilder
@RecordBuilder.Options(buildMethodName = "create")
public record Secret(
    String description,
    String documentationUrl) implements SecretBuilder.With {

    public static SecretBuilder builder() {
        return SecretBuilder.builder();
    }

}
