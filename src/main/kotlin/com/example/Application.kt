package com.example

import com.example.di.dependencies
import io.ktor.server.application.*
import com.example.plugins.*
import org.koin.core.context.startKoin

fun main(args: Array<String>): Unit = io.ktor.server.netty.EngineMain.main(args)

fun Application.module() {
    startKoin{ modules(dependencies) }
    DataBaseFactory.init()
    configureTemplating()
    configureMonitoring()
    configureSecurity()
    configureRouting()
}
