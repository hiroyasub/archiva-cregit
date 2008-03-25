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
name|ConfigurationNames
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
name|FileTypes
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
name|ManagedRepositoryConfiguration
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
name|KnownRepositoryContentConsumer
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
name|layout
operator|.
name|LayoutException
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
name|digest
operator|.
name|Digester
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
name|digest
operator|.
name|DigesterException
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|Initializable
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
name|personality
operator|.
name|plexus
operator|.
name|lifecycle
operator|.
name|phase
operator|.
name|InitializationException
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
name|registry
operator|.
name|Registry
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
name|registry
operator|.
name|RegistryListener
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
name|Date
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
comment|/**  * ArtifactUpdateDatabaseConsumer - Take an artifact off of disk and put it into the repository.  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"  *                   role-hint="update-db-artifact"  *                   instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactUpdateDatabaseConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
implements|,
name|Initializable
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_NOT_ARTIFACT
init|=
literal|"file-not-artifact"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|DB_ERROR
init|=
literal|"db-error"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CHECKSUM_CALCULATION
init|=
literal|"checksum-calc"
decl_stmt|;
comment|/**      * @plexus.configuration default-value="update-db-artifact"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Update the Artifact in the Database"      */
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
name|configuration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentFactory
name|repositoryFactory
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="sha1"      */
specifier|private
name|Digester
name|digestSha1
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="md5";      */
specifier|private
name|Digester
name|digestMd5
decl_stmt|;
specifier|private
name|ManagedRepositoryContent
name|repository
decl_stmt|;
specifier|private
name|File
name|repositoryDir
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
name|getDefaultArtifactExclusions
argument_list|()
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|this
operator|.
name|includes
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepositoryConfiguration
name|repo
parameter_list|)
throws|throws
name|ConsumerException
block|{
try|try
block|{
name|this
operator|.
name|repository
operator|=
name|repositoryFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|repositoryDir
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getRepoRoot
argument_list|()
argument_list|)
expr_stmt|;
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
literal|"Unable to start ArtifactUpdateDatabaseConsumer: "
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
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|ArchivaArtifact
name|artifact
init|=
name|getLiveArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|artifact
operator|==
literal|null
condition|)
block|{
return|return;
block|}
try|try
block|{
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setRepositoryId
argument_list|(
name|this
operator|.
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
comment|// Calculate the hashcodes.
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|repositoryDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
try|try
block|{
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumMD5
argument_list|(
name|digestMd5
operator|.
name|calc
argument_list|(
name|artifactFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|triggerConsumerWarning
argument_list|(
name|CHECKSUM_CALCULATION
argument_list|,
literal|"Unable to calculate the MD5 checksum: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumSHA1
argument_list|(
name|digestSha1
operator|.
name|calc
argument_list|(
name|artifactFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|triggerConsumerWarning
argument_list|(
name|CHECKSUM_CALCULATION
argument_list|,
literal|"Unable to calculate the SHA1 checksum: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setLastModified
argument_list|(
operator|new
name|Date
argument_list|(
name|artifactFile
operator|.
name|lastModified
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setSize
argument_list|(
name|artifactFile
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setOrigin
argument_list|(
literal|"FileSystem"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setWhenProcessed
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ArchivaDatabaseException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|DB_ERROR
argument_list|,
literal|"Unable to save artifact to database: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get a Live Artifact from a Path.      *<p/>      * Will resolve the artifact details from the path, and then return a database live version      * of that artifact.  Suitable for modification and saving (without the need to check for      * existance in database prior to save.)      *      * @param path the path to work from.      * @return the artifact that is suitable for database saving.      */
specifier|public
name|ArchivaArtifact
name|getLiveArtifact
parameter_list|(
name|String
name|path
parameter_list|)
block|{
try|try
block|{
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
name|ArchivaArtifact
name|liveArtifact
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|createArtifact
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
name|getClassifier
argument_list|()
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|liveArtifact
return|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|TYPE_NOT_ARTIFACT
argument_list|,
literal|"Path "
operator|+
name|path
operator|+
literal|" cannot be converted to artifact: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|null
return|;
block|}
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|ConfigurationNames
operator|.
name|isRepositoryScanning
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|private
name|void
name|initIncludes
parameter_list|()
block|{
name|includes
operator|.
name|clear
argument_list|()
expr_stmt|;
name|includes
operator|.
name|addAll
argument_list|(
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

