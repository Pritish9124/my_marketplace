package com.androstark.marketplace.domain.model

data class Category(
    val id: Int,
    val name: String,
    val icon: String,
    val color: String,
    val adCount: Int
)
