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
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import play.twirl.api.Html
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class StandardPageSpec extends BaseAppSpec:

  private val component: StandardPage = inject[StandardPage]

  given request: RequestHeader = FakeRequest(GET, "/service-root/some-page")
  given messages: Messages     = messagesApi.preferred(Seq.empty)

  private val content = """<h1 class="govuk-heading-xl">Page heading</h1><p class="govuk-body">Some page content</p>"""

  "StandardPage" should {
    "render as expected when given all parameters" in {
      val result = component("Page heading")(Html(content)).body

      result    should include("<title>Page heading - service.name - gov.name</title>")
      result    should include("""<link href="/service-root/assets/stylesheets/app.min.css" media="all" rel="stylesheet" type="text/css" />""")
      result    should include("""<meta name="hmrc-timeout-dialog"""")
      result    should include("""Help using GOV.UK""")
      result shouldNot include("""<div class="govuk-footer__meta-custom">""")
    }

    "render custom footer" in {
      val result = component("Page with custom footer", footerBlock = Some(Html("<p>Custom footer</p>")))(Html(content)).body

      result should include("<title>Page with custom footer - service.name - gov.name</title>")
      result should include("""<link href="/service-root/assets/stylesheets/app.min.css" media="all" rel="stylesheet" type="text/css" />""")
      result should include("""<div class="govuk-footer__meta-custom">""")
      result should include("""<p>Custom footer</p>""")
    }

    "have all template methods implemented" in
      forAll {
        (pageHeading: String) =>
          component.render(pageHeading, None, false, None, None, None, None, Seq.empty, Html(content), request, messages) shouldBe
            component.ref.f(pageHeading, None, false, None, None, None, None, Seq.empty)(Html(content))(request, messages)
      }
  }
