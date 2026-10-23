package com.eduvos.sme.monitor

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eduvos.sme.monitor.data.AppDatabase
import com.eduvos.sme.monitor.data.Repository
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val repository = Repository(db)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 100, 50, 50)
        }

        val etUsername = EditText(this).apply { hint = "Username" }
        val etPassword = EditText(this).apply { hint = "Password" }
        val btnLogin = Button(this).apply { text = "Login" }
        val btnRegister = Button(this).apply { text = "Go to Register" }

        layout.addView(etUsername)
        layout.addView(etPassword)
        layout.addView(btnLogin)
        layout.addView(btnRegister)

        setContentView(layout)

        btnLogin.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter username and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Hardcoded admin for marks + DB check
            if (username == "admin" && password == "sme123") {
                startActivity(Intent(this, MainActivity::class.java))
                return@setOnClickListener
            }

            lifecycleScope.launch {
                val user = repository.getUser(username, password)
                if (user != null) {
                    Toast.makeText(this@LoginActivity, "Login successful", Toast.LENGTH_SHORT).show()
                    startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                } else {
                    Toast.makeText(this@LoginActivity, "Invalid credentials", Toast.LENGTH_SHORT).show()
                }
            }
        }

        btnRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }
}