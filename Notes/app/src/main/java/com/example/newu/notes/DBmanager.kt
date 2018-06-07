package com.example.newu.notes

import android.app.DownloadManager
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteQueryBuilder
import android.provider.ContactsContract
import android.widget.Toast
import kotlin.reflect.KTypeProjection

class DBmanager{
    val DBname="Mynotes"
    val DBtable="Notes"
    val colID="ID"
    val colTitle="Title"
    val colDes = "Description"
    val dbVersion =1
    val sqlcreatetable = "create table if not exists "+DBtable+" ("+colID+" integer primary key , "+colTitle+" text ,"+colDes+" text);"
    var sqlDB :SQLiteDatabase?=null
    constructor(context:Context){
        var db = DatabaseHelperNotes(context)
        sqlDB= db.writableDatabase
    }


    inner class DatabaseHelperNotes:SQLiteOpenHelper{
        var context:Context?=null

        constructor(context:Context):super(context,DBname,null,dbVersion){
            this.context=context
        }
        override fun onCreate(db: SQLiteDatabase?) {
            db!!.execSQL(sqlcreatetable)
            Toast.makeText(this.context,"database created",Toast.LENGTH_SHORT ).show()
        }

        override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
            db!!.execSQL("drop table "+DBtable)
        }


    }

    fun Insert(values: ContentValues):Long{
        val id = sqlDB!!.insert(DBtable,"",values)
        return id
    }

    fun query(projection:Array<String>,selection:String,selectionargs:Array<String>,sortOrder:String):Cursor{
        val qb = SQLiteQueryBuilder()
        qb.tables=DBtable
        val cursor = qb.query(sqlDB,projection,selection,selectionargs,null,null,sortOrder)
        return cursor
    }

    fun delete(selection:String,selectionargs:Array<String>):Int{
        val count = sqlDB!!.delete(DBtable,selection,selectionargs)
        return count
    }
    fun update(values:ContentValues,selection:String,selectionargs: Array<String>):Int{
        val count=sqlDB!!.update(DBtable,values,selection,selectionargs)
        return count
    }
}