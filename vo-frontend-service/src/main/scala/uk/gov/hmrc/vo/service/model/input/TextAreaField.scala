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
import uk.gov.hmrc.govukfrontend.views.viewmodels.FormGroup
import uk.gov.hmrc.hmrcfrontend.views.Aliases.CharacterCount
import uk.gov.hmrc.hmrcfrontend.views.Implicits.*
import uk.gov.hmrc.hmrcfrontend.views.viewmodels.language.{Cy, En}
import uk.gov.hmrc.vo.service.model.input.LabelStyle.Bold

/**
  * Parameters to `HmrcCharacterCount` Twirl template.
  *
  * @author Yuriy Tumakha
  */
object TextAreaField extends FieldPropertyFormats:

  def characterCount(
    theForm: Form[?],
    prefix: String,
    name: String,
    rows: Int = 5,
    maxLength: Int = 1000,
    labelText: Option[String] = None,
    labelStyle: Option[LabelStyle] = Some(Bold),
    isPageHeading: Boolean = false,
    hideLabel: Boolean = false,
    inputWidth: InputWidthStyle = "",
    formGroupClasses: Option[String] = None,
    attributes: Map[String, String] = Map.empty
  )(using messages: Messages
  ): CharacterCount =
    CharacterCount(
      rows = rows,
      maxLength = Some(maxLength),
      label = buildInputLabel(isPageHeading, hideLabel, labelText, labelStyle, prefix, name),
      hint = fieldHint(prefix, name),
      classes = inputWidth.toCssClass,
      formGroup = FormGroup(classes = formGroupClasses),
      language = if messages.lang.language == Cy.code then Cy else En,
      attributes = attributes
    ).withFormField(theForm(name))
