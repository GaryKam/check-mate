package com.oukschub.checkmate.viewmodel

import androidx.lifecycle.ViewModel
import com.oukschub.checkmate.data.repository.ChecklistRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val repository: ChecklistRepository
) : ViewModel() {
    fun runTasks(onComplete: () -> Unit) {
    }
}
