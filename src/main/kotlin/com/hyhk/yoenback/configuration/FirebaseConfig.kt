package com.hyhk.yoenback.configuration

import com.google.auth.oauth2.GoogleCredentials
import com.google.firebase.FirebaseApp
import com.google.firebase.FirebaseOptions
import jakarta.annotation.PostConstruct
import org.springframework.context.annotation.Configuration
import org.springframework.core.env.Environment

@Configuration
class FirebaseConfig(
    private val env: Environment,
    ) {

    @PostConstruct
    fun init() {
        if (FirebaseApp.getApps().isEmpty()) {
            val resourcePath = env.getProperty("firebase.config.path")
            val storageBucket = env.getProperty("firebase.storage.bucket")
            println("Using $resourcePath")
            val serviceAccount = javaClass
                .classLoader
                .getResourceAsStream(resourcePath)

            val options = FirebaseOptions.builder()
                .setCredentials(GoogleCredentials.fromStream(serviceAccount))
                .setStorageBucket(storageBucket)
                .build()

            FirebaseApp.initializeApp(options)
        }
    }
}