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
package com.example.androiddevchallenge.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.ContentAlpha
import androidx.compose.material.Icon
import androidx.compose.material.LocalContentAlpha
import androidx.compose.material.MaterialTheme
import androidx.compose.material.ProvideTextStyle
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ListItem(
    text: @Composable () -> Unit,
    icon: @Composable () -> Unit,
    secondaryText: (@Composable () -> Unit)? = null,
    trailing: (@Composable () -> Unit)? = null,
    action: (() -> Unit)? = null
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colors.surface)
            .let { if (action != null) it.clickable(onClick = action) else it }
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .width(40.dp)
                .align(Alignment.CenterVertically)
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier
                .align(Alignment.CenterVertically)
                .weight(1F)
        ) {
            WithTextStyle(
                textStyle = MaterialTheme.typography.subtitle1,
                contentAlpha = ContentAlpha.high,
                content = text
            )

            if (secondaryText != null) {
                WithTextStyle(
                    textStyle = MaterialTheme.typography.caption,
                    contentAlpha = ContentAlpha.medium,
                    content = secondaryText
                )
            }
        }

        if (trailing != null) {
            Box(
                modifier = Modifier
                    .padding(start = 16.dp)
                    .align(Alignment.CenterVertically)
            ) {
                trailing()
            }
        }
    }
}

@Composable
private fun WithTextStyle(
    textStyle: TextStyle,
    contentAlpha: Float,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(LocalContentAlpha provides contentAlpha) {
        ProvideTextStyle(textStyle, content)
    }
}

@Preview
@Composable
private fun ListItemOneLineIconPreview() {
    ListItem(
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        },
        text = { Text(text = "Primary text") }
    )
}

@Preview
@Composable
private fun ListItemOneLineIconTrailingPreview() {
    ListItem(
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        },
        text = { Text(text = "Primary text") },
        trailing = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        }
    )
}

@Preview
@Composable
private fun ListItemTwoLineIconPreview() {
    ListItem(
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        },
        text = { Text(text = "Primary text") },
        secondaryText = { Text(text = "Secondary text") }
    )
}

@Preview
@Composable
private fun ListItemTwoLineIconTrailingPreview() {
    ListItem(
        icon = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        },
        text = { Text(text = "Primary text") },
        secondaryText = { Text(text = "Secondary text") },
        trailing = {
            Icon(
                imageVector = Icons.Default.Delete,
                contentDescription = ""
            )
        }
    )
}
