package com.example.testmvpkotlin.addPaper

import android.content.Intent
import android.content.res.ColorStateList
import android.os.Bundle
import android.text.Editable
import android.util.Log
import androidx.appcompat.app.AlertDialog
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import com.example.testmvpkotlin.R
import com.example.testmvpkotlin.databinding.ActivityAddPaperBinding
import com.example.testmvpkotlin.main.MainActivity
import com.flask.colorpicker.ColorPickerView
import com.flask.colorpicker.builder.ColorPickerDialogBuilder
import com.hannesdorfmann.mosby3.mvp.MvpActivity

class AddPaperActivity : MvpActivity<AddPaperView, AddPaperPresenter>(), AddPaperView{
    private lateinit var binding:ActivityAddPaperBinding
    private lateinit var color:AlertDialog
    private var selectedColor = -1
    private var status = false
    var topic=""
    var message =""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_paper)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_add_paper)
        createColor()
        if(intent.hasExtra("topic")){
            topic = intent.getStringExtra("topic")!!
            message = intent.getStringExtra("message")!!
            binding.header.setText(topic)
            binding.message.setText(message)
            status = true
        }
        binding.topAppBar.apply {
            setOnMenuItemClickListener {
                if (it.itemId == R.id.save) {
                    if(status)
                        presenter.updateDataToFirebase(topic,message, selectedColor,intent.getStringExtra("number")!!)
                    else
                        presenter.sendDataToFirebase(binding.header.text.toString(),binding.message.text.toString(), selectedColor)

                } else if (it.itemId == R.id.logout) {
                    color.show()
                }
                true
            }
            setNavigationOnClickListener { onBackPressed() }
            setBackgroundColor(selectedColor)

        }
    }

    override fun createPresenter(): AddPaperPresenter {
        return AddPaperPresenter()
    }

    override fun back() {
        val intent = Intent(this, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TOP
        startActivity(intent)
    }

    override fun createColor() {
        color = ColorPickerDialogBuilder
            .with(this)
            .setTitle("์Note color")
            .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
            .lightnessSliderOnly()
            .setPositiveButton("ok") { _, selectedColor, _ ->
                this.selectedColor = selectedColor
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                    ViewCompat.setBackgroundTintList(
                        binding.layout, ColorStateList.valueOf(
                            selectedColor
                        )
                    )
                    binding.topAppBar.setBackgroundColor(selectedColor)
                    Log.d("setColor", selectedColor.toString())
                }

            }
            .setNegativeButton("cancel") { _, _ -> }
            .build()

    }



}