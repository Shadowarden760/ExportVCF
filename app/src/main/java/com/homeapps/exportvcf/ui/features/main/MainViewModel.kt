package com.homeapps.exportvcf.ui.features.main

import androidx.lifecycle.ViewModel
import com.bilalazzam.contacts_provider.Contact
import com.bilalazzam.contacts_provider.ContactField
import com.bilalazzam.contacts_provider.ContactsProvider
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {
    val contacts: StateFlow<List<Contact>>
        field = MutableStateFlow<List<Contact>>(emptyList())

    fun getContacts(contactsProvider: ContactsProvider) {
        CoroutineScope(Dispatchers.IO).launch {
            val savedContacts = contactsProvider.getAllContacts(
                fields = setOf(
                    ContactField.ID,
                    ContactField.FIRST_NAME,
                    ContactField.LAST_NAME,
                    ContactField.PHONE_NUMBERS
                )
            )
            contacts.value = savedContacts
        }
    }

    fun resetContacts() {
        contacts.value = emptyList()
    }

    fun createVCFCards(): ByteArray {
        val stringBuilder = StringBuilder()
        contacts.value.forEach { contact ->
            val card =
                "BEGIN:VCARD\n" +
                "VERSION:3.0\n" +
                "N:${contact.lastName};${contact.firstName};;;\n" +
                "FN:${contact.firstName} ${contact.lastName}\n" +
                "${getContactPhones(contact)}\n" +
                "END:VCARD\n\n"
            stringBuilder.append(card)
        }
        return stringBuilder.toString().toByteArray()
    }

    private fun getContactPhones(contact: Contact): String {
        val stringBuilder = StringBuilder()
        contact.phoneNumbers.forEachIndexed { index, phone ->
            if (index != contact.phoneNumbers.size - 1) {
                stringBuilder.append("TEL;CELL:$phone\n")
            } else {
                stringBuilder.append("TEL;CELL:$phone")
            }
        }
        return stringBuilder.toString()
    }
}