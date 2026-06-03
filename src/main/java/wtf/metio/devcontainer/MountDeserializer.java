/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;

public final class MountDeserializer extends StdDeserializer<Mount> {

    public MountDeserializer() {
        this(Mount.class);
    }

    public MountDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Mount deserialize(final JsonParser parser, final DeserializationContext context)
            throws JacksonException {
        final JsonNode node = context.readTree(parser);
        if (node.isString()) {
            return new Mount(node.stringValue(), null);
        } else if (node.isObject()) {
            return new Mount(null, context.readTreeAsValue(node, MountObject.class));
        }

        return context.reportInputMismatch(Mount.class, "Cannot deserialize given input to Mount");
    }

}
