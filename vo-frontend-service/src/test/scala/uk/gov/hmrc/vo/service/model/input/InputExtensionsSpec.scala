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
import uk.gov.hmrc.vo.unit.test.BaseAppSpec
import play.api.data.Forms.{number, optional, single}
import uk.gov.hmrc.govukfrontend.views.Aliases.{HtmlContent, Text}
import uk.gov.hmrc.govukfrontend.views.viewmodels.label.Label

/**
  * @author Yuriy Tumakha
  */
class InputExtensionsSpec extends BaseAppSpec:

  val form: Form[Option[Int]] =
    Form(
      single(
        "some.uncommon.field" -> optional(number)
      )
    )

  "Input extensions" should {
    "add method .withLabelText to override input label text" in {
      val input = TextField.input(form, "section8.page13", "some.uncommon.field")

      input.label shouldBe Label(
        classes = "govuk-!-font-weight-bold",
        content = HtmlContent("section8.page13.some.uncommon.field.label")
      )

      input.withLabelText("New label text").label shouldBe Label(
        classes = "govuk-!-font-weight-bold",
        content = Text("New label text")
      )
    }
  }
