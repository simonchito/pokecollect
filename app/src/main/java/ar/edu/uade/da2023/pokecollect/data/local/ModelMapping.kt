package ar.edu.uade.da2023.pokecollect.data.local

import ar.edu.uade.da2023.pokecollect.model.PokemonInfo
import ar.edu.uade.da2023.pokecollect.model.atributos.Ability
import ar.edu.uade.da2023.pokecollect.model.atributos.Sprites
import ar.edu.uade.da2023.pokecollect.model.atributos.Type
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken



fun PokemonInfo.toLocal() = PokemonInfoLocal (
    abilities = Gson().toJson(abilities),
    id= id,
    name = name,
    order = order,
    sprites = Gson().toJson(sprites),
    types = Gson().toJson(types)
)

fun List<PokemonInfo>.toLocal() = map(PokemonInfo::toLocal)

fun PokemonInfoLocal.toExternal() = PokemonInfo(
    abilities = Gson().fromJson(abilities, object : TypeToken<List<Ability>>() {}.type),
    id = id,
    name = name,
    order = order,
    sprites = Gson().fromJson(sprites, Sprites::class.java),
    types = Gson().fromJson(types, object : TypeToken<List<Type>>() {}.type)
)
fun List<PokemonInfoLocal>.toExternal(): List<PokemonInfo> = map(PokemonInfoLocal::toExternal)

