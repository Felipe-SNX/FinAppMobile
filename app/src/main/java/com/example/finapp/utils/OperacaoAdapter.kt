package com.example.finapp.utils

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.example.finapp.R
import com.example.finapp.model.OperacaoModel
import com.example.finapp.model.enum.TipoOperacao
import com.example.finapp.utils.OperacaoAdapter.OperacaoViewHolder

class OperacaoAdapter(private var operacoes: List<OperacaoModel>) : Adapter<OperacaoViewHolder>() {

    class OperacaoViewHolder(itemView: View) : ViewHolder(itemView) {
        val imagemTipo: ImageView = itemView.findViewById(R.id.imageViewTipo)
        val textoDescricao: TextView = itemView.findViewById(R.id.textViewDescricao)
        val textoValor: TextView = itemView.findViewById(R.id.textViewValor)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OperacaoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_transacao, parent, false)
        return OperacaoViewHolder(view)
    }

    override fun onBindViewHolder(holder: OperacaoViewHolder, position: Int) {
        val operacao = operacoes[position]

        holder.textoDescricao.text = operacao.descricao
        holder.textoValor.text = String.format("R$ %.2f", operacao.valor)

        if (operacao.tipoOperacao == TipoOperacao.CREDITO) {
            holder.imagemTipo.setImageResource(R.drawable.img_credito)
        } else {
            holder.imagemTipo.setImageResource(R.drawable.img_debito)
        }
    }

    override fun getItemCount(): Int {
        return operacoes.size
    }
}