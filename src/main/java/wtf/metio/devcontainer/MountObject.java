/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * The object form of a {@code mounts} entry, mirroring the fields of the Docker CLI {@code --mount} flag.
 *
 * @param type   The kind of mount. Required.
 * @param source The source of the mount. Optional for a {@code volume} type, where Docker may create an anonymous
 *               volume.
 * @param target The path the mount is created at inside the container. Required.
 * @see <a href="https://containers.dev/implementors/json_reference/#general-properties">schema reference</a>
 */
@RecordBuilder
@RecordBuilder.Options(buildMethodName = "create")
public record MountObject(
    MountType type,
    String source,
    String target) implements MountObjectBuilder.With {

    public static MountObjectBuilder builder() {
        return MountObjectBuilder.builder();
    }

}
