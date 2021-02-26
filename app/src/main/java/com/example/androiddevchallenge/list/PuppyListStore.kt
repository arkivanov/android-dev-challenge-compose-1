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

import com.arkivanov.mvikotlin.core.store.Store
import com.example.androiddevchallenge.list.PuppyListStore.Intent
import com.example.androiddevchallenge.list.PuppyListStore.State

internal interface PuppyListStore : Store<Intent, State, Nothing> {

    sealed class Intent {
        object Refresh : Intent()
    }

    data class State(
        val isLoading: Boolean = false,
        val items: List<Item> = emptyList()
    ) {
        data class Item(
            val id: String,
            val imageUrl: String,
            val name: String,
            val ageMonths: Int,
            val phoneNumber: String,
            val breedName: String?,
            val breedGroup: String?,
            val breedTemperament: String?,
            val breedLifeSpan: String?,
            val breedHeight: String?,
            val breedWeight: String?
        )
    }
}
