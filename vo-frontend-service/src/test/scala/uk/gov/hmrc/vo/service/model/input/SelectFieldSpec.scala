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

import org.scalactic.Prettifier.default
import play.api.data.Form
import play.api.data.Forms.{optional, single, text}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.*
import uk.gov.hmrc.govukfrontend.views.html.components.SelectItem
import uk.gov.hmrc.vo.service.model.input.LabelStyle.Medium
import uk.gov.hmrc.vo.service.view.html.*
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class SelectFieldSpec extends BaseAppSpec:

  private val form: Form[Option[String]] =
    Form(
      single(
        "category" -> optional(text)
      )
    )

  private val values = Seq("cat1", "cat2", "cat3")

  private def yesNoValues(isWelsh: Boolean): Seq[(String, String)] = Seq(
    "true"  -> (if isWelsh then "Ydw" else "Yes"),
    "false" -> (if isWelsh then "Nac ydw" else "No")
  )

  "SelectField.select" should {
    "return configured Select" in {
      val select: Select = SelectField.select(form, "section1.enquiry", "category", values)

      select.label.content       shouldBe HtmlContent("section1.enquiry.category.label")
      select.label.classes       shouldBe "govuk-!-font-weight-bold"
      select.label.isPageHeading shouldBe false

      select.id      shouldBe "category"
      select.name    shouldBe "category"
      select.hint    shouldBe None
      select.classes shouldBe ""
      select.items   shouldBe values.map { value =>
        SelectItem(
          value = Some(value),
          text = s"section1.enquiry.category.$value.label"
        )
      }
    }

    "support properties - labelText, hint, isPageHeading" in {
      given Messages = stubMessages(
        "page2.category.label" -> "Category",
        "page2.category.hint"  -> "Category hint"
      )

      val select: Select = SelectField.select(
        form,
        "page2",
        "category",
        values,
        labelText = "New Label Text",
        isPageHeading = true
      )

      select.label.content       shouldBe HtmlContent("New Label Text")
      select.label.classes       shouldBe "govuk-label--l"
      select.label.isPageHeading shouldBe true

      select.hint    shouldBe Some(Hint(content = HtmlContent("Category hint")))
      select.classes shouldBe ""
      select.items   shouldBe values.map { value =>
        SelectItem(
          value = Some(value),
          text = s"page2.category.$value.label"
        )
      }
    }

    "set for label css class `govuk-label--m`" in {
      val select: Select = SelectField.select(form, "page2", "category", values, labelStyle = Medium)

      select.label.classes       shouldBe "govuk-label--m"
      select.label.isPageHeading shouldBe false
    }

    "don't set any extra css class for label" in {
      val select: Select = SelectField.select(form, "page2", "category", values, labelStyle = None)

      select.label.classes       shouldBe ""
      select.label.isPageHeading shouldBe false
    }

    "add css class" in {
      val select: Select = SelectField.select(form, "page2", "category", values, classes = " extra-css-class ")

      select.classes shouldBe "extra-css-class"

      select.label.classes       shouldBe "govuk-!-font-weight-bold"
      select.label.isPageHeading shouldBe false
    }

    "set selected = true for selected item" in {
      val filledForm = form.fillAndValidate(Some("cat2"))

      val select: Select = SelectField.select(filledForm, "", "category", values)

      val firstItem = select.items.head
      firstItem.text     shouldBe "category.cat1.label"
      firstItem.value    shouldBe Some("cat1")
      firstItem.selected shouldBe false

      val secondItem = select.items(1)
      secondItem.text     shouldBe "category.cat2.label"
      secondItem.value    shouldBe Some("cat2")
      secondItem.selected shouldBe true
    }

    "set values with their labels" in {
      val select: Select = SelectField.select(form, "", "question", valuesWithLabels = yesNoValues(isWelsh = false))

      select.items.map(item => item.value.get -> item.text) shouldBe Seq(
        "true"  -> "Yes",
        "false" -> "No"
      )

      val selectWelsh: Select = SelectField.select(form, "", "question", valuesWithLabels = yesNoValues(isWelsh = true))

      selectWelsh.items.map(item => item.value.get -> item.text) shouldBe Seq(
        "true"  -> "Ydw",
        "false" -> "Nac ydw"
      )
    }

  }
