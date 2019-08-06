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
operator|.
name|stats
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|ArtifactMetadata
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
name|maven2
operator|.
name|MavenArtifactFacet
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
name|repository
operator|.
name|MetadataRepository
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
name|repository
operator|.
name|MetadataRepositoryException
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
name|repository
operator|.
name|MetadataResolutionException
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
name|repository
operator|.
name|RepositorySession
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
import|;
end_import

begin_comment
comment|/**  *  * This is a default implementation of a statistics provider that walks the tree and  * counts the artifacts found during the walk.  * The implementation is not very fast. If metadata store provider can improve the  * process by using store specific techniques (like query language) they should provide  * their own implementation.  *  * @author Martin Stockhammer  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryWalkingStatisticsProvider
implements|implements
name|RepositoryStatisticsProvider
block|{
comment|/**      * Walks each namespace of the given repository id and counts the artifacts.      *      *      * @param repositorySession      * @param metadataRepository The repository implementation      * @param repositoryId The repository Id      * @param repositoryStatistics The statistics object that must be populated      * @throws MetadataRepositoryException Throws the repository exception, if an error occurs while accessing the repository.      */
annotation|@
name|Override
specifier|public
name|void
name|populateStatistics
parameter_list|(
name|RepositorySession
name|repositorySession
parameter_list|,
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|RepositoryStatistics
name|repositoryStatistics
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
try|try
block|{
for|for
control|(
name|String
name|ns
range|:
name|metadataRepository
operator|.
name|getRootNamespaces
argument_list|(
name|repositorySession
argument_list|,
name|repositoryId
argument_list|)
control|)
block|{
name|walkRepository
argument_list|(
name|repositorySession
argument_list|,
name|metadataRepository
argument_list|,
name|repositoryStatistics
argument_list|,
name|repositoryId
argument_list|,
name|ns
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|MetadataResolutionException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|MetadataRepositoryException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|void
name|walkRepository
parameter_list|(
name|RepositorySession
name|repositorySession
parameter_list|,
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|RepositoryStatistics
name|stats
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|ns
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
for|for
control|(
name|String
name|namespace
range|:
name|metadataRepository
operator|.
name|getNamespaces
argument_list|(
name|repositorySession
argument_list|,
name|repositoryId
argument_list|,
name|ns
argument_list|)
control|)
block|{
name|walkRepository
argument_list|(
name|repositorySession
argument_list|,
name|metadataRepository
argument_list|,
name|stats
argument_list|,
name|repositoryId
argument_list|,
name|ns
operator|+
literal|"."
operator|+
name|namespace
argument_list|)
expr_stmt|;
block|}
name|Collection
argument_list|<
name|String
argument_list|>
name|projects
init|=
name|metadataRepository
operator|.
name|getProjects
argument_list|(
name|repositorySession
argument_list|,
name|repositoryId
argument_list|,
name|ns
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|projects
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|stats
operator|.
name|setTotalGroupCount
argument_list|(
name|stats
operator|.
name|getTotalGroupCount
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setTotalProjectCount
argument_list|(
name|stats
operator|.
name|getTotalProjectCount
argument_list|()
operator|+
name|projects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|project
range|:
name|projects
control|)
block|{
for|for
control|(
name|String
name|version
range|:
name|metadataRepository
operator|.
name|getProjectVersions
argument_list|(
name|repositorySession
argument_list|,
name|repositoryId
argument_list|,
name|ns
argument_list|,
name|project
argument_list|)
control|)
block|{
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|metadataRepository
operator|.
name|getArtifacts
argument_list|(
name|repositorySession
argument_list|,
name|repositoryId
argument_list|,
name|ns
argument_list|,
name|project
argument_list|,
name|version
argument_list|)
control|)
block|{
name|stats
operator|.
name|setTotalArtifactCount
argument_list|(
name|stats
operator|.
name|getTotalArtifactCount
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|stats
operator|.
name|setTotalArtifactFileSize
argument_list|(
name|stats
operator|.
name|getTotalArtifactFileSize
argument_list|()
operator|+
name|artifact
operator|.
name|getSize
argument_list|()
argument_list|)
expr_stmt|;
name|MavenArtifactFacet
name|facet
init|=
operator|(
name|MavenArtifactFacet
operator|)
name|artifact
operator|.
name|getFacet
argument_list|(
name|MavenArtifactFacet
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|facet
operator|!=
literal|null
condition|)
block|{
name|String
name|type
init|=
name|facet
operator|.
name|getType
argument_list|()
decl_stmt|;
name|stats
operator|.
name|setTotalCountForType
argument_list|(
name|type
argument_list|,
name|stats
operator|.
name|getTotalCountForType
argument_list|(
name|type
argument_list|)
operator|+
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

