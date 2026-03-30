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
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.{HtmlContent, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.notificationbanner.NotificationBanner
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class VOServiceConfigSpec extends BaseAppSpec:

  private val voServiceConfig = inject[VOServiceConfig]

  "VOServiceConfig" should {
    "return serviceID" in {
      voServiceConfig.serviceID shouldBe "TestServiceID"
    }

    "return serviceRoot" in {
      voServiceConfig.serviceRoot.url shouldBe "/service-root"
    }

    "return true for isWelshTranslationAvailable" in {
      voServiceConfig.isWelshTranslationAvailable shouldBe true
    }

    "return None for platformFrontendHost" in {
      voServiceConfig.platformFrontendHost shouldBe None
    }

    "return feedbackFrontendUrl" in {
      voServiceConfig.feedbackFrontendUrl shouldBe "http://localhost:9514/feedback/TestServiceID"
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
  }
