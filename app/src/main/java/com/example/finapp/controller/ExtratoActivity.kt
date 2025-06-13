package com.example.finapp.controller

import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.ListView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finapp.model.OperacaoModel
import com.example.finapp.R
import com.example.finapp.utils.OperacaoAdapter
import com.example.finapp.model.enum.TipoOperacao
import com.example.finapp.data.dao.OperacaoDao

class ExtratoActivity : AppCompatActivity() {

    private lateinit var operacaoAdapter: OperacaoAdapter;
    private lateinit var operacaoDao: OperacaoDao;
    private lateinit var listView: RecyclerView;
    private lateinit var textViewSaldo: TextView;
    private lateinit var emptyTextView: TextView;
    private lateinit var comboFiltro: AutoCompleteTextView;

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_extrato);

        textViewSaldo = findViewById(R.id.textViewSaldo);
        emptyTextView = findViewById(R.id.emptyTextView);
        comboFiltro = findViewById(R.id.comboFiltro);
        listView = findViewById(R.id.recyclerViewTransacoes);
        operacaoDao = OperacaoDao(this);

        listAllOperacoes();

        val opcoesFiltro = listOf("Todos", "Débitos", "Créditos");
        val comboAdapter = ArrayAdapter(this, android.R.layout.simple_dropdown_item_1line, opcoesFiltro)
        comboFiltro.setAdapter(comboAdapter);

        comboFiltro.setOnItemClickListener { parent, view, position, id ->
            val itemSelecionado = parent.getItemAtPosition(position).toString()

            when (itemSelecionado) {
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
    }

    fun listAllOperacoes(){
        val operacoes: List<OperacaoModel> = operacaoDao.getAllOperacoes();
        showData(operacoes);
        calculateBalance(operacoes);
    }

    fun listAllByTipo(tipoOperacao: TipoOperacao){
        val operacoes: List<OperacaoModel> = operacaoDao.getAllByTipo(tipoOperacao);
        showData(operacoes);
    }

    fun showData(operacoes: List<OperacaoModel>){
        if(operacoes.isEmpty()){
            hideListView();
        }
        else {
            showListView();
            operacaoAdapter = OperacaoAdapter(operacoes);
            listView.layoutManager = LinearLayoutManager(this);
            listView.adapter = operacaoAdapter;
        }
    }

    fun calculateBalance(operacoes: List<OperacaoModel> ){
        var saldo = 0.0
        for (t in operacoes) {
            if (t.tipoOperacao == TipoOperacao.CREDITO) {
                saldo += t.valor
            } else {
                saldo -= t.valor
            }
        }
        textViewSaldo.text = String.format("Saldo da Carteira: R$ %.2f", saldo);
    }

    fun hideListView(){
        listView.visibility = ListView.GONE;
        emptyTextView.visibility = TextView.VISIBLE;
    }

    fun showListView(){
        listView.visibility = ListView.VISIBLE;
        emptyTextView.visibility = TextView.GONE;
    }
}