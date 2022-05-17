package hu.bme.aut.eventes.fragments

import android.icu.text.SimpleDateFormat
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import hu.bme.aut.eventes.data.Event
import hu.bme.aut.eventes.databinding.CreateeventfragmentBinding
import java.text.ParseException
import java.time.Instant.now
import java.time.LocalDate
import java.time.chrono.ChronoLocalDate
import java.time.format.DateTimeFormatter
import java.util.*

class CreateEventFragment : Fragment() {
    private lateinit var binding: CreateeventfragmentBinding

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = CreateeventfragmentBinding.inflate(inflater, container, false)
        binding.createButton.setOnClickListener{

            if(checkInput()) {
                var owner = FirebaseAuth.getInstance().currentUser?.email.toString()
                var name = binding.createName.text.toString()
                var date = binding.createDate.text.toString()
                var desc = binding.createDesc.text.toString()
                var location = binding.createLocation.text.toString()
                var uniqueID = UUID.randomUUID().toString()
                var newEvent = Event(uniqueID, owner, name, date, desc, location)
                var db = FirebaseFirestore.getInstance()
                db.collection("Events").document(uniqueID).set(newEvent)
                binding.createName.text = null
                binding.createDate.text = null
                binding.createDesc.text = null
                binding.createLocation.text = null
                Toast.makeText(
                    requireActivity(), "You created an Event!",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }
        return binding.root

    }
    private fun checkInput() : Boolean {
        var bad = false
        try {
            bad = checkDate(binding.createDate.text.toString())
            if (bad) {
                binding.createDate.error = "The date can't be in the past";
            }
        } catch (e: Exception) {
            binding.createDate.error = "The proper date format is yyyy-mm-dd";
            bad = true;
        }

        if (binding.createName.text.toString() == "") {
            binding.createName.error = "The name can't be empty";
            bad = true;
        }
        if (binding.createLocation.text.toString() == "") {
            binding.createLocation.error = "The location can't be empty";
            bad = true;
        }
        if (binding.createDesc.text.toString() == "") {
            binding.createDesc.error = "The description can't be empty";
            bad = true;
        }
        return !bad
    }

    @Throws(ParseException::class)
    fun checkDate(dateStr: String): Boolean {
        val date = LocalDate.parse(dateStr, DateTimeFormatter.ISO_DATE)
        return date.isBefore(LocalDate.now())
    }
}