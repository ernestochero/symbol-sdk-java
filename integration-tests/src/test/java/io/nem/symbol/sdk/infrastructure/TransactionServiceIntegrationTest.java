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

package io.nem.symbol.sdk.infrastructure;

import io.nem.symbol.sdk.api.RepositoryFactory;
import io.nem.symbol.sdk.model.account.Account;
import io.nem.symbol.sdk.model.account.AccountInfo;
import io.nem.symbol.sdk.model.account.Address;
import io.nem.symbol.sdk.model.message.PlainMessage;
import io.nem.symbol.sdk.model.mosaic.Mosaic;
import io.nem.symbol.sdk.model.mosaic.MosaicId;
import io.nem.symbol.sdk.model.mosaic.MosaicInfo;
import io.nem.symbol.sdk.model.namespace.NamespaceId;
import io.nem.symbol.sdk.model.namespace.NamespaceInfo;
import io.nem.symbol.sdk.model.transaction.AggregateTransaction;
import io.nem.symbol.sdk.model.transaction.Transaction;
import io.nem.symbol.sdk.model.transaction.TransferTransaction;
import io.nem.symbol.sdk.model.transaction.TransferTransactionFactory;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.lang3.tuple.Pair;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class TransactionServiceIntegrationTest extends BaseIntegrationTest {

    String mosaicAlias = "transaction-service-mosaic-test-5";

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    public void testTransferCatCurrencyFromNemesis(RepositoryType type) {
        String hash = transferUsingAliases(config().getNemesisAccount(), type, "cat.currency",
            "testaccount2", BigInteger.TEN).getTransactionInfo().get().getHash().get();

        List<Transaction> transactions = get(
            getTransactionService(type).resolveAliases(Collections.singletonList(hash)));

        Assertions.assertEquals(1, transactions.size());

        TransferTransaction resolvedTransaction = (TransferTransaction) transactions.get(0);
        System.out.println(toJson(resolvedTransaction));

        Assertions.assertEquals(config().getTestAccount2().getAddress(),
            resolvedTransaction.getRecipient());

        System.out.println(resolvedTransaction.getMosaics().get(0).getId());

        Assertions.assertTrue(resolvedTransaction.getMosaics().get(0).getId() instanceof MosaicId);
        Assertions.assertTrue(resolvedTransaction.getRecipient() instanceof Address);

    }


    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    public void testTransferCustomCurrencyFromAccount1(RepositoryType type) {

        MosaicId mosaicId = createMosaicUsingAlias(type, mosaicAlias);

        String transferTransactionHash = transferUsingAliases(
            config().getNemesisAccount(), type, mosaicAlias,
            "testaccount2", BigInteger.ONE).getTransactionInfo().get().getHash().get();

        List<Transaction> transactions = get(
            getTransactionService(type)
                .resolveAliases(Arrays.asList(transferTransactionHash)));

        Assertions.assertEquals(1, transactions.size());
        TransferTransaction resolvedTransaction = (TransferTransaction) transactions.get(0);
        assertTransaction(mosaicId, resolvedTransaction);


    }

    @ParameterizedTest
    @EnumSource(RepositoryType.class)
    public void testTransferCustomCurrencyFromAccount1UsingAggregate(RepositoryType type) {

        MosaicId mosaicId = createMosaicUsingAlias(type, mosaicAlias);

        String aggregateTransactionHash = transferUsingAliasesAggregate(
            config().getNemesisAccount(), type, mosaicAlias,
            "testaccount2", BigInteger.ONE).getTransactionInfo().get().getHash().get();

        List<Transaction> transactions = get(
            getTransactionService(type)
                .resolveAliases(Collections.singletonList(aggregateTransactionHash)));

        Assertions.assertEquals(1, transactions.size());
        TransferTransaction resolvedTransaction = (TransferTransaction) ((AggregateTransaction) transactions
            .get(0)).getInnerTransactions().get(0);
        assertTransaction(mosaicId, resolvedTransaction);


    }

    private MosaicId createMosaicUsingAlias(RepositoryType type, String mosaicAlias) {
        NamespaceId mosaicNamespace = NamespaceId.createFromName(mosaicAlias);
        NamespaceInfo namespaceInfo = null;
        MosaicId mosaicId = null;
        RepositoryFactory repositoryFactory = getRepositoryFactory(type);
        try {
            namespaceInfo = get(
                repositoryFactory.createNamespaceRepository()
                    .getNamespace(mosaicNamespace));
            System.out.println("Mosaic found!");
            mosaicId = get(
                repositoryFactory.createNamespaceRepository().getLinkedMosaicId(mosaicNamespace));
            System.out.println("Mosaic id: " + mosaicId.getIdAsHex());

            MosaicInfo mosaicInfo = get(
                repositoryFactory.createMosaicRepository().getMosaic(mosaicId));

            System.out.println("Supply: " + mosaicInfo.getSupply());

        } catch (Exception e) {

        }

        System.out.println("Mosaic Alias: " + mosaicAlias);
        if (namespaceInfo == null) {

            System.out.println("Creating mosaic!");

            Account account = this.config().getDefaultAccount();
            AccountInfo accountInfo = get(repositoryFactory.createAccountRepository()
                .getAccountInfo(account.getPublicAccount().getAddress()));

            Assertions.assertFalse(
                accountInfo.getMosaics().isEmpty());

            mosaicId = createMosaic(account, type, BigInteger.valueOf(100000), mosaicAlias);

        }
        return mosaicId;
    }

    private void assertTransaction(MosaicId mosaicId, TransferTransaction resolvedTransaction) {

        System.out.println(toJson(resolvedTransaction));
        Assertions.assertEquals(config().getTestAccount2().getAddress(),
            resolvedTransaction.getRecipient());
        System.out.println(resolvedTransaction.getMosaics().get(0).getId());
        Assertions.assertTrue(resolvedTransaction.getMosaics().get(0).getId() instanceof MosaicId);
        Assertions.assertTrue(resolvedTransaction.getRecipient() instanceof Address);
        Assertions.assertEquals(mosaicId, resolvedTransaction.getMosaics().get(0).getId());
        Assertions.assertEquals(config().getTestAccount2().getAddress(),
            resolvedTransaction.getRecipient());
    }


    private TransferTransaction transferUsingAliases(Account sender, RepositoryType type,
        String mosaicAlias, String recipientAlias, BigInteger amount) {

        NamespaceId recipientNamespace = NamespaceId.createFromName(recipientAlias);

        NamespaceId mosaicNamespace = NamespaceId.createFromName(mosaicAlias);

        System.out.println("Sending " + amount + " Mosaic to: " + mosaicAlias);

        TransferTransactionFactory factory =
            TransferTransactionFactory.create(
                getNetworkType(),
                recipientNamespace,
                Collections.singletonList(new Mosaic(mosaicNamespace, amount)),
                new PlainMessage("E2ETest:TransactionServiceIntegrationTest")
            );

        factory.maxFee(this.maxFee);
        TransferTransaction transferTransaction = factory.build();

        Assertions
            .assertTrue(transferTransaction.getMosaics().get(0).getId() instanceof NamespaceId);
        Assertions.assertTrue(transferTransaction.getRecipient() instanceof Address);

        TransferTransaction processedTransferTransaction = announceAndValidate(type, sender,
            transferTransaction);

        Assertions
            .assertEquals(amount, processedTransferTransaction.getMosaics().get(0).getAmount());

        System.out.println(toJson(processedTransferTransaction));

        Assertions.assertTrue(
            processedTransferTransaction.getMosaics().get(0).getId() instanceof NamespaceId);
        Assertions.assertTrue(processedTransferTransaction.getRecipient() instanceof NamespaceId);

        return processedTransferTransaction;

    }

    private AggregateTransaction transferUsingAliasesAggregate(Account sender, RepositoryType type,
        String mosaicAlias, String recipientAlias, BigInteger amount) {

        NamespaceId recipientNamespace = NamespaceId.createFromName(recipientAlias);

        NamespaceId mosaicNamespace = NamespaceId.createFromName(mosaicAlias);

        System.out.println("Sending " + amount + " Mosaic to: " + mosaicAlias);

        TransferTransactionFactory factory =
            TransferTransactionFactory.create(
                getNetworkType(),
                recipientNamespace,
                Collections.singletonList(new Mosaic(mosaicNamespace, amount)),
                new PlainMessage("E2ETest:TransactionServiceIntegrationTest")
            );

        factory.maxFee(this.maxFee);
        TransferTransaction transferTransaction = factory.build();

        Assertions
            .assertTrue(transferTransaction.getMosaics().get(0).getId() instanceof NamespaceId);
        Assertions.assertTrue(transferTransaction.getRecipient() instanceof NamespaceId);

        Pair<TransferTransaction, AggregateTransaction> pair = announceAggregateAndValidate(type,
            transferTransaction, sender);

        TransferTransaction processedTransferTransaction = pair.getLeft();
        Assertions
            .assertEquals(amount, processedTransferTransaction.getMosaics().get(0).getAmount());

        System.out.println(toJson(processedTransferTransaction));

        Assertions.assertTrue(
            processedTransferTransaction.getMosaics().get(0).getId() instanceof NamespaceId);
        Assertions.assertTrue(processedTransferTransaction.getRecipient() instanceof NamespaceId);

        return pair.getRight();

    }

}
