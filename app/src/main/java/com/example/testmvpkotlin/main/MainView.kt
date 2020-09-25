package com.example.testmvpkotlin.main

import android.content.Context
import androidx.cardview.widget.CardView
import com.example.testmvpkotlin.NoteItem
import com.hannesdorfmann.mosby3.mvp.MvpView

interface MainView : MvpView {
    fun updateRecyclerView(arrayList: ArrayList<NoteItem>)
    fun color(context: Context, note: CardView, number: String)
    fun hideProgress()
    fun updateDelete(charItem: NoteItem)
    fun delete(charItem: NoteItem, number: String, position: Int)
}