package com.example.campuskonnekt.ui.viewmodel.feature

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.campuskonnekt.data.model.Post
import com.example.campuskonnekt.data.repository.PostRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class PostState(
    val isLoading: Boolean = false,
    val posts: List<Post> = emptyList(),
    val error: String? = null
)

class PostViewModel(private val postRepository: PostRepository = PostRepository()) : ViewModel() {
    private val _postState = MutableStateFlow(PostState())
    val postState: StateFlow<PostState> = _postState.asStateFlow()

    init {
        getPosts()
    }

    private fun getPosts() {
        viewModelScope.launch {
            _postState.update { it.copy(isLoading = true) }
            postRepository.getPosts().collect { posts ->
                _postState.update { it.copy(isLoading = false, posts = posts) }
            }
        }
    }

    fun createPost(content: String, imageUrl: String? = null) {
        viewModelScope.launch {
            _postState.update { it.copy(isLoading = true) }
            val result = postRepository.createPost(content, imageUrl)
            result.onFailure { e ->
                _postState.update { it.copy(isLoading = false, error = e.message) }
            }
            // Success is handled by the real-time listener
        }
    }

    fun toggleLike(postId: String) {
        viewModelScope.launch {
            postRepository.toggleLike(postId)
        }
    }

    fun addComment(postId: String, content: String) {
        viewModelScope.launch {
            postRepository.addComment(postId, content)
        }
    }

    fun deletePost(postId: String) {
        viewModelScope.launch {
            postRepository.deletePost(postId)
        }
    }
}
