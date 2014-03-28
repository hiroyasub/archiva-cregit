begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|mock
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
name|RepositorySessionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositorySessionFactory#mock"
argument_list|)
specifier|public
class|class
name|MockRepositorySessionFactory
implements|implements
name|RepositorySessionFactory
block|{
specifier|public
name|RepositorySession
name|createSession
parameter_list|()
block|{
return|return
operator|new
name|RepositorySession
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
block|{
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
block|{
return|return;
block|}
annotation|@
name|Override
specifier|public
name|void
name|save
parameter_list|()
block|{
comment|// no op
block|}
annotation|@
name|Override
specifier|public
name|MetadataRepository
name|getRepository
parameter_list|()
block|{
return|return
operator|new
name|MetadataRepository
argument_list|()
block|{
specifier|public
name|boolean
name|hasMetadataFacet
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
return|return
literal|false
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
name|projectVersion
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
name|updateProject
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|ProjectMetadata
name|project
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|updateArtifact
parameter_list|(
name|String
name|repositoryId
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
throws|throws
name|MetadataRepositoryException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|updateProjectVersion
parameter_list|(
name|String
name|repositoryId
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
throws|throws
name|MetadataRepositoryException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|updateNamespace
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getMetadataFacets
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
throws|throws
name|MetadataRepositoryException
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
throws|throws
name|MetadataRepositoryException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataRepositoryException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataRepositoryException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|removeMetadataFacet
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
throws|throws
name|MetadataRepositoryException
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
name|repositoryId
parameter_list|,
name|Date
name|startTime
parameter_list|,
name|Date
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getRepositories
parameter_list|()
throws|throws
name|MetadataRepositoryException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByChecksum
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|checksum
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataRepositoryException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|removeRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataRepositoryException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataResolutionException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
name|MetadataResolutionException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataResolutionException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataResolutionException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataResolutionException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataResolutionException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataResolutionException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataResolutionException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
throws|throws
name|MetadataResolutionException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|save
parameter_list|()
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|revert
parameter_list|()
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
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
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
parameter_list|<
name|T
parameter_list|>
name|T
name|obtainAccess
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|aClass
parameter_list|)
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
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
return|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

