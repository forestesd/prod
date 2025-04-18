pluginManagement {
    repositories {
        google {
            content {
                includeGroupByRegex("com\\.android.*")
                includeGroupByRegex("com\\.google.*")
                includeGroupByRegex("androidx.*")
            }
        }
        mavenCentral()
        gradlePluginPortal()
    }
}

dependencyResolutionManagement {

    repositoriesMode.set(RepositoriesMode.FAIL_ON_PROJECT_REPOS)
    repositories {
        google()
        mavenCentral()

    }
}

rootProject.name = "PROD"
include(":app")
include(":navBarUI")
include(":navigation")
include(":home")
include(":NewYorkTimesApi")
include(":tickersApi")
include(":financeData")
include(":financeUi")
include(":notesData")
include(":notesUi")
