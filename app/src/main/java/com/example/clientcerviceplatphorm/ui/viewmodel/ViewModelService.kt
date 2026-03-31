package com.example.clientcerviceplatphorm.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientcerviceplatphorm.model.Service
import com.example.clientcerviceplatphorm.repository.RepositoryService
import kotlinx.coroutines.launch

class ViewModelService: ViewModel() {
    private val repoService = RepositoryService()

    private val _services = MutableLiveData<List<Service>>(emptyList())
    val services : LiveData<List<Service>> = _services

    private val _createService = MutableLiveData<Service>()
    val createService: LiveData<Service> = _createService
    private val _SrviceById = MutableLiveData<Service?>()
    val SrviceById: LiveData<Service?> = _SrviceById

    private val _deleteSuccess = MutableLiveData<Boolean>()
    val deleteSuccess: LiveData<Boolean> = _deleteSuccess

    private val _error = MutableLiveData<String>()
    val error : LiveData<String> = _error

    fun getServices(){
        viewModelScope.launch {
            try {
                _services.value = repoService.getServices()
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun createService(service: Service){
        viewModelScope.launch {
            try {
                val repence = repoService.createService(service)
                if (repence != null) _createService.value = service
                else _error.value = "error create service !"
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun getServiceById(id: String) {
        viewModelScope.launch {
            try {
                val repence = repoService.getServiceById(id)
                if (repence != null) _SrviceById.value = repence
                else _error.value = "error geting service !"
            }catch (e: Exception){
                _error.value = e.message
            }
        }
    }

    fun updateService(id: String, service: Service) {
        viewModelScope.launch {
            try {
                val response = repoService.updateService(id, service)
                if (response != null) {
                    getServices() 
                } else {
                    _error.value = "error updating service !"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteService(id: String) {
        viewModelScope.launch {
            try {
                val success = repoService.deleteService(id)
                _deleteSuccess.value = success
                if (!success) _error.value = "Error deleting service"
            } catch (e: Exception) {
                _error.value = e.message
                _deleteSuccess.value = false
            }
        }
    }
}