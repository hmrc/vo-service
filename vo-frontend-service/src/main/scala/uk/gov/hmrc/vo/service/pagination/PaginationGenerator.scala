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

/**
  * Generate `Pagination` parameters to `GovukPagination` Twirl template.
  *
  * @author Yuriy Tumakha
  */
class PaginationGenerator(currentPage: Int, totalResults: Int, pageToUrl: Int => String, maxPage: Int = 100, resultsPerPage: Int = 10):

  private val pagesAroundCurrent = 3

  val page: Int       = normalizedPage(currentPage)
  val totalPages: Int = normalizedTotalPages(Math.ceil(totalResults.toDouble / resultsPerPage).toInt)

  def generatePagination: Pagination =
    totalPages match
      case 0           => Pagination()
      case t if t < 11 => generateWithoutEllipsis
      case _           => generateWithEllipsis

  def resultsRange: (Int, Int) =
    ((page - 1) * resultsPerPage + 1, (page * resultsPerPage) min totalResults)

  def resultsLegend(legendKey: String)(using messages: Messages): String =
    val (from, to) = resultsRange
    messages(legendKey, from, to, totalResults)

  private def normalizedPage(page: Int) = Math.min(Math.max(page, 1), maxPage)

  private def normalizedTotalPages(totalPages: Int) = Math.min(Math.max(totalPages, 0), maxPage)

  private def previousLink: Option[PaginationLink] = Option.when(page > 1)(PaginationLink(pageToUrl(page - 1)))

  private def nextLink: Option[PaginationLink] = Option.when(page < totalPages)(PaginationLink(pageToUrl(page + 1)))

  private def pageItem(p: Int): PaginationItem =
    PaginationItem(
      href = pageToUrl(p),
      number = Some(p.toString),
      current = Some(p == page)
    )

  private def generateWithoutEllipsis: Pagination =
    Pagination(
      previous = previousLink,
      next = nextLink,
      items = Some((1 to totalPages).map(pageItem))
    )

  private def generateWithEllipsis: Pagination =
    val pages = (Set(1, totalPages) ++ (page - pagesAroundCurrent to page + pagesAroundCurrent).toSet)
      .filter(p => p >= 1 && p <= totalPages)
      .toSeq
      .sorted

    val items =
      pages.foldRight((List.empty[PaginationItem], totalPages)) {
        case (currentPage, (items, nextPage)) =>
          val updatedItems =
            if nextPage - currentPage > 1 then
              pageItem(currentPage) :: PaginationItem(ellipsis = Some(true)) :: items
            else
              pageItem(currentPage) :: items

          (updatedItems, currentPage)
      }._1

    Pagination(
      previous = previousLink,
      next = nextLink,
      items = Some(items)
    )
