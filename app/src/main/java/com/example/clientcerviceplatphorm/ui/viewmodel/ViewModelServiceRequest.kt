package com.example.clientcerviceplatphorm.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.clientcerviceplatphorm.model.ServiceRequest
import com.example.clientcerviceplatphorm.repository.RepositoryServiceRequest
import kotlinx.coroutines.launch

class ViewModelServiceRequest : ViewModel() {

    private val repo = RepositoryServiceRequest()

    private val _serviceRequests = MutableLiveData<List<ServiceRequest>>(emptyList())
    val serviceRequests: LiveData<List<ServiceRequest>> = _serviceRequests

    private val _createServiceRequest = MutableLiveData<ServiceRequest?>()
    val createServiceRequest: LiveData<ServiceRequest?> = _createServiceRequest

    private val _serviceRequestById = MutableLiveData<ServiceRequest?>()
    val serviceRequestById: LiveData<ServiceRequest?> = _serviceRequestById

    private val _deleteStatus = MutableLiveData<Boolean>()
    val deleteStatus: LiveData<Boolean> = _deleteStatus

    private val _updateServiceRequest = MutableLiveData<ServiceRequest?>()
    val updateServiceRequest: LiveData<ServiceRequest?> = _updateServiceRequest

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun getServiceRequests() {
        viewModelScope.launch {
            try {
                _serviceRequests.value = repo.getServiceRequests() ?: emptyList()
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun createServiceRequest(serviceRequest: ServiceRequest) {
        viewModelScope.launch {
            try {
                val response = repo.createServiceRequest(serviceRequest)
                if (response != null) {
                    _createServiceRequest.value = response
                } else {
                    _error.value = "Error creating service request!"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun getServiceRequestById(id: String) {
        viewModelScope.launch {
            try {
                val response = repo.getServiceRequestById(id)
                if (response != null) {
                    _serviceRequestById.value = response
                } else {
                    _error.value = "Error getting service request!"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun deleteServiceRequest(id: String) {
        viewModelScope.launch {
            try {
                val success = repo.deleteServiceRequest(id)
                _deleteStatus.value = success
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }

    fun updateServiceRequest(id: String, serviceRequest: ServiceRequest) {
        viewModelScope.launch {
            try {
                val response = repo.updateServiceRequest(id, serviceRequest)
                if (response != null) {
                    _updateServiceRequest.value = response
                } else {
                    _error.value = "Error updating service request!"
                }
            } catch (e: Exception) {
                _error.value = e.message
            }
        }
    }
}