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
package com.example.androiddevchallenge.details.mapers

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Category
import androidx.compose.material.icons.filled.EmojiNature
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.OpenInBrowser
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.details.PuppyDetails.Model
import com.example.androiddevchallenge.details.PuppyDetails.Model.DetailsIcon
import com.example.androiddevchallenge.details.PuppyDetails.Model.DetailsItem
import com.example.androiddevchallenge.details.PuppyDetailsStore.State
import com.example.androiddevchallenge.details.PuppyDetailsStore.State.Details
import com.example.androiddevchallenge.utils.Img
import com.example.androiddevchallenge.utils.formatNameAndAge

internal val stateToModel: State.() -> Model =
    {
        details
            ?.toModel()
            ?: Model()
    }

private fun Details.toModel(): Model =
    Model(
        image = Img.Remote(url = imageUrl),
        title = formatNameAndAge(name = name, ageMonths = ageMonths),
        phoneNumber = phoneNumber,
        detailItems = getDetailItems()
    )

private fun Details.getDetailItems(): List<DetailsItem> =
    listOfNotNull(
        breedName?.let {
            DetailsItem(
                icon = DetailsIcon(
                    imageVector = Icons.Default.Label,
                    contentDescription = "Breed icon"
                ),
                titleRes = R.string.breed,
                text = it,
                trailingIcon = DetailsIcon(
                    imageVector = Icons.Default.OpenInBrowser,
                    contentDescription = "Search breed icon"
                ),
                action = Model.DetailsAction.SEARCH_BREED
            )
        },
        breedGroup?.let {
            DetailsItem(
                icon = DetailsIcon(
                    imageVector = Icons.Default.Category,
                    contentDescription = "Breed icon"
                ),
                titleRes = R.string.breed_group,
                text = it
            )
        },
        breedTemperament?.let {
            DetailsItem(
                icon = DetailsIcon(
                    imageVector = Icons.Default.EmojiNature,
                    contentDescription = "Temperament icon"
                ),
                titleRes = R.string.typical_temperament,
                text = it
            )
        },
        breedLifeSpan?.let {
            DetailsItem(
                icon = DetailsIcon(
                    imageVector = Icons.Default.AccessTime,
                    contentDescription = "Life span icon"
                ),
                titleRes = R.string.typical_life_span,
                text = it
            )
        },
        breedHeight?.let {
            DetailsItem(
                icon = DetailsIcon(
                    imageVector = Icons.Default.Height,
                    contentDescription = "Height icon"
                ),
                titleRes = R.string.typical_height,
                text = it
            )
        },
        breedWeight?.let {
            DetailsItem(
                icon = DetailsIcon(
                    imageVector = Icons.Default.FitnessCenter,
                    contentDescription = "Weight icon"
                ),
                titleRes = R.string.typical_weight,
                text = it
            )
        }
    )
