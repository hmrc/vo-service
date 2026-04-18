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
import uk.gov.hmrc.govukfrontend.views.Aliases.{Fieldset, Legend, RadioItem, Radios}
import uk.gov.hmrc.govukfrontend.views.html.components.implicits.*

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
    values: Seq[T],
    labelText: Option[String] = None,
    isPageHeading: Boolean = true,
    inline: Boolean = false
  )(using messages: Messages
  ): Radios =
    Radios(
      fieldset = Some(
        Fieldset(
          legend = Some(
            Legend(
              content = fieldLabelAsContent(labelText, prefix, name),
              classes = if isPageHeading then "govuk-fieldset__legend--l" else "govuk-!-font-weight-bold",
              isPageHeading = isPageHeading
            )
          )
        )
      ),
      hint = fieldHint(prefix, name),
      items = values.map { value =>
        RadioItem(
          content = itemLabel(value, prefix, name),
          hint = itemHint(value, prefix, name),
          value = Some(value.toString)
        )
      },
      classes = if (inline) "govuk-radios--inline" else ""
    ).withFormField(theForm(name))
