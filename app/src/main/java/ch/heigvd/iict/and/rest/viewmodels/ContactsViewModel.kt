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

    val allContacts = repository.allContacts

    // actions
    fun enroll() {
        viewModelScope.launch {
            val url = "https://daa.iict.ch/"
            // Supprimer toutes les données locales
            //deleteAll() //TODO demander si on doit garder les trucs ajoutés localement
            // TODO handle quand pas de connection
            val enrollURL = URL(url + "enroll")
            val contactURL = URL(url + "contacts")
            thread {
                // Get a new UUID
                val uuid = enrollURL.readText(Charsets.UTF_8)

                // Get all contacts corresponding to the UUID
                val connection = contactURL.openConnection() as HttpURLConnection
                // Set the X-UUID header
                connection.setRequestProperty("X-UUID", uuid)
                // TODO check the response code
                // Read the response
                val response = connection.inputStream.bufferedReader().use {it.readText()}
                // Parse the response into a list of contacts
                val contactDTOList = Gson().fromJson(response, Array<ContactDTO>::class.java)
                val contactList = mutableListOf<Contact>()
                contactList.addAll(contactDTOList.map{ it.toContact() })
                // Insert all contacts into the local database
                for (contact in contactList) {
                    insert(contact)
                }
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