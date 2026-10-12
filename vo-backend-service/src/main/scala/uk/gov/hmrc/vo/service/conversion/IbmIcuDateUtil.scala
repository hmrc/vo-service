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

import com.ibm.icu.text.SimpleDateFormat
import com.ibm.icu.util.{TimeZone, ULocale}

import java.time.{LocalDate, ZoneId}

/**
  * Supports the Welsh (cy) locale.
  *
  * @author Yuriy Tumakha
  */
class IbmIcuDateUtil(language: String = "cy"):

  private val defaultUKTimeZone = TimeZone.getTimeZone("Europe/London")
  private val zoneId            = ZoneId.of(defaultUKTimeZone.getID)
  private val cyLocale          = ULocale(language)
  private val locale            = if ULocale.getAvailableLocales.contains(cyLocale) then cyLocale else ULocale.getDefault
  private val longDateFormat    = "d MMMM yyyy"

  /**
    * Format date in "d MMMM yyyy".
    */
  def formatDate(date: LocalDate): String =
    val sdf = SimpleDateFormat(longDateFormat, locale)
    sdf.setTimeZone(defaultUKTimeZone)
    sdf.format(date.atStartOfDay(zoneId).toInstant.toEpochMilli)
