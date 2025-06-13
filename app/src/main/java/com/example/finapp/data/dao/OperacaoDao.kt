package com.example.finapp.data.dao

import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import com.example.finapp.model.enum.TipoOperacao
import com.example.finapp.data.db.DBHelper
import com.example.finapp.model.OperacaoModel

class OperacaoDao (private val context: Context){
    private val dbHelper = DBHelper(context);

    fun addOperacao(operacao: OperacaoModel): Long {
        val db = dbHelper.writableDatabase
        val values = ContentValues().apply {
            put("descricao", operacao.descricao)
            put("valor", operacao.valor)
            put("tipoOperacao", operacao.tipoOperacao.name)
        }
        val id = db.insert(DBHelper.TABLE_NAME, null, values)
        db.close()
        return id
    }

    fun getAllOperacoes(): List<OperacaoModel>{
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(DBHelper.TABLE_NAME, null, null, null, null, null, null)
        val operacaoList = mutableListOf<OperacaoModel>()
        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"))
            val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))
            val stringTipoOperacao = cursor.getString(cursor.getColumnIndexOrThrow("tipoOperacao"))
            val tipoOperacao = TipoOperacao.valueOf(stringTipoOperacao);
            operacaoList.add(OperacaoModel(id,descricao,valor,tipoOperacao))
        }
        cursor.close()
        db.close()
        return operacaoList
    }

    fun getAllByTipo(tipoBusca: TipoOperacao): List<OperacaoModel>{
        val db = dbHelper.readableDatabase
        val cursor: Cursor = db.query(
            DBHelper.TABLE_NAME,
            null,
            "tipoOperacao = ?",
            arrayOf(tipoBusca.name),
            null,
            null,
            null
        );
        val operacaoList = mutableListOf<OperacaoModel>()
        while(cursor.moveToNext()){
            val id = cursor.getInt(cursor.getColumnIndexOrThrow("id"))
            val descricao = cursor.getString(cursor.getColumnIndexOrThrow("descricao"))
            val valor = cursor.getDouble(cursor.getColumnIndexOrThrow("valor"))
            val stringTipoOperacao = cursor.getString(cursor.getColumnIndexOrThrow("tipoOperacao"))
            val tipoOperacao = TipoOperacao.valueOf(stringTipoOperacao);
            operacaoList.add(OperacaoModel(id,descricao,valor,tipoOperacao))
        }
        cursor.close()
        db.close()
        return operacaoList
    }

}