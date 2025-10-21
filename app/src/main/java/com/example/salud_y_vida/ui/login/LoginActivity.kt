package com.example.salud_y_vida.ui.login

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.salud_y_vida.databinding.ActivityLoginBinding
import com.example.salud_y_vida.ui.dashboard.DashboardActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnIngresar.setOnClickListener {
            // Sin validación: siempre entra
            startActivity(Intent(this, DashboardActivity::class.java))
            finish()
        }
    }
}