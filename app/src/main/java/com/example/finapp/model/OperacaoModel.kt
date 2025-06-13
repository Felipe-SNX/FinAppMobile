package com.example.finapp.model

import com.example.finapp.model.enum.TipoOperacao

data class OperacaoModel(
    val id: Int = 0,
    val descricao: String,
    val valor: Double,
    val tipoOperacao: TipoOperacao
)
