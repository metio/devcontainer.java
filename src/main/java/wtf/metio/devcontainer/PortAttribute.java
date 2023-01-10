/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
package wtf.metio.devcontainer;

import io.soabase.recordbuilder.core.RecordBuilder;

/**
 * @param label            Display name for the port in the ports view. Defaults to not set.
 * @param protocol         Controls protocol handling for forwarded ports. When not set, the port is assumed to be a raw
 *                         TCP stream which, if forwarded to localhost, supports any number of protocols. However, if
 *                         the port is forwarded to a web URL (e.g. from a cloud service on the web), only HTTP ports in
 *                         the container are supported. Setting this property to https alters handling by ignoring any
 *                         SSL/TLS certificates present when communicating on the port and using the correct certificate
 *                         for the forwarded URL instead (e.g https://*.githubpreview.dev). If set to http, processing
 *                         is the same as if the protocol is not set. Defaults to not set.
 * @param onAutoForward    Controls what should happen when a port is auto-forwarded once you’ve connected to the
 *                         container. notify is the default, and a notification will appear when the port is
 *                         auto-forwarded. If set to openBrowser, the port will be opened in the system’s default
 *                         browser. openPreview will open the URL in devcontainer.json supporting services’ / tools’
 *                         embedded preview browser. A value of silent will forward the port, but take no further
 *                         action. A value of ignore means that this port should not be auto-forwarded at all.
 * @param requireLocalPort Dictates when port forwarding is required to map the port in the container to the same port
 *                         locally or not. If set to false, the devcontainer.json supporting services / tools will
 *                         attempt to use the specified port forward to localhost, and silently map to a different one
 *                         if it is unavailable. If set to true, you will be notified if it is not possible to use the
 *                         same port. Defaults to false.
 * @param elevateIfNeeded  Forwarding low ports like 22, 80, or 443 to localhost on the same port from devcontainer.json
 *                         supporting services / tools may require elevated permissions on certain operating systems.
 *                         Setting this property to true will automatically try to elevate the devcontainer.json
 *                         supporting tool’s permissions in this situation. Defaults to false.
 * @see <a href="https://containers.dev/implementors/json_reference/#port-attributes">schema reference</a>
 */
@RecordBuilder
@RecordBuilder.Options(buildMethodName = "create")
public record PortAttribute(
    String label,
    Protocol protocol,
    OnAutoForward onAutoForward,
    Boolean requireLocalPort,
    Boolean elevateIfNeeded) implements PortAttributeBuilder.With {

    public static PortAttributeBuilder builder() {
        return PortAttributeBuilder.builder();
    }

}
