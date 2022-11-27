/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

/**
 * @param cpus    Indicates the minimum required number of CPUs / virtual CPUs / cores. For example: "hostRequirements":
 *                {"cpus": 2}
 * @param memory  A string indicating minimum memory requirements with a tb, gb, mb, or kb suffix. For example,
 *                "hostRequirements": {"memory": "4gb"}
 * @param storage A string indicating minimum storage requirements with a tb, gb, mb, or kb suffix. For example,
 *                "hostRequirements": {"storage": "32gb"}
 */
public record HostRequirements(
    Integer cpus,
    String memory,
    String storage) {

}
