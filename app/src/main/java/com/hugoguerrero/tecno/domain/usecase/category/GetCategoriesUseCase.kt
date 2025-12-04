package com.hugoguerrero.tecno.domain.usecase.category

import com.hugoguerrero.tecno.domain.model.Category
import com.hugoguerrero.tecno.domain.repository.CategoryRepository

class GetCategoriesUseCase(
    private val repository: CategoryRepository
) {
    suspend operator fun invoke(): List<Category> {
        return repository.getCategories()
    }
}
