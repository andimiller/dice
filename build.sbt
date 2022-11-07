ThisBuild / version := "0.1.0-SNAPSHOT"

ThisBuild / scalaVersion := "3.1.1"

lazy val dice =
  crossProject(JVMPlatform, NativePlatform)
    .settings(
      name := "dice",
//      addCompilerPlugin("com.olegpy" %% "better-monadic-for" % "0.3.1"),
      scalaVersion := "3.1.1",
      libraryDependencies ++= List(
        "org.typelevel" %%% "cats-parse"      % "0.3.8",
        "org.typelevel" %%% "cats-effect-std" % "3.3.14",
        "org.typelevel" %%% "cats-effect"     % "3.3.14",
        "org.typelevel" %%% "cats-mtl"        % "1.3.0",
        "com.monovore" %%% "decline" % "2.3.1"
      )
    )
