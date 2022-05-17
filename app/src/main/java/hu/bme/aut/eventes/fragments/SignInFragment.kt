package hu.bme.aut.eventes.fragments

import android.app.Activity
import android.content.ContentValues.TAG
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.google.firebase.ktx.options
import hu.bme.aut.eventes.LoginActivity
import hu.bme.aut.eventes.MainActivity

import hu.bme.aut.eventes.databinding.RegisterFragmentBinding



class SignInFragment: Fragment() {

    private lateinit var binding: RegisterFragmentBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = RegisterFragmentBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        binding.btnRegister.setOnClickListener{
            if(checkPassword())
                create(binding.etEmailAddress.text.toString(), binding.etPassword.text.toString())

        }
        return binding.root

    }

    fun create(email : String,password : String){
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure", task.exception)

                    updateUI(null)
                }
            }
    }
    fun updateUI(user: FirebaseUser?){

        if(user != null)
        {
            val mainIntent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(mainIntent)
        }
        else{

            Toast.makeText(requireActivity().baseContext, "User creation failed.", Toast.LENGTH_SHORT).show()
        }

    }
    fun checkPassword(): Boolean {
        var bad = false
        if (!binding.etEmailAddress.text.toString().contains("@")) {
            binding.etEmailAddress.error = "The email address must contain @ sign"
            bad = true;
        }
        if (binding.etPassword.text.toString() != binding.etPassword2.text.toString()) {
            binding.etPassword.error = "Passwords should be the same"
            binding.etPassword2.error = "Passwords should be the same"
            bad = true;
        }
        if (binding.etEmailAddress.text.toString() == "") {
            binding.etEmailAddress.error = "The email address field can't be empty"
            bad = true;
        }
        if (binding.etPassword.text.toString().length < 6) {
            binding.etPassword.error = "The password can't be shorter than 6";
            bad = true;
        }
        if (binding.etPassword2.text.toString() == "") {
            binding.etPassword2.error = "The password field can't be empty";
            bad = true;
        }
        return !bad
    }
}