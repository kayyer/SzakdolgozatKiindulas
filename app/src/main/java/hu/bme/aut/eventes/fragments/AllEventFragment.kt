package hu.bme.aut.eventes.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.constraintlayout.motion.widget.OnSwipe
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.firebase.ui.firestore.FirestoreRecyclerOptions
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.*
import com.google.firebase.ktx.Firebase
import hu.bme.aut.eventes.adapter.MainListAdapter
import hu.bme.aut.eventes.data.Event
import hu.bme.aut.eventes.databinding.AlleventFragmentBinding

class AllEventFragment : Fragment(){
        private lateinit var binding: AlleventFragmentBinding
        private lateinit var eventArrayList: ArrayList<Event>
        private lateinit var adapter: MainListAdapter
        private lateinit var db: FirebaseFirestore


        override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
            binding = AlleventFragmentBinding.inflate(inflater, container, false)
            initRecyclerView()
            EventChangeListener()


            return binding.root
        }

    private fun initRecyclerView(){
        binding.AllEventList.layoutManager = LinearLayoutManager(requireActivity())
        eventArrayList = arrayListOf()
        adapter = MainListAdapter(eventArrayList)
        binding.AllEventList.adapter = adapter
    }

    private fun EventChangeListener(){
        db = FirebaseFirestore.getInstance()
        db.collection("Events").addSnapshotListener(object: EventListener<QuerySnapshot>{
            override fun onEvent(
                value: QuerySnapshot?,
                error: FirebaseFirestoreException?
            ){
                if(error != null){
                    Log.e("Firestore Error",error.message.toString())
                    return
                }
                for(dc : DocumentChange in value?.documentChanges!!){
                    if(dc.type == DocumentChange.Type.ADDED){
                        eventArrayList.add(dc.document.toObject(Event::class.java))
                    }
                    if(dc.type == DocumentChange.Type.REMOVED){
                        var d = dc.document.toObject(Event::class.java)!!
                        eventArrayList.removeAll{ it.id  == d.id }
                    }
                    if(dc.type == DocumentChange.Type.MODIFIED){
                        var d = dc.document.toObject(Event::class.java)!!
                        var ind = eventArrayList.indexOfFirst{it.id == d.id}
                        eventArrayList.set(ind,d)
                    }



                }

                adapter.notifyDataSetChanged()
            }
        })

    }
}