package com.example.clientcerviceplatphorm.model

data class Fournisseur(
    var id: String? = null,
    var name: String,
    var email: String,
    var phone: String,
    var services: List<Service>? = null,
    var password: String

)

