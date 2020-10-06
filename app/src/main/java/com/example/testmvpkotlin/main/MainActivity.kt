package com.example.testmvpkotlin.main

import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.cardview.widget.CardView
import androidx.core.view.GravityCompat
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.testmvpkotlin.NoteAdapter
import com.example.testmvpkotlin.NoteItem
import com.example.testmvpkotlin.R
import com.example.testmvpkotlin.addPaper.AddPaperActivity
import com.example.testmvpkotlin.databinding.ActivityMainBinding
import com.example.testmvpkotlin.databinding.HeaderNavigationBinding
import com.example.testmvpkotlin.login.LoginActivity
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.hannesdorfmann.mosby3.mvp.MvpActivity


class MainActivity : MvpActivity<MainView, MainPresenter>(), MainView,View.OnClickListener,NoteAdapter.AdapterNote {
    private lateinit var b :ActivityMainBinding
    private var auth: FirebaseAuth = Firebase.auth
    lateinit var recyclerView: RecyclerView
    private lateinit var gridLayoutManager: StaggeredGridLayoutManager
    private lateinit var alphaAdapters: NoteAdapter
    private var arrayList: ArrayList<NoteItem> = ArrayList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        b = DataBindingUtil.setContentView(this, R.layout.activity_main)
        b.navigation.setNavigationItemSelectedListener {
            it.isChecked = true
            it.isCheckable = true
            when(it.itemId){
                R.id.logout -> {
                    auth.signOut()
                    startActivity(Intent(this, LoginActivity::class.java))
                    finish()
                }
            }
            true
        }


        val bi:HeaderNavigationBinding = HeaderNavigationBinding.inflate(layoutInflater)
        bi.emailHeader.text = auth.currentUser!!.email
        b.navigation.addHeaderView(bi.root)
        b.floatingActionButton.setOnClickListener(this)
        b.topAppBar.setNavigationOnClickListener { b.drawer.openDrawer(GravityCompat.START) }
        gridLayoutManager = StaggeredGridLayoutManager(2, LinearLayoutManager.VERTICAL)
        recyclerView = b.recyclerViewItem
        recyclerView.layoutManager = gridLayoutManager
        recyclerView.setHasFixedSize(true)
        presenter.getData()


    }

    override fun hideProgress() {
        b.progress.visibility = View.GONE
    }
    override fun onClick(v: View?) {
        if(v == b.floatingActionButton){
            startActivity(Intent(this, AddPaperActivity::class.java))
        }

    }


    override fun updateRecyclerView(arrayList: ArrayList<NoteItem>) {
        Log.d("check",arrayList.toString())
        this.arrayList = arrayList
        alphaAdapters = NoteAdapter(arrayList,this,this)
        recyclerView.adapter = alphaAdapters
        alphaAdapters.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
        hideProgress()
    }

    override fun createPresenter(): MainPresenter {
        return MainPresenter()
    }

   /* override fun color(context: Context, note: CardView, number: String) {
        ColorPickerDialogBuilder
                .with(context)
            .setTitle("์Note color")
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .lightnessSliderOnly()
            .setPositiveButton("ok") { _, selectedColor, _ ->
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    presenter.updateData(selectedColor, number)
                    ViewCompat.setBackgroundTintList(note, ColorStateList.valueOf(selectedColor))
                }
            }
            .setNegativeButton("cancel") { _, _ -> }
            .build()
            .show()
    }
    override fun delete(charItem: NoteItem, number: String, position: Int) {
        presenter.delete(charItem,number, position, arrayList)
    }*/

    override fun updateDelete(charItem: NoteItem) {
        arrayList.remove(charItem)
        alphaAdapters.notifyDataSetChanged()
        recyclerView.scheduleLayoutAnimation()
    }

    override fun delete(charItem: NoteItem) {
        presenter.delete(charItem,charItem.number)
    }

    override fun changeColor(note: CardView, number: String) {
        ColorPickerDialogBuilder
            .with(this)
            .setTitle("์Note color")
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .lightnessSliderOnly()
            .setPositiveButton("ok") { _, selectedColor, _ ->
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    presenter.updateData(selectedColor, number)
                    ViewCompat.setBackgroundTintList(note, ColorStateList.valueOf(selectedColor))
                }
            }
            .setNegativeButton("cancel") { _, _ -> }
            .build()
            .show()
    }


}