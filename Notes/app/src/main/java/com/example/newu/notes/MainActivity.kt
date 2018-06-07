package com.example.newu.notes

import android.app.SearchManager
import android.content.Context
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract
import android.view.*
import android.widget.BaseAdapter
import android.widget.SearchView
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.ticket.view.*

class MainActivity : AppCompatActivity() {
    var listNotes = ArrayList<notes>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        LoadQuery("%")
    }

    override fun onResume() {
        super.onResume()
        LoadQuery("%")
    }

    fun LoadQuery(title:String){

        val dbmanager = DBmanager(this)
        val projections = arrayOf("ID","Title","Description")
        val selectionargs = arrayOf(title)
        val cursor = dbmanager.query(projections,"Title like ?",selectionargs,"Title")
        listNotes.clear()
        if(cursor.moveToFirst()){
            do{
                val id = cursor.getInt(cursor.getColumnIndex("ID"))
                val Title = cursor.getString(cursor.getColumnIndex("Title"))
                val description = cursor.getString(cursor.getColumnIndex("Description"))
                listNotes.add(notes(id,Title,description))
            }while (cursor.moveToNext())
        }
        val myadapter = mynotesadapter(this , listNotes)
        lvnotes.adapter=myadapter
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu,menu)

        val sv = menu!!.findItem(R.id.app_bar_search).actionView as SearchView
        val sm = getSystemService(Context.SEARCH_SERVICE) as SearchManager
        sv.setSearchableInfo(sm.getSearchableInfo(componentName))
        sv.setOnQueryTextListener(object:SearchView.OnQueryTextListener{
            override fun onQueryTextSubmit(query: String?): Boolean {
                Toast.makeText(applicationContext,query,Toast.LENGTH_SHORT).show()
                LoadQuery("%"+query+"%")
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {
        when(item!!.itemId){
            R.id.addnote -> {
                val intent = Intent(this , addnote::class.java)
                startActivity(intent)
            }
        }
        return super.onOptionsItemSelected(item)
    }

    inner class mynotesadapter:BaseAdapter{
        var listofnotes=ArrayList<notes>()
        var context:Context?=null
        constructor(context:Context ,mylistnotes:ArrayList<notes>):super(){
            this.listofnotes=mylistnotes
            this.context=context
        }

        override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
            val myview = layoutInflater.inflate(R.layout.ticket,null)
            val mynote = listofnotes[position]
            myview.title.text=mynote.name
            myview.content.text=mynote.des
            myview.deletebu.setOnClickListener(View.OnClickListener {
                var dBmanager=DBmanager(context!!)
                val selectionargs= arrayOf(mynote.noteID.toString())
                dBmanager.delete("ID=?",selectionargs)
                LoadQuery("%")
            })
            myview.editbu.setOnClickListener(View.OnClickListener {
                gotoupdate(mynote)
            })
            return myview


        }

        override fun getItem(position: Int): Any {
            return listofnotes[position]
        }

        override fun getItemId(position: Int): Long {
            return position.toLong()
        }

        override fun getCount(): Int {
            return listofnotes.size
        }
    }
    fun gotoupdate(note:notes){
        var intent = Intent(this,addnote::class.java)
        intent.putExtra("ID",note.noteID)
        intent.putExtra("name",note.name)
        intent.putExtra("des",note.des)
        startActivity(intent)
    }
}
