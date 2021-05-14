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
package info.novatec.micronaut.camunda.external.client.feature;

import io.micronaut.context.BeanContext;
import io.micronaut.context.annotation.Context;
import io.micronaut.core.annotation.AnnotationValue;
import io.micronaut.inject.BeanDefinition;
import org.camunda.bpm.client.ExternalTaskClient;
import org.camunda.bpm.client.task.ExternalTaskHandler;
import org.camunda.bpm.client.topic.TopicSubscription;
import org.camunda.bpm.client.topic.TopicSubscriptionBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

import static java.util.Collections.*;

/**
 * @author Martin Sawilla
 *
 * Allows to configure an external task worker with the {@link ExternalTaskSubscription} annotation. This allows to easily build
 * external workers for multiple topics.
 */
@Context
public class ExternalWorkerSubscriptionCreator {

    private static final Logger log = LoggerFactory.getLogger(ExternalWorkerSubscriptionCreator.class);

    protected final BeanContext beanContext;
    protected final ExternalTaskClient externalTaskClient;
    protected final Configuration configuration;
    protected List<TaskSubscription> subscriptions = emptyList();

    public ExternalWorkerSubscriptionCreator(BeanContext beanContext,
                                             ExternalTaskClient externalTaskClient,
                                             Configuration configuration) {
        this.beanContext = beanContext;
        this.externalTaskClient = externalTaskClient;
        this.configuration = configuration;

        if (configuration.getSubscriptions().isPresent()) {
            this.subscriptions = configuration.getSubscriptions().get();
        }

        beanContext.getBeanDefinitions(ExternalTaskHandler.class).forEach(this::registerExternalTaskHandler);
    }

    protected void registerExternalTaskHandler(BeanDefinition<ExternalTaskHandler> beanDefinition) {
        ExternalTaskHandler externalTaskHandler = beanContext.getBean(beanDefinition);
        AnnotationValue<ExternalTaskSubscription> annotationValue = beanDefinition.getAnnotation(ExternalTaskSubscription.class);

        if (annotationValue != null) {

            TopicSubscriptionBuilder builder = createTopicSubscription(externalTaskHandler, externalTaskClient, annotationValue);

            subscriptions.forEach(
                    it -> {
                        if(it.getTopicName().equals(annotationValue.stringValue("topicName").get())) {
                            overwriteTopicSubscription(builder, it);
                        }
                    }
            );

            TopicSubscription topicSubscription = builder.open();

            log.info("External task client subscribed to topic '{}'", topicSubscription.getTopicName());

        } else {
            log.warn("Skipping subscription. Could not find Annotation ExternalTaskSubscription on class {}", beanDefinition.getName());
        }
    }

    protected TopicSubscriptionBuilder createTopicSubscription(ExternalTaskHandler externalTaskHandler, ExternalTaskClient client, AnnotationValue<ExternalTaskSubscription> annotationValue) {

        //noinspection OptionalGetWithoutIsPresent
        TopicSubscriptionBuilder builder = client.subscribe(annotationValue.stringValue("topicName").get());

        builder.handler(externalTaskHandler);

        annotationValue.longValue("lockDuration").ifPresent(builder::lockDuration);

        annotationValue.get("variables", String[].class).ifPresent(it -> {
            if(!it[0].equals("")){
                builder.variables(it);
            }
        });

        annotationValue.booleanValue("localVariables").ifPresent(builder::localVariables);

        annotationValue.stringValue("businessKey").ifPresent(builder::businessKey);

        annotationValue.stringValue("processDefinitionId").ifPresent(builder::processDefinitionId);

        annotationValue.get("processDefinitionIdIn", String[].class).ifPresent(it -> {
            if(!it[0].equals("")){
                builder.processDefinitionIdIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionKey").ifPresent(builder::processDefinitionKey);

        annotationValue.get("processDefinitionKeyIn", String[].class).ifPresent(it -> {
            if(!it[0].equals("")) {
                builder.processDefinitionKeyIn(it);
            }
        });

        annotationValue.stringValue("processDefinitionVersionTag").ifPresent(builder::processDefinitionVersionTag);

        annotationValue.booleanValue("withoutTenantId").ifPresent(it -> {
            if (it) {
                builder.withoutTenantId();
            }
        });

        annotationValue.get("tenantIdIn", String[].class).ifPresent(it -> {
            if(!it[0].equals("")) {
                builder.tenantIdIn(it);
            }
        });

        annotationValue.booleanValue("includeExtensionProperties").ifPresent(builder::includeExtensionProperties);

        return builder;

    }

    protected void overwriteTopicSubscription(TopicSubscriptionBuilder builder, TaskSubscription taskSubscription) {

        if(taskSubscription.getLockDuration() != null) {
            builder.lockDuration(taskSubscription.getLockDuration());
        }

        if(taskSubscription.getVariables() != null) {
            builder.variables(taskSubscription.getVariables());
        }

        if(taskSubscription.getLocalVariables() != null) {
            builder.localVariables(taskSubscription.getLocalVariables());
        }

        if(taskSubscription.getBusinessKey() != null) {
            builder.businessKey(taskSubscription.getBusinessKey());
        }

        if(taskSubscription.getProcessDefinitionId() != null) {
            builder.processDefinitionId(taskSubscription.getProcessDefinitionId());
        }

        if(taskSubscription.getProcessDefinitionIdIn() != null) {
            builder.processDefinitionIdIn(taskSubscription.getProcessDefinitionIdIn());
        }

        if(taskSubscription.getProcessDefinitionKey() != null) {
            builder.processDefinitionKey(taskSubscription.getProcessDefinitionKey());
        }

        if(taskSubscription.getProcessDefinitionKeyIn() != null) {
            builder.processDefinitionKeyIn(taskSubscription.getProcessDefinitionKeyIn());
        }

        if(taskSubscription.getProcessDefinitionVersionTag() != null) {
            builder.processDefinitionVersionTag(taskSubscription.getProcessDefinitionVersionTag());
        }

        if(taskSubscription.getWithoutTenantId() != null && taskSubscription.getWithoutTenantId()) {
            builder.withoutTenantId();
        }

        if(taskSubscription.getTenantIdIn() != null) {
            builder.tenantIdIn(taskSubscription.getTenantIdIn());
        }

        if(taskSubscription.getIncludeExtensionProperties() != null) {
            builder.includeExtensionProperties(taskSubscription.getIncludeExtensionProperties());
        }
    }
}
