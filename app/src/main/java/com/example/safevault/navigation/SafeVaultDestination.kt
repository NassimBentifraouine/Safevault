package com.example.safevault.navigation

import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    data object DocumentsList : Route

    @Serializable
    data object AddDocument : Route

    @Serializable
    data class DocumentDetail(val documentId: Long) : Route

    @Serializable
    data class EditDocument(val documentId: Long) : Route
}
