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
import uk.gov.hmrc.govukfrontend.views.Aliases.{Content, Fieldset, Hint, HtmlContent, Legend, RadioItem, Radios, Text}
import uk.gov.hmrc.govukfrontend.views.html.components.implicits.*

/**
  * @author Yuriy Tumakha
  */
object RadioField:

  private def propertyFormat[T](fieldParts: Seq[String], property: String): String =
    s"${fieldParts.mkString(".")}.$property"

  private def itemPropertyFormat[T](fieldParts: Seq[String], value: T, property: String): String =
    s"${fieldParts.mkString(".")}.$value.$property"

  private def hintIfDefined(hintKey: String)(using messages: Messages): Option[Hint] =
    Option.when(messages.isDefinedAt(hintKey))(Hint(content = HtmlContent(messages(hintKey))))

  def legend(fieldParts: String*)(using messages: Messages): Content =
    HtmlContent(messages(propertyFormat(fieldParts, "label")))

  def hint(fieldParts: String*)(using messages: Messages): Option[Hint] =
    hintIfDefined(propertyFormat(fieldParts, "hint"))

  def itemLabel[T](value: T, fieldParts: String*)(using messages: Messages): Content =
    Text(messages(itemPropertyFormat(fieldParts, value, "label")))

  def itemHint[T](value: T, fieldParts: String*)(using messages: Messages): Option[Hint] =
    hintIfDefined(itemPropertyFormat(fieldParts, value, "hint"))

  def radios[T](
    theForm: Form[?],
    prefix: String,
    name: String,
    values: Seq[T],
    isPageHeading: Boolean = true,
    inline: Boolean = false
  )(using messages: Messages
  ): Radios =
    Radios(
      fieldset = Some(
        Fieldset(
          legend = Some(
            Legend(
              content = legend(prefix, name),
              classes = if isPageHeading then "govuk-fieldset__legend--l" else "govuk-fieldset__legend--m",
              isPageHeading = isPageHeading
            )
          )
        )
      ),
      hint = hint(prefix, name),
      items = values.map { value =>
        RadioItem(
          content = itemLabel(value, prefix, name),
          hint = itemHint(value, prefix, name),
          value = Some(value.toString)
        )
      },
      classes = if (inline) "govuk-radios--inline" else ""
    ).withFormField(theForm(name))
