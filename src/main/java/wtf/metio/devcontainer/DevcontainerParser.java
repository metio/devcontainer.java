/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;

public final class DevcontainerParser {

  public static Devcontainer parseDevcontainer(final Path devcontainer) throws IOException {
    return parseDevcontainer(devcontainer, defaultObjectMapper());
  }

  public static Devcontainer parseDevcontainer(final Path devcontainer, final ObjectMapper objectMapper) throws IOException {
    return objectMapper.readValue(devcontainer.toFile(), Devcontainer.class);
  }

  public static Devcontainer parseDevcontainer(final File devcontainer) throws IOException {
    return parseDevcontainer(devcontainer, defaultObjectMapper());
  }

  public static Devcontainer parseDevcontainer(final File devcontainer, final ObjectMapper objectMapper) throws IOException {
    return objectMapper.readValue(devcontainer, Devcontainer.class);
  }

  public static ObjectMapper defaultObjectMapper() {
    final var mapper = new ObjectMapper();
    mapper.disable(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES);
    mapper.enable(DeserializationFeature.ACCEPT_SINGLE_VALUE_AS_ARRAY);
    return mapper;
  }

  private DevcontainerParser() {
    // utility class
  }

}
