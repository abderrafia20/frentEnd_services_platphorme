package com.example.clientcerviceplatphorm.repository

import com.example.clientcerviceplatphorm.model.ServiceRequest
import com.example.clientcerviceplatphorm.service.RetrofiteInstence

class RepositoryServiceRequest {

    private val api = RetrofiteInstence.instance

    suspend fun getServiceRequests(): List<ServiceRequest>? {
        return try {
            api.getServiceRequests()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createServiceRequest(serviceRequest: ServiceRequest): ServiceRequest? {
        return try {
            api.createServiceRequest(serviceRequest)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getServiceRequestById(id: String): ServiceRequest? {
        return try {
            api.getServiceRequestById(id)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun deleteServiceRequest(id: String): Boolean {
        return try {
            val response = api.deleteServiceRequest(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }


}