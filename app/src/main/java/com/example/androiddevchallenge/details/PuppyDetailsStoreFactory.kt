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

import com.arkivanov.mvikotlin.core.store.Reducer
import com.arkivanov.mvikotlin.core.store.SimpleBootstrapper
import com.arkivanov.mvikotlin.core.store.Store
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.arkivanov.mvikotlin.extensions.coroutines.SuspendExecutor
import com.example.androiddevchallenge.details.PuppyDetailsStore.State
import com.example.androiddevchallenge.details.PuppyDetailsStore.State.Details

internal class PuppyDetailsStoreFactory(
    private val storeFactory: StoreFactory,
    private val puppyId: String,
    private val database: Database
) : () -> PuppyDetailsStore {

    override fun invoke(): PuppyDetailsStore =
        object :
            PuppyDetailsStore,
            Store<Nothing, State, Nothing> by storeFactory.create(
                name = "PuppyDetailsStore",
                initialState = State(),
                bootstrapper = SimpleBootstrapper(Unit),
                executorFactory = ::ExecutorImpl,
                reducer = ReducerImpl
            ) {}

    private sealed class Result {
        data class DetailsLoaded(val details: Details) : Result()
    }

    private inner class ExecutorImpl : SuspendExecutor<Nothing, Unit, State, Result, Nothing>() {
        override suspend fun executeAction(action: Unit, getState: () -> State) {
            val details = database.load(id = puppyId) ?: return
            dispatch(Result.DetailsLoaded(details))
        }
    }

    private object ReducerImpl : Reducer<State, Result> {
        override fun State.reduce(result: Result): State =
            when (result) {
                is Result.DetailsLoaded -> copy(details = result.details)
            }
    }

    interface Database {
        suspend fun load(id: String): Details?
    }
}
