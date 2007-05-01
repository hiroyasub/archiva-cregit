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
name|repository
operator|.
name|metadata
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
name|maven
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
name|maven
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
name|maven
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
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * RepositoryMetadataMerge   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
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
name|setLastUpdated
argument_list|(
name|merge
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
name|merged
operator|.
name|setTimestamp
argument_list|(
name|merge
argument_list|(
name|mainSnapshotVersion
operator|.
name|getTimestamp
argument_list|()
argument_list|,
name|sourceSnapshotVersion
operator|.
name|getTimestamp
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|merged
operator|.
name|setBuildNumber
argument_list|(
name|Math
operator|.
name|max
argument_list|(
name|mainSnapshotVersion
operator|.
name|getBuildNumber
argument_list|()
argument_list|,
name|sourceSnapshotVersion
operator|.
name|getBuildNumber
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|merged
return|;
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
name|mergeAvailableVersions
parameter_list|(
name|List
name|mainAvailableVersions
parameter_list|,
name|List
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
name|merged
init|=
name|ArchivaModelCloner
operator|.
name|cloneAvailableVersions
argument_list|(
name|mainAvailableVersions
argument_list|)
decl_stmt|;
name|Iterator
name|it
init|=
name|sourceAvailableVersions
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|sourceVersion
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
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

