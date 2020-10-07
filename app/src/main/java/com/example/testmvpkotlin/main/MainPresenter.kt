package com.example.testmvpkotlin.main


import android.util.Log
import com.example.testmvpkotlin.NoteItem
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import com.hannesdorfmann.mosby3.mvp.MvpBasePresenter


class MainPresenter: MvpBasePresenter<MainView>(){
    fun getData(){
        var arrayList: ArrayList<NoteItem> = ArrayList()
        val db = Firebase.firestore
        db.collection(FirebaseAuth.getInstance().uid.toString())
            .orderBy("order", Query.Direction.DESCENDING).get().addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    arrayList = ArrayList()
                    for (document in task.result!!) {
                        arrayList.add(
                            NoteItem(document.id,
                                document["topic"].toString(),
                                document["message"].toString(),
                                document["colorPaper"].toString().toInt()
                            )
                        )
                    }
                    returnData(arrayList)
                }
            }

    }
    private fun returnData(arrayList: ArrayList<NoteItem>) {
        Log.d("dfg","ก็ได้หนิ")
        ifViewAttached {
                view -> view.updateRecyclerView(arrayList)
            Log.d("dfg","ก็ได้หนิ2")}
    }

    fun updateData(selectedColor:Int,number:String){
        val map = HashMap<String, Any>()
        map["colorPaper"] = selectedColor
        val db = Firebase.firestore
        db.collection(FirebaseAuth.getInstance().uid.toString()).document(number).update(map)
    }
    fun delete(charItem: NoteItem, number: String) {
       // Log.d("sss",""+ position + " " + arrayList.size)
        val db = Firebase.firestore
        db.collection(FirebaseAuth.getInstance().uid.toString()).document(number).delete().addOnSuccessListener {
           ifViewAttached { view-> view.updateDelete(charItem) }
        }
    }

}
