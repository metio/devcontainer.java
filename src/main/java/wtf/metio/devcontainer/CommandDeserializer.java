/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import java.util.List;
import java.util.Map;
import tools.jackson.core.JacksonException;
import tools.jackson.core.JsonParser;
import tools.jackson.databind.DeserializationContext;
import tools.jackson.databind.JavaType;
import tools.jackson.databind.JsonNode;
import tools.jackson.databind.deser.std.StdDeserializer;
import tools.jackson.databind.exc.MismatchedInputException;
import tools.jackson.databind.node.StringNode;

public final class CommandDeserializer extends StdDeserializer<Command> {

    public CommandDeserializer() {
        this(Command.class);
    }

    public CommandDeserializer(final Class<?> vc) {
        super(vc);
    }

    @Override
    public Command deserialize(final JsonParser parser, final DeserializationContext context)
            throws JacksonException {
        final JsonNode node = context.readTree(parser);
        if (node instanceof StringNode stringNode) {
            return new Command(stringNode.stringValue(), null, null);
        } else if (node.isArray()) {
            final JavaType type = context.getTypeFactory().constructCollectionType(List.class, String.class);
            return new Command(null, context.readTreeAsValue(node, type), null);
        } else if (node.isObject()) {
            final JavaType type = context.getTypeFactory().constructMapType(Map.class, String.class, Command.class);
            return new Command(null, null, context.readTreeAsValue(node, type));
        }

        throw MismatchedInputException.from(parser, Command.class, "Cannot deserialize given input to Command");
    }

}
