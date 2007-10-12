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
name|consumers
operator|.
name|core
operator|.
name|repository
package|;
end_package

begin_comment
comment|/* * Licensed to the Apache Software Foundation (ASF) under one * or more contributor license agreements.  See the NOTICE file * distributed with this work for additional information * regarding copyright ownership.  The ASF licenses this file * to you under the Apache License, Version 2.0 (the * "License"); you may not use this file except in compliance * with the License.  You may obtain a copy of the License at * *  http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, * software distributed under the License is distributed on an * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY * KIND, either express or implied.  See the License for the * specific language governing permissions and limitations * under the License. */
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
name|maven
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
name|maven
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
name|maven
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
name|maven
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
name|List
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * Purge the repository by retention count. Retain only the specified number of snapshots.  *  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  */
end_comment

begin_class
specifier|public
class|class
name|RetentionCountRepositoryPurge
extends|extends
name|AbstractRepositoryPurge
block|{
specifier|private
name|int
name|retentionCount
decl_stmt|;
specifier|public
name|RetentionCountRepositoryPurge
parameter_list|(
name|ManagedRepositoryContent
name|repository
parameter_list|,
name|ArtifactDAO
name|artifactDao
parameter_list|,
name|int
name|retentionCount
parameter_list|)
block|{
name|super
argument_list|(
name|repository
argument_list|,
name|artifactDao
argument_list|)
expr_stmt|;
name|this
operator|.
name|retentionCount
operator|=
name|retentionCount
expr_stmt|;
block|}
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
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
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
argument_list|<
name|String
argument_list|>
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
name|doPurgeAllRelated
argument_list|(
name|artifact
argument_list|,
name|version
argument_list|)
expr_stmt|;
block|}
block|}
block|}
catch|catch
parameter_list|(
name|LayoutException
name|le
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryPurgeException
argument_list|(
name|le
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|ContentNotFoundException
name|e
parameter_list|)
block|{
comment|// Nothing to do here.
comment|// TODO: Log this condition?
block|}
block|}
specifier|private
name|void
name|doPurgeAllRelated
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|,
name|String
name|version
parameter_list|)
throws|throws
name|LayoutException
block|{
name|ArtifactReference
name|artifact
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setClassifier
argument_list|(
name|reference
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setType
argument_list|(
name|reference
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|Set
argument_list|<
name|ArtifactReference
argument_list|>
name|related
init|=
name|repository
operator|.
name|getRelatedArtifacts
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|purge
argument_list|(
name|related
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ContentNotFoundException
name|e
parameter_list|)
block|{
comment|// Nothing to do here.
comment|// TODO: Log this?
block|}
block|}
block|}
end_class

end_unit

