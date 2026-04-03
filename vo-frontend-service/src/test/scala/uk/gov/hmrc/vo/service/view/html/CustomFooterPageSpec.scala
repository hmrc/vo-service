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
import uk.gov.hmrc.govukfrontend.views.Aliases.{BackLink, HtmlContent, Text}
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.hmrcstandardpage.HmrcStandardPageParams
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class CustomFooterPageSpec extends BaseAppSpec:

  private val component = inject[CustomFooterPage]

  given request: RequestHeader = getRequest
  given messages: Messages     = messagesApi.preferred(Seq.empty)

  private val pageParams = HmrcStandardPageParams(pageTitle = Some("Page title"), serviceName = Some("Service Name"))
  private val content    = """<h1 class="govuk-heading-xl">Page heading</h1><p class="govuk-body">Some page content</p>"""

  "CustomFooterPage" should {
    "render as expected when given all parameters" in {
      val result = component(pageParams, HtmlContent("<p>Custom footer</p>"))(Html(content)).body

      result    should include("<title>Page title</title>")
      result    should include("""Help using GOV.UK""")
      result    should include("""<div class="govuk-footer__meta-custom">""")
      result    should include("""<p>Custom footer</p>""")
      result shouldNot include("""<link href="/service-root/assets/stylesheets/app.min.css" media="all" rel="stylesheet" type="text/css" />""")
    }

    "render BackLink" in {
      val pageParams = HmrcStandardPageParams(pageTitle = Some("Page title"), backLink = Some(BackLink("/service/previous-page")))
      val result     = component(pageParams, HtmlContent("<p>Custom footer</p>"))(Html(content)).body

      result should include("""<a href="/service/previous-page" class="govuk-back-link">Back</a>""")
      result should include("<title>Page title</title>")
      result should include("""<div class="govuk-footer__meta-custom">""")
      result should include("""<p>Custom footer</p>""")
    }

    "render BackLink with text 'BACK'" in {
      val pageParams = HmrcStandardPageParams(pageTitle = Some("Page with BACK Link"), backLink = Some(BackLink("/service/previous-page", content = Text("BACK"))))
      val result     = component(pageParams, HtmlContent("<p>Custom footer</p>"))(Html(content)).body

      result should include("""<a href="/service/previous-page" class="govuk-back-link">BACK</a>""")
      result should include("<title>Page with BACK Link</title>")
      result should include("""<p>Custom footer</p>""")

    }

    "have all template methods implemented" in
      forAll {
        (pageTitle: String, footer: String) =>
          val params = HmrcStandardPageParams(pageTitle = Option(pageTitle), serviceName = Some("Service Name"))
          component.render(params, HtmlContent(footer), Html(content), request, messages) shouldBe
            component.ref.f(params, HtmlContent(footer))(Html(content))(request, messages)
      }
  }
