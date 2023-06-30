package ar.edu.uade.da2023.pokecollect.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.pokecollect.R
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import com.bumptech.glide.Glide

class BagAdapter(private var favorites: List<PokemonInfo>) : RecyclerView.Adapter<BagAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = favorites[position]
        holder.bind(favorite)
    }

    override fun getItemCount(): Int {
        return favorites.size
    }

    fun setFavorites(favorites: List<PokemonInfo>) {
        this.favorites = favorites
        notifyDataSetChanged()

        // Imprimir un log de la lista de favoritos
        Log.d("BagAdapter", "Lista de favoritos: $favorites")

    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage: ImageView = itemView.findViewById(R.id.pokemonBagImg)
        private val itemTitle: TextView = itemView.findViewById(R.id.nameBagTxv)
        private val itemOrder: TextView = itemView.findViewById(R.id.figureBagTxv)

        fun bind(favorite: PokemonInfo) {
            // Establecer los datos del favorito en los elementos de la vista
            Glide.with(itemView)
                .load(favorite.sprites.front_default)
                .into(itemImage)
            itemTitle.text = favorite.name.capitalize()
            itemOrder.text = "Figure: ${favorite.order}"
            // Imprimir los valores como logs

            Log.d("ViewHolder", "Contenido de itemOrder: $itemOrder")
        }
    }
}