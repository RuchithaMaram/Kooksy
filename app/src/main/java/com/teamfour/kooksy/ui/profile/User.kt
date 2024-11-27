package com.teamfour.kooksy.ui.profile

data class User(
    val email: String,
    val joinDate: Long,
    val profilePhotoURL: String,
    val user_id: String,
    var user_name: String,
)
