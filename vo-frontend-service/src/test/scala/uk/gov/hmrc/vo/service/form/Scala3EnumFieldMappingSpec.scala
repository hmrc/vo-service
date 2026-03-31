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

package uk.gov.hmrc.vo.service.form

import play.api.data.Forms.mapping
import play.api.data.{Form, FormError}
import play.api.libs.json.Format
import uk.gov.hmrc.vo.service.form.Scala3EnumFieldMapping.*
import uk.gov.hmrc.vo.service.model.Scala3EnumJsonFormat
import uk.gov.hmrc.vo.unit.test.BaseSpec

/**
  * @author Yuriy Tumakha
  */
class Scala3EnumFieldMappingSpec extends BaseSpec:

  enum Color:
    case Red, Green, Blue

  given Format[Color] = Scala3EnumJsonFormat.format

  import Color.*

  case class ColorsPage(color: Color, colorOpt: Option[Color] = None, colors: Seq[Color] = Seq.empty)

  val colorsForm: Form[ColorsPage] = Form(
    mapping(
      "color"    -> enumMappingRequired(Color),
      "colorOpt" -> enumMapping(Color),
      "colors"   -> enumMappingSeq(Color)
    )(ColorsPage.apply)(o => Some(Tuple.fromProductTyped(o)))
  )

  "Scala3EnumFieldMapping" should {
    "return error about required color for empty data" in {
      val form = colorsForm.bind(Map.empty)

      form.errors shouldBe Seq(FormError("color", List("error.required")))
      form.value  shouldBe None
    }

    "return error about required color if 'color' param is missed" in {
      val data = Map(
        "colorOpt"  -> "Blue",
        "colors[0]" -> "Green",
        "colors[1]" -> "Red"
      )
      val form = colorsForm.bind(data)

      form.errors shouldBe Seq(FormError("color", List("error.required")))
      form.value  shouldBe None
    }

    "bind full data without errors " in {
      val data = Map(
        "color"     -> "Red",
        "colorOpt"  -> "Blue",
        "colors[0]" -> "Green",
        "colors[1]" -> "Red"
      )
      val form = colorsForm.bind(data)

      form.errors      shouldBe empty
      form.value.value shouldBe ColorsPage(Red, Some(Blue), Seq(Green, Red))
    }

    "bind minimal data without errors" in {
      val data = Map(
        "color" -> "Green"
      )
      val form = colorsForm.bind(data)

      form.errors      shouldBe empty
      form.value.value shouldBe ColorsPage(Green)
    }

    "fill form with ColorsPage value without errors" in {
      val value = ColorsPage(Red, Some(Blue), Seq(Green, Red))
      val form  = colorsForm.fillAndValidate(value)

      form.errors      shouldBe empty
      form.value.value shouldBe value
    }

  }
