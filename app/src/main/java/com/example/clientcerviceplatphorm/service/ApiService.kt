package com.example.clientcerviceplatphorm.service

import com.example.clientcerviceplatphorm.model.Admin
import com.example.clientcerviceplatphorm.model.Client
import com.example.clientcerviceplatphorm.model.Fournisseur
import com.example.clientcerviceplatphorm.model.Service
import com.example.clientcerviceplatphorm.model.ServiceRequest
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface ApiService {

    // ----- Admin -----
    @GET("api/admins")
    suspend fun getAdmins(): List<Admin>

    @POST("api/admins")
    suspend fun createAdmin(@Body admin: Admin): Admin

    @GET("api/admins/{id}")
    suspend fun getAdminById(@Path("id") id: String): Admin

    @DELETE("api/admins/{id}")
    suspend fun deleteAdmin(@Path("id") id: String): Response<Unit>

    // ----- Client -----
    @GET("api/clients")
    suspend fun getClients(): List<Client>

    @POST("api/clients")
    suspend fun createClient(@Body client: Client): Client

    @GET("api/clients/{id}")
    suspend fun getClientById(@Path("id") id: String): Client

    @DELETE("api/clients/{id}")
    suspend fun deleteClient(@Path("id") id: String): Response<Unit>

    // ----- Fournisseur -----
    @GET("api/fournisseurs")
    suspend fun getFournisseurs(): List<Fournisseur>

    @POST("api/fournisseurs")
    suspend fun createFournisseur(@Body fournisseur: Fournisseur): Fournisseur

    @GET("api/fournisseurs/{id}")
    suspend fun getFournisseurById(@Path("id") id: String): Fournisseur

    @DELETE("api/fournisseurs/{id}")
    suspend fun deleteFournisseur(@Path("id") id: String): Response<Unit>

    // ----- Services -----
    @GET("api/services")
    suspend fun getServices(): List<Service>

    @GET("api/services/{id}")
    suspend fun getServiceById(@Path("id") id: String): Service

    @POST("api/services")
    suspend fun createService(@Body service: Service): Service

    // ----- Services reqquest -----

    @GET("api/service-requests")
    suspend fun getServiceRequests(): List<ServiceRequest>

    @GET("api/service-requests/{id}")
    suspend fun getServiceRequestById(@Path("id") id: String): ServiceRequest

    @POST("api/service-requests")
    suspend fun createServiceRequest(@Body serviceRequest: ServiceRequest): ServiceRequest

    @DELETE("api/service-requests/{id}")
    suspend fun deleteServiceRequest(@Path("id") id: String): Response<Unit>
}