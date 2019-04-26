/*
 * (C) Copyright 2019 Nuxeo (http://nuxeo.com/) and others.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 *     Thomas Roger
 */

package org.nuxeo.ecm.core.io;

import static javax.servlet.http.HttpServletResponse.SC_BAD_REQUEST;

import java.util.Comparator;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.commons.lang3.builder.ToStringBuilder;
import org.nuxeo.ecm.core.api.NuxeoException;

import com.google.common.base.Functions;

/**
 * Represents a REST API version.
 *
 * @since 11.1
 */
public enum APIVersion {

    V1(1), V2(2);

    public static final String API_VERSION_ATTRIBUTE_NAME = "APIVersion";

    public static final Map<Integer, APIVersion> VALID_VERSIONS = Stream.of(
            APIVersion.values()).collect(Collectors.toMap(v -> v.version, Functions.identity()));

    public static final APIVersion LAST_VERSION = Stream.of(APIVersion.values())
                                                        .max(Comparator.comparingInt(v -> v.version))
                                                        .orElseThrow(
                                                                () -> new NuxeoException("No REST API version found"));

    /**
     * Returns the {@code APIVersion} object for given the {@code version}.
     *
     * @throws NuxeoException if the {@code version} is not a valid REST API version.
     */
    public static APIVersion of(int version) {
        APIVersion apiVersion = VALID_VERSIONS.get(version);
        if (apiVersion == null) {
            throw new NuxeoException(
                    String.format("%s is not part of the valid versions: %s", version, VALID_VERSIONS.keySet()),
                    SC_BAD_REQUEST);
        }
        return apiVersion;
    }

    /**
     * Returns the last REST API version.
     */
    public static APIVersion last() {
        return LAST_VERSION;
    }

    protected final int version;

    APIVersion(int version) {
        this.version = version;
    }

    public boolean eq(APIVersion other) {
        return this.version == other.version;
    }

    public boolean neq(APIVersion other) {
        return this.version != other.version;
    }

    public boolean lt(APIVersion other) {
        return this.version < other.version;
    }

    public boolean lte(APIVersion other) {
        return this.version <= other.version;
    }

    public boolean gt(APIVersion other) {
        return this.version > other.version;
    }

    public boolean gte(APIVersion other) {
        return this.version >= other.version;
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
