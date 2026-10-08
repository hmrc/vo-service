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

package uk.gov.hmrc.vo.service.pagination

import uk.gov.hmrc.govukfrontend.views.Aliases.{Pagination, PaginationItem, PaginationLink}
import uk.gov.hmrc.vo.unit.test.BaseSpec

/**
  * @author Yuriy Tumakha
  */
class PaginationGeneratorSpec extends BaseSpec:

  "PaginationGenerator" should {
    "return empty Pagination for total = 0" in {
      val pagination: Pagination = PaginationGenerator(1, 0, p => s"search/page/$p").generatePagination
      pagination.previous shouldBe None
      pagination.items    shouldBe None
      pagination.next     shouldBe None
    }

    "generate Pagination for page 1 of 5" in {
      val pagination: Pagination = PaginationGenerator(1, 5, p => s"search/page/$p").generatePagination
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

    "generate prev and next for page 3 of 5" in {
      val pagination: Pagination = PaginationGenerator(3, 5, p => s"search/page/$p").generatePagination
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

    "generate only prev for page 5 of 5" in {
      val pagination: Pagination = PaginationGenerator(5, 5, p => s"search/page/$p").generatePagination
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

    "generate Pagination with ellipsis for page 15 of 50" in {
      val paginationGenerator    = PaginationGenerator(15, 50, p => s"search/page/$p")
      val pagination: Pagination = paginationGenerator.generatePagination
      pagination.previous shouldBe Some(PaginationLink("search/page/14"))
      pagination.items    shouldBe Some(
        Seq(1, 0, 12, 13, 14, 15, 16, 17, 18, 0, paginationGenerator.total).map { p =>
          if p == 0 then
            PaginationItem(ellipsis = Some(true))
          else
            PaginationItem(
              href = s"search/page/$p",
              number = Some(p.toString),
              current = Some(p == paginationGenerator.page)
            )
        }
      )
      pagination.next     shouldBe Some(PaginationLink("search/page/16"))
    }

    "generate Pagination with ellipsis for page 46 of 50" in {
      val paginationGenerator    = PaginationGenerator(46, 50, p => s"search/page/$p")
      val pagination: Pagination = paginationGenerator.generatePagination
      pagination.previous shouldBe Some(PaginationLink("search/page/45"))
      pagination.items    shouldBe Some(
        Seq(1, 0, 43, 44, 45, 46, 47, 48, 49, 50).map { p =>
          if p == 0 then
            PaginationItem(ellipsis = Some(true))
          else
            PaginationItem(
              href = s"search/page/$p",
              number = Some(p.toString),
              current = Some(p == paginationGenerator.page)
            )
        }
      )
      pagination.next     shouldBe Some(PaginationLink("search/page/47"))
    }

  }
