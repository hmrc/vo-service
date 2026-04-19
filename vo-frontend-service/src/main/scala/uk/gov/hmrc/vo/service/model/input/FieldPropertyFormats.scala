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

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.{Content, Hint, HtmlContent, Label, Text}

/**
  * @author Yuriy Tumakha
  */
trait FieldPropertyFormats:

  private def removeBracketParts(input: String): String =
    input.replaceAll("\\[.*?]", "")

  private def fieldPropertyFormat[T](fieldParts: Seq[String], property: String): String =
    s"${fieldParts.map(removeBracketParts).filter(_.nonEmpty).mkString(".")}.$property"

  private def fieldItemPropertyFormat[T](fieldParts: Seq[String], itemValue: T, property: String): String =
    s"${fieldParts.map(removeBracketParts).filter(_.nonEmpty).mkString(".")}.$itemValue.$property"

  private def hintIfDefined(hintKey: String)(using messages: Messages): Option[Hint] =
    Option.when(messages.isDefinedAt(hintKey))(Hint(content = HtmlContent(messages(hintKey))))

  def fieldLabel(fieldParts: String*)(using messages: Messages): String =
    messages(fieldPropertyFormat(fieldParts, "label"))

  /**
    * Uses `labelText` if present, otherwise combines `fieldParts`.
    */
  def fieldLabelAsContent(labelText: Option[String], fieldParts: String*)(using messages: Messages): Content =
    HtmlContent(labelText.getOrElse(fieldLabel(fieldParts*)))

  def fieldHint(fieldParts: String*)(using messages: Messages): Option[Hint] =
    hintIfDefined(fieldPropertyFormat(fieldParts, "hint"))

  def itemLabel[T](itemValue: T, fieldParts: String*)(using messages: Messages): String =
    messages(fieldItemPropertyFormat(fieldParts, itemValue, "label"))

  def itemHint[T](itemValue: T, fieldParts: String*)(using messages: Messages): Option[Hint] =
    hintIfDefined(fieldItemPropertyFormat(fieldParts, itemValue, "hint"))

  def buildInputLabel(isPageHeading: Boolean, hideLabel: Boolean, labelText: Option[String], fieldParts: String*)(using messages: Messages): Label =
    if hideLabel then
      Label()
    else
      Label(
        isPageHeading = isPageHeading,
        content = fieldLabelAsContent(labelText, fieldParts*),
        classes = if isPageHeading then "govuk-label--l" else "govuk-!-font-weight-bold"
      )

  def combineClasses(classes: Option[String]*): String =
    classes
      .flatten
      .flatMap(_.trim.split("\\s+"))
      .filter(_.nonEmpty)
      .mkString(" ")
