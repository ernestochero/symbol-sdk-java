/*
 * Copyright 2019 NEM
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
/*
 * Catapult REST API Reference
 * No description provided (generated by Openapi Generator https://github.com/openapitools/openapi-generator)
 *
 * The version of the OpenAPI document: 0.7.15
 *
 *
 * NOTE: This class is auto generated by OpenAPI Generator (https://openapi-generator.tech).
 * https://openapi-generator.tech
 * Do not edit the class manually.
 */

package io.nem.sdk.infrastructure.api;

import com.google.gson.reflect.TypeToken;
import io.nem.sdk.infrastructure.ApiCallback;
import io.nem.sdk.infrastructure.ApiClient;
import io.nem.sdk.infrastructure.ApiException;
import io.nem.sdk.infrastructure.ApiResponse;
import io.nem.sdk.infrastructure.Configuration;
import io.nem.sdk.infrastructure.Pair;
import io.nem.sdk.infrastructure.model.BlockchainScoreDTO;
import io.nem.sdk.infrastructure.model.HeightInfoDTO;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ChainRoutesApi {

    private ApiClient localVarApiClient;

    public ChainRoutesApi() {
        this(Configuration.getDefaultApiClient());
    }

    public ChainRoutesApi(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return localVarApiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.localVarApiClient = apiClient;
    }

    /**
     * Build call for getBlockchainHeight
     *
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details <table summary="Response Details" border="1">
     * <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
     * <tr><td> 200 </td><td> success </td><td>  -  </td></tr>
     * </table>
     */
    public okhttp3.Call getBlockchainHeightCall(final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = new Object();

        // create path and map variables
        String localVarPath = "/chain/height";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {"application/json"};
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {};

        final String localVarContentType =
            localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[]{};
        return localVarApiClient.buildCall(
            localVarPath,
            "GET",
            localVarQueryParams,
            localVarCollectionQueryParams,
            localVarPostBody,
            localVarHeaderParams,
            localVarFormParams,
            localVarAuthNames,
            _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getBlockchainHeightValidateBeforeCall(final ApiCallback _callback)
        throws ApiException {

        okhttp3.Call localVarCall = getBlockchainHeightCall(_callback);
        return localVarCall;
    }

    /**
     * Get the current height of the chain Returns the current height of the blockchain.
     *
     * @return HeightInfoDTO
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the
     * response body
     * @http.response.details <table summary="Response Details" border="1">
     * <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
     * <tr><td> 200 </td><td> success </td><td>  -  </td></tr>
     * </table>
     */
    public HeightInfoDTO getBlockchainHeight() throws ApiException {
        ApiResponse<HeightInfoDTO> localVarResp = getBlockchainHeightWithHttpInfo();
        return localVarResp.getData();
    }

    /**
     * Get the current height of the chain Returns the current height of the blockchain.
     *
     * @return ApiResponse&lt;HeightInfoDTO&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the
     * response body
     * @http.response.details <table summary="Response Details" border="1">
     * <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
     * <tr><td> 200 </td><td> success </td><td>  -  </td></tr>
     * </table>
     */
    public ApiResponse<HeightInfoDTO> getBlockchainHeightWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = getBlockchainHeightValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<HeightInfoDTO>() {
        }.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Get the current height of the chain (asynchronously) Returns the current height of the
     * blockchain.
     *
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body
     * object
     * @http.response.details <table summary="Response Details" border="1">
     * <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
     * <tr><td> 200 </td><td> success </td><td>  -  </td></tr>
     * </table>
     */
    public okhttp3.Call getBlockchainHeightAsync(final ApiCallback<HeightInfoDTO> _callback)
        throws ApiException {

        okhttp3.Call localVarCall = getBlockchainHeightValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<HeightInfoDTO>() {
        }.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }

    /**
     * Build call for getBlockchainScore
     *
     * @param _callback Callback for upload/download progress
     * @return Call to execute
     * @throws ApiException If fail to serialize the request body object
     * @http.response.details <table summary="Response Details" border="1">
     * <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
     * <tr><td> 200 </td><td> success </td><td>  -  </td></tr>
     * </table>
     */
    public okhttp3.Call getBlockchainScoreCall(final ApiCallback _callback) throws ApiException {
        Object localVarPostBody = new Object();

        // create path and map variables
        String localVarPath = "/chain/score";

        List<Pair> localVarQueryParams = new ArrayList<Pair>();
        List<Pair> localVarCollectionQueryParams = new ArrayList<Pair>();
        Map<String, String> localVarHeaderParams = new HashMap<String, String>();
        Map<String, Object> localVarFormParams = new HashMap<String, Object>();
        final String[] localVarAccepts = {"application/json"};
        final String localVarAccept = localVarApiClient.selectHeaderAccept(localVarAccepts);
        if (localVarAccept != null) {
            localVarHeaderParams.put("Accept", localVarAccept);
        }

        final String[] localVarContentTypes = {};

        final String localVarContentType =
            localVarApiClient.selectHeaderContentType(localVarContentTypes);
        localVarHeaderParams.put("Content-Type", localVarContentType);

        String[] localVarAuthNames = new String[]{};
        return localVarApiClient.buildCall(
            localVarPath,
            "GET",
            localVarQueryParams,
            localVarCollectionQueryParams,
            localVarPostBody,
            localVarHeaderParams,
            localVarFormParams,
            localVarAuthNames,
            _callback);
    }

    @SuppressWarnings("rawtypes")
    private okhttp3.Call getBlockchainScoreValidateBeforeCall(final ApiCallback _callback)
        throws ApiException {

        okhttp3.Call localVarCall = getBlockchainScoreCall(_callback);
        return localVarCall;
    }

    /**
     * Get the current score of the chain Gets the current score of the blockchain. The higher the
     * score, the better the chain. During synchronization, nodes try to get the best blockchain in
     * the network. The score for a block is derived from its difficulty and the time (in seconds)
     * that has elapsed since the last block: block score &#x3D; difficulty − time elapsed since
     * last block
     *
     * @return BlockchainScoreDTO
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the
     * response body
     * @http.response.details <table summary="Response Details" border="1">
     * <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
     * <tr><td> 200 </td><td> success </td><td>  -  </td></tr>
     * </table>
     */
    public BlockchainScoreDTO getBlockchainScore() throws ApiException {
        ApiResponse<BlockchainScoreDTO> localVarResp = getBlockchainScoreWithHttpInfo();
        return localVarResp.getData();
    }

    /**
     * Get the current score of the chain Gets the current score of the blockchain. The higher the
     * score, the better the chain. During synchronization, nodes try to get the best blockchain in
     * the network. The score for a block is derived from its difficulty and the time (in seconds)
     * that has elapsed since the last block: block score &#x3D; difficulty − time elapsed since
     * last block
     *
     * @return ApiResponse&lt;BlockchainScoreDTO&gt;
     * @throws ApiException If fail to call the API, e.g. server error or cannot deserialize the
     * response body
     * @http.response.details <table summary="Response Details" border="1">
     * <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
     * <tr><td> 200 </td><td> success </td><td>  -  </td></tr>
     * </table>
     */
    public ApiResponse<BlockchainScoreDTO> getBlockchainScoreWithHttpInfo() throws ApiException {
        okhttp3.Call localVarCall = getBlockchainScoreValidateBeforeCall(null);
        Type localVarReturnType = new TypeToken<BlockchainScoreDTO>() {
        }.getType();
        return localVarApiClient.execute(localVarCall, localVarReturnType);
    }

    /**
     * Get the current score of the chain (asynchronously) Gets the current score of the blockchain.
     * The higher the score, the better the chain. During synchronization, nodes try to get the best
     * blockchain in the network. The score for a block is derived from its difficulty and the time
     * (in seconds) that has elapsed since the last block: block score &#x3D; difficulty − time
     * elapsed since last block
     *
     * @param _callback The callback to be executed when the API call finishes
     * @return The request call
     * @throws ApiException If fail to process the API call, e.g. serializing the request body
     * object
     * @http.response.details <table summary="Response Details" border="1">
     * <tr><td> Status Code </td><td> Description </td><td> Response Headers </td></tr>
     * <tr><td> 200 </td><td> success </td><td>  -  </td></tr>
     * </table>
     */
    public okhttp3.Call getBlockchainScoreAsync(final ApiCallback<BlockchainScoreDTO> _callback)
        throws ApiException {

        okhttp3.Call localVarCall = getBlockchainScoreValidateBeforeCall(_callback);
        Type localVarReturnType = new TypeToken<BlockchainScoreDTO>() {
        }.getType();
        localVarApiClient.executeAsync(localVarCall, localVarReturnType, _callback);
        return localVarCall;
    }
}
