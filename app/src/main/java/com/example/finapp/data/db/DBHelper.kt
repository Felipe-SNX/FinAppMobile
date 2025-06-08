package com.example.finapp.data.db

import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.content.Context
import com.example.finapp.com.example.finapp.TipoOperacao

class DBHelper(context: Context):
    SQLiteOpenHelper(context, DATABASE_NAME, null , DATABASE_VERSION){

    companion object{
        const val DATABASE_NAME = "finapp.db"
        const val DATABASE_VERSION = 1
        const val TABLE_NAME = "controle"
    }

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable = """
            CREATE TABLE $TABLE_NAME (
                id INTEGER PRIMARY KEY AUTOINCREMENT,
                descricao TEXT NOT NULL,
                valor DOUBLE NOT NULL,
                tipoOperacao TEXT NOT NULL
            )
        """.trimIndent()
        db?.execSQL(createTable)

        /*val values1 = android.content.ContentValues().apply {
            put("descricao", "Operação Teste de Crédito")
            put("valor", 9.98)
            put("tipoOperacao", TipoOperacao.CREDITO.toString())
        }
        db?.insert(TABLE_NAME, null, values1)

        val values2 = android.content.ContentValues().apply {
            put("descricao", "Operação Teste 2 de Crédito")
            put("valor", 12.98)
            put("tipoOperacao", TipoOperacao.CREDITO.toString())
        }
        db?.insert(TABLE_NAME, null, values2)

        val values3 = android.content.ContentValues().apply {
            put("descricao", "Operação Teste de Débito")
            put("valor", 23.98)
            put("tipoOperacao", TipoOperacao.DEBITO.toString())
        }
        db?.insert(TABLE_NAME, null, values3)

        val values4 = android.content.ContentValues().apply {
            put("descricao", "Operação Teste 2 de Débito")
            put("valor", 12.98)
            put("tipoOperacao", TipoOperacao.DEBITO.toString())
        }
        db?.insert(TABLE_NAME, null, values4)*/
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS $TABLE_NAME")
        onCreate(db)
    }
}