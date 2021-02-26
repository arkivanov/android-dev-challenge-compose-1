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

import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.androiddevchallenge.list.PuppyListStore.Intent
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

@OptIn(ExperimentalCoroutinesApi::class)
class PuppyListStoreTest {

    private val network = PuppyListStoreNetwork()
    private val database = PuppyListStoreDatabase()

    @Before
    fun before() {
        Dispatchers.setMain(TestCoroutineDispatcher())
        isAssertOnMainThreadEnabled = false
    }

    @Test
    fun `GIVEN database empty WHEN store created THEN items loaded from network`() {
        val items = listOf(item(), item(), item())
        network.items = items

        val store = store()

        assertEquals(items, store.state.items)
    }

    @Test
    fun `GIVEN database empty WHEN store created THEN items loaded from network saved to database`() {
        val items = listOf(item(), item(), item())
        network.items = items

        store()

        assertEquals(items, database.items)
    }

    @Test
    fun `GIVEN database not empty WHEN store created THEN items loaded from database`() {
        val items = listOf(item(), item(), item())
        database.items = items

        val store = store()

        assertEquals(items, store.state.items)
    }

    @Test
    fun `GIVEN items loaded WHEN Intent_Refresh THEN new items loaded from network`() {
        database.items = listOf(item())
        val store = store()
        val newItems = listOf(item(), item(), item())
        network.items = newItems

        store.accept(Intent.Refresh)

        assertEquals(newItems, store.state.items)
    }

    @Test
    fun `GIVEN items loaded WHEN Intent_Refresh THEN new items loaded from network saved to database`() {
        database.items = listOf(item())
        val store = store()
        val newItems = listOf(item(), item(), item())
        network.items = newItems

        store.accept(Intent.Refresh)

        assertEquals(newItems, database.items)
    }

    private fun store(): PuppyListStore =
        PuppyListStoreFactory(
            storeFactory = DefaultStoreFactory,
            network = network,
            database = database
        ).invoke()
}
