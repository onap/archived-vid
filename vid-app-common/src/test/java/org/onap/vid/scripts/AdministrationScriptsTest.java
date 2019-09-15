package org.onap.vid.scripts;


import static com.xebialabs.restito.builder.stub.StubHttp.whenHttp;
import static com.xebialabs.restito.builder.verify.VerifyHttp.verifyHttp;
import static com.xebialabs.restito.semantics.Action.ok;
import static com.xebialabs.restito.semantics.Action.stringContent;
import static com.xebialabs.restito.semantics.Condition.post;
import static com.xebialabs.restito.semantics.Condition.withHeader;
import static com.xebialabs.restito.semantics.Condition.withPostBodyContaining;
import static org.hamcrest.CoreMatchers.both;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.xebialabs.restito.semantics.Condition;
import com.xebialabs.restito.server.StubServer;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.regex.Pattern;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.testng.annotations.Test;

public class AdministrationScriptsTest {

    @Test
    public void categoryParameterSh_addReleaseCategory_PostAsExpected() throws IOException, InterruptedException {
        final StubServer stubServer = new StubServer();
        String scriptFileName = AdministrationScriptsTest.class.getResource("/scripts/category_parameter.sh").getPath();

        try {

            stubServer.start();

            Condition[] stubbedInterface = {
                post("/vid/maintenance/category_parameter/release"),
                withHeader("Content-Type", "application/json"),
                withPostBodyContaining(asSpacedRegex("{ \"options\" : [ \"2019.08\" ] }"))
            };

            whenHttp(stubServer).match(stubbedInterface)
                .then(ok(), stringContent("{\"errors\":[]}"));


            Pair<Integer, String> result = doRun(
                // move aside and set port
                "cp " + scriptFileName + " ./tmp.sh ",
                "sed -i 's/8080/" + stubServer.getPort() + "/g' ./tmp.sh ",
                // run with input file
                "echo 2019.08 > ./input.tmp",
                "./tmp.sh -o ADD -p ./input.tmp -c release"
            );

            verifyHttp(stubServer).once(stubbedInterface);
            assertThat("error code is 0",  result.getLeft(), is(0));
            assertThat("output is descriptive", result.getRight(),
                both(containsString("Sending request:")).and(containsString("wget output:")));

        } finally {
            stubServer.stop();
        }
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
        Process process = Runtime.getRuntime().exec("sh -c \"" + String.join(" && ", cmds) + "\"");

        int exitCode = process.waitFor();
        String output = IOUtils.toString(
            exitCode == 0 ? process.getInputStream() : process.getErrorStream(), Charset.defaultCharset());

        return ImmutablePair.of(exitCode, output);
    }

}
