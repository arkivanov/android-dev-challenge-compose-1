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

import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.androiddevchallenge.details.PuppyDetails.Model
import com.example.androiddevchallenge.ui.DarkMode
import com.example.androiddevchallenge.ui.ThemedSurface
import org.junit.Assert.assertTrue
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Instrumented test, which will execute on an Android device.
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
@Suppress("EXPERIMENTAL_API_USAGE", "TestFunctionName")
@RunWith(AndroidJUnit4::class)
class PuppyDetailsUiTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    @Test
    fun WHEN_screen_started_THEN_title_displayed() {
        start(FakePuppyDetails(model = Model(title = "title")))

        composeTestRule.onNodeWithText("title").assertExists()
    }

    @Test
    fun WHEN_fab_clicked_THEN_callPhoneNumber() {
        var isPhoneNumberClicked = false
        start(FakePuppyDetails(onPhoneNumberClicked = { isPhoneNumberClicked = true }))

        composeTestRule.onNodeWithContentDescription("Adopt icon").performClick()

        assertTrue(isPhoneNumberClicked)
    }

    private fun start(component: PuppyDetails) {
        composeTestRule.setContent {
            DarkMode {
                ThemedSurface {
                    PuppyDetailsUi(component)
                }
            }
        }
    }
}
