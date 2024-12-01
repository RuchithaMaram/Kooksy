package com.teamfour.kooksy

import android.app.Application
import com.google.firebase.FirebaseApp
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.ktx.Firebase
import com.google.firebase.firestore.ktx.firestore

class MyApp : Application() {

    override fun onCreate() {
        super.onCreate()
        Firebase.firestore
        FirebaseApp.initializeApp(this)
    }

    companion object {
        val db: FirebaseFirestore by lazy {
            // Create and return the Firestore instance
            Firebase.firestore.apply {
                // Set any Firestore settings here if necessary
            }
        }
    }
}
