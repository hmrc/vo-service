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

import play.api.{Configuration, Logging}
import uk.gov.hmrc.play.http.ws.WSProxyConfiguration
import uk.gov.hmrc.vo.service.exception.FeatureNotEnabledException
import uk.gov.service.notify.{NotificationClient, SendEmailResponse}

import java.net.{Authenticator, InetSocketAddress, PasswordAuthentication, Proxy}
import scala.jdk.CollectionConverters.*
import scala.util.{Failure, Success, Try}

/**
  * GOV.UK Notify REST API client.
  *
  * @author Yuriy Tumakha
  */
abstract class GovNotifyConnector(config: Configuration) extends Logging:

  val featureNotifyEnabled: Boolean = config.getOptional[Boolean]("feature.notify.enabled").contains(true)

  private val apiKey  = config.getOptional[String]("notify.apiKey").getOrElse("Config:notify.apiKey")
  private val baseUrl = config.getOptional[String]("notify.baseUrl").getOrElse("https://api.notifications.service.gov.uk")

  protected val notificationClient: NotificationClient = NotificationClient(apiKey, baseUrl, getProxy)

  private val dummySuccessfulResponse =
    SendEmailResponse("""{
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
                        |}""".stripMargin)

  private def getProxy: Proxy =
    WSProxyConfiguration.buildWsProxyServer(config).fold(Proxy.NO_PROXY) { wsProxy =>
      System.setProperty("jdk.http.auth.tunneling.disabledSchemes", "") // removing Basic scheme

      Authenticator.setDefault(
        new Authenticator:
          override val getPasswordAuthentication: PasswordAuthentication =
            PasswordAuthentication(wsProxy.principal.getOrElse(""), wsProxy.password.getOrElse("").toCharArray)
      )
      new Proxy(Proxy.Type.HTTP, InetSocketAddress(wsProxy.host, wsProxy.port))
    }

  def getOrNotProvided(value: Option[String]): String = value.filter(_.trim != "").getOrElse("Not provided")

  def asYesNo(value: Boolean): String = if value then "Yes" else "No"

  def sendEmail(
    templateId: String,
    emailAddress: String,
    personalisation: Map[String, String],
    reference: String
  ): Try[SendEmailResponse] =
    logger.debug(s"sendEmail($templateId, $emailAddress, $personalisation, $reference)")

    val result = if featureNotifyEnabled then
      Try {
        notificationClient.sendEmail(templateId, emailAddress, personalisation.asJava, reference)
      }
    else
      Failure(FeatureNotEnabledException("Feature Notify is not enabled"))

    result match
      case Success(response: SendEmailResponse)     =>
        logger.info(s"Email successfully sent to Notify. Template: ${response.getTemplateId}, Ref: ${response.getReference}")
        result
      case Failure(FeatureNotEnabledException(msg)) =>
        logger.warn(s"Sending email was skipped. $msg")
        Success(dummySuccessfulResponse)
      case Failure(exception)                       =>
        logger.error(s"Error when sending email to Notify: ${exception.getMessage}", exception)
        result
