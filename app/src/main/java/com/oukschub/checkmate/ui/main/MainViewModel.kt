package com.oukschub.checkmate.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.data.repository.UserRepository
import com.oukschub.checkmate.util.FirebaseUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val checklistRepository: ChecklistRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    var isAppReady = false
        private set

    fun loadData() {
        if (FirebaseUtil.isSignedIn()) {
            viewModelScope.launch {
                checklistRepository.fetchFavoriteChecklists()
                isAppReady = true
            }

            viewModelScope.launch {
                userRepository.fetchDisplayName()
                checklistRepository.fetchChecklists()
            }
        } else {
            isAppReady = true
        }
    }
}
