/*
 * Copyright 2021 original authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package info.novatec.micronaut.camunda.external.client.feature.test

import info.novatec.micronaut.camunda.external.client.feature.Configuration
import io.micronaut.test.extensions.junit5.annotation.MicronautTest
import org.assertj.core.api.Assertions.*
import org.camunda.bpm.client.ExternalTaskClient
import org.camunda.bpm.client.impl.ExternalTaskClientImpl
import org.junit.jupiter.api.Test
import javax.inject.Inject

/**
 * @author Martin Sawilla
 */
@MicronautTest(propertySources = ["classpath:overwrite.yml"])
class ExternalTaskSubscriptionOverwriteTest {

    @Inject
    lateinit var configuration: Configuration
    @Inject
    lateinit var externalTaskClient: ExternalTaskClient

    @Test
    fun `test that all values are set in configuration`() {
        val subscription = configuration.subscriptions.get()
        assertThat(subscription.size).isEqualTo(2)
        assertThat(subscription[0].topicName).isEqualTo("test-configuration")
        assertThat(subscription[0].lockDuration).isEqualTo(2000)
        assertThat(subscription[0].variables).containsExactly("two", "three")
        assertThat(subscription[0].localVariables).isFalse
        assertThat(subscription[0].businessKey).isEqualTo(null)
    }

    @Test
    fun `test that properties got set`() {
        val client = externalTaskClient as ExternalTaskClientImpl
        val subscriptions = client.topicSubscriptionManager.subscriptions

        assertThat(subscriptions.size).isEqualTo(2)
        assertThat(subscriptions[0].topicName).isEqualTo("test-configuration")
        assertThat(subscriptions[0].lockDuration).isEqualTo(2000)
        assertThat(subscriptions[0].variableNames).containsExactly("two", "three")
        assertThat(subscriptions[0].isLocalVariables).isFalse
    }
}
