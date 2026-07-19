pluginManagement {
    repositories {
        // مخزن مایکت برای پلاگین‌ها
        maven {
            url = uri("https://maven.myket.ir/")
        }
        maven { url = uri("https://gradle.jamko.ir") }
        maven { url = uri("https://en-mirror.ir") }
        maven { url = uri("https://google403.ir") }
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
        maven {
            url = uri("https://maven.myket.ir/")
        }
        maven { url = uri("https://gradle.jamko.ir") }
        maven { url = uri("https://en-mirror.ir") }
        maven { url = uri("https://google403.ir") }

        google()
        mavenCentral()
    }
}

rootProject.name = "NewsFlow"
include(":app")
 