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
name|database
operator|.
name|browsing
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|collections
operator|.
name|CollectionUtils
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
name|collections
operator|.
name|PredicateUtils
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
name|collections
operator|.
name|functors
operator|.
name|NotPredicate
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
name|lang
operator|.
name|StringUtils
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
name|constraints
operator|.
name|ArtifactsRelatedConstraint
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
name|ProjectsByArtifactUsageConstraint
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
name|UniqueArtifactIdConstraint
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
name|UniqueGroupIdConstraint
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
name|database
operator|.
name|updater
operator|.
name|DatabaseUpdater
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|Keys
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
comment|/**  * DefaultRepositoryBrowsing  *  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.database.browsing.RepositoryBrowsing"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultRepositoryBrowsing
implements|implements
name|RepositoryBrowsing
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|DefaultRepositoryBrowsing
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|DatabaseUpdater
name|dbUpdater
decl_stmt|;
comment|/**      * @see RepositoryBrowsing#getRoot(String, List)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|BrowsingResults
name|getRoot
parameter_list|(
specifier|final
name|String
name|principal
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|)
block|{
specifier|final
name|BrowsingResults
name|results
init|=
operator|new
name|BrowsingResults
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|observableRepositoryIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|groups
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueGroupIdConstraint
argument_list|(
name|observableRepositoryIds
argument_list|)
argument_list|)
decl_stmt|;
name|results
operator|.
name|setSelectedRepositoryIds
argument_list|(
name|observableRepositoryIds
argument_list|)
expr_stmt|;
name|results
operator|.
name|setGroupIds
argument_list|(
name|GroupIdFilter
operator|.
name|filterGroups
argument_list|(
name|groups
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
comment|/**      * @see RepositoryBrowsing#selectArtifactId(String, List, String, String)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|BrowsingResults
name|selectArtifactId
parameter_list|(
specifier|final
name|String
name|principal
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|,
specifier|final
name|String
name|groupId
parameter_list|,
specifier|final
name|String
name|artifactId
parameter_list|)
block|{
specifier|final
name|BrowsingResults
name|results
init|=
operator|new
name|BrowsingResults
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|observableRepositoryIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// NOTE: No group Id or artifact Id's should be returned here.
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
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueVersionConstraint
argument_list|(
name|observableRepositoryIds
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|)
argument_list|)
decl_stmt|;
name|results
operator|.
name|setSelectedRepositoryIds
argument_list|(
name|observableRepositoryIds
argument_list|)
expr_stmt|;
name|results
operator|.
name|setVersions
argument_list|(
name|processSnapshots
argument_list|(
name|versions
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
comment|/**      * @see RepositoryBrowsing#selectGroupId(String, List, String)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|BrowsingResults
name|selectGroupId
parameter_list|(
specifier|final
name|String
name|principal
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|,
specifier|final
name|String
name|groupId
parameter_list|)
block|{
specifier|final
name|BrowsingResults
name|results
init|=
operator|new
name|BrowsingResults
argument_list|(
name|groupId
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|observableRepositoryIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|groups
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueGroupIdConstraint
argument_list|(
name|observableRepositoryIds
argument_list|,
name|groupId
argument_list|)
argument_list|)
decl_stmt|;
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|artifacts
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueArtifactIdConstraint
argument_list|(
name|observableRepositoryIds
argument_list|,
name|groupId
argument_list|)
argument_list|)
decl_stmt|;
comment|// Remove searched for groupId from groups list.
comment|// Easier to do this here, vs doing it in the SQL query.
name|CollectionUtils
operator|.
name|filter
argument_list|(
name|groups
argument_list|,
name|NotPredicate
operator|.
name|getInstance
argument_list|(
name|PredicateUtils
operator|.
name|equalPredicate
argument_list|(
name|groupId
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|results
operator|.
name|setGroupIds
argument_list|(
name|groups
argument_list|)
expr_stmt|;
name|results
operator|.
name|setArtifacts
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
comment|/**      * @see RepositoryBrowsing#selectVersion(String, List, String, String, String)      */
specifier|public
name|ArchivaProjectModel
name|selectVersion
parameter_list|(
specifier|final
name|String
name|principal
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|,
specifier|final
name|String
name|groupId
parameter_list|,
specifier|final
name|String
name|artifactId
parameter_list|,
specifier|final
name|String
name|version
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
if|if
condition|(
name|observableRepositoryIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ArchivaDatabaseException
argument_list|(
literal|"There are no observable repositories for the user "
operator|+
name|principal
argument_list|)
throw|;
block|}
name|ArchivaArtifact
name|pomArtifact
init|=
name|getArtifact
argument_list|(
name|principal
argument_list|,
name|observableRepositoryIds
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|ArchivaProjectModel
name|model
decl_stmt|;
if|if
condition|(
operator|!
name|pomArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|isProcessed
argument_list|()
condition|)
block|{
comment|// Process it.
name|dbUpdater
operator|.
name|updateUnprocessed
argument_list|(
name|pomArtifact
argument_list|)
expr_stmt|;
block|}
name|model
operator|=
name|getProjectModel
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|pomArtifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|model
return|;
block|}
specifier|public
name|String
name|getRepositoryId
parameter_list|(
specifier|final
name|String
name|principal
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|,
specifier|final
name|String
name|groupId
parameter_list|,
specifier|final
name|String
name|artifactId
parameter_list|,
specifier|final
name|String
name|version
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
if|if
condition|(
name|observableRepositoryIds
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|ArchivaDatabaseException
argument_list|(
literal|"There are no observable repositories for the user "
operator|+
name|principal
argument_list|)
throw|;
block|}
try|try
block|{
name|ArchivaArtifact
name|pomArchivaArtifact
init|=
name|getArtifact
argument_list|(
name|principal
argument_list|,
name|observableRepositoryIds
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
return|return
name|pomArchivaArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
return|return
name|getNoPomArtifactRepoId
argument_list|(
name|principal
argument_list|,
name|observableRepositoryIds
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|observableRepositoryIds
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
block|}
comment|/**      * @see RepositoryBrowsing#getOtherSnapshotVersions(List, String, String, String)      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOtherSnapshotVersions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|,
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
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|List
argument_list|<
name|String
argument_list|>
name|timestampedVersions
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|version
argument_list|)
condition|)
block|{
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
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueVersionConstraint
argument_list|(
name|observableRepositoryIds
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|)
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|uniqueVersion
range|:
name|versions
control|)
block|{
if|if
condition|(
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|uniqueVersion
argument_list|)
operator|.
name|equals
argument_list|(
name|version
argument_list|)
operator|||
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|uniqueVersion
argument_list|)
operator|.
name|equals
argument_list|(
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|version
argument_list|)
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|timestampedVersions
operator|.
name|contains
argument_list|(
name|uniqueVersion
argument_list|)
condition|)
block|{
name|timestampedVersions
operator|.
name|add
argument_list|(
name|uniqueVersion
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
name|timestampedVersions
return|;
block|}
specifier|private
name|ArchivaArtifact
name|getArtifact
parameter_list|(
specifier|final
name|String
name|principal
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|,
specifier|final
name|String
name|groupId
parameter_list|,
specifier|final
name|String
name|artifactId
parameter_list|,
specifier|final
name|String
name|version
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|ArchivaArtifact
name|pomArtifact
init|=
literal|null
decl_stmt|;
for|for
control|(
specifier|final
name|String
name|repositoryId
range|:
name|observableRepositoryIds
control|)
block|{
try|try
block|{
name|pomArtifact
operator|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
break|break;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
name|pomArtifact
operator|=
name|handleGenericSnapshots
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
block|}
block|}
if|if
condition|(
name|pomArtifact
operator|==
literal|null
condition|)
block|{
name|String
name|type
init|=
name|getArtifactType
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
comment|// We dont want these to persist in the database
name|pomArtifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
name|type
argument_list|,
name|observableRepositoryIds
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
name|pomArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenProcessed
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Allowed to see this?
if|if
condition|(
name|observableRepositoryIds
operator|.
name|contains
argument_list|(
name|pomArtifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|pomArtifact
return|;
block|}
else|else
block|{
throw|throw
operator|new
name|ObjectNotFoundException
argument_list|(
literal|"Unable to find artifact "
operator|+
name|Keys
operator|.
name|toKey
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
operator|+
literal|" in observable repository ["
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|observableRepositoryIds
operator|.
name|iterator
argument_list|()
argument_list|,
literal|", "
argument_list|)
operator|+
literal|"] for user "
operator|+
name|principal
argument_list|)
throw|;
block|}
block|}
specifier|public
name|List
argument_list|<
name|ArchivaProjectModel
argument_list|>
name|getUsedBy
parameter_list|(
specifier|final
name|String
name|principal
parameter_list|,
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositoryIds
parameter_list|,
specifier|final
name|String
name|groupId
parameter_list|,
specifier|final
name|String
name|artifactId
parameter_list|,
specifier|final
name|String
name|version
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|ProjectsByArtifactUsageConstraint
name|constraint
init|=
operator|new
name|ProjectsByArtifactUsageConstraint
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArchivaProjectModel
argument_list|>
name|results
init|=
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|queryProjectModels
argument_list|(
name|constraint
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|==
literal|null
condition|)
block|{
comment|// defensive. to honor contract as specified. never null.
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
return|return
name|results
return|;
block|}
comment|/**      * Removes SNAPSHOT versions with build numbers. Retains only the generic SNAPSHOT version.       * Example, if the list of versions are:       * - 2.0       * - 2.0.1       * - 2.1-20070522.143249-1       * - 2.1-20070522.157829-2       *       * the returned version list would contain 2.0, 2.0.1 and 2.1-SNAPSHOT.      *       * @param versions      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|processSnapshots
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|versions
parameter_list|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|cleansedVersions
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
name|String
name|version
range|:
name|versions
control|)
block|{
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|version
argument_list|)
condition|)
block|{
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
name|cleansedVersions
operator|.
name|contains
argument_list|(
name|baseVersion
argument_list|)
condition|)
block|{
name|cleansedVersions
operator|.
name|add
argument_list|(
name|baseVersion
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|cleansedVersions
operator|.
name|add
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|cleansedVersions
return|;
block|}
comment|/**      * Handles querying of generic (*-SNAPSHOT) snapshot version. Process: - Get all the timestamped/unique versions of      * the artifact from the db - Sort the queried project models - Reverse the list of queried project models to get      * the latest timestamp version - Loop through the list and get the first one to match the generic (*-SNAPHOT)      * version      *       * @param groupId      * @param artifactId      * @param version      * @param pomArtifact      * @throws ArchivaDatabaseException      */
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|ArchivaArtifact
name|handleGenericSnapshots
parameter_list|(
specifier|final
name|String
name|groupId
parameter_list|,
specifier|final
name|String
name|artifactId
parameter_list|,
specifier|final
name|String
name|version
parameter_list|,
specifier|final
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaDatabaseException
block|{
name|ArchivaArtifact
name|result
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|VersionUtil
operator|.
name|isGenericSnapshot
argument_list|(
name|version
argument_list|)
condition|)
block|{
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
name|dao
operator|.
name|query
argument_list|(
operator|new
name|UniqueVersionConstraint
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|versions
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|reverse
argument_list|(
name|versions
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|uniqueVersion
range|:
name|versions
control|)
block|{
if|if
condition|(
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|uniqueVersion
argument_list|)
operator|.
name|equals
argument_list|(
name|version
argument_list|)
condition|)
block|{
try|try
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Retrieving artifact with version "
operator|+
name|uniqueVersion
argument_list|)
expr_stmt|;
name|result
operator|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|getArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|uniqueVersion
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
break|break;
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
literal|"Artifact '"
operator|+
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|uniqueVersion
operator|+
literal|"' in repository '"
operator|+
name|repositoryId
operator|+
literal|"' not found in the database."
argument_list|)
expr_stmt|;
continue|continue;
block|}
block|}
block|}
block|}
return|return
name|result
return|;
block|}
comment|/**      * Get the project model from the database.      *       * @param groupId      * @param artifactId      * @param version      * @return      * @throws ArchivaDatabaseException      */
specifier|private
name|ArchivaProjectModel
name|getProjectModel
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
name|ArchivaDatabaseException
block|{
name|ArchivaProjectModel
name|model
init|=
literal|null
decl_stmt|;
try|try
block|{
name|model
operator|=
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|getProjectModel
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
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
literal|"Unable to find project model for ["
operator|+
name|Keys
operator|.
name|toKey
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
operator|+
literal|"]"
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|model
operator|==
literal|null
condition|)
block|{
name|model
operator|=
operator|new
name|ArchivaProjectModel
argument_list|()
expr_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
block|}
return|return
name|model
return|;
block|}
specifier|private
name|String
name|getNoPomArtifactRepoId
parameter_list|(
name|String
name|principal
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|observableRepos
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|ArchivaArtifact
name|artifact
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
name|getArtifactType
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|artifact
operator|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
name|type
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
if|if
condition|(
name|artifact
operator|==
literal|null
condition|)
block|{
comment|// Lets not persist these
name|artifact
operator|=
operator|new
name|ArchivaArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|null
argument_list|,
name|type
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
block|}
comment|// Allowed to see this?
if|if
condition|(
operator|!
name|observableRepos
operator|.
name|contains
argument_list|(
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ObjectNotFoundException
argument_list|(
literal|"Unable to find artifact "
operator|+
name|Keys
operator|.
name|toKey
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
operator|+
literal|" in observable repository ["
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|observableRepos
operator|.
name|iterator
argument_list|()
argument_list|,
literal|", "
argument_list|)
operator|+
literal|"] for user "
operator|+
name|principal
argument_list|)
throw|;
block|}
return|return
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
return|;
block|}
specifier|private
name|String
name|getArtifactType
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
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
block|{
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|queryArtifacts
argument_list|(
operator|new
name|ArtifactsRelatedConstraint
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifacts
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|type
operator|=
name|artifacts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|()
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
comment|// swallow exception?
block|}
return|return
name|type
return|;
block|}
block|}
end_class

end_unit

