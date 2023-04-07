package com.example.plugins

import com.mitchellbosecke.pebble.loader.ClasspathLoader
import io.ktor.server.pebble.*
import io.ktor.server.application.*

fun Application.configureTemplating() {
    install(Pebble) {
        loader(ClasspathLoader().apply { prefix = "templates" })
    }
}
