/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import io.soabase.recordbuilder.core.RecordBuilder;
import tools.jackson.databind.annotation.JsonDeserialize;

/**
 * Wrapper for the various ways to specify the {@code hostRequirements.gpu} property. At most one of the parameters is
 * set.
 *
 * @param enabled      The boolean form: {@code true} requires a GPU, {@code false} does not.
 * @param optional     The string form. The only accepted value is {@code optional}, indicating a GPU is used when
 *                     available but not required.
 * @param requirements The object form, expressing detailed GPU requirements.
 * @see <a href="https://containers.dev/implementors/json_reference/#min-host-reqs">schema reference</a>
 */
@RecordBuilder
@RecordBuilder.Options(buildMethodName = "create")
@JsonDeserialize(using = GpuDeserializer.class)
public record Gpu(
    Boolean enabled,
    String optional,
    GpuRequirements requirements) implements GpuBuilder.With {

    public static GpuBuilder builder() {
        return GpuBuilder.builder();
    }

}
