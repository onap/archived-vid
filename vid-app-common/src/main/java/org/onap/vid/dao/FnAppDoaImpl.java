/*-
 * ============LICENSE_START=======================================================
 * VID
 * ================================================================================
 * Copyright (C) 2017 AT&T Intellectual Property. All rights reserved.
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

import org.onap.portalsdk.core.logging.logic.EELFLoggerDelegate;
import org.springframework.stereotype.Repository;

import java.sql.*;

@Repository
public class FnAppDoaImpl {

    static EELFLoggerDelegate logger = EELFLoggerDelegate.getLogger(FnAppDoaImpl.class);

    public int getProfileCount(String URL, String username, String password) throws SQLException {
        String q = "select count(*) from fn_app";
        int count = 0;
        try (Connection dbc = getConnection(URL, username, password)) {
            try (PreparedStatement pst = dbc.prepareStatement(q); ResultSet rs = pst.executeQuery()) {
                logger.debug(EELFLoggerDelegate.debugLogger, "getConnection:::" + dbc);
                if (rs.next()) {
                    count = rs.getInt(1);
                }
            } catch (Exception ex) {
                if (dbc != null) {
                    dbc.rollback();
                }
                throw ex;
            }
        } catch (Exception ex) {
            logger.error(EELFLoggerDelegate.errorLogger, "Failed to perform health check", ex);
            throw ex;
        }
        logger.debug(EELFLoggerDelegate.debugLogger, "count:::" + count);
        return count;
    }

    private static Connection getConnection(String url, String username, String password) throws SQLException {
        return credentialsExist(url, username, password) ? DriverManager.getConnection(url, username, password) : null;
    }

    private static boolean credentialsExist(String url, String username, String password) {
        return url != null && username != null && password != null;
    }
}
