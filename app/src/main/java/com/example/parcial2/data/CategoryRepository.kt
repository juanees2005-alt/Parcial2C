package com.example.parcial2.data

import com.example.parcial2.model.Category

interface CategoryRepository {
    fun getCategories(): List<Category>
}

class InMemoryCategoryRepository : CategoryRepository {
    override fun getCategories(): List<Category> {
        return listOf(
            Category("1", "Alimentación"),
            Category("2", "Alojamiento"),
            Category("3", "Transporte"),
            Category("4", "Entretenimiento"),
            Category("5", "Otros")
        )
    }
}