package org.hilfulfujul.allajhari.repository

import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import org.hilfulfujul.allajhari.books.Book
import org.hilfulfujul.allajhari.books.BookData
import org.hilfulfujul.allajhari.books.Chapter
import org.hilfulfujul.allajhari.books.Information
import org.hilfulfujul.allajhari.setting.Response

class BooksRepository {
    private val firestore = FirebaseFirestore.getInstance()
    private val collectionName = "books"
    fun getAllBooksData(): Flow<Response<List<Book>>> = flow {
        emit(Response.Loading)
        try {
            val snapshot = firestore.collection(collectionName).get().await()
            val bookList = snapshot.documents.mapNotNull { document ->
                Book(
                    bid = document.id,
                    name = document.getString("name") ?: "",
                    cover = document.getString("cover") ?: ""
                )
            }
            emit(Response.Success(bookList))
        } catch (exception: Exception) {
            emit(Response.Error(exception.message ?: "Error fetching data"))
        }
    }

    fun getBookChaptersAndInfo(bid: String): Flow<Response<BookData>> = flow {
        emit(Response.Loading)
        try {
            val bookDocument = firestore.collection(collectionName).document(bid).get().await()

            val chaptersData = bookDocument.get("chapters") as? Map<*, *>
                ?: throw IllegalArgumentException("Invalid 'chapters' data format")
            val info = bookDocument.get("info") as? Map<*, *>
                ?: throw IllegalArgumentException("Invalid 'chapters' data format")

            val chapterList = chaptersData.map { (title, url) ->
                Chapter(title = title as String, url = url as String)
            }.sortedBy { it.title }

            val information = info.map { (title, url) ->
                Information(title = title as String, url = url as String)
            }.sortedBy { it.title }

            val bookData = BookData(chapterList, information)
            emit(Response.Success(bookData))
        } catch (exception: Exception) {
            emit(Response.Error(exception.message ?: "Error fetching data"))
        }
    }
}