package com.aloysius.voicehealthguide.data.pref

data class UserModel(
    val name: String,
    val username: String,
    val email: String,
    val token: String,
    val isLogin: Boolean,
    val timestamp: Long = System.currentTimeMillis()
)