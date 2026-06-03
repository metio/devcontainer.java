/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.nio.file.Paths;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.ObjectMapper;

class DevcontainerParseTest {

  private static final String JSON = "{\"name\":\"example\"}";
  private static final File FILE = new File("src/test/resources/name.json");

  @Test
  void parseFromString() {
    assertEquals("example", Devcontainer.parse(JSON).name());
  }

  @Test
  void parseFromStringWithMapper() {
    assertEquals("example", Devcontainer.parse(JSON, Devcontainer.defaultObjectMapper()).name());
  }

  @Test
  void parseFromFile() {
    assertEquals("example", Devcontainer.parse(FILE).name());
  }

  @Test
  void parseFromFileWithMapper() {
    assertEquals("example", Devcontainer.parse(FILE, Devcontainer.defaultObjectMapper()).name());
  }

  @Test
  void parseFromPath() {
    assertEquals("example", Devcontainer.parse(Paths.get("src/test/resources/name.json")).name());
  }

  @Test
  void parseFromPathWithMapper() {
    final ObjectMapper mapper = Devcontainer.defaultObjectMapper();
    assertEquals("example", Devcontainer.parse(Paths.get("src/test/resources/name.json"), mapper).name());
  }

}
