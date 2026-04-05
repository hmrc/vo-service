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
import play.api.mvc.{Call, RequestHeader}
import play.api.test.FakeRequest
import play.api.test.Helpers.*
import play.twirl.api.Html
import test.EmptyAppConfig
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

    ".getString returns a String config without throwing an exception when not found" in {
      voServiceConfig.getString("service.id")         shouldBe "TestServiceID"
      voServiceConfig.getString("some.service.param") shouldBe "Config:some.service.param"
    }

    ".getInt returns default value for numeric config without throwing an exception when not found" in {
      voServiceConfig.getInt("some.number.with.default", 8080) shouldBe 8080
      voServiceConfig.getInt("some.unknown.number")            shouldBe 1
    }

    ".getBoolean returns a Boolean config without throwing an exception when not found" in {
      voServiceConfig.getBoolean("notificationBanner.enabled") shouldBe true
      voServiceConfig.getBoolean("some.boolean", true)         shouldBe true
      voServiceConfig.getBoolean("some.boolean")               shouldBe false
    }

    "return serviceLocalRoot url" in {
      voServiceConfig.serviceLocalRoot.url shouldBe "/service-root"
    }

    "return serviceMenuHome url" in {
      voServiceConfig.serviceMenuHome.url shouldBe "/service-root/home"
    }

    "return serviceMenuSignOut url" in {
      voServiceConfig.serviceMenuSignOut.value.url shouldBe "/service-root/logout"
    }

    "return feedbackPage url" in {
      voServiceConfig.feedbackPage.url shouldBe "/service-root/feedback"
    }

    "return true for isWelshTranslationAvailable" in {
      voServiceConfig.isWelshTranslationAvailable shouldBe true
    }

    "return stylesheet url" in {
      voServiceConfig.stylesheet.value.url shouldBe "/service-root/assets/stylesheets/app.min.css"
    }

    "return langCodes" in {
      voServiceConfig.langCodes shouldBe Set(en, cy)
    }

    "return None for platformFrontendHost" in {
      voServiceConfig.platformFrontendHost shouldBe None
    }

    "return true for isPhaseBannerEnabled" in {
      voServiceConfig.isPhaseBannerEnabled shouldBe true
    }

    "return true for isNotificationBannerEnabled" in {
      voServiceConfig.isNotificationBannerEnabled shouldBe true
    }

    "return true for showNotificationBanner on theFirstPage" in {
      given RequestHeader = FakeRequest(GET, voServiceConfig.theFirstPage.url)
      voServiceConfig.showNotificationBanner shouldBe true
    }

    "return false for showNotificationBanner on feedback page" in {
      given RequestHeader = FakeRequest(GET, voServiceConfig.feedbackPage.url)
      voServiceConfig.showNotificationBanner shouldBe false
    }

    "return false for showNotificationBanner on random page" in {
      given RequestHeader = FakeRequest(GET, "/service-root/some-page")
      voServiceConfig.showNotificationBanner shouldBe false
    }

    "build NotificationBanner" in {
      given Messages = messagesApi.preferred(Seq.empty)

      voServiceConfig.notificationBanner shouldBe
        NotificationBanner(
          HtmlContent("<p class='govuk-notification-banner__heading'>This service will be unavailable while we carry out some essential maintenance.</p>"),
          title = Text("Important")
        )
    }

    "return notificationBannerEnabledOn pages" in {
      voServiceConfig.notificationBannerEnabledOn shouldBe Set(voServiceConfig.theFirstPage)
    }

    "return timeoutDialogEnabledExcept pages" in {
      voServiceConfig.timeoutDialogEnabledExcept shouldBe Set(voServiceConfig.serviceMenuHome)
    }

    "handle empty Configuration" in {
      val voServiceConfig = EmptyAppConfig

      voServiceConfig.isWelshTranslationAvailable shouldBe false
      voServiceConfig.serviceID                   shouldBe "Config:service.id"
      voServiceConfig.serviceLocalRoot.url        shouldBe "/some-service-root/home"
      voServiceConfig.serviceMenuHome.url         shouldBe "/some-service-root/home"
      voServiceConfig.feedbackPage.url            shouldBe "http://localhost:9514/feedback/Config:service.id"
      voServiceConfig.serviceMenuSignOut          shouldBe None
      voServiceConfig.stylesheet                  shouldBe None
      voServiceConfig.langCodes                   shouldBe Set(en)
      voServiceConfig.isPhaseBannerEnabled        shouldBe false

      voServiceConfig.notificationBannerEnabledOn shouldBe Set(voServiceConfig.serviceMenuHome, voServiceConfig.theFirstPage)
      voServiceConfig.timeoutDialogEnabledExcept  shouldBe empty

      val theFirstPage = FakeRequest(GET, voServiceConfig.theFirstPage.url)
      val randomPage   = FakeRequest(GET, "/service-root/some-page")

      voServiceConfig.isTimeoutDialogEnabled(using theFirstPage) shouldBe false
      voServiceConfig.showNotificationBanner(using theFirstPage) shouldBe false

      voServiceConfig.isTimeoutDialogEnabled(using randomPage) shouldBe false
      voServiceConfig.showNotificationBanner(using randomPage) shouldBe false
    }

    "handle empty Configuration but notificationBanner.enabled = true" in {
      val voServiceConfig =
        new VOServiceConfig:
          override def configuration: Configuration = Configuration(
            "notificationBanner.enabled"  -> true,
            "notificationBanner.en.title" -> "Important",
            "notificationBanner.en.body"  -> "Some notice"
          )
          override def stylesheet: Option[Call]     = None
          def serviceMenuHome: Call                 = Call("GET", "/some-service-root/home")
          def theFirstPage: Call                    = Call("GET", "/some-service-root/first")

      voServiceConfig.theFirstPage.url shouldBe "/some-service-root/first"

      voServiceConfig.notificationBannerEnabledOn shouldBe Set(voServiceConfig.serviceMenuHome, voServiceConfig.theFirstPage)
      voServiceConfig.timeoutDialogEnabledExcept  shouldBe empty

      val theFirstPage = FakeRequest(GET, voServiceConfig.theFirstPage.url)
      val randomPage   = FakeRequest(GET, "/service-root/some-page")

      voServiceConfig.isTimeoutDialogEnabled(using theFirstPage) shouldBe false
      voServiceConfig.showNotificationBanner(using theFirstPage) shouldBe true

      voServiceConfig.isTimeoutDialogEnabled(using randomPage) shouldBe false
      voServiceConfig.showNotificationBanner(using randomPage) shouldBe false
    }

    "build HmrcStandardPageParams" in {
      given RequestHeader = getRequest
      given Messages      = messagesApi.preferred(Seq.empty)

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
        phaseBanner = Some(StandardBetaBanner()("/service-root/feedback"))
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
      given RequestHeader = getRequest
      given Messages      = messagesApi.preferred(Seq.empty)

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
        phaseBanner = Some(StandardBetaBanner()("/service-root/feedback"))
      )

      standardPageParams.templateOverrides.additionalHeadBlock    shouldBe None
      standardPageParams.templateOverrides.additionalScriptsBlock shouldBe None
      standardPageParams.templateOverrides.beforeContentBlock     shouldBe None
      standardPageParams.templateOverrides.mainContentLayout      shouldBe None
      standardPageParams.serviceNavigation                        shouldBe None
    }

  }
