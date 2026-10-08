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

/**
  * Generate `Pagination` parameters to `GovukPagination` Twirl template.
  *
  * @author Yuriy Tumakha
  */
class PaginationGenerator(currentPage: Int, totalPages: Int, pageToUrl: Int => String, maxPage: Int = 100):

  private val pagesAroundCurrent = 3

  val page: Int  = normalizedPage(currentPage)
  val total: Int = normalizedTotal(totalPages)

  def generatePagination: Pagination =
    total match
      case 0           => Pagination()
      case t if t < 11 => generateWithoutEllipsis
      case _           => generateWithEllipsis

  private def normalizedPage(page: Int) = Math.min(Math.max(page, 1), maxPage)

  private def normalizedTotal(total: Int) = Math.min(Math.max(total, 0), maxPage)

  private def previousLink: Option[PaginationLink] = Option.when(page > 1)(PaginationLink(pageToUrl(page - 1)))

  private def nextLink: Option[PaginationLink] = Option.when(page < total)(PaginationLink(pageToUrl(page + 1)))

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
      items = Some((1 to total).map(pageItem))
    )

  private def generateWithEllipsis: Pagination =
    val pages = (Set(1, total) ++ (page - pagesAroundCurrent to page + pagesAroundCurrent).toSet)
      .filter(p => p >= 1 && p <= total)
      .toSeq
      .sorted

    val items =
      pages.foldRight((List.empty[PaginationItem], total)) {
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
