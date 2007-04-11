begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|proxy
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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArchivaRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|ProjectReference
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

begin_comment
comment|/**  * Handler for potential repository proxy connectors.  *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryProxyConnectors
block|{
comment|/**      * Performs the artifact fetch operation against the target repositories      * of the provided source repository.      *       * If the artifact is found, it is downloaded and placed into the source repository      * filesystem.      *       * @param repository the source repository to use. (must be a managed repository)      * @param artifact the artifact to fetch.      * @return true if the fetch operation succeeded in obtaining content, false if no content was obtained.      * @throws ProxyException if there was a problem fetching the content from the target repositories.      */
specifier|public
name|boolean
name|fetchFromProxies
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|,
name|ArtifactReference
name|artifact
parameter_list|)
throws|throws
name|ProxyException
function_decl|;
comment|/**      * Performs the metadata fetch operation against the target repositories      * of the provided source repository.      *       * If the metadata is found, it is downloaded and placed into the source repository      * filesystem.      *       * @param repository the source repository to use. (must be a managed repository)      * @param metadata the metadata to fetch.      * @return true if the fetch operation succeeded in obtaining content, false if no content was obtained.      * @throws ProxyException if there was a problem fetching the content from the target repositories.      */
specifier|public
name|boolean
name|fetchFromProxies
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|,
name|ProjectReference
name|metadata
parameter_list|)
throws|throws
name|ProxyException
function_decl|;
comment|/**      * Get the List of {@link ProxyConnector} objects of the source repository.      *       * @param repository the source repository to look for.      * @return the List of {@link ProxyConnector} objects.      */
specifier|public
name|List
name|getProxyConnectors
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|)
function_decl|;
comment|/**      * Tests to see if the provided repository is a source repository for      * any {@link ProxyConnector} objects.      *       * @param repository the source repository to look for.      * @return true if there are proxy connectors that use the provided       *   repository as a source repository.      */
specifier|public
name|boolean
name|hasProxies
parameter_list|(
name|ArchivaRepository
name|repository
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

