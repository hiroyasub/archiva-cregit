begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|metadata
operator|.
name|base
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
name|model
operator|.
name|ArchivaModelCloner
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
name|ArchivaRepositoryMetadata
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
name|Plugin
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
name|SnapshotVersion
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
name|metadata
operator|.
name|RepositoryMetadataException
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
name|lang3
operator|.
name|StringUtils
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
name|List
import|;
end_import

begin_comment
comment|/**  * RepositoryMetadataMerge   *  *  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryMetadataMerge
block|{
specifier|public
specifier|static
name|ArchivaRepositoryMetadata
name|merge
parameter_list|(
specifier|final
name|ArchivaRepositoryMetadata
name|mainMetadata
parameter_list|,
specifier|final
name|ArchivaRepositoryMetadata
name|sourceMetadata
parameter_list|)
throws|throws
name|RepositoryMetadataException
block|{
if|if
condition|(
name|mainMetadata
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryMetadataException
argument_list|(
literal|"Cannot merge a null main project."
argument_list|)
throw|;
block|}
if|if
condition|(
name|sourceMetadata
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryMetadataException
argument_list|(
literal|"Cannot copy to a null parent project."
argument_list|)
throw|;
block|}
name|ArchivaRepositoryMetadata
name|merged
init|=
operator|new
name|ArchivaRepositoryMetadata
argument_list|()
decl_stmt|;
name|merged
operator|.
name|setGroupId
argument_list|(
name|merge
argument_list|(
name|mainMetadata
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|sourceMetadata
operator|.
name|getGroupId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|merged
operator|.
name|setArtifactId
argument_list|(
name|merge
argument_list|(
name|mainMetadata
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|sourceMetadata
operator|.
name|getArtifactId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|merged
operator|.
name|setVersion
argument_list|(
name|merge
argument_list|(
name|mainMetadata
operator|.
name|getVersion
argument_list|()
argument_list|,
name|sourceMetadata
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|merged
operator|.
name|setReleasedVersion
argument_list|(
name|merge
argument_list|(
name|mainMetadata
operator|.
name|getReleasedVersion
argument_list|()
argument_list|,
name|sourceMetadata
operator|.
name|getReleasedVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|merged
operator|.
name|setSnapshotVersion
argument_list|(
name|merge
argument_list|(
name|mainMetadata
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|,
name|sourceMetadata
operator|.
name|getSnapshotVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|merged
operator|.
name|setAvailableVersions
argument_list|(
name|mergeAvailableVersions
argument_list|(
name|mainMetadata
operator|.
name|getAvailableVersions
argument_list|()
argument_list|,
name|sourceMetadata
operator|.
name|getAvailableVersions
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|merged
operator|.
name|setPlugins
argument_list|(
name|mergePlugins
argument_list|(
name|mainMetadata
operator|.
name|getPlugins
argument_list|()
argument_list|,
name|sourceMetadata
operator|.
name|getPlugins
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
comment|//Don't set if merge was not possible
name|long
name|lastUpdated
init|=
name|mergeTimestamp
argument_list|(
name|mainMetadata
operator|.
name|getLastUpdated
argument_list|()
argument_list|,
name|sourceMetadata
operator|.
name|getLastUpdated
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastUpdated
operator|>
operator|-
literal|1
condition|)
block|{
name|merged
operator|.
name|setLastUpdated
argument_list|(
name|Long
operator|.
name|toString
argument_list|(
name|lastUpdated
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|merged
return|;
block|}
specifier|private
specifier|static
name|boolean
name|empty
parameter_list|(
name|String
name|val
parameter_list|)
block|{
if|if
condition|(
name|val
operator|==
literal|null
condition|)
block|{
return|return
literal|true
return|;
block|}
return|return
operator|(
name|val
operator|.
name|trim
argument_list|()
operator|.
name|length
argument_list|()
operator|<=
literal|0
operator|)
return|;
block|}
specifier|private
specifier|static
name|long
name|mergeTimestamp
parameter_list|(
name|String
name|mainTimestamp
parameter_list|,
name|String
name|sourceTimestamp
parameter_list|)
block|{
if|if
condition|(
name|sourceTimestamp
operator|==
literal|null
operator|&&
name|mainTimestamp
operator|!=
literal|null
condition|)
block|{
return|return
name|convertTimestampToLong
argument_list|(
name|mainTimestamp
argument_list|)
return|;
block|}
if|if
condition|(
name|mainTimestamp
operator|==
literal|null
operator|&&
name|sourceTimestamp
operator|!=
literal|null
condition|)
block|{
return|return
name|convertTimestampToLong
argument_list|(
name|sourceTimestamp
argument_list|)
return|;
block|}
if|if
condition|(
name|sourceTimestamp
operator|==
literal|null
operator|&&
name|mainTimestamp
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|mergeTimestamp
argument_list|(
name|convertTimestampToLong
argument_list|(
name|mainTimestamp
argument_list|)
argument_list|,
name|convertTimestampToLong
argument_list|(
name|sourceTimestamp
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|long
name|mergeTimestamp
parameter_list|(
name|long
name|mainTimestamp
parameter_list|,
name|long
name|sourceTimestamp
parameter_list|)
block|{
return|return
name|Math
operator|.
name|max
argument_list|(
name|mainTimestamp
argument_list|,
name|sourceTimestamp
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|SnapshotVersion
name|merge
parameter_list|(
name|SnapshotVersion
name|mainSnapshotVersion
parameter_list|,
name|SnapshotVersion
name|sourceSnapshotVersion
parameter_list|)
block|{
if|if
condition|(
name|sourceSnapshotVersion
operator|==
literal|null
condition|)
block|{
return|return
name|mainSnapshotVersion
return|;
block|}
if|if
condition|(
name|mainSnapshotVersion
operator|==
literal|null
condition|)
block|{
return|return
name|ArchivaModelCloner
operator|.
name|clone
argument_list|(
name|sourceSnapshotVersion
argument_list|)
return|;
block|}
name|SnapshotVersion
name|merged
init|=
operator|new
name|SnapshotVersion
argument_list|()
decl_stmt|;
name|long
name|mainSnapshotLastUpdated
init|=
name|convertTimestampToLong
argument_list|(
name|mainSnapshotVersion
operator|.
name|getTimestamp
argument_list|()
argument_list|)
decl_stmt|;
name|long
name|sourceSnapshotLastUpdated
init|=
name|convertTimestampToLong
argument_list|(
name|sourceSnapshotVersion
operator|.
name|getTimestamp
argument_list|()
argument_list|)
decl_stmt|;
name|long
name|lastUpdated
init|=
name|mergeTimestamp
argument_list|(
name|mainSnapshotLastUpdated
argument_list|,
name|sourceSnapshotLastUpdated
argument_list|)
decl_stmt|;
if|if
condition|(
name|lastUpdated
operator|==
name|mainSnapshotLastUpdated
condition|)
block|{
name|merged
operator|.
name|setTimestamp
argument_list|(
name|mainSnapshotVersion
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
name|merged
operator|.
name|setBuildNumber
argument_list|(
name|mainSnapshotVersion
operator|.
name|getBuildNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|merged
operator|.
name|setTimestamp
argument_list|(
name|sourceSnapshotVersion
operator|.
name|getTimestamp
argument_list|()
argument_list|)
expr_stmt|;
name|merged
operator|.
name|setBuildNumber
argument_list|(
name|sourceSnapshotVersion
operator|.
name|getBuildNumber
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|merged
return|;
block|}
specifier|private
specifier|static
name|long
name|convertTimestampToLong
parameter_list|(
name|String
name|timestamp
parameter_list|)
block|{
if|if
condition|(
name|timestamp
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
return|return
name|getLongFromTimestampSafely
argument_list|(
name|StringUtils
operator|.
name|replace
argument_list|(
name|timestamp
argument_list|,
literal|"."
argument_list|,
literal|""
argument_list|)
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|long
name|getLongFromTimestampSafely
parameter_list|(
name|String
name|timestampString
parameter_list|)
block|{
try|try
block|{
return|return
name|Long
operator|.
name|parseLong
argument_list|(
name|timestampString
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NumberFormatException
name|e
parameter_list|)
block|{
return|return
operator|-
literal|1
return|;
block|}
block|}
specifier|private
specifier|static
name|String
name|merge
parameter_list|(
name|String
name|main
parameter_list|,
name|String
name|source
parameter_list|)
block|{
if|if
condition|(
name|empty
argument_list|(
name|main
argument_list|)
operator|&&
operator|!
name|empty
argument_list|(
name|source
argument_list|)
condition|)
block|{
return|return
name|source
return|;
block|}
return|return
name|main
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|Plugin
argument_list|>
name|mergePlugins
parameter_list|(
name|List
argument_list|<
name|Plugin
argument_list|>
name|mainPlugins
parameter_list|,
name|List
argument_list|<
name|Plugin
argument_list|>
name|sourcePlugins
parameter_list|)
block|{
if|if
condition|(
name|sourcePlugins
operator|==
literal|null
condition|)
block|{
return|return
name|mainPlugins
return|;
block|}
if|if
condition|(
name|mainPlugins
operator|==
literal|null
condition|)
block|{
return|return
name|clonePlugins
argument_list|(
name|sourcePlugins
argument_list|)
return|;
block|}
name|List
argument_list|<
name|Plugin
argument_list|>
name|merged
init|=
name|clonePlugins
argument_list|(
name|mainPlugins
argument_list|)
decl_stmt|;
for|for
control|(
name|Plugin
name|plugin
range|:
name|sourcePlugins
control|)
block|{
if|if
condition|(
operator|!
name|merged
operator|.
name|contains
argument_list|(
name|plugin
argument_list|)
condition|)
block|{
name|merged
operator|.
name|add
argument_list|(
name|plugin
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|merged
return|;
block|}
comment|/**      * Clones a list of plugins.      *       * This method exists because ArchivaModelCloner.clonePlugins()       * only works with artifact references.      *       * @param plugins      * @return list of cloned plugins      */
specifier|private
specifier|static
name|List
argument_list|<
name|Plugin
argument_list|>
name|clonePlugins
parameter_list|(
name|List
argument_list|<
name|Plugin
argument_list|>
name|plugins
parameter_list|)
block|{
if|if
condition|(
name|plugins
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|List
argument_list|<
name|Plugin
argument_list|>
name|result
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
for|for
control|(
name|Plugin
name|plugin
range|:
name|plugins
control|)
block|{
name|Plugin
name|clonedPlugin
init|=
operator|new
name|Plugin
argument_list|()
decl_stmt|;
name|clonedPlugin
operator|.
name|setArtifactId
argument_list|(
name|plugin
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|clonedPlugin
operator|.
name|setName
argument_list|(
name|plugin
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|clonedPlugin
operator|.
name|setPrefix
argument_list|(
name|plugin
operator|.
name|getPrefix
argument_list|()
argument_list|)
expr_stmt|;
name|result
operator|.
name|add
argument_list|(
name|plugin
argument_list|)
expr_stmt|;
block|}
return|return
name|result
return|;
block|}
specifier|private
specifier|static
name|List
argument_list|<
name|String
argument_list|>
name|mergeAvailableVersions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|mainAvailableVersions
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|sourceAvailableVersions
parameter_list|)
block|{
if|if
condition|(
name|sourceAvailableVersions
operator|==
literal|null
condition|)
block|{
return|return
name|mainAvailableVersions
return|;
block|}
if|if
condition|(
name|mainAvailableVersions
operator|==
literal|null
condition|)
block|{
return|return
name|ArchivaModelCloner
operator|.
name|cloneAvailableVersions
argument_list|(
name|sourceAvailableVersions
argument_list|)
return|;
block|}
name|List
argument_list|<
name|String
argument_list|>
name|merged
init|=
name|ArchivaModelCloner
operator|.
name|cloneAvailableVersions
argument_list|(
name|mainAvailableVersions
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|sourceVersion
range|:
name|sourceAvailableVersions
control|)
block|{
if|if
condition|(
operator|!
name|merged
operator|.
name|contains
argument_list|(
name|sourceVersion
argument_list|)
condition|)
block|{
name|merged
operator|.
name|add
argument_list|(
name|sourceVersion
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|merged
return|;
block|}
block|}
end_class

end_unit
