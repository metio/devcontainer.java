/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import java.util.List;
import java.util.Map;

/**
 * @param dockerfile Required when using a Dockerfile. The location of a Dockerfile that defines the contents of the
 *                   container. The path is relative to the devcontainer.json file.
 * @param context    Path that the Docker build should be run from relative to devcontainer.json. For example, a value
 *                   of ".." would allow you to reference content in sibling directories. Defaults to ".".
 * @param args       A set of name-value pairs containing Docker image build arguments that should be passed when
 *                   building a Dockerfile. Environment and pre-defined variables may be referenced in the values.
 *                   Defaults to not set. For example: "build": { "args": { "MYARG": "MYVALUE", "MYARGFROMENVVAR":
 *                   "${localEnv:VARIABLE_NAME}" } }
 * @param target     A string that specifies a Docker image build target that should be passed when building a
 *                   Dockerfile. Defaults to not set. For example: "build": { "target": "development" }
 * @param cacheFrom  A string or array of strings that specify one or more images to use as caches when building the
 *                   image. Cached image identifiers are passed to the docker build command with --cache-from. Note that
 *                   the array syntax will execute the command without a shell. You can learn more about formatting
 *                   string vs array properties.
 */
public record Build(
    String dockerfile,
    String context,
    Map<String, String> args,
    String target,
    List<String> cacheFrom) {

}
