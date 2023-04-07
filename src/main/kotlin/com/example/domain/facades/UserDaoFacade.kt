package com.example.domain.facades

import com.example.domain.models.UserModel

interface UserDaoFacade {
    suspend fun createUser(userName: String, password: String, email: String?): UserModel?
    suspend fun checkUser(userName: String, password: String, email: String?): UserModel?

    suspend fun getUser(userName: String, email: String?): UserModel?
}