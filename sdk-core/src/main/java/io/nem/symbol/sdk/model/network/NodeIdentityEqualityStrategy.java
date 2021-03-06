/*
 * Copyright 2020 NEM
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
 */


package io.nem.symbol.sdk.model.network;

/**
 * Node equality strategy. Defines if the identifier for the node must be its public key or host.
 */
public enum NodeIdentityEqualityStrategy {

    HOST("host"),

    PUBLIC_KEY("public-key");

    private String value;

    NodeIdentityEqualityStrategy(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static NodeIdentityEqualityStrategy fromValue(String value) {
        for (NodeIdentityEqualityStrategy b : NodeIdentityEqualityStrategy.values()) {
            if (b.value.equals(value)) {
                return b;
            }
        }
        throw new IllegalArgumentException("Unexpected value '" + value + "'");
    }

    @Override
    public String toString() {
        return value;
    }
}

