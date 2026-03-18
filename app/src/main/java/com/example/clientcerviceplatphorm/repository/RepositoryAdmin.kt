package com.example.clientcerviceplatphorm.repository

import com.example.clientcerviceplatphorm.model.Admin
import com.example.clientcerviceplatphorm.service.RetrofiteInstence

class RepositoryAdmin {
    private val apiSrvice = RetrofiteInstence.instance

    suspend fun getAdmins (): List<Admin>? {
        return try {
            apiSrvice.getAdmins()
        }catch (e: Exception){
            null
        }
    }

    suspend fun createAdmin(admin : Admin): Admin? {
        return try {
            apiSrvice.createAdmin(admin)
        }catch (e: Exception){
            null
        }
    }
    suspend fun getAdminById(id : String): Admin?{
        return try {
            apiSrvice.getAdminById(id)
        }catch (e: Exception){
            null
        }
    }
}