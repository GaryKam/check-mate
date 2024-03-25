package com.oukschub.checkmate.ui.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.data.repository.UserRepository
import com.oukschub.checkmate.util.FirebaseUtil
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val checklistRepository: ChecklistRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    fun runTasks(onComplete: () -> Unit) {
        if (FirebaseUtil.isSignedIn()) {
            viewModelScope.launch {
                checklistRepository.fetchFavoriteChecklists()
                onComplete()
            }

            viewModelScope.launch {
                userRepository.fetchDisplayName()
                checklistRepository.fetchChecklists()
            }
        } else {
            onComplete()
        }
    }
}
