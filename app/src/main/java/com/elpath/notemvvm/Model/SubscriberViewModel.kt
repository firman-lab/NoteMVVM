package com.elpath.notemvvm.Model

import androidx.databinding.Bindable
import androidx.databinding.Observable
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.elpath.notemvvm.Event
import com.elpath.notemvvm.Room.db.Subscriber
import com.elpath.notemvvm.Room.db.SubscriberRepository
import kotlinx.coroutines.launch

class SubscriberViewModel(private val repository: SubscriberRepository) : ViewModel(), Observable{

    val subscriber = repository.subscribers
    private var isUpdateOrDelete = false
    private lateinit var subscriberToUpdateOrDelete : Subscriber

    @Bindable
    val inputNama = MutableLiveData<String>()
    @Bindable
    val inputEmail = MutableLiveData<String>()
    @Bindable
    val btnSaveOrUpdate = MutableLiveData<String>()
    @Bindable
    val btnClearAllOrDell = MutableLiveData<String>()

    //Event handler
    private val statusMessage = MutableLiveData<Event<String>>()
    val message : LiveData<Event<String>>
            get() = statusMessage


    init {
        btnSaveOrUpdate.value = "Save"
        btnClearAllOrDell.value = "Clear All"
    }

    fun saveOrUpdate(){
        if (isUpdateOrDelete){
            subscriberToUpdateOrDelete.name = inputNama.value!!
            subscriberToUpdateOrDelete.email = inputEmail.value!!
            update(subscriberToUpdateOrDelete)
        }else {
            val name: String = inputNama.value!!
            val email: String = inputEmail.value!!
            insert(Subscriber(0, name, email))
            inputNama.value = null
            inputEmail.value = null
        }
    }
    fun clearOrDell(){
        if (isUpdateOrDelete){
            delete(subscriberToUpdateOrDelete)
        }else{
            clearAll()
        }

    }

    fun insert(subscriber: Subscriber) = viewModelScope.launch {
            val newRowId = repository.insert(subscriber)
            if(newRowId >-1){
                statusMessage.value = Event("Subscriber Inserted Successfully $newRowId")
            }else{
                statusMessage.value = Event("Error Occured")
            }

        }

    fun update(subscriber: Subscriber) = viewModelScope.launch {
        val noOfRow = repository.update(subscriber)
        if (noOfRow > 0){
            inputNama.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            btnSaveOrUpdate.value = "Save"
            btnClearAllOrDell.value = "Clear All"
            statusMessage.value = Event("$noOfRow Subscriber Updated Successfully")
        }else{
            statusMessage.value = Event("Error Occured!")
        }

    }

    fun delete(subscriber: Subscriber) = viewModelScope.launch {
       val noOfRow = repository.delete(subscriber)

        if (noOfRow > 0) {
            repository.delete(subscriber)
            inputNama.value = null
            inputEmail.value = null
            isUpdateOrDelete = false
            btnSaveOrUpdate.value = "Save"
            btnClearAllOrDell.value = "Clear All"
            statusMessage.value = Event("$noOfRow Subscriber Deleted Successfully")
        }else{
            statusMessage.value = Event("Error Occured!")
        }
    }

    fun clearAll() = viewModelScope.launch {
        val noOfRow = repository.deleteAll()
        if (noOfRow>0){
            statusMessage.value = Event("$noOfRow Subscribers Clear!")
        }else{
            statusMessage.value = Event("Error Occured")
        }

    }

    fun initUpdateAndDelete(subscriber: Subscriber){
        inputNama.value = subscriber.name
        inputEmail.value = subscriber.email
        isUpdateOrDelete = true
        subscriberToUpdateOrDelete = subscriber
        btnSaveOrUpdate.value = "Update"
        btnClearAllOrDell.value = "Delete"

    }



    override fun addOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

    override fun removeOnPropertyChangedCallback(callback: Observable.OnPropertyChangedCallback?) {

    }

}