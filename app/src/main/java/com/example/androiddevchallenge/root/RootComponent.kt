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
package com.example.androiddevchallenge.root

import android.os.Parcelable
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.Router
import com.arkivanov.decompose.RouterState
import com.arkivanov.decompose.pop
import com.arkivanov.decompose.push
import com.arkivanov.decompose.router
import com.arkivanov.decompose.value.Value
import com.arkivanov.mvikotlin.core.store.StoreFactory
import com.example.androiddevchallenge.database.PuppyDatabase
import com.example.androiddevchallenge.details.PuppyDetailsComponent
import com.example.androiddevchallenge.details.PuppyDetailsUi
import com.example.androiddevchallenge.list.PuppyListComponent
import com.example.androiddevchallenge.list.PuppyListUi
import com.example.androiddevchallenge.photo.PuppyPhotoComponent
import com.example.androiddevchallenge.photo.PuppyPhotoUi
import com.example.androiddevchallenge.utils.Content
import com.example.androiddevchallenge.utils.asContent
import io.ktor.client.HttpClient
import kotlinx.parcelize.Parcelize

class RootComponent(
    componentContext: ComponentContext,
    private val storeFactory: StoreFactory,
    private val client: HttpClient,
    private val database: PuppyDatabase,
    private val searchInternet: (String) -> Unit,
    private val callPhoneNumber: (puppyId: String) -> Unit,
    private val setDarkMode: (Boolean) -> Unit
) : Root, ComponentContext by componentContext {

    private val router: Router<Screen, () -> Unit> =
        router(
            initialConfiguration = Screen.List,
            handleBackButton = true,
            componentFactory = ::createChild
        )

    override val routerState: Value<RouterState<*, Content>> = router.state

    private fun createChild(screen: Screen, componentContext: ComponentContext): Content =
        when (screen) {
            is Screen.List -> list(componentContext)
            is Screen.Details -> details(componentContext, puppyId = screen.puppyId)
            is Screen.Photo -> photo(imageUrl = screen.imageUrl)
        }

    private fun list(componentContext: ComponentContext): Content =
        PuppyListComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            client = client,
            database = database,
            setDarkMode = setDarkMode,
            onPuppyActivated = { id -> router.push(Screen.Details(puppyId = id)) }
        ).asContent { PuppyListUi(it) }

    private fun details(componentContext: ComponentContext, puppyId: String): Content =
        PuppyDetailsComponent(
            componentContext = componentContext,
            storeFactory = storeFactory,
            puppyId = puppyId,
            database = database,
            searchInternet = searchInternet,
            callPhoneNumber = callPhoneNumber,
            onFinished = router::pop,
            onPhotoActivated = { router.push(Screen.Photo(imageUrl = it)) }
        ).asContent { PuppyDetailsUi(it) }

    private fun photo(imageUrl: String): Content =
        PuppyPhotoComponent(
            imageUrl = imageUrl,
            onFinished = router::pop
        ).asContent { PuppyPhotoUi(it) }

    private sealed class Screen : Parcelable {
        @Parcelize
        object List : Screen()

        @Parcelize
        data class Details(val puppyId: String) : Screen()

        @Parcelize
        data class Photo(val imageUrl: String) : Screen()
    }
}
