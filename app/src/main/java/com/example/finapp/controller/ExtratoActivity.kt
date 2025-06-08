import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.finapp.model.OperacaoModel
import com.example.finapp.R

class ExtratoActivity : AppCompatActivity() {

    // Supondo que você tenha um objeto para acessar o banco de dados
    // private lateinit var database: AppDatabase
    private lateinit var transacaoAdapter: TransacaoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_extrato)

        // --- 1. Encontrar os componentes visuais do XML ---
        val textViewSaldo: TextView = findViewById(R.id.textViewSaldo)
        val spinnerFiltro: Spinner = findViewById(R.id.spinnerFiltro)
        val recyclerView: RecyclerView = findViewById(R.id.recyclerViewTransacoes)

        // --- 2. Carregar TODOS os dados e calcular o saldo ---
        // (Aqui você chamaria seu banco de dados para pegar a lista completa)
        // val todasAsTransacoes = database.transacaoDao().buscarTodas()
        val todasAsTransacoes = listOf<OperacaoModel>() // Lista de exemplo vazia

        // O saldo é calculado com base em TODAS as transações, independente do filtro
        var saldo = 0.0
        for (t in todasAsTransacoes) {
            if (t.tipoOperacao == "crédito") {
                saldo += t.valor
            } else {
                saldo -= t.valor
            }
        }
        textViewSaldo.text = String.format("Saldo da Carteira: R$ %.2f", saldo)

        // --- 3. Configurar o RecyclerView ---
        transacaoAdapter = TransacaoAdapter(todasAsTransacoes)
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = transacaoAdapter

        // --- 4. Configurar o Spinner (ComboBox) de filtro ---
        val opcoesFiltro = listOf("Todos", "Débitos", "Créditos")
        val spinnerAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, opcoesFiltro)
        spinnerFiltro.adapter = spinnerAdapter

        // --- 5. Adicionar a lógica de filtro ao Spinner ---
        spinnerFiltro.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                // Pega a opção que o usuário selecionou
                when (opcoesFiltro[position]) {
                    "Todos" -> {
                        // Aqui você busca TODAS as transações do BD e atualiza o adapter
                        // val listaFiltrada = database.transacaoDao().buscarTodas()
                        // transacaoAdapter.atualizarLista(listaFiltrada)
                    }
                    "Débitos" -> {
                        // Aqui você busca SÓ OS DÉBITOS do BD e atualiza o adapter
                        // val listaFiltrada = database.transacaoDao().buscarDebitos()
                        // transacaoAdapter.atualizarLista(listaFiltrada)
                    }
                    "Créditos" -> {
                        // Aqui você busca SÓ OS CRÉDITOS do BD e atualiza o adapter
                        // val listaFiltrada = database.transacaoDao().buscarCreditos()
                        // transacaoAdapter.atualizarLista(listaFiltrada)
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                // Não precisa fazer nada aqui
            }
        }
    }
}