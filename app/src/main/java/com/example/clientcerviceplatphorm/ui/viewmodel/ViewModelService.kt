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
}