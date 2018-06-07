package com.example.newu.notes

import android.content.ContentValues
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_addnote.*

class addnote : AppCompatActivity() {

    var id = 0
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_addnote)

        try {
            var bundle :Bundle=intent.extras
            id = bundle.getInt("ID")
            notetitle.setText(bundle.getString("name").toString())
            notedes.setText(bundle.getString("des").toString())
        }catch (ex:Exception){}
    }

    fun finishact(view:View){
        var dBmanager=DBmanager(this)
        val values = ContentValues()
        values.put("title",notetitle.text.toString())
        values.put("description",notedes.text.toString())

        if(id==0) {
            val id = dBmanager.Insert(values)
            if (id > 0) {
                Toast.makeText(this, "note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "cannot add note", Toast.LENGTH_SHORT).show()
            }
        }
        else{
            val selectionargs = arrayOf(id.toString())
            val id = dBmanager.update(values,"ID=?",selectionargs)
            if (id > 0) {
                Toast.makeText(this, "note is added", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, "cannot add note", Toast.LENGTH_SHORT).show()
            }
        }
    }
}
