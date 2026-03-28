package com.homeapps.exportvcf.ui.features.main.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
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
    LazyVerticalGrid(
        columns = GridCells.Fixed(count = 2),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
        modifier = modifier
    ) {
        items(contacts) { contact ->
            Card(
                shape = CardDefaults.outlinedShape,
                border = CardDefaults.outlinedCardBorder()
            ) {
                Text(
                    text = "${contact.firstName} ${contact.lastName}",
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )
                contact.phoneNumbers.forEach { phone ->
                    Text(
                        text = phone,
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
    ContactCards(
        contacts = listOf(
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
        ),
        modifier = Modifier.padding(16.dp)
    )
}