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

/**
  * @author Yuriy Tumakha
  */
object InputWidthStyle:

  type InputWidthStyle = 2 | 3 | 4 | 5 | 10 | 20 | 30 | "half" | "two-thirds" | "full" | "" | String

  extension (w: InputWidthStyle)

    def toCssClass: String = w match
      case width: Int       => s"govuk-input--width-$width"
      case "half"           => "govuk-!-width-one-half"
      case "two-thirds"     => "govuk-!-width-two-thirds"
      case "full"           => "govuk-!-width-full"
      case cssClass: String => cssClass
