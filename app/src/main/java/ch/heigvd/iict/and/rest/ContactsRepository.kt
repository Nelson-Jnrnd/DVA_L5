package ch.heigvd.iict.and.rest

import ch.heigvd.iict.and.rest.database.ContactsDao
import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.models.ContactStatus
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class ContactsRepository(private val contactsDao: ContactsDao) {

    val allContacts = contactsDao.getAllContactsLiveData()

    suspend fun enroll(contact: Contact) {
        contact.status = ContactStatus.NEW
        withContext(Dispatchers.IO) {
            contactsDao.insert(contact)
        }
    }

    suspend fun refresh() {
        withContext(Dispatchers.IO) {
            println("Number of contacts in the database: ${contactsDao.getCount()}")
        }
    }

    suspend fun delete(contact: Contact) {
        contact.status = ContactStatus.DELETED
        withContext(Dispatchers.IO) {
            contactsDao.update(contact)
        }
    }

    suspend fun update(contact: Contact) {
        contact.status = ContactStatus.MODIFIED
        withContext(Dispatchers.IO) {
            contactsDao.update(contact)
        }
    }

    suspend fun deleteAll() {
        contactsDao.getAllContacts().forEach {
            it.status = ContactStatus.DELETED
            withContext(Dispatchers.IO) {
                contactsDao.update(it)
            }
        }
    }

    suspend fun insert(contact: Contact) {
        contact.status = ContactStatus.NEW
        contact.id = null
        withContext(Dispatchers.IO) {
            contactsDao.insert(contact)
        }
    }

    companion object {
        private val TAG = "ContactsRepository"
    }

}