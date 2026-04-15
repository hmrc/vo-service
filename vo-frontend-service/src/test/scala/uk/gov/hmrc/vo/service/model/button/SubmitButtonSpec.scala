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

package uk.gov.hmrc.vo.service.model.button

import uk.gov.hmrc.govukfrontend.views.Aliases.Button
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.vo.service.model.button.SubmitButton
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class SubmitButtonSpec extends BaseAppSpec:

  "SubmitButton" should {
    "implement the required properties of the parent Button class" in {
      val button: Button = SubmitButton()
      button.id                 shouldBe Some("submit-button")
      button.name               shouldBe Some("submit-button")
      button.value              shouldBe Some("submit-button")
      button.inputType          shouldBe Some("submit")
      button.isStartButton      shouldBe false
      button.content            shouldBe Text("button.submit.label")
      button.preventDoubleClick shouldBe Some(true)
      button.classes            shouldBe ""
      button.attributes         shouldBe Map.empty
      button.href               shouldBe None
    }

    "allow to override id, name, value, classes and attributes" in {
      val button: Button = SubmitButton("send", "govuk-button--warning", Map("aria-disabled" -> "true"))
      button.id                 shouldBe Some("send-button")
      button.name               shouldBe Some("send-button")
      button.value              shouldBe Some("send-button")
      button.inputType          shouldBe Some("submit")
      button.content            shouldBe Text("button.send.label")
      button.preventDoubleClick shouldBe Some(true)
      button.classes            shouldBe "govuk-button--warning"
      button.attributes         shouldBe Map("aria-disabled" -> "true")
    }
  }
