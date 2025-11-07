// Top-level build file where you can add configuration options common to all sub-projects/modules.
buildscript {
    dependencies {
        classpath(libs.detekt.gradlePlugin)
    }
}

plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.serialization) apply false
    alias(libs.plugins.compose.compiler) apply false
    alias(libs.plugins.detekt) apply false
}

apply(plugin = "io.gitlab.arturbosch.detekt")

subprojects {
    apply {
        plugin(rootProject.libs.plugins.detekt.get().pluginId)
    }
    extensions.configure<io.gitlab.arturbosch.detekt.extensions.DetektExtension> {
        autoCorrect = true
        config.setFrom("$rootDir/config/detekt/detekt_for_base_mad.yml")
    }

    dependencies {
        add("detektPlugins", rootProject.libs.detekt.formatting)
        add("detekt", rootProject.libs.detekt.cli)
    }

    tasks.withType<io.gitlab.arturbosch.detekt.Detekt>().configureEach {
        jvmTarget = "1.8"
    }

    tasks.register<io.gitlab.arturbosch.detekt.Detekt>("detektAutoCorrect") {
        description = "Runs Detekt with auto-correction"
        setSource(files(projectDir))
        config.setFrom(files("$rootDir/config/detekt/detekt_for_base_mad.yml"))
        autoCorrect = true
        buildUponDefaultConfig = true
        include("**/*.kt")
        include("**/*.kts")
        exclude("**/resources/**")
        exclude("**/build/**")
    }
}