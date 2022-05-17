package hu.bme.aut.eventes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.eventes.R
import hu.bme.aut.eventes.data.Event
import hu.bme.aut.eventes.databinding.ItemEventBinding

class MainListAdapter(private val events: ArrayList<Event>) : RecyclerView.Adapter<MainListAdapter.MyViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MainListAdapter.MyViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_event, parent, false)
        return MyViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event : Event  = events[position]
        holder.name.text = event.name
        var guests = ""
        var list : ArrayList<String>? = event.beThere
        if(list!= null)
        {
            var first = true
            for(be in list)
            {
                var add = ""
                if(!first)
                {
                    add += ", "
                }
                add += be
                first = false
                guests += add
            }
        }

        holder.beThere.text = guests
        holder.desc.text = event.desc
        holder.date.text = event.date
        holder.location.text = event.location
        holder.owner.text = event.owner

        holder.subButton.setOnClickListener{
            if(event.beThere == null)
            {
                event.beThere = ArrayList<String>()
            }
            if(!(event.owner.equals(FirebaseAuth.getInstance().currentUser?.email.toString()) || event.beThere!!.contains(FirebaseAuth.getInstance().currentUser?.email.toString())))
            {
                var db = FirebaseFirestore.getInstance()
                event.beThere?.add(FirebaseAuth.getInstance().currentUser?.email.toString())
                var idT = event.id?.toString() ?: ""
                val updEvent = db.collection("Events").document(idT)
                updEvent.update("beThere", event.beThere)

            }

        }

    }

    override fun getItemCount(): Int = events.size

    fun addEvent(newEvent: Event) {
        events.add(newEvent)
        notifyItemInserted(events.size - 1)
    }

    fun removeEvent(position: Int) {
        events.removeAt(position)
        notifyItemRemoved(position)
        if (position < events.size) {
            notifyItemRangeChanged(position, events.size - position)
        }
    }

    inner class MyViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding = ItemEventBinding.bind(itemView)
        var name = binding.eventName
        var desc = binding.eventDesc
        var date = binding.eventDate
        var beThere = binding.eventBeThere
        var owner = binding.eventOwner
        var location = binding.eventLocation
        var subButton = binding.fab

    }
}
