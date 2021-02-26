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

import com.example.androiddevchallenge.database.Puppy
import com.example.androiddevchallenge.database.PuppyDatabaseQueries
import com.example.androiddevchallenge.list.PuppyListStore.State.Item
import com.example.androiddevchallenge.utils.suspendAsList
import com.example.androiddevchallenge.utils.suspendTransaction

internal class PuppyListDatabase(
    private val queries: PuppyDatabaseQueries
) : PuppyListStoreFactory.Database {

    override suspend fun loadItems(): List<Item> =
        queries
            .getAll()
            .suspendAsList()
            .map { it.toItem() }

    override suspend fun saveItems(items: List<Item>) {
        queries.suspendTransaction {
            clear()
            items.forEach {
                insert(it.toEntity())
            }
        }
    }

    private fun Puppy.toItem(): Item =
        Item(
            id = id,
            imageUrl = image_url,
            name = name,
            ageMonths = age_months.toInt(),
            phoneNumber = phone_number,
            breedName = breed_name,
            breedGroup = breed_group,
            breedTemperament = breed_temperament,
            breedLifeSpan = breed_life_span,
            breedHeight = breed_height,
            breedWeight = breed_weight
        )

    private fun Item.toEntity(): Puppy =
        Puppy(
            id = id,
            image_url = imageUrl,
            name = name,
            age_months = ageMonths.toLong(),
            phone_number = phoneNumber,
            breed_name = breedName,
            breed_group = breedGroup,
            breed_temperament = breedTemperament,
            breed_life_span = breedLifeSpan,
            breed_height = breedHeight,
            breed_weight = breedWeight
        )
}
