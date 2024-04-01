package com.oukschub.checkmate.ui.profile

import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.repository.ChecklistRepository
import com.oukschub.checkmate.data.repository.UserRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val checklistRepository: ChecklistRepository,
    private val userRepository: UserRepository
) : ViewModel() {
    val displayName = userRepository.displayName

    fun setNewDisplayName(displayName: String) {
        userRepository.setNewDisplayName(displayName.trim())
    }

    fun signOut() {
        checklistRepository.clearChecklists()
        userRepository.signOut()
    }
}
