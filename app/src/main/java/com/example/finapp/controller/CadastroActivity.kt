package com.example.finapp.controller

import android.os.Bundle
import android.text.TextWatcher
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finapp.R
import com.example.finapp.com.example.finapp.TipoOperacao
import com.example.finapp.data.dao.OperacaoDao
import com.example.finapp.model.OperacaoModel
import android.text.Editable
import java.text.DecimalFormat

class CadastroActivity : AppCompatActivity() {
    private lateinit var operacaoDao: OperacaoDao;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        operacaoDao = OperacaoDao(this);

        val rgTipo = findViewById<RadioGroup>(R.id.rgTipo);
        val etDescricao = findViewById<EditText>(R.id.etDescricao);
        val etValor = findViewById<EditText>(R.id.etValor);
        val btnSalvar = findViewById<Button>(R.id.btnSalvar);

        btnSalvar.setOnClickListener {
            val marcado = rgTipo.checkedRadioButtonId;
            val tipoStr = when (marcado) {
                R.id.rbDebito -> TipoOperacao.DEBITO
                R.id.rbCredito -> TipoOperacao.CREDITO
                else -> {
                    Toast.makeText(this, "Selecione Débito ou Crédito", Toast.LENGTH_SHORT).show()
                    return@setOnClickListener
                }
            };

            val descr = etDescricao.text.toString().trim();
            val valStr = etValor.text.toString().trim();
            if (descr.isEmpty() || valStr.isEmpty()) {
                Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            };

            val valorFormatado = valStr.replace(",", ".");
            val valor = valorFormatado.toDoubleOrNull();

            if (valor == null) {
                Toast.makeText(this, "Valor inválido!", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            };

            val operacao = OperacaoModel(
                id = 0,
                descricao = descr,
                valor = valor,
                tipoOperacao = tipoStr.toString()
            );

            operacaoDao.addOperacao(operacao);

            Toast.makeText(this, "Operação cadastrada!", Toast.LENGTH_SHORT).show();
            finish();
        }

        //Formata o valor para duas casas decimais
        etValor.addTextChangedListener(object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            }

            override fun afterTextChanged(editable: Editable?) {
                etValor.removeTextChangedListener(this);

                val stringOriginal = editable.toString();
                val separadorDecimal = DecimalFormat().decimalFormatSymbols.decimalSeparator;
                var textoLimpo = stringOriginal.replace(".", separadorDecimal.toString());

                val temVirgula = textoLimpo.indexOf(separadorDecimal);

                if (temVirgula >= 0) {
                    val integerPart = textoLimpo.substring(0, temVirgula);
                    var decimalPart = textoLimpo.substring(temVirgula + 1);

                    if (decimalPart.length > 2) {
                        decimalPart = decimalPart.substring(0, 2);
                    }

                    val newText = "$integerPart$separadorDecimal$decimalPart";
                    etValor.setText(newText);
                    etValor.setSelection(newText.length);
                } else {
                    etValor.setText(textoLimpo);
                    etValor.setSelection(textoLimpo.length);
                }

                etValor.addTextChangedListener(this)
            }
        });
    }
}
