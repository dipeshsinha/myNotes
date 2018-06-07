package com.example.newu.notes

class notes{

    var noteID:Int?=null
    var name:String?=null
    var des:String?=null
    constructor(note:Int,name:String,des:String){
        this.name = name
        this.noteID = note
        this.des = des
    }
}