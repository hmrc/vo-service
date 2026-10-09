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

import play.api.i18n.Messages
import uk.gov.hmrc.govukfrontend.views.Aliases.{Pagination, PaginationItem, PaginationLink}
import uk.gov.hmrc.vo.unit.test.BaseAppSpec

/**
  * @author Yuriy Tumakha
  */
class PaginationGeneratorSpec extends BaseAppSpec:

  "PaginationGenerator" should {
    "return empty Pagination for totalResults = 0" in {
      val paginationGenerator = PaginationGenerator(1, 0, p => s"search/page/$p")
      paginationGenerator.totalPages shouldBe 0

      val pagination: Pagination = paginationGenerator.generatePagination
      pagination.previous shouldBe None
      pagination.items    shouldBe None
      pagination.next     shouldBe None
    }

    "generate Pagination for page 1 of 5" in {
      val paginationGenerator = PaginationGenerator(1, 44, p => s"search/page/$p")
      paginationGenerator.totalPages shouldBe 5

      val pagination: Pagination = paginationGenerator.generatePagination
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
      val paginationGenerator = PaginationGenerator(3, 46, p => s"search/page/$p")
      paginationGenerator.totalPages shouldBe 5

      val pagination: Pagination = paginationGenerator.generatePagination
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
      val paginationGenerator = PaginationGenerator(5, 50, p => s"search/page/$p")
      paginationGenerator.totalPages shouldBe 5

      val pagination: Pagination = paginationGenerator.generatePagination
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
      val paginationGenerator = PaginationGenerator(15, 499, p => s"search/page/$p")
      paginationGenerator.totalPages shouldBe 50

      val pagination: Pagination = paginationGenerator.generatePagination
      pagination.previous shouldBe Some(PaginationLink("search/page/14"))
      pagination.items    shouldBe Some(
        Seq(1, 0, 12, 13, 14, 15, 16, 17, 18, 0, paginationGenerator.totalPages).map { p =>
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
      val paginationGenerator = PaginationGenerator(46, 500, p => s"search/page/$p", resultsPerPage = 10)
      paginationGenerator.totalPages shouldBe 50

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

    "provide results range" in {
      val paginationGenerator = PaginationGenerator(46, 500, p => s"search/page/$p", resultsPerPage = 10)
      paginationGenerator.totalPages   shouldBe 50
      paginationGenerator.resultsRange shouldBe (451, 460)
    }

    "provide results legend" in {
      given Messages = stubMessages(
        "results.pagination.range.legend" -> "Showing {0} to {1} of {2} results"
      )

      val paginationGenerator = PaginationGenerator(46, 500, p => s"search/page/$p", resultsPerPage = 10)
      paginationGenerator.totalPages                                       shouldBe 50
      paginationGenerator.resultsLegend("results.pagination.range.legend") shouldBe "Showing 451 to 460 of 500 results"
    }
  }
