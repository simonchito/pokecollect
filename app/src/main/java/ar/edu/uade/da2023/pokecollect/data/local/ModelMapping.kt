package ar.edu.uade.da2023.pokecollect.data.local

import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import com.google.gson.Gson


fun PokemonInfo.toLocal() = PokemonInfoLocal (
    id= id,
    name = name,
    order = order,
    abilities = Gson().toJson(abilities),
    sprites = Gson().toJson(sprites),
    types = Gson().toJson(types)
)

fun List<PokemonInfo>.toLocal() = map(PokemonInfo::toLocal)

