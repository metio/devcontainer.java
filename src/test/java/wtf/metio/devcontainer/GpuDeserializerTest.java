/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

class GpuDeserializerTest {

  private final ObjectMapper mapper = Devcontainer.defaultObjectMapper();

  @Test
  void deserializesBoolean() {
    assertTrue(mapper.readValue("true", Gpu.class).enabled());
  }

  @Test
  void deserializesString() {
    assertEquals("optional", mapper.readValue("\"optional\"", Gpu.class).optional());
  }

  @Test
  void deserializesObject() {
    final Gpu gpu = mapper.readValue("{\"cores\": 2, \"memory\": \"8gb\"}", Gpu.class);
    assertEquals(2, gpu.requirements().cores());
    assertEquals("8gb", gpu.requirements().memory());
  }

  @Test
  void rejectsNumber() {
    assertThrows(JacksonException.class, () -> mapper.readValue("123", Gpu.class));
  }

}
