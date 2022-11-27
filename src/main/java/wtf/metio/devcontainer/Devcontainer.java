/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import java.util.List;
import java.util.Map;

/**
 * @param name                        A name for the dev container displayed in the UI.
 * @param forwardPorts                An array of port numbers or "host:port" values (e.g. [3000, "db:5432"]) that
 *                                    should always be forwarded from inside the primary container to the local machine
 *                                    (including on the web). The property is most useful for forwarding ports that
 *                                    cannot be auto-forwarded because the related process that starts before the
 *                                    devcontainer.json supporting service / tool connects or for forwarding a service
 *                                    not in the primary container in Docker Compose scenarios (e.g. "db:5432").
 *                                    Defaults to [].
 * @param portsAttributes             Object that maps a port number, "host:port" value, range, or regular expression to
 *                                    a set of default options. See port attributes for available options. For example:
 *                                    "portsAttributes": {"3000": {"label": "Application port"}}
 * @param otherPortsAttributes        Default options for ports, port ranges, and hosts that aren’t configured using
 *                                    portsAttributes. See port attributes for available options. For example:
 *                                    "otherPortsAttributes": {"onAutoForward": "silent"}
 * @param remoteEnv                   A set of name-value pairs that sets or overrides environment variables for the
 *                                    devcontainer.json supporting service / tool (or sub-processes like terminals) but
 *                                    not the container as a whole. Environment and pre-defined variables may be
 *                                    referenced in the values.
 * @param remoteUser                  Overrides the user that devcontainer.json supporting services tools / runs as in
 *                                    the container (along with sub-processes like terminals, tasks, or debugging). Does
 *                                    not change the user the container as a whole runs as which can be set using
 *                                    containerUser. Defaults to the user the container as a whole is running as (often
 *                                    root).
 * @param containerEnv                A set of name-value pairs that sets or overrides environment variables for the
 *                                    container. Environment and pre-defined variables may be referenced in the values.
 *                                    For example: "containerEnv": { "MY_VARIABLE": "${localEnv:MY_VARIABLE}" } If you
 *                                    want to reference an existing container variable while setting this one (like
 *                                    updating the PATH), use remoteEnv instead.
 * @param containerUser               Overrides the user for all operations run as inside the container. Defaults to
 *                                    either root or the last USER instruction in the related Dockerfile used to create
 *                                    the image. If you want any connected tools or related processes to use a different
 *                                    user than the one for the container, see remoteUser.
 * @param updateRemoteUserUID         On Linux, if containerUser or remoteUser is specified, the user’s UID/GID will be
 *                                    updated to match the local user’s UID/GID to avoid permission problems with bind
 *                                    mounts. Defaults to true.
 * @param userEnvProbe                Indicates the type of shell to use to “probe” for user environment variables to
 *                                    include in devcontainer.json supporting services’ / tools’ processes: none,
 *                                    interactiveShell, loginShell, or loginInteractiveShell (default). The specific
 *                                    shell used is based on the default shell for the user (typically bash). For
 *                                    example, bash interactive shells will typically include variables set in
 *                                    /etc/bash.bashrc and ~/.bashrc while login shells usually include variables from
 *                                    /etc/profile and ~/.profile. Setting this property to loginInteractiveShell will
 *                                    get variables from all four files.
 * @param overrideCommand             Tells devcontainer.json supporting services / tools whether they should run
 *                                    /bin/sh -c "while sleep 1000; do :; done" when starting the container instead of
 *                                    the container’s default command (since the container can shut down if the default
 *                                    command fails). Set to false if the default command must run for the container to
 *                                    function properly. Defaults to true for when using an image Dockerfile and false
 *                                    when referencing a Docker Compose file.
 * @param shutdownAction              Indicates whether devcontainer.json supporting tools should stop the containers
 *                                    when the related tool window is closed / shut down. Values are none, stopContainer
 *                                    (default for image or Dockerfile), and stopCompose (default for Docker Compose).
 * @param init                        Defaults to false. Cross-orchestrator way to indicate whether the tini init
 *                                    process should be used to help deal with zombie processes.
 * @param privileged                  Defaults to false. Cross-orchestrator way to cause the container to run in
 *                                    priviledged mode (--priviledged). Required for things like Docker-in-Docker, but
 *                                    has security implications particularly when running directly on Linux.
 * @param capAdd                      Defaults to []. Cross-orchestrator way to add capabilities typically disabled for
 *                                    a container. Most often used to add the ptrace capability required to debug
 *                                    languages like C++, Go, and Rust. For example: "capAdd": ["SYS_PTRACE"]
 * @param securityOpt                 Defaults to []. Cross-orchestrator way to set container security options. For
 *                                    example: "securityOpt": [ "seccomp=unconfined" ]
 * @param mounts                      Defaults to unset. Cross-orchestrator way to add additional mounts to a container.
 *                                    Each value is a string that accepts the same values as the Docker CLI --mount
 *                                    flag. Environment and pre-defined variables may be referenced in the value. For
 *                                    example: "mounts": [{ "source": "dind-var-lib-docker", "target":
 *                                    "/var/lib/docker", "type": "volume" }]
 * @param features                    An object of Dev Container Feature IDs and related options to be added into your
 *                                    primary container. The specific options that are available varies by feature, so
 *                                    see its documentation for additional details. For example: "features": {
 *                                    "ghcr.io/devcontainers/features/github-cli": {} }
 * @param overrideFeatureInstallOrder By default, Features will attempt to automatically set the order they are
 *                                    installed based on a installsAfter property within each of them. This property
 *                                    allows you to override the Feature install order when needed. For example:
 *                                    "overrideFeatureInstallorder": [ "ghcr.io/devcontainers/features/common-utils",
 *                                    "ghcr.io/devcontainers/features/github-cli" ]
 * @param customizations              Product specific properties, defined in supporting tools
 * @param image                       Required when using an image. The name of an image in a container registry
 *                                    (DockerHub, GitHub Container Registry, Azure Container Registry) that
 *                                    devcontainer.json supporting services / tools should use to create the dev
 *                                    container.
 * @param appPort                     In most cases, we recommend using the new forwardPorts property. This property
 *                                    accepts a port or array of ports that should be published locally when the
 *                                    container is running.Unlike forwardPorts, your application may need to listen on
 *                                    all interfaces (0.0.0.0) not just localhost for it to be available externally.
 *                                    Defaults to []. Learn more about publishing vs forwarding ports here. Note that
 *                                    the array syntax will execute the command without a shell. You can learn more
 *                                    about formatting string vs array properties.
 * @param workspaceMount              Requires workspaceFolder be set as well. Overrides the default local mount point
 *                                    for the workspace when the container is created. Supports the same values as the
 *                                    Docker CLI --mount flag. Environment and pre-defined variables may be referenced
 *                                    in the value. For example: "workspaceMount":
 *                                    "source=${localWorkspaceFolder}/sub-folder,target=/workspace,type=bind,consistency=cached",
 *                                    "workspaceFolder": "/workspace"
 * @param workspaceFolder             Requires workspaceMount be set. Sets the default path that devcontainer.json
 *                                    supporting services / tools should open when connecting to the container. Defaults
 *                                    to the automatic source code mount location.
 * @param runArgs                     An array of Docker CLI arguments that should be used when running the container.
 *                                    Defaults to []. For example, this allows ptrace based debuggers like C++ to work
 *                                    in the container: "runArgs": [ "--cap-add=SYS_PTRACE", "--security-opt",
 *                                    "seccomp=unconfined" ] .
 * @param dockerComposeFile           Required when using Docker Compose. Path or an ordered list of paths to Docker
 *                                    Compose files relative to the devcontainer.json file. Using an array is useful
 *                                    when extending your Docker Compose configuration. The order of the array matters
 *                                    since the contents of later files can override values set in previous ones. The
 *                                    default .env file is picked up from the root of the project, but you can use
 *                                    env_file in your Docker Compose file to specify an alternate location. Note that
 *                                    the array syntax will execute the command without a shell. You can learn more
 *                                    about formatting string vs array properties.
 * @param service                     Required when using Docker Compose. The name of the service devcontainer.json
 *                                    supporting services / tools should connect to once running.
 * @param runServices                 An array of services in your Docker Compose configuration that should be started
 *                                    by devcontainer.json supporting services / tools. These will also be stopped when
 *                                    you disconnect unless "shutdownAction" is "none". Defaults to all services.
 * @param initializeCommand           A command string or list of command arguments to run on the host machine before
 *                                    the container is created. .
 *                                    <p>
 *                                    ⚠️ The command is run wherever the source code is located on the host. For cloud
 *                                    services, this is in the cloud.
 *                                    <p>
 *                                    Note that the array syntax will execute the command without a shell. You can learn
 *                                    more about formatting string vs array vs object properties.
 * @param onCreateCommand             This command is the first of three (along with updateContentCommand and
 *                                    postCreateCommand) that finalizes container setup when a dev container is created.
 *                                    It and subsequent commands execute inside the container immediately after it has
 *                                    started for the first time.
 *                                    <p>
 *                                    Cloud services can use this command when caching or prebuilding a container. This
 *                                    means that it will not typically have access to user-scoped assets or secrets.
 *                                    <p>
 *                                    Note that the array syntax will execute the command without a shell. You can learn
 *                                    more about formatting string vs array vs object properties.
 * @param updateContentCommand        This command is the second of three that finalizes container setup when a dev
 *                                    container is created. It executes inside the container after onCreateCommand
 *                                    whenever new content is available in the source tree during the creation process.
 *                                    <p>
 *                                    It will execute at least once, but cloud services will also periodically execute
 *                                    the command to refresh cached or prebuilt containers. Like cloud services using
 *                                    onCreateCommand, it can only take advantage of repository and org scoped secrets
 *                                    or permissions.
 *                                    <p>
 *                                    Note that the array syntax will execute the command without a shell. You can learn
 *                                    more about formatting string vs array vs object properties.
 * @param postCreateCommand           This command is the last of three that finalizes container setup when a dev
 *                                    container is created. It happens after updateContentCommand and once the dev
 *                                    container has been assigned to a user for the first time.
 *                                    <p>
 *                                    Cloud services can use this command to take advantage of user specific secrets and
 *                                    permissions.
 *                                    <p>
 *                                    Note that the array syntax will execute the command without a shell. You can learn
 *                                    more about formatting string vs array vs object properties.
 * @param postStartCommand            A command to run each time the container is successfully started.
 *                                    <p>
 *                                    Note that the array syntax will execute the command without a shell. You can learn
 *                                    more about formatting string vs array vs object properties.
 * @param postAttachCommand           A command to run each time a tool has successfully attached to the container.
 *                                    <p>
 *                                    Note that the array syntax will execute the command without a shell. You can learn
 *                                    more about formatting string vs array vs object properties.
 * @param waitFor                     An enum that specifies the command any tool should wait for before connecting.
 *                                    Defaults to updateContentCommand. This allows you to use onCreateCommand or
 *                                    updateContentCommand for steps that must happen before devcontainer.json
 *                                    supporting tools connect while still using postCreateCommand for steps that can
 *                                    happen behind the scenes afterwards.
 * @see <a href="https://code.visualstudio.com/docs/remote/devcontainerjson-reference">devcontainer reference</a>
 */
public record Devcontainer(
    String name,
    List<String> forwardPorts,
    Map<String, PortAttribute> portsAttributes,
    PortAttribute otherPortsAttributes,
    Map<String, String> remoteEnv,
    String remoteUser,
    Map<String, String> containerEnv,
    String containerUser,
    Boolean updateRemoteUserUID,
    UserEnvProbe userEnvProbe,
    Boolean overrideCommand,
    ShutdownAction shutdownAction,
    Boolean init,
    Boolean privileged,
    List<String> capAdd,
    List<String> securityOpt,
    List<Map<String, String>> mounts,
    Map<String, Map<String, String>> features,
    List<String> overrideFeatureInstallOrder,
    Map<String, Map<String, Object>> customizations,
    String image,
    Build build,
    List<String> appPort,
    String workspaceMount,
    String workspaceFolder,
    List<String> runArgs,
    List<String> dockerComposeFile,
    String service,
    List<String> runServices,
    Command initializeCommand,
    Command onCreateCommand,
    Command updateContentCommand,
    Command postCreateCommand,
    Command postStartCommand,
    Command postAttachCommand,
    WaitFor waitFor,
    HostRequirements hostRequirements) {

}
