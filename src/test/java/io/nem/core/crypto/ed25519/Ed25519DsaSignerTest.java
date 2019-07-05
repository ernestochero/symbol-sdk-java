/*
 * Copyright 2018 NEM
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

package io.nem.core.crypto.ed25519;

import io.nem.core.crypto.CryptoEngine;
import io.nem.core.crypto.CryptoEngines;
import io.nem.core.crypto.CryptoException;
import io.nem.core.crypto.DsaSigner;
import io.nem.core.crypto.DsaSignerTest;
import io.nem.core.crypto.KeyPair;
import io.nem.core.crypto.PublicKey;
import io.nem.core.crypto.Signature;
import io.nem.core.crypto.ed25519.arithmetic.MathUtils;
import io.nem.core.test.Utils;
import java.math.BigInteger;
import org.hamcrest.core.IsEqual;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class Ed25519DsaSignerTest extends DsaSignerTest {

    @Test
    public void isCanonicalReturnsFalseForNonCanonicalSignature() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp = KeyPair.random(engine);
        final DsaSigner dsaSigner = this.getDsaSigner(kp);
        final byte[] input = Utils.generateRandomBytes();

        // Act:
        final Signature signature = dsaSigner.sign(input);
        final BigInteger nonCanonicalS = engine.getCurve().getGroupOrder().add(signature.getS());
        final Signature nonCanonicalSignature = new Signature(signature.getR(), nonCanonicalS);

        // Assert:
        Assert.assertThat(
            dsaSigner.isCanonicalSignature(nonCanonicalSignature), IsEqual.equalTo(false));
    }

    @Test
    public void makeCanonicalMakesNonCanonicalSignatureCanonical() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp = KeyPair.random(engine);
        final DsaSigner dsaSigner = this.getDsaSigner(kp);
        final byte[] input = Utils.generateRandomBytes();

        // Act:
        final Signature signature = dsaSigner.sign(input);
        final BigInteger nonCanonicalS = engine.getCurve().getGroupOrder().add(signature.getS());
        final Signature nonCanonicalSignature = new Signature(signature.getR(), nonCanonicalS);
        Assert.assertThat(
            dsaSigner.isCanonicalSignature(nonCanonicalSignature), IsEqual.equalTo(false));
        final Signature canonicalSignature = dsaSigner
            .makeSignatureCanonical(nonCanonicalSignature);

        // Assert:
        Assert
            .assertThat(dsaSigner.isCanonicalSignature(canonicalSignature), IsEqual.equalTo(true));
    }

    @Test
    public void replacingRWithGroupOrderPlusRInSignatureRuinsSignature() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final BigInteger groupOrder = engine.getCurve().getGroupOrder();
        final KeyPair kp = KeyPair.random(engine);
        final DsaSigner dsaSigner = this.getDsaSigner(kp);
        Signature signature;
        byte[] input;
        while (true) {
            input = Utils.generateRandomBytes();
            signature = dsaSigner.sign(input);
            if (signature.getR().add(groupOrder).compareTo(BigInteger.ONE.shiftLeft(256)) < 0) {
                break;
            }
        }

        // Act:
        final Signature signature2 = new Signature(groupOrder.add(signature.getR()),
            signature.getS());

        // Assert:
        Assert.assertThat(dsaSigner.verify(input, signature2), IsEqual.equalTo(false));
    }

    @Test
    public void signReturnsExpectedSignature() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair keyPair = KeyPair.random(engine);
        for (int i = 0; i < 20; i++) {
            final DsaSigner dsaSigner = this.getDsaSigner(keyPair);
            final byte[] input = Utils.generateRandomBytes();

            // Act:
            final Signature signature1 = dsaSigner.sign(input);
            final Signature signature2 = MathUtils.sign(keyPair, input);

            // Assert:
            Assert.assertThat(signature1, IsEqual.equalTo(signature2));
        }
    }

    @Test
    public void signReturnsVerifiableSignature() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair keyPair = KeyPair.random(engine);
        for (int i = 0; i < 20; i++) {
            final DsaSigner dsaSigner = this.getDsaSigner(keyPair);
            final byte[] input = Utils.generateRandomBytes();

            // Act:
            final Signature signature1 = dsaSigner.sign(input);

            // Assert:
            Assert.assertThat(dsaSigner.verify(input, signature1), IsEqual.equalTo(true));
        }
    }

    @Test(expected = CryptoException.class)
    public void signThrowsIfGeneratedSignatureIsNotCanonical() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair keyPair = KeyPair.random(engine);
        final Ed25519DsaSigner dsaSigner = Mockito.mock(Ed25519DsaSigner.class);
        final byte[] input = Utils.generateRandomBytes();
        Mockito.when(dsaSigner.getKeyPair()).thenReturn(keyPair);
        Mockito.when(dsaSigner.sign(input)).thenCallRealMethod();
        Mockito.when(dsaSigner.isCanonicalSignature(Mockito.any())).thenReturn(false);

        // Act:
        dsaSigner.sign(input);
    }

    @Test
    public void verifyReturnsFalseIfPublicKeyIsZeroArray() {
        // Arrange:
        final CryptoEngine engine = this.getCryptoEngine();
        final KeyPair kp = KeyPair.random(engine);
        final DsaSigner dsaSigner = this.getDsaSigner(kp);
        final byte[] input = Utils.generateRandomBytes();
        final Signature signature = dsaSigner.sign(input);
        final Ed25519DsaSigner dsaSignerWithZeroArrayPublicKey = Mockito
            .mock(Ed25519DsaSigner.class);
        final KeyPair keyPairWithZeroArrayPublicKey = Mockito.mock(KeyPair.class);
        Mockito.when(dsaSignerWithZeroArrayPublicKey.getKeyPair())
            .thenReturn(keyPairWithZeroArrayPublicKey);
        Mockito.when(keyPairWithZeroArrayPublicKey.getPublicKey())
            .thenReturn(new PublicKey(new byte[32]));
        Mockito.when(dsaSignerWithZeroArrayPublicKey.verify(input, signature)).thenCallRealMethod();
        Mockito.when(dsaSignerWithZeroArrayPublicKey.isCanonicalSignature(signature))
            .thenReturn(true);

        // Act:
        final boolean result = dsaSignerWithZeroArrayPublicKey.verify(input, signature);

        // Assert (getKeyPair() would be called more than once if it got beyond the second check):
        Assert.assertThat(result, IsEqual.equalTo(false));
        Mockito.verify(dsaSignerWithZeroArrayPublicKey, Mockito.times(1))
            .isCanonicalSignature(signature);
        Mockito.verify(dsaSignerWithZeroArrayPublicKey, Mockito.times(1)).getKeyPair();
    }

    @Override
    protected CryptoEngine getCryptoEngine() {
        return CryptoEngines.ed25519Engine();
    }
}
