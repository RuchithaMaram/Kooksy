package com.teamfour.kooksy.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.teamfour.kooksy.MyApp
import kotlinx.coroutines.launch

class ProfileViewModel : ViewModel() {

    private val _isProfileDetailsUpdated: MutableLiveData<Boolean> = MutableLiveData()
    val liveData : LiveData<Boolean> = _isProfileDetailsUpdated

    private val _text = MutableLiveData<String>().apply {
        value = "This is Profile Fragment"
    }
    val text: LiveData<String> = _text

    fun updateProfileDetails(userName: String, user: User) {
        viewModelScope.launch {
            val reference = MyApp.db.collection("users").document(user.user_id)
            reference.update("user_name", userName)
                .addOnSuccessListener {
                    _isProfileDetailsUpdated.value = true
                }
                .addOnFailureListener {
                    _isProfileDetailsUpdated.value = false
                }
        }
    }

}
