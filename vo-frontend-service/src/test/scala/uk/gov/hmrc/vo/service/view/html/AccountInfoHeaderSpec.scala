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

package uk.gov.hmrc.vo.service.view.html

import play.api.i18n.Messages
import play.twirl.api.Html
import uk.gov.hmrc.vo.service.model.AccountInfo
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class AccountInfoHeaderSpec extends BaseAppSpec:

  private val component = AccountInfoHeader

  private def expectedHtml(accountInfo: AccountInfo) =
    s"""<ul id="account-info-header" style="margin: 0; padding: 10px; border-bottom: 2px solid #1d70b8; background-color: #f4f4f4;">
       |    <li style="display: inline-block; margin-right: 30px; margin-left: 0">
       |        <span class="govuk-body-s govuk-!-font-weight-bold">service.accountInfo.key1:</span>
       |        <span class="govuk-body-s">${accountInfo.value1}</span>
       |    </li>
       |        <li style="display: inline-block">
       |            <span class="govuk-body-s govuk-!-font-weight-bold">service.accountInfo.key2:</span>
       |            <span class="govuk-body-s">${accountInfo.value2}</span>
       |        </li>
       |</ul>""".stripMargin

  given messages: Messages = messagesApi.preferred(Seq.empty)

  "FullWidthMainContent" should {
    "render as expected account info" in {
      val account = AccountInfo("Param 1", "Param 2")
      component(account).body.trimEmptyLines shouldBe Html(expectedHtml(account)).body
    }

    "render only value1 if value2 is empty" in {
      component(AccountInfo("Account param 1", "")).body should include("""<span class="govuk-body-s">Account param 1</span>""")
    }

    "have all template methods implemented" in
      forAll {
        (param1: String, param2: String) =>
          val account = AccountInfo(param1, param2)
          component.render(account, messages) shouldBe component.ref.f(account)(messages)
      }
  }
