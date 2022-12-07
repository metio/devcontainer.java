/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class DevcontainerBuilderTest {

  @Test
  void createInstanceWithBuilder() {
    final var devcontainer = DevcontainerBuilder.builder()
        .name("test")
        .create();

    Assertions.assertEquals("test", devcontainer.name());
  }

  @Test
  void createNestedBuildComponent() {
    final var devcontainer = DevcontainerBuilder.builder()
        .name("test")
        .build(BuildBuilder.builder().context(".").dockerfile("Containerfile").create())
        .create();

    Assertions.assertEquals("test", devcontainer.name());
    Assertions.assertEquals(".", devcontainer.build().context());
    Assertions.assertEquals("Containerfile", devcontainer.build().dockerfile());
  }

}
