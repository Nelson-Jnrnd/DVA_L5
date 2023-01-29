package ch.heigvd.iict.and.rest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.Button
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.MutableLiveData
import ch.heigvd.iict.and.rest.databinding.ActivityMainBinding
import ch.heigvd.iict.and.rest.fragments.ContactFragment
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
            val contactFragment =
                ContactFragment.newInstance(MutableLiveData<Contact?>().apply { value = null })
            supportFragmentManager.beginTransaction()
                .replace(R.id.main_content_fragment, contactFragment)
                .addToBackStack(null)
                .commit()
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