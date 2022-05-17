package hu.bme.aut.eventes.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.eventes.R
import hu.bme.aut.eventes.data.Event
import hu.bme.aut.eventes.databinding.ItemEventBinding
import hu.bme.aut.eventes.databinding.ItemMyeventBinding

class MyListAdapter(private val myEvents: ArrayList<Event>) : RecyclerView.Adapter<MyListAdapter.MyListViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyListAdapter.MyListViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_myevent, parent, false)
        return MyListViewHolder(view)
    }

    override fun onBindViewHolder(holder: MyListViewHolder, position: Int) {
        val event : Event = myEvents[position]
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
            var db = FirebaseFirestore.getInstance()
            var idT = event.id ?: ""
            if(event.owner.equals(FirebaseAuth.getInstance().currentUser?.email.toString()))
            {
                db.collection("Events").document(idT).delete()

            }
            else if(event.beThere!!.contains(FirebaseAuth.getInstance().currentUser?.email.toString())){
                event.beThere?.remove(FirebaseAuth.getInstance().currentUser?.email.toString())
                val updEvent = db.collection("Events").document(idT)
                updEvent.update("beThere", event.beThere)
            }
        }

    }

    override fun getItemCount(): Int = myEvents.size

    fun addEvent(newEvent: Event) {
        myEvents.add(newEvent)
        notifyItemInserted(myEvents.size - 1)
    }

    fun removeEvent(position: Int) {
        myEvents.removeAt(position)
        notifyItemRemoved(position)
        if (position < myEvents.size) {
            notifyItemRangeChanged(position, myEvents.size - position)
        }
    }

    inner class MyListViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var binding = ItemMyeventBinding.bind(itemView)
        var name = binding.eventName
        var desc = binding.eventDesc
        var date = binding.eventDate
        var beThere = binding.eventBeThere
        var owner = binding.eventOwner
        var location = binding.eventLocation
        var subButton = binding.fab

    }
}

