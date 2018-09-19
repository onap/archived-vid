package org.onap.vid.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import static org.assertj.core.api.Java6Assertions.assertThat;
import static org.assertj.core.api.Java6Assertions.assertThatThrownBy;
import static org.mockito.BDDMockito.given;
import static org.mockito.Matchers.anyString;

@RunWith(MockitoJUnitRunner.class)
public class FnAppDoaImplTest {

    private FnAppDoaImpl fnAppDoa;

    @Mock
    private FnAppDbConnection fnAppDbConnection;

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
        given(fnAppDbConnection.getConnection(anyString(), anyString(), anyString())).willReturn(connection);
        fnAppDoa = new FnAppDoaImpl(fnAppDbConnection);
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
    public void getProfileCount_shouldThrow_whenExceptionIsThrown() throws SQLException {
        nokCaseSetup();
        assertThatThrownBy(() -> fnAppDoa.getProfileCount("anyUrl", "anyUsername", "anyPassword"))
                .isInstanceOf(SQLException.class).hasMessage(ERROR_MESSAGE);
    }
}