import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// Supondo que você tenha uma classe de dados como esta
// data class Transacao(val tipo: String, val descricao: String, val valor: Double)

class TransacaoAdapter(private var transacoes: List<Transacao>) : RecyclerView.Adapter<TransacaoAdapter.TransacaoViewHolder>() {

    // 1. Esta classe representa UM item da lista (nosso list_item_transacao.xml)
    class TransacaoViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imagemTipo: ImageView = itemView.findViewById(R.id.imageViewTipo)
        val textoDescricao: TextView = itemView.findViewById(R.id.textViewDescricao)
        val textoValor: TextView = itemView.findViewById(R.id.textViewValor)
    }

    // 2. Este método é chamado para criar cada item visual da lista pela primeira vez
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TransacaoViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item_transacao, parent, false)
        return TransacaoViewHolder(view)
    }

    // 3. Este método conecta os DADOS da transação com os ELEMENTOS visuais do item
    override fun onBindViewHolder(holder: TransacaoViewHolder, position: Int) {
        val transacao = transacoes[position]

        // Define a descrição e o valor nos TextViews
        holder.textoDescricao.text = transacao.descricao
        holder.textoValor.text = String.format("R$ %.2f", transacao.valor)

        // Define a imagem baseada no tipo da transação
        if (transacao.tipo == "crédito") {
            // holder.imagemTipo.setImageResource(R.drawable.ic_credito) // Imagem de crédito
        } else {
            // holder.imagemTipo.setImageResource(R.drawable.ic_debito) // Imagem de débito
        }
    }

    // 4. Informa ao RecyclerView quantos itens existem na lista
    override fun getItemCount(): Int {
        return transacoes.size
    }

    // Função para atualizar a lista quando o filtro for aplicado
    fun atualizarLista(novaLista: List<Transacao>) {
        transacoes = novaLista
        notifyDataSetChanged() // Avisa a lista que os dados mudaram e ela precisa se redesenhar
    }
}