package com.example.testmvpkotlin.addPaper

import android.content.Intent
import android.graphics.Bitmap
import android.util.Log
import com.google.firebase.Timestamp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter


class AddPaperPresenter:MvpBasePresenter<AddPaperView>() {

    fun sendDataToFirebase(topic: String, message: String, selectedColor: Int) {
        val db = Firebase.firestore
        var count = 0
        db.collection(FirebaseAuth.getInstance().uid.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {
                for (document in task.result!!) {
                    count++
                }
                val map = HashMap<String, Any>()
                map["topic"] = topic
                map["message"] = message
                map["colorPaper"] = selectedColor
                map["order"] = FieldValue.serverTimestamp()
                db.collection(FirebaseAuth.getInstance().uid.toString()).add(map)
                    .run { ifViewAttached { view -> view.back() }}
            }
        }

    }
    fun updateDataToFirebase(topic: String, message: String, selectedColor: Int,number:String) {
        val db = Firebase.firestore
        db.collection(FirebaseAuth.getInstance().uid.toString()).get().addOnCompleteListener { task ->
            if (task.isSuccessful) {

                val map = HashMap<String, Any>()
                map["topic"] = topic
                map["message"] = message
                map["colorPaper"] = selectedColor
                map["order"] = FieldValue.serverTimestamp()
                db.collection(FirebaseAuth.getInstance().uid.toString()).document(number).update(map)
                    .run { ifViewAttached { view -> view.back() }}
            }
        }

    }
    fun imageToText(data: Intent){
        val imageBitmap = data.extras!!.get("data") as Bitmap
        val image = InputImage.fromBitmap(imageBitmap, 0)
        val recognizer = TextRecognition.getClient()
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                // Task completed successfully
                // ...
                val resultText = visionText.text
                ifViewAttached {
                    it.imageToText(resultText)
                }
            }
            .addOnFailureListener { e ->
                // Task failed with an exception
                // ...
            }
    }

}