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
name|reporting
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|reporting
operator|.
name|model
operator|.
name|ArtifactResults
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
name|reporting
operator|.
name|model
operator|.
name|MetadataResults
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
name|reporting
operator|.
name|model
operator|.
name|Reporting
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
name|reporting
operator|.
name|model
operator|.
name|Result
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
name|artifact
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
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
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
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|RepositoryMetadata
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
name|HashMap
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
name|Map
import|;
end_import

begin_comment
comment|/**  * @todo i18n, including message formatting and parameterisation  */
end_comment

begin_class
specifier|public
class|class
name|ReportingDatabase
block|{
specifier|private
specifier|final
name|Reporting
name|reporting
decl_stmt|;
specifier|private
name|Map
name|artifactMap
decl_stmt|;
specifier|private
name|Map
name|metadataMap
decl_stmt|;
specifier|private
name|int
name|numFailures
decl_stmt|;
specifier|private
name|int
name|numWarnings
decl_stmt|;
specifier|private
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|private
name|boolean
name|inProgress
decl_stmt|;
specifier|private
name|long
name|startTime
decl_stmt|;
specifier|public
name|ReportingDatabase
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|Reporting
argument_list|()
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ReportingDatabase
parameter_list|(
name|Reporting
name|reporting
parameter_list|)
block|{
name|this
argument_list|(
name|reporting
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ReportingDatabase
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|)
block|{
name|this
argument_list|(
operator|new
name|Reporting
argument_list|()
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ReportingDatabase
parameter_list|(
name|Reporting
name|reporting
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
block|{
name|this
operator|.
name|reporting
operator|=
name|reporting
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|initArtifactMap
argument_list|()
expr_stmt|;
name|initMetadataMap
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addFailure
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|ArtifactResults
name|results
init|=
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|results
operator|.
name|addFailure
argument_list|(
name|createResults
argument_list|(
name|reason
argument_list|)
argument_list|)
expr_stmt|;
name|numFailures
operator|++
expr_stmt|;
name|updateTimings
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addWarning
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|ArtifactResults
name|results
init|=
name|getArtifactResults
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|results
operator|.
name|addWarning
argument_list|(
name|createResults
argument_list|(
name|reason
argument_list|)
argument_list|)
expr_stmt|;
name|numWarnings
operator|++
expr_stmt|;
name|updateTimings
argument_list|()
expr_stmt|;
block|}
specifier|private
name|ArtifactResults
name|getArtifactResults
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|Map
name|artifactMap
init|=
name|this
operator|.
name|artifactMap
decl_stmt|;
name|String
name|key
init|=
name|getArtifactKey
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|,
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
decl_stmt|;
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|artifactMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|==
literal|null
condition|)
block|{
name|results
operator|=
operator|new
name|ArtifactResults
argument_list|()
expr_stmt|;
name|results
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|.
name|setClassifier
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|.
name|setType
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|artifactMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|results
argument_list|)
expr_stmt|;
name|reporting
operator|.
name|getArtifacts
argument_list|()
operator|.
name|add
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
specifier|private
name|void
name|initArtifactMap
parameter_list|()
block|{
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|reporting
operator|.
name|getArtifacts
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|ArtifactResults
name|result
init|=
operator|(
name|ArtifactResults
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|getArtifactKey
argument_list|(
name|result
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|result
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|result
operator|.
name|getVersion
argument_list|()
argument_list|,
name|result
operator|.
name|getType
argument_list|()
argument_list|,
name|result
operator|.
name|getClassifier
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|numFailures
operator|+=
name|result
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|numWarnings
operator|+=
name|result
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
name|artifactMap
operator|=
name|map
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getArtifactKey
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
name|String
name|type
parameter_list|,
name|String
name|classifier
parameter_list|)
block|{
return|return
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
operator|+
literal|":"
operator|+
name|type
operator|+
literal|":"
operator|+
name|classifier
return|;
block|}
specifier|private
specifier|static
name|Result
name|createResults
parameter_list|(
name|String
name|reason
parameter_list|)
block|{
name|Result
name|result
init|=
operator|new
name|Result
argument_list|()
decl_stmt|;
name|result
operator|.
name|setReason
argument_list|(
name|reason
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|void
name|addFailure
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|MetadataResults
name|results
init|=
name|getMetadataResults
argument_list|(
name|metadata
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|results
operator|.
name|addFailure
argument_list|(
name|createResults
argument_list|(
name|reason
argument_list|)
argument_list|)
expr_stmt|;
name|numFailures
operator|++
expr_stmt|;
name|updateTimings
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addWarning
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|MetadataResults
name|results
init|=
name|getMetadataResults
argument_list|(
name|metadata
argument_list|,
name|System
operator|.
name|currentTimeMillis
argument_list|()
argument_list|)
decl_stmt|;
name|results
operator|.
name|addWarning
argument_list|(
name|createResults
argument_list|(
name|reason
argument_list|)
argument_list|)
expr_stmt|;
name|numWarnings
operator|++
expr_stmt|;
name|updateTimings
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|initMetadataMap
parameter_list|()
block|{
name|Map
name|map
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|reporting
operator|.
name|getMetadata
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|MetadataResults
name|result
init|=
operator|(
name|MetadataResults
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|getMetadataKey
argument_list|(
name|result
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|result
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|result
operator|.
name|getVersion
argument_list|()
argument_list|)
decl_stmt|;
name|map
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|result
argument_list|)
expr_stmt|;
name|numFailures
operator|+=
name|result
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|numWarnings
operator|+=
name|result
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
block|}
name|metadataMap
operator|=
name|map
expr_stmt|;
block|}
specifier|private
specifier|static
name|String
name|getMetadataKey
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
block|{
return|return
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
return|;
block|}
specifier|public
name|int
name|getNumFailures
parameter_list|()
block|{
return|return
name|numFailures
return|;
block|}
specifier|public
name|int
name|getNumWarnings
parameter_list|()
block|{
return|return
name|numWarnings
return|;
block|}
specifier|public
name|Reporting
name|getReporting
parameter_list|()
block|{
return|return
name|reporting
return|;
block|}
specifier|public
name|Iterator
name|getArtifactIterator
parameter_list|()
block|{
return|return
name|reporting
operator|.
name|getArtifacts
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|getMetadataIterator
parameter_list|()
block|{
return|return
name|reporting
operator|.
name|getMetadata
argument_list|()
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|isMetadataUpToDate
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|long
name|timestamp
parameter_list|)
block|{
name|String
name|key
init|=
name|getMetadataKey
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|Map
name|map
init|=
name|metadataMap
decl_stmt|;
name|MetadataResults
name|results
init|=
operator|(
name|MetadataResults
operator|)
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
return|return
name|results
operator|!=
literal|null
operator|&&
name|results
operator|.
name|getLastModified
argument_list|()
operator|>=
name|timestamp
return|;
block|}
comment|/**      * Make sure the metadata record exists, but remove any previous reports in preparation for adding new ones.      *      * @param metadata     the metadata      * @param lastModified the modification time of the file being tracked      */
specifier|public
name|void
name|cleanMetadata
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|long
name|lastModified
parameter_list|)
block|{
name|MetadataResults
name|results
init|=
name|getMetadataResults
argument_list|(
name|metadata
argument_list|,
name|lastModified
argument_list|)
decl_stmt|;
name|results
operator|.
name|setLastModified
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
name|numFailures
operator|-=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|numWarnings
operator|-=
name|results
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|results
operator|.
name|getWarnings
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
block|}
specifier|private
name|MetadataResults
name|getMetadataResults
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|long
name|lastModified
parameter_list|)
block|{
name|String
name|key
init|=
name|getMetadataKey
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|Map
name|metadataMap
init|=
name|this
operator|.
name|metadataMap
decl_stmt|;
name|MetadataResults
name|results
init|=
operator|(
name|MetadataResults
operator|)
name|metadataMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|==
literal|null
condition|)
block|{
name|results
operator|=
operator|new
name|MetadataResults
argument_list|()
expr_stmt|;
name|results
operator|.
name|setArtifactId
argument_list|(
name|metadata
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|.
name|setGroupId
argument_list|(
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|.
name|setVersion
argument_list|(
name|metadata
operator|.
name|getBaseVersion
argument_list|()
argument_list|)
expr_stmt|;
name|results
operator|.
name|setLastModified
argument_list|(
name|lastModified
argument_list|)
expr_stmt|;
name|metadataMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|results
argument_list|)
expr_stmt|;
name|reporting
operator|.
name|getMetadata
argument_list|()
operator|.
name|add
argument_list|(
name|results
argument_list|)
expr_stmt|;
block|}
return|return
name|results
return|;
block|}
specifier|private
specifier|static
name|String
name|getMetadataKey
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|)
block|{
return|return
name|getMetadataKey
argument_list|(
name|metadata
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|metadata
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|metadata
operator|.
name|getBaseVersion
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|void
name|removeArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|Map
name|map
init|=
name|artifactMap
decl_stmt|;
name|String
name|key
init|=
name|getArtifactKey
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|,
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
decl_stmt|;
name|ArtifactResults
name|results
init|=
operator|(
name|ArtifactResults
operator|)
name|map
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|results
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|reporting
operator|.
name|getArtifacts
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
if|if
condition|(
name|results
operator|.
name|equals
argument_list|(
name|i
operator|.
name|next
argument_list|()
argument_list|)
condition|)
block|{
name|i
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
name|numFailures
operator|-=
name|results
operator|.
name|getFailures
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|numWarnings
operator|-=
name|results
operator|.
name|getWarnings
argument_list|()
operator|.
name|size
argument_list|()
expr_stmt|;
name|map
operator|.
name|remove
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|ArtifactRepository
name|getRepository
parameter_list|()
block|{
return|return
name|repository
return|;
block|}
specifier|public
name|boolean
name|isInProgress
parameter_list|()
block|{
return|return
name|inProgress
return|;
block|}
specifier|public
name|void
name|setInProgress
parameter_list|(
name|boolean
name|inProgress
parameter_list|)
block|{
name|this
operator|.
name|inProgress
operator|=
name|inProgress
expr_stmt|;
if|if
condition|(
name|inProgress
condition|)
block|{
name|startTime
operator|=
name|System
operator|.
name|currentTimeMillis
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|clear
parameter_list|()
block|{
comment|// clear the values rather than destroy the instance so that the "inProgress" indicator is in tact.
name|numWarnings
operator|=
literal|0
expr_stmt|;
name|numFailures
operator|=
literal|0
expr_stmt|;
name|artifactMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|metadataMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|reporting
operator|.
name|getArtifacts
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|reporting
operator|.
name|getMetadata
argument_list|()
operator|.
name|clear
argument_list|()
expr_stmt|;
name|updateTimings
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|setStartTime
parameter_list|(
name|long
name|startTime
parameter_list|)
block|{
name|this
operator|.
name|startTime
operator|=
name|startTime
expr_stmt|;
block|}
specifier|public
name|long
name|getStartTime
parameter_list|()
block|{
return|return
name|startTime
return|;
block|}
specifier|public
name|void
name|updateTimings
parameter_list|()
block|{
name|long
name|startTime
init|=
name|getStartTime
argument_list|()
decl_stmt|;
name|Date
name|endTime
init|=
operator|new
name|Date
argument_list|()
decl_stmt|;
if|if
condition|(
name|startTime
operator|>
literal|0
condition|)
block|{
name|getReporting
argument_list|()
operator|.
name|setExecutionTime
argument_list|(
name|endTime
operator|.
name|getTime
argument_list|()
operator|-
name|startTime
argument_list|)
expr_stmt|;
block|}
name|getReporting
argument_list|()
operator|.
name|setLastModified
argument_list|(
name|endTime
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

