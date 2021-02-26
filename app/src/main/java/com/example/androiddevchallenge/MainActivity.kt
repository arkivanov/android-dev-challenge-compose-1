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
package com.example.androiddevchallenge

import android.app.SearchManager
import android.content.ActivityNotFoundException
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import com.arkivanov.decompose.extensions.compose.jetpack.rememberRootComponent
import com.arkivanov.mvikotlin.main.store.DefaultStoreFactory
import com.example.androiddevchallenge.database.PuppyDatabase
import com.example.androiddevchallenge.root.RootComponent
import com.example.androiddevchallenge.root.RootUi
import com.example.androiddevchallenge.ui.DarkMode
import com.example.androiddevchallenge.ui.ThemedSurface
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.HttpClient
import io.ktor.client.features.json.JsonFeature
import io.ktor.client.features.json.serializer.KotlinxSerializer
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val client = httpClient()
        val database = PuppyDatabase(AndroidSqliteDriver(PuppyDatabase.Schema, this))

        setContent {
            val isSystemInDarkMode = isSystemInDarkTheme()
            var isDarkMode by remember { mutableStateOf(isSystemInDarkMode) }

            DarkMode(isDarkMode = isDarkMode) {
                ThemedSurface {
                    RootUi(
                        rememberRootComponent { componentContext ->
                            RootComponent(
                                componentContext = componentContext,
                                storeFactory = DefaultStoreFactory,
                                client = client,
                                database = database,
                                searchInternet = ::searchInternet,
                                callPhoneNumber = ::callPhoneNumber,
                                setDarkMode = { isDarkMode = it }
                            )
                        }
                    )
                }
            }
        }
    }

    private fun httpClient(): HttpClient =
        HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(
                    Json {
                        isLenient = true
                        ignoreUnknownKeys = true
                    }
                )
            }
        }

    private fun searchInternet(query: String) {
        try {
            startActivity(
                Intent(Intent.ACTION_WEB_SEARCH)
                    .putExtra(SearchManager.QUERY, query)
            )
        } catch (ignored: ActivityNotFoundException) {
            Toast.makeText(this, R.string.error_search, Toast.LENGTH_LONG).show()
        }
    }

    private fun callPhoneNumber(phoneNumber: String) {
        try {
            startActivity(Intent(Intent.ACTION_DIAL, Uri.parse("tel:$phoneNumber")))
        } catch (e: ActivityNotFoundException) {
            Toast.makeText(this, getString(R.string.error_making_call, phoneNumber), Toast.LENGTH_LONG).show()
        }
    }
}
