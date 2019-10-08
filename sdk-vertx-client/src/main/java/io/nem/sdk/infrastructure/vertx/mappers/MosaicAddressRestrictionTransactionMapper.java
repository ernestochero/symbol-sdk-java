/*
 * Copyright 2019. NEM
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 *
 */

package io.nem.sdk.infrastructure.vertx.mappers;

import static io.nem.core.utils.MapperUtils.toMosaicId;

import io.nem.core.utils.MapperUtils;
import io.nem.sdk.model.blockchain.NetworkType;
import io.nem.sdk.model.transaction.JsonHelper;
import io.nem.sdk.model.transaction.MosaicAddressRestrictionTransaction;
import io.nem.sdk.model.transaction.MosaicAddressRestrictionTransactionFactory;
import io.nem.sdk.model.transaction.TransactionFactory;
import io.nem.sdk.model.transaction.TransactionType;
import io.nem.sdk.openapi.vertx.model.MosaicAddressRestrictionTransactionDTO;
import java.math.BigInteger;

/**
 * Mosaic address restriction transaction mapper.
 */
class MosaicAddressRestrictionTransactionMapper extends
    AbstractTransactionMapper<MosaicAddressRestrictionTransactionDTO, MosaicAddressRestrictionTransaction> {

    public MosaicAddressRestrictionTransactionMapper(JsonHelper jsonHelper) {
        super(jsonHelper, TransactionType.MOSAIC_ADDRESS_RESTRICTION,
            MosaicAddressRestrictionTransactionDTO.class);
    }

    @Override
    protected TransactionFactory<MosaicAddressRestrictionTransaction> createFactory(
        NetworkType networkType,
        MosaicAddressRestrictionTransactionDTO transaction) {
        return new MosaicAddressRestrictionTransactionFactory(networkType,
            toMosaicId(transaction.getMosaicId()),
            new BigInteger(transaction.getRestrictionKey()),
            MapperUtils.toAddressFromUnresolved(transaction.getTargetAddress()),
            new BigInteger(transaction.getNewRestrictionValue())).previousRestrictionValue(
            new BigInteger(transaction.getPreviousRestrictionValue()));
    }
}
