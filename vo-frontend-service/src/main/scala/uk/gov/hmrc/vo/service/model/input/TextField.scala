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
import uk.gov.hmrc.govukfrontend.views.Aliases.{Input, Label}
import uk.gov.hmrc.govukfrontend.views.html.components.implicits.*
import uk.gov.hmrc.govukfrontend.views.viewmodels.FormGroup
import uk.gov.hmrc.govukfrontend.views.viewmodels.content.Content
import uk.gov.hmrc.govukfrontend.views.viewmodels.input.PrefixOrSuffix
import uk.gov.hmrc.vo.service.model.input.FieldPropertyFormats.{fieldHint, fieldLabelAsContent}
import WidthOrClass.*

/**
  * Parameters to `GovukInput` Twirl template.
  *
  * @author Yuriy Tumakha
  */
object TextField:

  def input[T](
    theForm: Form[?],
    prefix: String,
    name: String,
    inputType: String = "text",
    inputMode: Option[String] = None,
    pattern: Option[String] = None,
    autocomplete: Option[String] = None,
    ariaDescribedBy: Option[String] = None,
    isPageHeading: Boolean = false,
    hideLabel: Boolean = false,
    inputWidthOrClass: WidthOrClass = "",
    labelClasses: String = "",
    formGroupClasses: Option[String] = None,
    spellcheck: Option[Boolean] = None,
    prefixContent: Option[Content] = None,
    suffixContent: Option[Content] = None,
    attributes: Map[String, String] = Map.empty
  )(using messages: Messages
  ): Input =
    Input(
      inputType = inputType,
      inputmode = inputMode,
      pattern = pattern,
      autocomplete = autocomplete,
      describedBy = ariaDescribedBy,
      classes = inputWidthOrClass.toCssClass,
      formGroup = FormGroup(classes = formGroupClasses),
      label = if hideLabel then Label() else Label(isPageHeading = isPageHeading, content = fieldLabelAsContent(prefix, name), classes = labelClasses),
      hint = fieldHint(prefix, name),
      spellcheck = spellcheck,
      prefix = prefixContent.map(c => PrefixOrSuffix(content = c)),
      suffix = suffixContent.map(c => PrefixOrSuffix(content = c)),
      attributes = attributes
    ).withFormField(theForm(name))
