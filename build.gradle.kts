plugins {
    `java-library`
    id("floodgate.build-logic") apply false
//    id("com.github.spotbugs") version "4.8.0" apply false
    id("io.freefair.lombok") version "6.3.0" apply false
}

allprojects {
    group = "org.geysermc.floodgate"
    version = "2.1.1-SNAPSHOT"
    description = "Allows Bedrock players to join Java edition servers while keeping the server in online mode"
}

val platforms = setOf(
    projects.bungee,
    projects.spigot,
    projects.velocity
).map { it.dependencyProject }

//todo re-add git properties (or at least build number, branch and commit)
// re-add pmd and organisation/license/sdcm/issuemanagement stuff

val api: Project = projects.api.dependencyProject

subprojects {
//    apply(plugin = "pmd")
//    apply(plugin = "com.github.spotbugs")

    apply {
        plugin("java-library")
        plugin("io.freefair.lombok")
        plugin("floodgate.build-logic")
    }

    val relativePath = projectDir.relativeTo(rootProject.projectDir).path

    if (relativePath.startsWith("database" + File.separator)) {
        group = rootProject.group as String + ".database"
        plugins.apply("floodgate.database-conventions")
    } else {
        when (this) {
            in platforms -> plugins.apply("floodgate.shadow-conventions")
            api -> plugins.apply("floodgate.shadow-conventions")
            else -> plugins.apply("floodgate.base-conventions")
        }
    }
}