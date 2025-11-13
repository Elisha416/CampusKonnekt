package com.example.campuskonnekt.data.repository

import com.example.campuskonnekt.data.model.Comment
import com.example.campuskonnekt.data.model.Post
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await

class PostRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()
    private val postsCollection = firestore.collection("posts")
    private val commentsCollection = firestore.collection("comments")

    // Create a new post
    suspend fun createPost(content: String, imageUrl: String? = null): Result<Post> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
            val userName = userDoc.getString("displayName") ?: "Unknown"
            val userMajor = userDoc.getString("major") ?: "Unknown"

            val postId = postsCollection.document().id
            val post = Post(
                id = postId,
                authorId = currentUser.uid,
                authorName = userName,
                authorMajor = userMajor,
                content = content,
                imageUrl = imageUrl
            )

            postsCollection.document(postId).set(post).await()
            Result.success(post)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Get posts as a Flow (real-time updates)
    fun getPosts(): Flow<List<Post>> = callbackFlow {
        val listener = postsCollection
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    close(error)
                    return@addSnapshotListener
                }

                val posts = snapshot?.documents?.mapNotNull {
                    it.toObject(Post::class.java)
                } ?: emptyList()

                trySend(posts)
            }

        awaitClose { listener.remove() }
    }

    // Like/Unlike a post
    suspend fun toggleLike(postId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val postRef = postsCollection.document(postId)

            firestore.runTransaction { transaction ->
                val post = transaction.get(postRef).toObject(Post::class.java)
                    ?: throw Exception("Post not found")

                val likes = post.likes.toMutableList()
                if (likes.contains(currentUser.uid)) {
                    likes.remove(currentUser.uid)
                } else {
                    likes.add(currentUser.uid)
                }

                transaction.update(postRef, "likes", likes)
            }.await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Add comment to post
    suspend fun addComment(postId: String, content: String): Result<Comment> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val userDoc = firestore.collection("users").document(currentUser.uid).get().await()
            val userName = userDoc.getString("displayName") ?: "Unknown"

            val commentId = commentsCollection.document().id
            val comment = Comment(
                id = commentId,
                postId = postId,
                authorId = currentUser.uid,
                authorName = userName,
                content = content
            )

            commentsCollection.document(commentId).set(comment).await()

            // Update comment count
            val postRef = postsCollection.document(postId)
            firestore.runTransaction { transaction ->
                val post = transaction.get(postRef).toObject(Post::class.java)
                    ?: throw Exception("Post not found")
                transaction.update(postRef, "commentCount", post.commentCount + 1)
            }.await()

            Result.success(comment)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    // Delete post
    suspend fun deletePost(postId: String): Result<Unit> {
        return try {
            val currentUser = auth.currentUser ?: throw Exception("User not logged in")
            val postDoc = postsCollection.document(postId).get().await()
            val post = postDoc.toObject(Post::class.java) ?: throw Exception("Post not found")

            if (post.authorId != currentUser.uid) {
                throw Exception("Unauthorized to delete this post")
            }

            postsCollection.document(postId).delete().await()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
