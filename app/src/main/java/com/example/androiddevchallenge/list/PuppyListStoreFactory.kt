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

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.example.androiddevchallenge.list.PuppyListStore.Intent
import com.example.androiddevchallenge.list.PuppyListStore.State
import com.example.androiddevchallenge.list.PuppyListStore.State.Item

internal class PuppyListStoreFactory(
    private val storeFactory: StoreFactory,
    private val network: Network,
    private val database: Database
) : () -> PuppyListStore {

    override fun invoke(): PuppyListStore =
        object :
            PuppyListStore,
            Store<Intent, State, Nothing> by storeFactory.create(
                name = "PuppyListStore",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Result {
        object LoadingStarted : Result()
        data class ItemsLoaded(val items: List<Item>) : Result()
        object LoadingFailed : Result()
    }

    private inner class ExecutorImpl : SuspendExecutor<Intent, Unit, State, Result, Nothing>() {
        override suspend fun executeAction(action: Unit, getState: () -> State) {
            val items = database.loadItems()
            if (items.isNotEmpty()) {
                dispatch(Result.ItemsLoaded(items))
            } else {
                refresh()
            }
        }

        override suspend fun executeIntent(intent: Intent, getState: () -> State): Unit =
            when (intent) {
                is Intent.Refresh -> if (!getState().isLoading) refresh() else Unit
            }

        private suspend fun refresh() {
            dispatch(Result.LoadingStarted)
            val items = network.loadItems()
            if (items != null) {
                database.saveItems(items)
                dispatch(Result.ItemsLoaded(items))
            } else {
                dispatch(Result.LoadingFailed)
            }
        }
    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                is Result.LoadingStarted -> copy(isLoading = true)
                is Result.ItemsLoaded -> copy(isLoading = false, items = result.items)
                is Result.LoadingFailed -> copy(isLoading = false)
            }
    }

    interface Network {
        suspend fun loadItems(): List<Item>?
    }

    interface Database {
        suspend fun loadItems(): List<Item>
        suspend fun saveItems(items: List<Item>)
    }
}
