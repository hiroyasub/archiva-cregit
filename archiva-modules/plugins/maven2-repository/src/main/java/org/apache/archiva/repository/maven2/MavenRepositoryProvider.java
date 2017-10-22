begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|maven2
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
name|configuration
operator|.
name|AbstractRepositoryConfiguration
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
name|archiva
operator|.
name|configuration
operator|.
name|RemoteRepositoryConfiguration
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
name|EditableManagedRepository
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
name|EditableRemoteRepository
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
name|EditableRepository
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
name|ManagedRepository
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
name|PasswordCredentials
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
name|ReleaseScheme
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
name|RemoteRepository
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
name|RepositoryCredentials
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
name|RepositoryException
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
name|RepositoryProvider
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
name|RepositoryType
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
name|UnsupportedURIException
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
name|features
operator|.
name|ArtifactCleanupFeature
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
name|features
operator|.
name|IndexCreationFeature
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
name|features
operator|.
name|RemoteIndexFeature
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
name|features
operator|.
name|StagingRepositoryFeature
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
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Period
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|temporal
operator|.
name|ChronoUnit
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashSet
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
comment|/**  * Provider for the maven2 repository implementations  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"mavenRepositoryProvider"
argument_list|)
specifier|public
class|class
name|MavenRepositoryProvider
implements|implements
name|RepositoryProvider
block|{
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MavenRepositoryProvider
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|static
specifier|final
name|Set
argument_list|<
name|RepositoryType
argument_list|>
name|TYPES
init|=
operator|new
name|HashSet
argument_list|<>
argument_list|(  )
decl_stmt|;
static|static
block|{
name|TYPES
operator|.
name|add
argument_list|(
name|RepositoryType
operator|.
name|MAVEN
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Set
argument_list|<
name|RepositoryType
argument_list|>
name|provides
parameter_list|( )
block|{
return|return
name|TYPES
return|;
block|}
annotation|@
name|Override
specifier|public
name|EditableManagedRepository
name|createManagedInstance
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|MavenManagedRepository
argument_list|(
name|id
argument_list|,
name|name
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|EditableRemoteRepository
name|createRemoteInstance
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
operator|new
name|MavenRemoteRepository
argument_list|(
name|id
argument_list|,
name|name
argument_list|)
return|;
block|}
specifier|private
name|URI
name|getURIFromString
parameter_list|(
name|String
name|uriStr
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|URI
name|uri
decl_stmt|;
try|try
block|{
if|if
condition|(
name|uriStr
operator|.
name|startsWith
argument_list|(
literal|"/"
argument_list|)
condition|)
block|{
comment|// only absolute paths are prepended with file scheme
name|uri
operator|=
operator|new
name|URI
argument_list|(
literal|"file://"
operator|+
name|uriStr
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|uriStr
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|uri
operator|.
name|getScheme
argument_list|()
operator|!=
literal|null
operator|&&
operator|!
literal|"file"
operator|.
name|equals
argument_list|(
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Bad URI scheme found: {}, URI={}"
argument_list|,
name|uri
operator|.
name|getScheme
argument_list|()
argument_list|,
name|uri
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"The uri "
operator|+
name|uriStr
operator|+
literal|" is not valid. Only file:// URI is allowed for maven."
argument_list|)
throw|;
block|}
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|String
name|newCfg
init|=
literal|"file://"
operator|+
name|uriStr
decl_stmt|;
try|try
block|{
name|uri
operator|=
operator|new
name|URI
argument_list|(
name|newCfg
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e1
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not create URI from {} -> "
argument_list|,
name|uriStr
argument_list|,
name|newCfg
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"The config entry "
operator|+
name|uriStr
operator|+
literal|" cannot be converted to URI."
argument_list|)
throw|;
block|}
block|}
return|return
name|uri
return|;
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepository
name|createManagedInstance
parameter_list|(
name|ManagedRepositoryConfiguration
name|cfg
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|MavenManagedRepository
name|repo
init|=
operator|new
name|MavenManagedRepository
argument_list|(
name|cfg
operator|.
name|getId
argument_list|()
argument_list|,
name|cfg
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|updateManagedInstance
argument_list|(
name|repo
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateManagedInstance
parameter_list|(
name|EditableManagedRepository
name|repo
parameter_list|,
name|ManagedRepositoryConfiguration
name|cfg
parameter_list|)
throws|throws
name|RepositoryException
block|{
try|try
block|{
name|repo
operator|.
name|setLocation
argument_list|(
name|getURIFromString
argument_list|(
name|cfg
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedURIException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"The location entry is not a valid uri: "
operator|+
name|cfg
operator|.
name|getLocation
argument_list|()
argument_list|)
throw|;
block|}
name|setBaseConfig
argument_list|(
name|repo
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
name|Path
name|repoDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repo
operator|.
name|getAbsoluteLocation
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|repoDir
argument_list|)
condition|)
block|{
try|try
block|{
name|Files
operator|.
name|createDirectories
argument_list|(
name|repoDir
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not create directory {} for repository {}"
argument_list|,
name|repo
operator|.
name|getAbsoluteLocation
argument_list|()
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"Could not create directory for repository "
operator|+
name|repo
operator|.
name|getAbsoluteLocation
argument_list|()
argument_list|)
throw|;
block|}
block|}
name|repo
operator|.
name|setSchedulingDefinition
argument_list|(
name|cfg
operator|.
name|getRefreshCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setBlocksRedeployment
argument_list|(
name|cfg
operator|.
name|isBlockRedeployments
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setScanned
argument_list|(
name|cfg
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cfg
operator|.
name|isReleases
argument_list|()
condition|)
block|{
name|repo
operator|.
name|addActiveReleaseScheme
argument_list|(
name|ReleaseScheme
operator|.
name|RELEASE
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cfg
operator|.
name|isSnapshots
argument_list|()
condition|)
block|{
name|repo
operator|.
name|addActiveReleaseScheme
argument_list|(
name|ReleaseScheme
operator|.
name|SNAPSHOT
argument_list|)
expr_stmt|;
block|}
name|StagingRepositoryFeature
name|stagingRepositoryFeature
init|=
name|repo
operator|.
name|getFeature
argument_list|(
name|StagingRepositoryFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|stagingRepositoryFeature
operator|.
name|setStageRepoNeeded
argument_list|(
name|cfg
operator|.
name|isStageRepoNeeded
argument_list|()
argument_list|)
expr_stmt|;
name|IndexCreationFeature
name|indexCreationFeature
init|=
name|repo
operator|.
name|getFeature
argument_list|(
name|IndexCreationFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|( )
decl_stmt|;
name|indexCreationFeature
operator|.
name|setSkipPackedIndexCreation
argument_list|(
name|cfg
operator|.
name|isSkipPackedIndexCreation
argument_list|()
argument_list|)
expr_stmt|;
name|indexCreationFeature
operator|.
name|setIndexPath
argument_list|(
name|getURIFromString
argument_list|(
name|cfg
operator|.
name|getIndexDir
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|ArtifactCleanupFeature
name|artifactCleanupFeature
init|=
name|repo
operator|.
name|getFeature
argument_list|(
name|ArtifactCleanupFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|artifactCleanupFeature
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
name|cfg
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|artifactCleanupFeature
operator|.
name|setRetentionCount
argument_list|(
name|cfg
operator|.
name|getRetentionCount
argument_list|()
argument_list|)
expr_stmt|;
name|artifactCleanupFeature
operator|.
name|setRetentionTime
argument_list|(
name|Period
operator|.
name|ofDays
argument_list|(
name|cfg
operator|.
name|getRetentionTime
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepository
name|createStagingInstance
parameter_list|(
name|ManagedRepositoryConfiguration
name|baseConfiguration
parameter_list|)
throws|throws
name|RepositoryException
block|{
return|return
name|createManagedInstance
argument_list|(
name|getStageRepoConfig
argument_list|(
name|baseConfiguration
argument_list|)
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|RemoteRepository
name|createRemoteInstance
parameter_list|(
name|RemoteRepositoryConfiguration
name|cfg
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|MavenRemoteRepository
name|repo
init|=
operator|new
name|MavenRemoteRepository
argument_list|(
name|cfg
operator|.
name|getId
argument_list|( )
argument_list|,
name|cfg
operator|.
name|getName
argument_list|( )
argument_list|)
decl_stmt|;
name|updateRemoteInstance
argument_list|(
name|repo
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateRemoteInstance
parameter_list|(
name|EditableRemoteRepository
name|repo
parameter_list|,
name|RemoteRepositoryConfiguration
name|cfg
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|setBaseConfig
argument_list|(
name|repo
argument_list|,
name|cfg
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setCheckPath
argument_list|(
name|cfg
operator|.
name|getCheckPath
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setSchedulingDefinition
argument_list|(
name|cfg
operator|.
name|getRefreshCronExpression
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|repo
operator|.
name|setLocation
argument_list|(
operator|new
name|URI
argument_list|(
name|cfg
operator|.
name|getUrl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|UnsupportedURIException
decl||
name|URISyntaxException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not set remote url "
operator|+
name|cfg
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"The url config is not a valid uri: "
operator|+
name|cfg
operator|.
name|getUrl
argument_list|()
argument_list|)
throw|;
block|}
name|repo
operator|.
name|setTimeout
argument_list|(
name|Duration
operator|.
name|ofSeconds
argument_list|(
name|cfg
operator|.
name|getTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|RemoteIndexFeature
name|remoteIndexFeature
init|=
name|repo
operator|.
name|getFeature
argument_list|(
name|RemoteIndexFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|remoteIndexFeature
operator|.
name|setDownloadRemoteIndex
argument_list|(
name|cfg
operator|.
name|isDownloadRemoteIndex
argument_list|()
argument_list|)
expr_stmt|;
name|remoteIndexFeature
operator|.
name|setDownloadRemoteIndexOnStartup
argument_list|(
name|cfg
operator|.
name|isDownloadRemoteIndexOnStartup
argument_list|()
argument_list|)
expr_stmt|;
name|remoteIndexFeature
operator|.
name|setDownloadTimeout
argument_list|(
name|Duration
operator|.
name|ofSeconds
argument_list|(
name|cfg
operator|.
name|getRemoteDownloadTimeout
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|remoteIndexFeature
operator|.
name|setProxyId
argument_list|(
name|cfg
operator|.
name|getRemoteDownloadNetworkProxyId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|cfg
operator|.
name|isDownloadRemoteIndex
argument_list|()
condition|)
block|{
try|try
block|{
name|remoteIndexFeature
operator|.
name|setIndexUri
argument_list|(
operator|new
name|URI
argument_list|(
name|cfg
operator|.
name|getRemoteIndexUrl
argument_list|( )
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not set remote index url "
operator|+
name|cfg
operator|.
name|getRemoteIndexUrl
argument_list|( )
argument_list|)
expr_stmt|;
name|remoteIndexFeature
operator|.
name|setDownloadRemoteIndex
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|remoteIndexFeature
operator|.
name|setDownloadRemoteIndexOnStartup
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
name|repo
operator|.
name|setExtraHeaders
argument_list|(
name|cfg
operator|.
name|getExtraHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setExtraParameters
argument_list|(
name|cfg
operator|.
name|getExtraParameters
argument_list|()
argument_list|)
expr_stmt|;
name|PasswordCredentials
name|credentials
init|=
operator|new
name|PasswordCredentials
argument_list|()
decl_stmt|;
if|if
condition|(
name|cfg
operator|.
name|getPassword
argument_list|()
operator|!=
literal|null
operator|&&
name|cfg
operator|.
name|getUsername
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|credentials
operator|.
name|setPassword
argument_list|(
name|cfg
operator|.
name|getPassword
argument_list|( )
operator|.
name|toCharArray
argument_list|( )
argument_list|)
expr_stmt|;
name|credentials
operator|.
name|setUsername
argument_list|(
name|cfg
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setCredentials
argument_list|(
name|credentials
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|credentials
operator|.
name|setPassword
argument_list|(
operator|new
name|char
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|cfg
operator|.
name|getIndexDir
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|IndexCreationFeature
name|indexCreationFeature
init|=
name|repo
operator|.
name|getFeature
argument_list|(
name|IndexCreationFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|indexCreationFeature
operator|.
name|setIndexPath
argument_list|(
name|getURIFromString
argument_list|(
name|cfg
operator|.
name|getIndexDir
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RemoteRepositoryConfiguration
name|getRemoteConfiguration
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|)
throws|throws
name|RepositoryException
block|{
if|if
condition|(
operator|!
operator|(
name|remoteRepository
operator|instanceof
name|MavenRemoteRepository
operator|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Wrong remote repository type "
operator|+
name|remoteRepository
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"The given repository type cannot be handled by the maven provider: "
operator|+
name|remoteRepository
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|RemoteRepositoryConfiguration
name|cfg
init|=
operator|new
name|RemoteRepositoryConfiguration
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setType
argument_list|(
name|remoteRepository
operator|.
name|getType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setId
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setName
argument_list|(
name|remoteRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setDescription
argument_list|(
name|remoteRepository
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setUrl
argument_list|(
name|remoteRepository
operator|.
name|getLocation
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setTimeout
argument_list|(
operator|(
name|int
operator|)
name|remoteRepository
operator|.
name|getTimeout
argument_list|()
operator|.
name|toMillis
argument_list|()
operator|/
literal|1000
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setCheckPath
argument_list|(
name|remoteRepository
operator|.
name|getCheckPath
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryCredentials
name|creds
init|=
name|remoteRepository
operator|.
name|getLoginCredentials
argument_list|( )
decl_stmt|;
if|if
condition|(
name|creds
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|creds
operator|instanceof
name|PasswordCredentials
condition|)
block|{
name|PasswordCredentials
name|pCreds
init|=
operator|(
name|PasswordCredentials
operator|)
name|creds
decl_stmt|;
name|cfg
operator|.
name|setPassword
argument_list|(
operator|new
name|String
argument_list|(
name|pCreds
operator|.
name|getPassword
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setUsername
argument_list|(
name|pCreds
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|cfg
operator|.
name|setLayout
argument_list|(
name|remoteRepository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setExtraParameters
argument_list|(
name|remoteRepository
operator|.
name|getExtraParameters
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setExtraHeaders
argument_list|(
name|remoteRepository
operator|.
name|getExtraHeaders
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setRefreshCronExpression
argument_list|(
name|remoteRepository
operator|.
name|getSchedulingDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|IndexCreationFeature
name|indexCreationFeature
init|=
name|remoteRepository
operator|.
name|getFeature
argument_list|(
name|IndexCreationFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setIndexDir
argument_list|(
name|indexCreationFeature
operator|.
name|getIndexPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|RemoteIndexFeature
name|remoteIndexFeature
init|=
name|remoteRepository
operator|.
name|getFeature
argument_list|(
name|RemoteIndexFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setRemoteIndexUrl
argument_list|(
name|remoteIndexFeature
operator|.
name|getIndexUri
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setRemoteDownloadTimeout
argument_list|(
operator|(
name|int
operator|)
name|remoteIndexFeature
operator|.
name|getDownloadTimeout
argument_list|()
operator|.
name|get
argument_list|(
name|ChronoUnit
operator|.
name|SECONDS
argument_list|)
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setDownloadRemoteIndexOnStartup
argument_list|(
name|remoteIndexFeature
operator|.
name|isDownloadRemoteIndexOnStartup
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setDownloadRemoteIndex
argument_list|(
name|remoteIndexFeature
operator|.
name|isDownloadRemoteIndex
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setRemoteDownloadNetworkProxyId
argument_list|(
name|remoteIndexFeature
operator|.
name|getProxyId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|cfg
return|;
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepositoryConfiguration
name|getManagedConfiguration
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|RepositoryException
block|{
if|if
condition|(
operator|!
operator|(
name|managedRepository
operator|instanceof
name|MavenManagedRepository
operator|)
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Wrong remote repository type "
operator|+
name|managedRepository
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"The given repository type cannot be handled by the maven provider: "
operator|+
name|managedRepository
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
throw|;
block|}
name|ManagedRepositoryConfiguration
name|cfg
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setType
argument_list|(
name|managedRepository
operator|.
name|getType
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setId
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setName
argument_list|(
name|managedRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setDescription
argument_list|(
name|managedRepository
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setLocation
argument_list|(
name|managedRepository
operator|.
name|getLocation
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setLayout
argument_list|(
name|managedRepository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setRefreshCronExpression
argument_list|(
name|managedRepository
operator|.
name|getSchedulingDefinition
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setScanned
argument_list|(
name|managedRepository
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setBlockRedeployments
argument_list|(
name|managedRepository
operator|.
name|blocksRedeployments
argument_list|()
argument_list|)
expr_stmt|;
name|StagingRepositoryFeature
name|stagingRepositoryFeature
init|=
name|managedRepository
operator|.
name|getFeature
argument_list|(
name|StagingRepositoryFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setStageRepoNeeded
argument_list|(
name|stagingRepositoryFeature
operator|.
name|isStageRepoNeeded
argument_list|()
argument_list|)
expr_stmt|;
name|IndexCreationFeature
name|indexCreationFeature
init|=
name|managedRepository
operator|.
name|getFeature
argument_list|(
name|IndexCreationFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setIndexDir
argument_list|(
name|indexCreationFeature
operator|.
name|getIndexPath
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setSkipPackedIndexCreation
argument_list|(
name|indexCreationFeature
operator|.
name|isSkipPackedIndexCreation
argument_list|()
argument_list|)
expr_stmt|;
name|ArtifactCleanupFeature
name|artifactCleanupFeature
init|=
name|managedRepository
operator|.
name|getFeature
argument_list|(
name|ArtifactCleanupFeature
operator|.
name|class
argument_list|)
operator|.
name|get
argument_list|()
decl_stmt|;
name|cfg
operator|.
name|setRetentionCount
argument_list|(
name|artifactCleanupFeature
operator|.
name|getRetentionCount
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setRetentionTime
argument_list|(
name|artifactCleanupFeature
operator|.
name|getRetentionTime
argument_list|()
operator|.
name|getDays
argument_list|()
argument_list|)
expr_stmt|;
name|cfg
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
name|artifactCleanupFeature
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|managedRepository
operator|.
name|getActiveReleaseSchemes
argument_list|()
operator|.
name|contains
argument_list|(
name|ReleaseScheme
operator|.
name|RELEASE
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfg
operator|.
name|setReleases
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|managedRepository
operator|.
name|getActiveReleaseSchemes
argument_list|()
operator|.
name|contains
argument_list|(
name|ReleaseScheme
operator|.
name|SNAPSHOT
argument_list|)
condition|)
block|{
name|cfg
operator|.
name|setSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|cfg
operator|.
name|setSnapshots
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
return|return
name|cfg
return|;
block|}
specifier|private
name|ManagedRepositoryConfiguration
name|getStageRepoConfig
parameter_list|(
name|ManagedRepositoryConfiguration
name|repository
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|stagingRepository
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|stagingRepository
operator|.
name|setId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
operator|+
name|StagingRepositoryFeature
operator|.
name|STAGING_REPO_POSTFIX
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setLayout
argument_list|(
name|repository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setName
argument_list|(
name|repository
operator|.
name|getName
argument_list|()
operator|+
name|StagingRepositoryFeature
operator|.
name|STAGING_REPO_POSTFIX
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setBlockRedeployments
argument_list|(
name|repository
operator|.
name|isBlockRedeployments
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setRetentionTime
argument_list|(
name|repository
operator|.
name|getRetentionTime
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
name|repository
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|path
init|=
name|repository
operator|.
name|getLocation
argument_list|()
decl_stmt|;
name|int
name|lastIndex
init|=
name|path
operator|.
name|replace
argument_list|(
literal|'\\'
argument_list|,
literal|'/'
argument_list|)
operator|.
name|lastIndexOf
argument_list|(
literal|'/'
argument_list|)
decl_stmt|;
name|stagingRepository
operator|.
name|setLocation
argument_list|(
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|lastIndex
argument_list|)
operator|+
literal|"/"
operator|+
name|stagingRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|repository
operator|.
name|getIndexDir
argument_list|()
argument_list|)
condition|)
block|{
name|Path
name|indexDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repository
operator|.
name|getIndexDir
argument_list|()
argument_list|)
decl_stmt|;
comment|// in case of absolute dir do not use the same
if|if
condition|(
name|indexDir
operator|.
name|isAbsolute
argument_list|()
condition|)
block|{
name|stagingRepository
operator|.
name|setIndexDir
argument_list|(
name|stagingRepository
operator|.
name|getLocation
argument_list|()
operator|+
literal|"/.index"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|stagingRepository
operator|.
name|setIndexDir
argument_list|(
name|repository
operator|.
name|getIndexDir
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|stagingRepository
operator|.
name|setRefreshCronExpression
argument_list|(
name|repository
operator|.
name|getRefreshCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setReleases
argument_list|(
name|repository
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setRetentionCount
argument_list|(
name|repository
operator|.
name|getRetentionCount
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setScanned
argument_list|(
name|repository
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setSnapshots
argument_list|(
name|repository
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|stagingRepository
operator|.
name|setSkipPackedIndexCreation
argument_list|(
name|repository
operator|.
name|isSkipPackedIndexCreation
argument_list|()
argument_list|)
expr_stmt|;
comment|// do not duplicate description
comment|//stagingRepository.getDescription("")
return|return
name|stagingRepository
return|;
block|}
specifier|private
name|void
name|setBaseConfig
parameter_list|(
name|EditableRepository
name|repo
parameter_list|,
name|AbstractRepositoryConfiguration
name|cfg
parameter_list|)
throws|throws
name|RepositoryException
block|{
specifier|final
name|String
name|baseUriStr
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getArchivaRuntimeConfiguration
argument_list|()
operator|.
name|getRepositoryBaseDirectory
argument_list|()
decl_stmt|;
try|try
block|{
name|URI
name|baseUri
init|=
operator|new
name|URI
argument_list|(
name|baseUriStr
argument_list|)
decl_stmt|;
name|repo
operator|.
name|setBaseUri
argument_list|(
name|baseUri
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not set base URI {}: {}"
argument_list|,
name|baseUriStr
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"Could not set base URI "
operator|+
name|baseUriStr
argument_list|)
throw|;
block|}
name|repo
operator|.
name|setDescription
argument_list|(
name|repo
operator|.
name|getPrimaryLocale
argument_list|()
argument_list|,
name|cfg
operator|.
name|getDescription
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLayout
argument_list|(
name|cfg
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ArchivaConfiguration
name|getArchivaConfiguration
parameter_list|( )
block|{
return|return
name|archivaConfiguration
return|;
block|}
specifier|public
name|void
name|setArchivaConfiguration
parameter_list|(
name|ArchivaConfiguration
name|archivaConfiguration
parameter_list|)
block|{
name|this
operator|.
name|archivaConfiguration
operator|=
name|archivaConfiguration
expr_stmt|;
block|}
block|}
end_class

end_unit

