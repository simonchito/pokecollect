package ar.edu.uade.da2023.pokecollect.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import ar.edu.uade.da2023.pokecollect.R

class BagAdapter: RecyclerView.Adapter<BagAdapter.ViewHolder>(){


    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): ViewHolder {
        val v = LayoutInflater.from(viewGroup.context).inflate(R.layout.card_view,viewGroup,false)
        return ViewHolder(v)
    }

    val titles = arrayOf("Pokemon","Pokemon","Pokemon","Pokemon")

    val details = arrayOf("1","2","3","4")

    val image = intArrayOf(R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground,
        R.drawable.ic_launcher_foreground)

    override fun onBindViewHolder(viewHolder: ViewHolder, i: Int) {
        viewHolder.itemImage.setImageResource(image[i])
        viewHolder.itemTitle.text = titles[i]
        viewHolder.itemOrder.text = details [i]

    }

    override fun getItemCount(): Int{
        return titles.size
    }



    inner class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        var itemImage: ImageView
        var itemTitle: TextView
        var itemOrder: TextView

        init{
            itemImage = itemView.findViewById(R.id.pokemonBagImg)
            itemTitle = itemView.findViewById(R.id.nameBagTxv)
            itemOrder = itemView.findViewById(R.id.figureBagTxv)
        }


    }


}