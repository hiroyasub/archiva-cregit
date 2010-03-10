begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|xmlrpc
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
name|indexer
operator|.
name|search
operator|.
name|RepositorySearch
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
name|indexer
operator|.
name|search
operator|.
name|SearchResultHit
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
name|indexer
operator|.
name|search
operator|.
name|SearchResultLimits
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
name|indexer
operator|.
name|search
operator|.
name|SearchResults
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
name|FacetedMetadata
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
name|MetadataResolver
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
name|storage
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
name|storage
operator|.
name|maven2
operator|.
name|MavenProjectFacet
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
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|SearchService
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
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|beans
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
name|web
operator|.
name|xmlrpc
operator|.
name|api
operator|.
name|beans
operator|.
name|Dependency
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
name|web
operator|.
name|xmlrpc
operator|.
name|security
operator|.
name|XmlRpcUserRepositories
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
name|SearchServiceImpl
implements|implements
name|SearchService
block|{
specifier|private
name|RepositorySearch
name|search
decl_stmt|;
specifier|private
name|XmlRpcUserRepositories
name|xmlRpcUserRepositories
decl_stmt|;
specifier|private
name|MetadataResolver
name|metadataResolver
decl_stmt|;
specifier|private
name|MetadataRepository
name|metadataRepository
decl_stmt|;
specifier|public
name|SearchServiceImpl
parameter_list|(
name|XmlRpcUserRepositories
name|xmlRpcUserRepositories
parameter_list|,
name|MetadataResolver
name|metadataResolver
parameter_list|,
name|MetadataRepository
name|metadataRepository
parameter_list|,
name|RepositorySearch
name|search
parameter_list|)
block|{
name|this
operator|.
name|xmlRpcUserRepositories
operator|=
name|xmlRpcUserRepositories
expr_stmt|;
name|this
operator|.
name|search
operator|=
name|search
expr_stmt|;
name|this
operator|.
name|metadataResolver
operator|=
name|metadataResolver
expr_stmt|;
name|this
operator|.
name|metadataRepository
operator|=
name|metadataRepository
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|quickSearch
parameter_list|(
name|String
name|queryString
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|observableRepos
init|=
name|xmlRpcUserRepositories
operator|.
name|getObservableRepositories
argument_list|()
decl_stmt|;
name|SearchResultLimits
name|limits
init|=
operator|new
name|SearchResultLimits
argument_list|(
name|SearchResultLimits
operator|.
name|ALL_PAGES
argument_list|)
decl_stmt|;
name|SearchResults
name|results
decl_stmt|;
name|results
operator|=
name|search
operator|.
name|search
argument_list|(
literal|""
argument_list|,
name|observableRepos
argument_list|,
name|queryString
argument_list|,
name|limits
argument_list|,
literal|null
argument_list|)
expr_stmt|;
for|for
control|(
name|SearchResultHit
name|resultHit
range|:
name|results
operator|.
name|getHits
argument_list|()
control|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|resultHitVersions
init|=
name|resultHit
operator|.
name|getVersions
argument_list|()
decl_stmt|;
if|if
condition|(
name|resultHitVersions
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|String
name|version
range|:
name|resultHitVersions
control|)
block|{
name|Artifact
name|artifact
init|=
literal|null
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|observableRepos
control|)
block|{
comment|// slight behaviour change to previous implementation: instead of allocating "jar" when not
comment|// found in the database, we can rely on the metadata repository to create it on the fly. We
comment|// just allocate the default packaging if the Maven facet is not found.
name|FacetedMetadata
name|model
init|=
name|metadataResolver
operator|.
name|getProjectVersion
argument_list|(
name|repoId
argument_list|,
name|resultHit
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|resultHit
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|model
operator|!=
literal|null
condition|)
block|{
name|String
name|packaging
init|=
literal|"jar"
decl_stmt|;
name|MavenProjectFacet
name|facet
init|=
operator|(
name|MavenProjectFacet
operator|)
name|model
operator|.
name|getFacet
argument_list|(
name|MavenProjectFacet
operator|.
name|FACET_ID
argument_list|)
decl_stmt|;
if|if
condition|(
name|facet
operator|!=
literal|null
operator|&&
name|facet
operator|.
name|getPackaging
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|packaging
operator|=
name|facet
operator|.
name|getPackaging
argument_list|()
expr_stmt|;
block|}
name|artifact
operator|=
operator|new
name|Artifact
argument_list|(
name|repoId
argument_list|,
name|resultHit
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|resultHit
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|version
argument_list|,
name|packaging
argument_list|)
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|artifact
operator|!=
literal|null
condition|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|artifacts
return|;
block|}
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|getArtifactByChecksum
parameter_list|(
name|String
name|checksum
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|observableRepos
init|=
name|xmlRpcUserRepositories
operator|.
name|getObservableRepositories
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|Artifact
argument_list|>
name|results
init|=
operator|new
name|ArrayList
argument_list|<
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|observableRepos
control|)
block|{
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|metadataRepository
operator|.
name|getArtifactsByChecksum
argument_list|(
name|repoId
argument_list|,
name|checksum
argument_list|)
control|)
block|{
comment|// TODO: customise XMLRPC to handle non-Maven artifacts
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
name|results
operator|.
name|add
argument_list|(
operator|new
name|Artifact
argument_list|(
name|artifact
operator|.
name|getRepositoryId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|artifact
operator|.
name|getProject
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|facet
operator|!=
literal|null
condition|?
name|facet
operator|.
name|getType
argument_list|()
else|:
literal|null
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|results
return|;
block|}
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|getArtifactVersions
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|observableRepos
init|=
name|xmlRpcUserRepositories
operator|.
name|getObservableRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|observableRepos
control|)
block|{
name|Collection
argument_list|<
name|String
argument_list|>
name|results
init|=
name|metadataResolver
operator|.
name|getProjectVersions
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|)
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|version
range|:
name|results
control|)
block|{
specifier|final
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|"pom"
argument_list|)
decl_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|artifacts
return|;
block|}
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|getArtifactVersionsByDate
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|Date
name|since
parameter_list|)
throws|throws
name|Exception
block|{
comment|//        List<Artifact> artifacts = new ArrayList<Artifact>();
comment|// 1. get observable repositories
comment|// 2. use RepositoryBrowsing method to query uniqueVersions? (but with date)
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"getArtifactVersionsByDate not yet implemented"
argument_list|)
throw|;
comment|//        return artifacts;
block|}
specifier|public
name|List
argument_list|<
name|Dependency
argument_list|>
name|getDependencies
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|String
argument_list|>
name|observableRepos
init|=
name|xmlRpcUserRepositories
operator|.
name|getObservableRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|observableRepos
control|)
block|{
name|ProjectVersionMetadata
name|model
init|=
name|metadataResolver
operator|.
name|getProjectVersion
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|model
operator|!=
literal|null
condition|)
block|{
name|List
argument_list|<
name|Dependency
argument_list|>
name|dependencies
init|=
operator|new
name|ArrayList
argument_list|<
name|Dependency
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
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
name|Dependency
argument_list|>
name|modelDeps
init|=
name|model
operator|.
name|getDependencies
argument_list|()
decl_stmt|;
for|for
control|(
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
name|Dependency
name|dep
range|:
name|modelDeps
control|)
block|{
name|Dependency
name|dependency
init|=
operator|new
name|Dependency
argument_list|(
name|dep
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|dep
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|dep
operator|.
name|getVersion
argument_list|()
argument_list|,
name|dep
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|dep
operator|.
name|getType
argument_list|()
argument_list|,
name|dep
operator|.
name|getScope
argument_list|()
argument_list|)
decl_stmt|;
name|dependencies
operator|.
name|add
argument_list|(
name|dependency
argument_list|)
expr_stmt|;
block|}
return|return
name|dependencies
return|;
block|}
block|}
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Artifact does not exist."
argument_list|)
throw|;
block|}
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|getDependencyTree
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
comment|//        List<Artifact> a = new ArrayList<Artifact>();
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"getDependencyTree not yet implemented"
argument_list|)
throw|;
comment|//        return a;
block|}
specifier|public
name|List
argument_list|<
name|Artifact
argument_list|>
name|getDependees
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|Exception
block|{
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|observableRepos
init|=
name|xmlRpcUserRepositories
operator|.
name|getObservableRepositories
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|repoId
range|:
name|observableRepos
control|)
block|{
name|Collection
argument_list|<
name|ProjectVersionReference
argument_list|>
name|refs
init|=
name|metadataResolver
operator|.
name|getProjectReferences
argument_list|(
name|repoId
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
for|for
control|(
name|ProjectVersionReference
name|ref
range|:
name|refs
control|)
block|{
name|artifacts
operator|.
name|add
argument_list|(
operator|new
name|Artifact
argument_list|(
name|repoId
argument_list|,
name|ref
operator|.
name|getNamespace
argument_list|()
argument_list|,
name|ref
operator|.
name|getProjectId
argument_list|()
argument_list|,
name|ref
operator|.
name|getProjectVersion
argument_list|()
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|artifacts
return|;
block|}
block|}
end_class

end_unit

