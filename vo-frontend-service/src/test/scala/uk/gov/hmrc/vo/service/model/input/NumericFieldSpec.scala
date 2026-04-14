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

import scala.util.Try

/**
  * @author Yuriy Tumakha
  */
class NumericFieldSpec extends BaseAppSpec:

  val form: Form[Int] =
    Form(
      single(
        "parkingSpaces" ->
          text
            .verifying(nonEmpty)
            .verifying("error.number.invalid", v => Try(v.toInt).isSuccess)
            .transform[Int](
              _.toInt,
              _.toString
            )
      )
    )

  "NumericField.input" should {
    "return Input parameters for GovukInput Twirl template" in {
      val input = NumericField.input(form, "section2.page3", "parkingSpaces")
      input.id           shouldBe "parkingSpaces"
      input.name         shouldBe "parkingSpaces"
      input.inputType    shouldBe "text"
      input.inputmode    shouldBe Some("numeric")
      input.value        shouldBe None
      input.label        shouldBe Label(classes = "govuk-!-font-weight-bold", content = HtmlContent("section2.page3.parkingSpaces.label"))
      input.hint         shouldBe None
      input.errorMessage shouldBe None
      input.formGroup    shouldBe FormGroup.empty
      input.classes      shouldBe ""
      input.autocomplete shouldBe Some("off")
      input.pattern      shouldBe Some("^\\d*$")
      input.attributes   shouldBe Map("maxlength" -> "8", "aria-label" -> "section2.page3.parkingSpaces.label")
      input.spellcheck   shouldBe Some(false)
      input.prefix       shouldBe None
      input.suffix       shouldBe None
    }

    "return Input as page heading with 10 character width and filled value" in {
      val filledForm = form.fillAndValidate(777)

      val input = NumericField.input(filledForm, "page2", "parkingSpaces", isPageHeading = true, inputWidth = 10)
      input.id           shouldBe "parkingSpaces"
      input.name         shouldBe "parkingSpaces"
      input.inputType    shouldBe "text"
      input.inputmode    shouldBe Some("numeric")
      input.value        shouldBe Some("777")
      input.label        shouldBe Label(isPageHeading = true, classes = "govuk-label--l", content = HtmlContent("page2.parkingSpaces.label"))
      input.hint         shouldBe None
      input.errorMessage shouldBe None
      input.classes      shouldBe "govuk-input--width-10"
      input.autocomplete shouldBe Some("off")
      input.prefix       shouldBe None
    }

    "return Input with error message" in {
      val filledForm = form.bind(Map("parkingSpaces" -> "xxx"))

      val input = NumericField.input(filledForm, "", "parkingSpaces", inputWidth = "two-thirds")
      input.id                          shouldBe "parkingSpaces"
      input.name                        shouldBe "parkingSpaces"
      input.inputType                   shouldBe "text"
      input.inputmode                   shouldBe Some("numeric")
      input.value                       shouldBe Some("xxx")
      input.label                       shouldBe Label(classes = "govuk-!-font-weight-bold", content = HtmlContent("parkingSpaces.label"))
      input.hint                        shouldBe None
      input.errorMessage.map(_.content) shouldBe Some(Text("error.number.invalid"))
      input.classes                     shouldBe "govuk-!-width-two-thirds"
      input.autocomplete                shouldBe Some("off")
      input.prefix                      shouldBe None
    }
  }
