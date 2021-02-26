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

import com.arkivanov.decompose.ComponentContext
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.androiddevchallenge.database.PuppyDatabase
import com.example.androiddevchallenge.list.PuppyList.Model
import com.example.androiddevchallenge.list.PuppyListStore.Intent
import com.example.androiddevchallenge.list.additionaldata.PuppyAdditionalDataProvider
import com.example.androiddevchallenge.list.mappers.stateToModel
import com.example.androiddevchallenge.utils.getOrCreateStore
import com.example.androiddevchallenge.utils.models
import com.example.androiddevchallenge.utils.scope
import io.ktor.client.HttpClient
import kotlinx.coroutines.flow.StateFlow

class PuppyListComponent(
    private val componentContext: ComponentContext,
    storeFactory: StoreFactory,
    client: HttpClient,
    database: PuppyDatabase,
    private val setDarkMode: (Boolean) -> Unit,
    private val onPuppyActivated: (id: String) -> Unit
) : PuppyList, ComponentContext by componentContext {

    private val store =
        instanceKeeper.getOrCreateStore(
            PuppyListStoreFactory(
                storeFactory = storeFactory,
                network = PuppyListNetwork(
                    client = client,
                    additionalDataProvider = PuppyAdditionalDataProvider()
                ),
                database = PuppyListDatabase(
                    queries = database.puppyDatabaseQueries
                )
            )
        )

    override val models: StateFlow<Model> = store.models(scope(), stateToModel)

    override fun onPuppyClicked(id: String) {
        onPuppyActivated(id)
    }

    override fun onRefreshClicked() {
        store.accept(Intent.Refresh)
    }

    override fun onSetDarkModeClicked(isDarkMode: Boolean) {
        setDarkMode(isDarkMode)
    }
}
