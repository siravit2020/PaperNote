package com.example.testmvpkotlin

import android.app.Activity
import android.content.Intent
import android.content.res.ColorStateList
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.PopupMenu
import androidx.core.view.ViewCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.testmvpkotlin.addPaper.AddPaperActivity
import com.example.testmvpkotlin.databinding.GridViewLayoutItemsBinding
import com.example.testmvpkotlin.main.MainActivity
import com.google.android.material.snackbar.Snackbar


class NoteAdapter(private var arrayList: ArrayList<NoteItem>, var context: Activity) : RecyclerView.Adapter<NoteAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding: GridViewLayoutItemsBinding = DataBindingUtil.inflate(inflater,
            R.layout.grid_view_layout_items, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val charItem: NoteItem = arrayList[position]
        holder.binding.titleTextView.text = charItem.Topic
        holder.binding.message.text = charItem.message
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            ViewCompat.setBackgroundTintList(holder.binding.note, ColorStateList.valueOf(charItem.color));
        }
        holder.binding.note.setOnClickListener {
            val intent = Intent(context,AddPaperActivity::class.java)
            intent.putExtra("topic",charItem.Topic)
            intent.putExtra("message",charItem.message)
            intent.putExtra("number",charItem.number)
            context.startActivity(intent)
        }
        holder.binding.note.setOnLongClickListener{
            val m: MainActivity = (context as MainActivity)
            Snackbar.make(it, charItem.message.toString(), Snackbar.LENGTH_SHORT).show()
            val popup = if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                PopupMenu(context, holder.binding.titleTextView,Gravity.START)
            } else {
                PopupMenu(context, holder.binding.root)
            }
            popup.menuInflater.inflate(R.menu.menu_popup, popup.menu)
            popup.setOnMenuItemClickListener {it ->

                if(it.itemId == R.id.delete){
                    m.delete(arrayList[position],charItem.number,position)
                }
                if(it.itemId == R.id.color){
                    m.color(context,holder.binding.note,charItem.number)
                }
                true
            }
            popup.show()
            true
        }
    }


    override fun getItemCount(): Int {
        return arrayList.size
    }

    class ViewHolder(val binding: GridViewLayoutItemsBinding) :
        RecyclerView.ViewHolder(binding.root){

    }





}
