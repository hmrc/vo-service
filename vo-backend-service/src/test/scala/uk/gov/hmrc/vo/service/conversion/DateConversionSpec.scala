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

import play.api.i18n.*
import uk.gov.hmrc.vo.service.conversion.DateConversion.*
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

import java.time.*
import java.time.Month.*
import java.time.format.DateTimeFormatter.ISO_DATE_TIME
import java.time.temporal.ChronoUnit.SECONDS
import java.util.Locale
import scala.language.implicitConversions

/**
  * @author Yuriy Tumakha
  */
class DateConversionSpec extends BaseAppSpec:

  private val testLocalDate = LocalDate.of(2026, FEBRUARY, 2)
  private val testZonedDate = ZonedDateTime.of(testLocalDate.plusMonths(1).plusDays(15), LocalTime.of(13, 45, 55), ukTimezone)

  "nowInUK" should {
    "return current time in UK time zone" in {
      logger.info("UK Time: " + nowInUK)
      nowInUK.formatMediumDateTime shouldBe ZonedDateTime.now(ukTimezone).formatMediumDateTime
    }
  }

  "nowIsoDateTimeUTC" should {
    "use format \"yyyy-MM-dd'T'HH:mm:ss'Z'\" in zone UTC" in {
      nowIsoDateTimeUTC shouldBe Instant.now.truncatedTo(SECONDS).atOffset(ZoneOffset.UTC).format(ISO_DATE_TIME)
    }
  }

  "formatIsoDateTimeUTC" should {
    "use format \"yyyy-MM-dd'T'HH:mm:ss'Z'\" in zone UTC" in {
      testZonedDate.formatIsoDateTimeUTC           shouldBe "2026-03-17T13:45:55Z"
      testZonedDate.toInstant.formatIsoDateTimeUTC shouldBe "2026-03-17T13:45:55Z"

      testLocalDate.formatIsoDateTimeUTC shouldBe "2026-02-02T00:00:00Z"
    }
  }

  "formatLongDate" should {
    "return date in format 'd MMMM yyyy' as per GDS" in {
      LocalDate.of(2026, JANUARY, 2).formatLongDate       shouldBe "2 January 2026"
      LocalDate.of(2026, FEBRUARY, 22).formatLongDate     shouldBe "22 February 2026"
      Instant.ofEpochMilli(1798202700000L).formatLongDate shouldBe "25 December 2026"
    }
  }

  "formatLongDateByLang" should {
    "return date in format 'd MMMM yyyy' using lang = 'en'" in {
      LocalDate.of(2026, JANUARY, 2).formatLongDateByLang(using en)   shouldBe "2 January 2026"
      LocalDate.of(2026, FEBRUARY, 22).formatLongDateByLang(using en) shouldBe "22 February 2026"
      LocalDate.of(2026, AUGUST, 31).formatLongDateByLang(using en)   shouldBe "31 August 2026"
    }

    "return date in format 'd MMMM yyyy' using lang = 'cy'" in {
      given Lang = cy
      LocalDate.of(2026, MARCH, 5).formatLongDateByLang  shouldBe "5 Mawrth 2026"
      LocalDate.of(2026, APRIL, 25).formatLongDateByLang shouldBe "25 Ebrill 2026"
      LocalDate.of(2026, MAY, 30).formatLongDateByLang   shouldBe "30 Mai 2026"
    }

    "return date in format 'd MMMM yyyy' in English for unsupported language" in {
      given Lang = Lang(Locale.FRANCE)
      LocalDate.of(2026, SEPTEMBER, 30).formatLongDateByLang shouldBe "30 September 2026"
    }
  }

  "formatMediumDate" should {
    "format date in format 'd MMM yyyy'" in {
      testLocalDate.formatMediumDate shouldBe "2 Feb 2026"
      testZonedDate.formatMediumDate shouldBe "17 Mar 2026"
    }
  }

  "formatIsoDate" should {
    "format date in format 'yyyy-MM-dd'" in {
      testLocalDate.formatIsoDate shouldBe "2026-02-02"
      testZonedDate.formatIsoDate shouldBe "2026-03-17"
    }
  }

  "formatLongDateTime" should {
    "format date in format \"d MMMM yyyy 'at' HH:mm\"" in {
      testLocalDate.formatLongDateTime shouldBe "2 February 2026 at 00:00"
      testZonedDate.formatLongDateTime shouldBe "17 March 2026 at 13:45"
    }
  }

  "formatMediumDateTime" should {
    "format date in format \"d MMM yyyy HH:mm\"" in {
      testLocalDate.formatMediumDateTime                        shouldBe "2 Feb 2026 00:00"
      testZonedDate.formatMediumDateTime                        shouldBe "17 Mar 2026 13:45"
      Instant.ofEpochMilli(1767359325000L).formatMediumDateTime shouldBe "2 Jan 2026 13:08"
    }
  }

  "formatTime" should {
    "use time format 'HH:mm'" in {
      testZonedDate.formatTime shouldBe "13:45"
      testLocalDate.formatTime shouldBe "00:00"
    }
  }

  "parseIsoInstantUTC" should {
    "parse Instant in zone UTC from \"yyyy-MM-dd'T'HH:mm:ss'Z'\"" in {
      "2026-01-01T08:13:15Z".parseIsoInstantUTC.get.toEpochMilli shouldBe 1767255195000L
      "2026-01-02T13:08:45Z".parseIsoInstantUTC.get.toEpochMilli shouldBe 1767359325000L
      "2026-12-25T12:45:00Z".parseIsoInstantUTC.get.toEpochMilli shouldBe 1798202700000L
    }

    "return None when Instant parsing fails" in {
      "2026-12-22".parseIsoInstantUTC shouldBe None
    }
  }

  "parseLongDate" should {
    "parse LocalDate from long date format 'd MMMM yyyy'" in {
      "1 January 2026".parseLongDate   shouldBe Some(LocalDate.of(2026, JANUARY, 1))
      "02 January 2026".parseLongDate  shouldBe Some(LocalDate.of(2026, JANUARY, 2))
      "25 December 2026".parseLongDate shouldBe Some(LocalDate.of(2026, DECEMBER, 25))
    }

    "return None when date parsing fails" in {
      "123 Jan 2026".parseLongDate shouldBe None
    }
  }

  "parseLongDateAndFormatByLang" should {
    "parse date in long format 'd MMMM yyyy' and reformat by lang 'en'" in {
      given Lang = en
      "1 January 2026".parseLongDateAndFormatByLang   shouldBe Some("1 January 2026")
      "02 January 2026".parseLongDateAndFormatByLang  shouldBe Some("2 January 2026")
      "25 December 2026".parseLongDateAndFormatByLang shouldBe Some("25 December 2026")
    }

    "parse date in long format 'd MMMM yyyy' and reformat by lang 'cy'" in {
      given Lang = cy
      "1 January 2026".parseLongDateAndFormatByLang   shouldBe Some("1 Ionawr 2026")
      "02 January 2026".parseLongDateAndFormatByLang  shouldBe Some("2 Ionawr 2026")
      "25 December 2026".parseLongDateAndFormatByLang shouldBe Some("25 Rhagfyr 2026")
    }
  }

  "Conversion[ZonedDateTime, LocalDate]" should {
    "auto convert ZonedDateTime to LocalDate" in {
      val localDate: LocalDate = testZonedDate

      testZonedDate.getClass.getSimpleName shouldBe "ZonedDateTime"
      testZonedDate.getZone.getId          shouldBe "Europe/London"
      localDate.getClass.getSimpleName     shouldBe "LocalDate"
      localDate.toString                   shouldBe "2026-03-17"
    }
  }

  "Conversion[LocalDate, ZonedDateTime]" should {
    "auto convert LocalDate to ZonedDateTime" in {
      val zonedDateTime: ZonedDateTime = LocalDate.of(2026, JANUARY, 1)

      zonedDateTime.getClass.getSimpleName shouldBe "ZonedDateTime"
      zonedDateTime.getZone.getId          shouldBe "Europe/London"
      zonedDateTime.toString               shouldBe "2026-01-01T00:00Z[Europe/London]"
    }
  }

  "implicit def langFromMessages(using messages: Messages)" should {
    "implicitly extract Lang from implicit Messages" in {
      given Messages = stubMessages(
        "some.key" -> "Text in English"
      )

      LocalDate.of(2026, JANUARY, 14).formatLongDateByLang shouldBe "14 January 2026"

      summon[Lang].language shouldBe "en"
    }
  }
