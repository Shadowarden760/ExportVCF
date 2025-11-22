package com.homeapps.exportvcf.domain.vcard.models

data class VCardData(
    val firstName: String,
    val lastName: String,
    val phones: List<String>,
    val emails: List<String>,
    val organization: String? = null,
    val title: String? = null
)