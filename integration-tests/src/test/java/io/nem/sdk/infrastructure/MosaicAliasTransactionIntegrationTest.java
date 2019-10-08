/*
 * Copyright 2019 NEM
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package io.nem.sdk.infrastructure;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import io.nem.sdk.model.account.Account;
import io.nem.sdk.model.account.AccountInfo;
import io.nem.sdk.model.blockchain.BlockDuration;
import io.nem.sdk.model.mosaic.MosaicFlags;
import io.nem.sdk.model.mosaic.MosaicId;
import io.nem.sdk.model.mosaic.MosaicNames;
import io.nem.sdk.model.mosaic.MosaicNonce;
import io.nem.sdk.model.namespace.AliasAction;
import io.nem.sdk.model.namespace.NamespaceId;
import io.nem.sdk.model.transaction.MosaicAliasTransaction;
import io.nem.sdk.model.transaction.MosaicAliasTransactionFactory;
import io.nem.sdk.model.transaction.MosaicDefinitionTransaction;
import io.nem.sdk.model.transaction.MosaicDefinitionTransactionFactory;
import io.nem.sdk.model.transaction.NamespaceRegistrationTransaction;
import io.nem.sdk.model.transaction.NamespaceRegistrationTransactionFactory;
import java.math.BigInteger;
import java.util.Collections;
import java.util.List;
import org.junit.Assert;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class MosaicAliasTransactionIntegrationTest extends BaseIntegrationTest {


    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    void sendMosaicAliasTransaction(RepositoryType type) throws InterruptedException {
        String namespaceName =
            "test-root-namespace-for-mosaic-alias-" + new Double(Math.floor(Math.random() * 10000))
                .intValue();

        Account account = this.config().getNemesisAccount();
        AccountInfo accountInfo = get(getRepositoryFactory(type).createAccountRepository()
            .getAccountInfo(account.getPublicAccount().getAddress()));

        Assert.assertFalse(
            accountInfo.getMosaics().isEmpty());

        MosaicId mosaicId = createMosaic(type, account);

        NamespaceRegistrationTransaction namespaceRegistrationTransaction =
            NamespaceRegistrationTransactionFactory.createRootNamespace(
                getNetworkType(),
                namespaceName,
                BigInteger.valueOf(100)).build();

        NamespaceId rootNamespaceId = announceAggregateAndValidate(type, account,
            namespaceRegistrationTransaction).getNamespaceId();

        Thread.sleep(1000);

        MosaicAliasTransaction addressAliasTransaction =
            new MosaicAliasTransactionFactory(
                getNetworkType(),
                AliasAction.LINK,
                rootNamespaceId,
                mosaicId).build();

        announceAggregateAndValidate(type, account, addressAliasTransaction);

        Thread.sleep(2000);

        List<MosaicNames> accountNames = get(getRepositoryFactory(type).createMosaicRepository()
            .getMosaicsNames(Collections.singletonList(mosaicId)));

        Assert.assertEquals(1, accountNames.size());

        assertEquals(1, accountNames.size());
        assertEquals(mosaicId, accountNames.get(0).getMosaicId());
        assertTrue(accountNames.get(0).getNames().stream()
            .anyMatch(n -> namespaceName.equals(n.getName())));
    }

    private MosaicId createMosaic(RepositoryType type, Account account) {
        MosaicNonce nonce = MosaicNonce.createRandom();
        MosaicId mosaicId = MosaicId.createFromNonce(nonce, account.getPublicAccount());

        MosaicDefinitionTransaction mosaicDefinitionTransaction =
            new MosaicDefinitionTransactionFactory(getNetworkType(),
                nonce,
                mosaicId,
                MosaicFlags.create(true, true, true),
                4, new BlockDuration(100)).build();

        return announceAndValidate(type, account, mosaicDefinitionTransaction).getMosaicId();
    }
}
