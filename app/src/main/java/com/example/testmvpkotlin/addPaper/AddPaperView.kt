package com.example.testmvpkotlin.addPaper

import com.hannesdorfmann.mosby3.mvp.MvpView

interface AddPaperView :MvpView{
    fun createColor()
    fun back()
}