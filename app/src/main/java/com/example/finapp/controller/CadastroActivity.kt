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
import android.util.Log
import java.math.BigDecimal
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.NumberFormat
import java.util.Locale

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
        etValor.addTextChangedListener(MoneyTextWatcher(etValor));
    }

    class MoneyTextWatcher(val editText: EditText): TextWatcher{
        companion object{
            private const val replaceRegex: String = "[R$,.\u00A0]";
            private const val replaceFinal: String = "R$\u00A0";
        }

        override fun beforeTextChanged(
            editable: CharSequence?,
            start: Int,
            count: Int,
            after: Int
        ) {
        }

        override fun onTextChanged(
            editable: CharSequence?,
            start: Int,
            before: Int,
            count: Int
        ) {
        }

        override fun afterTextChanged(editable: Editable?) {
            try{
                val stringEditable = editable.toString();

                if(stringEditable.isEmpty()) return;

                editText.removeTextChangedListener(this);
                val cleanString = stringEditable.replace(replaceRegex.toRegex(), "");

                val parsed = BigDecimal(cleanString)
                    .setScale(2, RoundingMode.FLOOR)
                    .divide(BigDecimal(100), RoundingMode.FLOOR);

                val decimalFormat = NumberFormat.getCurrencyInstance(Locale("pt", "BR")) as DecimalFormat;
                val formatted = decimalFormat.format(parsed);

                val stringFinal = formatted.replace(replaceFinal, "");
                editText.setText(stringFinal);
                editText.setSelection(stringFinal.length);
                editText.addTextChangedListener(this);

            }catch(e: Exception){
                Log.e("ERROR", e.toString());
            }
        }

    }
}
