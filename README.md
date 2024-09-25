# extended-collections
*Specialized Data Structures*

// TODO maven central
// TODO tests passing
![pages-build-deployment](https://github.com/aeckar/extended-collections/actions/workflows/pages/pages-build-deployment/badge.svg?branch=master)
[![License: MIT](https://img.shields.io/badge/License-MIT-yellow.svg)](https://opensource.org/licenses/MIT)
![Maintained?: yes](https://img.shields.io/badge/Maintained%3F-yes-green.svg)

---

```kotlin
commonMain {
    implementation("io.github.aeckar:extended-collections:1.0.0")
}
```

Started as a utility library for my [modular-parsers](https://github.com/aeckar/modular-parsers) project,
this library builds upon the Kotlin Collections Framework by providing:

- Multi-sets
- Rich iterators
- Read-only views over collections
- Linked lists & trees, and
- Resizable lists of unboxed values

among other utilities. The overall structure of the API is designed to be similar to that of the standard library,
allowing for seamless integration into existing codebases.
Additionally, the core library module holds no dependencies and is designed with performance in mind.
The `io` module extends the functionality of the library by providing iterators over
[kotlinx.io.RawSource](https://kotlin.github.io/kotlinx-io/kotlinx-io-core/kotlinx.io/-raw-source/index.html).

The full online documentation can be found [here](https://aeckar.github.io/extended-collections/).