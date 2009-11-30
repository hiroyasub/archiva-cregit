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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|VersionUtil
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
name|database
operator|.
name|ArchivaDAO
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
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|ArtifactDAO
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
name|database
operator|.
name|ObjectNotFoundException
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
name|database
operator|.
name|browsing
operator|.
name|BrowsingResults
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
name|database
operator|.
name|browsing
operator|.
name|RepositoryBrowsing
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
name|database
operator|.
name|constraints
operator|.
name|ArtifactsByChecksumConstraint
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
name|database
operator|.
name|constraints
operator|.
name|UniqueVersionConstraint
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
name|ArchivaArtifact
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
name|ArchivaProjectModel
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_comment
comment|/**  * SearchServiceImpl  *  * quick/general text search which returns a list of artifacts  * query for an artifact based on a checksum  * query for all available versions of an artifact, sorted in version significance order  * query for all available versions of an artifact since a given date  * query for an artifact's direct dependencies  * query for an artifact's dependency tree (as with mvn dependency:tree - no duplicates should be included)  * query for all artifacts that depend on a given artifact  *  * @version $Id: SearchServiceImpl.java  */
end_comment

begin_class
specifier|public
class|class
name|SearchServiceImpl
implements|implements
name|SearchService
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|SearchServiceImpl
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|RepositorySearch
name|search
decl_stmt|;
specifier|private
name|XmlRpcUserRepositories
name|xmlRpcUserRepositories
decl_stmt|;
specifier|private
name|ArchivaDAO
name|archivaDAO
decl_stmt|;
specifier|private
name|RepositoryBrowsing
name|repoBrowsing
decl_stmt|;
specifier|private
name|MetadataResolver
name|metadataResolver
decl_stmt|;
specifier|public
name|SearchServiceImpl
parameter_list|(
name|XmlRpcUserRepositories
name|xmlRpcUserRepositories
parameter_list|,
name|ArchivaDAO
name|archivaDAO
parameter_list|,
name|RepositoryBrowsing
name|repoBrowsing
parameter_list|,
name|MetadataResolver
name|metadataResolver
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
name|archivaDAO
operator|=
name|archivaDAO
expr_stmt|;
name|this
operator|.
name|repoBrowsing
operator|=
name|repoBrowsing
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
init|=
literal|null
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
comment|// double-check all versions as done in SearchAction
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|archivaDAO
operator|.
name|query
argument_list|(
operator|new
name|UniqueVersionConstraint
argument_list|(
name|observableRepos
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
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|versions
operator|!=
literal|null
operator|&&
operator|!
name|versions
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|resultHit
operator|.
name|setVersion
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|resultHit
operator|.
name|setVersions
argument_list|(
name|filterTimestampedSnapshots
argument_list|(
name|versions
argument_list|)
argument_list|)
expr_stmt|;
block|}
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
try|try
block|{
name|ArchivaProjectModel
name|model
init|=
name|repoBrowsing
operator|.
name|selectVersion
argument_list|(
literal|""
argument_list|,
name|observableRepos
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
name|String
name|repoId
init|=
name|repoBrowsing
operator|.
name|getRepositoryId
argument_list|(
literal|""
argument_list|,
name|observableRepos
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
name|Artifact
name|artifact
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
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
literal|"jar"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|artifact
operator|=
operator|new
name|Artifact
argument_list|(
name|repoId
argument_list|,
name|model
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|version
argument_list|,
name|model
operator|.
name|getPackaging
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Unable to find pom artifact : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Error occurred while getting pom artifact from database : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
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
comment|/**      * Remove timestamped snapshots from versions      */
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|filterTimestampedSnapshots
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|versions
parameter_list|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|filtered
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|version
range|:
name|versions
control|)
block|{
specifier|final
name|String
name|baseVersion
init|=
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|filtered
operator|.
name|contains
argument_list|(
name|baseVersion
argument_list|)
condition|)
block|{
name|filtered
operator|.
name|add
argument_list|(
name|baseVersion
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|filtered
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
comment|// 1. get ArtifactDAO from ArchivaDAO
comment|// 2. create ArtifactsByChecksumConstraint( "queryTerm" )
comment|// 3. query artifacts using constraint
comment|// 4. convert results to list of Artifact objects
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
name|ArtifactDAO
name|artifactDAO
init|=
name|archivaDAO
operator|.
name|getArtifactDAO
argument_list|()
decl_stmt|;
name|ArtifactsByChecksumConstraint
name|constraint
init|=
operator|new
name|ArtifactsByChecksumConstraint
argument_list|(
name|checksum
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|artifactDAO
operator|.
name|queryArtifacts
argument_list|(
name|constraint
argument_list|)
decl_stmt|;
for|for
control|(
name|ArchivaArtifact
name|archivaArtifact
range|:
name|artifacts
control|)
block|{
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|(
name|archivaArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
argument_list|,
name|archivaArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|archivaArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|archivaArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|,
name|archivaArtifact
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
comment|//archivaArtifact.getModel().getWhenGathered() );
name|results
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
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
specifier|final
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
specifier|final
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
specifier|final
name|BrowsingResults
name|results
init|=
name|repoBrowsing
operator|.
name|selectArtifactId
argument_list|(
literal|""
argument_list|,
name|observableRepos
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
operator|.
name|getVersions
argument_list|()
control|)
block|{
specifier|final
name|Artifact
name|artifact
init|=
operator|new
name|Artifact
argument_list|(
literal|""
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
comment|//ArchivaArtifact pomArtifact = artifactDAO.getArtifact( groupId, artifactId, version, "", "pom",  );
comment|//Artifact artifact = new Artifact( "", groupId, artifactId, version, pomArtifact.getType() );
comment|//pomArtifact.getModel().getWhenGathered() );
name|artifacts
operator|.
name|add
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
comment|// 1. get observable repositories
comment|// 2. use RepositoryBrowsing method to query uniqueVersions?
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
comment|// 1. get observable repositories
comment|// 2. use RepositoryBrowsing method to query uniqueVersions? (but with date)
return|return
name|artifacts
return|;
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
name|String
argument_list|>
name|observableRepos
init|=
name|xmlRpcUserRepositories
operator|.
name|getObservableRepositories
argument_list|()
decl_stmt|;
try|try
block|{
name|ArchivaProjectModel
name|model
init|=
name|repoBrowsing
operator|.
name|selectVersion
argument_list|(
literal|""
argument_list|,
name|observableRepos
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|List
argument_list|<
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
name|maven
operator|.
name|archiva
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
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|oe
parameter_list|)
block|{
throw|throw
operator|new
name|Exception
argument_list|(
literal|"Artifact does not exist."
argument_list|)
throw|;
block|}
return|return
name|dependencies
return|;
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
name|List
argument_list|<
name|Artifact
argument_list|>
name|a
init|=
operator|new
name|ArrayList
argument_list|<
name|Artifact
argument_list|>
argument_list|()
decl_stmt|;
return|return
name|a
return|;
block|}
comment|//get artifacts that depend on a given artifact
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

