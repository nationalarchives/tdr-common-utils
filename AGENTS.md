# AGENTS.md

## Project Overview

Multi-module Scala 2.13 sbt library published to Maven Central under `uk.gov.nationalarchives`. Provides shared utilities consumed by other [TDR (Transfer Digital Records)](https://github.com/nationalarchives) services. This is a **library-only** project — there is no runnable application.

## Architecture

Four independent sub-modules aggregated by a root project (`build.sbt`). The root project has `publish / skip := true`; only sub-modules are published.

| Module dir | Artifact | Purpose |
|---|---|---|
| `authorisation/` | `tdr-authorisation` | Checks consignment access via TDR GraphQL API (Cats Effect `IO`) |
| `objectkeycontext/` | `tdr-object-key-context` | Parses S3 object keys into structured context (pure Scala, no deps) |
| `serviceinputs/` | `tdr-service-inputs` | Case classes + Circe JSON encoders for Step Function triggers |
| `statuses/` | `tdr-statuses` | Sealed-trait enums for workflow status types and values (pure Scala) |

Dependencies are centralised in `project/Dependencies.scala`. Versions for TDR internal libs (`tdr-generated-graphql`, `tdr-graphql-client`, `tdr-auth-utils`) change frequently.

## Build & Test

```bash
sbt compile        # compile all modules
sbt test           # run all tests
sbt authorisation/test  # run tests for a single module
sbt publishLocal   # publish SNAPSHOT jars to ~/.ivy2/local
```

Current version is in `version.sbt`. Releases use `sbt release` (sbt-release plugin) which tags, signs (sbt-pgp), and publishes to Maven Central via Sonatype.

## Code Conventions

### Sealed-trait enum pattern
Domain enumerations use a consistent pattern: a `sealed trait` with an `id: String` field, `case object` variants, and a `toXxx(s: String): Xxx` factory that throws `RuntimeException` on unknown input. Follow this exactly when adding new values.

```scala
// Example: ObjectTypes.scala
sealed trait ObjectType { val id: String }
case object Csv extends ObjectType { val id: String = "csv" }
def toObjectType(s: String): ObjectType = s match { ... case _ => throw new RuntimeException(...) }
```

This pattern is used in `ObjectTypes`, `ObjectCategories`, `AssetSources`, `StatusTypes`, and `StatusValues`.

### Package structure
All source lives under `uk.gov.nationalarchives.tdr.common.utils.<module>`. Mirror the module directory name in the package (e.g. `objectkeycontext` → `.objectkeycontext`).

### Authorisation module
`Authorisation[T]` is a generic trait; concrete implementations (e.g. `ConsignmentAuthorisation`) take a `GraphQLClient` and implicit `SttpBackend`. The `hasAccess` method returns `IO[AuthorisationResult]` (sealed trait: `Allow`/`Deny`).

### Service inputs module
Input case classes extend `StepFunctionInput` and derive Circe encoders via `deriveEncoder` (semiauto). When adding a new input, declare the implicit encoder **before** the case class inside the `Inputs` object.

## Testing Patterns

- **Test framework:** ScalaTest with Matchers. Most modules use `AnyWordSpec`; `authorisation` uses `AnyFlatSpec` with `TableDrivenPropertyChecks`.
- **Mocking:** `mockito-scala` (`org.mockito.MockitoSugar`).
- **HTTP stubs:** `authorisation` tests use WireMock (`WireMockServer` on port 9001) with JSON fixtures in `authorisation/src/test/resources/json/`.
- **Cats Effect tests:** Use `unsafeRunSync()` to unwrap `IO` in tests.
- Tests for enum modules verify every variant's field values **and** the `toXxx` factory (including the error case for unknown input).

## Key Files

- `build.sbt` — module definitions and release process
- `project/Dependencies.scala` — all dependency declarations
- `version.sbt` — current SNAPSHOT version
- `authorisation/src/test/.../AuthorisationSpecUtils.scala` — shared WireMock test base class

