package com.example.testmvpkotlin.main

import com.example.testmvpkotlin.NoteItem
import com.hannesdorfmann.mosby3.mvp.MvpView

interface MainView : MvpView {
    fun updateRecyclerView(arrayList: ArrayList<NoteItem>)
    fun hideProgress()
    fun updateDelete(charItem: NoteItem)
}