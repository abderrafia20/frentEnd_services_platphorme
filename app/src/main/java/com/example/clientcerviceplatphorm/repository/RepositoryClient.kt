package com.example.clientcerviceplatphorm.repository

import com.example.clientcerviceplatphorm.model.Admin
import com.example.clientcerviceplatphorm.model.Client
import com.example.clientcerviceplatphorm.service.RetrofiteInstence

class RepositoryClient {

    private val api = RetrofiteInstence.instance

    suspend fun getClients(): List<Client>? {
        return try {
            api.getClient()
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

    suspend fun getClientById(id : String): Client?{
        return try {
            api.getClientById(id)
        }catch (e: Exception){
            null
        }
    }
}