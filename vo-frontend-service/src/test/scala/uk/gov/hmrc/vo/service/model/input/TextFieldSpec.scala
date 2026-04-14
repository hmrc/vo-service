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

package uk.gov.hmrc.vo.service.model.input

import play.api.data.Form
import play.api.data.Forms.{single, text}
import play.api.data.validation.Constraints.nonEmpty
import uk.gov.hmrc.govukfrontend.views.Aliases.HtmlContent
import uk.gov.hmrc.govukfrontend.views.viewmodels.FormGroup
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.govukfrontend.views.viewmodels.errormessage.ErrorMessage
import uk.gov.hmrc.govukfrontend.views.viewmodels.hint.Hint
import uk.gov.hmrc.govukfrontend.views.viewmodels.input.PrefixOrSuffix
import uk.gov.hmrc.govukfrontend.views.viewmodels.label.Label
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class TextFieldSpec extends BaseAppSpec:

  val form: Form[String] =
    Form(
      single(
        "name" -> text.verifying(nonEmpty)
      )
    )

  "TextField.input" should {
    "return Input parameters for GovukInput Twirl template" in {
      val input = TextField.input(form, "section1.page1", "name")
      input.id           shouldBe "name"
      input.name         shouldBe "name"
      input.inputType    shouldBe "text"
      input.inputmode    shouldBe None
      input.value        shouldBe None
      input.label        shouldBe Label(classes = "govuk-!-font-weight-bold", content = HtmlContent("section1.page1.name.label"))
      input.hint         shouldBe None
      input.errorMessage shouldBe None
      input.formGroup    shouldBe FormGroup.empty
      input.classes      shouldBe ""
      input.autocomplete shouldBe None
      input.pattern      shouldBe None
      input.attributes   shouldBe Map.empty
      input.spellcheck   shouldBe None
      input.prefix       shouldBe None
      input.suffix       shouldBe None
    }

    "return Input as page heading, with 10 character width, with suffix content and filled value" in {
      val filledForm = form.fillAndValidate("Full name")

      val input = TextField.input(filledForm, "page2", "name", isPageHeading = true, inputWidth = 10, suffixContent = Some(Text("miles")))
      input.id                    shouldBe "name"
      input.name                  shouldBe "name"
      input.inputType             shouldBe "text"
      input.inputmode             shouldBe None
      input.value                 shouldBe Some("Full name")
      input.label                 shouldBe Label(isPageHeading = true, classes = "govuk-label--l", content = HtmlContent("page2.name.label"))
      input.hint                  shouldBe None
      input.errorMessage          shouldBe None
      input.classes               shouldBe "govuk-input--width-10"
      input.autocomplete          shouldBe None
      input.prefix                shouldBe None
      input.suffix.map(_.content) shouldBe Some(Text("miles"))
    }

    "return Input with error message and without label" in {
      val filledForm = form.bind(Map.empty)

      val input = TextField.input(filledForm, "", "name", inputWidth = "two-thirds", hideLabel = true)
      input.id                          shouldBe "name"
      input.name                        shouldBe "name"
      input.inputType                   shouldBe "text"
      input.value                       shouldBe None
      input.label                       shouldBe Label()
      input.hint                        shouldBe None
      input.errorMessage.map(_.content) shouldBe Some(Text("This field is required"))
      input.classes                     shouldBe "govuk-!-width-two-thirds"
      input.autocomplete                shouldBe None
      input.prefix                      shouldBe None
    }
  }
