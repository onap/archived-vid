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
