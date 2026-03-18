package com.example.clientcerviceplatphorm.model

data class Service(
    var id: String? = null,
    var title: String,
    val fournisseurId: String,
    var description: String,
    var price: Double
)
