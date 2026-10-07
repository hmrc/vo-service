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
class PrintLinkSpec extends BaseAppSpec:

  private val component = PrintLink

  private def expectedHtml(linkTextKey: String) =
    s"""
       |<p class="govuk-body govuk-!-display-none-print">
       |    <a class="govuk-link print-link" href="">$linkTextKey</a>
       |</p>
       |
       |<script>
       |  document.querySelector(".print-link").addEventListener("click", event => {
       |    event.preventDefault()
       |    window.print()
       |  })
       |</script>
       |""".stripMargin

  "PrintLink" should {
    "render as expected when given a linkTextKey" in {
      val linkTextKey = "print.link.text"

      component(linkTextKey) shouldBe Html(expectedHtml(linkTextKey))
    }

    "render even if linkTextKey is empty" in {
      component("") shouldBe Html(expectedHtml(""))
    }

    "have all template methods implemented" in
      forAll {
        (linkTextKey: String) =>
          component.render(linkTextKey, messages) shouldBe component.ref.f(linkTextKey)(messages)
      }
  }
