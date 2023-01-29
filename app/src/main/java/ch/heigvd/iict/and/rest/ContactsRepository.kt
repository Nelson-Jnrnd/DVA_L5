package ch.heigvd.iict.and.rest

import ch.heigvd.iict.and.rest.database.ContactsDao
import ch.heigvd.iict.and.rest.database.converters.CalendarConverter
import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.models.ContactStatus
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import java.util.Calendar

class ContactsRepository(private val contactsDao: ContactsDao) {


    val allContacts = contactsDao.getAllContactsLiveData()

    suspend fun refresh() {
        withContext(Dispatchers.IO) {

        }
    }

    suspend fun delete(contact: Contact) {
        //contact.status = ContactStatus.DELETED
        withContext(Dispatchers.IO) {
            contactsDao.update(contact)
        }
    }

    suspend fun update(contact: Contact) {
        //contact.status = ContactStatus.MODIFIED
        withContext(Dispatchers.IO) {
            contactsDao.update(contact)
        }
    }

    suspend fun deleteAll() {
        withContext(Dispatchers.IO) {
            contactsDao.getAllContacts().forEach {
                //it.status = ContactStatus.DELETED
                contactsDao.delete(it)
            }
        }
    }

    suspend fun insert(contact: Contact) {
        //contact.status = ContactStatus.NEW
        contact.id = null
        withContext(Dispatchers.IO) {
            contactsDao.insert(contact)
        }
    }

    companion object {
        private val TAG = "ContactsRepository"
    }

}