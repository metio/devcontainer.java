/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import io.soabase.recordbuilder.core.RecordBuilder;
import tools.jackson.databind.annotation.JsonDeserialize;

/**
 * Wrapper for the two ways to specify a {@code mounts} entry. At most one of the parameters is set.
 *
 * @param string The string form, accepting the same syntax as the Docker CLI {@code --mount} flag (e.g.
 *               {@code source=dind-var-lib-docker,target=/var/lib/docker,type=volume}).
 * @param object The object form, expressing the mount with named fields.
 * @see <a href="https://containers.dev/implementors/json_reference/#general-properties">schema reference</a>
 */
@RecordBuilder
@RecordBuilder.Options(buildMethodName = "create")
@JsonDeserialize(using = MountDeserializer.class)
public record Mount(
    String string,
    MountObject object) implements MountBuilder.With {

    public static MountBuilder builder() {
        return MountBuilder.builder();
    }

}
