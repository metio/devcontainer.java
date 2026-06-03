/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * The object form of the {@code hostRequirements.gpu} property, used to express detailed GPU requirements.
 *
 * @param cores  Indicates the minimum required number of cores. For example: "gpu": {"cores": 2}
 * @param memory A string indicating minimum memory requirements with a tb, gb, mb, or kb suffix. For example, "gpu":
 *               {"memory": "8gb"}
 * @see <a href="https://containers.dev/implementors/json_reference/#min-host-reqs">schema reference</a>
 */
@RecordBuilder
@RecordBuilder.Options(buildMethodName = "create")
public record GpuRequirements(
    Integer cores,
    String memory) implements GpuRequirementsBuilder.With {

    public static GpuRequirementsBuilder builder() {
        return GpuRequirementsBuilder.builder();
    }

}
