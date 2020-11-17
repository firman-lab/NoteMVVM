package com.elpath.notemvvm

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.elpath.notemvvm.databinding.CardSubscriberBinding
import com.elpath.notemvvm.Room.db.Subscriber

class SubscriberAdapter (private val clickListener:(Subscriber)->Unit)
    : RecyclerView.Adapter<MyViewHolder>()
{
    private val subscriberList = ArrayList<Subscriber>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding : CardSubscriberBinding =
            DataBindingUtil.inflate(layoutInflater, R.layout.card_subscriber, parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.bind(subscriberList[position],clickListener)
    }
    fun setList(subscribers: List<Subscriber>){
        subscriberList.clear()
        subscriberList.addAll(subscribers)
    }

    override fun getItemCount(): Int {
        return subscriberList.size
    }
}
class MyViewHolder(val binding: CardSubscriberBinding): RecyclerView.ViewHolder(binding.root){

    fun bind(subscriber: Subscriber, clickListener:(Subscriber)->Unit){
        binding.txtNama.text = subscriber.name
        binding.txtEmail.text = subscriber.email
        binding.listItem.setOnClickListener {
            clickListener(subscriber)
        }
    }
}