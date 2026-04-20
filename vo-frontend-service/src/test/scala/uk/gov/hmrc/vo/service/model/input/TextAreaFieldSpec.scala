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
import play.api.i18n.{Lang, Messages, MessagesImpl}
import uk.gov.hmrc.govukfrontend.views.Aliases.{Hint, HtmlContent}
import uk.gov.hmrc.govukfrontend.views.viewmodels.FormGroup
import uk.gov.hmrc.govukfrontend.views.viewmodels.label.Label
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.language.{Cy, En}
import uk.gov.hmrc.vo.service.view.html.*
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

import java.util.Locale

/**
  * @author Yuriy Tumakha
  */
class TextAreaFieldSpec extends BaseAppSpec:

  val form: Form[String] =
    Form(
      single(
        "comments" -> text.verifying(nonEmpty)
      )
    )

  "TextAreaField.characterCount" should {
    "return CharacterCount parameters for HmrcCharacterCount Twirl template" in {
      val characterCount = TextAreaField.characterCount(form, "page10", "comments")
      characterCount.id           shouldBe "comments"
      characterCount.name         shouldBe "comments"
      characterCount.value        shouldBe None
      characterCount.rows         shouldBe 5
      characterCount.maxLength    shouldBe Some(1000)
      characterCount.language     shouldBe En
      characterCount.label        shouldBe Label(classes = "govuk-!-font-weight-bold", content = HtmlContent("page10.comments.label"))
      characterCount.hint         shouldBe None
      characterCount.errorMessage shouldBe None
      characterCount.formGroup    shouldBe FormGroup.empty
      characterCount.classes      shouldBe ""
      characterCount.attributes   shouldBe Map.empty
    }

    "support properties - rows, maxLength, labelText, hint, isPageHeading, inputWidth" in {
      given Messages = stubMessages(
        "page13.comments.label" -> "Feedback comments",
        "page13.comments.hint"  -> "Comments hint"
      )

      val characterCount = TextAreaField.characterCount(
        form,
        "page13",
        "comments",
        rows = 4,
        maxLength = 250,
        labelText = "New Label Text",
        isPageHeading = true,
        inputWidth = 20
      )

      characterCount.id           shouldBe "comments"
      characterCount.name         shouldBe "comments"
      characterCount.value        shouldBe None
      characterCount.rows         shouldBe 4
      characterCount.maxLength    shouldBe Some(250)
      characterCount.label        shouldBe Label(isPageHeading = true, classes = "govuk-label--l", content = HtmlContent("New Label Text"))
      characterCount.hint         shouldBe Some(Hint(content = HtmlContent("Comments hint")))
      characterCount.errorMessage shouldBe None
      characterCount.formGroup    shouldBe FormGroup.empty
      characterCount.classes      shouldBe "govuk-input--width-20"
      characterCount.attributes   shouldBe Map.empty
    }

    "use language code form Messages" in {
      given cyMessages: Messages = MessagesImpl(Lang(Locale.of("cy")), messagesApi)

      cyMessages.lang.language shouldBe "cy"

      val characterCount = TextAreaField.characterCount(form, "section1.page2", "comments")

      characterCount.language shouldBe Cy
    }

  }
