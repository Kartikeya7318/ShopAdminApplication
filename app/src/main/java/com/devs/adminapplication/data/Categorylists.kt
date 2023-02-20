package com.devs.adminapplication.data

class Categorylists {
    companion object{
        fun getCategories(): List<String> {
            return listOf(
                "All Categories",
                "Men",
                "Women",
                "Kids",
            )

        }

        fun getSubCategories(): List<String> {
            return listOf(
                "All Categories",
                "Shirts",
                "Jeans",
                "Trousers",
                "T-Shirts"
            )
        }
    }

}