# Compose Template

This template repository provides a quick start for creating new Android apps using [Jetpack Compose](https://developer.android.com/jetpack/compose) as the UI framework and following the [MVVM architecture pattern](https://developer.android.com/topic/architecture).

It includes the following popular libraries:

- [Hilt](https://dagger.dev/hilt) - Hilt is a dependency injection library for Android that reduces the boilerplate of doing manual dependency injection in your project.
- [Room](https://developer.android.com/training/data-storage/room) - Room persistence library provides an abstraction layer over SQLite to allow fluent database access while harnessing the full power of SQLite.
- [Retrofit](https://github.com/square/retrofit) - A type-safe HTTP client for Android and the JVM.
- [Moshi](https://github.com/square/moshi) - A modern JSON library for Kotlin and Java.
- [Coil](https://github.com/coil-kt/coil) - Image loading for Android backed by Kotlin Coroutines.
- [Flower](https://github.com/hadiyarajesh/flower) - Flower simplifies networking and database caching on Android/Multiplatform.

## How to use
To use this template, simply click on the **Use this template** button at the top (or fork the repository) and start building your app on top of it. 
Make sure to update the package name and other app-specific details before building and deploying your app.

## Annotation Processing
This project uses [Kotlin Symbol Processing (KSP)](https://kotlinlang.org/docs/ksp-overview.html) for annotation processing, which provides faster build times compared to [KAPT](https://kotlinlang.org/docs/kapt.html). ~~However, some dependencies (like Hilt) may not support KSP yet, which is why KAPT is still used in some cases~~.

## Build and Configuration Caching
This project also takes advantage of Gradle's [Build Cache](https://docs.gradle.org/current/userguide/build_cache.html) and [Configuration Cache](https://docs.gradle.org/current/userguide/configuration_cache.html) features to speed up builds and reduce build times.
Note that these features may not always provide significant improvements in build times depending on the project structure and build complexity.

## Contribution
Contributions to this project are welcome! If you encounter any problems or have suggestions for improvement, feel free to submit a pull request or open an issue.

## License
This project is licensed under the [MIT License](https://github.com/hadiyarajesh/compose-template/blob/master/LICENSE).
