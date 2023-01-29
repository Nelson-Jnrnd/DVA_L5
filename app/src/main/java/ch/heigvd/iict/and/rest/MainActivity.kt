package ch.heigvd.iict.and.rest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import ch.heigvd.iict.and.rest.databinding.ActivityMainBinding
import ch.heigvd.iict.and.rest.fragments.ListFragment
import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.models.ContactStatus
import ch.heigvd.iict.and.rest.models.PhoneType
import ch.heigvd.iict.and.rest.viewmodels.ContactsViewModel
import ch.heigvd.iict.and.rest.viewmodels.ContactsViewModelFactory
import java.util.*

class MainActivity : AppCompatActivity() {

    private lateinit var binding : ActivityMainBinding
    private val contactsViewModel: ContactsViewModel by viewModels {
        ContactsViewModelFactory((application as ContactsApplication).repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val plusButton = findViewById<Button>(R.id.main_fab_new)
        /*plusButton.setOnClickListener {
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_content_fragment, EditFragment.newInstance())
                .addToBackStack(null)
                .commit()
        }*/

        binding.mainFabNew.setOnClickListener {
            // FIXME - create a new contact
            Toast.makeText(this, "TODO - Création d'un nouveau contact", Toast.LENGTH_SHORT).show()

            contactsViewModel.insert(
                Contact(id = null,
                    remote_id = null,
                    status = ContactStatus.NEW,
                    name = "Fisher",
                    firstname = "Brenda",
                    birthday = GregorianCalendar.getInstance().apply {
                        set(Calendar.YEAR, 2001)
                        set(Calendar.MONTH, Calendar.JULY)
                        set(Calendar.DAY_OF_MONTH, 9)
                        set(Calendar.HOUR_OF_DAY, 12)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    },
                    email = "b.fisher@heig-vd.ch",
                    address = "Avenue des Sports 20",
                    zip = "1400", city = "Yverdon-les-Bains",
                    type = PhoneType.MOBILE, phoneNumber = "079 111 22 33" )
            )
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when(item.itemId) {
            R.id.menu_main_synchronize -> {
                contactsViewModel.refresh()
                true
            }
            R.id.menu_main_populate -> {
                contactsViewModel.enroll()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    companion object {
        private val TAG = MainActivity::class.java.simpleName
    }

}