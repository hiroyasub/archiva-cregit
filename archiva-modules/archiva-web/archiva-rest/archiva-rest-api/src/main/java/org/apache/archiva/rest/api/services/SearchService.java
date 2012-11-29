begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
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
name|maven2
operator|.
name|model
operator|.
name|Artifact
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|GroupIdList
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|SearchRequest
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|StringList
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
name|redback
operator|.
name|authorization
operator|.
name|RedbackAuthorization
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|POST
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
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

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/searchService/"
argument_list|)
specifier|public
interface|interface
name|SearchService
block|{
comment|/*     * quick/general text search which returns a list of artifacts     * query for an artifact based on a checksum     * query for all available versions of an artifact, sorted in version significance order     * query for an artifact's direct dependencies     *<b>search will be apply on all repositories the current user has karma</b>     * TODO query for an artifact's dependency tree (as with mvn dependency:tree - no duplicates should be included)     * TODO query for all artifacts that depend on a given artifact     */
annotation|@
name|Path
argument_list|(
literal|"quickSearch"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
name|List
argument_list|<
name|Artifact
argument_list|>
name|quickSearch
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"queryString"
argument_list|)
name|String
name|queryString
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"quickSearchWithRepositories"
argument_list|)
annotation|@
name|POST
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
comment|/**      *<b>if not repositories in SearchRequest: search will be apply on all repositories the current user has karma</b>      */
name|List
argument_list|<
name|Artifact
argument_list|>
name|quickSearchWithRepositories
parameter_list|(
name|SearchRequest
name|searchRequest
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"searchArtifacts"
argument_list|)
annotation|@
name|POST
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
comment|/**      * If searchRequest contains repositories, the search will be done only on those repositories.      *<b>if no repositories, the search will be apply on all repositories the current user has karma</b>      */
name|List
argument_list|<
name|Artifact
argument_list|>
name|searchArtifacts
parameter_list|(
name|SearchRequest
name|searchRequest
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"getArtifactVersions"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
comment|/**      *<b>search will be apply on all repositories the current user has karma</b>      */
name|List
argument_list|<
name|Artifact
argument_list|>
name|getArtifactVersions
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"groupId"
argument_list|)
name|String
name|groupId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"artifactId"
argument_list|)
name|String
name|artifactId
parameter_list|,
annotation|@
name|QueryParam
argument_list|(
literal|"packaging"
argument_list|)
name|String
name|packaging
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"getAllGroupIds"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|false
argument_list|)
comment|/**      *<b>this method applies on Maven Indexer lucene index, so datas not yet indexed won't be available</b>      */
name|GroupIdList
name|getAllGroupIds
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"selectedRepos"
argument_list|)
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepos
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"observableRepoIds"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noPermission
operator|=
literal|true
argument_list|,
name|noRestriction
operator|=
literal|true
argument_list|)
comment|/**      * @since 1.4-M3      */
name|StringList
name|getObservablesRepoIds
parameter_list|()
throws|throws
name|ArchivaRestServiceException
function_decl|;
comment|/*     @Path( "getDependencies" )     @GET     @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN } )     @RedbackAuthorization( noPermission = true, noRestriction = true )     List<Dependency> getDependencies( @QueryParam( "groupId" ) String groupId,                                       @QueryParam( "artifactId" ) String artifactId,                                       @QueryParam( "version" ) String version )         throws ArchivaRestServiceException;       @Path( "getArtifactByChecksum" )     @GET     @Produces( { MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML, MediaType.TEXT_PLAIN } )     @RedbackAuthorization( noPermission = true, noRestriction = true )     List<Artifact> getArtifactByChecksum( @QueryParam( "checksum" ) String checksum )         throws ArchivaRestServiceException;     */
block|}
end_interface

end_unit

