package com.example.finapp.controller

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.finapp.R
import com.example.finapp.model.enum.TipoOperacao
import com.example.finapp.data.dao.OperacaoDao
import com.example.finapp.model.OperacaoModel
import com.example.finapp.utils.MoneyTextWatcher


class CadastroActivity : AppCompatActivity() {
    private lateinit var operacaoDao: OperacaoDao;
    private lateinit var rgTipo: RadioGroup;
    private lateinit var etDescricao: EditText;
    private lateinit var etValor: EditText;
    private lateinit var btnSalvar: Button;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro);
        operacaoDao = OperacaoDao(this);

        rgTipo = findViewById<RadioGroup>(R.id.rgTipo);
        etDescricao = findViewById<EditText>(R.id.etDescricao);
        etValor = findViewById<EditText>(R.id.etValor);
        btnSalvar = findViewById<Button>(R.id.btnSalvar);

        btnSalvar.setOnClickListener {
            salvarOperacao();
        }

        //Formata o valor para duas casas decimais
        etValor.addTextChangedListener(MoneyTextWatcher(etValor));
    }

    private fun salvarOperacao(){
        if(!validarFormulario()){
            return;
        }

        val operacao = criarOperacao() ?: return;

        operacaoDao.addOperacao(operacao);

        Toast.makeText(this, "Operação cadastrada!", Toast.LENGTH_SHORT).show();
        finish();
    }

    private fun validarFormulario(): Boolean{
        if(rgTipo.checkedRadioButtonId == -1){
            Toast.makeText(this, "Selecione Débito ou Crédito", Toast.LENGTH_SHORT).show();
            return false;
        }

        if(etDescricao.text.isNullOrBlank() || etValor.text.isNullOrBlank()){
            Toast.makeText(this, "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private fun criarOperacao(): OperacaoModel?{

        val tipoOperacao = when (rgTipo.checkedRadioButtonId) {
            R.id.rbDebito -> TipoOperacao.DEBITO
            else -> TipoOperacao.CREDITO
        };

        val descr = etDescricao.text.toString().trim();
        val valStr = etValor.text.toString().trim();

        val valor = valStr.replace(".", "").replace(",", ".").toDoubleOrNull();
        if (valor == null) {
            Toast.makeText(this, "Valor inválido!", Toast.LENGTH_SHORT).show();
            return null;
        }

        return OperacaoModel(
            id = 0,
            descricao = descr,
            valor = valor,
            tipoOperacao = tipoOperacao
        );
    }
}
