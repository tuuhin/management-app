package com.example.data.dao

import com.example.data.entity.UserEntity
import com.example.domain.facades.UserDaoFacade
import com.example.domain.models.UserAuthModel
import com.example.domain.models.UserModel
import com.example.plugins.DataBaseFactory.dbQuery
import com.example.utils.Constants
import com.toxicbakery.bcrypt.Bcrypt
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import java.time.LocalDate

@OptIn(ExperimentalUnsignedTypes::class)
class UserDaoImpl : UserDaoFacade {
    private fun resultRowToUser(row: ResultRow): UserAuthModel = UserAuthModel(
        id = row[UserEntity.id],
        username = row[UserEntity.username],
        email = row[UserEntity.email],
        passwordHash = row[UserEntity.password].toUByteArray(),
        createdAt = row[UserEntity.createdAt]
    )

    override suspend fun createUser(userName: String, password: String, email: String?): UserModel? {
        val create = UserEntity.insert {
            it[UserEntity.email] = email
            it[UserEntity.username] = userName
            it[UserEntity.password] = Bcrypt.hash(password, Constants.SALT_ROUNDS)
            it[UserEntity.createdAt] = LocalDate.now()
        }
        return dbQuery { create.resultedValues?.singleOrNull()?.let(::resultRowToUser)?.toModel() }
    }

    override suspend fun checkUser(userName: String, password: String, email: String?): UserModel? {
        val userExists = dbQuery {
            UserEntity
                .select {
                    UserEntity.username eq userName
                    UserEntity.email eq email
                }
                .singleOrNull()
                ?.let(::resultRowToUser)
        } ?: return null
        val check = Bcrypt.verify(password, userExists.passwordHash.toByteArray())
        return if (!check) null else userExists.toModel()
    }

    override suspend fun getUser(userName: String, email: String?): UserModel? {
        val user = dbQuery {
            UserEntity.select {
                UserEntity.username eq userName
                UserEntity.email eq email
            }.singleOrNull()?.let(::resultRowToUser)
        }
        return user?.toModel()
    }
}