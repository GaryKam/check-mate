package com.oukschub.checkmate.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.util.FirebaseUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val repository: ChecklistRepository,
) : ViewModel() {
    fun getChecklists(onComplete: () -> Unit) {
        if (FirebaseUtil.isLoggedIn()) {
            viewModelScope.launch {
                repository.getChecklists()
                onComplete()
            }
        } else {
            onComplete()
        }
    }
}
