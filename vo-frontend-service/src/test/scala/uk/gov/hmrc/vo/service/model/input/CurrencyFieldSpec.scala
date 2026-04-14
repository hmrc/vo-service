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
import play.api.data.Forms.{bigDecimal, single}
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
class CurrencyFieldSpec extends BaseAppSpec:

  val form: Form[BigDecimal] =
    Form(
      single(
        "amount" -> bigDecimal
      )
    )

  "CurrencyField.input" should {
    "return Input parameters for GovukInput Twirl template" in {
      val input = CurrencyField.input(form, "section2.page3", "rent.amount")
      input.id           shouldBe "rent.amount"
      input.name         shouldBe "rent.amount"
      input.inputType    shouldBe "text"
      input.inputmode    shouldBe None
      input.value        shouldBe None
      input.label        shouldBe Label(classes = "govuk-!-font-weight-bold", content = HtmlContent("section2.page3.rent.amount.label"))
      input.hint         shouldBe None
      input.errorMessage shouldBe None
      input.formGroup    shouldBe FormGroup.empty
      input.classes      shouldBe ""
      input.autocomplete shouldBe Some("off")
      input.pattern      shouldBe Some("^\\s*£?\\s*(?:\\d+|\\d{1,3}(?:,\\d{3})*)(?:\\.\\d{1,2})?\\s*$")
      input.attributes   shouldBe Map("maxlength" -> "13", "aria-label" -> "section2.page3.rent.amount.label")
      input.spellcheck   shouldBe Some(false)
      input.prefix       shouldBe Some(PrefixOrSuffix(content = Text("£")))
      input.suffix       shouldBe None
    }

    "return Input as page heading with 10 character width and filled value" in {
      val filledForm = form.fillAndValidate(BigDecimal(199.99))

      val input = CurrencyField.input(filledForm, "page2", "amount", isPageHeading = true, inputWidth = 10)
      input.id           shouldBe "amount"
      input.name         shouldBe "amount"
      input.inputType    shouldBe "text"
      input.inputmode    shouldBe None
      input.value        shouldBe Some("199.99")
      input.label        shouldBe Label(isPageHeading = true, classes = "govuk-label--l", content = HtmlContent("page2.amount.label"))
      input.hint         shouldBe None
      input.errorMessage shouldBe None
      input.classes      shouldBe "govuk-input--width-10"
      input.autocomplete shouldBe Some("off")
      input.prefix       shouldBe Some(PrefixOrSuffix(content = Text("£")))
    }

    "return Input with error message" in {
      val filledForm = form.bind(Map("amount" -> "abc"))

      val input = CurrencyField.input(filledForm, "", "amount", inputWidth = "two-thirds")
      input.id                          shouldBe "amount"
      input.name                        shouldBe "amount"
      input.inputType                   shouldBe "text"
      input.inputmode                   shouldBe None
      input.value                       shouldBe Some("abc")
      input.label                       shouldBe Label(classes = "govuk-!-font-weight-bold", content = HtmlContent("amount.label"))
      input.hint                        shouldBe None
      input.errorMessage.map(_.content) shouldBe Some(Text("Real number value expected"))
      input.classes                     shouldBe "govuk-!-width-two-thirds"
      input.autocomplete                shouldBe Some("off")
      input.prefix                      shouldBe Some(PrefixOrSuffix(content = Text("£")))
    }
  }
