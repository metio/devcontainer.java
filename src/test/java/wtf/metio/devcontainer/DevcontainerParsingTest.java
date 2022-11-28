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
import java.util.function.Function;
import java.util.stream.Stream;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.DynamicTest;
import org.junit.jupiter.api.TestFactory;
import org.junit.jupiter.api.function.Executable;

class DevcontainerParsingTest {

  @TestFactory
  @DisplayName("Parse JSON files")
  Stream<DynamicTest> shouldParseJsonFiles() {
    final Map<String, Function<Devcontainer, Executable>> testcases = Map.ofEntries(
        Map.entry("update-content-command.json", devcontainer -> () -> assertAll(
            () -> assertEquals("npm start",
                devcontainer.updateContentCommand().object().get("server").string(), "updateContentCommand.server"),
            () -> assertIterableEquals(List.of("mysql", "-u", "root", "-p", "my database"),
                devcontainer.updateContentCommand().object().get("db").array(), "updateContentCommand.db")
        )),
        Map.entry("compose.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("some-file.yml"), devcontainer.dockerComposeFile(), "dockerComposeFile"),
            () -> assertEquals("dev", devcontainer.service(), "service"),
            () -> assertEquals("my-name", devcontainer.name(), "name"),
            () -> assertIterableEquals(List.of("first", "second"), devcontainer.runServices(), "runServices")
        )),
        Map.entry("image.json", devcontainer -> () -> assertAll(
            () -> assertEquals("example:123", devcontainer.image(), "image")
        )),
        Map.entry("shell-big.json", devcontainer -> () -> assertAll(
            () -> assertEquals("example:123", devcontainer.image(), "image"),
            () -> assertEquals("testUser", devcontainer.remoteUser(), "remoteUser"),
            () -> assertEquals("root", devcontainer.containerUser(), "containerUser"),
            () -> assertTrue(devcontainer.overrideCommand(), "overrideCommand"),
            () -> assertEquals("/home/testUser/project", devcontainer.workspaceFolder(), "workspaceFolder"),
            () -> assertIterableEquals(List.of("--pull"), devcontainer.runArgs(), "runArgs"),
            () -> assertIterableEquals(List.of("12345", "9876"), devcontainer.forwardPorts(), "forwardPorts")
        )),
        Map.entry("host-requirements.json", devcontainer -> () -> assertAll(
            () -> assertEquals(2, devcontainer.hostRequirements().cpus(), "hostRequirements.cpus"),
            () -> assertEquals("4gb", devcontainer.hostRequirements().memory(), "hostRequirements.memory"),
            () -> assertEquals("32gb", devcontainer.hostRequirements().storage(), "hostRequirements.storage")
        )),
        Map.entry("ports-attributes.json", devcontainer -> () -> assertAll(
            () -> assertEquals("Application Port", devcontainer.portsAttributes().get("3000").label(), "label"),
            () -> assertEquals(Protocol.http, devcontainer.portsAttributes().get("3000").protocol(), "protocol"),
            () -> assertEquals(OnAutoForward.openBrowser, devcontainer.portsAttributes().get("3000").onAutoForward(),
                "onAutoForward"),
            () -> assertTrue(devcontainer.portsAttributes().get("3000").requireLocalPort(), "requireLocalPort"),
            () -> assertTrue(devcontainer.portsAttributes().get("3000").elevateIfNeeded(), "elevateIfNeeded")
        )),
        Map.entry("other-ports-attributes.json", devcontainer -> () -> assertAll(
            () -> assertEquals("Application Port", devcontainer.otherPortsAttributes().label(), "label"),
            () -> assertEquals(Protocol.http, devcontainer.otherPortsAttributes().protocol(), "protocol"),
            () -> assertEquals(OnAutoForward.openBrowser, devcontainer.otherPortsAttributes().onAutoForward(),
                "onAutoForward"),
            () -> assertTrue(devcontainer.otherPortsAttributes().requireLocalPort(), "requireLocalPort"),
            () -> assertTrue(devcontainer.otherPortsAttributes().elevateIfNeeded(), "elevateIfNeeded")
        )),
        Map.entry("name.json", devcontainer -> () -> assertAll(
            () -> assertEquals("example", devcontainer.name(), "name")
        )),
        Map.entry("forward-ports.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("db:5432"), devcontainer.forwardPorts(), "forwardPorts")
        )),
        Map.entry("forward-ports-ints.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("5432", "12345"), devcontainer.forwardPorts(), "forwardPorts")
        )),
        Map.entry("forward-ports-mixed.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("3000", "db:5432"), devcontainer.forwardPorts(), "forwardPorts")
        )),
        Map.entry("forward-ports-empty.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of(), devcontainer.forwardPorts(), "forwardPorts")
        )),
        Map.entry("remote-env.json", devcontainer -> () -> assertAll(
            () -> assertEquals(Map.of("key", "value", "other", "entry"), devcontainer.remoteEnv(), "remoteEnv")
        )),
        Map.entry("remote-user.json", devcontainer -> () -> assertAll(
            () -> assertEquals("user-a", devcontainer.remoteUser(), "remoteUser")
        )),
        Map.entry("container-env.json", devcontainer -> () -> assertAll(
            () -> assertEquals(Map.of("key", "value", "other", "entry"), devcontainer.containerEnv(), "containerEnv")
        )),
        Map.entry("container-user.json", devcontainer -> () -> assertAll(
            () -> assertEquals("user-a", devcontainer.containerUser(), "containerUser")
        )),
        Map.entry("update-remote-user-uid.json", devcontainer -> () -> assertAll(
            () -> assertEquals(true, devcontainer.updateRemoteUserUID(), "updateRemoteUserUID")
        )),
        Map.entry("user-env-probe.json", devcontainer -> () -> assertAll(
            () -> assertEquals(UserEnvProbe.loginInteractiveShell, devcontainer.userEnvProbe(), "userEnvProbe")
        )),
        Map.entry("user-env-probe-none.json", devcontainer -> () -> assertAll(
            () -> assertEquals(UserEnvProbe.none, devcontainer.userEnvProbe(), "userEnvProbe")
        )),
        Map.entry("user-env-probe-login-shell.json", devcontainer -> () -> assertAll(
            () -> assertEquals(UserEnvProbe.loginShell, devcontainer.userEnvProbe(), "userEnvProbe")
        )),
        Map.entry("user-env-probe-interactive-shell.json", devcontainer -> () -> assertAll(
            () -> assertEquals(UserEnvProbe.interactiveShell, devcontainer.userEnvProbe(), "userEnvProbe")
        )),
        Map.entry("override-command.json", devcontainer -> () -> assertAll(
            () -> assertEquals(true, devcontainer.overrideCommand(), "overrideCommand")
        )),
        Map.entry("shutdown-action.json", devcontainer -> () -> assertAll(
            () -> assertEquals(ShutdownAction.none, devcontainer.shutdownAction(), "shutdownAction")
        )),
        Map.entry("shutdown-action-stop-compose.json", devcontainer -> () -> assertAll(
            () -> assertEquals(ShutdownAction.stopCompose, devcontainer.shutdownAction(), "shutdownAction")
        )),
        Map.entry("shutdown-action-stop-container.json", devcontainer -> () -> assertAll(
            () -> assertEquals(ShutdownAction.stopContainer, devcontainer.shutdownAction(), "shutdownAction")
        )),
        Map.entry("init.json", devcontainer -> () -> assertAll(
            () -> assertEquals(true, devcontainer.init(), "init")
        )),
        Map.entry("privileged.json", devcontainer -> () -> assertAll(
            () -> assertEquals(true, devcontainer.privileged(), "privileged")
        )),
        Map.entry("cap-add.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("SYS_PTRACE"), devcontainer.capAdd(), "capAdd")
        )),
        Map.entry("cap-add-empty.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of(), devcontainer.capAdd(), "capAdd")
        )),
        Map.entry("on-create-command.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("echo", "foo='bar'"), devcontainer.onCreateCommand().array(),
                "onCreateCommand")
        )),
        Map.entry("initialize-command.json", devcontainer -> () -> assertAll(
            () -> assertEquals("echo foo='bar'", devcontainer.initializeCommand().string(), "initializeCommand")
        )),
        Map.entry("wait-for-post-attach.json", devcontainer -> () -> assertAll(
            () -> assertEquals(WaitFor.postAttachCommand, devcontainer.waitFor(), "waitFor")
        )),
        Map.entry("wait-for-post-start.json", devcontainer -> () -> assertAll(
            () -> assertEquals(WaitFor.postStartCommand, devcontainer.waitFor(), "waitFor")
        )),
        Map.entry("wait-for-post-create.json", devcontainer -> () -> assertAll(
            () -> assertEquals(WaitFor.postCreateCommand, devcontainer.waitFor(), "waitFor")
        )),
        Map.entry("wait-for-update-content.json", devcontainer -> () -> assertAll(
            () -> assertEquals(WaitFor.updateContentCommand, devcontainer.waitFor(), "waitFor")
        )),
        Map.entry("wait-for-on-create.json", devcontainer -> () -> assertAll(
            () -> assertEquals(WaitFor.onCreateCommand, devcontainer.waitFor(), "waitFor")
        )),
        Map.entry("wait-for.json", devcontainer -> () -> assertAll(
            () -> assertEquals(WaitFor.initializeCommand, devcontainer.waitFor(), "waitFor")
        )),
        Map.entry("run-services.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("database", "proxy"), devcontainer.runServices(), "runServices")
        )),
        Map.entry("service.json", devcontainer -> () -> assertAll(
            () -> assertEquals("dev", devcontainer.service(), "service")
        )),
        Map.entry("docker-compose-file.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("docker-compose.yml"),
                devcontainer.dockerComposeFile(), "dockerComposeFile")
        )),
        Map.entry("run-args.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("--cap-add=SYS_PTRACE", "--security-opt", "seccomp=unconfined"),
                devcontainer.runArgs(), "runArgs")
        )),
        Map.entry("workspace-folder.json", devcontainer -> () -> assertAll(
            () -> assertEquals("/workspace", devcontainer.workspaceFolder(), "workspaceFolder")
        )),
        Map.entry("workspace-mount.json", devcontainer -> () -> assertAll(
            () -> assertEquals(
                "source=${localWorkspaceFolder}/sub-folder,target=/workspace,type=bind,consistency=cached",
                devcontainer.workspaceMount(), "workspaceMount")
        )),
        Map.entry("app-port.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("12345", "5432"), devcontainer.appPort(), "appPort")
        )),
        Map.entry("build.json", devcontainer -> () -> assertAll(
            () -> assertEquals("Containerfile", devcontainer.build().dockerfile(), "build.dockerfile"),
            () -> assertEquals(".", devcontainer.build().context(), "build.context"),
            () -> assertEquals("dev", devcontainer.build().target(), "build.target"),
            () -> assertEquals(Map.of("some", "value"), devcontainer.build().args(), "build.args"),
            () -> assertIterableEquals(List.of("some-cache:latest"), devcontainer.build().cacheFrom(),
                "build.cacheFrom")
        )),
        Map.entry("customizations.json", devcontainer -> () -> assertAll(
            () -> assertEquals(Map.of("vscode", Map.of("settings", Map.of(), "extensions", List.of())),
                devcontainer.customizations(), "customizations")
        )),
        Map.entry("override-feature-install-order.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(
                List.of("ghcr.io/devcontainers/features/common-utils", "ghcr.io/devcontainers/features/github-cli"),
                devcontainer.overrideFeatureInstallOrder(), "overrideFeatureInstallOrder")
        )),
        Map.entry("features.json", devcontainer -> () -> assertAll(
            () -> assertEquals(Map.of("ghcr.io/devcontainers/features/github-cli", Map.of()),
                devcontainer.features(), "features")
        )),
        Map.entry("mounts.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of(Map.of(
                    "source", "dind-var-lib-docker",
                    "target", "/var/lib/docker",
                    "type", "volume")),
                devcontainer.mounts(), "mounts")
        )),
        Map.entry("security-opt-empty.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of(), devcontainer.securityOpt(), "securityOpt")
        )),
        Map.entry("security-opt.json", devcontainer -> () -> assertAll(
            () -> assertIterableEquals(List.of("seccomp=unconfined"), devcontainer.securityOpt(), "securityOpt")
        ))
    );

    return testcases.entrySet().stream()
        .map(entry -> DynamicTest.dynamicTest(entry.getKey(), () ->
            entry.getValue().apply(parse(entry.getKey())).execute()));
  }

  private Devcontainer parse(final String testFile) throws IOException {
    return Devcontainer.parse(Paths.get("src/test/resources/").resolve(testFile));
  }

}
