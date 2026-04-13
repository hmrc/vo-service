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

/**
  * Parameters to `GovukPagination` Twirl template.
  *
  * @author Yuriy Tumakha
  */
class PaginationItems(
  current: Int,
  total: Int,
  pageToUrl: Int => String
) extends Pagination(
    previous = Option.when(current > 1)(PaginationLink(pageToUrl(current - 1))),
    next = Option.when(current < total)(PaginationLink(pageToUrl(current + 1))),
    items = Some(
      (1 to total).map { p =>
        PaginationItem(
          href = pageToUrl(p),
          number = Some(p.toString),
          current = Some(p == current)
        )
      }
    )
  )
