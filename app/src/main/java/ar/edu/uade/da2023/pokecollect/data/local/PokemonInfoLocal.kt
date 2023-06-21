package ar.edu.uade.da2023.pokecollect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "Pokemon")
data class PokemonInfoLocal(
    @PrimaryKey val id: Int,
    val name: String,
    val order: Int,
    val abilities: String,
    val sprites: String,
    val types : String
)
