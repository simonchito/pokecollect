package ar.edu.uade.da2023.pokecollect.data.local

import ar.edu.uade.da2023.pokecollect.model.Atributos.Ability
import ar.edu.uade.da2023.pokecollect.model.Atributos.GameIndex
import ar.edu.uade.da2023.pokecollect.model.Atributos.Sprites
import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import com.google.gson.Gson


fun PokemonInfo.toLocal() = PokemonInfoLocal (
    id= id,
    name = name,
    height = height,
    order = order,
    weight = weight,
    abilities = Gson().toJson(abilities),
    gameIndices = Gson().toJson(gameIndices),
    sprites = Gson().toJson(sprites)
)

fun List<PokemonInfo>.toLocal() = map(PokemonInfo::toLocal)

