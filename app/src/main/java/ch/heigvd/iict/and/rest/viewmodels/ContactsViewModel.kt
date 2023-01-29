package ch.heigvd.iict.and.rest.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import ch.heigvd.iict.and.rest.ContactsRepository
import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.models.ContactDTO
import com.google.gson.Gson
import kotlinx.coroutines.launch
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class ContactsViewModel(private val repository: ContactsRepository) : ViewModel() {

    private val path = "https://daa.iict.ch"
    private val enrollPath: String = "$path/enroll"
    private val contactsPath: String = "$path/contacts"
    private var uuid: String? = null
    private val uuid_header = "X-UUID"
    val allContacts = repository.allContacts

    // actions
    fun enroll() {
        viewModelScope.launch {
            val enrollURL = URL(enrollPath)
            val contactURL = URL(contactsPath)
            thread {
                deleteAll()
                uuid = enrollURL.readText(Charsets.UTF_8)
                val connection = contactURL.openConnection() as HttpURLConnection
                connection.setRequestProperty(uuid_header, uuid)
                val data = connection.inputStream.bufferedReader().use { it.readText() }
                val contacts = Gson().fromJson(data, Array<ContactDTO>::class.java)
                contacts.forEach { contact ->
                        insert(contact.toContact())
                }
                println( "Enrolled with UUID: $uuid")
            }
        }
    }

    fun refresh() {
        viewModelScope.launch {
            repository.refresh()
        }
    }

    fun insert(contact: Contact) {
        viewModelScope.launch {
            repository.insert(contact)
        }
    }

    fun deleteAll() {
        viewModelScope.launch {
            repository.deleteAll()
        }
    }

    fun delete(contact: Contact) {
        viewModelScope.launch {
            repository.delete(contact)
        }
    }

    fun update(contact: Contact) {
        viewModelScope.launch {
            repository.update(contact)
        }
    }

}

class ContactsViewModelFactory(private val repository: ContactsRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ContactsViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ContactsViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}