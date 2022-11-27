/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.fasterxml.jackson.databind.node.TextNode;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public final class CommandDeserializer extends StdDeserializer<Command> {

  public CommandDeserializer() {
    this(Command.class);
  }

  public CommandDeserializer(final Class<?> vc) {
    super(vc);
  }

  @Override
  public Command deserialize(final JsonParser parser, final DeserializationContext context)
      throws IOException, JacksonException {
    final var node = parser.getCodec().readTree(parser);
    if (node.isValueNode()) {
      if (node instanceof TextNode textNode) {
        return new Command(textNode.textValue(), null, null);
      }
    } else if (node.isArray()) {
      try (final var arrayNode = node.traverse(parser.getCodec())) {
        final List<String> list = arrayNode.readValueAs(new TypeReference<List<String>>() {
        });
        return new Command(null, list, null);
      }
    } else if (node.isObject()) {
      try (final var objectNode = node.traverse(parser.getCodec())) {
        final Map<String, Command> object = objectNode.readValueAs(new TypeReference<Map<String, Command>>() {
        });
        return new Command(null, null, object);
      }
    }

    return context.reportInputMismatch(Command.class, "Cannot deserialize given input to Command");
  }

}
