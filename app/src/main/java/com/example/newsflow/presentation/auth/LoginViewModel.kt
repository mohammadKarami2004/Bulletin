package com.example.newsflow.presentation.auth

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsflow.data.remote.dto.auth.LoginRequest
import com.example.newsflow.domain.useCase.auth.LoginUseCase
import com.example.newsflow.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject


@HiltViewModel
class LoginViewModel @Inject constructor(private val loginUseCase: LoginUseCase) : ViewModel() {
    private val _uiState = MutableStateFlow(LoginUiState())
    val uiState: StateFlow<LoginUiState> = _uiState.asStateFlow()

    fun login(username: String, password: String) {


        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, error = null) }
            val result = loginUseCase.invoke(LoginRequest(username, password))
            when (result) {

                is Resource.Success -> {

                    Log.d("LoginViewModel", "Success: ${result.data.username}")
                    _uiState.update { it.copy(isLoading = false, isSuccess = true) }
                }

                is Resource.Error -> {
                    Log.d("LoginViewModel", "Error: ${result.message}")
                    _uiState.update { it.copy(isLoading = false, error = result.message) }
                }

                else -> {}
            }


        }
    }

}