package org.hilfulfujul.allajhari.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.asLiveData
import org.hilfulfujul.allajhari.books.BookData
import org.hilfulfujul.allajhari.repository.BooksRepository
import org.hilfulfujul.allajhari.setting.Response

class BooksViewModel : ViewModel() {
    private val repository: BooksRepository = BooksRepository()

    val bookData = repository.getAllBooksData().asLiveData()
    fun getChapterData(bid: String): LiveData<Response<BookData>> =
        repository.getBookChaptersAndInfo(bid).asLiveData()
}