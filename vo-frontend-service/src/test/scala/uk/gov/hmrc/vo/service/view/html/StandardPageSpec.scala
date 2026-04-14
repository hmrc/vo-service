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

import play.api.mvc.RequestHeader
import play.api.test.FakeRequest
import play.api.test.Helpers.GET
import play.twirl.api.Html
import uk.gov.hmrc.govukfrontend.views.Aliases.{ErrorLink, ErrorSummary, Text}
import uk.gov.hmrc.vo.service.model.AccountInfo
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

import scala.language.implicitConversions

/**
  * @author Yuriy Tumakha
  */
class StandardPageSpec extends BaseAppSpec:

  private val component: StandardPage = inject[StandardPage]

  given request: RequestHeader = FakeRequest(GET, "/service-root/some-page")

  private val content: Html = """<p class="govuk-body">Some page content</p>"""

  "StandardPage" should {
    "render as expected when given all parameters" in {
      val result = component(
        "Page heading",
        accountInfo = AccountInfo("Account param 1", "Param 2"),
        backLinkUrl = "/service/previous-page",
        errorSummary = ErrorSummary(Seq(ErrorLink("#field1", Text("field1")))),
        showNotificationBanner = true,
        sectionName = "Section",
        fullWidth = true
      )(content).body

      result    should include("<title>error.prefix Page heading - service.name - gov.name</title>")
      result    should include("""<span class="govuk-body-s">Account param 1</span>""")
      result    should include("""<a href="/service/previous-page" class="govuk-back-link">Back</a>""")
      result    should include("""<ul class="govuk-list govuk-error-summary__list">""")
      result    should include("""<a href="#field1">field1</a>""")
      result    should include("""<div class="govuk-notification-banner"""")
      result    should include("""<p class="hmrc-caption govuk-caption-m"><span class="govuk-visually-hidden">This section is </span>Section</p>""")
      result    should include("""<h1 class="govuk-heading-l">Page heading</h1>""")
      result    should include("""<link href="/service-root/assets/stylesheets/app.min.css" media="all" rel="stylesheet" type="text/css" />""")
      result    should include("""<meta name="hmrc-timeout-dialog"""")
      result    should include("""Help using GOV.UK""")
      result shouldNot include("""<div class="govuk-footer__meta-custom">""")
    }

    "render custom footer" in {
      val result = component(
        "Page with custom footer",
        showH1 = false,
        footerBlock = "<p>Custom footer</p>"
      )(content).body

      result    should include("<title>Page with custom footer - service.name - gov.name</title>")
      result shouldNot include("""<h1""")
      result shouldNot include("""Back</a>""")
      result    should include("""<link href="/service-root/assets/stylesheets/app.min.css" media="all" rel="stylesheet" type="text/css" />""")
      result    should include("""<div class="govuk-footer__meta-custom">""")
      result    should include("""<p>Custom footer</p>""")
    }

    "render JavaScript history back link" in {
      val result = component(
        "Page with JavaScript history back link",
        backLinkUrl = "javascript-back"
      )(content).body

      result should include("<title>Page with JavaScript history back link - service.name - gov.name</title>")
      result should include("""<a href="#" class="govuk-back-link" data-module="hmrc-back-link">Back</a>""")
    }

    "have all template methods implemented" in
      forAll {
        (pageHeading: String) =>
          component.render(pageHeading, None, None, None, None, None, true, false, None, None, None, Seq.empty, content, request, messages) shouldBe
            component.ref.f(pageHeading, None, None, None, None, None, true, false, None, None, None, Seq.empty)(content)(request, messages)
      }
  }
