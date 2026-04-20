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

import uk.gov.hmrc.govukfrontend.views.Aliases.{Input, Radios, Text}

import scala.collection.immutable.ArraySeq
import scala.language.implicitConversions

/**
  * @author Yuriy Tumakha
  */
implicit def arrayToSeq[T](array: Array[T]): Seq[T] = ArraySeq.unsafeWrapArray(array)

extension (input: Input)

  def withLabelText(labelText: String): Input =
    input.copy(
      label = input.label.copy(content = Text(labelText))
    )

extension (radios: Radios)

  def withLabelText(labelText: String): Radios =
    radios.copy(
      fieldset = radios.fieldset.map(fs =>
        fs.copy(legend = fs.legend.map(_.copy(content = Text(labelText))))
      )
    )
