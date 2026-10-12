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

import play.api.Logging
import play.api.i18n.{Lang, Messages}

import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit.SECONDS
import java.time.*
import java.util.Locale
import scala.language.implicitConversions
import scala.util.{Failure, Try}

/**
  * @author Yuriy Tumakha
  */
object DateConversion extends CymraegLangSupport with Logging:

  val ukTimezone: ZoneId = ZoneId.of("Europe/London")

  val en: Lang = Lang(Locale.UK)
  val cy: Lang = Lang(Locale.of("cy"))

  // Date
  private val longDateFormat   = DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.UK)
  private val mediumDateFormat = DateTimeFormatter.ofPattern("d MMM yyyy", Locale.UK)
  private val slashDateFormat  = DateTimeFormatter.ofPattern("dd/MM/yyyy", Locale.UK)
  private val isoDateFormat    = DateTimeFormatter.ISO_LOCAL_DATE // "yyyy-MM-dd"

  // Time
  private val timeFormat = DateTimeFormatter.ofPattern("HH:mm", Locale.UK)

  // DateTime
  private val longDateTimeFormat     = DateTimeFormatter.ofPattern("d MMMM yyyy 'at' HH:mm", Locale.UK)
  private val mediumDateTimeFormat   = DateTimeFormatter.ofPattern("d MMM yyyy HH:mm", Locale.UK)
  private val isoSpaceDateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.UK)
  private val isoDateTimeFormat      = DateTimeFormatter.ISO_DATE_TIME // "yyyy-MM-dd'T'HH:mm:ssXXX'['VV']'"  - 2011-12-03T10:15:30+01:00[Europe/Paris]

  /**
    * Current time in zone Europe/London.
    */
  def nowInUK: ZonedDateTime = ZonedDateTime.now(ukTimezone).truncatedTo(SECONDS)

  /**
    * Current time in zone UTC in format "yyyy-MM-dd'T'HH:mm:ss'Z'".
    *
    * Example: 2026-10-22T13:45:55Z
    */
  def nowIsoDateTimeUTC: String = Instant.now.formatIsoDateTimeUTC

  extension (instant: Instant)

    /**
      * Format Instant in zone UTC in format "yyyy-MM-dd'T'HH:mm:ss'Z'".
      *
      * Example: 2026-12-31T13:45:55Z
      */
    def formatIsoDateTimeUTC: String = instant.truncatedTo(SECONDS).atOffset(ZoneOffset.UTC).format(isoDateTimeFormat)

  extension (zonedDateTime: ZonedDateTime)

    /**
      * Format in "d MMMM yyyy 'at' HH:mm".
      *
      * Example: 2 February 2026 at 09:45
      */
    def formatLongDateTime: String = longDateTimeFormat.format(zonedDateTime)

    /**
      * Format in "d MMM yyyy HH:mm".
      *
      * Example: 17 Mar 2026 13:45
      */
    def formatMediumDateTime: String = mediumDateTimeFormat.format(zonedDateTime)

    /**
      * Format in "yyyy-MM-dd HH:mm:ss".
      *
      * Example: 2026-03-17 13:45:55
      */
    def formatIsoSpaceDateTime: String = isoSpaceDateTimeFormat.format(zonedDateTime)

    /**
      * Format in "yyyy-MM-dd'T'HH:mm:ssXXX'['VV']'".
      *
      * Example: 2026-03-17T13:45:55Z[Europe/London]
      */
    def formatIsoDateTime: String = isoDateTimeFormat.format(zonedDateTime)

    /**
      * Format ZonedDateTime in zone UTC in format "yyyy-MM-dd'T'HH:mm:ss'Z'".
      *
      * Example: 2026-12-31T13:45:55Z
      */
    def formatIsoDateTimeUTC: String = zonedDateTime.toInstant.formatIsoDateTimeUTC

    /**
      * Format in "HH:mm".
      *
      * Example: 13:45
      */
    def formatTime: String = timeFormat.format(zonedDateTime)

  extension (localDate: LocalDate)

    /**
      * Format in "d MMMM yyyy".
      *
      * Example: 2 January 2026
      */
    def formatLongDate: String = longDateFormat.format(localDate)

    /**
      * Format in "d MMMM yyyy".
      *
      * Example: 2 Ionawr 2026
      */
    def formatLongDateByLang(using lang: Lang): String =
      val enResult = formatLongDate
      lang.language match
        case "cy" => translateMonth(enResult, localDate.getMonth)
        case _    => enResult

    /**
      * Format in "d MMM yyyy".
      *
      * Example: 17 Mar 2026
      */
    def formatMediumDate: String = mediumDateFormat.format(localDate)

    /**
      * Format in "dd/MM/yyyy".
      *
      * Example: 17/03/2026
      */
    def formatSlashDate: String = slashDateFormat.format(localDate)

    /**
      * Format in "yyyy-MM-dd".
      *
      * Example: 2026-03-17
      */
    def formatIsoDate: String = isoDateFormat.format(localDate)

  extension (string: String)

    /**
      * Parse LocalDate from long date format "d MMMM yyyy".
      */
    def parseLongDate: Option[LocalDate] =
      Try(LocalDate.parse(string, longDateFormat))
        .recoverWith { ex =>
          logger.warn(s"Can't parse date: $string", ex)
          Failure(ex)
        }
        .toOption

    /**
      * Parse LocalDate from long date format "d MMMM yyyy" and reformat by lang.
      *
      * Example: 5 December 2026
      */
    def parseLongDateAndFormatByLang(using lang: Lang): Option[String] =
      parseLongDate.map(_.formatLongDateByLang)

  implicit val zonedDateTimeToLocalDate: Conversion[ZonedDateTime, LocalDate] = _.toLocalDate

  implicit val localDateToZonedDateTime: Conversion[LocalDate, ZonedDateTime] = _.atStartOfDay(ukTimezone)

  implicit def langFromMessages(using messages: Messages): Lang = messages.lang
