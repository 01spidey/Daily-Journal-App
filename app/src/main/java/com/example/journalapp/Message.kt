package com.example.journalapp

class Message(private var message: String, private var sentBy: String) {

    val SENT_BY_ME:String = "me"
    val SENT_BY_BOT:String = "bot"


    fun getMessage():String{
        return message;
    }

    fun setMessage(message:String){
        this.message = message
    }

    fun getSentBy():String{
        return sentBy;
    }

    fun setSentBy(sentBy:String){
        this.sentBy = sentBy
    }

}