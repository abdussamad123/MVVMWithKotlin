package com.mvvm

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mvvm.appcomponent.domain.model.response.UserDetailsData
import com.mvvm.appcomponent.domain.repository.BaseRepository
import com.mvvm.appcomponent.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(
    private val baseRepository: BaseRepository
) : ViewModel() {


    private val _userDetails = MutableStateFlow<Resource< out List<UserDetailsData>>>(Resource.Loading())
    val userDetails = _userDetails.asStateFlow()


    fun getUserDetails(){
        viewModelScope.launch {
            baseRepository.getUserDetails().collectLatest{
                _userDetails.value=it
            }
        }
    }
}