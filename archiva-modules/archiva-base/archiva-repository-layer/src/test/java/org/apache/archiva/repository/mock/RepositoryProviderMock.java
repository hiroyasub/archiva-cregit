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
name|mock
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
name|configuration
operator|.
name|RepositoryGroupConfiguration
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
name|*
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
name|event
operator|.
name|Event
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
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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
comment|/**  * Just a simple mock class for the repository provider  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"mockRepositoryProvider"
argument_list|)
specifier|public
class|class
name|RepositoryProviderMock
implements|implements
name|RepositoryProvider
block|{
specifier|private
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
argument_list|( )
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
name|TYPES
operator|.
name|add
argument_list|(
name|RepositoryType
operator|.
name|NPM
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
throws|throws
name|IOException
block|{
return|return
name|BasicManagedRepository
operator|.
name|newFilesystemInstance
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
literal|"target/repositories"
argument_list|)
operator|.
name|resolve
argument_list|(
name|id
argument_list|)
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
try|try
block|{
return|return
name|BasicRemoteRepository
operator|.
name|newFilesystemInstance
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
literal|"target/remotes"
argument_list|)
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|EditableRepositoryGroup
name|createRepositoryGroup
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|)
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepository
name|createManagedInstance
parameter_list|(
name|ManagedRepositoryConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|BasicManagedRepository
name|managedRepository
init|=
literal|null
decl_stmt|;
try|try
block|{
name|managedRepository
operator|=
name|BasicManagedRepository
operator|.
name|newFilesystemInstance
argument_list|(
name|configuration
operator|.
name|getId
argument_list|()
argument_list|,
name|configuration
operator|.
name|getName
argument_list|()
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
literal|"target/repositories"
argument_list|)
operator|.
name|resolve
argument_list|(
name|configuration
operator|.
name|getId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|updateManagedInstance
argument_list|(
name|managedRepository
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
return|return
name|managedRepository
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateManagedInstance
parameter_list|(
name|EditableManagedRepository
name|managedRepository
parameter_list|,
name|ManagedRepositoryConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
block|{
try|try
block|{
name|managedRepository
operator|.
name|setName
argument_list|(
name|managedRepository
operator|.
name|getPrimaryLocale
argument_list|()
argument_list|,
name|configuration
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setLocation
argument_list|(
operator|new
name|URI
argument_list|(
name|configuration
operator|.
name|getLocation
argument_list|( )
operator|==
literal|null
condition|?
literal|""
else|:
name|configuration
operator|.
name|getLocation
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setBaseUri
argument_list|(
operator|new
name|URI
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setBlocksRedeployment
argument_list|(
name|configuration
operator|.
name|isBlockRedeployments
argument_list|( )
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setDescription
argument_list|(
name|managedRepository
operator|.
name|getPrimaryLocale
argument_list|()
argument_list|,
name|configuration
operator|.
name|getDescription
argument_list|( )
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setLayout
argument_list|(
name|configuration
operator|.
name|getLayout
argument_list|( )
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setScanned
argument_list|(
name|configuration
operator|.
name|isScanned
argument_list|( )
argument_list|)
expr_stmt|;
name|managedRepository
operator|.
name|setSchedulingDefinition
argument_list|(
name|configuration
operator|.
name|getRefreshCronExpression
argument_list|( )
argument_list|)
expr_stmt|;
if|if
condition|(
name|configuration
operator|.
name|isReleases
argument_list|()
condition|)
block|{
name|managedRepository
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
name|configuration
operator|.
name|isSnapshots
argument_list|()
condition|)
block|{
name|managedRepository
operator|.
name|addActiveReleaseScheme
argument_list|(
name|ReleaseScheme
operator|.
name|SNAPSHOT
argument_list|)
expr_stmt|;
block|}
name|ArtifactCleanupFeature
name|acf
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
argument_list|( )
decl_stmt|;
name|acf
operator|.
name|setRetentionPeriod
argument_list|(
name|Period
operator|.
name|ofDays
argument_list|(
name|configuration
operator|.
name|getRetentionPeriod
argument_list|( )
argument_list|)
argument_list|)
expr_stmt|;
name|acf
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
name|configuration
operator|.
name|isDeleteReleasedSnapshots
argument_list|( )
argument_list|)
expr_stmt|;
name|acf
operator|.
name|setRetentionCount
argument_list|(
name|configuration
operator|.
name|getRetentionCount
argument_list|( )
argument_list|)
expr_stmt|;
name|IndexCreationFeature
name|icf
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
argument_list|( )
decl_stmt|;
name|icf
operator|.
name|setIndexPath
argument_list|(
operator|new
name|URI
argument_list|(
name|configuration
operator|.
name|getIndexDir
argument_list|( )
argument_list|)
argument_list|)
expr_stmt|;
name|icf
operator|.
name|setSkipPackedIndexCreation
argument_list|(
name|configuration
operator|.
name|isSkipPackedIndexCreation
argument_list|( )
argument_list|)
expr_stmt|;
name|StagingRepositoryFeature
name|srf
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
argument_list|( )
decl_stmt|;
name|srf
operator|.
name|setStageRepoNeeded
argument_list|(
name|configuration
operator|.
name|isStageRepoNeeded
argument_list|( )
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
name|RepositoryException
argument_list|(
literal|"Error"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepository
name|createStagingInstance
parameter_list|(
name|ManagedRepositoryConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|String
name|id
init|=
name|configuration
operator|.
name|getId
argument_list|( )
operator|+
name|StagingRepositoryFeature
operator|.
name|STAGING_REPO_POSTFIX
decl_stmt|;
name|BasicManagedRepository
name|managedRepository
init|=
literal|null
decl_stmt|;
try|try
block|{
name|managedRepository
operator|=
name|BasicManagedRepository
operator|.
name|newFilesystemInstance
argument_list|(
name|id
argument_list|,
name|configuration
operator|.
name|getName
argument_list|()
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
literal|"target/repositories"
argument_list|)
operator|.
name|resolve
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|updateManagedInstance
argument_list|(
name|managedRepository
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
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
operator|.
name|setStageRepoNeeded
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|managedRepository
return|;
block|}
annotation|@
name|Override
specifier|public
name|RemoteRepository
name|createRemoteInstance
parameter_list|(
name|RemoteRepositoryConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|BasicRemoteRepository
name|remoteRepository
init|=
literal|null
decl_stmt|;
try|try
block|{
name|remoteRepository
operator|=
name|BasicRemoteRepository
operator|.
name|newFilesystemInstance
argument_list|(
name|configuration
operator|.
name|getId
argument_list|( )
argument_list|,
name|configuration
operator|.
name|getName
argument_list|( )
argument_list|,
name|Paths
operator|.
name|get
argument_list|(
literal|"target/remotes"
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
name|e
argument_list|)
throw|;
block|}
name|updateRemoteInstance
argument_list|(
name|remoteRepository
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
return|return
name|remoteRepository
return|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Override
specifier|public
name|void
name|updateRemoteInstance
parameter_list|(
name|EditableRemoteRepository
name|remoteRepository
parameter_list|,
name|RemoteRepositoryConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
block|{
try|try
block|{
name|remoteRepository
operator|.
name|setName
argument_list|(
name|remoteRepository
operator|.
name|getPrimaryLocale
argument_list|()
argument_list|,
name|configuration
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setBaseUri
argument_list|(
operator|new
name|URI
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setDescription
argument_list|(
name|remoteRepository
operator|.
name|getPrimaryLocale
argument_list|()
argument_list|,
name|configuration
operator|.
name|getDescription
argument_list|( )
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setLayout
argument_list|(
name|configuration
operator|.
name|getLayout
argument_list|( )
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setSchedulingDefinition
argument_list|(
name|configuration
operator|.
name|getRefreshCronExpression
argument_list|( )
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setCheckPath
argument_list|(
name|configuration
operator|.
name|getCheckPath
argument_list|( )
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setExtraHeaders
argument_list|(
name|configuration
operator|.
name|getExtraHeaders
argument_list|( )
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setExtraParameters
argument_list|(
name|configuration
operator|.
name|getExtraParameters
argument_list|( )
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setTimeout
argument_list|(
name|Duration
operator|.
name|ofSeconds
argument_list|(
name|configuration
operator|.
name|getTimeout
argument_list|( )
argument_list|)
argument_list|)
expr_stmt|;
name|char
index|[]
name|pwd
init|=
name|configuration
operator|.
name|getPassword
argument_list|()
operator|==
literal|null
condition|?
literal|""
operator|.
name|toCharArray
argument_list|()
else|:
name|configuration
operator|.
name|getPassword
argument_list|()
operator|.
name|toCharArray
argument_list|()
decl_stmt|;
name|remoteRepository
operator|.
name|setCredentials
argument_list|(
operator|new
name|PasswordCredentials
argument_list|(
name|configuration
operator|.
name|getUsername
argument_list|( )
argument_list|,
name|pwd
argument_list|)
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setLocation
argument_list|(
operator|new
name|URI
argument_list|(
name|configuration
operator|.
name|getUrl
argument_list|( )
operator|==
literal|null
condition|?
literal|""
else|:
name|configuration
operator|.
name|getUrl
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|RemoteIndexFeature
name|rif
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
argument_list|( )
decl_stmt|;
name|rif
operator|.
name|setDownloadRemoteIndexOnStartup
argument_list|(
name|configuration
operator|.
name|isDownloadRemoteIndexOnStartup
argument_list|( )
argument_list|)
expr_stmt|;
name|rif
operator|.
name|setDownloadRemoteIndex
argument_list|(
name|configuration
operator|.
name|isDownloadRemoteIndex
argument_list|( )
argument_list|)
expr_stmt|;
name|rif
operator|.
name|setIndexUri
argument_list|(
operator|new
name|URI
argument_list|(
name|configuration
operator|.
name|getIndexDir
argument_list|( )
argument_list|)
argument_list|)
expr_stmt|;
name|rif
operator|.
name|setDownloadTimeout
argument_list|(
name|Duration
operator|.
name|ofSeconds
argument_list|(
name|configuration
operator|.
name|getRemoteDownloadTimeout
argument_list|( )
argument_list|)
argument_list|)
expr_stmt|;
name|rif
operator|.
name|setProxyId
argument_list|(
name|configuration
operator|.
name|getRemoteDownloadNetworkProxyId
argument_list|( )
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
name|RepositoryException
argument_list|(
literal|"Error"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroup
name|createRepositoryGroup
parameter_list|(
name|RepositoryGroupConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|updateRepositoryGroupInstance
parameter_list|(
name|EditableRepositoryGroup
name|repositoryGroup
parameter_list|,
name|RepositoryGroupConfiguration
name|configuration
parameter_list|)
throws|throws
name|RepositoryException
block|{
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
name|ManagedRepositoryConfiguration
name|configuration
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setId
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setName
argument_list|(
name|managedRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setLocation
argument_list|(
name|managedRepository
operator|.
name|getLocation
argument_list|( )
operator|==
literal|null
condition|?
literal|""
else|:
name|managedRepository
operator|.
name|getLocation
argument_list|()
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setBlockRedeployments
argument_list|(
name|managedRepository
operator|.
name|blocksRedeployments
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setDescription
argument_list|(
name|managedRepository
operator|.
name|getDescription
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setLayout
argument_list|(
name|managedRepository
operator|.
name|getLayout
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setScanned
argument_list|(
name|managedRepository
operator|.
name|isScanned
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRefreshCronExpression
argument_list|(
name|managedRepository
operator|.
name|getSchedulingDefinition
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setReleases
argument_list|(
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
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setSnapshots
argument_list|(
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
argument_list|)
expr_stmt|;
name|ArtifactCleanupFeature
name|acf
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
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setRetentionPeriod
argument_list|(
name|acf
operator|.
name|getRetentionPeriod
argument_list|( )
operator|.
name|getDays
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
name|acf
operator|.
name|isDeleteReleasedSnapshots
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRetentionCount
argument_list|(
name|acf
operator|.
name|getRetentionCount
argument_list|( )
argument_list|)
expr_stmt|;
name|IndexCreationFeature
name|icf
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
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setSkipPackedIndexCreation
argument_list|(
name|icf
operator|.
name|isSkipPackedIndexCreation
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setIndexDir
argument_list|(
name|icf
operator|.
name|getIndexPath
argument_list|( )
operator|==
literal|null
condition|?
literal|""
else|:
name|icf
operator|.
name|getIndexPath
argument_list|()
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
name|StagingRepositoryFeature
name|srf
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
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setStageRepoNeeded
argument_list|(
name|srf
operator|.
name|isStageRepoNeeded
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryGroupConfiguration
name|getRepositoryGroupConfiguration
parameter_list|(
name|RepositoryGroup
name|repositoryGroup
parameter_list|)
throws|throws
name|RepositoryException
block|{
return|return
literal|null
return|;
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
name|RemoteRepositoryConfiguration
name|configuration
init|=
operator|new
name|RemoteRepositoryConfiguration
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setId
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setName
argument_list|(
name|remoteRepository
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setDescription
argument_list|(
name|remoteRepository
operator|.
name|getDescription
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setLayout
argument_list|(
name|remoteRepository
operator|.
name|getLayout
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRefreshCronExpression
argument_list|(
name|remoteRepository
operator|.
name|getSchedulingDefinition
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setCheckPath
argument_list|(
name|remoteRepository
operator|.
name|getCheckPath
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setExtraHeaders
argument_list|(
name|remoteRepository
operator|.
name|getExtraHeaders
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setExtraParameters
argument_list|(
name|remoteRepository
operator|.
name|getExtraParameters
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setTimeout
argument_list|(
operator|(
name|int
operator|)
name|remoteRepository
operator|.
name|getTimeout
argument_list|( )
operator|.
name|getSeconds
argument_list|( )
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
name|PasswordCredentials
name|pwdCreds
init|=
operator|(
name|PasswordCredentials
operator|)
name|creds
decl_stmt|;
name|configuration
operator|.
name|setUsername
argument_list|(
name|pwdCreds
operator|.
name|getUsername
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setPassword
argument_list|(
operator|new
name|String
argument_list|(
name|pwdCreds
operator|.
name|getPassword
argument_list|( )
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|configuration
operator|.
name|setUrl
argument_list|(
name|remoteRepository
operator|.
name|getLocation
argument_list|( )
operator|==
literal|null
condition|?
literal|""
else|:
name|remoteRepository
operator|.
name|getLocation
argument_list|()
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
name|RemoteIndexFeature
name|rif
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
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setDownloadRemoteIndex
argument_list|(
name|rif
operator|.
name|isDownloadRemoteIndex
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setDownloadRemoteIndexOnStartup
argument_list|(
name|rif
operator|.
name|isDownloadRemoteIndexOnStartup
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setIndexDir
argument_list|(
name|rif
operator|.
name|getIndexUri
argument_list|( )
operator|==
literal|null
condition|?
literal|""
else|:
name|rif
operator|.
name|getIndexUri
argument_list|()
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRemoteDownloadNetworkProxyId
argument_list|(
name|rif
operator|.
name|getProxyId
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
block|}
block|}
end_class

end_unit

