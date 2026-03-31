import sbt.*

private object LibDependencies {

  val bootstrapVersion    = "10.7.0"
  val playFrontendVersion = "12.32.0"
  val voTestVersion       = "0.1.0"

  private val common: Seq[ModuleID] = Seq(
    "uk.gov.hmrc" %% "bootstrap-test-play-30" % bootstrapVersion % Test,
    "uk.gov.hmrc" %% "vo-unit-test"           % voTestVersion    % Test
  )

  val backendDependencies: Seq[ModuleID] = common ++ Seq(
    "uk.gov.hmrc" %% "bootstrap-backend-play-30" % bootstrapVersion % Provided
  )

  val frontendDependencies: Seq[ModuleID] = common ++ Seq(
    "uk.gov.hmrc" %% "bootstrap-frontend-play-30" % bootstrapVersion    % Provided,
    "uk.gov.hmrc" %% "play-frontend-hmrc-play-30" % playFrontendVersion % Provided
  )

}
