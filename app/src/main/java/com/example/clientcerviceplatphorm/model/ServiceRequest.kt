package com.example.clientcerviceplatphorm.model

data class ServiceRequest(
    var id: String? = null,
    var clientId: String,
    var fournisseurId: String ,
    var serviceId: String,
    var status: StatusSR,
    var date: String
)
