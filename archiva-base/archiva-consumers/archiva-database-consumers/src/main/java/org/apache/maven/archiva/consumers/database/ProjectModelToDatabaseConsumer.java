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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|RepositoryConfiguration
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
name|consumers
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
name|model
operator|.
name|RepositoryURL
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
name|layout
operator|.
name|BidirectionalRepositoryLayout
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
name|BidirectionalRepositoryLayoutFactory
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
name|FilenameParts
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
name|RepositoryLayoutUtils
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
name|ProjectModelException
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
name|ProjectModelFilter
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

begin_comment
comment|/**  * ProjectModelToDatabaseConsumer   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.consumers.DatabaseUnprocessedArtifactConsumer"  *                   role-hint="update-db-project"  *                   instantiation-strategy="per-lookup"  */
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
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|BidirectionalRepositoryLayoutFactory
name|layoutFactory
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="model400"      */
specifier|private
name|ProjectModelReader
name|project400Reader
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="model300"      */
specifier|private
name|ProjectModelReader
name|project300Reader
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="expression"      */
specifier|private
name|ProjectModelFilter
name|expressionModelFilter
decl_stmt|;
comment|/**      * @plexus.requirement       *          role="org.apache.maven.archiva.repository.project.ProjectModelFilter"      *          role-hint="effective"      */
specifier|private
name|EffectiveProjectModelFilter
name|effectiveModelFilter
decl_stmt|;
specifier|private
name|List
name|includes
decl_stmt|;
specifier|public
name|ProjectModelToDatabaseConsumer
parameter_list|()
block|{
name|includes
operator|=
operator|new
name|ArrayList
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
if|if
condition|(
name|hasProjectModelInDatabase
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
condition|)
block|{
comment|// Already in the database.  Skip it.
return|return;
block|}
name|File
name|artifactFile
init|=
name|toFile
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|RepositoryConfiguration
name|repo
init|=
name|getRepository
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|ProjectModelReader
name|reader
init|=
name|project400Reader
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
literal|"legacy"
argument_list|,
name|repo
operator|.
name|getLayout
argument_list|()
argument_list|)
condition|)
block|{
name|reader
operator|=
name|project300Reader
expr_stmt|;
block|}
try|try
block|{
name|ArchivaProjectModel
name|model
init|=
name|reader
operator|.
name|read
argument_list|(
name|artifactFile
argument_list|)
decl_stmt|;
name|model
operator|.
name|setOrigin
argument_list|(
literal|"filesystem"
argument_list|)
expr_stmt|;
comment|// Filter the model
name|model
operator|=
name|expressionModelFilter
operator|.
name|filter
argument_list|(
name|model
argument_list|)
expr_stmt|;
comment|// Resolve the project model
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
name|artifact
argument_list|)
condition|)
block|{
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
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Invalid or corrupt pom. Project model "
operator|+
name|model
operator|+
literal|" was not added in the database."
argument_list|)
expr_stmt|;
block|}
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
catch|catch
parameter_list|(
name|ProjectModelException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
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
argument_list|,
name|e
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
name|getLogger
argument_list|()
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
name|getLogger
argument_list|()
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
name|boolean
name|hasProjectModelInDatabase
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
operator|(
name|model
operator|!=
literal|null
operator|)
return|;
block|}
catch|catch
parameter_list|(
name|ObjectNotFoundException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
specifier|private
name|RepositoryConfiguration
name|getRepository
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
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
return|return
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findRepositoryById
argument_list|(
name|repoId
argument_list|)
return|;
block|}
specifier|private
name|File
name|toFile
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
block|{
name|RepositoryConfiguration
name|repoConfig
init|=
name|getRepository
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|BidirectionalRepositoryLayout
name|layout
init|=
literal|null
decl_stmt|;
try|try
block|{
name|layout
operator|=
name|layoutFactory
operator|.
name|getLayout
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to determine layout of "
operator|+
name|artifact
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
name|String
name|path
init|=
name|layout
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|RepositoryURL
name|url
init|=
operator|new
name|RepositoryURL
argument_list|(
name|repoConfig
operator|.
name|getUrl
argument_list|()
argument_list|)
decl_stmt|;
return|return
operator|new
name|File
argument_list|(
name|url
operator|.
name|getPath
argument_list|()
argument_list|,
name|path
argument_list|)
return|;
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
name|String
name|toPath
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
block|{
try|try
block|{
name|BidirectionalRepositoryLayout
name|layout
init|=
name|layoutFactory
operator|.
name|getLayout
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
return|return
name|layout
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to calculate path for artifact: "
operator|+
name|artifact
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|private
name|boolean
name|isValidModel
parameter_list|(
name|ArchivaProjectModel
name|model
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
name|toFile
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
try|try
block|{
name|FilenameParts
name|parts
init|=
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
name|artifactFile
operator|.
name|getName
argument_list|()
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|parts
operator|.
name|artifactId
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
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Project Model "
operator|+
name|model
operator|+
literal|" artifactId: "
operator|+
name|model
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|" does not match the pom file's artifactId: "
operator|+
name|parts
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|addProblem
argument_list|(
name|artifact
argument_list|,
literal|"Project Model "
operator|+
name|model
operator|+
literal|" artifactId: "
operator|+
name|model
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|" does not match the pom file's artifactId: "
operator|+
name|parts
operator|.
name|artifactId
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
operator|!
name|parts
operator|.
name|version
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
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Project Model "
operator|+
name|model
operator|+
literal|" artifactId: "
operator|+
name|model
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|" does not match the pom file's artifactId: "
operator|+
name|parts
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|addProblem
argument_list|(
name|artifact
argument_list|,
literal|"Project Model "
operator|+
name|model
operator|+
literal|" version: "
operator|+
name|model
operator|.
name|getVersion
argument_list|()
operator|+
literal|" does not match the pom file's version: "
operator|+
name|parts
operator|.
name|version
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|//check if the file name matches the values indicated in the pom
if|if
condition|(
operator|!
name|artifactFile
operator|.
name|getName
argument_list|()
operator|.
name|equalsIgnoreCase
argument_list|(
name|model
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-"
operator|+
name|model
operator|.
name|getVersion
argument_list|()
operator|+
literal|"-"
operator|+
name|parts
operator|.
name|classifier
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Artifact "
operator|+
name|artifact
operator|+
literal|" does not match the artifactId and/or version "
operator|+
literal|"specified in the project model "
operator|+
name|model
argument_list|)
expr_stmt|;
name|addProblem
argument_list|(
name|artifact
argument_list|,
literal|"Artifact "
operator|+
name|artifact
operator|+
literal|" does not match the artifactId and/or version "
operator|+
literal|"specified in the project model "
operator|+
name|model
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
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
name|ConsumerException
argument_list|(
name|le
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
return|return
literal|true
return|;
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
name|getLogger
argument_list|()
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
block|}
end_class

end_unit

