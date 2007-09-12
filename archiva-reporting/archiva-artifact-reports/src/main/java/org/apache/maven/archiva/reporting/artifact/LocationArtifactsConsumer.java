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
operator|.
name|artifact
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
name|ArchivaArtifactConsumer
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
name|ArchivaRepository
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
name|repository
operator|.
name|ArchivaConfigurationAdaptor
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
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|SelectorUtils
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
name|IOException
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
name|Enumeration
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
name|List
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarFile
import|;
end_import

begin_comment
comment|/**  * Validate the location of the artifact based on the values indicated  * in its pom (both the pom packaged with the artifact& the pom in the  * file system).  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.consumers.ArchivaArtifactConsumer"  * role-hint="validate-artifacts-location"  */
end_comment

begin_class
specifier|public
class|class
name|LocationArtifactsConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|ArchivaArtifactConsumer
implements|,
name|RegistryListener
implements|,
name|Initializable
block|{
comment|/**      * @plexus.configuration default-value="duplicate-artifacts"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Check for Duplicate Artifacts via SHA1 Checksums"      */
specifier|private
name|String
name|description
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
comment|/**      * @plexus.requirement role-hint="jdo"      */
specifier|private
name|ArchivaDAO
name|dao
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|BidirectionalRepositoryLayoutFactory
name|layoutFactory
decl_stmt|;
specifier|private
name|Map
name|repositoryMap
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|List
name|includes
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
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
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|List
name|getIncludedTypes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
comment|/**      * Check whether the artifact is in its proper location. The location of the artifact      * is validated first against the groupId, artifactId and versionId in the specified model      * object (pom in the file system). Then unpack the artifact (jar file) and get the model (pom)      * included in the package. If a model exists inside the package, then check if the artifact's      * location is valid based on the location specified in the pom. Check if the both the location      * specified in the file system pom and in the pom included in the package is the same.      */
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
name|ArchivaRepository
name|repository
init|=
name|findRepository
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getUrl
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|toPath
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
name|ArchivaProjectModel
name|fsModel
init|=
name|readFilesystemModel
argument_list|(
name|artifactFile
argument_list|)
decl_stmt|;
name|ArchivaProjectModel
name|embeddedModel
init|=
name|readEmbeddedModel
argument_list|(
name|artifact
argument_list|,
name|artifactFile
argument_list|)
decl_stmt|;
name|validateAppropriateModel
argument_list|(
literal|"Filesystem"
argument_list|,
name|artifact
argument_list|,
name|fsModel
argument_list|)
expr_stmt|;
name|validateAppropriateModel
argument_list|(
literal|"Embedded"
argument_list|,
name|artifact
argument_list|,
name|embeddedModel
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|validateAppropriateModel
parameter_list|(
name|String
name|location
parameter_list|,
name|ArchivaArtifact
name|artifact
parameter_list|,
name|ArchivaProjectModel
name|model
parameter_list|)
throws|throws
name|ConsumerException
block|{
if|if
condition|(
name|model
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|model
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
condition|)
block|{
name|addProblem
argument_list|(
name|artifact
argument_list|,
literal|"The groupId of the "
operator|+
name|location
operator|+
literal|" project model doesn't match with the artifact, expected<"
operator|+
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|+
literal|">, but was actually<"
operator|+
name|model
operator|.
name|getGroupId
argument_list|()
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
condition|)
block|{
name|addProblem
argument_list|(
name|artifact
argument_list|,
literal|"The artifactId of the "
operator|+
name|location
operator|+
literal|" project model doesn't match with the artifact, expected<"
operator|+
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|">, but was actually<"
operator|+
name|model
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|StringUtils
operator|.
name|equals
argument_list|(
name|model
operator|.
name|getVersion
argument_list|()
argument_list|,
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|addProblem
argument_list|(
name|artifact
argument_list|,
literal|"The version of the "
operator|+
name|location
operator|+
literal|" project model doesn't match with the artifact, expected<"
operator|+
name|artifact
operator|.
name|getVersion
argument_list|()
operator|+
literal|">, but was actually<"
operator|+
name|model
operator|.
name|getVersion
argument_list|()
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|ArchivaProjectModel
name|readEmbeddedModel
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|,
name|File
name|artifactFile
parameter_list|)
throws|throws
name|ConsumerException
block|{
try|try
block|{
name|JarFile
name|jar
init|=
operator|new
name|JarFile
argument_list|(
name|artifactFile
argument_list|)
decl_stmt|;
comment|// Get the entry and its input stream.
name|JarEntry
name|expectedEntry
init|=
name|jar
operator|.
name|getJarEntry
argument_list|(
literal|"META-INF/maven/"
operator|+
name|artifact
operator|.
name|getGroupId
argument_list|()
operator|+
literal|"/"
operator|+
name|artifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"/pom.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|expectedEntry
operator|!=
literal|null
condition|)
block|{
comment|// TODO: read and resolve model here.
return|return
literal|null
return|;
block|}
comment|/* Expected Entry not found, look for alternate that might             * indicate that the artifact is, indeed located in the wrong place.             */
name|List
name|actualPomXmls
init|=
name|findJarEntryPattern
argument_list|(
name|jar
argument_list|,
literal|"META-INF/maven/**/pom.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|actualPomXmls
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// No check needed.
block|}
comment|// TODO: test for invalid actual pom.xml
comment|// TODO: test
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// Not able to read from the file.
name|String
name|emsg
init|=
literal|"Unable to read file contents: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
decl_stmt|;
name|addProblem
argument_list|(
name|artifact
argument_list|,
name|emsg
argument_list|)
expr_stmt|;
block|}
return|return
literal|null
return|;
block|}
specifier|private
name|List
name|findJarEntryPattern
parameter_list|(
name|JarFile
name|jar
parameter_list|,
name|String
name|pattern
parameter_list|)
block|{
name|List
name|hits
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|Enumeration
name|entries
init|=
name|jar
operator|.
name|entries
argument_list|()
decl_stmt|;
while|while
condition|(
name|entries
operator|.
name|hasMoreElements
argument_list|()
condition|)
block|{
name|JarEntry
name|entry
init|=
operator|(
name|JarEntry
operator|)
name|entries
operator|.
name|nextElement
argument_list|()
decl_stmt|;
if|if
condition|(
name|SelectorUtils
operator|.
name|match
argument_list|(
name|pattern
argument_list|,
name|entry
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
name|hits
operator|.
name|add
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|hits
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
name|LocationArtifactsReport
operator|.
name|PROBLEM_TYPE_BAD_ARTIFACT_LOCATION
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
specifier|private
name|ArchivaProjectModel
name|readFilesystemModel
parameter_list|(
name|File
name|artifactFile
parameter_list|)
block|{
name|File
name|pomFile
init|=
name|createPomFileReference
argument_list|(
name|artifactFile
argument_list|)
decl_stmt|;
comment|// TODO: read and resolve model here.
return|return
literal|null
return|;
block|}
specifier|private
name|File
name|createPomFileReference
parameter_list|(
name|File
name|artifactFile
parameter_list|)
block|{
name|String
name|pomFilename
init|=
name|artifactFile
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|int
name|pos
init|=
name|pomFilename
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
decl_stmt|;
if|if
condition|(
name|pos
operator|<=
literal|0
condition|)
block|{
comment|// Invalid filename.
return|return
literal|null
return|;
block|}
name|pomFilename
operator|=
name|pomFilename
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|pos
argument_list|)
operator|+
literal|".pom"
expr_stmt|;
return|return
operator|new
name|File
argument_list|(
name|pomFilename
argument_list|)
return|;
block|}
specifier|private
name|ArchivaRepository
name|findRepository
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
block|{
return|return
operator|(
name|ArchivaRepository
operator|)
name|this
operator|.
name|repositoryMap
operator|.
name|get
argument_list|(
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
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
name|isManagedRepositories
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initRepositoryMap
argument_list|()
expr_stmt|;
block|}
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
specifier|private
name|void
name|initRepositoryMap
parameter_list|()
block|{
synchronized|synchronized
init|(
name|this
operator|.
name|repositoryMap
init|)
block|{
name|this
operator|.
name|repositoryMap
operator|.
name|clear
argument_list|()
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepositoryConfiguration
argument_list|>
name|map
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|ManagedRepositoryConfiguration
argument_list|>
name|entry
range|:
name|map
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|ArchivaRepository
name|repository
init|=
name|ArchivaConfigurationAdaptor
operator|.
name|toArchivaRepository
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
decl_stmt|;
name|this
operator|.
name|repositoryMap
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|repository
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|initRepositoryMap
argument_list|()
expr_stmt|;
name|initIncludes
argument_list|()
expr_stmt|;
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

