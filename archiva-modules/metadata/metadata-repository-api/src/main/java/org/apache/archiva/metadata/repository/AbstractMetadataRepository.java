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
comment|/* * Licensed to the Apache Software Foundation (ASF) under one * or more contributor license agreements.  See the NOTICE file * distributed with this work for additional information * regarding copyright ownership.  The ASF licenses this file * to you under the Apache License, Version 2.0 (the * "License"); you may not use this file except in compliance * with the License.  You may obtain a copy of the License at * * http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, * software distributed under the License is distributed on an * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY * KIND, either express or implied.  See the License for the * specific language governing permissions and limitations * under the License. */
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
name|QueryParameter
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
name|MetadataFacetFactory
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
name|commons
operator|.
name|collections4
operator|.
name|ComparatorUtils
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|ZonedDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Stream
import|;
end_import

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractMetadataRepository
implements|implements
name|MetadataRepository
block|{
specifier|protected
name|MetadataService
name|metadataService
decl_stmt|;
specifier|public
name|AbstractMetadataRepository
parameter_list|()
block|{
block|}
specifier|public
name|AbstractMetadataRepository
parameter_list|(
name|MetadataService
name|metadataService
parameter_list|)
block|{
name|this
operator|.
name|metadataService
operator|=
name|metadataService
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateProject
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|ProjectMetadata
name|project
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
annotation|@
name|Override
specifier|public
name|void
name|updateArtifact
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateProjectVersion
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateNamespace
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getMetadataFacets
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
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
annotation|@
name|Override
specifier|public
name|boolean
name|hasMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
annotation|@
name|Override
specifier|public
name|void
name|addMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|MetadataFacet
name|metadataFacet
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeMetadataFacets
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|facetId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByDateRange
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|ZonedDateTime
name|startTime
parameter_list|,
name|ZonedDateTime
name|endTime
parameter_list|,
name|QueryParameter
name|queryParameter
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByChecksum
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|checksum
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByProjectVersionFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|repositoryId
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByAttribute
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|repositoryId
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByProjectVersionAttribute
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|,
name|String
name|repositoryId
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
annotation|@
name|Override
specifier|public
name|void
name|removeArtifact
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeTimestampedArtifact
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
annotation|@
name|Override
specifier|public
name|void
name|removeFacetFromArtifact
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
annotation|@
name|Override
specifier|public
name|void
name|removeRepository
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeNamespace
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|namespace
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
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
annotation|@
name|Override
specifier|public
name|ProjectMetadata
name|getProject
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|ProjectVersionMetadata
name|getProjectVersion
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getArtifactVersions
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|ProjectVersionReference
argument_list|>
name|getProjectReferences
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getRootNamespaces
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getChildNamespaces
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjects
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|String
name|namespace
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|String
argument_list|>
name|getProjectVersions
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeProjectVersion
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
specifier|protected
specifier|static
name|Comparator
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactMetadataComparator
parameter_list|(
specifier|final
name|QueryParameter
name|queryParameter
parameter_list|,
name|String
name|defaultAttr
parameter_list|)
block|{
name|List
argument_list|<
name|Comparator
argument_list|<
name|ArtifactMetadata
argument_list|>
argument_list|>
name|compList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|sortFields
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|queryParameter
operator|.
name|getSortFields
argument_list|()
operator|.
name|size
argument_list|()
operator|==
literal|0
condition|)
block|{
name|sortFields
operator|.
name|add
argument_list|(
name|defaultAttr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sortFields
operator|=
name|queryParameter
operator|.
name|getSortFields
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|String
name|attribute
range|:
name|sortFields
control|)
block|{
switch|switch
condition|(
name|attribute
condition|)
block|{
case|case
literal|"id"
case|:
name|compList
operator|.
name|add
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getId
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"whenGathered"
case|:
name|compList
operator|.
name|add
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getWhenGathered
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"fileLastModified"
case|:
name|compList
operator|.
name|add
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getFileLastModified
argument_list|)
argument_list|)
expr_stmt|;
case|case
literal|"version"
case|:
name|compList
operator|.
name|add
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getVersion
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"projectVersion"
case|:
name|compList
operator|.
name|add
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getProjectVersion
argument_list|)
argument_list|)
expr_stmt|;
break|break;
case|case
literal|"project"
case|:
name|compList
operator|.
name|add
argument_list|(
name|Comparator
operator|.
name|comparing
argument_list|(
name|ArtifactMetadata
operator|::
name|getProject
argument_list|)
argument_list|)
expr_stmt|;
break|break;
default|default:
comment|//
block|}
block|}
name|Comparator
argument_list|<
name|ArtifactMetadata
argument_list|>
name|comp
init|=
name|ComparatorUtils
operator|.
name|chainedComparator
argument_list|(
name|compList
argument_list|)
decl_stmt|;
if|if
condition|(
name|queryParameter
operator|.
name|isAscending
argument_list|()
condition|)
block|{
return|return
name|comp
return|;
block|}
else|else
block|{
return|return
name|comp
operator|.
name|reversed
argument_list|()
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifacts
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|()
throw|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeProject
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
annotation|@
name|Override
specifier|public
name|void
name|close
parameter_list|()
throws|throws
name|MetadataRepositoryException
block|{
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|searchArtifacts
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|text
parameter_list|,
name|boolean
name|exact
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
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|searchArtifacts
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|text
parameter_list|,
name|boolean
name|exact
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
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|MetadataFacet
parameter_list|>
name|Stream
argument_list|<
name|T
argument_list|>
name|getMetadataFacetStream
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|facetClazz
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
return|return
name|getMetadataFacetStream
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|facetClazz
argument_list|,
operator|new
name|QueryParameter
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Stream
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactByDateRangeStream
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|ZonedDateTime
name|startTime
parameter_list|,
name|ZonedDateTime
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
return|return
name|getArtifactByDateRangeStream
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|startTime
argument_list|,
name|endTime
argument_list|,
operator|new
name|QueryParameter
argument_list|()
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|MetadataFacet
name|getMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
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
name|getMetadataFacet
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
name|getFactoryClassForId
argument_list|(
name|facetId
argument_list|)
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|MetadataFacet
parameter_list|>
name|Stream
argument_list|<
name|T
argument_list|>
name|getMetadataFacetStream
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|facetClazz
parameter_list|,
name|QueryParameter
name|queryParameter
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
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|MetadataFacet
parameter_list|>
name|T
name|getMetadataFacet
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|String
name|name
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
annotation|@
name|Override
specifier|public
name|Stream
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactByDateRangeStream
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repositoryId
parameter_list|,
name|ZonedDateTime
name|startTime
parameter_list|,
name|ZonedDateTime
name|endTime
parameter_list|,
name|QueryParameter
name|queryParameter
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
specifier|protected
parameter_list|<
name|T
extends|extends
name|MetadataFacet
parameter_list|>
name|MetadataFacetFactory
name|getFacetFactory
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|facetClazz
parameter_list|)
block|{
return|return
name|metadataService
operator|.
name|getFactory
argument_list|(
name|facetClazz
argument_list|)
return|;
block|}
specifier|protected
name|MetadataFacetFactory
name|getFacetFactory
parameter_list|(
name|String
name|facetId
parameter_list|)
block|{
return|return
name|metadataService
operator|.
name|getFactory
argument_list|(
name|facetId
argument_list|)
return|;
block|}
specifier|protected
name|Set
argument_list|<
name|String
argument_list|>
name|getSupportedFacets
parameter_list|()
block|{
return|return
name|metadataService
operator|.
name|getSupportedFacets
argument_list|( )
return|;
block|}
specifier|protected
name|Class
argument_list|<
name|?
extends|extends
name|MetadataFacet
argument_list|>
name|getFactoryClassForId
parameter_list|(
name|String
name|facetId
parameter_list|)
block|{
return|return
name|metadataService
operator|.
name|getFactoryClassForId
argument_list|(
name|facetId
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactsByDateRange
parameter_list|(
name|RepositorySession
name|session
parameter_list|,
name|String
name|repoId
parameter_list|,
name|ZonedDateTime
name|startTime
parameter_list|,
name|ZonedDateTime
name|endTime
parameter_list|)
throws|throws
name|MetadataRepositoryException
block|{
return|return
name|getArtifactsByDateRange
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|startTime
argument_list|,
name|endTime
argument_list|,
operator|new
name|QueryParameter
argument_list|(  )
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Stream
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactStream
parameter_list|(
specifier|final
name|RepositorySession
name|session
parameter_list|,
specifier|final
name|String
name|repositoryId
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
return|return
name|getArtifactStream
argument_list|(
name|session
argument_list|,
name|repositoryId
argument_list|,
operator|new
name|QueryParameter
argument_list|(  )
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Stream
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactStream
parameter_list|(
specifier|final
name|RepositorySession
name|session
parameter_list|,
specifier|final
name|String
name|repoId
parameter_list|,
specifier|final
name|String
name|namespace
parameter_list|,
specifier|final
name|String
name|projectId
parameter_list|,
specifier|final
name|String
name|projectVersion
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
return|return
name|getArtifactStream
argument_list|(
name|session
argument_list|,
name|repoId
argument_list|,
name|namespace
argument_list|,
name|projectId
argument_list|,
name|projectVersion
argument_list|,
operator|new
name|QueryParameter
argument_list|(  )
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Stream
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactStream
parameter_list|(
specifier|final
name|RepositorySession
name|session
parameter_list|,
specifier|final
name|String
name|repositoryId
parameter_list|,
name|QueryParameter
name|queryParameter
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|( )
throw|;
block|}
annotation|@
name|Override
specifier|public
name|Stream
argument_list|<
name|ArtifactMetadata
argument_list|>
name|getArtifactStream
parameter_list|(
specifier|final
name|RepositorySession
name|session
parameter_list|,
specifier|final
name|String
name|repoId
parameter_list|,
specifier|final
name|String
name|namespace
parameter_list|,
specifier|final
name|String
name|projectId
parameter_list|,
specifier|final
name|String
name|projectVersion
parameter_list|,
specifier|final
name|QueryParameter
name|queryParameter
parameter_list|)
throws|throws
name|MetadataResolutionException
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|( )
throw|;
block|}
block|}
end_class

end_unit

