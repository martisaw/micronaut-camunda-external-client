/* Copyright 2021 original authors
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

public class TaskSubscription {
    private String topicName;
    private Long lockDuration;
    private String[] variables;
    private Boolean localVariables;
    private String businessKey;
    private String processDefinitionId;
    private String[] processDefinitionIdIn;
    private String processDefinitionKey;
    private String[] processDefinitionKeyIn;
    private String processDefinitionVersionTag;
    private Boolean withoutTenantId;
    private String[] tenantIdIn;
    private Boolean includeExtensionProperties;

    public String getTopicName() {
        return topicName;
    }

    public Long getLockDuration() {
        return lockDuration;
    }

    public String[] getVariables() {
        return variables;
    }

    public Boolean getLocalVariables() {
        return localVariables;
    }

    public String getBusinessKey() {
        return businessKey;
    }

    public String getProcessDefinitionId() {
        return processDefinitionId;
    }

    public String[] getProcessDefinitionIdIn() {
        return processDefinitionIdIn;
    }

    public String getProcessDefinitionKey() {
        return processDefinitionKey;
    }

    public String[] getProcessDefinitionKeyIn() {
        return processDefinitionKeyIn;
    }

    public String getProcessDefinitionVersionTag() {
        return processDefinitionVersionTag;
    }

    public Boolean getWithoutTenantId() {
        return withoutTenantId;
    }

    public String[] getTenantIdIn() {
        return tenantIdIn;
    }

    public Boolean getIncludeExtensionProperties() {
        return includeExtensionProperties;
    }
}
