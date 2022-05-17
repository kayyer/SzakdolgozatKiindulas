package hu.bme.aut.eventes

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.ktx.Firebase
import hu.bme.aut.eventes.adapter.LoginAdapter
import hu.bme.aut.eventes.databinding.ActivityLoginBinding

class LoginActivity : AppCompatActivity() {

        private lateinit var binding: ActivityLoginBinding

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            binding = ActivityLoginBinding.inflate(layoutInflater)

            setContentView(binding.root)

            binding.vpLogin.adapter = LoginAdapter(supportFragmentManager)

            Toast.makeText(baseContext, "Swipe right to Sign Up",
                Toast.LENGTH_LONG).show()

        }

    }
