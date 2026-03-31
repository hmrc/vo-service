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

/**
  * @author Yuriy Tumakha
  */
class FullWidthMainContentSpec extends BaseAppSpec:

  private val component = inject[FullWidthMainContent]

  private def expectedHtml(content: String) = s"""
                                                 |<div class="govuk-grid-row">
                                                 |    <div class="govuk-grid-column-full">
                                                 |        $content
                                                 |    </div>
                                                 |</div>
                                                 |""".stripMargin

  "Given a contentBlock of HTML, rendering the FullWidthMainContent" should {
    "render as expected" in {
      val content            = """<h1 class="govuk-heading-xl">Page heading</h1><p class="govuk-body">Some page content</p>"""
      val contentBlock: Html = Html(content)

      component.render(contentBlock) shouldBe Html(expectedHtml(content))
    }
  }

  "FullWidthMainContent" should {
    "handle empty contentBlock" in {
      component.ref.f(Html("")) shouldBe Html(expectedHtml(""))
    }
  }
