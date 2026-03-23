package com.example.clientcerviceplatphorm.model

data class Service(
    var id: String? = null,
    var nameFournisseur: String,
    val fournisseurId: String,
    var title: String,
    var description: String,
    var price: Double
)
