package com.example.testmvpkotlin.login

import com.hannesdorfmann.mosby3.mvp.MvpView

interface LoginView  : MvpView{
    fun showProgress()
    fun hideProgress()
    fun disableForm()
    fun enableForm()
    fun showErrorMessage(message : String)
    fun showMessageRegister(message: String)
    fun showMessageForgetPassword(message: String)
    fun start(email:String)
    fun showRegister()
    fun hideRegister()
    fun emailError()
    fun passwordError(s: String)
    fun confirmPasswordError(message: String)
    fun createDialog()
    fun hideFirstPaper()
    fun hideForgetPassword()
}