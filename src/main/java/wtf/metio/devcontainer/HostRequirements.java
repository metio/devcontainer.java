/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * @param cpus    Indicates the minimum required number of CPUs / virtual CPUs / cores. For example: "hostRequirements":
 *                {"cpus": 2}
 * @param memory  A string indicating minimum memory requirements with a tb, gb, mb, or kb suffix. For example,
 *                "hostRequirements": {"memory": "4gb"}
 * @param storage A string indicating minimum storage requirements with a tb, gb, mb, or kb suffix. For example,
 *                "hostRequirements": {"storage": "32gb"}
 * @param gpu     Indicates whether a GPU is required. The string "optional" indicates that a GPU is used if available.
 *                An object value allows detailed GPU requirements to be expressed. For example: "hostRequirements":
 *                {"gpu": "optional"}
 */
@RecordBuilder
@RecordBuilder.Options(buildMethodName = "create")
public record HostRequirements(
    Integer cpus,
    String memory,
    String storage,
    Gpu gpu) implements HostRequirementsBuilder.With {

    public static HostRequirementsBuilder builder() {
        return HostRequirementsBuilder.builder();
    }

}
