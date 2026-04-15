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

import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class InputWidthStyleSpec extends BaseAppSpec:

  private val toCSS: InputWidthStyle => String = _.toCssClass

  "InputWidthStyle" should {
    "convert Int to input width CSS class" in {
      toCSS(2)  shouldBe "govuk-input--width-2"
      toCSS(3)  shouldBe "govuk-input--width-3"
      toCSS(4)  shouldBe "govuk-input--width-4"
      toCSS(5)  shouldBe "govuk-input--width-5"
      toCSS(10) shouldBe "govuk-input--width-10"
      toCSS(20) shouldBe "govuk-input--width-20"
      toCSS(30) shouldBe "govuk-input--width-30"
    }

    "convert String to fluid width input CSS class" in {
      toCSS("half")                                  shouldBe "govuk-!-width-one-half"
      toCSS("two-thirds")                            shouldBe "govuk-!-width-two-thirds"
      toCSS("full")                                  shouldBe "govuk-!-width-full"
      toCSS("")                                      shouldBe ""
      toCSS("govuk-!-width-one-third")               shouldBe "govuk-!-width-one-third"
      toCSS("govuk-input govuk-!-width-one-quarter") shouldBe "govuk-input govuk-!-width-one-quarter"
    }
  }
