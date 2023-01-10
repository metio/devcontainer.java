/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.Map;

class DevcontainerBuilderTest {

  @Test
  void createInstanceWithBuilder() {
    final var devcontainer = Devcontainer.builder()
        .name("test")
        .create();

    Assertions.assertEquals("test", devcontainer.name());
  }

  @Test
  void createNestedBuildComponent() {
    final var devcontainer = Devcontainer.builder()
            .name("test")
            .build(Build.builder().context(".").dockerfile("Containerfile").create())
            .create();

    Assertions.assertEquals("test", devcontainer.name());
    Assertions.assertEquals(".", devcontainer.build().context());
    Assertions.assertEquals("Containerfile", devcontainer.build().dockerfile());
  }

  @Test
  void createNestedCommandComponent() {
    final var devcontainer = Devcontainer.builder()
            .name("test")
            .initializeCommand(Command.builder().string("ps -aux").create())
            .create();

    Assertions.assertEquals("test", devcontainer.name());
    Assertions.assertEquals("ps -aux", devcontainer.initializeCommand().string());
  }

  @Test
  void createNestedHostRequirementsComponent() {
    final var devcontainer = Devcontainer.builder()
            .name("test")
            .hostRequirements(HostRequirements.builder().cpus(3).create())
            .create();

    Assertions.assertEquals("test", devcontainer.name());
    Assertions.assertEquals(3, devcontainer.hostRequirements().cpus());
  }

  @Test
  void createNestedPortAttributeComponent() {
    final var devcontainer = Devcontainer.builder()
            .name("test")
            .portsAttributes(Map.of("http", PortAttribute.builder().elevateIfNeeded(true).create()))
            .create();

    Assertions.assertEquals("test", devcontainer.name());
    Assertions.assertNotNull(devcontainer.portsAttributes().get("http"));
    Assertions.assertEquals(true, devcontainer.portsAttributes().get("http").elevateIfNeeded());
  }

  @Test
  void adjustDevcontainerWithWither() {
    final var original = Devcontainer.builder()
            .name("test")
            .create();
    final var changed = original.withName("new name");

    Assertions.assertEquals("test", original.name());
    Assertions.assertEquals("new name", changed.name());
  }

  @Test
  void adjustBuildWithWither() {
    final var original = Build.builder().dockerfile("Containerfile").create();
    final var changed = original.withDockerfile("Dockerfile");

    Assertions.assertEquals("Containerfile", original.dockerfile());
    Assertions.assertEquals("Dockerfile", changed.dockerfile());
  }

  @Test
  void adjustCommandWithWither() {
    final var original = Command.builder().string("ps -aux").create();
    final var changed = original.withString("htop");

    Assertions.assertEquals("ps -aux", original.string());
    Assertions.assertEquals("htop", changed.string());
  }

  @Test
  void adjustHostRequirementsWithWither() {
    final var original = HostRequirements.builder().cpus(3).create();
    final var changed = original.withCpus(5);

    Assertions.assertEquals(3, original.cpus());
    Assertions.assertEquals(5, changed.cpus());
  }

  @Test
  void adjustHostPortAttributeWithWither() {
    final var original = PortAttribute.builder().elevateIfNeeded(true).create();
    final var changed = original.withElevateIfNeeded(false);

    Assertions.assertEquals(true, original.elevateIfNeeded());
    Assertions.assertEquals(false, changed.elevateIfNeeded());
  }

}
