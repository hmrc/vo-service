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
import uk.gov.hmrc.govukfrontend.views.Aliases.*
import uk.gov.hmrc.govukfrontend.views.html.components.implicits.*
import uk.gov.hmrc.govukfrontend.views.viewmodels.FormGroup
import uk.gov.hmrc.vo.service.model.input.LabelStyle.Bold

/**
  * Parameters to `GovukSelect` Twirl template.
  *
  * @author Yuriy Tumakha
  */
object SelectField extends FieldPropertyFormats:

  def select[T](
    theForm: Form[?],
    prefix: String,
    name: String,
    values: Seq[T] = Seq.empty,
    valuesWithLabels: Option[Seq[(String, String)]] = None, // If `valuesWithLabels` is specified, then the `values` parameter is skipped
    labelText: Option[String] = None,
    labelStyle: Option[LabelStyle] = Some(Bold),
    isPageHeading: Boolean = false,
    hideLabel: Boolean = false,
    classes: Option[String] = None,
    formGroupClasses: Option[String] = None,
    attributes: Map[String, String] = Map.empty
  )(using messages: Messages
  ): Select =
    Select(
      label = buildInputLabel(isPageHeading, hideLabel, labelText, labelStyle, prefix, name),
      hint = fieldHint(prefix, name),
      items = valuesWithLabels.getOrElse(
        values.map(value => value.toString -> itemLabel(value, prefix, name))
      ).map {
        case (value, label) => SelectItem(value = Some(value), text = label)
      },
      classes = combineClasses(classes),
      formGroup = FormGroup(classes = formGroupClasses),
      attributes = attributes
    ).withFormField(theForm(name))
