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

package org.onap.vid.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;

@RunWith(MockitoJUnitRunner.class)
public class FnAppDoaImplTest {

    private FnAppDoaImpl fnAppDoa;

    @Mock
    private ConnectionFactory connectionFactory;

    @Mock
    private Connection connection;

    @Mock
    private PreparedStatement preparedStatement;

    @Mock
    private ResultSet resultSet;

    private static final String ERROR_MESSAGE = "error message";
    private static final String QUERY = "select count(*) from fn_app";

    @Before
    public void setUp() throws SQLException {
        given(resultSet.next()).willReturn(true);
        given(resultSet.getInt(1)).willReturn(5);
        given(preparedStatement.executeQuery()).willReturn(resultSet);
        given(connectionFactory.getConnection(anyString(), anyString(), anyString())).willReturn(connection);
        fnAppDoa = new FnAppDoaImpl(connectionFactory);
    }

    private void okCaseSetUp() throws SQLException {

        given(connection.prepareStatement(QUERY)).willReturn(preparedStatement);
    }

    private void nokCaseSetup() throws SQLException {
        given(connection.prepareStatement(QUERY)).willThrow(new SQLException(ERROR_MESSAGE));
    }

    @Test
    public void getProfileCount_shouldReturnNumber_whenNoExceptionIsThrown() throws SQLException {
        okCaseSetUp();
        assertThat(fnAppDoa.getProfileCount("anyUrl", "anyUsername", "anyPassword")).isEqualTo(5);
    }

    @Test
    public void getProfileCount_shouldRethrowSQLException() throws SQLException {
        nokCaseSetup();
        assertThatThrownBy(() -> fnAppDoa.getProfileCount("anyUrl", "anyUsername", "anyPassword"))
                .isInstanceOf(SQLException.class).hasMessage(ERROR_MESSAGE);
    }
}
