package com.example.testmvpkotlin

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.cardview.widget.CardView
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testmvpkotlin.addPaper.AddPaperActivity
import com.example.testmvpkotlin.databinding.GridViewLayoutItemsBinding
import com.example.testmvpkotlin.main.MainActivity
import com.google.android.material.snackbar.Snackbar


class NoteAdapter(private var arrayList: ArrayList<NoteItem>, var context: Activity,private val adap:AdapterNote) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: GridViewLayoutItemsBinding = DataBindingUtil.inflate(inflater,
            R.layout.grid_view_layout_items, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.colorOrDelete(adap,context,arrayList[position])

    }


    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(val binding: GridViewLayoutItemsBinding) :
        RecyclerView.ViewHolder(binding.root){
        fun colorOrDelete(action:AdapterNote, context:Context, item: NoteItem){
            binding.titleTextView.text = item.Topic
            binding.message.text = item.message
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
                ViewCompat.setBackgroundTintList(binding.note, ColorStateList.valueOf(item.color));
            }
            binding.note.setOnLongClickListener {
                val popup = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    PopupMenu(context, binding.titleTextView,Gravity.START)
                } else {
                    PopupMenu(context, binding.root)
                }
                popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)
                popup.setOnMenuItemClickListener {it ->

                    if(it.itemId == R.id.delete){
                        action.delete(item)
                    }
                    if(it.itemId == R.id.color){
                        action.changeColor(binding.note,item.number)

                    }
                    true
                }
                popup.show()
                true
            }


            binding.note.setOnClickListener {
                val intent = Intent(context,AddPaperActivity::class.java)
                intent.putExtra("topic",item.Topic)
                intent.putExtra("message",item.message)
                intent.putExtra("number",item.number)
                context.startActivity(intent)
            }
        }
    }

    interface AdapterNote{
        fun delete(charItem: NoteItem)
        fun changeColor(note: CardView, number: String)
    }





}
