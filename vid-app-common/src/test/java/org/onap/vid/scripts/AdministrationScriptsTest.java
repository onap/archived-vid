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

package org.onap.vid.scripts;


import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.withHeader;
import static com.xebialabs.restito.semantics.Condition.withPostBodyContaining;
import static java.nio.file.StandardCopyOption.COPY_ATTRIBUTES;
import static java.nio.file.StandardCopyOption.REPLACE_EXISTING;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.common.collect.ImmutableList;
import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.Test;

public class AdministrationScriptsTest {

    @Test
    public void categoryParameterSh_addReleaseCategory_PostAsExpected()
        throws IOException, InterruptedException, URISyntaxException {
        final StubServer stubServer = new StubServer();
        final URI scriptSource = AdministrationScriptsTest.class.getResource("/scripts/category_parameter.sh").toURI();

        try {

            stubServer.start();

            Condition[] stubbedInterface = {
                post("/vid/maintenance/category_parameter/release"),
                withHeader("Content-Type", "application/json"),
                withPostBodyContaining(asSpacedRegex("{ \"options\" : [ \"2019.08\" ] }"))
            };

            String mockedResponse = "{\"errors\":[]}";
            whenHttp(stubServer).match(stubbedInterface)
                .then(ok(), stringContent(mockedResponse));

            Path script = Files.createTempFile("script", ".sh");
            Files.copy(Paths.get(scriptSource), script, COPY_ATTRIBUTES, REPLACE_EXISTING);

            Path input = Files.createTempFile("input", ".txt");
            Files.write(input, ImmutableList.of("2019.08"));

            Pair<Integer, String> result = doRun(
                "sed -i s/8080/" + stubServer.getPort() + "/g " + path(script),
                path(script) + " -o ADD -p "+ path(input) +" -c release"
            );


            assertThat("output is descriptive", result.getRight(), allOf(
                    containsString("Sending request:"),
                    containsString("wget output:"),
                    containsString(mockedResponse)
                ));
            assertThat("error code is 0",  result.getLeft(), is(0));
            verifyHttp(stubServer).once(stubbedInterface);

        } finally {
            stubServer.stop();
        }
    }

    private String path(Path filePath) {
        return filePath.toUri().getPath();
    }

    private Pattern asSpacedRegex(String string) {
        final String spaces = "\\s*";
        return Pattern.compile(""
            + spaces
            + string
                .replaceAll("([\\[\\]{}.])", "\\\\$0") // escape special
                .replace(" ", spaces) // allow 0 or more spaces anywhere
            + spaces
        );
    }

    private Pair<Integer, String> doRun(String... cmds) throws IOException, InterruptedException {
        String cmd = "" + String.join(" && ", cmds) + "";
        Process process = Runtime.getRuntime().exec(new String[]{"sh", "-c", cmd});

        int exitCode = process.waitFor();
        String output = IOUtils.toString(
            exitCode == 0 ? process.getInputStream() : process.getErrorStream(), Charset.defaultCharset());

        return ImmutablePair.of(exitCode, cmd + ": " + output);
    }

}
