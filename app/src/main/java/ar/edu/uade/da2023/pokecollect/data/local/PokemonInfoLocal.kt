package ar.edu.uade.da2023.pokecollect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import ar.edu.uade.da2023.pokecollect.model.Atributos.Ability
import ar.edu.uade.da2023.pokecollect.model.Atributos.GameIndex
import ar.edu.uade.da2023.pokecollect.model.Atributos.Sprites


@Entity(tableName = "Pokemon")
data class PokemonInfoLocal(
    @PrimaryKey val id: Int,
    val name: String,
    val height: Int,
    val order: Int,
    val weight: Int,
    val abilities: String,
    val gameIndices: String,
    val sprites: String
)
