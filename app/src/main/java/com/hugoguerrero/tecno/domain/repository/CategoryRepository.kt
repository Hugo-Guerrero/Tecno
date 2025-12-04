package com.hugoguerrero.tecno.domain.repository

import com.hugoguerrero.tecno.domain.model.Category

interface CategoryRepository {
    suspend fun getCategories(): List<Category>
}
