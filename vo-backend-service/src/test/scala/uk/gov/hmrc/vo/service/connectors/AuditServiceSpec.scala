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

package uk.gov.hmrc.vo.service.connectors

import play.api.libs.json.{JsObject, Json, Writes}
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class AuditServiceSpec extends BaseAppSpec:

  private val auditConnector = AuditConnectorMock()
  private val audit          = AuditService(auditConnector)

  "AuditService.send" should {
    "derive the audit type from the event class name and serialize all event properties to JSON" in {
      val event           = LoginFailure("user123", 3)
      val eventDetailJson = Json.toJson(event)

      event.auditType shouldBe "LoginFailure"
      eventDetailJson shouldBe Json.obj("username" -> "user123", "attempts" -> 3)

      audit.send(event)

      val extendedDataEvent = auditConnector._event
      extendedDataEvent.auditSource shouldBe "AuditConnectorMock"
      extendedDataEvent.auditType   shouldBe "LoginFailure"
      extendedDataEvent.detail      shouldBe eventDetailJson
    }
  }
