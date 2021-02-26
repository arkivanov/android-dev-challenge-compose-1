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
package com.example.androiddevchallenge.details

import com.example.androiddevchallenge.database.Puppy
import com.example.androiddevchallenge.database.PuppyDatabaseQueries
import com.example.androiddevchallenge.details.PuppyDetailsStore.State.Details
import com.example.androiddevchallenge.utils.suspendAsOneOrNull

internal class PuppyDetailsDatabase(
    private val queries: PuppyDatabaseQueries
) : PuppyDetailsStoreFactory.Database {

    override suspend fun load(id: String): Details? =
        queries
            .getById(id = id)
            .suspendAsOneOrNull()
            ?.toDetails()

    private fun Puppy.toDetails(): Details =
        Details(
            id = id,
            imageUrl = image_url,
            name = name,
            phoneNumber = phone_number,
            ageMonths = age_months.toInt(),
            breedName = breed_name,
            breedGroup = breed_group,
            breedTemperament = breed_temperament,
            breedLifeSpan = breed_life_span,
            breedHeight = breed_height,
            breedWeight = breed_weight
        )
}
