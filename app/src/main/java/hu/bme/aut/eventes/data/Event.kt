package hu.bme.aut.eventes.data

import com.google.firebase.auth.FirebaseUser

data class Event(var id: String ?= null, var owner: String ?= null,var name: String ?= null,var date: String ?= null,var desc: String ?= null, var location: String ?= null,var beThere: ArrayList<String> ?= null){

}