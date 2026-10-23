package com.eduvos.sme.monitor

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.eduvos.sme.monitor.data.AppDatabase
import com.eduvos.sme.monitor.data.Repository
import com.eduvos.sme.monitor.model.User
import kotlinx.coroutines.launch

class RegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val db = AppDatabase.getDatabase(this)
        val repository = Repository(db)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(50, 100, 50, 50)
        }

        val etUsername = EditText(this).apply { hint = "Username" }
        val etEmail = EditText(this).apply { hint = "Email" }
        val etPassword = EditText(this).apply { hint = "Password" }
        val btnRegister = Button(this).apply { text = "Register" }

        layout.addView(etUsername)
        layout.addView(etEmail)
        layout.addView(etPassword)
        layout.addView(btnRegister)

        setContentView(layout)

        btnRegister.setOnClickListener {
            val username = etUsername.text.toString().trim()
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            lifecycleScope.launch {
                repository.registerUser(User(username = username, password = password, email = email))
                Toast.makeText(this@RegisterActivity, "Registered! Now login", Toast.LENGTH_SHORT).show()
                finish()
            }
        }
    }
}