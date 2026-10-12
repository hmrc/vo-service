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

import uk.gov.hmrc.vo.unit.test.BaseSpec

import java.time.Month.*

/**
  * @author Yuriy Tumakha
  */
class CymraegLangSupportSpec extends BaseSpec with CymraegLangSupport:

  "CymraegLangSupport.translateMonth" should {
    "replace the English month name in a string with its Welsh equivalent" in {
      translateMonth("1 January 2026", JANUARY)     shouldBe "1 Ionawr 2026"
      translateMonth("2 February 2026", FEBRUARY)   shouldBe "2 Chwefror 2026"
      translateMonth("3 March 2026", MARCH)         shouldBe "3 Mawrth 2026"
      translateMonth("4 April 2026", APRIL)         shouldBe "4 Ebrill 2026"
      translateMonth("5 May 2026", MAY)             shouldBe "5 Mai 2026"
      translateMonth("6 June 2026", JUNE)           shouldBe "6 Mehefin 2026"
      translateMonth("7 July 2026", JULY)           shouldBe "7 Gorffennaf 2026"
      translateMonth("8 August 2026", AUGUST)       shouldBe "8 Awst 2026"
      translateMonth("9 September 2026", SEPTEMBER) shouldBe "9 Medi 2026"
      translateMonth("10 October 2026", OCTOBER)    shouldBe "10 Hydref 2026"
      translateMonth("11 November 2026", NOVEMBER)  shouldBe "11 Tachwedd 2026"
      translateMonth("12 December 2026", DECEMBER)  shouldBe "12 Rhagfyr 2026"
    }
  }
