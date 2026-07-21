// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
    alias(libs.plugins.ksp) apply false
    alias(libs.plugins.hilt.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.room) apply false
    alias(libs.plugins.spotless)
}

/*
 * Spotless configuration for the entire repository.
 * Formats Kotlin (ktlint), Markdown, and XML files across all modules.
 * Auto-format: ./gradlew spotlessApply
 * Verify:      ./gradlew spotlessCheck
 *
 * ktlint settings are passed via editorConfigOverride so they are applied
 * deterministically regardless of .editorconfig resolution. The same values
 * live in .editorconfig so the IDE formats identically.
 */
val ktlintRules =
    mapOf(
        "ktlint_code_style" to "intellij_idea",
        // @Composable functions are intentionally PascalCase.
        "ktlint_function_naming_ignore_when_annotated_with" to "Composable",
        // Keep an annotated primary constructor compact on one line, e.g.
        // `class HomeRepositoryImpl @Inject constructor(...)`.
        "ktlint_standard_class-signature" to "disabled",
        // No trailing comma after the last argument/parameter.
        "ij_kotlin_allow_trailing_comma" to "false",
        "ij_kotlin_allow_trailing_comma_on_call_site" to "false",
        // Generous line-length guard; won't force-wrap normal code.
        "max_line_length" to "140"
    )

spotless {
    // Only enforce/format files changed since the base branch (the whole project
    // has already been formatted once). CI must use a full checkout (fetch-depth: 0)
    // so this ref is available.
    ratchetFrom("origin/main")

    kotlin {
        target("**/*.kt")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get()).editorConfigOverride(ktlintRules)
    }
    kotlinGradle {
        target("**/*.gradle.kts")
        targetExclude("**/build/**")
        ktlint(libs.versions.ktlint.get()).editorConfigOverride(ktlintRules)
    }
    format("misc") {
        target("**/*.md", "**/.gitignore")
        targetExclude("**/build/**")
        trimTrailingWhitespace()
        endWithNewline()
    }
    format("xml") {
        target("**/*.xml")
        targetExclude("**/build/**", "**/.idea/**")
        trimTrailingWhitespace()
        endWithNewline()
    }
}
