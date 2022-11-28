/*
 * SPDX-FileCopyrightText: The devcontainer.java Authors
 * SPDX-License-Identifier: 0BSD
 */
module wtf.metio.devcontainer {

  requires com.fasterxml.jackson.databind;

  opens wtf.metio.devcontainer to com.fasterxml.jackson.databind;

  exports wtf.metio.devcontainer;

}
