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

import play.api.Logging
import play.api.libs.json.{JsObject, Json, Writes}
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.connector.AuditConnector
import uk.gov.hmrc.vo.service.model.event.AuditEvent

import javax.inject.{Inject, Singleton}
import scala.concurrent.ExecutionContext

/**
  * @author Yuriy Tumakha
  */
@Singleton
class AuditService @Inject() (auditConnector: AuditConnector)(using ec: ExecutionContext) extends Logging:

  /**
    * Sends an [[uk.gov.hmrc.vo.service.model.event.AuditEvent]].
    *
    * @param auditEvent the audit event to send
    */
  final def send[T <: AuditEvent](auditEvent: T)(using hc: HeaderCarrier, writes: Writes[T]): Unit =
    val auditType       = auditEvent.auditType
    val eventDetailJson = Json.toJson(auditEvent).as[JsObject]
    logger.debug(s"\nSend event $auditType:\n${Json.prettyPrint(eventDetailJson)}\n")

    auditConnector.sendExplicitAudit(auditType, eventDetailJson)
