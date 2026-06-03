/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

class CommandDeserializerTest {

  private final ObjectMapper mapper = Devcontainer.defaultObjectMapper();

  @Test
  void deserializesString() {
    assertEquals("echo hi", mapper.readValue("\"echo hi\"", Command.class).string());
  }

  @Test
  void deserializesArray() {
    assertIterableEquals(List.of("echo", "hi"), mapper.readValue("[\"echo\", \"hi\"]", Command.class).array());
  }

  @Test
  void deserializesObject() {
    final Command command = mapper.readValue("{\"server\": \"npm start\"}", Command.class);
    assertEquals("npm start", command.object().get("server").string());
  }

  @Test
  void rejectsNumber() {
    assertThrows(JacksonException.class, () -> mapper.readValue("123", Command.class));
  }

}
