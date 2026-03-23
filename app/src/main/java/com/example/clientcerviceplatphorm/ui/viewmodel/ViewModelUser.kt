package com.example.clientcerviceplatphorm.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientcerviceplatphorm.model.*
import com.example.clientcerviceplatphorm.repository.RepositoryAdmin
import com.example.clientcerviceplatphorm.repository.RepositoryClient
import com.example.clientcerviceplatphorm.repository.RepositoryFournisseur
import kotlinx.coroutines.launch

class ViewModelUser : ViewModel() {

    private val repoAdmin = RepositoryAdmin()
    private val repoClient = RepositoryClient()
    private val repoFournisseur = RepositoryFournisseur()

    private val _users = MutableLiveData<List<User>>(emptyList())
    val users: LiveData<List<User>> = _users

    private val _createdUser = MutableLiveData<User?>()
    val createdUser: LiveData<User?> = _createdUser

    private val _loginResult = MutableLiveData<User?>()
    val loginResult: LiveData<User?> = _loginResult

    private val _userById = MutableLiveData<User?>()
    val userById: LiveData<User?> = _userById

    private val _error = MutableLiveData<String?>()
    val error: LiveData<String?> = _error


    fun createUser(user: User) {
        viewModelScope.launch {
            try {
                when(user) {
                    is User.AdminUser -> {
                        val response = repoAdmin.createAdmin(user.admin)
                        if(response != null) _createdUser.value = user
                        else _error.value = "Error creating Admin"
                    }
                    is User.ClientUser -> {
                        val response = repoClient.createClient(user.client)
                        if(response != null) _createdUser.value = user
                        else _error.value = "Error creating Client"
                    }
                    is User.FournisseurUser -> {
                        val response = repoFournisseur.createFournisseur(user.fournisseur)
                        if(response != null) _createdUser.value = user
                        else _error.value = "Error creating Fournisseur"
                    }
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    fun getUsers() {
        viewModelScope.launch {
            try {
                val admins = repoAdmin.getAdmins()?.map { User.AdminUser(it) } ?: emptyList()
                val clients = repoClient.getClients()?.map { User.ClientUser(it) } ?: emptyList()
                val fournisseurs = repoFournisseur.getFournisseurs()?.map { User.FournisseurUser(it) } ?: emptyList()

                _users.value = admins + clients + fournisseurs
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val allUsers = _users.value ?: emptyList()
                val found = allUsers.find {
                    when(it) {
                        is User.AdminUser -> it.admin.email == email && it.admin.password == password
                        is User.ClientUser -> it.client.email == email && it.client.password == password
                        is User.FournisseurUser -> it.fournisseur.email == email && it.fournisseur.password == password
                    }
                }
                _loginResult.value = found
                if(found == null) _error.value = "Email or password incorrect"
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    fun getUserById(id: String) {
        viewModelScope.launch {
            try {
                val admin = repoAdmin.getAdminById(id)
                if (admin != null) {
                    _userById.value = User.AdminUser(admin)
                    return@launch
                }

                val client = repoClient.getClientById(id)
                if (client != null) {
                    _userById.value = User.ClientUser(client)
                    return@launch
                }

                val fournisseur = repoFournisseur.getFournisseurtById(id)
                if (fournisseur != null) {
                    _userById.value = User.FournisseurUser(fournisseur)
                    return@launch
                }

                _error.value = "User not found"
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }


    fun deleteUser(id: String) {
        viewModelScope.launch {
            try {
                var deleted = false


                val admin = repoAdmin.getAdminById(id)
                if(admin != null){
                    deleted = repoAdmin.deleteAdmin(id)
                }


                if(!deleted){
                    val client = repoClient.getClientById(id)
                    if(client != null){
                        deleted = repoClient.deleteClient(id)
                    }
                }


                if(!deleted){
                    val fournisseur = repoFournisseur.getFournisseurtById(id)
                    if(fournisseur != null){
                        deleted = repoFournisseur.deleteFournisseur(id)
                    }
                }

                if(deleted){
                    _userById.value = null
                } else {
                    _error.value = "User not found or cannot be deleted"
                }

            } catch (e: Exception){
                _error.value = e.message
            }
        }
    }
}