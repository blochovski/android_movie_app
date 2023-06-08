package com.example.domain.model.pagination

data class Pagination(
    val currentPage: Int,
    val totalPages: Int
) {

    companion object {
        const val UNKNOWN_TOTAL = -1
        const val PAGE_SIZE = 20
    }

    val canFetchNextPage: Boolean
        get() = totalPages == UNKNOWN_TOTAL || currentPage < totalPages
}