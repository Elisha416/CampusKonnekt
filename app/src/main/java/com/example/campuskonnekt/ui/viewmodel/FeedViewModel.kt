package com.example.campuskonnekt.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.Post
import com.example.campuskonnekt.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch

data class FeedState(
    val posts: List<Post> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class FeedViewModel : ViewModel() {
    private val repository = PostRepository()

    private val _feedState = MutableStateFlow(FeedState())
    val feedState: StateFlow<FeedState> = _feedState

    init {
        loadPosts()
    }

    private fun loadPosts() {
        viewModelScope.launch {
            _feedState.value = _feedState.value.copy(isLoading = true)

            repository.getPosts()
                .catch { e ->
                    _feedState.value = FeedState(error = e.message)
                }
                .collect { posts ->
                    _feedState.value = FeedState(posts = posts)
                }
        }
    }

    fun createPost(content: String, imageUrl: String? = null) {
        viewModelScope.launch {
            _feedState.value = _feedState.value.copy(isLoading = true)

            repository.createPost(content, imageUrl).fold(
                onSuccess = {
                    // Post will be added automatically via the Flow
                    _feedState.value = _feedState.value.copy(isLoading = false)
                },
                onFailure = { exception ->
                    _feedState.value = _feedState.value.copy(
                        isLoading = false,
                        error = exception.message
                    )
                }
            )
        }
    }

    fun toggleLike(postId: String) {
        viewModelScope.launch {
            repository.toggleLike(postId).fold(
                onSuccess = {
                    // Update will come through the Flow
                },
                onFailure = { exception ->
                    _feedState.value = _feedState.value.copy(error = exception.message)
                }
            )
        }
    }

    fun addComment(postId: String, content: String) {
        viewModelScope.launch {
            repository.addComment(postId, content).fold(
                onSuccess = {
                    // Update will come through the Flow
                },
                onFailure = { exception ->
                    _feedState.value = _feedState.value.copy(error = exception.message)
                }
            )
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            repository.deletePost(postId).fold(
                onSuccess = {
                    // Update will come through the Flow
                },
                onFailure = { exception ->
                    _feedState.value = _feedState.value.copy(error = exception.message)
                }
            )
        }
    }
}