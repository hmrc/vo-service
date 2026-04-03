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

package uk.gov.hmrc.vo.service.view.html

import play.api.i18n.Messages
import play.api.mvc.RequestHeader
import play.twirl.api.Html
import test.EmptyAppConfig
import uk.gov.hmrc.hmrcfrontend.views.html.helpers.HmrcTimeoutDialogHelper
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class StandardHeadSpec extends BaseAppSpec:

  // TODO: Move to vo-unit-test
  extension (string: String)
    def trimEmptyLines(replaceWith: String = ""): String = string.replace("\n", replaceWith).replace("\r", replaceWith)

  private val component               = inject[StandardHead]
  private val componentForEmptyConfig = StandardHead(EmptyAppConfig, inject[HmrcTimeoutDialogHelper])

  given request: RequestHeader = getRequest
  given messages: Messages     = messagesApi.preferred(Seq.empty)

  private val timeoutDialogStart = """<meta name="hmrc-timeout-dialog" content="hmrc-timeout-dialog" """

  "StandardHead" should {
    "render as expected when given all parameters" in {
      val content                   = """<link href="/stylesheet/extra-cool.css" media="all" rel="stylesheet" type="text/css" />"""
      val additionalHeadBlock: Html = Html(content)

      val result = component(Some(additionalHeadBlock)).body.trimEmptyLines(" ")

      result should include("""<link href="/service-root/assets/stylesheets/app.min.css" media="all" rel="stylesheet" type="text/css" />""")
      result should include(content)
      result should include(timeoutDialogStart)
    }

    "render as expected when given all parameters and empty config" in {
      val content                   = """<link href="/stylesheet/extra-cool.css" media="all" rel="stylesheet" type="text/css" />"""
      val additionalHeadBlock: Html = Html(content)

      val result = componentForEmptyConfig(Some(additionalHeadBlock)).body.trimEmptyLines(" ")

      result    should include(content)
      result shouldNot include("""<link href="/service-root/assets/stylesheets/app.min.css" media="all" rel="stylesheet" type="text/css" />""")
      result shouldNot include(timeoutDialogStart)
    }

    "render as expected when all parameters are None" in {
      component().body.trim.trimEmptyLines(" ") should include(timeoutDialogStart)
    }

    "render as expected when all parameters are None and empty config" in {
      componentForEmptyConfig(None).body.trim shouldBe ""
    }

    "have all template methods implemented" in
      forAll {
        (additionalHeadBlock: String) =>
          component.render(Option(Html(additionalHeadBlock)), request, messages) shouldBe
            component.ref.f(Option(Html(additionalHeadBlock)))(request, messages)
      }
  }
