<!--
SPDX-FileCopyrightText: The devcontainer.java Authors
SPDX-License-Identifier: 0BSD
 -->

# devcontainer.java [![Chat](https://img.shields.io/badge/matrix-%23talk.metio:matrix.org-brightgreen.svg?style=social&label=Matrix)](https://matrix.to/#/#talk.metio:matrix.org)

Java implementation for the [devcontainer](https://containers.dev/implementors/json_reference/) file specification.

## Usage

### Parsing

The `Devcontainer` class exposes methods to parse `java.io.File`, `java.nio.file.Path`, and `java.lang.String` values.

```java
import wtf.metio.devcontainer.Devcontainer;
import java.nio.file.Paths;

// parse JSON file
Devcontainer devcontainer = Devcontainer.parse(Paths.get("path/to/devcontainer.json"));

// parse JSON string
Devcontainer devcontainer = Devcontainer.parse("""
    {
        "image": "docker.io/someuser/someimage:someversion"
    }
""");


// access properties
String image = devcontainer.image();
```

### Building

The `Devcontainer` class is annotated with [record-builder](https://github.com/Randgalt/record-builder) annotations which allow you to create new `Devcontainer` instances like this:

```java
Devcontainer devcontainer = Devcontainer.builder()
        .image("docker.io/someuser/someimage:someversion")
        .create()
```

All records in this project support withers and return a new instance of themselves using the new value:

```java
Devcontainer devcontainer = ...
Devcontainer changed = devcontainer.withImage("quay.io/other/image:version");
```

### Integration

In order to use this library in your project, declare the following dependency:

```xml
<dependency>
    <groupId>wtf.metio.devcontainer</groupId>
    <artifactId>devcontainer.java</artifactId>
    <version>${devcontainer-version}</version>
</dependency>
```

Replace `${devcontainer-version}` with the [latest release](https://central.sonatype.com/artifact/wtf.metio.devcontainer/devcontainer.java).

## License

```
Permission to use, copy, modify, and/or distribute this software for any
purpose with or without fee is hereby granted.

THE SOFTWARE IS PROVIDED "AS IS" AND THE AUTHOR DISCLAIMS ALL WARRANTIES WITH
REGARD TO THIS SOFTWARE INCLUDING ALL IMPLIED WARRANTIES OF MERCHANTABILITY AND
FITNESS. IN NO EVENT SHALL THE AUTHOR BE LIABLE FOR ANY SPECIAL, DIRECT,
INDIRECT, OR CONSEQUENTIAL DAMAGES OR ANY DAMAGES WHATSOEVER RESULTING FROM
LOSS OF USE, DATA OR PROFITS, WHETHER IN AN ACTION OF CONTRACT, NEGLIGENCE OR
OTHER TORTIOUS ACTION, ARISING OUT OF OR IN CONNECTION WITH THE USE OR
PERFORMANCE OF THIS SOFTWARE.
```

## Mirrors

- https://github.com/metio/devcontainer.java
- https://gitlab.com/metio.wtf/devcontainer.java
- https://bitbucket.org/metio-wtf/devcontainer.java
- https://codeberg.org/metio.wtf/devcontainer.java