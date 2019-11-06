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

import java.net.URI;
import java.net.URISyntaxException;
import java.util.function.Supplier;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.onap.vid.exceptions.GenericUncheckedException;

public class Unchecked {
    private Unchecked() {
        // explicit private constructor, to hide the implicit public constructor
    }

    public static URI toURI(String uri) {
        try {
            // Indulge spaces in the URI by the replcement
            return new URI(uri.replace(" ", "%20"));
        } catch (URISyntaxException e) {
            throw new GenericUncheckedException(e);
        }
    }

    @FunctionalInterface
    public interface UncheckedThrowingSupplier<T> extends Supplier<T> {

        @Override
        default T get() {
            try {
                return getThrows();
            } catch (Exception e) {
                return ExceptionUtils.rethrow(e);
            }
        }

        T getThrows() throws Exception;
    }

}
