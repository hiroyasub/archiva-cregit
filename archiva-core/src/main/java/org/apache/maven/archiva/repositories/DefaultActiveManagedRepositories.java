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
name|repositories
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
name|common
operator|.
name|artifact
operator|.
name|managed
operator|.
name|ManagedArtifact
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
name|artifact
operator|.
name|managed
operator|.
name|ManagedArtifactTypes
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
name|artifact
operator|.
name|managed
operator|.
name|ManagedEjbArtifact
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
name|artifact
operator|.
name|managed
operator|.
name|ManagedJavaArtifact
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
name|Configuration
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
name|ConfiguredRepositoryFactory
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
name|discoverer
operator|.
name|DiscovererStatistics
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
name|factory
operator|.
name|ArtifactFactory
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
name|model
operator|.
name|Model
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
name|project
operator|.
name|MavenProject
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
name|project
operator|.
name|MavenProjectBuilder
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
name|project
operator|.
name|ProjectBuildingException
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
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
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
comment|/**  * DefaultActiveManagedRepositories  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.repositories.ActiveManagedRepositories"  */
end_comment

begin_class
specifier|public
class|class
name|DefaultActiveManagedRepositories
extends|extends
name|AbstractLogEnabled
implements|implements
name|ActiveManagedRepositories
implements|,
name|Initializable
implements|,
name|RegistryListener
block|{
comment|/**      * @plexus.requirement role-hint="artifactCache"      */
specifier|private
name|Cache
name|artifactCache
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|MavenProjectBuilder
name|projectBuilder
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="projectCache"      */
specifier|private
name|Cache
name|projectCache
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ConfiguredRepositoryFactory
name|repositoryFactory
decl_stmt|;
specifier|private
name|Configuration
name|configuration
decl_stmt|;
specifier|private
name|ArtifactRepository
name|localRepository
decl_stmt|;
specifier|private
name|List
name|repositories
decl_stmt|;
specifier|public
name|Artifact
name|createRelatedArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|String
name|groupId
init|=
name|artifact
operator|.
name|getGroupId
argument_list|()
decl_stmt|;
name|String
name|artifactId
init|=
name|artifact
operator|.
name|getArtifactId
argument_list|()
decl_stmt|;
name|String
name|version
init|=
name|artifact
operator|.
name|getVersion
argument_list|()
decl_stmt|;
name|String
name|reltype
init|=
name|StringUtils
operator|.
name|defaultIfEmpty
argument_list|(
name|type
argument_list|,
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|artifactFactory
operator|.
name|createArtifactWithClassifier
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|reltype
argument_list|,
name|classifier
argument_list|)
return|;
block|}
specifier|public
name|ManagedArtifact
name|findArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|ManagedArtifact
name|managedArtifact
init|=
operator|(
name|ManagedArtifact
operator|)
name|artifactCache
operator|.
name|get
argument_list|(
name|toKey
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|managedArtifact
operator|!=
literal|null
condition|)
block|{
return|return
name|managedArtifact
return|;
block|}
name|boolean
name|snapshot
init|=
name|artifact
operator|.
name|isSnapshot
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|repositories
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
name|ArtifactRepository
name|repository
init|=
operator|(
name|ArtifactRepository
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|snapshot
operator|&&
operator|!
name|repository
operator|.
name|getSnapshots
argument_list|()
operator|.
name|isEnabled
argument_list|()
condition|)
block|{
comment|// skip repo.
continue|continue;
block|}
name|String
name|path
init|=
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
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
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|f
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// Found it.
name|managedArtifact
operator|=
name|createManagedArtifact
argument_list|(
name|repository
argument_list|,
name|artifact
argument_list|,
name|f
argument_list|)
expr_stmt|;
name|artifactCache
operator|.
name|put
argument_list|(
name|toKey
argument_list|(
name|artifact
argument_list|)
argument_list|,
name|managedArtifact
argument_list|)
expr_stmt|;
return|return
name|managedArtifact
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|ManagedArtifact
name|findArtifact
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
throws|throws
name|ProjectBuildingException
block|{
name|MavenProject
name|project
init|=
name|findProject
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|Model
name|model
init|=
name|project
operator|.
name|getModel
argument_list|()
decl_stmt|;
return|return
name|findArtifact
argument_list|(
name|model
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|model
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|model
operator|.
name|getVersion
argument_list|()
argument_list|,
name|model
operator|.
name|getPackaging
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|ManagedArtifact
name|findArtifact
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
parameter_list|)
block|{
name|Artifact
name|artifact
init|=
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|type
argument_list|)
decl_stmt|;
return|return
name|findArtifact
argument_list|(
name|artifact
argument_list|)
return|;
block|}
specifier|public
name|MavenProject
name|findProject
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
throws|throws
name|ProjectBuildingException
block|{
name|MavenProject
name|project
init|=
operator|(
name|MavenProject
operator|)
name|projectCache
operator|.
name|get
argument_list|(
name|toKey
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|project
operator|!=
literal|null
condition|)
block|{
return|return
name|project
return|;
block|}
name|Artifact
name|projectArtifact
init|=
name|artifactFactory
operator|.
name|createProjectArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|project
operator|=
name|projectBuilder
operator|.
name|buildFromRepository
argument_list|(
name|projectArtifact
argument_list|,
name|repositories
argument_list|,
name|localRepository
argument_list|)
expr_stmt|;
name|projectCache
operator|.
name|put
argument_list|(
name|toKey
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
argument_list|,
name|project
argument_list|)
expr_stmt|;
return|return
name|project
return|;
block|}
specifier|public
name|ArtifactRepository
name|getArtifactRepository
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|RepositoryConfiguration
name|repoConfig
init|=
name|getRepositoryConfiguration
argument_list|(
name|id
argument_list|)
decl_stmt|;
if|if
condition|(
name|repoConfig
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|repositoryFactory
operator|.
name|createRepository
argument_list|(
name|repoConfig
argument_list|)
return|;
block|}
specifier|public
name|List
name|getAllArtifactRepositories
parameter_list|()
block|{
return|return
name|repositoryFactory
operator|.
name|createRepositories
argument_list|(
name|configuration
argument_list|)
return|;
block|}
specifier|public
name|RepositoryConfiguration
name|getRepositoryConfiguration
parameter_list|(
name|String
name|id
parameter_list|)
block|{
return|return
name|this
operator|.
name|configuration
operator|.
name|getRepositoryById
argument_list|(
name|id
argument_list|)
return|;
block|}
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|InitializationException
block|{
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|archivaConfiguration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|configureSelf
argument_list|(
name|config
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|toKey
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
if|if
condition|(
name|artifact
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
return|return
name|toKey
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
return|;
block|}
specifier|private
name|String
name|toKey
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
specifier|private
name|void
name|configureSelf
parameter_list|(
name|Configuration
name|config
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|config
expr_stmt|;
name|this
operator|.
name|artifactCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|projectCache
operator|.
name|clear
argument_list|()
expr_stmt|;
name|repositories
operator|=
name|repositoryFactory
operator|.
name|createRepositories
argument_list|(
name|this
operator|.
name|configuration
argument_list|)
expr_stmt|;
name|localRepository
operator|=
name|repositoryFactory
operator|.
name|createLocalRepository
argument_list|(
name|this
operator|.
name|configuration
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ManagedArtifact
name|createManagedArtifact
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|File
name|f
parameter_list|)
block|{
name|artifact
operator|.
name|isSnapshot
argument_list|()
expr_stmt|;
name|String
name|path
init|=
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
switch|switch
condition|(
name|ManagedArtifactTypes
operator|.
name|whichType
argument_list|(
name|artifact
operator|.
name|getType
argument_list|()
argument_list|)
condition|)
block|{
case|case
name|ManagedArtifactTypes
operator|.
name|EJB
case|:
name|ManagedEjbArtifact
name|managedEjbArtifact
init|=
operator|new
name|ManagedEjbArtifact
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|artifact
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|managedEjbArtifact
operator|.
name|setJavadocPath
argument_list|(
name|pathToRelatedArtifact
argument_list|(
name|repository
argument_list|,
name|artifact
argument_list|,
literal|"javadoc"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
name|managedEjbArtifact
operator|.
name|setSourcesPath
argument_list|(
name|pathToRelatedArtifact
argument_list|(
name|repository
argument_list|,
name|artifact
argument_list|,
literal|"sources"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
name|managedEjbArtifact
operator|.
name|setClientPath
argument_list|(
name|pathToRelatedArtifact
argument_list|(
name|repository
argument_list|,
name|artifact
argument_list|,
literal|"client"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|managedEjbArtifact
return|;
case|case
name|ManagedArtifactTypes
operator|.
name|JAVA
case|:
name|ManagedJavaArtifact
name|managedJavaArtifact
init|=
operator|new
name|ManagedJavaArtifact
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|artifact
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|managedJavaArtifact
operator|.
name|setJavadocPath
argument_list|(
name|pathToRelatedArtifact
argument_list|(
name|repository
argument_list|,
name|artifact
argument_list|,
literal|"javadoc"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
name|managedJavaArtifact
operator|.
name|setSourcesPath
argument_list|(
name|pathToRelatedArtifact
argument_list|(
name|repository
argument_list|,
name|artifact
argument_list|,
literal|"sources"
argument_list|,
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|managedJavaArtifact
return|;
case|case
name|ManagedArtifactTypes
operator|.
name|GENERIC
case|:
default|default:
return|return
operator|new
name|ManagedArtifact
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|,
name|artifact
argument_list|,
name|path
argument_list|)
return|;
block|}
block|}
specifier|private
name|String
name|pathToRelatedArtifact
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|Artifact
name|relatedArtifact
init|=
name|createRelatedArtifact
argument_list|(
name|artifact
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|relatedArtifact
operator|.
name|isSnapshot
argument_list|()
expr_stmt|;
name|String
name|path
init|=
name|repository
operator|.
name|pathOf
argument_list|(
name|relatedArtifact
argument_list|)
decl_stmt|;
name|File
name|relatedFile
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
operator|!
name|relatedFile
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// Return null to set the ManagedArtifact related path to empty.
return|return
literal|null
return|;
block|}
return|return
name|path
return|;
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
comment|// nothing to do
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
name|propertyName
operator|.
name|startsWith
argument_list|(
literal|"repositories"
argument_list|)
operator|||
name|propertyName
operator|.
name|startsWith
argument_list|(
literal|"localRepository"
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Triggering managed repository configuration change with "
operator|+
name|propertyName
operator|+
literal|" set to "
operator|+
name|propertyValue
argument_list|)
expr_stmt|;
name|configureSelf
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Not triggering managed repository configuration change with "
operator|+
name|propertyName
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|long
name|getLastDataRefreshTime
parameter_list|()
block|{
name|long
name|lastDataRefreshTime
init|=
literal|0
decl_stmt|;
for|for
control|(
name|Iterator
name|i
init|=
name|getAllArtifactRepositories
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
name|ArtifactRepository
name|repository
init|=
operator|(
name|ArtifactRepository
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|DiscovererStatistics
name|stats
init|=
operator|new
name|DiscovererStatistics
argument_list|(
name|repository
argument_list|)
decl_stmt|;
if|if
condition|(
name|stats
operator|.
name|getTimestampFinished
argument_list|()
operator|>
name|lastDataRefreshTime
condition|)
block|{
name|lastDataRefreshTime
operator|=
name|stats
operator|.
name|getTimestampFinished
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|lastDataRefreshTime
return|;
block|}
specifier|public
name|boolean
name|needsDataRefresh
parameter_list|()
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|getAllArtifactRepositories
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
name|ArtifactRepository
name|repository
init|=
operator|(
name|ArtifactRepository
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|DiscovererStatistics
name|stats
init|=
operator|new
name|DiscovererStatistics
argument_list|(
name|repository
argument_list|)
decl_stmt|;
if|if
condition|(
name|stats
operator|.
name|getTimestampFinished
argument_list|()
operator|<=
literal|0
condition|)
block|{
comment|// Found a repository that has NEVER had it's data walked.
return|return
literal|true
return|;
block|}
block|}
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

