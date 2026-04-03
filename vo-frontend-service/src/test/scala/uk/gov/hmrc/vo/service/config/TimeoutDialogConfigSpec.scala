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

import play.api.mvc.RequestHeader
import test.EmptyAppConfig
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class TimeoutDialogConfigSpec extends BaseAppSpec:

  private val voServiceConfig = inject[VOServiceConfig]

  given request: RequestHeader = getRequest

  "TimeoutDialogConfig" should {
    "have enabled TimeoutDialog" in {
      voServiceConfig.isTimeoutDialogEnabled shouldBe true
    }

    "return signOutCall" in {
      voServiceConfig.signOutCall shouldBe None
    }

    "return keepAliveCall" in {
      voServiceConfig.keepAliveCall shouldBe None
    }

    "return timeoutCall" in {
      voServiceConfig.timeoutCall shouldBe None
    }

    "handle empty config" in {
      val voServiceConfig = EmptyAppConfig

      voServiceConfig.isTimeoutDialogEnabled shouldBe false
      voServiceConfig.signOutCall            shouldBe None
      voServiceConfig.keepAliveCall          shouldBe None
      voServiceConfig.timeoutCall            shouldBe None
    }
  }
