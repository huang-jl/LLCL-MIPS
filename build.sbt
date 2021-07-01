ThisBuild / scalaVersion     := "2.11.12"
ThisBuild / version          := "1.0.0"
ThisBuild / organization     := ""
ThisBuild / transitiveClassifiers := Seq(Artifact.SourceClassifier)

val spinalVersion = "1.4.3"

lazy val root = (project in file("."))
  .settings(
    name := "llcl-mips",
    libraryDependencies ++= Seq(
      "com.github.spinalhdl" %% "spinalhdl-core" % spinalVersion,
      "com.github.spinalhdl" %% "spinalhdl-lib" % spinalVersion,
      compilerPlugin("com.github.spinalhdl" %% "spinalhdl-idsl-plugin" % spinalVersion)
    ),
    fork := true
  )


