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
name|ArrayList
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
comment|/**  * @plexus.component role="org.apache.maven.archiva.reporting.ArtifactReporter"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultArtifactReporter
implements|implements
name|ArtifactReporter
block|{
specifier|private
name|List
name|artifactFailures
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|List
name|artifactSuccesses
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|List
name|artifactWarnings
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|List
name|metadataFailures
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|List
name|metadataSuccesses
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|List
name|metadataWarnings
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
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
name|artifactFailures
operator|.
name|add
argument_list|(
operator|new
name|ArtifactResult
argument_list|(
name|artifact
argument_list|,
name|reason
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addSuccess
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|artifactSuccesses
operator|.
name|add
argument_list|(
operator|new
name|ArtifactResult
argument_list|(
name|artifact
argument_list|)
argument_list|)
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
name|message
parameter_list|)
block|{
name|artifactWarnings
operator|.
name|add
argument_list|(
operator|new
name|ArtifactResult
argument_list|(
name|artifact
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
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
name|metadataFailures
operator|.
name|add
argument_list|(
operator|new
name|RepositoryMetadataResult
argument_list|(
name|metadata
argument_list|,
name|reason
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addSuccess
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|)
block|{
name|metadataSuccesses
operator|.
name|add
argument_list|(
operator|new
name|RepositoryMetadataResult
argument_list|(
name|metadata
argument_list|)
argument_list|)
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
name|message
parameter_list|)
block|{
name|metadataWarnings
operator|.
name|add
argument_list|(
operator|new
name|RepositoryMetadataResult
argument_list|(
name|metadata
argument_list|,
name|message
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Iterator
name|getArtifactFailureIterator
parameter_list|()
block|{
return|return
name|artifactFailures
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|getArtifactSuccessIterator
parameter_list|()
block|{
return|return
name|artifactSuccesses
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|getArtifactWarningIterator
parameter_list|()
block|{
return|return
name|artifactWarnings
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|getRepositoryMetadataFailureIterator
parameter_list|()
block|{
return|return
name|metadataFailures
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|getRepositoryMetadataSuccessIterator
parameter_list|()
block|{
return|return
name|metadataSuccesses
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|Iterator
name|getRepositoryMetadataWarningIterator
parameter_list|()
block|{
return|return
name|metadataWarnings
operator|.
name|iterator
argument_list|()
return|;
block|}
specifier|public
name|int
name|getNumFailures
parameter_list|()
block|{
return|return
name|artifactFailures
operator|.
name|size
argument_list|()
operator|+
name|metadataFailures
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|getNumSuccesses
parameter_list|()
block|{
return|return
name|artifactSuccesses
operator|.
name|size
argument_list|()
operator|+
name|metadataSuccesses
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|int
name|getNumWarnings
parameter_list|()
block|{
return|return
name|artifactWarnings
operator|.
name|size
argument_list|()
operator|+
name|metadataWarnings
operator|.
name|size
argument_list|()
return|;
block|}
block|}
end_class

end_unit

