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

package uk.gov.hmrc.vo.service.conversion

import uk.gov.hmrc.vo.service.conversion.IbmIcuDateUtil
import uk.gov.hmrc.vo.unit.test.BaseSpec

import java.time.LocalDate
import java.time.Month.*

/**
  * @author Yuriy Tumakha
  */
class IbmIcuDateUtilSpec extends BaseSpec:

  "IbmIcuDateUtil.formatDate" should {
    "return date in format 'd MMMM yyyy' using language 'cy'" in {
      val cyDateUtil = IbmIcuDateUtil()
      cyDateUtil.formatDate(LocalDate.of(2026, JANUARY, 1))   shouldBe "1 Ionawr 2026"
      cyDateUtil.formatDate(LocalDate.of(2026, FEBRUARY, 22)) shouldBe "22 Chwefror 2026"
      cyDateUtil.formatDate(LocalDate.of(2026, AUGUST, 31))   shouldBe "31 Awst 2026"
    }

    "use language 'en' for unsupported language 'xx'" in {
      val ibmIcuDateUtil = IbmIcuDateUtil("xx")
      ibmIcuDateUtil.formatDate(LocalDate.of(2026, JANUARY, 1))   shouldBe "1 January 2026"
      ibmIcuDateUtil.formatDate(LocalDate.of(2026, FEBRUARY, 22)) shouldBe "22 February 2026"
    }
  }
