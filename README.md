# mopro-kotlin-package

A Kotlin/Android library for generating and verifying zero-knowledge proofs (ZKPs) using native Rust code via UniFFI and JNI. This package provides a simple interface to interact with proof systems such as Circom and Halo2, supporting multiple proof libraries (e.g., Arkworks, Rapidsnark).

## Features
- Generate and verify Circom and Halo2 zero-knowledge proofs from Kotlin/Android.
- Uses native Rust bindings for performance and security.
- Supports multiple proof libraries: Arkworks, Rapidsnark.

## Getting mopro via JitPack

To get this library from GitHub using [JitPack](https://jitpack.io/#zkmopro/mopro-kotlin-package):

**Step 1.** Add the JitPack repository to your `settings.gradle.kts` at the end of repositories:
```kotlin
dependencyResolutionManagement {
    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        mavenCentral()
        maven { url = uri("https://jitpack.io") }
    }
}
```

**Step 2.** Add the dependency to your `build.gradle.kts`:
```kotlin
  dependencies {
      implementation("com.github.zkmopro:mopro-kotlin-package:Tag")
  }
```
Replace `Tag` with the desired release version, e.g. `v0.1.0`. See the [JitPack page](https://jitpack.io/#zkmopro/mopro-kotlin-package) for available versions.

**Note:**
Comment out the following Uniffi dependencies in your build file to avoid duplicate classes error:
```kotlin
  // // Uniffi
  // implementation("net.java.dev.jna:jna:5.13.0@aar")
  // implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.6.4")
```

## Usage Example
```kotlin
import uniffi.mopro.generateCircomProof
import uniffi.mopro.verifyCircomProof
import uniffi.mopro.ProofLib

val inputStr = "{\"b\":[\"5\"],\"a\":[\"3\"]}"
val zkeyPath = "/path/to/multiplier2_final.zkey"
val proof = generateCircomProof(zkeyPath, inputStr, ProofLib.ARKWORKS)
val isValid = verifyCircomProof(zkeyPath, proof, ProofLib.ARKWORKS)
```

**For creating an Android app with the mopro template, please refer to the official guide:**
[https://zkmopro.org/docs/getting-started#4-create-templates](https://zkmopro.org/docs/getting-started#4-create-templates)
