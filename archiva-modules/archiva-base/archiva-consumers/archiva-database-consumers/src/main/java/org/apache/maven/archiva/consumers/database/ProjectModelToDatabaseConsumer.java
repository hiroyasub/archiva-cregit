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
name|database
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|consumers
operator|.
name|AbstractMonitoredConsumer
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
name|consumers
operator|.
name|ConsumerException
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
name|updater
operator|.
name|DatabaseUnprocessedArtifactConsumer
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|RepositoryProblem
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
name|artifact
operator|.
name|CorruptArtifactReport
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
name|RepositoryContentFactory
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
name|RepositoryException
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
name|content
operator|.
name|ManagedLegacyRepositoryContent
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
name|project
operator|.
name|ProjectModelReader
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
name|project
operator|.
name|filters
operator|.
name|EffectiveProjectModelFilter
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
name|project
operator|.
name|readers
operator|.
name|ProjectModel300Reader
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
name|project
operator|.
name|readers
operator|.
name|ProjectModel400Reader
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
name|xml
operator|.
name|XMLException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|cache
operator|.
name|Cache
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
comment|/**  * ProjectModelToDatabaseConsumer  *  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.database.updater.DatabaseUnprocessedArtifactConsumer"  * role-hint="update-db-project"  * instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ProjectModelToDatabaseConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|DatabaseUnprocessedArtifactConsumer
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ProjectModelToDatabaseConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * @plexus.configuration default-value="update-db-project"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Update database with project model information."      */
specifier|private
name|String
name|description
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement role="org.apache.maven.archiva.repository.project.ProjectModelFilter"      * role-hint="effective"      */
specifier|private
name|EffectiveProjectModelFilter
name|effectiveModelFilter
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="effective-project-cache"      */
specifier|private
name|Cache
name|effectiveProjectCache
decl_stmt|;
specifier|public
name|ProjectModelToDatabaseConsumer
parameter_list|()
block|{
name|includes
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|()
block|{
comment|/* nothing to do here */
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* nothing to do here */
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludedTypes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
specifier|public
name|void
name|processArchivaArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ConsumerException
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
literal|"pom"
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
comment|// Not a pom.  Skip it.
return|return;
block|}
name|ArchivaProjectModel
name|model
init|=
literal|null
decl_stmt|;
comment|// remove old project model if it already exists in the database
if|if
condition|(
operator|(
name|model
operator|=
name|getProjectModelFromDatabase
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
argument_list|)
operator|)
operator|!=
literal|null
condition|)
block|{
name|removeOldProjectModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|model
operator|=
literal|null
expr_stmt|;
block|}
name|ManagedRepositoryContent
name|repo
init|=
name|getRepository
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|File
name|artifactFile
init|=
name|repo
operator|.
name|toFile
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|ProjectModelReader
name|reader
decl_stmt|;
if|if
condition|(
name|repo
operator|instanceof
name|ManagedLegacyRepositoryContent
condition|)
block|{
name|reader
operator|=
operator|new
name|ProjectModel300Reader
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|reader
operator|=
operator|new
name|ProjectModel400Reader
argument_list|()
expr_stmt|;
block|}
try|try
block|{
name|model
operator|=
name|reader
operator|.
name|read
argument_list|(
name|artifactFile
argument_list|)
expr_stmt|;
comment|// The version should be updated to the artifact/filename version if it is a unique snapshot
if|if
condition|(
name|VersionUtil
operator|.
name|isUniqueSnapshot
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|model
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// Resolve the project model (build effective model, resolve expressions)
name|model
operator|=
name|effectiveModelFilter
operator|.
name|filter
argument_list|(
name|model
argument_list|)
expr_stmt|;
if|if
condition|(
name|isValidModel
argument_list|(
name|model
argument_list|,
name|repo
argument_list|,
name|artifact
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Adding project model to database - "
operator|+
name|Keys
operator|.
name|toKey
argument_list|(
name|model
argument_list|)
argument_list|)
expr_stmt|;
comment|// Clone model, since DAO while detachingCopy resets its contents
comment|// This changes contents of the cache in EffectiveProjectModelFilter
name|model
operator|=
name|ArchivaModelCloner
operator|.
name|clone
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|saveProjectModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Invalid or corrupt pom. Project model not added to database - "
operator|+
name|Keys
operator|.
name|toKey
argument_list|(
name|model
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to read project model "
operator|+
name|artifactFile
operator|+
literal|" : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
name|addProblem
argument_list|(
name|artifact
argument_list|,
literal|"Unable to read project model "
operator|+
name|artifactFile
operator|+
literal|" : "
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
name|warn
argument_list|(
literal|"Unable to save project model "
operator|+
name|artifactFile
operator|+
literal|" to the database : "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|t
parameter_list|)
block|{
comment|// Catch the other errors in the process to allow the rest of the process to complete.
name|log
operator|.
name|error
argument_list|(
literal|"Unable to process model "
operator|+
name|artifactFile
operator|+
literal|" due to : "
operator|+
name|t
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" : "
operator|+
name|t
operator|.
name|getMessage
argument_list|()
argument_list|,
name|t
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|ArchivaProjectModel
name|getProjectModelFromDatabase
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
try|try
block|{
name|ArchivaProjectModel
name|model
init|=
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
decl_stmt|;
return|return
name|model
return|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|ManagedRepositoryContent
name|getRepository
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|String
name|repoId
init|=
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
decl_stmt|;
try|try
block|{
return|return
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repoId
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
literal|"Unable to process project model: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
comment|// Tells the configuration that this consumer cannot be disabled.
return|return
literal|true
return|;
block|}
specifier|private
name|boolean
name|isValidModel
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|,
name|ManagedRepositoryContent
name|repo
parameter_list|,
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|File
name|artifactFile
init|=
name|repo
operator|.
name|toFile
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
name|StringBuffer
name|emsg
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|emsg
operator|.
name|append
argument_list|(
literal|"File "
argument_list|)
operator|.
name|append
argument_list|(
name|artifactFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|emsg
operator|.
name|append
argument_list|(
literal|" has an invalid project model ["
argument_list|)
expr_stmt|;
name|appendModel
argument_list|(
name|emsg
argument_list|,
name|model
argument_list|)
expr_stmt|;
name|emsg
operator|.
name|append
argument_list|(
literal|"]: The model artifactId ["
argument_list|)
operator|.
name|append
argument_list|(
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|emsg
operator|.
name|append
argument_list|(
literal|"] does not match the artifactId portion of the filename: "
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|emsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|addProblem
argument_list|(
name|artifact
argument_list|,
name|emsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|artifact
operator|.
name|getVersion
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|model
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|&&
operator|!
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
operator|.
name|equalsIgnoreCase
argument_list|(
name|model
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|StringBuffer
name|emsg
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|emsg
operator|.
name|append
argument_list|(
literal|"File "
argument_list|)
operator|.
name|append
argument_list|(
name|artifactFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|emsg
operator|.
name|append
argument_list|(
literal|" has an invalid project model ["
argument_list|)
expr_stmt|;
name|appendModel
argument_list|(
name|emsg
argument_list|,
name|model
argument_list|)
expr_stmt|;
name|emsg
operator|.
name|append
argument_list|(
literal|"]; The model version ["
argument_list|)
operator|.
name|append
argument_list|(
name|model
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|emsg
operator|.
name|append
argument_list|(
literal|"] does not match the version portion of the filename: "
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|emsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|addProblem
argument_list|(
name|artifact
argument_list|,
name|emsg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
specifier|private
name|void
name|appendModel
parameter_list|(
name|StringBuffer
name|buf
parameter_list|,
name|ArchivaProjectModel
name|model
parameter_list|)
block|{
name|buf
operator|.
name|append
argument_list|(
literal|"groupId:"
argument_list|)
operator|.
name|append
argument_list|(
name|model
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"|artifactId:"
argument_list|)
operator|.
name|append
argument_list|(
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"|version:"
argument_list|)
operator|.
name|append
argument_list|(
name|model
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|buf
operator|.
name|append
argument_list|(
literal|"|packaging:"
argument_list|)
operator|.
name|append
argument_list|(
name|model
operator|.
name|getPackaging
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|addProblem
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|,
name|String
name|msg
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|ManagedRepositoryContent
name|repo
init|=
name|getRepository
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|RepositoryProblem
name|problem
init|=
operator|new
name|RepositoryProblem
argument_list|()
decl_stmt|;
name|problem
operator|.
name|setRepositoryId
argument_list|(
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setPath
argument_list|(
name|repo
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setGroupId
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setArtifactId
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setType
argument_list|(
name|CorruptArtifactReport
operator|.
name|PROBLEM_TYPE_CORRUPT_ARTIFACT
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setOrigin
argument_list|(
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|problem
operator|.
name|setMessage
argument_list|(
name|msg
argument_list|)
expr_stmt|;
try|try
block|{
name|dao
operator|.
name|getRepositoryProblemDAO
argument_list|()
operator|.
name|saveRepositoryProblem
argument_list|(
name|problem
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
name|String
name|emsg
init|=
literal|"Unable to save problem with artifact location to DB: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|log
operator|.
name|warn
argument_list|(
name|emsg
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConsumerException
argument_list|(
name|emsg
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|private
name|String
name|toProjectKey
parameter_list|(
name|ArchivaProjectModel
name|project
parameter_list|)
block|{
name|StringBuilder
name|key
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|key
operator|.
name|append
argument_list|(
name|project
operator|.
name|getGroupId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|project
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|project
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|key
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|removeOldProjectModel
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|)
block|{
try|try
block|{
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
operator|.
name|deleteProjectModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|ae
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Unable to delete existing project model."
argument_list|)
expr_stmt|;
block|}
comment|// Force removal of project model from effective cache
name|String
name|projectKey
init|=
name|toProjectKey
argument_list|(
name|model
argument_list|)
decl_stmt|;
synchronized|synchronized
init|(
name|effectiveProjectCache
init|)
block|{
if|if
condition|(
name|effectiveProjectCache
operator|.
name|hasKey
argument_list|(
name|projectKey
argument_list|)
condition|)
block|{
name|effectiveProjectCache
operator|.
name|remove
argument_list|(
name|projectKey
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

