package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.Service
import com.example.campuskonnekt.data.repository.ServiceRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class ServicesState(
    val services: List<Service> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val selectedCategory: String = "All"
)

class ServicesViewModel : ViewModel() {
    private val repository = ServiceRepository()

    private val _servicesState = MutableStateFlow(ServicesState())
    val servicesState: StateFlow<ServicesState> = _servicesState

    init {
        loadServices()
    }

    private fun loadServices() {
        viewModelScope.launch {
            _servicesState.value = _servicesState.value.copy(isLoading = true)

            repository.getServices()
                .catch { e ->
                    _servicesState.value = ServicesState(error = e.message)
                }
                .collect { services ->
                    _servicesState.value = _servicesState.value.copy(
                        services = services,
                        isLoading = false
                    )
                }
        }
    }

    fun setCategory(category: String) {
        _servicesState.value = _servicesState.value.copy(selectedCategory = category)

        if (category == "All") {
            loadServices()
        } else {
            loadServicesByCategory(category)
        }
    }

    private fun loadServicesByCategory(category: String) {
        viewModelScope.launch {
            _servicesState.value = _servicesState.value.copy(isLoading = true)

            repository.getServicesByCategory(category)
                .catch { e ->
                    _servicesState.value = _servicesState.value.copy(
                        error = e.message,
                        isLoading = false
                    )
                }
                .collect { services ->
                    _servicesState.value = _servicesState.value.copy(
                        services = services,
                        isLoading = false
                    )
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
            _servicesState.value = _servicesState.value.copy(isLoading = true)

            repository.createService(title, category, price, description, imageUrl).fold(
                onSuccess = {
                    _servicesState.value = _servicesState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _servicesState.value = _servicesState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
            )
        }
    }

    fun rateService(serviceId: String, rating: Float, review: String) {
        viewModelScope.launch {
            repository.rateService(serviceId, rating, review).fold(
                onSuccess = {
                    // Update will come through the Flow
                },
                onFailure = { exception ->
                    _servicesState.value = _servicesState.value.copy(error = exception.message)
                }
            )
        }
    }

    fun deleteService(serviceId: String) {
        viewModelScope.launch {
            repository.deleteService(serviceId).fold(
                onSuccess = {
                    // Update will come through the Flow
                },
                onFailure = { exception ->
                    _servicesState.value = _servicesState.value.copy(error = exception.message)
                }
            )
        }
    }
}
