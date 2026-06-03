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

public final class GpuDeserializer extends StdDeserializer<Gpu> {

    public GpuDeserializer() {
        this(Gpu.class);
    }

    public GpuDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Gpu deserialize(final JsonParser parser, final DeserializationContext context)
            throws JacksonException {
        final JsonNode node = context.readTree(parser);
        if (node.isBoolean()) {
            return new Gpu(node.booleanValue(), null, null);
        } else if (node.isString()) {
            return new Gpu(null, node.stringValue(), null);
        } else if (node.isObject()) {
            return new Gpu(null, null, context.readTreeAsValue(node, GpuRequirements.class));
        }

        return context.reportInputMismatch(Gpu.class, "Cannot deserialize given input to Gpu");
    }

}
