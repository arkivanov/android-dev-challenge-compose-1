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

import androidx.annotation.StringRes
import androidx.compose.ui.graphics.vector.ImageVector
import com.example.androiddevchallenge.utils.Img
import kotlinx.coroutines.flow.StateFlow

interface PuppyDetails {

    val models: StateFlow<Model>

    fun onCloseClicked()
    fun onPhotoClicked()
    fun onBreedNameClicked()
    fun onPhoneNumberClicked()

    data class Model(
        val image: Img? = null,
        val title: String = "",
        val phoneNumber: String = "",
        val detailItems: List<DetailsItem> = emptyList()
    ) {
        data class DetailsItem(
            val icon: DetailsIcon,
            @StringRes val titleRes: Int,
            val text: String? = null,
            val trailingIcon: DetailsIcon? = null,
            val action: DetailsAction? = null
        )

        data class DetailsIcon(
            val imageVector: ImageVector,
            val contentDescription: String
        )

        enum class DetailsAction {
            SEARCH_BREED
        }
    }
}
