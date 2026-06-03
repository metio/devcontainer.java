<!--
SPDX-FileCopyrightText: The devcontainer.java Authors
SPDX-License-Identifier: 0BSD
 -->

# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## What this is

A single-purpose Java library that parses and builds [devcontainer.json](https://containers.dev/implementors/json_reference/) files. Published to Maven Central as `wtf.metio.devcontainer:devcontainer.java`. No CLI, no runtime — it is a model + Jackson glue.

## Commands

JDK 25 (`global.jdkVersion` in `pom.xml`), Maven. Inherits most config from the `wtf.metio.maven:maven-parent` POM. That parent does **not** manage third-party dependency versions, so this POM pins them itself: Jackson and JUnit via imported BOMs in `<dependencyManagement>`, record-builder via the `version.record-builder` property (shared between the dependency and the annotation-processor path — they must move together).

- Build & test: `mvn --batch-mode verify`
- Native-compatibility check (what CI's PR check runs — the GraalVM `native` profile): `mvn --batch-mode --activate-profiles=native verify`
- Single test class: `mvn test -Dtest=DevcontainerParsingTest`

The local machine has no JDK/Maven installed; run builds through `ilo` (see global instructions) or in the GraalVM container CI uses.

## Architecture

The schema is modeled as a graph of Java **records**, rooted at `Devcontainer`. Supporting records (`Build`, `Command`, `HostRequirements`, `PortAttribute`) and enums (`OnAutoForward`, `Protocol`, `ShutdownAction`, `UserEnvProbe`, `WaitFor`) cover the nested and constrained fields. Records carry the spec docs as Javadoc on each component.

- **record-builder annotation processor** (`io.soabase.record-builder`, `provided` scope) generates a `<Record>Builder` for every `@RecordBuilder` record at compile time. Records implement `<Record>Builder.With` to get wither methods. The build method is renamed to `create()` via `@RecordBuilder.Options`. These `*Builder` classes do not exist until you compile — don't go looking for them in source.
- **Jackson 3** (`tools.jackson.*` packages, `tools.jackson` BOM coordinates — not the Jackson 2 `com.fasterxml.jackson.*`). The mapper is immutable and built via `JsonMapper.builder()...build()`; there are no `enable()`/`disable()` mutators on an instance. Jackson 3 throws unchecked `JacksonException`, so the `parse(...)` methods declare no checked `IOException`.
- **Parsing** lives entirely in `Devcontainer.parse(...)` overloads (Path/File/String), backed by `defaultObjectMapper()`: a `JsonMapper` that disables `FAIL_ON_UNKNOWN_PROPERTIES` (forward-compat with spec additions) and enables `ACCEPT_SINGLE_VALUE_AS_ARRAY` (the spec lets array fields appear as a single scalar).
- **`Command` is a polymorphic field.** In the spec a lifecycle command may be a string, a string array, or an object of named commands. `Command` holds all three (`string`, `array`, `object`) with at most one non-null, populated by `CommandDeserializer` (a `StdDeserializer` wired via `@JsonDeserialize`). The `object` variant recurses into more `Command`s.
- **JPMS:** `module-info.java` opens the package to `tools.jackson.databind` (needed for reflective deserialization) and requires record-builder + java.compiler as `static` (compile-only).

## GraalVM native image — keep reflect-config.json in sync

The library's contract is that downstream consumers (e.g. ilo) can native-compile an app that depends on it. `src/main/resources/META-INF/native-image/.../reflect-config.json` registers every record and enum for reflection so Jackson works in a native image. **When you add a new record or enum type to the model, add a corresponding entry to this file**, or native-image deserialization will fail at runtime (not compile time) in consumers.

The `native` profile verifies this contract by mimicking a consumer: it adds `src/native/java` as a source root (so the stand-in never ships in the published jar), builds `NativeImageSmokeTest` into a native image via the plugin's `compile-no-fork` goal, then runs it (`exec`). Building exercises `reflect-config.json`; running surfaces missing runtime reflection metadata. When you add a model field worth covering, exercise it in that smoke main. It deliberately does **not** native-compile the JUnit test suite — that would only test JUnit's own native compatibility, not the library's.

## Tests are data-driven

`DevcontainerParsingTest` is a `@TestFactory` that pairs JSON fixtures in `src/test/resources/*.json` with assertion lambdas. To test a new field or parsing case: add a fixture file and a `Map.entry("fixture.json", devcontainer -> ...)` with the expected values. `DevcontainerBuilderTest` covers the generated builders/withers.

## Conventions

- Every file carries an SPDX header; the project license is **0BSD**. Match the existing header when adding files.
- DCO sign-off is required: commit with `git commit --signoff`.
- Releases are **calendar-versioned and automated** (`.github/workflows/release.yml`, Friday cron, version = `date +'%Y.%-m.%-d'`). A release only happens when commits since the last tag touched `src/main/java` or `pom.xml`. The POM version stays `0.0.0-SNAPSHOT` in the tree; the real version is injected at release time.
