package com.homeapps.exportvcf.ui.features.main

import androidx.lifecycle.ViewModel
import com.bilalazzam.contacts_provider.Contact
import com.bilalazzam.contacts_provider.ContactField
import com.bilalazzam.contacts_provider.ContactsProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    private val _contacts = MutableStateFlow<List<Contact>>(emptyList())
    val contacts = _contacts.asStateFlow()

    fun getContacts(contactsProvider: ContactsProvider) {
        CoroutineScope(Dispatchers.IO).launch {
            val contacts = contactsProvider.getAllContacts(
                fields = setOf(
                    ContactField.ID,
                    ContactField.FIRST_NAME,
                    ContactField.LAST_NAME,
                    ContactField.PHONE_NUMBERS
                )
            )
            _contacts.value = contacts
        }
    }

    fun resetContacts() {
        _contacts.value = emptyList()
    }
}