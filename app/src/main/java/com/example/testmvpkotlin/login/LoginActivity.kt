package com.example.testmvpkotlin.login

import android.app.Dialog
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.LinearLayout
import androidx.databinding.DataBindingUtil
import com.example.testmvpkotlin.main.MainActivity
import com.example.testmvpkotlin.R
import com.example.testmvpkotlin.databinding.ActivityLoginBinding
import com.example.testmvpkotlin.databinding.ForgetPasswordBinding
import com.example.testmvpkotlin.databinding.RegisterBinding
import com.google.android.material.snackbar.Snackbar
import com.hannesdorfmann.mosby3.mvp.MvpActivity
import com.mai.progressdialog.ProgressDialogMai


class LoginActivity : MvpActivity<LoginView, LoginPresenter>(),LoginView,View.OnClickListener{
    private lateinit var binding: ActivityLoginBinding
    private lateinit var bindingRegister: RegisterBinding
    private lateinit var bindingForgetPassword: ForgetPasswordBinding
    private lateinit var dialog: Dialog
    private lateinit var dialogRegister: Dialog
    private lateinit var dialogForgetPassword:Dialog
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        bindingRegister = RegisterBinding.inflate(layoutInflater)
        bindingForgetPassword = ForgetPasswordBinding.inflate(layoutInflater)
        createDialog()
        dialog = ProgressDialogMai(this).dialog()
        binding.submit.setOnClickListener(this)
        binding.toRegister.setOnClickListener(this)
        binding.resetPassword.setOnClickListener(this)
        bindingRegister.submit.setOnClickListener(this)
        bindingForgetPassword.submit.setOnClickListener(this)
        presenter.checkUser()

    }

    override fun hideFirstPaper() {
        val aniFade: Animation = AnimationUtils.loadAnimation(applicationContext, R.anim.fade_out)
        binding.firstPaper.startAnimation(aniFade)
        binding.firstPaper.visibility = View.GONE
    }
    override fun createDialog() {
        dialogRegister = Dialog(this)
        val width = (resources.displayMetrics.widthPixels * 0.90).toInt()
        dialogRegister.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogRegister.setContentView(bindingRegister.root)
        dialogRegister.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)

        dialogForgetPassword = Dialog(this)
        dialogForgetPassword.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialogForgetPassword.setContentView(bindingForgetPassword.root)
        dialogForgetPassword.window!!.setLayout(width, LinearLayout.LayoutParams.WRAP_CONTENT)
    }
    override fun createPresenter(): LoginPresenter {
        return LoginPresenter()
    }

    override fun showProgress() {
        dialog.show()
    }

    override fun hideProgress() {
        dialog.dismiss()
    }

    override fun disableForm() {
        binding.apply {
            username.isEnabled = false
            password.isEnabled = false
            submit.isEnabled = false
        }
    }

    override fun enableForm() {
        binding.apply {
            username.isEnabled = true
            password.isEnabled = true
            submit.isEnabled = true
        }
    }

    override fun showErrorMessage(message: String) {
        Snackbar.make(binding.linearLogin, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun start(email:String) {
        val intent = Intent(this, MainActivity::class.java)
        intent.putExtra("email",email)
        startActivity(intent)
        finish()
    }

    override fun showMessageRegister(message: String) {
        Snackbar.make(bindingRegister.submit, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showMessageForgetPassword(message: String) {
        Snackbar.make(binding.submit, message, Snackbar.LENGTH_SHORT).show()
    }

    override fun showRegister() {
        dialogRegister.show()
    }

    override fun hideRegister() {
        dialogRegister.dismiss()
    }

    override fun emailError() {
        bindingRegister.username.error = "Please enter your email"
    }

    override fun passwordError(s: String) {
        bindingRegister.password.error = s
    }

    override fun confirmPasswordError(message: String) {
        bindingRegister.confirmPassword.error = message
    }

    override fun hideForgetPassword() {
        dialogForgetPassword.dismiss()
    }

    override fun onClick(v: View?) {
        if (v == binding.submit)
        {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()
            presenter.doLogin(username, password, this)

        }
        if(v == binding.toRegister)
        {
            dialogRegister.show()
        }
        if(v == bindingRegister.submit){
            val username = bindingRegister.username.text.toString()
            val password = bindingRegister.password.text.toString()
            val confirmPassword = bindingRegister.confirmPassword.text.toString()
            presenter.register(username, password, confirmPassword, this)
        }
        if(v == binding.resetPassword){
            dialogForgetPassword.show()
        }
        if(v == bindingForgetPassword.submit){
            presenter.forgetPassword(bindingForgetPassword.emailForget.text.toString())
        }
    }
}