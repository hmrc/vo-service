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

package uk.gov.hmrc.vo.service.config

import play.api.Configuration
import play.api.mvc.Call

/**
  * @author Yuriy Tumakha
  */
trait VOServiceConfig extends LangCodes with StandardPageConfig with TimeoutDialogConfig:

  def configuration: Configuration
  def serviceMenuHome: Call
  def theFirstPage: Call
  def serviceMenuSignOut: Option[Call]              = None
  def feedbackPage: Call                            = feedbackFrontendForm
  def serviceLocalRoot: Call                        = serviceMenuHome
  def notificationBannerEnabledOn: Set[Call]        = Set(serviceMenuHome, theFirstPage)
  def timeoutDialogEnabledExcept: Set[Call]         = Set.empty
  override def isWelshTranslationAvailable: Boolean = false

  def getString(path: String, default: String => String = path => s"Config:$path"): String =
    configuration.getOptional[String](path).getOrElse(default(path))

  def getInt(path: String, default: Int = 1): Int =
    configuration.getOptional[Int](path).getOrElse(default)

  def getBoolean(path: String, default: Boolean = false): Boolean =
    configuration.getOptional[Boolean](path).getOrElse(default)

  val serviceID: String = getString("service.id")

  /**
    * "platform.frontend.host" is defined only in the cloud environment.
    */
  val platformFrontendHost: Option[String] = configuration.getOptional[String]("platform.frontend.host")

  // Feedback frontend - start
  private val localFeedbackBase          = "http://localhost:9514"
  private val feedbackBase: String       = platformFrontendHost.getOrElse(localFeedbackBase)
  private val feedbackFrontendForm: Call = Call("GET", s"$feedbackBase/feedback/$serviceID")
  // Feedback frontend - end
