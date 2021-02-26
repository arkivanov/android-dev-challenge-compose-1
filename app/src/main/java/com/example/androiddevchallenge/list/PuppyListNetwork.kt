/*
 * Copyright 2021 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.androiddevchallenge.list

import com.example.androiddevchallenge.list.PuppyListStore.State.Item
import com.example.androiddevchallenge.list.additionaldata.PuppyAdditionalDataProvider
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import kotlinx.serialization.Serializable

internal class PuppyListNetwork(
    private val client: HttpClient,
    private val additionalDataProvider: PuppyAdditionalDataProvider
) : PuppyListStoreFactory.Network {

    override suspend fun loadItems(): List<Item>? =
        client
            .runCatching {
                try {
                    get<List<Dog>>("https://api.thedogapi.com/v1/images/search?limit=100&page=0&order=Desc")
                } catch (e: Throwable) {
                    e.printStackTrace()
                    null
                }
            }
            .getOrElse {
                emptyList()
            }
            ?.asSequence()
            ?.mapNotNull { it.toItem() }
            ?.take(50)
            ?.toList()

    private fun Dog.toItem(): Item? {
        val breed: Breed = breeds?.firstOrNull { it.isValid() } ?: return null
        val additionalData = additionalDataProvider.get(id = id)

        return Item(
            id = id,
            imageUrl = url,
            name = additionalData.name,
            ageMonths = additionalData.ageMonth,
            phoneNumber = additionalData.phoneNumber,
            breedName = breed.name,
            breedGroup = breed.breed_group,
            breedTemperament = breed.temperament,
            breedLifeSpan = breed.life_span,
            breedHeight = breed.height?.metric,
            breedWeight = breed.weight?.metric
        )
    }

    private fun Breed.isValid(): Boolean {
        val fieldCount =
            countField(name) +
                countField(breed_group) +
                countField(temperament) +
                countField(life_span) +
                countField(weight) +
                countField(height)

        return fieldCount >= 2
    }

    private fun countField(arg: Any?): Int =
        if (arg == null) 0 else 1

    @Serializable
    private class Dog(
        val id: String,
        val url: String,
        val breeds: List<Breed>?
    )

    @Serializable
    private class Breed(
        val name: String? = null,
        val breed_group: String? = null,
        val temperament: String? = null,
        val life_span: String? = null,
        val weight: Measure? = null,
        val height: Measure? = null
    )

    @Serializable
    private class Measure(
        val metric: String? = null
    )
}
