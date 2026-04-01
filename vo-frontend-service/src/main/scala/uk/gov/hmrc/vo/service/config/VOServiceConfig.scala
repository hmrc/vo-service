/*
 * Copyright 2026 HM Revenue & Customs
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package uk.gov.hmrc.vo.service.config

import play.api.Configuration
import play.api.i18n.Messages
import play.api.mvc.Call
import play.twirl.api.Html
import uk.gov.hmrc.govukfrontend.views.Aliases.BackLink
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.{HtmlContent, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.notificationbanner.NotificationBanner
import uk.gov.hmrc.hmrcfrontend.views.config.StandardBetaBanner
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.hmrcstandardpage.{Banners, HmrcStandardPageParams, ServiceURLs, TemplateOverrides}
import uk.gov.hmrc.vo.service.view.html.FullWidthMainContent

/**
  * @author Yuriy Tumakha
  */
trait VOServiceConfig extends LangCodes:

  def configuration: Configuration
  def serviceID: String
  def serviceLocalRoot: Call
  def serviceHome: Call
  def serviceFeedback: Call                         = feedbackFrontendForm
  override def isWelshTranslationAvailable: Boolean = false

  /**
    * "platform.frontend.host" is defined only in the cloud environment.
    */
  val platformFrontendHost: Option[String] = configuration.getOptional[String]("platform.frontend.host")

  // Feedback frontend
  private val localFeedbackBase          = "http://localhost:9514"
  private val feedbackBase: String       = platformFrontendHost.getOrElse(localFeedbackBase)
  private val feedbackFrontendForm: Call = Call("GET", s"$feedbackBase/feedback/$serviceID")

  // Notification Banner
  private def buildNotificationBanner(lang: String): NotificationBanner =
    NotificationBanner(
      content = HtmlContent("<p class='govuk-notification-banner__heading'>" + configuration.get[String](s"bannerNotice.$lang.body") + "</p>"),
      title = Text(configuration.get[String](s"bannerNotice.$lang.title"))
    )

  val isNotificationBannerEnabled: Boolean = configuration.getOptional[Boolean]("bannerNotice.enabled").getOrElse(false)

  private val notificationBannerMap: Map[String, NotificationBanner] =
    if isNotificationBannerEnabled then
      langCodes.map(lang => lang -> buildNotificationBanner(lang)).toMap[String, NotificationBanner]
    else Map.empty

  def notificationBanner(using messages: Messages): NotificationBanner = notificationBannerMap(lang)

  def standardPageParams(
    pageTitle: String,
    backLink: Option[BackLink] = None,
    additionalHeadBlock: Option[Html] = None,
    additionalScriptsBlock: Option[Html] = None,
    beforeContentBlock: Option[Html] = None,
    fullWidth: Boolean = false
  )(using messages: Messages
  ): HmrcStandardPageParams =
    HmrcStandardPageParams(
      pageTitle = Some(pageTitle),
      backLink = backLink,
      isWelshTranslationAvailable = isWelshTranslationAvailable,
      serviceName = Some(messages("service.name")),
      serviceURLs = ServiceURLs(
        serviceUrl = Some(serviceHome.url)
      ),
      banners = Banners(
        displayHmrcBanner = false,
        phaseBanner = Some(StandardBetaBanner()(serviceFeedback.url))
      ),
      templateOverrides = TemplateOverrides(
        additionalHeadBlock = additionalHeadBlock,
        additionalScriptsBlock = additionalScriptsBlock,
        beforeContentBlock = beforeContentBlock,
        mainContentLayout = Option.when(fullWidth)(FullWidthMainContent()(_))
      )
    )
