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
import play.api.test.Helpers.*
import uk.gov.hmrc.govukfrontend.views.Aliases.*
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class RadioFieldSpec extends BaseAppSpec:

  val feedbackForm: Form[(Int, Option[String])] =
    Form(
      tuple(
        "satisfaction" -> number,
        "comments"     -> optional(text)
      )
    )

  "RadioField.radios" should {
    "return configured Radios" in {
      given Messages = messagesApi.preferred(Seq.empty)

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

    "support properties - hint, isPageHeading, inline" in {
      given Messages = stubMessagesApi(
        Map("en" -> Map(
          "feedback.satisfaction.label" -> "Satisfaction",
          "feedback.satisfaction.hint"  -> "Satisfaction hint"
        ))
      ).preferred(Seq.empty)

      val values         = 5 to 1 by -1
      val radios: Radios = RadioField.radios(feedbackForm, "feedback", "satisfaction", values, isPageHeading = false, inline = true)
      val legend         = radios.fieldset.get.legend.get

      legend.content       shouldBe HtmlContent("Satisfaction")
      legend.classes       shouldBe "govuk-fieldset__legend--m"
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
  }
