package com.example.ak7mt_projekt.ToDoList

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.CheckBox
import android.widget.ImageButton
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.ak7mt_projekt.R
import com.example.ak7mt_projekt.SecondFragment
import com.google.android.material.internal.ContextUtils.getActivity


class ToDoAdapter(context: Fragment, toDoList:MutableList<ToDoModel>): BaseAdapter() {
    private var mujcontext = context.context
    private var inflater: LayoutInflater = LayoutInflater.from(mujcontext)
    private var itemList=toDoList
    private var updateAndDelete: UpdateAndDelete = context as UpdateAndDelete

    override fun getView(p0: Int, p1: View?, p2: ViewGroup?): View {
        val UID: String = itemList[p0].UID as String
        //val itemTextData = itemList[p0].itemDataText as String
        val itemTextData = itemList[p0].itemDataText.toString()
        val done: Boolean = itemList[p0].done as Boolean

        val view: View
        val viewHolder: ListViewHolder

        if (p1 == null)
        {

            view = inflater.inflate(R.layout.row_itemslayout, p2, false)
            viewHolder = ListViewHolder(view)
            view.tag = viewHolder
        }
        else
        {
            view = p1
            viewHolder = view.tag as ListViewHolder
        }

        viewHolder.textLabel.text = itemTextData
        viewHolder.isDone.isChecked = done
        viewHolder.isDone.setOnClickListener {
            updateAndDelete.modifyItem(UID, !done)
        }

        viewHolder.isDeleted.setOnClickListener {
            updateAndDelete.onItemDelete(UID)
        }
        return view

    }

    private class ListViewHolder (row: View?){
        val textLabel: TextView = row!!.findViewById(R.id.item_textView) as TextView
        val isDone: CheckBox = row!!.findViewById(R.id.checkbox) as CheckBox
        val isDeleted: ImageButton = row!!.findViewById(R.id.close) as ImageButton
    }

    override fun getItem(p0: Int): Any {
        return itemList.get(p0)
    }

    override fun getItemId(p0: Int): Long {
        return p0.toLong()
    }

    override fun getCount(): Int {
        return itemList.size
    }

}