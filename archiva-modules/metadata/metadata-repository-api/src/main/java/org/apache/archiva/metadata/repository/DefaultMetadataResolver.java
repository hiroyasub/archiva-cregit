begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
name|metadata
operator|.
name|model
operator|.
name|ProjectMetadata
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
name|metadata
operator|.
name|model
operator|.
name|ProjectVersionMetadata
import|;
end_import

begin_comment
comment|/**  * @plexus.component role="org.apache.archiva.metadata.repository.MetadataResolver"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultMetadataResolver
implements|implements
name|MetadataResolver
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
comment|/**      * FIXME: this needs to be configurable based on storage type, and availability of proxy module      * ... could be a different type since we need methods to modify the storage metadata, which would also allow more      *     appropriate methods to pass in the already determined repository configuration, for example, instead of the ID      *      * @plexus.requirement role-hint="maven2"      */
specifier|private
name|MetadataResolver
name|storageResolver
decl_stmt|;
specifier|public
name|ProjectMetadata
name|getProject
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
block|{
comment|// TODO: intercept
return|return
name|metadataRepository
operator|.
name|getProject
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|)
return|;
block|}
specifier|public
name|ProjectVersionMetadata
name|getProjectVersion
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
throws|throws
name|MetadataResolverException
block|{
name|ProjectVersionMetadata
name|metadata
init|=
name|metadataRepository
operator|.
name|getProjectVersion
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
decl_stmt|;
comment|// TODO: do we want to detect changes as well by comparing timestamps? isProjectVersionNewerThan(updated)
comment|//       in such cases we might also remove/update stale metadata, including adjusting plugin-based facets
if|if
condition|(
name|metadata
operator|==
literal|null
condition|)
block|{
name|metadata
operator|=
name|storageResolver
operator|.
name|getProjectVersion
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
expr_stmt|;
if|if
condition|(
name|metadata
operator|!=
literal|null
condition|)
block|{
name|metadataRepository
operator|.
name|updateProjectVersion
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|metadata
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getArtifactVersions
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|)
block|{
comment|// TODO: intercept
return|return
name|metadataRepository
operator|.
name|getArtifactVersions
argument_list|(
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|)
return|;
block|}
block|}
end_class

end_unit

