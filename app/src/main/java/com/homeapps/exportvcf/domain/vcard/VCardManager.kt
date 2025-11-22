package com.homeapps.exportvcf.domain.vcard

import android.content.ContentResolver
import android.database.Cursor
import android.provider.ContactsContract

class VCardManager {

    fun loadContacts(contentResolver: ContentResolver): MutableList<String> {
        val contactsList = mutableListOf<String>()
        val projection = arrayOf(
            ContactsContract.Contacts._ID,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY
        )

        val cursor: Cursor? = contentResolver.query(
            ContactsContract.Contacts.CONTENT_URI,
            projection,
            null,
            null,
            ContactsContract.Contacts.DISPLAY_NAME_PRIMARY + " ASC"
        )

        cursor?.use { cursor ->
            val idColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts._ID)
            val nameColumnIndex = cursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME_PRIMARY)

            while (cursor.moveToNext()) {
                val contactId = cursor.getString(idColumnIndex)
                val displayName = cursor.getString(nameColumnIndex)

                val phoneCursor: Cursor? = contentResolver.query(
                    ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                    arrayOf(ContactsContract.CommonDataKinds.Phone.NUMBER),
                    ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ?",
                    arrayOf(contactId),
                    null
                )

                phoneCursor?.use { phoneCursor ->
                    val numberColumnIndex = phoneCursor.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER)
                    while (phoneCursor.moveToNext()) {
                        val phoneNumber = phoneCursor.getString(numberColumnIndex)
                        contactsList.add("Name: $displayName, Phone: $phoneNumber")
                    }
                }
            }
        }
        return contactsList
    }
}