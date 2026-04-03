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
import uk.gov.hmrc.vo.unit.test.BaseSpec

/**
  * @author Yuriy Tumakha
  */
class FullWidthMainContentSpec extends BaseSpec:

  private val component = FullWidthMainContent

  private def expectedHtml(content: String) = s"""
                                                 |<div class="govuk-grid-row">
                                                 |    <div class="govuk-grid-column-full">
                                                 |        $content
                                                 |    </div>
                                                 |</div>
                                                 |""".stripMargin

  "FullWidthMainContent" should {
    "render as expected when given a contentBlock" in {
      val content            = """<h1 class="govuk-heading-xl">Page heading</h1><p class="govuk-body">Some page content</p>"""
      val contentBlock: Html = Html(content)

      component(contentBlock) shouldBe Html(expectedHtml(content))
    }

    "render an empty contentBlock" in {
      component(Html("")) shouldBe Html(expectedHtml(""))
    }

    "have all template methods implemented" in
      forAll {
        (str: String) =>
          component.render(Html(str)) shouldBe component.ref.f(Html(str))
      }
  }
