package com.example.testmvpkotlin.login

import android.app.Activity
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter


class LoginPresenter : MvpBasePresenter<LoginView>() {
    private var auth: FirebaseAuth = Firebase.auth
    private var check: FirebaseAuth.AuthStateListener
    private val db = Firebase.firestore
    init {
        check = FirebaseAuth.AuthStateListener {
            val user = FirebaseAuth.getInstance().currentUser
            if (user != null) {
                ifViewAttached { view ->
                    view.start(auth.uid!!)
                    Log.d("ert",auth.uid + " success")
                }
            }
            else{
                ifViewAttached { view ->view.hideFirstPaper()
                    Log.d("ert","3 ")}
            }
        }
    }
    fun checkUser(){
        auth.addAuthStateListener(check)
    }
    fun doLogin(email : String, password : String, context: Activity)
    {
        ifViewAttached { view ->
            if(email.trim().isEmpty())
            {
                view.showErrorMessage("Please enter your username")
                return@ifViewAttached
            }
            else if(password.trim().isEmpty())
            {
                view.showErrorMessage("Please enter your password")
                return@ifViewAttached
            }
            view.disableForm()
            view.showProgress()
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(context) { task ->
                    if (!task.isSuccessful) {
                        Log.w("TAG", "signInWithEmail:failure", task.exception)
                        view.showErrorMessage("Please enter a valid email and password.")
                        view.enableForm()
                        view.hideProgress()
                        return@addOnCompleteListener
                    }
                }
        }
    }
    fun register(email : String, password : String,confirmPassword : String, context: Activity)
    {

        ifViewAttached {view ->
            view.showRegister()
            when {
                email.trim().isEmpty() -> {
                    view.emailError()
                    return@ifViewAttached
                }
                password.trim().isEmpty() -> {
                    view.passwordError("Please enter your password")
                    return@ifViewAttached
                }
                password.trim().length < 8 -> {
                    view.passwordError("Please enter a password of at least 8 characters.")
                    return@ifViewAttached
                }
                confirmPassword.trim().isEmpty() -> {
                    view.confirmPasswordError("Please enter confirm password.")
                    return@ifViewAttached
                }
                confirmPassword.trim().length < 8-> {
                    view.confirmPasswordError("Please enter a confirm password of at least 8 characters or numbers.")
                    return@ifViewAttached
                }
                password != confirmPassword -> {
                    view.confirmPasswordError("Passwords do not match.")
                    return@ifViewAttached
                }
                else -> auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(context) { task ->
                        if (task.isSuccessful) {
                            Log.d("TAG", "createUserWithEmail:success")
                            view.hideRegister()
                            view.showErrorMessage("Create user success.")

                        } else {
                            Log.w("TAG", "createUserWithEmail:failure", task.exception)
                            view.hideRegister()
                            view.showErrorMessage("This email has already been registered.")
                        }

                    }
            }
        }
    }
    fun forgetPassword(email:String){
        Firebase.auth.sendPasswordResetEmail(email)
            .addOnSuccessListener {
                Log.d("ddd", "Email sent.")
                ifViewAttached {
                    it.hideForgetPassword()
                    it.showMessageForgetPassword("Success, please check your email.")
                }
            }
            .addOnFailureListener {
                ifViewAttached {
                    it.hideForgetPassword()
                    it.showMessageForgetPassword("The email is invalid or not registered.")
                }
            }

    }
}