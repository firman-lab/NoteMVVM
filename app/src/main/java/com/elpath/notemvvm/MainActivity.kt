package com.elpath.notemvvm

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elpath.notemvvm.Model.SubscriberViewModel
import com.elpath.notemvvm.Model.SubscriberViewModelFactory
import com.elpath.notemvvm.databinding.ActivityMainBinding
import com.elpath.notemvvm.Room.db.Subscriber
import com.elpath.notemvvm.Room.db.SubscriberDatabase
import com.elpath.notemvvm.Room.db.SubscriberRepository

class MainActivity : AppCompatActivity() {
    private lateinit var binding : ActivityMainBinding
    private lateinit var subscriberViewModel: SubscriberViewModel
    private lateinit var adapter : SubscriberAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)

        val dao = SubscriberDatabase.getInstance(application).subscriberDAO
        val repository = SubscriberRepository(dao)
        val factory = SubscriberViewModelFactory(repository)
        subscriberViewModel = ViewModelProvider(this, factory).get(SubscriberViewModel::class.java)
        binding.myViewModel = subscriberViewModel
        binding.lifecycleOwner = this
        initRecyclerView()

        //Event Handler
        subscriberViewModel.message.observe(this, Observer {
            it.getContentIfNotHandled()?.let {
                Toast.makeText(this, it, Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun initRecyclerView(){
        binding.rvSubscribers.layoutManager =LinearLayoutManager(this)
        adapter = SubscriberAdapter { selectedItem: Subscriber ->
            listItemClicked(selectedItem)}
        binding.rvSubscribers.adapter = adapter
        displaySubsList()

    }

    private fun displaySubsList(){
        subscriberViewModel.subscriber.observe(this, Observer {
            Log.i("MyTAG", it.toString())
            adapter.setList(it)
            adapter.notifyDataSetChanged()
        })
    }
    private fun listItemClicked(subscriber: Subscriber){
        //Toast.makeText(this, "Its me, ${subscriber.name}!", Toast.LENGTH_SHORT).show()
        subscriberViewModel.initUpdateAndDelete(subscriber)
    }

}