package com.example.safevault.navigation

object SafeVaultDestination {
    const val DOCUMENTS_LIST_ROUTE = "documents_list"
    const val ADD_DOCUMENT_ROUTE = "add_document"
    const val DOCUMENT_DETAIL_ROUTE = "document_detail/{documentId}"
    const val EDIT_DOCUMENT_ROUTE = "edit_document/{documentId}"

    fun detailRoute(documentId: Long): String {
        return "document_detail/$documentId"
    }

    fun editRoute(documentId: Long): String {
        return "edit_document/$documentId"
    }
}
