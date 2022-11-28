<!--
SPDX-FileCopyrightText: The devcontainer.java Authors
SPDX-License-Identifier: 0BSD
 -->

# devcontainer.java

Java implementation for the [devcontainer](https://containers.dev/implementors/json_reference/) file specification.

## Usage

```java
import wtf.metio.devcontainer.Devcontainer;
import java.nio.file.Paths;

// parse JSON file
Devcontainer devcontainer = Devcontainer.parse(Paths.get("path/to/devcontainer.json"));

devcontainer.image(); // access properties
```
