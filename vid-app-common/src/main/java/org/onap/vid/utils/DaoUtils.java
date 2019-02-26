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

package org.onap.vid.utils;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.onap.vid.exceptions.GenericUncheckedException;
import org.onap.portalsdk.core.domain.FusionObject;

import java.util.HashMap;
import java.util.function.Function;

public class DaoUtils {

    //all credit for this wonderful method go to is9613
    public static<T> T tryWithSessionAndTransaction(SessionFactory sessionFactory, Function<Session, T> update) {
        // opens a session and transactions, executes the input query.
        // gracefully close session and transaction once error occurres.
        Session session = null;
        Transaction tx = null;
        try {
            session = sessionFactory.openSession();
            tx = session.beginTransaction();

            T res = update.apply(session);

            tx.commit();

            return res;
        } catch (RuntimeException e) {
            try {
                if (tx != null) {
                    tx.rollback();
                }
            } catch (RuntimeException e2) {
                // e2 is ingnored; we would like to know the
                // original failure reason, not only the reason
                // for rollback's failure
                throw new GenericUncheckedException("Failed rolling back transaction", e);
            }
            throw new GenericUncheckedException("Rolled back transaction", e);
        } finally {
            if (session != null) {
                session.close();
            }
        }
    }

    public static HashMap<String, Object> getPropsMap() {
        HashMap<String, Object> props = new HashMap<>();
        props.put(FusionObject.Parameters.PARAM_USERID, 0);
        return props;
    }
}
