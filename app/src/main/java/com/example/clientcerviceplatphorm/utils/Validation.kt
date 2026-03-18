package com.example.clientcerviceplatphorm.utils

class Validation {
    fun valideemail(email: String):String?{
        val regex = "^[A-Za-z0-9+-._]+@[A-Za-z0-9+-.]+$".toRegex()
        if (regex.matches(email)){
            return email
        }else{
            return null
        }
    }

    fun validpass(pass: String): String?{
        val regex = "^(?=.*[0-9]).{8,}\$".toRegex()
        if (regex.matches(pass)){
            return pass
        }else{
            return null
        }
    }
}