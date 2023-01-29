package ch.heigvd.iict.and.rest.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import ch.heigvd.iict.and.rest.ContactsApplication
import ch.heigvd.iict.and.rest.R
import ch.heigvd.iict.and.rest.models.Contact
import ch.heigvd.iict.and.rest.viewmodels.ContactsViewModel
import ch.heigvd.iict.and.rest.viewmodels.ContactsViewModelFactory

class ContactFragment(private var contactLiveData: LiveData<Contact?> = MutableLiveData<Contact?>(null)) : Fragment() {

    private val contactsViewModel: ContactsViewModel by activityViewModels {
        ContactsViewModelFactory(((requireActivity().application as ContactsApplication).repository))
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // TODO save contact and / or edittext values ?
        if (savedInstanceState != null) {
            contactLiveData = MutableLiveData<Contact?>().apply {
                value = savedInstanceState.getSerializable("contactLiveData") as Contact?
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return inflater.inflate(R.layout.fragment_edit, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        view.findViewById<View>(R.id.editCancel).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        view.findViewById<View>(R.id.editDelete).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
        view.findViewById<View>(R.id.editSave).setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        contactLiveData.observe(viewLifecycleOwner) { updatedContact ->
           //TODO populate the view with the contact data
        }
    }



    companion object {
        @JvmStatic
        fun newInstance(contact: LiveData<Contact?>) =
            ContactFragment(contact)

        private val TAG = ContactFragment::class.java.simpleName
    }

}