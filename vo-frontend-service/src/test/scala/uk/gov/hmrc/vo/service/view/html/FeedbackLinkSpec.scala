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

import play.twirl.api.Html
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

import scala.language.implicitConversions

/**
  * @author Yuriy Tumakha
  */
class FeedbackLinkSpec extends BaseAppSpec:

  private val component = inject[FeedbackLink]

  private def expectedHtml(feedbackTag: String) =
    s"""
       |<p class="govuk-body govuk-!-display-none-print">
       |    <a href="http://localhost:9514/feedback/testserviceid-$feedbackTag" class="govuk-link" target="feedback">feedback.link.text</a>
       |    feedback.link.legend
       |</p>
       |""".stripMargin

  "FeedbackLink" should {
    "render as expected when given a feedbackTag" in {
      val feedbackTag = "submit"

      component(feedbackTag) shouldBe Html(expectedHtml(feedbackTag))
    }

    "render even if feedbackTag is empty" in {
      component("") shouldBe Html(expectedHtml(""))
    }

    "have all template methods implemented" in
      forAll {
        (feedbackTag: String) =>
          component.render(feedbackTag, messages) shouldBe component.ref.f(feedbackTag)(messages)
      }
  }
