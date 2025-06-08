package com.example.finapp.controller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finapp.R
import com.example.finapp.data.dao.OperacaoDao
import com.example.finapp.model.OperacaoModel

class CadastroActivity : AppCompatActivity() {
    private lateinit var operacaoDao: OperacaoDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cadastro)
        operacaoDao = OperacaoDao(this)

        val rgTipo = findViewById<RadioGroup>(R.id.rgTipo)
        val etDescricao = findViewById<EditText>(R.id.etDescricao)
        val etValor = findViewById<EditText>(R.id.etValor)
        val btnSalvar = findViewById<Button>(R.id.btnSalvar)

        btnSalvar.setOnClickListener {
            val marcado = rgTipo.checkedRadioButtonId
            val tipoStr = when (marcado) {
                R.id.rbDebito -> "debito"
                R.id.rbCredito -> "credito"
                else -> {
                    Toast.makeText(this, "Selecione Débito ou Crédito", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            }

            val descr = etDescricao.text.toString().trim()
            val valStr = etValor.text.toString().trim()
            if (descr.isEmpty() || valStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }
            val valor = valStr.toDoubleOrNull()
            if (valor == null) {
                Toast.makeText(this, "Valor inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val operacao = OperacaoModel(
                id = 0,
                descricao = descr,
                valor = valor,
                tipoOperacao = tipoStr
            )

            operacaoDao.addOperacao(operacao)

            Toast.makeText(this, "Operação cadastrada!", Toast.LENGTH_SHORT).show()
            finish()
        }
    }
}
