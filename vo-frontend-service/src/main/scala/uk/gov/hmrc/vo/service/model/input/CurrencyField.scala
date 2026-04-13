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
import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.{Input, Text}
import uk.gov.hmrc.govukfrontend.views.html.components.implicits.*
import uk.gov.hmrc.vo.service.model.input.FieldPropertyFormats.fieldLabel
import WidthOrClass.*

/**
  * Parameters to `GovukInput` Twirl template.
  *
  * @author Yuriy Tumakha
  */
object CurrencyField:

  def input[T](
    theForm: Form[?],
    prefix: String,
    name: String,
    ariaLabel: Option[String] = None, // Field label by default
    inputMode: Option[String] = None, // "numeric"
    pattern: Option[String] = Some("^\\s*£?\\s*(?:\\d+|\\d{1,3}(?:,\\d{3})*)(?:\\.\\d{1,2})?\\s*$"),
    isPageHeading: Boolean = false,
    hideLabel: Boolean = false,
    inputWidthOrClass: WidthOrClass = "",
    maxlength: Int = 13,
    attributes: Map[String, String] = Map.empty
  )(using messages: Messages
  ): Input =
    TextField.input(
      theForm,
      prefix,
      name,
      prefixContent = Some(Text("£")),
      inputMode = inputMode,
      pattern = pattern,
      autocomplete = Some("off"),
      isPageHeading = isPageHeading,
      labelClasses = if isPageHeading then "govuk-label--l" else "govuk-!-font-weight-bold",
      hideLabel = hideLabel,
      inputWidthOrClass = inputWidthOrClass,
      attributes = Map(
        "maxlength"  -> maxlength.toString,
        "aria-label" -> ariaLabel.getOrElse(fieldLabel(prefix, name))
      ) ++ attributes
    ).withFormField(theForm(name))
