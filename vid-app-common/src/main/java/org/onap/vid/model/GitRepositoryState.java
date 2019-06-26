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

package org.onap.vid.model;

import java.util.Properties;

public class GitRepositoryState {

	public static final GitRepositoryState EMPTY = new GitRepositoryState("", "", "");

	private final String commitId;
	private final String commitMessageShort;
	private final String commitTime;

	public GitRepositoryState(Properties properties) {
		this(String.valueOf(String.valueOf(properties.get("git.commit.id"))),
				String.valueOf(properties.get("git.commit.message.short")),
				String.valueOf(properties.get("git.commit.time"))
		);
	}

	private GitRepositoryState(String commitId, String commitMessageShort, String commitTime) {
		this.commitId = commitId;
		this.commitMessageShort = commitMessageShort;
		this.commitTime = commitTime;
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
