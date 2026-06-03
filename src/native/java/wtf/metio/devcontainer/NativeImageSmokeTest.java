/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import java.util.List;

/**
 * Stand-in for a downstream consumer such as ilo: a minimal program that depends on the library and
 * is compiled to a GraalVM native image by the {@code native} profile. It drives the
 * reflection-backed JSON parsing and the generated builders, so a successful native build and run
 * proves that the records and their {@code reflect-config.json} resolve under native-image. If this
 * image builds and runs, any project depending on the library can native-compile it as well.
 *
 * <p>This class lives outside {@code src/main/java} and is only compiled when the {@code native}
 * profile adds {@code src/native/java} as a source root, so it never ships in the published jar.
 */
public final class NativeImageSmokeTest {

  public static void main(final String[] args) throws Exception {
    final var parsed = Devcontainer.parse("""
        {
          "name": "smoke",
          "image": "example:123",
          "forwardPorts": [3000, "db:5432"],
          "postCreateCommand": "npm install",
          "build": { "options": ["--no-cache"] },
          "hostRequirements": { "gpu": { "cores": 2, "memory": "8gb" } },
          "secrets": { "TOKEN": { "description": "a token" } },
          "mounts": [{ "source": "vol", "target": "/data", "type": "volume" }]
        }
        """);
    require("example:123".equals(parsed.image()), "image");
    require(List.of("3000", "db:5432").equals(parsed.forwardPorts()), "forwardPorts");
    require("npm install".equals(parsed.postCreateCommand().string()), "postCreateCommand");
    require(List.of("--no-cache").equals(parsed.build().options()), "build.options");
    require(Integer.valueOf(2).equals(parsed.hostRequirements().gpu().requirements().cores()), "gpu.requirements.cores");
    require("a token".equals(parsed.secrets().get("TOKEN").description()), "secrets.description");
    require(MountType.volume.equals(parsed.mounts().get(0).object().type()), "mounts.type");

    final var built = Devcontainer.builder().name("built").image("quay.io/x:1").create();
    require("built".equals(built.name()), "builder");
    require("renamed".equals(built.withName("renamed").name()), "wither");

    System.out.println("native-image smoke check passed");
  }

  private static void require(final boolean condition, final String what) {
    if (!condition) {
      throw new AssertionError("native-image smoke check failed for: " + what);
    }
  }

}
