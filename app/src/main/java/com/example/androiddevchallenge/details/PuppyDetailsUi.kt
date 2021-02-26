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

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Height
import androidx.compose.material.icons.filled.Label
import androidx.compose.material.icons.filled.OpenInBrowser
import androidx.compose.material.icons.filled.Pets
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.androiddevchallenge.R
import com.example.androiddevchallenge.details.PuppyDetails.Model
import com.example.androiddevchallenge.details.PuppyDetails.Model.DetailsItem
import com.example.androiddevchallenge.ui.ListItem
import com.example.androiddevchallenge.ui.StatusBarBox
import com.example.androiddevchallenge.ui.ThemedSurface
import com.example.androiddevchallenge.utils.Img
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

@OptIn(ExperimentalMaterialApi::class)
@Composable
fun PuppyDetailsUi(puppyDetails: PuppyDetails) {
    val model by puppyDetails.models.collectAsState()

    Scaffold(
        topBar = {
            PuppyHeader(
                image = model.image,
                title = model.title,
                onBackClick = puppyDetails::onCloseClicked,
                onImageClick = puppyDetails::onPhotoClicked
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = puppyDetails::onPhoneNumberClicked) {
                Icon(
                    imageVector = Icons.Default.Pets,
                    contentDescription = "Adopt icon"
                )
            }
        },
        content = {
            PuppyDetails(
                items = model.detailItems,
                onSearchBreedClick = puppyDetails::onBreedNameClicked
            )
        }
    )
}

@Composable
private fun PuppyHeader(
    image: Img?,
    title: String,
    onBackClick: () -> Unit,
    onImageClick: () -> Unit
) {
    Box {
        PuppyImage(
            image = image,
            action = onImageClick
        )

        PuppyTitle(
            title = title,
            modifier = Modifier.align(Alignment.BottomStart)
        )

        Column(modifier = Modifier.align(Alignment.TopCenter)) {
            StatusBarBox()
            TopAppBar(onBackClick = onBackClick)
        }
    }
}

@Composable
private fun TopAppBar(onBackClick: () -> Unit) {
    TopAppBar(
        title = {},
        backgroundColor = Color.Transparent,
        elevation = 0.dp,
        navigationIcon = {
            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .background(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color.Black.copy(alpha = 0.3F),
                                Color.Black.copy(alpha = 0.3F),
                                Color.Transparent
                            )
                        ),
                        shape = CircleShape
                    )
            ) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back button",
                    tint = Color.White
                )
            }
        }
    )
}

@Composable
private fun PuppyImage(
    image: Img?,
    action: () -> Unit
) {
    Img(
        image = image,
        contentDescription = "Puppy image",
        modifier = Modifier
            .fillMaxWidth()
            .height(256.dp)
            .clickable(onClick = action),
        contentScale = ContentScale.Crop
    )
}

@Composable
private fun PuppyTitle(title: String, modifier: Modifier) {
    Text(
        text = title,
        modifier = modifier
            .fillMaxWidth()
            .background(
                brush = Brush.verticalGradient(
                    colors = listOf(
                        Color.Transparent,
                        Color.Black.copy(alpha = 0.5F),
                        Color.Black.copy(alpha = 0.5F)
                    )
                )
            )
            .padding(start = 88.dp, bottom = 24.dp, top = 24.dp),
        style = MaterialTheme.typography.h4,
        color = Color.White
    )
}

@Composable
private fun PuppyDetails(
    items: List<DetailsItem>,
    onSearchBreedClick: () -> Unit
) {
    LazyColumn {
        items(items = items) { item ->
            PuppyItem(
                icon = {
                    Icon(
                        imageVector = item.icon.imageVector,
                        contentDescription = "Breed icon"
                    )
                },
                title = stringResource(item.titleRes),
                text = item.text,
                trailingIcon = item.trailingIcon?.let {
                    {
                        Icon(
                            imageVector = it.imageVector,
                            contentDescription = it.contentDescription
                        )
                    }
                },
                action = item.action?.let {
                    when (it) {
                        Model.DetailsAction.SEARCH_BREED -> onSearchBreedClick
                    }
                }
            )
        }
    }
}

@Composable
private fun PuppyItem(
    icon: @Composable () -> Unit,
    title: String,
    text: String?,
    trailingIcon: (@Composable () -> Unit)?,
    action: (() -> Unit)?
) {
    ListItem(
        icon = icon,
        text = { Text(text = title) },
        secondaryText = text?.let { { Text(text = it) } },
        trailing = trailingIcon,
        action = action
    )
}

@Preview
@Composable
private fun PuppyDetailsUiLightPreview() {
    ThemedSurface {
        PuppyDetailsUi(PuppyDetailsPreview())
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFFFF)
@Composable
private fun PuppyDetailsUiDarkPreview() {
    ThemedSurface(darkTheme = true) {
        PuppyDetailsUi(PuppyDetailsPreview())
    }
}

class PuppyDetailsPreview : PuppyDetails {
    override val models: StateFlow<Model> =
        MutableStateFlow(
            Model(
                title = "Charlie, 8 months",
                image = Img.Res(id = R.drawable.puppy),
                detailItems = listOf(
                    DetailsItem(
                        icon = Model.DetailsIcon(
                            imageVector = Icons.Default.Label,
                            contentDescription = ""
                        ),
                        titleRes = R.string.breed,
                        text = "Breed Name",
                        trailingIcon = Model.DetailsIcon(
                            imageVector = Icons.Default.OpenInBrowser,
                            contentDescription = ""
                        )
                    ),
                    DetailsItem(
                        icon = Model.DetailsIcon(
                            imageVector = Icons.Default.Height,
                            contentDescription = ""
                        ),
                        titleRes = R.string.typical_height,
                        text = "40 - 50 cm"
                    )
                )
            )
        )

    override fun onCloseClicked() {
    }

    override fun onPhotoClicked() {
    }

    override fun onBreedNameClicked() {
    }

    override fun onPhoneNumberClicked() {
    }
}
