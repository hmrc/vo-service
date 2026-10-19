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

package uk.gov.hmrc.vo.service.model.event

/**
  * Base trait for audit events.
  *
  * The subclass name is used as the audit event type.
  * The `appName` configuration value is used as the audit source.
  *
  * Audit events are serialized to JSON using [[play.api.libs.json.Json.toJson]].
  *
  * Guidelines for audit event attributes:
  * - Use only the following property types where possible: `String`, `Boolean`,
  *   `Int`, `Long`, `LocalDate`, `Instant`, enums, and `Option` values wrapping
  *   these types.
  * - Do not duplicate attributes provided by the audit tags, such as device ID,
  *   request ID, request path, session ID, or the user's public IP address.
  * - Use `camelCase` for attribute names, for example, `customerReference`.
  * - Use full, descriptive names that are meaningful and require minimal
  *   background knowledge, except for standard attribute names and abbreviations
  *   specified in the next guideline.
  * - Use [standard attribute names](https://cip-qa.tax.service.gov.uk/customer-insight-platform/services-and-support/explicit-auditing-guidance/standard-attributes)
  *   and their accepted abbreviations, such as `nino`, `vrn`, and `payeRef`.
  * - Avoid other abbreviations and acronyms unless they are on the accepted
  *   abbreviations list or are widely understood across HMRC.
  * - Avoid generic or non-descriptive names such as `key`, `value`, `question`,
  *   `answer`, `map`, `attribute`, `field`, `data`, `xml`, `json`, and `formData`.
  * - Use consistent attribute names across audit event types unless existing
  *   names do not follow these guidelines.
  * - Include units where they are not self-evident, particularly for currencies
  *   other than GBP and for smaller denominations such as pence.
  * - Use British English.
  * - For identifiers, use the suffix `Id`, for example, `clientId`, not `clientID`.
  *
  * Choose the most appropriate data type:
  * - Use `Boolean` rather than special strings or numbers to represent
  *   boolean values.
  * - Use `Int` for whole-number monetary amounts or quantities rather than
  *   `String`.
  * - Use `String` for numeric identifiers, such as VAT numbers, company
  *   registration numbers, Unique Taxpayer References, and phone numbers,
  *   to preserve leading zeros.
  * - Do not use special or placeholder values.
  * - Omit missing or non-applicable values rather than using `""` (empty string),
  *   `"-"` (dash), or `"N/A"`.
  *
  * @author Yuriy Tumakha
  */
trait AuditEvent:

  final def auditType: String = getClass.getSimpleName
