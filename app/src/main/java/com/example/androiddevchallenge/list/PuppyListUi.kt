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

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.GridCells
import androidx.compose.foundation.lazy.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.LocalContentColor
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DarkMode
import androidx.compose.material.icons.filled.LightMode
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.primarySurface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.list.PuppyList.Model
import com.example.androiddevchallenge.list.PuppyList.Model.Item
import com.example.androiddevchallenge.ui.StatusBarBox
import com.example.androiddevchallenge.ui.ThemedSurface
import com.example.androiddevchallenge.ui.isDarkMode
import com.example.androiddevchallenge.utils.Img
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@Composable
fun PuppyListUi(puppyList: PuppyList) {
    val model by puppyList.models.collectAsState()

    Column {
        StatusBarBox(modifier = Modifier.background(MaterialTheme.colors.primarySurface))

        TopAppBar(
            title = { Text(text = stringResource(id = R.string.app_name)) },
            actions = {
                if (model.isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(48.dp)
                            .padding(12.dp),
                        color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                    )
                } else {
                    IconButton(onClick = puppyList::onRefreshClicked) {
                        Icon(
                            imageVector = Icons.Default.Refresh,
                            contentDescription = "Refresh button"
                        )
                    }
                }

                if (isDarkMode()) {
                    IconButton(onClick = { puppyList.onSetDarkModeClicked(isDarkMode = false) }) {
                        Icon(
                            imageVector = Icons.Default.LightMode,
                            contentDescription = "Dark mode button"
                        )
                    }
                } else {
                    IconButton(onClick = { puppyList.onSetDarkModeClicked(isDarkMode = true) }) {
                        Icon(
                            imageVector = Icons.Default.DarkMode,
                            contentDescription = "Light mode button"
                        )
                    }
                }
            }
        )

        PuppyGrid(
            list = model.items,
            modifier = Modifier.fillMaxWidth(),
            onClick = puppyList::onPuppyClicked
        )
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
private fun PuppyGrid(
    list: List<Item>,
    modifier: Modifier,
    onClick: (id: String) -> Unit
) {
    LazyVerticalGrid(
        cells = GridCells.Adaptive(196.dp),
        modifier = modifier,
        contentPadding = PaddingValues(8.dp),
    ) {
        items(list) { item ->
            Puppy(
                name = item.name,
                age = item.age,
                image = item.image,
                onClick = { onClick(item.id) },
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun Puppy(
    name: String,
    age: String,
    image: Img,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier,
        elevation = 8.dp,
        shape = RoundedCornerShape(size = 16.dp)
    ) {
        Column(modifier = Modifier.clickable(onClick = onClick)) {
            Img(
                image = image,
                contentDescription = "Puppy image",
                modifier = Modifier
                    .fillMaxSize()
                    .aspectRatio(1F),
                contentScale = ContentScale.Crop,
                fadeIn = true
            )

            Column(modifier = Modifier.padding(16.dp)) {
                Text(
                    text = name,
                    style = MaterialTheme.typography.h6
                )

                Text(
                    text = age,
                    style = MaterialTheme.typography.body2,
                    color = LocalContentColor.current.copy(alpha = LocalContentAlpha.current)
                )
            }
        }
    }
}

@Preview
@Composable
private fun PuppyListUiLightPreview() {
    ThemedSurface {
        PuppyListUi(PuppyListPreview())
    }
}

@Preview
@Composable
private fun PuppyListUiDarkPreview() {
    ThemedSurface(darkTheme = true) {
        PuppyListUi(PuppyListPreview())
    }
}

class PuppyListPreview : PuppyList {
    override val models: StateFlow<Model> =
        MutableStateFlow(
            Model(
                isLoading = false,
                items = listOf(
                    item("Annie", "1.5 years"),
                    item("Betsey", "10 months"),
                    item("Barkley", "6 months"),
                    item("Buddy", "3.5 years"),
                    item("Casey", "4 years"),
                    item("Charlie", "1 year")
                )
            )
        )

    override fun onPuppyClicked(id: String) {
    }

    override fun onRefreshClicked() {
    }

    override fun onSetDarkModeClicked(isDarkMode: Boolean) {
    }

    private fun item(name: String, age: String): Item =
        Item(
            id = "0",
            name = name,
            age = age,
            image = Img.Res(id = R.drawable.puppy)
        )
}
