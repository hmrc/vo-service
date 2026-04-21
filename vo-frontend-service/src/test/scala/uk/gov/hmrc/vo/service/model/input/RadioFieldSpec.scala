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
import play.api.data.Forms.{number, optional, text, tuple}
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.*
import uk.gov.hmrc.vo.service.model.input.LabelStyle.Medium
import uk.gov.hmrc.vo.service.view.html.*
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class RadioFieldSpec extends BaseAppSpec:

  private val feedbackForm: Form[(Int, Option[String])] =
    Form(
      tuple(
        "satisfaction" -> number,
        "comments"     -> optional(text)
      )
    )

  private def yesNoValues(isWelsh: Boolean): Seq[(String, String)] = Seq(
    "true"  -> (if isWelsh then "Ydw" else "Yes"),
    "false" -> (if isWelsh then "Nac ydw" else "No")
  )

  "RadioField.radios" should {
    "return configured Radios" in {
      val values         = 5 to 1 by -1
      val radios: Radios = RadioField.radios(feedbackForm, "feedback", "satisfaction", values)
      val legend         = radios.fieldset.get.legend.get

      legend.content       shouldBe HtmlContent("feedback.satisfaction.label")
      legend.classes       shouldBe "govuk-fieldset__legend--l"
      legend.isPageHeading shouldBe true

      radios.idPrefix shouldBe Some("satisfaction")
      radios.name     shouldBe "satisfaction"
      radios.hint     shouldBe None
      radios.classes  shouldBe ""
      radios.items    shouldBe values.map { value =>
        RadioItem(
          content = Text(s"feedback.satisfaction.$value.label"),
          value = Some(s"$value")
        )
      }
    }

    "support properties - labelText, hint, isPageHeading, inline" in {
      given Messages = stubMessages(
        "feedback.satisfaction.label" -> "Satisfaction",
        "feedback.satisfaction.hint"  -> "Satisfaction hint"
      )

      val values         = 5 to 1 by -1
      val radios: Radios = RadioField.radios(
        feedbackForm,
        "feedback",
        "satisfaction",
        values,
        labelText = "New Label Text",
        isPageHeading = false,
        inline = true
      )

      val legend = radios.fieldset.get.legend.get

      legend.content       shouldBe HtmlContent("New Label Text")
      legend.classes       shouldBe "govuk-!-font-weight-bold"
      legend.isPageHeading shouldBe false

      radios.hint    shouldBe Some(Hint(content = HtmlContent("Satisfaction hint")))
      radios.classes shouldBe "govuk-radios--inline"
      radios.items   shouldBe values.map { value =>
        RadioItem(
          content = Text(s"feedback.satisfaction.$value.label"),
          value = Some(s"$value")
        )
      }
    }

    "set for legend css class `govuk-label--m`" in {
      val radios: Radios = RadioField.radios(
        feedbackForm,
        "feedback",
        "satisfaction",
        5 to 1 by -1,
        labelStyle = Medium,
        isPageHeading = false
      )

      val legend = radios.fieldset.get.legend.get

      legend.classes       shouldBe "govuk-label--m"
      legend.isPageHeading shouldBe false
    }

    "don't set any extra css class for legend" in {
      val radios: Radios = RadioField.radios(
        feedbackForm,
        "feedback",
        "satisfaction",
        5 to 1 by -1,
        labelStyle = None,
        isPageHeading = false
      )

      val legend = radios.fieldset.get.legend.get

      legend.classes       shouldBe ""
      legend.isPageHeading shouldBe false
    }

    "add css class" in {
      val radios: Radios = RadioField.radios(
        feedbackForm,
        "feedback",
        "satisfaction",
        5 to 1 by -1,
        classes = " extra-css-class "
      )

      radios.classes shouldBe "extra-css-class"

      val legend = radios.fieldset.get.legend.get

      legend.classes       shouldBe "govuk-fieldset__legend--l"
      legend.isPageHeading shouldBe true
    }

    "add an extra classes while keeping `govuk-radios--inline`" in {
      val radios: Radios = RadioField.radios(
        feedbackForm,
        "feedback",
        "satisfaction",
        5 to 1 by -1,
        inline = true,
        classes = "extra-css-class    class2   class3 "
      )

      radios.classes shouldBe "govuk-radios--inline extra-css-class class2 class3"

      val legend = radios.fieldset.get.legend.get

      legend.classes       shouldBe "govuk-fieldset__legend--l"
      legend.isPageHeading shouldBe true
    }

    "set checked = true for selected item" in {
      val values = 5 to 1 by -1
      val form   = feedbackForm.fillAndValidate((4, None))

      val radios: Radios = RadioField.radios(form, "feedback", "satisfaction", values)

      val firstItem = radios.items.head
      firstItem.content shouldBe Text("feedback.satisfaction.5.label")
      firstItem.value   shouldBe Some("5")
      firstItem.checked shouldBe false

      val secondItem = radios.items(1)
      secondItem.content shouldBe Text("feedback.satisfaction.4.label")
      secondItem.value   shouldBe Some("4")
      secondItem.checked shouldBe true
    }

    "set values with their labels" in {
      val radios: Radios = RadioField.radios(feedbackForm, "page1", "field1", valuesWithLabels = yesNoValues(isWelsh = false))

      radios.items.map(item => item.value.get -> item.content.asHtml.toString) shouldBe Seq(
        "true"  -> "Yes",
        "false" -> "No"
      )

      val radiosWelsh: Radios = RadioField.radios(feedbackForm, "page1", "field1", valuesWithLabels = yesNoValues(isWelsh = true))

      radiosWelsh.items.map(item => item.value.get -> item.content.asHtml.toString) shouldBe Seq(
        "true"  -> "Ydw",
        "false" -> "Nac ydw"
      )
    }

  }
