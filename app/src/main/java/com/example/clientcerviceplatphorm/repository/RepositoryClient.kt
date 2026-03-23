package com.example.clientcerviceplatphorm.repository

import com.example.clientcerviceplatphorm.model.Client
import com.example.clientcerviceplatphorm.service.RetrofiteInstence

class RepositoryClient {

    private val api = RetrofiteInstence.instance

    suspend fun getClients(): List<Client>? {
        return try {
            api.getClients()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createClient(client: Client): Client? {
        return try {
            api.createClient(client)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getClientById(id: String): Client? {
        return try {
            api.getClientById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    suspend fun deleteClient(id: String): Boolean {
        return try {
            val response = api.deleteClient(id)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}