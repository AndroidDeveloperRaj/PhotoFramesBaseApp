package com.example.photoframesbaseapp.share

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class ShareAlbumViewModel: ViewModel() {

    private val mutableStateFlow = MutableStateFlow(true)
    val isLoading = mutableStateFlow.asStateFlow()
    private val mutableShareFlow = MutableStateFlow(false)
    val isMutableShareFlow = mutableShareFlow.asStateFlow()

    init {
        viewModelScope.launch {
            delay(2000)
            mutableStateFlow.value = false
        }
    }

    fun shareImage(){
        mutableShareFlow.value = true
    }
}