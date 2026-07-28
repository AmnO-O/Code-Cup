# Restore Build and Suppress AGP Warning

Removing `android.disallowKotlinSourceSets=false` caused a build error because the project uses KSP (for Room), and KSP currently adds generated files to Kotlin source sets using a DSL that is disallowed by default in AGP 9.x's "built-in Kotlin" mode.

To fix the build while still hiding the experimental warning, we need to restore the flag and add a suppression property as suggested by the original AGP warning.

## Proposed Changes

### Configuration

#### [MODIFY] [gradle.properties](file:///C:/Users/LAPTOP_CUA_NAM/AndroidStudioProjects/Code-Cup/gradle.properties)
- Restore `android.disallowKotlinSourceSets=false`.
- Add `android.sync.suppressAgpWarnings=UNSUPPORTED_PROJECT_OPTION_USE` to hide the experimental warning.

## Verification Plan

### Automated Tests
- Run `gradle build` to ensure the project compiles successfully.
- Verify that both the build error and the experimental warning are gone.
