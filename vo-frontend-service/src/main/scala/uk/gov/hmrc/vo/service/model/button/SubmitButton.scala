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

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.Button
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Text
import uk.gov.hmrc.vo.service.model.button.SubmitButton.{buttonIdFormat, buttonLabelFormat}

/**
  * @author Yuriy Tumakha
  */
class SubmitButton(
  prefix: String = "submit",
  classes: String = "",
  attributes: Map[String, String] = Map.empty
)(using messages: Messages
) extends Button(
    id = Some(buttonIdFormat(prefix)),
    name = Some(buttonIdFormat(prefix)),
    value = Some(buttonIdFormat(prefix)),
    inputType = Some("submit"),
    content = Text(buttonLabelFormat(prefix)),
    preventDoubleClick = Some(true),
    classes = classes,
    attributes = attributes
  )

object SubmitButton:

  def buttonIdFormat(prefix: String): String =
    s"$prefix-button"

  def buttonLabelFormat(prefix: String)(using messages: Messages): String =
    messages(s"button.$prefix.label")
