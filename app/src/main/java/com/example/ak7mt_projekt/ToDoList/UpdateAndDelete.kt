package com.example.ak7mt_projekt.ToDoList

interface UpdateAndDelete {
    fun modifyItem(itemUID : String, isDone : Boolean)
    fun onItemDelete(itemUID: String)
}