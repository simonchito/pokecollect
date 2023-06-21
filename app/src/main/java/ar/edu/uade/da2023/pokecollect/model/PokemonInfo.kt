package ar.edu.uade.da2023.pokecollect.model

import ar.edu.uade.da2023.pokecollect.model.atributos.Ability
import ar.edu.uade.da2023.pokecollect.model.atributos.Sprites
import ar.edu.uade.da2023.pokecollect.model.atributos.Type


data class PokemonInfo(
    val abilities: List<Ability>,
    val id: Int,
    val name: String,
    val order: Int,
    val sprites: Sprites,
    val types : List<Type>
)
