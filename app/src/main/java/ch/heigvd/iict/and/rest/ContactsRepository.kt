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

    private val path = "https://daa.iict.ch"
    private val enrollPath: String = "$path/enroll"
    private val contactsPath: String = "$path/contacts"
    private var uuid: String? = null
    private val uuid_header = "X-UUID"
    val allContacts = contactsDao.getAllContactsLiveData()

    suspend fun enroll() {
        withContext(Dispatchers.IO) {
            contactsDao.clearAllContacts()
            uuid = URL(enrollPath).readText(Charsets.UTF_8)
            /*// Get all the contacts as json array from the server uuid is sent in the header
            val contactsJson = URL(contactsPath)
                .openConnection()
                .apply {
                    setRequestProperty(uuid_header, uuid!!)
                }
                .getInputStream()
                .bufferedReader()
                .use { it.readText() }
            // Parse the json array and insert the contacts in the database using GSON
            println(contactsJson)
            val contacts = GsonBuilder()
                /*.registerTypeAdapter(Calendar::class.java, CalendarDeserializer())*/
                .create()
                .fromJson(contactsJson, mutableListOf<Contact>()::class.java)
            contacts.forEach { contact ->
                run {
                    //contact.status = ContactStatus.OK
                    contactsDao.insert(contact)
                }
            }*/

            val url = URL("https://daa.iict.ch/contacts")


            val conn = url.openConnection() as HttpURLConnection
            conn.requestMethod = "GET"
            conn.setRequestProperty("x-uuid", uuid!!)

            var data = "";
            BufferedReader(InputStreamReader(conn.inputStream)).use { br ->
                data = br.readText()
            }

            val gson = Gson()
            val valtype = mutableListOf<Contact>()::class.java
            val result = gson.fromJson(data, valtype)

            result.forEach { contact ->
                run {
                    //contact.status = ContactStatus.OK
                    contactsDao.insert(contact)
                }
            }
        }
    }

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
        contactsDao.getAllContacts().forEach {
            //it.status = ContactStatus.DELETED
            withContext(Dispatchers.IO) {
                contactsDao.update(it)
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