package hu.bme.aut.eventes.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import hu.bme.aut.eventes.adapter.MyListAdapter
import hu.bme.aut.eventes.data.Event
import hu.bme.aut.eventes.databinding.MyeventFragmentBinding

class MyEventFragment : Fragment() {
    private lateinit var binding: MyeventFragmentBinding
    private lateinit var ownEvents: ArrayList<Event>
    private lateinit var wantedEvents: ArrayList<Event>
    private lateinit var adapter: MyListAdapter
    private lateinit var wAdapter: MyListAdapter


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = MyeventFragmentBinding.inflate(inflater, container, false)


        binding.OwnEventList.layoutManager = LinearLayoutManager(requireActivity())
        ownEvents = arrayListOf()
        adapter = MyListAdapter(ownEvents)
        binding.OwnEventList.adapter = adapter
        var db = FirebaseFirestore.getInstance()
        val myEvents = db.collection("Events")
        myEvents.whereEqualTo("owner", FirebaseAuth.getInstance().currentUser?.email.toString()).addSnapshotListener(object:
            EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ) {
                if (error != null) {
                    Log.e("Firestore Error", error.message.toString())
                    return
                }
                for (dc: DocumentChange in value?.documentChanges!!) {
                    if (dc.type == DocumentChange.Type.ADDED) {
                       ownEvents.add(dc.document.toObject(Event::class.java))
                    }
                    if(dc.type == DocumentChange.Type.REMOVED)
                    {
                        ownEvents.remove(dc.document.toObject(Event::class.java))
                    }
                    if(dc.type == DocumentChange.Type.MODIFIED){
                        var d = dc.document.toObject(Event::class.java)!!
                        var ind =  ownEvents.indexOfFirst{it.id == d.id}
                        ownEvents.set(ind,d)
                    }


                }
                adapter.notifyDataSetChanged()

            }
        })

        binding.IBeThereEventList.layoutManager = LinearLayoutManager(requireActivity())
        wantedEvents = arrayListOf()
        wAdapter = MyListAdapter(wantedEvents)
        binding.IBeThereEventList.adapter = wAdapter
        myEvents.whereArrayContains("beThere",FirebaseAuth.getInstance().currentUser?.email.toString()).addSnapshotListener(object:
            EventListener<QuerySnapshot> {
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ){
                if(error != null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }
                for(dc : DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.REMOVED){
                        var d = dc.document.toObject(Event::class.java)!!
                        wantedEvents.removeAll{ it.id  == d.id }

                    }
                    if(dc.type ==DocumentChange.Type.ADDED){
                        wantedEvents.add(dc.document.toObject(Event::class.java))
                    }

                    if(dc.type == DocumentChange.Type.MODIFIED){
                        var d = dc.document.toObject(Event::class.java)!!
                        var ind = wantedEvents.indexOfFirst{it.id == d.id}
                        wantedEvents.set(ind,d)
                    }



                }
                wAdapter.notifyDataSetChanged()

            }
        })

        return binding.root

    }


}