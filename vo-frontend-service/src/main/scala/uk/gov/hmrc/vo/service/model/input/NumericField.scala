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
import uk.gov.hmrc.govukfrontend.views.Aliases.Input
import uk.gov.hmrc.govukfrontend.views.html.components.implicits.*
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Content

/**
  * Parameters to `GovukInput` Twirl template.
  *
  * @author Yuriy Tumakha
  */
object NumericField extends FieldPropertyFormats:

  def input(
    theForm: Form[?],
    prefix: String,
    name: String,
    ariaLabel: Option[String] = None, // Field label by default
    isPageHeading: Boolean = false,
    hideLabel: Boolean = false,
    inputWidth: InputWidthStyle = "",
    maxlength: Int = 8,
    prefixContent: Option[Content] = None,
    suffixContent: Option[Content] = None,
    attributes: Map[String, String] = Map.empty
  )(using messages: Messages
  ): Input =
    TextField.input(
      theForm,
      prefix,
      name,
      inputMode = Some("numeric"),
      pattern = Some("^\\d*$"),
      autocomplete = Some("off"),
      spellcheck = Some(false),
      isPageHeading = isPageHeading,
      hideLabel = hideLabel,
      inputWidth = inputWidth,
      prefixContent = prefixContent,
      suffixContent = suffixContent,
      attributes = Map(
        "maxlength"  -> maxlength.toString,
        "aria-label" -> ariaLabel.getOrElse(fieldLabel(prefix, name))
      ) ++ attributes
    ).withFormField(theForm(name))
