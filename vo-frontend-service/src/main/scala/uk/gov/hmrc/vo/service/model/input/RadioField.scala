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
import uk.gov.hmrc.govukfrontend.views.Aliases.{Fieldset, Legend, RadioItem, Radios, Text}
import uk.gov.hmrc.govukfrontend.views.html.components.implicits.*
import uk.gov.hmrc.vo.service.model.input.LabelStyle.Bold

/**
  * Parameters to `GovukRadios` Twirl template.
  *
  * @author Yuriy Tumakha
  */
object RadioField extends FieldPropertyFormats:

  def radios[T](
    theForm: Form[?],
    prefix: String,
    name: String,
    values: Seq[T] = Seq.empty,
    valuesWithLabels: Option[Seq[(String, String)]] = None, // If `valuesWithLabels` is specified, then the `values` parameter is skipped
    labelText: Option[String] = None,
    labelStyle: Option[LabelStyle] = Some(Bold),
    isPageHeading: Boolean = true,
    inline: Boolean = false,
    classes: Option[String] = None
  )(using messages: Messages
  ): Radios =
    Radios(
      fieldset = Some(
        Fieldset(
          legend = Some(
            Legend(
              content = fieldLabelAsContent(labelText, prefix, name),
              classes = if isPageHeading then "govuk-fieldset__legend--l" else labelStyle.fold("")(_.cssClass),
              isPageHeading = isPageHeading
            )
          )
        )
      ),
      hint = fieldHint(prefix, name),
      items = valuesWithLabels.getOrElse(
        values.map(value => value.toString -> itemLabel(value, prefix, name))
      ).map {
        case (value, label) =>
          RadioItem(
            content = Text(label),
            hint = itemHint(value, prefix, name),
            value = Some(value)
          )
      },
      classes = combineClasses(Option.when(inline)("govuk-radios--inline"), classes)
    ).withFormField(theForm(name))
