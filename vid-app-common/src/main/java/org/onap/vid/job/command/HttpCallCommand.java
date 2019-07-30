/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 - 2019 AT&T Intellectual Property. All rights reserved.
 * ================================================================================
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * ============LICENSE_END=========================================================
 */

package org.onap.vid.job.command;

import com.google.common.collect.ImmutableMap;
import org.onap.vid.job.Job;
import org.onap.vid.job.JobCommand;
import org.onap.vid.job.NextCommand;
import org.onap.vid.job.impl.JobSharedData;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import javax.ws.rs.client.ClientBuilder;
import javax.ws.rs.client.Entity;
import java.util.Map;
import java.util.UUID;


@Component
@Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
public class HttpCallCommand implements JobCommand {
    private String url;
    private UUID uuid;

    public HttpCallCommand() {
    }

    public HttpCallCommand(String url, UUID uuid) {
        init(url, uuid);
    }

    @Override
    public NextCommand call() {
        ClientBuilder.newClient().target(url).request().post(Entity.text(uuid.toString()));
        return new NextCommand(Job.JobStatus.COMPLETED);
    }

    @Override
    public HttpCallCommand init(JobSharedData sharedData, Map<String, Object> commandData) {
        return init((String) commandData.get("url"), sharedData.getJobUuid());
    }

    private HttpCallCommand init(String url, UUID jobUuid) {
        this.url = url;
        this.uuid = jobUuid;
        return this;
    }

    @Override
    public Map<String, Object> getData() {
        return ImmutableMap.of("url", url);
    }
}
