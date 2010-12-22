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
name|audit
operator|.
name|AuditEvent
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
name|repository
operator|.
name|ManagedRepositoryContent
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
name|FilenameFilter
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
comment|/**  * Base class for all repository purge tasks.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryPurge
implements|implements
name|RepositoryPurge
block|{
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractRepositoryPurge
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|final
name|ManagedRepositoryContent
name|repository
decl_stmt|;
specifier|protected
specifier|final
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
decl_stmt|;
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"org.apache.archiva.AuditLog"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|DELIM
init|=
literal|' '
decl_stmt|;
specifier|public
name|AbstractRepositoryPurge
parameter_list|(
name|ManagedRepositoryContent
name|repository
parameter_list|,
name|List
argument_list|<
name|RepositoryListener
argument_list|>
name|listeners
parameter_list|)
block|{
name|this
operator|.
name|repository
operator|=
name|repository
expr_stmt|;
name|this
operator|.
name|listeners
operator|=
name|listeners
expr_stmt|;
block|}
comment|/**      * Purge the repo. Update db and index of removed artifacts.      *      * @param references      */
specifier|protected
name|void
name|purge
parameter_list|(
name|Set
argument_list|<
name|ArtifactReference
argument_list|>
name|references
parameter_list|)
block|{
if|if
condition|(
name|references
operator|!=
literal|null
operator|&&
operator|!
name|references
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|ArtifactReference
name|reference
range|:
name|references
control|)
block|{
name|File
name|artifactFile
init|=
name|repository
operator|.
name|toFile
argument_list|(
name|reference
argument_list|)
decl_stmt|;
comment|// FIXME: looks incomplete, might not delete related metadata?
for|for
control|(
name|RepositoryListener
name|listener
range|:
name|listeners
control|)
block|{
name|listener
operator|.
name|deleteArtifact
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|reference
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|reference
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|reference
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifactFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// TODO: this needs to be logged
name|artifactFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|triggerAuditEvent
argument_list|(
name|repository
operator|.
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|ArtifactReference
operator|.
name|toKey
argument_list|(
name|reference
argument_list|)
argument_list|,
name|AuditEvent
operator|.
name|PURGE_ARTIFACT
argument_list|)
expr_stmt|;
name|purgeSupportFiles
argument_list|(
name|artifactFile
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      *<p>      * This find support files for the artifactFile and deletes them.      *</p>      *<p>      * Support Files are things like ".sha1", ".md5", ".asc", etc.      *</p>      *      * @param artifactFile the file to base off of.      */
specifier|private
name|void
name|purgeSupportFiles
parameter_list|(
name|File
name|artifactFile
parameter_list|)
block|{
name|File
name|parentDir
init|=
name|artifactFile
operator|.
name|getParentFile
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|parentDir
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return;
block|}
name|FilenameFilter
name|filter
init|=
operator|new
name|ArtifactFilenameFilter
argument_list|(
name|artifactFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|File
index|[]
name|files
init|=
name|parentDir
operator|.
name|listFiles
argument_list|(
name|filter
argument_list|)
decl_stmt|;
for|for
control|(
name|File
name|file
range|:
name|files
control|)
block|{
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|String
name|fileName
init|=
name|file
operator|.
name|getName
argument_list|()
decl_stmt|;
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
comment|// TODO: log that it was deleted
name|triggerAuditEvent
argument_list|(
name|repository
operator|.
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
argument_list|,
name|fileName
argument_list|,
name|AuditEvent
operator|.
name|PURGE_FILE
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|triggerAuditEvent
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|resource
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|String
name|msg
init|=
name|repoId
operator|+
name|DELIM
operator|+
literal|"<system-purge>"
operator|+
name|DELIM
operator|+
literal|"<system>"
operator|+
name|DELIM
operator|+
literal|'\"'
operator|+
name|resource
operator|+
literal|'\"'
operator|+
name|DELIM
operator|+
literal|'\"'
operator|+
name|action
operator|+
literal|'\"'
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|msg
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

