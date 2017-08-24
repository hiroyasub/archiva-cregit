begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|core
operator|.
name|repository
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
name|repository
operator|.
name|events
operator|.
name|RepositoryListener
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
name|time
operator|.
name|DateUtils
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
name|common
operator|.
name|utils
operator|.
name|VersionComparator
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
name|archiva
operator|.
name|model
operator|.
name|VersionedReference
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
name|repository
operator|.
name|ContentNotFoundException
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
name|repository
operator|.
name|ManagedRepositoryContent
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
name|repository
operator|.
name|layout
operator|.
name|LayoutException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|SimpleDateFormat
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_comment
comment|/**  * Purge from repository all snapshots older than the specified days in the repository configuration.  */
end_comment

begin_class
specifier|public
class|class
name|DaysOldRepositoryPurge
extends|extends
name|AbstractRepositoryPurge
block|{
specifier|private
name|SimpleDateFormat
name|timestampParser
decl_stmt|;
specifier|private
name|int
name|daysOlder
decl_stmt|;
specifier|private
name|int
name|retentionCount
decl_stmt|;
specifier|public
name|DaysOldRepositoryPurge
parameter_list|(
name|ManagedRepositoryContent
name|repository
parameter_list|,
name|int
name|daysOlder
parameter_list|,
name|int
name|retentionCount
parameter_list|,
name|RepositorySession
name|repositorySession
parameter_list|,
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|super
argument_list|(
name|repository
argument_list|,
name|repositorySession
argument_list|,
name|listeners
argument_list|)
expr_stmt|;
name|this
operator|.
name|daysOlder
operator|=
name|daysOlder
expr_stmt|;
name|this
operator|.
name|retentionCount
operator|=
name|retentionCount
expr_stmt|;
name|timestampParser
operator|=
operator|new
name|SimpleDateFormat
argument_list|(
literal|"yyyyMMdd.HHmmss"
argument_list|)
expr_stmt|;
name|timestampParser
operator|.
name|setTimeZone
argument_list|(
name|DateUtils
operator|.
name|UTC_TIME_ZONE
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|process
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|RepositoryPurgeException
block|{
try|try
block|{
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getRepoRoot
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifactFile
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return;
block|}
name|ArtifactReference
name|artifact
init|=
name|repository
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Calendar
name|olderThanThisDate
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|DateUtils
operator|.
name|UTC_TIME_ZONE
argument_list|)
decl_stmt|;
name|olderThanThisDate
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|DATE
argument_list|,
operator|-
name|daysOlder
argument_list|)
expr_stmt|;
comment|// respect retention count
name|VersionedReference
name|reference
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|repository
operator|.
name|getVersions
argument_list|(
name|reference
argument_list|)
argument_list|)
decl_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|versions
argument_list|,
name|VersionComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|retentionCount
operator|>
name|versions
operator|.
name|size
argument_list|()
condition|)
block|{
comment|// Done. nothing to do here. skip it.
return|return;
block|}
name|int
name|countToPurge
init|=
name|versions
operator|.
name|size
argument_list|()
operator|-
name|retentionCount
decl_stmt|;
name|Set
argument_list|<
name|ArtifactReference
argument_list|>
name|artifactsToDelete
init|=
operator|new
name|HashSet
argument_list|<>
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
name|countToPurge
operator|--
operator|<=
literal|0
condition|)
block|{
break|break;
block|}
name|ArtifactReference
name|newArtifactReference
init|=
name|repository
operator|.
name|toArtifactReference
argument_list|(
name|artifactFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
decl_stmt|;
name|newArtifactReference
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|File
name|newArtifactFile
init|=
name|repository
operator|.
name|toFile
argument_list|(
name|newArtifactReference
argument_list|)
decl_stmt|;
comment|// Is this a generic snapshot "1.0-SNAPSHOT" ?
if|if
condition|(
name|VersionUtil
operator|.
name|isGenericSnapshot
argument_list|(
name|newArtifactReference
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
name|newArtifactFile
operator|.
name|lastModified
argument_list|()
operator|<
name|olderThanThisDate
operator|.
name|getTimeInMillis
argument_list|()
condition|)
block|{
name|artifactsToDelete
operator|.
name|addAll
argument_list|(
name|repository
operator|.
name|getRelatedArtifacts
argument_list|(
name|newArtifactReference
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Is this a timestamp snapshot "1.0-20070822.123456-42" ?
if|else if
condition|(
name|VersionUtil
operator|.
name|isUniqueSnapshot
argument_list|(
name|newArtifactReference
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|Calendar
name|timestampCal
init|=
name|uniqueSnapshotToCalendar
argument_list|(
name|newArtifactReference
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|timestampCal
operator|.
name|getTimeInMillis
argument_list|()
operator|<
name|olderThanThisDate
operator|.
name|getTimeInMillis
argument_list|()
condition|)
block|{
name|artifactsToDelete
operator|.
name|addAll
argument_list|(
name|repository
operator|.
name|getRelatedArtifacts
argument_list|(
name|newArtifactReference
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|purge
argument_list|(
name|artifactsToDelete
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ContentNotFoundException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryPurgeException
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
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Not processing file that is not an artifact: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|Calendar
name|uniqueSnapshotToCalendar
parameter_list|(
name|String
name|version
parameter_list|)
block|{
comment|// The latestVersion will contain the full version string "1.0-alpha-5-20070821.213044-8"
comment|// This needs to be broken down into ${base}-${timestamp}-${build_number}
name|Matcher
name|m
init|=
name|VersionUtil
operator|.
name|UNIQUE_SNAPSHOT_PATTERN
operator|.
name|matcher
argument_list|(
name|version
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|Matcher
name|mtimestamp
init|=
name|VersionUtil
operator|.
name|TIMESTAMP_PATTERN
operator|.
name|matcher
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|mtimestamp
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|tsDate
init|=
name|mtimestamp
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
name|String
name|tsTime
init|=
name|mtimestamp
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
name|Date
name|versionDate
decl_stmt|;
try|try
block|{
name|versionDate
operator|=
name|timestampParser
operator|.
name|parse
argument_list|(
name|tsDate
operator|+
literal|"."
operator|+
name|tsTime
argument_list|)
expr_stmt|;
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|(
name|DateUtils
operator|.
name|UTC_TIME_ZONE
argument_list|)
decl_stmt|;
name|cal
operator|.
name|setTime
argument_list|(
name|versionDate
argument_list|)
expr_stmt|;
return|return
name|cal
return|;
block|}
catch|catch
parameter_list|(
name|ParseException
name|e
parameter_list|)
block|{
comment|// Invalid Date/Time
return|return
literal|null
return|;
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

