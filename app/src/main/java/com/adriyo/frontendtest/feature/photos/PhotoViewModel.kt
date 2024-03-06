package com.adriyo.frontendtest.feature.photos

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.cachedIn
import com.adriyo.frontendtest.data.repository.AuthRepository
import com.adriyo.frontendtest.data.repository.PhotoRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by adriyo on 05/03/2024.
 * <a href="https://github.com/adriyo">Github</a>
 */
@HiltViewModel
class PhotoViewModel @Inject constructor(
    private val photoRepository: PhotoRepository,
    private val authRepository: AuthRepository,
) : ViewModel() {

    private val _uiEvent = Channel<UiEvent>()
    val uiEvent = _uiEvent.receiveAsFlow()

    val photosPaging = photoRepository.getPager().cachedIn(viewModelScope)

    fun changeUiEvent(event: UiEvent) {
        when (event) {
            UiEvent.OnLogout -> {
                onLogoutAction()
            }
        }
    }

    private fun onLogoutAction() {
        viewModelScope.launch {
            photoRepository.deleteAll()
            authRepository.logout()
            _uiEvent.send(UiEvent.OnLogout)
        }
    }

}




