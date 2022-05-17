package hu.bme.aut.eventes.fragments

import android.content.ContentValues.TAG
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import hu.bme.aut.eventes.LoginActivity
import hu.bme.aut.eventes.databinding.LoginFragmentBinding
import android.content.Intent
import hu.bme.aut.eventes.MainActivity


class LoginFragment: Fragment() {
    private lateinit var binding: LoginFragmentBinding
    private lateinit var auth: FirebaseAuth




    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        binding = LoginFragmentBinding.inflate(inflater, container, false)
        auth = FirebaseAuth.getInstance()
        binding.btnLogin.setOnClickListener{
            login(binding.etEmailAddress.text.toString(),binding.etPassword.text.toString());
        }
        return binding.root
    }


    fun login(email: String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(requireActivity()) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
//                    Toast.makeText(baseContext, "Authentication failed.",
//                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                }
            }

    }
    fun updateUI(user : FirebaseUser?){

        if(user != null) {
            val mainIntent = Intent(requireActivity(), MainActivity::class.java)
            startActivity(mainIntent)
        }
        else
        {
            Toast.makeText(requireActivity().baseContext, "Login failed.",
                        Toast.LENGTH_SHORT).show()
        }

    }

}