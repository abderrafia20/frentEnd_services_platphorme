package com.example.clientcerviceplatphorm.repository

import com.example.clientcerviceplatphorm.model.Admin
import com.example.clientcerviceplatphorm.service.RetrofiteInstence

class RepositoryAdmin {
    private val apiService = RetrofiteInstence.instance

    suspend fun getAdmins(): List<Admin>? {
        return try {
            apiService.getAdmins()
        } catch (e: Exception) {
            null
        }
    }

    suspend fun createAdmin(admin: Admin): Admin? {
        return try {
            apiService.createAdmin(admin)
        } catch (e: Exception) {
            null
        }
    }

    suspend fun getAdminById(id: String): Admin? {
        return try {
            apiService.getAdminById(id)
        } catch (e: Exception) {
            null
        }
    }


    suspend fun deleteAdmin(id: String): Boolean {
        return try {
            val response = apiService.deleteAdmin(id)
            response.isSuccessful
        } catch (e: Exception) {
            false
        }
    }
}