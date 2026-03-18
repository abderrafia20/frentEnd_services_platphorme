package com.example.clientcerviceplatphorm.model

sealed class User {

    data class AdminUser(val admin: Admin) : User()
    data class ClientUser(val client: Client) : User()
    data class FournisseurUser(val fournisseur: Fournisseur) : User()

    fun getName(): String = when(this) {
        is AdminUser -> admin.name ?: ""
        is ClientUser -> client.name ?: ""
        is FournisseurUser -> fournisseur.name ?: ""
    }

    fun getEmail(): String = when(this) {
        is AdminUser -> admin.email ?: ""
        is ClientUser -> client.email ?: ""
        is FournisseurUser -> fournisseur.email ?: ""
    }

    fun getId(): String = when(this) {
        is AdminUser -> admin.id ?: ""
        is ClientUser -> client.id ?: ""
        is FournisseurUser -> fournisseur.id ?: ""
    }

    fun getPhone(): String = when(this) {
        is AdminUser -> admin.phone ?: ""
        is ClientUser -> client.phone ?: ""
        is FournisseurUser -> fournisseur.phone ?: ""
    }
}