package com.example.finapp.model

data class OperacaoModel(
    val id: Int = 0,
    val descricao: String,
    val valor: Double,
    val tipoOperacao: String
)
