package ar.edu.uade.da2023.pokecollect.ui

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.text.HtmlCompat
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.pokecollect.R
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import com.bumptech.glide.Glide

class BagAdapter(private var allFavorites: List<PokemonInfo>, private val listener: OnRemoveFavoriteListener?) : RecyclerView.Adapter<BagAdapter.ViewHolder>() {
    private var filteredFavorites: List<PokemonInfo> = allFavorites

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.card_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val favorite = filteredFavorites[position]
        holder.bind(favorite)
    }

    override fun getItemCount(): Int {
        return filteredFavorites.size
    }

    fun setFavorites(favorites: List<PokemonInfo>) {
        allFavorites = favorites
        filterFavorites("")
    }

    fun filterFavorites(query: String) {
        filteredFavorites = if (query.isNotBlank()) {
            allFavorites.filter { favorite ->
                favorite.name.contains(query, ignoreCase = true)
            }
        } else {
            allFavorites
        }
        notifyDataSetChanged()
    }
    fun removeFavorite(pokemon: PokemonInfo) {
        allFavorites = allFavorites.filterNot { it.id == pokemon.id }
        filterFavorites("") // Actualizar la lista filtrada
        notifyDataSetChanged() // Notificar cambios en la vista
    }
    interface OnRemoveFavoriteListener {
        fun onRemoveFavorite(pokemon: PokemonInfo)
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val itemImage: ImageView = itemView.findViewById(R.id.pokemonBagImg)
        private val itemTitle: TextView = itemView.findViewById(R.id.nameBagTxv)
        private val itemOrder: TextView = itemView.findViewById(R.id.figureBagTxv)
        private val itemSkill: TextView = itemView.findViewById(R.id.skillBagTxv)
        private val itemType : TextView = itemView.findViewById(R.id.typeBagTxv)
        private val removeButton: Button = itemView.findViewById(R.id.borrarBtn)



        fun bind(favorite: PokemonInfo) {
            // Establecer los datos del favorito en los elementos de la vista
            Glide.with(itemView)
                .load(favorite.sprites.front_default)
                .into(itemImage)
            itemTitle.text = favorite.name.replaceFirstChar { it.uppercaseChar() }
            itemOrder.text = "Figure: ${favorite.order}"
            itemSkill.text = "Skill: ${favorite.abilities.joinToString(", ") { it.ability?.name?.capitalize() ?: "" }}"
            itemType.text = "Type: ${favorite.types.joinToString(", ") { it.type?.name?.capitalize() ?: "" }}"

            // Imprimir los valores como logs

            Log.d("ViewHolder", "Contenido de itemOrder: $itemOrder")

            removeButton.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    val pokemon = filteredFavorites[position]
                    listener?.onRemoveFavorite(pokemon)
                }
            }
        }
    }
}
