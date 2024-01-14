package com.oukschub.checkmate.viewmodel

import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.database.Database

class ProfileViewModel(
    private val database: Database = Database()
) : ViewModel() {
    fun loadDisplayNameFromDb(): String {
        return database.loadDisplayName()
    }
}
