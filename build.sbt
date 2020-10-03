import ResourceFilter.filterResources

ThisBuild / scalaVersion := "2.13.3"
ThisBuild / version := "1.0.0"
ThisBuild / description := "A SpigotCord gateway to coordinate with BungeeSemaphore"

//region dependency config

resolvers ++= Seq(
  "hub.spigotmc.org" at "https://hub.spigotmc.org/nexus/content/repositories/snapshots",
)

val providedDependencies = Seq(
  "org.spigotmc" % "spigot-api" % "1.12.2-R0.1-SNAPSHOT",
).map(_ % "provided")

val testDependencies = Seq[ModuleID](
).map(_ % "test")

val dependenciesToEmbed = Seq(
)

// treat localDependencies as "provided" and do not shade them in the output Jar
assemblyExcludedJars in assembly := {
  (fullClasspath in assembly).value
    .filter { a =>
      def directoryContainsFile(directory: File, file: File) =
        file.absolutePath.startsWith(directory.absolutePath)

      directoryContainsFile(baseDirectory.value / "localDependencies", a.data)
    }
}

//endregion

//region settings for token replacements

val tokenReplacementMap = settingKey[Map[String, String]]("Map specifying what tokens should be replaced to")

tokenReplacementMap := Map(
  "name" -> name.value,
  "version" -> version.value
)

val filesToBeReplacedInResourceFolder = Seq("plugin.yml")

val filteredResourceGenerator = taskKey[Seq[File]]("Resource generator to filter resources")

filteredResourceGenerator in Compile :=
  filterResources(
    filesToBeReplacedInResourceFolder,
    tokenReplacementMap.value,
    (resourceManaged in Compile).value, (resourceDirectory in Compile).value
  )

resourceGenerators in Compile += (filteredResourceGenerator in Compile)

unmanagedResources in Compile += baseDirectory.value / "LICENSE"

// exclude replaced files from copying of unmanagedResources
excludeFilter in unmanagedResources :=
  filesToBeReplacedInResourceFolder.foldLeft((excludeFilter in unmanagedResources).value)(_.||(_))

//endregion

addCompilerPlugin("org.typelevel" %% "kind-projector" % "0.11.0" cross CrossVersion.full)

lazy val root = (project in file("."))
  .settings(
    name := "SpigotSemaphore",
    assemblyOutputPath in assembly := baseDirectory.value / "target" / "build" / s"SpigotSemaphore-${version.value}.jar",
    libraryDependencies := providedDependencies ++ testDependencies ++ dependenciesToEmbed,
    unmanagedBase := baseDirectory.value / "localDependencies",
    scalacOptions ++= Seq(
      "-encoding", "utf8",
      "-unchecked",
      "-language:higherKinds",
      "-deprecation",
      "-Ypatmat-exhaust-depth", "320",
      "-Ymacro-annotations",
    ),
    javacOptions ++= Seq("-encoding", "utf8")
  )