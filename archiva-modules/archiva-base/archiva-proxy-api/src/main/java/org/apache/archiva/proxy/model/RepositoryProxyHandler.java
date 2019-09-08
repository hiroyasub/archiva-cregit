begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArtifactReference
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|policies
operator|.
name|Policy
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|policies
operator|.
name|ProxyDownloadException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|RepositoryType
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|storage
operator|.
name|StorageAsset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * A repository proxy handler is used to fetch remote artifacts from different remote repositories.  * A proxy handler is connected to one managed repository and a list of remote repositories.  *  * Repository proxies should not be confused with network proxies. Network are proxies for specific network protocols,  * like HTTP. A repository proxy delegates the repository requests to remote repositories and caches artifacts.  *  * If a artifact is requested for the managed repository and the artifact is not cached locally, the handler goes through  * the list of remotes and tries to download the artifact. If a download was successful the artifact is cached locally.  *  * The connection between managed and remote repositories is defined by list of {@link ProxyConnector} each defines a one-to-one relationship.  *  * A proxy connector defines specifics about the download behaviour:  *<ul>  *<li>Policies {@link org.apache.archiva.policies.Policy} define the behaviour for different cases (errors, not available, caching lifetime).</li>  *<li>Black- and Whitelists are used to ban or allow certain paths on the remote repositories.  *</ul>  *  * The policies and black- and whitelist are set on the {@link ProxyConnector}  *  * There may be network proxies needed to connect the remote repositories.  *  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryProxyHandler
block|{
name|List
argument_list|<
name|RepositoryType
argument_list|>
name|supports
parameter_list|( )
function_decl|;
comment|/**      * Performs the artifact fetch operation against the target repositories      * of the provided source repository.      *<p>      * If the artifact is found, it is downloaded and placed into the source repository      * filesystem.      *      * @param repository the source repository to use. (must be a managed repository)      * @param artifact   the artifact to fetch.      * @return the file that was obtained, or null if no content was obtained      * @throws ProxyDownloadException if there was a problem fetching the content from the target repositories.      */
name|StorageAsset
name|fetchFromProxies
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|ArtifactReference
name|artifact
parameter_list|)
throws|throws
name|ProxyDownloadException
function_decl|;
comment|/**      * Performs the metadata fetch operation against the target repositories      * of the provided source repository.      *<p>      * If the metadata is found, it is downloaded and placed into the source repository      * filesystem.      *      * @param repository  the source repository to use. (must be a managed repository)      * @param logicalPath the metadata to fetch.      * @return the file that was obtained, or null if no content was obtained      */
name|ProxyFetchResult
name|fetchMetadataFromProxies
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|String
name|logicalPath
parameter_list|)
function_decl|;
comment|/**      * Performs the fetch operation against the target repositories      * of the provided source repository by a specific path.      *      * @param managedRepository the source repository to use. (must be a managed repository)      * @param path              the path of the resource to fetch      * @return the file that was obtained, or null if no content was obtained      */
name|StorageAsset
name|fetchFromProxies
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|,
name|String
name|path
parameter_list|)
function_decl|;
comment|/**      * Get the List of {@link ProxyConnector} objects of the source repository.      *      * @param repository the source repository to look for.      * @return the List of {@link ProxyConnector} objects.      */
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|getProxyConnectors
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Tests to see if the provided repository is a source repository for      * any {@link ProxyConnector} objects.      *      * @param repository the source repository to look for.      * @return true if there are proxy connectors that use the provided      * repository as a source repository.      */
name|boolean
name|hasProxies
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Sets network proxies (normally HTTP proxies) to access the remote repositories.      *      * @param networkProxies A map of (repository id, network proxy) where the repository id must be the id of an      *                existing remote repository.      */
name|void
name|setNetworkProxies
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|networkProxies
parameter_list|)
function_decl|;
comment|/**      * Adds a network proxy that is used to access the remote repository.      *      * @param id The repository id      * @param networkProxy The network proxy to use      */
name|void
name|addNetworkproxy
parameter_list|(
name|String
name|id
parameter_list|,
name|NetworkProxy
name|networkProxy
parameter_list|)
function_decl|;
comment|/**      * Returns a map of the defined network proxies, or a empty map, if no proxy is defined.      *      * @return A map (repository id, network proxy). If none is defined, a empty map is returned.      */
name|Map
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|getNetworkProxies
parameter_list|( )
function_decl|;
comment|/**      * Returns the network proxy that is defined for the given repository id.      * @param id The remote repository id      * @return A network proxy or<code>null</code> if no one is defined for this id.      */
name|NetworkProxy
name|getNetworkProxy
parameter_list|(
name|String
name|id
parameter_list|)
function_decl|;
comment|/**      * Returns the proxy handler implementation. This can be used, if the underlying implementation for a specific      * repository type is needed.      *      * @param clazz The class to convert to      * @param<T>   The type      * @return The handler      */
parameter_list|<
name|T
extends|extends
name|RepositoryProxyHandler
parameter_list|>
name|T
name|getHandler
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
throws|throws
name|IllegalArgumentException
function_decl|;
comment|/**      * Sets the policies that this handler should validate.      * @param policyList      */
name|void
name|setPolicies
parameter_list|(
name|List
argument_list|<
name|Policy
argument_list|>
name|policyList
parameter_list|)
function_decl|;
comment|/**      * Adds a policy      * @param policy      */
name|void
name|addPolicy
parameter_list|(
name|Policy
name|policy
parameter_list|)
function_decl|;
comment|/**      * Removes a policy      * @param policy      */
name|void
name|removePolicy
parameter_list|(
name|Policy
name|policy
parameter_list|)
function_decl|;
name|void
name|addProxyConnector
parameter_list|(
name|ProxyConnector
name|connector
parameter_list|)
function_decl|;
name|void
name|setProxyConnectors
parameter_list|(
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|proxyConnectors
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

