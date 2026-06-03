/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import tools.jackson.core.JacksonException;
import tools.jackson.databind.ObjectMapper;

class MountDeserializerTest {

  private final ObjectMapper mapper = Devcontainer.defaultObjectMapper();

  @Test
  void deserializesString() {
    assertEquals("source=vol,target=/data,type=volume",
        mapper.readValue("\"source=vol,target=/data,type=volume\"", Mount.class).string());
  }

  @Test
  void deserializesObject() {
    final Mount mount = mapper.readValue("{\"type\": \"volume\", \"target\": \"/data\"}", Mount.class);
    assertEquals(MountType.volume, mount.object().type());
    assertEquals("/data", mount.object().target());
  }

  @Test
  void rejectsNumber() {
    assertThrows(JacksonException.class, () -> mapper.readValue("123", Mount.class));
  }

}
