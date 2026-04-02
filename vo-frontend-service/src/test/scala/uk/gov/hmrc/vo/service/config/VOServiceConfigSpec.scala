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
import play.api.mvc.{Call, Request}
import play.twirl.api.Html
import uk.gov.hmrc.govukfrontend.views.Aliases.{ServiceNavigation, ServiceNavigationItem}
import uk.gov.hmrc.govukfrontend.views.html.components.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.backlink.BackLink
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.notificationbanner.NotificationBanner
import uk.gov.hmrc.hmrcfrontend.views.config.StandardBetaBanner
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.hmrcstandardpage.{Banners, ServiceURLs}
import uk.gov.hmrc.vo.service.view.html.FullWidthMainContent
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class VOServiceConfigSpec extends BaseAppSpec with LangSupport:

  private val voServiceConfig = inject[VOServiceConfig]

  "VOServiceConfig" should {
    "return serviceID" in {
      voServiceConfig.serviceID shouldBe "TestServiceID"
    }

    "return serviceLocalRoot url" in {
      voServiceConfig.serviceLocalRoot.url shouldBe "/service-root"
    }

    "return serviceHome url" in {
      voServiceConfig.serviceHome.url shouldBe "/service-root/home"
    }

    "return serviceMenuSignOut url" in {
      voServiceConfig.serviceMenuSignOut shouldBe None
    }

    "return serviceFeedback url" in {
      voServiceConfig.serviceFeedback.url shouldBe "http://localhost:9514/feedback/TestServiceID"
    }

    "return true for isWelshTranslationAvailable" in {
      voServiceConfig.isWelshTranslationAvailable shouldBe true
    }

    "return langCodes" in {
      voServiceConfig.langCodes shouldBe Set(en, cy)
    }

    "return None for platformFrontendHost" in {
      voServiceConfig.platformFrontendHost shouldBe None
    }

    "return true for isNotificationBannerEnabled" in {
      voServiceConfig.isNotificationBannerEnabled shouldBe true
    }

    "build NotificationBanner" in {
      given Messages = messagesApi.preferred(Seq.empty)

      voServiceConfig.notificationBanner shouldBe
        NotificationBanner(
          HtmlContent("<p class='govuk-notification-banner__heading'>This service will be unavailable while we carry out some essential maintenance.</p>"),
          title = Text("Important")
        )
    }

    "return empty sequence timeoutDialogEnabledExcept" in {
      voServiceConfig.timeoutDialogEnabledExcept shouldBe empty
    }

    "handle empty Configuration" in {
      val voServiceConfig =
        new VOServiceConfig:
          def configuration: Configuration   = Configuration.empty
          def serviceID: String              = "SomeServiceID"
          def serviceHome: Call              = Call("GET", "/some-service-root/home")
          override def serviceFeedback: Call = Call("GET", "/some-service-root/feedback")

      voServiceConfig.isWelshTranslationAvailable shouldBe false
      voServiceConfig.serviceID                   shouldBe "SomeServiceID"
      voServiceConfig.serviceLocalRoot.url        shouldBe "/some-service-root/home"
      voServiceConfig.serviceHome.url             shouldBe "/some-service-root/home"
      voServiceConfig.serviceFeedback.url         shouldBe "/some-service-root/feedback"
      voServiceConfig.langCodes                   shouldBe Set(en)
    }

    "build HmrcStandardPageParams" in {
      given Request[?] = getRequest
      given Messages   = messagesApi.preferred(Seq.empty)

      val standardPageParams = voServiceConfig.pageParams(
        "Page heading",
        Some("/back/link"),
        true,
        Some(Html("<head/>")),
        Some(Html("<script/>")),
        Some(Html("<div>beforeContent</div>")),
        Seq(ServiceNavigationItem(Text("Menu item 1"), "#"))
      )

      standardPageParams.pageTitle.value             shouldBe "Page heading - service.name - gov.name"
      standardPageParams.backLink.value              shouldBe BackLink("/back/link")
      standardPageParams.isWelshTranslationAvailable shouldBe true
      standardPageParams.serviceName.value           shouldBe "service.name"

      standardPageParams.serviceURLs shouldBe ServiceURLs(
        serviceUrl = Some("/service-root/home")
      )

      standardPageParams.banners shouldBe Banners(
        displayHmrcBanner = false,
        phaseBanner = Some(StandardBetaBanner()("http://localhost:9514/feedback/TestServiceID"))
      )

      standardPageParams.templateOverrides.additionalHeadBlock.value    shouldBe Html("<head/>")
      standardPageParams.templateOverrides.additionalScriptsBlock.value shouldBe Html("<script/>")
      standardPageParams.templateOverrides.beforeContentBlock.value     shouldBe Html("<div>beforeContent</div>")

      val content = Html("<b>content</b>")
      standardPageParams.templateOverrides.mainContentLayout.value(content) shouldBe FullWidthMainContent(content)

      standardPageParams.serviceNavigation.value shouldBe ServiceNavigation(
        Some("service.name"),
        Some("/service-root/home"),
        List(ServiceNavigationItem(Text("Menu item 1"), "#"))
      )
    }

    "build minimal HmrcStandardPageParams" in {
      given Request[?] = getRequest
      given Messages   = messagesApi.preferred(Seq.empty)

      val standardPageParams = voServiceConfig.pageParams("Simple page heading")

      standardPageParams.pageTitle.value             shouldBe "Simple page heading - service.name - gov.name"
      standardPageParams.backLink                    shouldBe None
      standardPageParams.isWelshTranslationAvailable shouldBe true
      standardPageParams.serviceName.value           shouldBe "service.name"

      standardPageParams.serviceURLs shouldBe ServiceURLs(
        serviceUrl = Some("/service-root/home")
      )

      standardPageParams.banners shouldBe Banners(
        displayHmrcBanner = false,
        phaseBanner = Some(StandardBetaBanner()("http://localhost:9514/feedback/TestServiceID"))
      )

      standardPageParams.templateOverrides.additionalHeadBlock    shouldBe None
      standardPageParams.templateOverrides.additionalScriptsBlock shouldBe None
      standardPageParams.templateOverrides.beforeContentBlock     shouldBe None
      standardPageParams.templateOverrides.mainContentLayout      shouldBe None
      standardPageParams.serviceNavigation                        shouldBe None
    }

  }
