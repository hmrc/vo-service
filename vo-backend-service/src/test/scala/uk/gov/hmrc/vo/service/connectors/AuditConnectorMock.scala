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
import play.api.libs.json.Json
import uk.gov.hmrc.http.HeaderCarrier
import uk.gov.hmrc.play.audit.http.config.AuditingConfig
import uk.gov.hmrc.play.audit.http.connector.{AuditChannel, AuditConnector, AuditResult, DatastreamMetrics}
import uk.gov.hmrc.play.audit.model.ExtendedDataEvent

import scala.concurrent.{ExecutionContext, Future}

/**
  * @author Yuriy Tumakha
  */
class AuditConnectorMock extends AuditConnector with Logging:

  var _event: ExtendedDataEvent = ExtendedDataEvent("source", "type")

  val auditingConfig: AuditingConfig = AuditingConfig(
    enabled = false,
    consumer = None,
    auditSource = "AuditConnectorMock",
    auditSentHeaders = false
  )

  def auditChannel: AuditChannel = ???

  def datastreamMetrics: DatastreamMetrics = ???

  override def sendExtendedEvent(event: ExtendedDataEvent)(implicit hc: HeaderCarrier, ec: ExecutionContext): Future[AuditResult] =
    _event = event

    val extendedDataEventJson = auditSerialiser.serialise(event)

    logger.info(s"\nSend ExtendedDataEvent:\n${Json.prettyPrint(extendedDataEventJson)}\n")
    Future.successful(AuditResult.Success)
