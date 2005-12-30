begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
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
name|ArtifactRepositoryMetadata
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
name|Metadata
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
name|Snapshot
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
name|io
operator|.
name|xpp3
operator|.
name|MetadataXpp3Reader
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
name|io
operator|.
name|FileReader
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
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryQueryLayer
implements|implements
name|RepositoryQueryLayer
block|{
specifier|protected
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|public
name|boolean
name|containsArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
return|return
name|f
operator|.
name|exists
argument_list|()
return|;
block|}
specifier|public
name|boolean
name|containsArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Snapshot
name|snapshot
parameter_list|)
block|{
name|String
name|artifactPath
init|=
name|getSnapshotArtifactRepositoryPath
argument_list|(
name|artifact
argument_list|,
name|snapshot
argument_list|)
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|artifactPath
argument_list|)
decl_stmt|;
return|return
name|artifactFile
operator|.
name|exists
argument_list|()
return|;
block|}
specifier|public
name|List
name|getVersions
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|RepositoryQueryLayerException
block|{
name|Metadata
name|metadata
init|=
name|getMetadata
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
return|return
name|metadata
operator|.
name|getVersioning
argument_list|()
operator|.
name|getVersions
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getSnapshotArtifactRepositoryPath
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Snapshot
name|snapshot
parameter_list|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|snapshotInfo
init|=
name|artifact
operator|.
name|getVersion
argument_list|()
operator|.
name|replaceFirst
argument_list|(
literal|"SNAPSHOT"
argument_list|,
name|snapshot
operator|.
name|getTimestamp
argument_list|()
operator|+
literal|"-"
operator|+
name|snapshot
operator|.
name|getBuildNumber
argument_list|()
operator|+
literal|".pom"
argument_list|)
decl_stmt|;
name|File
name|snapshotFile
init|=
operator|new
name|File
argument_list|(
name|f
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-"
operator|+
name|snapshotInfo
argument_list|)
decl_stmt|;
return|return
name|snapshotFile
operator|.
name|getAbsolutePath
argument_list|()
return|;
block|}
specifier|protected
name|Metadata
name|getMetadata
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|RepositoryQueryLayerException
block|{
name|Metadata
name|metadata
decl_stmt|;
name|ArtifactRepositoryMetadata
name|repositoryMetadata
init|=
operator|new
name|ArtifactRepositoryMetadata
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|String
name|path
init|=
name|repository
operator|.
name|pathOfRemoteRepositoryMetadata
argument_list|(
name|repositoryMetadata
argument_list|)
decl_stmt|;
name|File
name|metadataFile
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|metadataFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|MetadataXpp3Reader
name|reader
init|=
operator|new
name|MetadataXpp3Reader
argument_list|()
decl_stmt|;
try|try
block|{
name|metadata
operator|=
name|reader
operator|.
name|read
argument_list|(
operator|new
name|FileReader
argument_list|(
name|metadataFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryQueryLayerException
argument_list|(
literal|"Error occurred while attempting to read metadata file"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|RepositoryQueryLayerException
argument_list|(
literal|"Metadata not found: "
operator|+
name|metadataFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
return|return
name|metadata
return|;
block|}
block|}
end_class

end_unit

