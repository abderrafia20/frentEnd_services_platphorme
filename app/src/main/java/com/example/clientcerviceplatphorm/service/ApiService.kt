package com.example.clientcerviceplatphorm.service

import com.example.clientcerviceplatphorm.model.Admin
import com.example.clientcerviceplatphorm.model.Client
import com.example.clientcerviceplatphorm.model.Fournisseur
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {
    @GET("api/admins")
    suspend fun getAdmins (): List<Admin>


    @POST("api/admins")
    suspend fun createAdmin(@Body admin: Admin): Admin

    @GET("api/admins/{id}")
    suspend fun getAdminById(@Path("id") id: String): Admin

    @GET("api/clients")
    suspend fun getClient (): List<Client>


    @POST("api/clients")
    suspend fun createClient(@Body client: Client): Client

    @GET("api/clients/{id}")
    suspend fun getClientById(@Path("id") id: String): Client

    @GET("api/fournisseurs")
    suspend fun getFournisseurs (): List<Fournisseur>


    @POST("api/fournisseurs")
    suspend fun createFournisseurs(@Body admin: Fournisseur): Fournisseur

    @GET("api/fournisseurs/{id}")
    suspend fun getFournisseurById(@Path("id") id: String): Fournisseur


}