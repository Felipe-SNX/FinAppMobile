package com.example.finapp.controller

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.ListView
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finapp.model.OperacaoModel
import com.example.finapp.R
import com.example.finapp.com.example.finapp.OperacaoAdapter
import com.example.finapp.com.example.finapp.TipoOperacao
import com.example.finapp.data.dao.OperacaoDao

class ExtratoActivity : AppCompatActivity() {

    private lateinit var operacaoAdapter: OperacaoAdapter
    private lateinit var operacaoDao: OperacaoDao;
    private lateinit var listView: RecyclerView;
    private lateinit var textViewSaldo: TextView;
    private lateinit var emptyTextView: TextView;
    private lateinit var comboFiltro: Spinner;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato);

        textViewSaldo = findViewById(R.id.textViewSaldo);
        emptyTextView = findViewById(R.id.emptyTextView);
        comboFiltro = findViewById(R.id.spinnerFiltro);
        listView = findViewById(R.id.recyclerViewTransacoes);
        operacaoDao = OperacaoDao(this);

        listAllOperacoes();

        val opcoesFiltro = listOf("Todos", "Débitos", "Créditos");
        val comboAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoesFiltro);
        comboFiltro.adapter = comboAdapter;

        comboFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                when (opcoesFiltro[position]) {
                    "Todos" -> {
                        listAllOperacoes();
                    }
                    "Débitos" -> {
                        listAllByTipo(TipoOperacao.DEBITO);
                    }
                    "Créditos" -> {
                        listAllByTipo(TipoOperacao.CREDITO);
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }
    }

    fun listAllOperacoes(){
        val operacoes: List<OperacaoModel> = operacaoDao.getAllOperacoes();
        if(operacoes.isEmpty()){
            listView.visibility = ListView.GONE
            emptyTextView.visibility = TextView.VISIBLE
        }
        else {
            listView.visibility = ListView.VISIBLE
            emptyTextView.visibility = TextView.GONE

            operacaoAdapter = OperacaoAdapter(operacoes)
            listView.layoutManager = LinearLayoutManager(this)
            listView.adapter = operacaoAdapter
        }
        calcularSaldo(operacoes);
    }

    fun listAllByTipo(tipoOperacao: TipoOperacao){
        val operacoes: List<OperacaoModel> = operacaoDao.getAllByTipo(tipoOperacao.toString());
        if(operacoes.isEmpty()){
            listView.visibility = ListView.GONE
            emptyTextView.visibility = TextView.VISIBLE
        }
        else {
            listView.visibility = ListView.VISIBLE
            emptyTextView.visibility = TextView.GONE

            operacaoAdapter = OperacaoAdapter(operacoes)
            listView.layoutManager = LinearLayoutManager(this)
            listView.adapter = operacaoAdapter
        }
    }

    fun calcularSaldo(operacoes: List<OperacaoModel> ){
        var saldo = 0.0
        for (t in operacoes) {
            if (t.tipoOperacao == TipoOperacao.CREDITO.toString()) {
                saldo += t.valor
            } else {
                saldo -= t.valor
            }
        }
        textViewSaldo.text = String.format("Saldo da Carteira: R$ %.2f", saldo)
    }
}