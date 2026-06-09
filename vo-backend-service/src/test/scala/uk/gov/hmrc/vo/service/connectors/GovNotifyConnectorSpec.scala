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

/**
  * @author Yuriy Tumakha
  */
import org.scalatestplus.mockito.MockitoSugar
import play.api.Configuration
import uk.gov.hmrc.vo.unit.test.BaseAppSpec
import uk.gov.service.notify.{NotificationClient, SendEmailResponse}

import java.util.Collections
import scala.util.Success

/**
  * @author Yuriy Tumakha
  */
class GovNotifyConnectorSpec extends BaseAppSpec with MockitoSugar:

  private val configuration = inject[Configuration]

  private val templateId: String                   = "Template-ID"
  private val emailAddress: String                 = "ndrinboxvo@test.email.com"
  private val personalisation: Map[String, String] = Map("firstName" -> "First", "lastName" -> "Last")
  private val reference: String                    = "Reference-TrackID-123"

  private val sendEmailResponse: SendEmailResponse =
    SendEmailResponse(
      """{
        |  "id": "740e5834-3a29-46b4-9a6f-16142fde533a",
        |  "content": {
        |    "subject": "SUBJECT TEXT",
        |    "body": "MESSAGE TEXT",
        |  },
        |  "template": {
        |    "id": "f33517ff-2a88-4f6e-b855-c550268ce08a",
        |    "version": 1,
        |    "uri": "https://api.notifications.service.gov.uk/v2/template/f33517ff-2a88-4f6e-b855-c550268ce08a"
        |  }
        |}""".stripMargin
    )

  private val dummyResponse = Success(sendEmailResponse).toString

  private def govNotifyConnector(notifyEnabled: Boolean): GovNotifyConnector =
    new GovNotifyConnector(configuration):
      override val featureNotifyEnabled: Boolean = notifyEnabled

  "GovNotifyConnector" should {
    "send email when feature.notify.enabled = true" in {
      val result = govNotifyConnector(true).sendEmail(templateId, emailAddress, personalisation, reference)
      result.isFailure shouldBe true
    }

    "send email when feature.notify.enabled = false" in {
      govNotifyConnector(false).sendEmail(templateId, emailAddress, personalisation, reference).toString shouldBe dummyResponse
    }

    "send email when proxy enabled" in {
      val proxyConfig = Configuration(
        "http-verbs.proxy.enabled" -> "true",
        "proxy.username"           -> "service-name",
        "proxy.password"           -> "pass",
        "proxy.host"               -> "outbound-proxy-vip",
        "proxy.port"               -> "3128",
        "proxy.protocol"           -> "https"
      )

      val notifyConnector = new GovNotifyConnector(proxyConfig):
        override val featureNotifyEnabled: Boolean = false

      notifyConnector.sendEmail(templateId, emailAddress, personalisation, reference).toString shouldBe dummyResponse
    }

    "send email successfully" in {
      val notificationClientMock = mock[NotificationClient]
      when(notificationClientMock.sendEmail(eqTo(templateId), eqTo(emailAddress), eqTo(Collections.emptyMap()), eqTo(reference)))
        .thenReturn(sendEmailResponse)

      val notifyConnector = new GovNotifyConnector(configuration):
        override val featureNotifyEnabled: Boolean                    = true
        override protected val notificationClient: NotificationClient = notificationClientMock

      val result = notifyConnector.sendEmail(templateId, emailAddress, Map.empty, reference)
      result.isSuccess shouldBe true

      verify(notificationClientMock, times(1)).sendEmail(eqTo(templateId), eqTo(emailAddress), eqTo(Collections.emptyMap()), eqTo(reference))
    }
  }

  "GovNotifyConnector.getOrNotProvided()" should {
    "return Option value" in {
      govNotifyConnector(false).getOrNotProvided(Some("value")) shouldBe "value"
    }

    "return Not provided" in {
      govNotifyConnector(false).getOrNotProvided(None) shouldBe "Not provided"
    }
  }

  "GovNotifyConnector.asYesNo()" should {
    "return Yes" in {
      govNotifyConnector(false).asYesNo(true) shouldBe "Yes"
    }

    "return No" in {
      govNotifyConnector(false).asYesNo(false) shouldBe "No"
    }
  }
