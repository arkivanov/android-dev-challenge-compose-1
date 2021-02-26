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
package com.example.androiddevchallenge.utils

import com.squareup.sqldelight.Query
import com.squareup.sqldelight.Transacter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

suspend fun <T : Any> Query<T>.suspendAsList(): List<T> =
    withContext(Dispatchers.IO) {
        executeAsList()
    }

suspend fun <T : Any> Query<T>.suspendAsOneOrNull(): T? =
    withContext(Dispatchers.IO) {
        executeAsOne()
    }

suspend fun <T : Transacter> T.suspendTransaction(block: T.() -> Unit) {
    withContext(Dispatchers.IO) {
        transaction {
            block()
        }
    }
}
