package ar.edu.uade.da2023.pokecollect.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "PokemonInfo")
data class PokemonInfoLocal(
    val abilities: String,
    @PrimaryKey val id: Int,
    val name: String,
    val order: Int,
    val sprites: String,
    val types : String
)
