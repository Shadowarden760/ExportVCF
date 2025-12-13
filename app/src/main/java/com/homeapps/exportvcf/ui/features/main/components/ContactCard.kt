package com.homeapps.exportvcf.ui.features.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bilalazzam.contacts_provider.Contact

@Composable
fun ContactCards(contacts: List<Contact>, modifier: Modifier) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(contacts) {contact ->
            Card(
                shape = CardDefaults.outlinedShape,
                border = CardDefaults.outlinedCardBorder()
            ) {
                Text(
                    text = "First Name: ${contact.firstName}",
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                Text(
                    text = "Last Name: ${contact.lastName}",
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                Text(
                    text = "Phones:",
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(start = 8.dp, top = 8.dp, end = 8.dp, bottom = 4.dp)
                )
                contact.phoneNumbers.forEachIndexed { index, phone ->
                    Text(
                        text = "${index.inc()}) $phone",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun ContactCardsPreview() {
    ContactCards(contacts = listOf(
        Contact(
            id = "1",
            firstName = "first name",
            lastName = "last name",
            phoneNumbers = listOf("12342312", "12314234")
        ),
        Contact(
            id = "2",
            firstName = "first name",
            lastName = "last name",
            phoneNumbers = listOf("12342312", "12314234")
        )
    ), modifier = Modifier.padding(16.dp))
}