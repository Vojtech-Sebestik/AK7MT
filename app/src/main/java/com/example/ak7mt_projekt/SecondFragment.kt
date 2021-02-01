package com.example.ak7mt_projekt

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.EditText
import android.widget.ListView
import android.widget.Toast
import com.example.ak7mt_projekt.ToDoList.ToDoAdapter
import com.example.ak7mt_projekt.ToDoList.ToDoModel
import com.example.ak7mt_projekt.ToDoList.UpdateAndDelete
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_home_2.*
import kotlinx.android.synthetic.main.fragment_second.view.*


class SecondFragment : Fragment(), UpdateAndDelete {


    private lateinit var database : DatabaseReference
    var toDOList: MutableList<ToDoModel>? = null
    lateinit var adapter: ToDoAdapter
    private var listViewItem : ListView? = null


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle? ): View? {
        val view: View = inflater.inflate(R.layout.fragment_second, container, false)
        val fab = view.fab as FloatingActionButton
        listViewItem = view.item_listview as ListView


        database = FirebaseDatabase.getInstance().reference

        fab.setOnClickListener {
            val alertDialog = AlertDialog.Builder(context)
            val textEditText = EditText(context)
            alertDialog.setMessage("Add ToDo item")
            alertDialog.setTitle("Enter ToDo item")
            alertDialog.setView(textEditText)
            alertDialog.setPositiveButton("Add"){dialog, i ->
                val todoItemData = ToDoModel.createList()
                todoItemData.itemDataText = textEditText.text.toString()
                todoItemData.done = false

                val newItemData = database.child("todo").push()
                todoItemData.UID = newItemData.key

                newItemData.setValue(todoItemData)

                dialog.dismiss()
                Toast.makeText(context, "item saved", Toast.LENGTH_LONG).show()

            }
            alertDialog.show()
        }

        toDOList = mutableListOf<ToDoModel>()
        adapter = ToDoAdapter(this, toDOList!!)
        listViewItem!!.adapter=adapter
        database.addValueEventListener(object: ValueEventListener{
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context,"No item Added", Toast.LENGTH_LONG).show()
            }

            override fun onDataChange(snapshot: DataSnapshot) {
                toDOList!!.clear()
                addItemToList(snapshot)
            }

        })
        return view

    }

    private fun addItemToList(snapshot: DataSnapshot) {
        val items = snapshot.children.iterator()

        if (items.hasNext())
        {
            val toDoIndexedValue = items.next()
            val itemsIterator = toDoIndexedValue.children.iterator()

            while(itemsIterator.hasNext())
            {
                val currentItem = itemsIterator.next()
                val toDoItemData = ToDoModel.createList()
                val map = currentItem.value as HashMap<String, Any>

                toDoItemData.UID = currentItem.key
                toDoItemData.done = map.get("done") as Boolean?
                toDoItemData.itemDataText = map["itemDataText"] as String?
                toDOList!!.add(toDoItemData)
            }
        }
        adapter.notifyDataSetChanged()
    }

    override fun modifyItem(itemUID: String, isDone: Boolean) {
        val itemReference = database.child("todo").child(itemUID)
        itemReference.child("done").setValue(isDone)
    }

    override fun onItemDelete(itemUID: String) {
        val itemReference = database.child("todo").child(itemUID)
        itemReference.removeValue()
        adapter.notifyDataSetChanged()
    }
}