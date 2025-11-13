package com.example.campuskonnekt.ui.viewmodel.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.Service
import com.example.campuskonnekt.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class ServiceState(
    val isLoading: Boolean = false,
    val services: List<Service> = emptyList(),
    val error: String? = null
)

class ServiceViewModel(private val serviceRepository: ServiceRepository = ServiceRepository()) : ViewModel() {
    private val _serviceState = MutableStateFlow(ServiceState())
    val serviceState: StateFlow<ServiceState> = _serviceState.asStateFlow()

    init {
        getServices()
    }

    private fun getServices() {
        viewModelScope.launch {
            _serviceState.update { it.copy(isLoading = true) }
            serviceRepository.getServices().collect { services ->
                _serviceState.update { it.copy(isLoading = false, services = services) }
            }
        }
    }

    fun getServicesByCategory(category: String) {
        viewModelScope.launch {
            _serviceState.update { it.copy(isLoading = true) }
            serviceRepository.getServicesByCategory(category).collect { services ->
                _serviceState.update { it.copy(isLoading = false, services = services) }
            }
        }
    }

    fun createService(
        title: String,
        category: String,
        price: String,
        description: String,
        imageUrl: String? = null
    ) {
        viewModelScope.launch {
            _serviceState.update { it.copy(isLoading = true) }
            val result = serviceRepository.createService(title, category, price, description, imageUrl)
            result.onFailure { e ->
                _serviceState.update { it.copy(isLoading = false, error = e.message) }
            }
            // Success is handled by the real-time listener
        }
    }

    fun rateService(serviceId: String, rating: Float, review: String) {
        viewModelScope.launch {
            serviceRepository.rateService(serviceId, rating, review)
        }
    }

    fun deleteService(serviceId: String) {
        viewModelScope.launch {
            serviceRepository.deleteService(serviceId)
        }
    }
}
