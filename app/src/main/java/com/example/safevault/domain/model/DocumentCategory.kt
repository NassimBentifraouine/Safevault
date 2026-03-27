package com.example.safevault.domain.model

import androidx.annotation.StringRes
import com.example.safevault.R

enum class DocumentCategory(@param:StringRes val labelRes: Int) {
    IDENTITY(R.string.category_identity),
    HEALTH(R.string.category_health),
    INSURANCE(R.string.category_insurance),
    EDUCATION(R.string.category_education),
    FINANCE(R.string.category_finance),
    OTHER(R.string.category_other),
}
