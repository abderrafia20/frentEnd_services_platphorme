package com.example.clientcerviceplatphorm.repository

import com.example.clientcerviceplatphorm.model.Fournisseur
import com.example.clientcerviceplatphorm.model.Service
import com.example.clientcerviceplatphorm.service.RetrofiteInstence

class RepositoryService {
    private val api = RetrofiteInstence.instance

    suspend fun getServices(): List<Service>?{
        return try {
            api.getServices()
        }catch (e: Exception){
            null
        }
    }

    suspend fun createService(service: Service): Service? {
        return try {
            api.createService(service)
        }catch (e: Exception){
            null
        }
    }

    suspend fun getServiceById(id : String): Service?{
        return try {
            api.getServiceById(id)
        }catch (e: Exception){
            null
        }
    }

}