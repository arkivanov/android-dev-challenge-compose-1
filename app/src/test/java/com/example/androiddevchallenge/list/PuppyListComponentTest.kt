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

import com.arkivanov.decompose.DefaultComponentContext
import com.arkivanov.decompose.lifecycle.LifecycleRegistry
import com.arkivanov.decompose.lifecycle.resume
import com.arkivanov.mvikotlin.core.utils.isAssertOnMainThreadEnabled
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.androiddevchallenge.database.PuppyDatabase
import com.example.androiddevchallenge.list.PuppyListStore.State.Item
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import io.ktor.http.headersOf
import junit.framework.Assert.assertEquals
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.setMain
import kotlinx.coroutines.withTimeout
import kotlinx.serialization.json.Json
import org.junit.Before
import org.junit.Test

@Suppress("EXPERIMENTAL_API_USAGE")
class PuppyListComponentTest {

    private val driver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY)
    private val database = PuppyDatabase(driver)
    private val queries = database.puppyDatabaseQueries

    @Before
    fun before() {
        isAssertOnMainThreadEnabled = false
        Dispatchers.setMain(TestCoroutineDispatcher())
        PuppyDatabase.Schema.create(driver)
        queries.clear()
    }

    @Test
    fun `GIVEN database empty WHEN component created THEN items loaded from network`() = runTest {
        val items = listOf(item(id = "1"), item(id = "2"), item(id = "3"))

        val component = startComponent(networkItems = items)

        val loadedModel = component.models.first { it.items.isNotEmpty() }
        assertEquals(3, loadedModel.items.size)
    }

    @Test
    fun `GIVEN database not empty WHEN component created THEN items loaded from database`() = runTest {
        val items = listOf(item(id = "1"), item(id = "2"), item(id = "3"))
        var component = startComponent(networkItems = items)
        component.models.first { it.items.isNotEmpty() }

        component = startComponent(networkItems = emptyList())

        val loadedModel = component.models.first { it.items.isNotEmpty() }
        assertEquals(3, loadedModel.items.size)
    }

    private fun startComponent(networkItems: List<Item>): PuppyListComponent {
        val lifecycle = LifecycleRegistry()
        val client = httpClient(networkItems)

        val component =
            PuppyListComponent(
                componentContext = DefaultComponentContext(lifecycle = lifecycle),
                storeFactory = DefaultStoreFactory,
                client = client,
                database = database,
                setDarkMode = {},
                onPuppyActivated = {}
            )

        lifecycle.resume()

        return component
    }

    private fun httpClient(items: List<Item>): HttpClient =
        HttpClient(MockEngine) {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }

            engine {
                addHandler { request ->
                    val url = request.url.toString()
                    when {
                        url.startsWith("https://api.thedogapi.com/v1/images/search") -> {
                            val json = itemsJson(items.map { it.toJson() })
                            respond(
                                content = json,
                                headers = headersOf("content-type", "application/json")
                            )
                        }

                        else -> error("Unhandled url: $url")
                    }
                }
            }
        }

    private fun runTest(block: suspend () -> Unit) {
        runBlocking {
            withTimeout(timeMillis = 5000L) {
                block()
            }
        }
    }

    private fun Item.toJson(): String =
        """
            {
                "breeds": [
                    {
                        "weight": { "metric": "$breedWeight" },
                        "height": { "metric": "$breedHeight" },
                        "name": "$breedName",
                        "bred_for": "",
                        "breed_group": "$breedGroup",
                        "life_span": "$breedLifeSpan",
                        "temperament": "$breedTemperament"
                    }
                ],
                "id": "$id",
                "url": "$imageUrl"
            }
        """.trimIndent()

    private fun itemsJson(items: List<String>): String =
        "[${items.joinToString()}]"
}
