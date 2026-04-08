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

package uk.gov.hmrc.vo.service.model.pagination

import uk.gov.hmrc.govukfrontend.views.Aliases.Pagination
import uk.gov.hmrc.govukfrontend.views.viewmodels.pagination.{PaginationItem, PaginationLink}
import uk.gov.hmrc.vo.unit.test.BaseSpec

/**
  * @author Yuriy Tumakha
  */
class PaginationItemsSpec extends BaseSpec:

  "PaginationItems" should {
    "implement the required properties of the parent Pagination class" in {
      val pagination: Pagination = PaginationItems(1, 5, p => s"search/page/$p")
      pagination.previous shouldBe None
      pagination.items    shouldBe Some(
        (1 to 5).map { p =>
          PaginationItem(
            href = s"search/page/$p",
            number = Some(p.toString),
            current = Some(p == 1)
          )
        }
      )
      pagination.next     shouldBe Some(PaginationLink("search/page/2"))
    }

    "show prev and next for page 3 of 5" in {
      val pagination: Pagination = PaginationItems(3, 5, p => s"search/page/$p")
      pagination.previous shouldBe Some(PaginationLink("search/page/2"))
      pagination.items    shouldBe Some(
        (1 to 5).map { p =>
          PaginationItem(
            href = s"search/page/$p",
            number = Some(p.toString),
            current = Some(p == 3)
          )
        }
      )
      pagination.next     shouldBe Some(PaginationLink("search/page/4"))
    }

    "show only prev for page 5 of 5" in {
      val pagination: Pagination = PaginationItems(5, 5, p => s"search/page/$p")
      pagination.previous shouldBe Some(PaginationLink("search/page/4"))
      pagination.items    shouldBe Some(
        (1 to 5).map { p =>
          PaginationItem(
            href = s"search/page/$p",
            number = Some(p.toString),
            current = Some(p == 5)
          )
        }
      )
      pagination.next     shouldBe None
    }
  }
