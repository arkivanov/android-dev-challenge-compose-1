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
package com.example.androiddevchallenge.list.additionaldata

internal class PuppyAdditionalDataProvider {

    private val additionalData = HashMap<String, PuppyAdditionalData>()

    fun get(id: String): PuppyAdditionalData =
        additionalData.getOrPut(id) {
            PuppyAdditionalData(
                name = NAMES.random(),
                ageMonth = (2..120).random(),
                phoneNumber = "+447390123456"
            )
        }

    private companion object {
        private val NAMES =
            listOf(
                "Bella", "Luna", "Charlie", "Lucy", "Cooper",
                "Max", "Bailey", "Daisy", "Sadie", "Lola",
                "Buddy", "Molly", "Stella", "Tucker", "Bear",
                "Zoey", "Duke", "Harley", "Maggie", "Jax",
                "Bentley", "Milo", "Oliver", "Riley", "Rocky",
                "Penny", "Sophie", "Chloe", "Jack", "Lily",
                "Nala", "Piper", "Zeus", "Ellie", "Winston",
                "Toby", "Loki", "Murphy", "Roxy", "Coco",
                "Rosie", "Teddy", "Ruby", "Gracie", "Leo",
                "Finn", "Scout", "Dexter", "Ollie", "Koda"
            )
    }
}
