package org.onap.vid.model;

import java.util.Properties;

public class GitRepositoryState {

    private final String commitId;
    private final String commitMessageShort;
    private final String commitTime;

    public GitRepositoryState(Properties properties) {
        this.commitId = String.valueOf(properties.get("git.commit.id"));
        this.commitMessageShort = String.valueOf(properties.get("git.commit.message.short"));
        this.commitTime = String.valueOf(properties.get("git.commit.time"));
    }

    public String getCommitId() {
        return commitId;
    }

    public String getCommitMessageShort() {
        return commitMessageShort;
    }

    public String getCommitTime() {
        return commitTime;
    }
}
