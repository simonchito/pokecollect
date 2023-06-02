package ar.edu.uade.da2023.pokecollect.model

import ar.edu.uade.da2023.pokecollect.model.Atributos.Ability
import ar.edu.uade.da2023.pokecollect.model.Atributos.GameIndex
import ar.edu.uade.da2023.pokecollect.model.Atributos.Sprites

data class PokemonInfo(
    val id: Int,
    val name: String,
    val height: Int,
    val order: Int,
    val weight: Int,
    val abilities: List<Ability>,
    val gameIndices: List<GameIndex>,
    val sprites: Sprites

)
