package com.example.clientcerviceplatphorm.model

import java.time.LocalDateTime
import java.util.Date

data class ServiceRequest(
    var id: String? = null,
    var clientId: String,
    var fournisseurId: String ,
    var serviceId: String,
    var decreption: String,
    var status: StatusSR,
    var date: Date = Date() //LocalDateTime = LocalDateTime.now()
)
