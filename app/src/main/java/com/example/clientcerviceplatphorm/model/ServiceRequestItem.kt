package com.example.clientcerviceplatphorm.model

data class ServiceRequestItem(
    val serviceRequest: ServiceRequest,
    val title: String,
    val clientName: String,
    val price: String
)