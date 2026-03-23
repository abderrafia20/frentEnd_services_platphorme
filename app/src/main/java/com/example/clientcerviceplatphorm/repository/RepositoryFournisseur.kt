package com.example.clientcerviceplatphorm.repository

import com.example.clientcerviceplatphorm.model.Fournisseur
import com.example.clientcerviceplatphorm.service.RetrofiteInstence

class RepositoryFournisseur {

    private val api = RetrofiteInstence.instance

    suspend fun getFournisseurs(): List<Fournisseur>? {
        return try {
            api.getFournisseurs()
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun createFournisseur(fournisseur: Fournisseur): Fournisseur? {
        return try {
            api.createFournisseur(fournisseur)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }

    suspend fun getFournisseurtById(id: String): Fournisseur? {
        return try {
            api.getFournisseurById(id)
        } catch (e: Exception) {
            e.printStackTrace()
            null
        }
    }


    suspend fun deleteFournisseur(id: String): Boolean {
        return try {
            val response = api.deleteFournisseur(id)
            response.isSuccessful
        } catch (e: Exception) {
            e.printStackTrace()
            false
        }
    }
}