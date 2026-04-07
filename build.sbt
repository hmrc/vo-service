import uk.gov.hmrc.DefaultBuildSettings.targetJvm

ThisBuild / majorVersion := 0
ThisBuild / scalaVersion := "3.8.3"
ThisBuild / targetJvm := "jvm-21"
ThisBuild / isPublicArtefact := false
ThisBuild / scalacOptions ++= Seq("-feature", "-Wconf:msg=Flag .* set repeatedly:s")

lazy val library = Project("vo-service", file("."))
  .settings(publish / skip := true)
  .aggregate(voBackendService, voFrontendService)
  .disablePlugins(JUnitXmlReportPlugin)

lazy val voBackendService = Project("vo-backend-service", file("vo-backend-service"))
  .settings(
    libraryDependencies ++= LibDependencies.backendDependencies
  )

val templateImports: Seq[String] = Seq(
  "play.api.mvc.*",
  "play.api.data.*",
  "play.api.i18n.*"
)

lazy val voFrontendService = Project("vo-frontend-service", file("vo-frontend-service"))
  .enablePlugins(SbtTwirl)
  .settings(
    TwirlKeys.templateImports ++= templateImports,
    TwirlKeys.constructorAnnotations += "@javax.inject.Inject()",
    Compile / TwirlKeys.compileTemplates / sourceDirectories += baseDirectory.value / "src/main/twirl",
    libraryDependencies ++= LibDependencies.frontendDependencies
  )
  .dependsOn(voBackendService)

addCommandAlias("precommit", "scalafmtSbt;scalafmtAll")
