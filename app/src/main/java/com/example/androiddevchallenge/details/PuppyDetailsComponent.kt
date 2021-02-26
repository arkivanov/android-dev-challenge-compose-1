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

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.androiddevchallenge.database.PuppyDatabase
import com.example.androiddevchallenge.details.PuppyDetailsStore.State.Details
import com.example.androiddevchallenge.details.mapers.stateToModel
import com.example.androiddevchallenge.utils.getOrCreateStore
import com.example.androiddevchallenge.utils.models
import com.example.androiddevchallenge.utils.scope
import kotlinx.coroutines.flow.StateFlow

class PuppyDetailsComponent(
    componentContext: ComponentContext,
    storeFactory: StoreFactory,
    puppyId: String,
    database: PuppyDatabase,
    private val searchInternet: (String) -> Unit,
    private val callPhoneNumber: (String) -> Unit,
    private val onFinished: () -> Unit,
    private val onPhotoActivated: (String) -> Unit
) : PuppyDetails, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getOrCreateStore(
            PuppyDetailsStoreFactory(
                storeFactory = storeFactory,
                puppyId = puppyId,
                database = PuppyDetailsDatabase(
                    queries = database.puppyDatabaseQueries
                )
            )
        )

    override val models: StateFlow<PuppyDetails.Model> = store.models(scope(), stateToModel)

    override fun onCloseClicked() {
        onFinished()
    }

    override fun onPhotoClicked() {
        onPhotoActivated(requireDetails().imageUrl)
    }

    override fun onBreedNameClicked() {
        searchInternet(requireNotNull(requireDetails().breedName))
    }

    override fun onPhoneNumberClicked() {
        callPhoneNumber(requireDetails().phoneNumber)
    }

    private fun requireDetails(): Details =
        requireNotNull(store.state.details)
}
