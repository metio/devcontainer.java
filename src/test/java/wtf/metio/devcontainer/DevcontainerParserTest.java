/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class DevcontainerParserTest {

  @Test
  @DisplayName("compose.json")
  void compose() throws IOException {
    final var devcontainer = parse("compose.json");
    assertAll(
//        () -> assertIterableEquals(List.of("some-file.yml"), devcontainer.dockerComposeFile, "dockerComposeFile"),
//        () -> assertEquals("dev", devcontainer.service, "service"),
        () -> assertEquals("my-name", devcontainer.name(), "name")
//        () -> assertIterableEquals(List.of("first", "second"), devcontainer.runServices, "runServices")
    );
  }

  @Test
  @DisplayName("image.json")
  void image() throws IOException {
    final var devcontainer = parse("image.json");
    assertAll(
        () -> assertEquals("example:123", devcontainer.image(), "image")
    );
  }

  @Test
  @DisplayName("shell-big.json")
  void shellBig() throws IOException {
    final var devcontainer = parse("shell-big.json");
    assertAll(
        () -> assertEquals("example:123", devcontainer.image(), "image"),
        () -> assertEquals("testUser", devcontainer.remoteUser(), "remoteUser"),
        () -> assertEquals("root", devcontainer.containerUser(), "containerUser"),
        () -> assertTrue(devcontainer.overrideCommand(), "overrideCommand"),
        () -> assertEquals("/home/testUser/project", devcontainer.workspaceFolder(), "workspaceFolder"),
        () -> assertIterableEquals(List.of("--pull"), devcontainer.runArgs(), "runArgs"),
        () -> assertIterableEquals(List.of("12345", "9876"), devcontainer.forwardPorts(), "forwardPorts")
    );
  }

  @Test
  @DisplayName("host-requirements.json")
  void hostRequirements() throws IOException {
    final var devcontainer = parse("host-requirements.json");
    assertAll(
        () -> assertEquals(2, devcontainer.hostRequirements().cpus(), "hostRequirements.cpus"),
        () -> assertEquals("4gb", devcontainer.hostRequirements().memory(), "hostRequirements.memory"),
        () -> assertEquals("32gb", devcontainer.hostRequirements().storage(), "hostRequirements.storage")
    );
  }

  @Test
  @DisplayName("ports-attributes.json")
  void portsAttributes() throws IOException {
    final var devcontainer = parse("ports-attributes.json");
    assertAll(
        () -> assertEquals("Application Port", devcontainer.portsAttributes().get("3000").label(), "label"),
        () -> assertEquals(Protocol.http, devcontainer.portsAttributes().get("3000").protocol(), "protocol"),
        () -> assertEquals(OnAutoForward.openBrowser, devcontainer.portsAttributes().get("3000").onAutoForward(),
            "onAutoForward"),
        () -> assertTrue(devcontainer.portsAttributes().get("3000").requireLocalPort(), "requireLocalPort"),
        () -> assertTrue(devcontainer.portsAttributes().get("3000").elevateIfNeeded(), "elevateIfNeeded")
    );
  }

  @Test
  @DisplayName("other-ports-attributes.json")
  void otherPortsAttributes() throws IOException {
    final var devcontainer = parse("other-ports-attributes.json");
    assertAll(
        () -> assertEquals("Application Port", devcontainer.otherPortsAttributes().label(), "label"),
        () -> assertEquals(Protocol.http, devcontainer.otherPortsAttributes().protocol(), "protocol"),
        () -> assertEquals(OnAutoForward.openBrowser, devcontainer.otherPortsAttributes().onAutoForward(),
            "onAutoForward"),
        () -> assertTrue(devcontainer.otherPortsAttributes().requireLocalPort(), "requireLocalPort"),
        () -> assertTrue(devcontainer.otherPortsAttributes().elevateIfNeeded(), "elevateIfNeeded")
    );
  }

  @Test
  @DisplayName("name.json")
  void name() throws IOException {
    final var devcontainer = parse("name.json");
    assertAll(
        () -> assertEquals("example", devcontainer.name(), "name")
    );
  }

  @Test
  @DisplayName("forward-ports.json")
  void forwardPorts() throws IOException {
    final var devcontainer = parse("forward-ports.json");
    assertAll(
        () -> assertIterableEquals(List.of("db:5432"), devcontainer.forwardPorts(), "forwardPorts")
    );
  }

  @Test
  @DisplayName("forward-ports-ints.json")
  void forwardPortsInts() throws IOException {
    final var devcontainer = parse("forward-ports-ints.json");
    assertAll(
        () -> assertIterableEquals(List.of("5432", "12345"), devcontainer.forwardPorts(), "forwardPorts")
    );
  }

  @Test
  @DisplayName("forward-ports-mixed.json")
  void forwardPortsMixed() throws IOException {
    final var devcontainer = parse("forward-ports-mixed.json");
    assertAll(
        () -> assertIterableEquals(List.of("3000", "db:5432"), devcontainer.forwardPorts(), "forwardPorts")
    );
  }

  @Test
  @DisplayName("forward-ports-empty.json")
  void forwardPortsEmpty() throws IOException {
    final var devcontainer = parse("forward-ports-empty.json");
    assertAll(
        () -> assertIterableEquals(List.of(), devcontainer.forwardPorts(), "forwardPorts")
    );
  }

  @Test
  @DisplayName("remote-env.json")
  void remoteEnv() throws IOException {
    final var devcontainer = parse("remote-env.json");
    assertAll(
        () -> assertEquals(Map.of("key", "value", "other", "entry"), devcontainer.remoteEnv(), "remoteEnv")
    );
  }

  @Test
  @DisplayName("remote-user.json")
  void remoteUser() throws IOException {
    final var devcontainer = parse("remote-user.json");
    assertAll(
        () -> assertEquals("user-a", devcontainer.remoteUser(), "remoteUser")
    );
  }

  @Test
  @DisplayName("container-env.json")
  void containerEnv() throws IOException {
    final var devcontainer = parse("container-env.json");
    assertAll(
        () -> assertEquals(Map.of("key", "value", "other", "entry"), devcontainer.containerEnv(), "containerEnv")
    );
  }

  @Test
  @DisplayName("container-user.json")
  void containerUser() throws IOException {
    final var devcontainer = parse("container-user.json");
    assertAll(
        () -> assertEquals("user-a", devcontainer.containerUser(), "containerUser")
    );
  }

  @Test
  @DisplayName("update-remote-user-uid.json")
  void updateRemoteUserUID() throws IOException {
    final var devcontainer = parse("update-remote-user-uid.json");
    assertAll(
        () -> assertEquals(true, devcontainer.updateRemoteUserUID(), "updateRemoteUserUID")
    );
  }

  @Test
  @DisplayName("user-env-probe.json")
  void userEnvProbe() throws IOException {
    final var devcontainer = parse("user-env-probe.json");
    assertAll(
        () -> assertEquals(UserEnvProbe.loginInteractiveShell, devcontainer.userEnvProbe(), "userEnvProbe")
    );
  }

  @Test
  @DisplayName("user-env-probe-none.json")
  void userEnvProbeNone() throws IOException {
    final var devcontainer = parse("user-env-probe-none.json");
    assertAll(
        () -> assertEquals(UserEnvProbe.none, devcontainer.userEnvProbe(), "userEnvProbe")
    );
  }

  @Test
  @DisplayName("user-env-probe-login-shell.json")
  void userEnvProbeLoginShell() throws IOException {
    final var devcontainer = parse("user-env-probe-login-shell.json");
    assertAll(
        () -> assertEquals(UserEnvProbe.loginShell, devcontainer.userEnvProbe(), "userEnvProbe")
    );
  }

  @Test
  @DisplayName("user-env-probe-interactive-shell.json")
  void userEnvProbeInteractiveShell() throws IOException {
    final var devcontainer = parse("user-env-probe-interactive-shell.json");
    assertAll(
        () -> assertEquals(UserEnvProbe.interactiveShell, devcontainer.userEnvProbe(), "userEnvProbe")
    );
  }

  @Test
  @DisplayName("override-command.json")
  void overrideCommand() throws IOException {
    final var devcontainer = parse("override-command.json");
    assertAll(
        () -> assertEquals(true, devcontainer.overrideCommand(), "overrideCommand")
    );
  }

  @Test
  @DisplayName("shutdown-action.json")
  void shutdownAction() throws IOException {
    final var devcontainer = parse("shutdown-action.json");
    assertAll(
        () -> assertEquals(ShutdownAction.none, devcontainer.shutdownAction(), "shutdownAction")
    );
  }

  @Test
  @DisplayName("shutdown-action-stop-compose.json")
  void shutdownActionStopCompose() throws IOException {
    final var devcontainer = parse("shutdown-action-stop-compose.json");
    assertAll(
        () -> assertEquals(ShutdownAction.stopCompose, devcontainer.shutdownAction(), "shutdownAction")
    );
  }

  @Test
  @DisplayName("shutdown-action-stop-container.json")
  void shutdownActionStopContainer() throws IOException {
    final var devcontainer = parse("shutdown-action-stop-container.json");
    assertAll(
        () -> assertEquals(ShutdownAction.stopContainer, devcontainer.shutdownAction(), "shutdownAction")
    );
  }

  @Test
  @DisplayName("init.json")
  void init() throws IOException {
    final var devcontainer = parse("init.json");
    assertAll(
        () -> assertEquals(true, devcontainer.init(), "init")
    );
  }

  @Test
  @DisplayName("privileged.json")
  void privileged() throws IOException {
    final var devcontainer = parse("privileged.json");
    assertAll(
        () -> assertEquals(true, devcontainer.privileged(), "privileged")
    );
  }

  @Test
  @DisplayName("cap-add.json")
  void capAdd() throws IOException {
    final var devcontainer = parse("cap-add.json");
    assertAll(
        () -> assertIterableEquals(List.of("SYS_PTRACE"), devcontainer.capAdd(), "capAdd")
    );
  }

  @Test
  @DisplayName("cap-add-empty.json")
  void capAddEmpty() throws IOException {
    final var devcontainer = parse("cap-add-empty.json");
    assertAll(
        () -> assertIterableEquals(List.of(), devcontainer.capAdd(), "capAdd")
    );
  }

  @Test
  @DisplayName("security-opt.json")
  void securityOpt() throws IOException {
    final var devcontainer = parse("security-opt.json");
    assertAll(
        () -> assertIterableEquals(List.of("seccomp=unconfined"), devcontainer.securityOpt(), "securityOpt")
    );
  }

  @Test
  @DisplayName("security-opt-empty.json")
  void securityOptEmpty() throws IOException {
    final var devcontainer = parse("security-opt-empty.json");
    assertAll(
        () -> assertIterableEquals(List.of(), devcontainer.securityOpt(), "securityOpt")
    );
  }

  @Test
  @DisplayName("mounts.json")
  void mounts() throws IOException {
    final var devcontainer = parse("mounts.json");
    assertAll(
        () -> assertIterableEquals(List.of(Map.of(
                "source", "dind-var-lib-docker",
                "target", "/var/lib/docker",
                "type", "volume")),
            devcontainer.mounts(), "mounts")
    );
  }

  @Test
  @DisplayName("features.json")
  void features() throws IOException {
    final var devcontainer = parse("features.json");
    assertAll(
        () -> assertEquals(Map.of("ghcr.io/devcontainers/features/github-cli", Map.of()),
            devcontainer.features(), "features")
    );
  }

  @Test
  @DisplayName("override-feature-install-order.json")
  void overrideFeatureInstallOrder() throws IOException {
    final var devcontainer = parse("override-feature-install-order.json");
    assertAll(
        () -> assertIterableEquals(
            List.of("ghcr.io/devcontainers/features/common-utils", "ghcr.io/devcontainers/features/github-cli"),
            devcontainer.overrideFeatureInstallOrder(), "overrideFeatureInstallOrder")
    );
  }

  @Test
  @DisplayName("customizations.json")
  void customizations() throws IOException {
    final var devcontainer = parse("customizations.json");
    assertAll(
        () -> assertEquals(Map.of("vscode", Map.of("settings", Map.of(), "extensions", List.of())),
            devcontainer.customizations(), "customizations")
    );
  }

  @Test
  @DisplayName("build.json")
  void build() throws IOException {
    final var devcontainer = parse("build.json");
    assertAll(
        () -> assertEquals("Containerfile", devcontainer.build().dockerfile(), "build.dockerfile"),
        () -> assertEquals(".", devcontainer.build().context(), "build.context"),
        () -> assertEquals("dev", devcontainer.build().target(), "build.target"),
        () -> assertEquals(Map.of("some", "value"), devcontainer.build().args(), "build.args"),
        () -> assertIterableEquals(List.of("some-cache:latest"), devcontainer.build().cacheFrom(), "build.cacheFrom")
    );
  }

  @Test
  @DisplayName("app-port.json")
  void appPort() throws IOException {
    final var devcontainer = parse("app-port.json");
    assertAll(
        () -> assertIterableEquals(List.of("12345", "5432"), devcontainer.appPort(), "appPort")
    );
  }

  @Test
  @DisplayName("workspace-mount.json")
  void workspaceMount() throws IOException {
    final var devcontainer = parse("workspace-mount.json");
    assertAll(
        () -> assertEquals("source=${localWorkspaceFolder}/sub-folder,target=/workspace,type=bind,consistency=cached",
            devcontainer.workspaceMount(), "workspaceMount")
    );
  }

  @Test
  @DisplayName("workspace-folder.json")
  void workspaceFolder() throws IOException {
    final var devcontainer = parse("workspace-folder.json");
    assertAll(
        () -> assertEquals("/workspace", devcontainer.workspaceFolder(), "workspaceFolder")
    );
  }

  @Test
  @DisplayName("run-args.json")
  void runArgs() throws IOException {
    final var devcontainer = parse("run-args.json");
    assertAll(
        () -> assertIterableEquals(List.of("--cap-add=SYS_PTRACE", "--security-opt", "seccomp=unconfined"),
            devcontainer.runArgs(), "runArgs")
    );
  }

  @Test
  @DisplayName("docker-compose-file.json")
  void dockerComposeFile() throws IOException {
    final var devcontainer = parse("docker-compose-file.json");
    assertAll(
        () -> assertIterableEquals(List.of("docker-compose.yml"),
            devcontainer.dockerComposeFile(), "dockerComposeFile")
    );
  }

  @Test
  @DisplayName("service.json")
  void service() throws IOException {
    final var devcontainer = parse("service.json");
    assertAll(
        () -> assertEquals("dev", devcontainer.service(), "service")
    );
  }

  @Test
  @DisplayName("run-services.json")
  void runServices() throws IOException {
    final var devcontainer = parse("run-services.json");
    assertAll(
        () -> assertIterableEquals(List.of("database", "proxy"), devcontainer.runServices(), "runServices")
    );
  }

  @Test
  @DisplayName("wait-for.json")
  void waitFor() throws IOException {
    final var devcontainer = parse("wait-for.json");
    assertAll(
        () -> assertEquals(WaitFor.initializeCommand, devcontainer.waitFor(), "waitFor")
    );
  }

  @Test
  @DisplayName("wait-for-on-create.json")
  void waitForOnCreate() throws IOException {
    final var devcontainer = parse("wait-for-on-create.json");
    assertAll(
        () -> assertEquals(WaitFor.onCreateCommand, devcontainer.waitFor(), "waitFor")
    );
  }

  @Test
  @DisplayName("wait-for-update-content.json")
  void waitForUpdateContent() throws IOException {
    final var devcontainer = parse("wait-for-update-content.json");
    assertAll(
        () -> assertEquals(WaitFor.updateContentCommand, devcontainer.waitFor(), "waitFor")
    );
  }

  @Test
  @DisplayName("wait-for-post-create.json")
  void waitForPostCreate() throws IOException {
    final var devcontainer = parse("wait-for-post-create.json");
    assertAll(
        () -> assertEquals(WaitFor.postCreateCommand, devcontainer.waitFor(), "waitFor")
    );
  }

  @Test
  @DisplayName("wait-for-post-start.json")
  void waitForPostStart() throws IOException {
    final var devcontainer = parse("wait-for-post-start.json");
    assertAll(
        () -> assertEquals(WaitFor.postStartCommand, devcontainer.waitFor(), "waitFor")
    );
  }

  @Test
  @DisplayName("wait-for-post-attach.json")
  void waitForPostAttach() throws IOException {
    final var devcontainer = parse("wait-for-post-attach.json");
    assertAll(
        () -> assertEquals(WaitFor.postAttachCommand, devcontainer.waitFor(), "waitFor")
    );
  }

  @Test
  @DisplayName("initialize-command.json")
  void initializeCommand() throws IOException {
    final var devcontainer = parse("initialize-command.json");
    assertAll(
        () -> assertEquals("echo foo='bar'", devcontainer.initializeCommand().string(), "initializeCommand")
    );
  }

  @Test
  @DisplayName("on-create-command.json")
  void onCreateCommand() throws IOException {
    final var devcontainer = parse("on-create-command.json");
    assertAll(
        () -> assertIterableEquals(List.of("echo", "foo='bar'"), devcontainer.onCreateCommand().array(), "onCreateCommand")
    );
  }

  @Test
  @DisplayName("update-content-command.json")
  void updateContentCommand() throws IOException {
    final var devcontainer = parse("update-content-command.json");
    assertAll(
        () -> assertEquals("npm start",
            devcontainer.updateContentCommand().object().get("server").string(), "updateContentCommand.server"),
        () -> assertIterableEquals(List.of("mysql", "-u", "root", "-p", "my database"),
            devcontainer.updateContentCommand().object().get("db").array(), "updateContentCommand.db")
    );
  }

  private Devcontainer parse(final String testFile) throws IOException {
    return DevcontainerParser.parseDevcontainer(Paths.get("src/test/resources/").resolve(testFile));
  }

}
