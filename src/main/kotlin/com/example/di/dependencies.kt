package com.example.di

import com.example.data.dao.UserDaoImpl
import com.example.domain.facades.UserDaoFacade
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val dependencies = module {
    singleOf(::UserDaoImpl) bind UserDaoFacade::class
}