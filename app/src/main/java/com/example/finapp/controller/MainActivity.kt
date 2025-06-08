package com.example.finapp.controller

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.finapp.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val btnCadastro = findViewById<Button>(R.id.btnCadastro)
        val btnExtrato  = findViewById<Button>(R.id.btnExtrato)
        val btnSair     = findViewById<Button>(R.id.btnSair)

        btnCadastro.setOnClickListener {
            startActivity(Intent(this, CadastroActivity::class.java))
        }
        btnExtrato.setOnClickListener {
            startActivity(Intent(this, ExtratoActivity::class.java))
        }
        btnSair.setOnClickListener {
            finish()
        }
    }
}
