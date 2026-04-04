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

import play.api.i18n.Messages
import play.api.mvc.{Call, RequestHeader}
import play.twirl.api.Html
import uk.gov.hmrc.govukfrontend.views.Aliases.{BackLink, HtmlContent, ServiceNavigation, ServiceNavigationItem}
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.notificationbanner.NotificationBanner
import uk.gov.hmrc.hmrcfrontend.views.config.StandardBetaBanner
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.hmrcstandardpage.{Banners, HmrcStandardPageParams, ServiceURLs, TemplateOverrides}
import uk.gov.hmrc.vo.service.config.VOServiceConfig
import uk.gov.hmrc.vo.service.view.html.FullWidthMainContent

/**
  * @author Yuriy Tumakha
  */
trait StandardPageConfig:

  this: VOServiceConfig =>

  def stylesheet: Option[Call]

  def pageTitleFormat(pageHeading: String)(using messages: Messages): String =
    s"$pageHeading - ${messages("service.name")} - ${messages("gov.name")}"

  def serviceName(using messages: Messages): Option[String] = Some(messages("service.name"))

  def homePageUrl(using messages: Messages): Option[String] = configuration.getOptional[String](s"service.homePageUrl.$lang")

  def serviceUrls(using messages: Messages) = ServiceURLs(
    serviceUrl = homePageUrl.orElse(Some(serviceMenuHome.url)),
    signOutUrl = serviceMenuSignOut.map(_.url)
  )

  def pageParams(
    pageHeading: String,
    backLinkUrl: Option[String] = None,
    fullWidth: Boolean = false,
    additionalHeadBlock: Option[Html] = None,
    additionalScriptsBlock: Option[Html] = None,
    beforeContentBlock: Option[Html] = None,
    serviceNavigationItems: Seq[ServiceNavigationItem] = Seq.empty
  )(using request: RequestHeader,
    messages: Messages
  ): HmrcStandardPageParams =
    HmrcStandardPageParams(
      pageTitle = Some(pageTitleFormat(pageHeading)),
      backLink = backLinkUrl.map(BackLink(_)),
      isWelshTranslationAvailable = isWelshTranslationAvailable,
      serviceName = serviceName,
      serviceURLs = serviceUrls,
      banners = Banners(
        displayHmrcBanner = request.path == serviceLocalRoot.url,
        phaseBanner = Option.when(request.path != feedbackPage.url)(StandardBetaBanner()(feedbackPage.url))
      ),
      templateOverrides = TemplateOverrides(
        additionalHeadBlock = additionalHeadBlock,
        additionalScriptsBlock = additionalScriptsBlock,
        beforeContentBlock = beforeContentBlock,
        mainContentLayout = Option.when(fullWidth)(FullWidthMainContent(_))
      ),
      serviceNavigation = Option(serviceNavigationItems).filter(_.nonEmpty).map(ServiceNavigation(serviceName, serviceUrls.serviceUrl, _))
    )

  // Notification Banner - start
  private def buildNotificationBanner(lang: String): NotificationBanner =
    NotificationBanner(
      content = HtmlContent("<p class='govuk-notification-banner__heading'>" + configuration.get[String](s"bannerNotice.$lang.body") + "</p>"),
      title = Text(configuration.get[String](s"bannerNotice.$lang.title"))
    )

  val isNotificationBannerEnabled: Boolean = getBoolean("bannerNotice.enabled")

  private val notificationBannerMap: Map[String, NotificationBanner] =
    if isNotificationBannerEnabled then
      langCodes.map(lang => lang -> buildNotificationBanner(lang)).toMap[String, NotificationBanner]
    else Map.empty

  def notificationBanner(using messages: Messages): NotificationBanner = notificationBannerMap(lang)

  private lazy val notificationBannerPage: Set[String] = notificationBannerEnabledOn.map(_.url)

  def showNotificationBanner(using request: RequestHeader): Boolean =
    isNotificationBannerEnabled && notificationBannerPage(request.path)
  // Notification Banner - end
