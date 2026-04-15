# TDR Common Utils

Common libraries and shared code used across [Transfer Digital Records (TDR)](https://github.com/nationalarchives) services.

## Overview

This is a multi-module [sbt](https://www.scala-sbt.org/) project written in Scala 2.13. It provides reusable utilities that are published to Maven Central under the `uk.gov.nationalarchives` organisation for consumption by other TDR services.

## Modules

| Module | Artifact | Description |
|---|---|---|
| [authorisation](#authorisation) | `tdr-authorisation` | Authorisation logic for TDR transfers |
| [objectkeycontext](#object-key-context) | `tdr-object-key-context` | S3 object key parsing and context extraction |
| [serviceinputs](#service-inputs) | `tdr-service-inputs` | Input models for triggering TDR services |
| [statuses](#statuses) | `tdr-statuses` | Status type and value definitions for TDR workflows |

### Authorisation

Provides a generic `Authorisation` trait and a concrete `ConsignmentAuthorisation` implementation that checks whether a user has access to a given consignment via the TDR GraphQL API.

**Key types:**

- `Authorisation[T]` — trait defining `hasAccess(input: T): IO[AuthorisationResult]`
- `ConsignmentAuthorisation` — checks consignment access using the TDR GraphQL client
- `AuthorisationResult` — sealed trait with `Allow` / `Deny` case objects

**Dependencies:** Cats Effect, TDR GraphQL Client, TDR Auth Utils

### Object Key Context

Parses AWS S3 object keys into structured context objects. Supports two key formats:

- **Default key:** `{consignmentId}/{object}`
- **Upload key:** `{userId}/{assetSource}/{consignmentId}/{objectCategory}/{object}`

**Key types:**

- `ObjectKeyContext` — case class containing parsed key fields (`userId`, `transferId`, `assetSource`, `category`, `objectType`, `objectName`)
- `ObjectTypes` — `Csv`, `Error`, `Metadata`, `Record`
- `ObjectCategories` — `DryRunMetadata`, `Metadata`, `Records`
- `AssetSources` — `Droid`, `HardDrive`, `NetworkDrive`, `SharePoint`

### Service Inputs

Defines case classes for inputs used to trigger TDR backend services (e.g. Step Functions), with [Circe](https://circe.github.io/circe/) JSON encoders.

**Key types:**

- `BackendChecksInput` — triggers backend file checks (`consignmentId`, `s3SourceBucketPrefix`)
- `ExportInput` — triggers export (`consignmentId`)
- `MetadataValidationInput` — triggers metadata validation (`consignmentId`, `fileName`)

**Dependencies:** Circe Core, Circe Generic

### Statuses

Defines the status types and values used throughout the TDR workflow.

**Status types:** `Series`, `Upload`, `TransferAgreement`, `ClientChecks`, `ServerAntivirus`, `ServerChecksum`, `ServerFFID`, `ServerRedaction`, `ConfirmTransfer`, `Export`, `DraftMetadata`, `MetadataReview`, `DraftMetadataUpload`

**Status values:** `Completed`, `CompletedWithIssues`, `InProgress`, `Failed`

**Metadata review statuses:** `Requested`, `Rejected`, `Approved`, `Completed`

The `MetadataReviewStatus` object provides a dedicated enum for metadata review workflow states:

```scala
import uk.gov.nationalarchives.tdr.common.utils.statuses.MetadataReviewStatus

MetadataReviewStatus.Requested.value  // "Requested"
MetadataReviewStatus.Rejected.value   // "Rejected"
MetadataReviewStatus.Approved.value   // "Approved"
MetadataReviewStatus.Completed.value  // "Completed"

// Parse from a string:
MetadataReviewStatus.MetadataReviewStatus("Approved")
```

**Metadata review log actions:** `Submission`, `Rejection`, `Approval`, `Confirmation`

The `MetadataReviewLogAction` object provides an enum for metadata review log actions. Each action has a `reviewStatus` field that links it to the corresponding `MetadataReviewStatus`:

| Action | `reviewStatus` |
|---|---|
| `Submission` | `MetadataReviewStatus.Requested` |
| `Rejection` | `MetadataReviewStatus.Rejected` |
| `Approval` | `MetadataReviewStatus.Approved` |
| `Confirmation` | `MetadataReviewStatus.Completed` |

```scala
import uk.gov.nationalarchives.tdr.common.utils.statuses.MetadataReviewLogAction

val action = MetadataReviewLogAction.MetadataReviewLogAction("Submission")
action.value                // "Submission"
action.reviewStatus         // MetadataReviewStatus.Requested
action.reviewStatus.value   // "Requested"
```

## Prerequisites

- **Java** 11+
- **sbt** 1.11.7+ (defined in `project/build.properties`)

## Getting Started

### Build

```bash
sbt compile
```

### Test

```bash
sbt test
```

### Publish Snapshot Locally

To create a snapshot version locally for use with local development:

```bash
sbt publishLocal
```

The snapshot version can then be imported into local development environments.

## Using the Libraries

Add the relevant module as a dependency in your `build.sbt`:

```scala
libraryDependencies += "uk.gov.nationalarchives" %% "tdr-authorisation"     % "<version>"
libraryDependencies += "uk.gov.nationalarchives" %% "tdr-object-key-context" % "<version>"
libraryDependencies += "uk.gov.nationalarchives" %% "tdr-service-inputs"    % "<version>"
libraryDependencies += "uk.gov.nationalarchives" %% "tdr-statuses"          % "<version>"
```

## Release Process

Releases are automated via GitHub Actions. When a pull request is merged to `main`, the pipeline will:

1. Check for snapshot dependencies
2. Clean and run tests
3. Set and commit the release version
4. Tag the release
5. Publish signed artefacts
6. Release to Maven Central via Sonatype
7. Set and commit the next snapshot version
8. Push changes

## License

This project is licensed under the [MIT License](LICENSE).
