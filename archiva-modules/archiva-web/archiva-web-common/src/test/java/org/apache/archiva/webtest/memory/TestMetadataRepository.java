begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webtest
operator|.
name|memory
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
name|MetadataFacet
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
name|ProjectVersionReference
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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

begin_class
specifier|public
class|class
name|TestMetadataRepository
implements|implements
name|MetadataRepository
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TEST_REPO
init|=
literal|"test-repo"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TEST_NAMESPACE
init|=
literal|"org.apache.archiva"
decl_stmt|;
specifier|private
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|TestMetadataRepository
parameter_list|()
block|{
name|Date
name|whenGathered
init|=
operator|new
name|Date
argument_list|(
literal|123456789
argument_list|)
decl_stmt|;
name|addArtifact
argument_list|(
literal|"artifact-one"
argument_list|,
literal|"1.0"
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
name|addArtifact
argument_list|(
literal|"artifact-one"
argument_list|,
literal|"1.1"
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
name|addArtifact
argument_list|(
literal|"artifact-one"
argument_list|,
literal|"2.0"
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
name|addArtifact
argument_list|(
literal|"artifact-two"
argument_list|,
literal|"1.0.1"
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
name|addArtifact
argument_list|(
literal|"artifact-two"
argument_list|,
literal|"1.0.2"
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
name|addArtifact
argument_list|(
literal|"artifact-two"
argument_list|,
literal|"1.0.3-SNAPSHOT"
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
name|addArtifact
argument_list|(
literal|"artifact-three"
argument_list|,
literal|"2.0-SNAPSHOT"
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
name|addArtifact
argument_list|(
literal|"artifact-four"
argument_list|,
literal|"1.1-beta-2"
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addArtifact
parameter_list|(
name|String
name|projectId
parameter_list|,
name|String
name|projectVersion
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
block|{
name|ArtifactMetadata
name|artifact
init|=
operator|new
name|ArtifactMetadata
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setFileLastModified
argument_list|(
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setNamespace
argument_list|(
name|TEST_NAMESPACE
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setProjectVersion
argument_list|(
name|projectVersion
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|projectVersion
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setId
argument_list|(
name|projectId
operator|+
literal|"-"
operator|+
name|projectVersion
operator|+
literal|".jar"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setProject
argument_list|(
name|projectId
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setRepositoryId
argument_list|(
name|TEST_REPO
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setWhenGathered
argument_list|(
name|whenGathered
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|versions
operator|.
name|add
argument_list|(
name|projectVersion
argument_list|)
expr_stmt|;
block|}
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Collection
argument_list|<
name|ProjectVersionReference
argument_list|>
name|getProjectReferences
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getRootNamespaces
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getNamespaces
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjects
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjectVersions
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
return|return
name|versions
return|;
block|}
specifier|public
name|void
name|updateProject
parameter_list|(
name|String
name|repoId
parameter_list|,
name|ProjectMetadata
name|project
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|updateArtifact
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
parameter_list|,
name|ArtifactMetadata
name|artifactMeta
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|updateProjectVersion
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
name|ProjectVersionMetadata
name|versionMetadata
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|updateNamespace
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getMetadataFacets
parameter_list|(
name|String
name|repodId
parameter_list|,
name|String
name|facetId
parameter_list|)
block|{
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|public
name|MetadataFacet
name|getMetadataFacet
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|,
name|String
name|name
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|addMetadataFacet
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|MetadataFacet
name|metadataFacet
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|removeMetadataFacets
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|removeMetadataFacet
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|facetId
parameter_list|,
name|String
name|name
parameter_list|)
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByDateRange
parameter_list|(
name|String
name|repoId
parameter_list|,
name|Date
name|startTime
parameter_list|,
name|Date
name|endTime
parameter_list|)
block|{
return|return
name|artifacts
return|;
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getRepositories
parameter_list|()
block|{
return|return
name|Collections
operator|.
name|singletonList
argument_list|(
name|TEST_REPO
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByChecksum
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|checksum
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|removeArtifact
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|project
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|id
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|removeArtifact
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|project
parameter_list|,
name|String
name|version
parameter_list|,
name|MetadataFacet
name|metadataFacet
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|removeRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
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
return|return
name|artifacts
return|;
block|}
specifier|public
name|void
name|save
parameter_list|()
block|{
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
block|}
specifier|public
name|void
name|revert
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|boolean
name|canObtainAccess
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|Object
name|obtainAccess
parameter_list|(
name|Class
argument_list|<
name|?
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
parameter_list|(
name|String
name|repositoryId
parameter_list|)
block|{
return|return
name|artifacts
return|;
block|}
specifier|public
name|void
name|removeArtifact
parameter_list|(
name|ArtifactMetadata
name|artifactMetadata
parameter_list|,
name|String
name|baseVersion
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|public
name|void
name|removeNamespace
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
block|}
specifier|public
name|void
name|removeProjectVersion
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
name|MetadataRepositoryException
block|{
block|}
specifier|public
name|void
name|removeProject
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
parameter_list|,
name|String
name|projectId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
block|}
end_class

end_unit

