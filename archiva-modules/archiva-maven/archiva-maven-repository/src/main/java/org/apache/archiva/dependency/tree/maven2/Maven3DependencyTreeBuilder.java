begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|dependency
operator|.
name|tree
operator|.
name|maven2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|RepositoryAdminException
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|NetworkProxy
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ProxyConnector
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
name|admin
operator|.
name|model
operator|.
name|networkproxy
operator|.
name|NetworkProxyAdmin
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
name|admin
operator|.
name|model
operator|.
name|proxyconnector
operator|.
name|ProxyConnectorAdmin
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridgeException
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
name|archiva
operator|.
name|maven2
operator|.
name|metadata
operator|.
name|MavenMetadataReader
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
name|maven2
operator|.
name|model
operator|.
name|TreeEntry
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
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|RepositoryPathTranslator
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
name|model
operator|.
name|ArchivaRepositoryMetadata
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
name|RepositoryRegistry
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
name|maven2
operator|.
name|MavenSystemManager
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
name|metadata
operator|.
name|MetadataTools
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
name|storage
operator|.
name|StorageAsset
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
name|xml
operator|.
name|XMLException
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
name|lang3
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
name|bridge
operator|.
name|MavenRepositorySystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|RepositorySystem
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|RepositorySystemSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|artifact
operator|.
name|DefaultArtifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|collection
operator|.
name|CollectRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|collection
operator|.
name|CollectResult
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|collection
operator|.
name|DependencyCollectionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|graph
operator|.
name|Dependency
import|;
end_import

begin_import
import|import
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|graph
operator|.
name|DependencyVisitor
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
name|annotation
operator|.
name|PostConstruct
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
name|javax
operator|.
name|inject
operator|.
name|Named
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

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"dependencyTreeBuilder#maven3"
argument_list|)
specifier|public
class|class
name|Maven3DependencyTreeBuilder
implements|implements
name|DependencyTreeBuilder
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|Maven3DependencyTreeBuilder
operator|.
name|class
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
specifier|private
name|MavenRepositorySystem
name|mavenRepositorySystem
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
literal|"repositoryPathTranslator#maven2"
argument_list|)
specifier|private
name|RepositoryPathTranslator
name|pathTranslator
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ProxyConnectorAdmin
name|proxyConnectorAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|NetworkProxyAdmin
name|networkProxyAdmin
decl_stmt|;
annotation|@
name|Inject
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|Inject
name|MavenSystemManager
name|mavenSystemManager
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|PlexusSisuBridgeException
block|{
name|mavenRepositorySystem
operator|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|MavenRepositorySystem
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|buildDependencyTree
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|,
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|DependencyVisitor
name|dependencyVisitor
parameter_list|)
throws|throws
name|DependencyTreeBuilderException
block|{
name|Artifact
name|projectArtifact
init|=
name|mavenRepositorySystem
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
name|ManagedRepository
name|repository
init|=
name|findArtifactInRepositories
argument_list|(
name|repositoryIds
argument_list|,
name|projectArtifact
argument_list|)
decl_stmt|;
if|if
condition|(
name|repository
operator|==
literal|null
condition|)
block|{
comment|// metadata could not be resolved
name|log
operator|.
name|info
argument_list|(
literal|"Did not find repository with artifact {}/{}/{}"
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
expr_stmt|;
return|return;
block|}
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|RemoteRepository
argument_list|>
name|remoteRepositories
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|networkProxies
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
try|try
block|{
comment|// MRM-1411
comment|// TODO: this is a workaround for a lack of proxy capability in the resolvers - replace when it can all be
comment|//       handled there. It doesn't cache anything locally!
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|proxyConnectorsMap
init|=
name|proxyConnectorAdmin
operator|.
name|getProxyConnectorAsMap
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|proxyConnectors
init|=
name|proxyConnectorsMap
operator|.
name|get
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|proxyConnectors
operator|!=
literal|null
condition|)
block|{
for|for
control|(
name|ProxyConnector
name|proxyConnector
range|:
name|proxyConnectors
control|)
block|{
name|remoteRepositories
operator|.
name|add
argument_list|(
name|repositoryRegistry
operator|.
name|getRemoteRepository
argument_list|(
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|NetworkProxy
name|networkProxyConfig
init|=
name|networkProxyAdmin
operator|.
name|getNetworkProxy
argument_list|(
name|proxyConnector
operator|.
name|getProxyId
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|networkProxyConfig
operator|!=
literal|null
condition|)
block|{
comment|// key/value: remote repo ID/proxy info
name|networkProxies
operator|.
name|put
argument_list|(
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|,
name|networkProxyConfig
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|DependencyTreeBuilderException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
comment|// FIXME take care of relative path
name|ResolveRequest
name|resolveRequest
init|=
operator|new
name|ResolveRequest
argument_list|()
decl_stmt|;
name|resolveRequest
operator|.
name|dependencyVisitor
operator|=
name|dependencyVisitor
expr_stmt|;
name|resolveRequest
operator|.
name|localRepoDir
operator|=
name|repository
operator|.
name|getContent
argument_list|()
operator|.
name|getRepoRoot
argument_list|()
expr_stmt|;
name|resolveRequest
operator|.
name|groupId
operator|=
name|groupId
expr_stmt|;
name|resolveRequest
operator|.
name|artifactId
operator|=
name|artifactId
expr_stmt|;
name|resolveRequest
operator|.
name|version
operator|=
name|version
expr_stmt|;
name|resolveRequest
operator|.
name|remoteRepositories
operator|=
name|remoteRepositories
expr_stmt|;
name|resolveRequest
operator|.
name|networkProxies
operator|=
name|networkProxies
expr_stmt|;
name|resolve
argument_list|(
name|resolveRequest
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|TreeEntry
argument_list|>
name|buildDependencyTree
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|,
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
name|DependencyTreeBuilderException
block|{
name|List
argument_list|<
name|TreeEntry
argument_list|>
name|treeEntries
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|TreeDependencyNodeVisitor
name|treeDependencyNodeVisitor
init|=
operator|new
name|TreeDependencyNodeVisitor
argument_list|(
name|treeEntries
argument_list|)
decl_stmt|;
name|buildDependencyTree
argument_list|(
name|repositoryIds
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|treeDependencyNodeVisitor
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"treeEntries: {}"
argument_list|,
name|treeEntries
argument_list|)
expr_stmt|;
return|return
name|treeEntries
return|;
block|}
specifier|private
specifier|static
class|class
name|ResolveRequest
block|{
name|String
name|localRepoDir
decl_stmt|,
name|groupId
decl_stmt|,
name|artifactId
decl_stmt|,
name|version
decl_stmt|;
name|DependencyVisitor
name|dependencyVisitor
decl_stmt|;
name|List
argument_list|<
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|RemoteRepository
argument_list|>
name|remoteRepositories
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|NetworkProxy
argument_list|>
name|networkProxies
decl_stmt|;
block|}
specifier|private
name|void
name|resolve
parameter_list|(
name|ResolveRequest
name|resolveRequest
parameter_list|)
block|{
name|RepositorySystem
name|system
init|=
name|mavenSystemManager
operator|.
name|getRepositorySystem
argument_list|()
decl_stmt|;
name|RepositorySystemSession
name|session
init|=
name|MavenSystemManager
operator|.
name|newRepositorySystemSession
argument_list|(
name|resolveRequest
operator|.
name|localRepoDir
argument_list|)
decl_stmt|;
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|artifact
operator|.
name|Artifact
name|artifact
init|=
operator|new
name|DefaultArtifact
argument_list|(
name|resolveRequest
operator|.
name|groupId
operator|+
literal|":"
operator|+
name|resolveRequest
operator|.
name|artifactId
operator|+
literal|":"
operator|+
name|resolveRequest
operator|.
name|version
argument_list|)
decl_stmt|;
name|CollectRequest
name|collectRequest
init|=
operator|new
name|CollectRequest
argument_list|()
decl_stmt|;
name|collectRequest
operator|.
name|setRoot
argument_list|(
operator|new
name|Dependency
argument_list|(
name|artifact
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
comment|// add remote repositories
for|for
control|(
name|RemoteRepository
name|remoteRepository
range|:
name|resolveRequest
operator|.
name|remoteRepositories
control|)
block|{
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|repository
operator|.
name|RemoteRepository
name|repo
init|=
operator|new
name|org
operator|.
name|eclipse
operator|.
name|aether
operator|.
name|repository
operator|.
name|RemoteRepository
operator|.
name|Builder
argument_list|(
name|remoteRepository
operator|.
name|getId
argument_list|( )
argument_list|,
literal|"default"
argument_list|,
name|remoteRepository
operator|.
name|getLocation
argument_list|( )
operator|.
name|toString
argument_list|()
argument_list|)
operator|.
name|build
argument_list|( )
decl_stmt|;
name|collectRequest
operator|.
name|addRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
block|}
name|collectRequest
operator|.
name|setRequestContext
argument_list|(
literal|"project"
argument_list|)
expr_stmt|;
comment|//collectRequest.addRepository( repo );
try|try
block|{
name|CollectResult
name|collectResult
init|=
name|system
operator|.
name|collectDependencies
argument_list|(
name|session
argument_list|,
name|collectRequest
argument_list|)
decl_stmt|;
name|collectResult
operator|.
name|getRoot
argument_list|()
operator|.
name|accept
argument_list|(
name|resolveRequest
operator|.
name|dependencyVisitor
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Collected dependency results for resolve"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DependencyCollectionException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Error while collecting dependencies (resolve): {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|ManagedRepository
name|findArtifactInRepositories
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositoryIds
parameter_list|,
name|Artifact
name|projectArtifact
parameter_list|)
block|{
for|for
control|(
name|String
name|repoId
range|:
name|repositoryIds
control|)
block|{
name|ManagedRepository
name|managedRepo
init|=
name|repositoryRegistry
operator|.
name|getManagedRepository
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
name|StorageAsset
name|repoDir
init|=
name|managedRepo
operator|.
name|getAsset
argument_list|(
literal|""
argument_list|)
decl_stmt|;
name|StorageAsset
name|file
init|=
name|pathTranslator
operator|.
name|toFile
argument_list|(
name|repoDir
argument_list|,
name|projectArtifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|projectArtifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|projectArtifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|,
name|projectArtifact
operator|.
name|getArtifactId
argument_list|()
operator|+
literal|"-"
operator|+
name|projectArtifact
operator|.
name|getVersion
argument_list|()
operator|+
literal|".pom"
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|managedRepo
return|;
block|}
comment|// try with snapshot version
if|if
condition|(
name|StringUtils
operator|.
name|endsWith
argument_list|(
name|projectArtifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|,
name|VersionUtil
operator|.
name|SNAPSHOT
argument_list|)
condition|)
block|{
name|StorageAsset
name|metadataFile
init|=
name|file
operator|.
name|getParent
argument_list|()
operator|.
name|resolve
argument_list|(
name|MetadataTools
operator|.
name|MAVEN_METADATA
argument_list|)
decl_stmt|;
if|if
condition|(
name|metadataFile
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|ArchivaRepositoryMetadata
name|archivaRepositoryMetadata
init|=
name|MavenMetadataReader
operator|.
name|read
argument_list|(
name|metadataFile
argument_list|)
decl_stmt|;
name|int
name|buildNumber
init|=
name|archivaRepositoryMetadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|.
name|getBuildNumber
argument_list|()
decl_stmt|;
name|String
name|timeStamp
init|=
name|archivaRepositoryMetadata
operator|.
name|getSnapshotVersion
argument_list|()
operator|.
name|getTimestamp
argument_list|()
decl_stmt|;
comment|// rebuild file name with timestamped version and build number
name|String
name|timeStampFileName
init|=
operator|new
name|StringBuilder
argument_list|(
name|projectArtifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|remove
argument_list|(
name|projectArtifact
operator|.
name|getBaseVersion
argument_list|()
argument_list|,
literal|"-"
operator|+
name|VersionUtil
operator|.
name|SNAPSHOT
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
operator|.
name|append
argument_list|(
name|timeStamp
argument_list|)
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
operator|.
name|append
argument_list|(
name|Integer
operator|.
name|toString
argument_list|(
name|buildNumber
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|".pom"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|StorageAsset
name|timeStampFile
init|=
name|file
operator|.
name|getParent
argument_list|()
operator|.
name|resolve
argument_list|(
name|timeStampFileName
argument_list|)
decl_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"try to find timestamped snapshot version file: {}"
argument_list|,
name|timeStampFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|timeStampFile
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|managedRepo
return|;
block|}
block|}
catch|catch
parameter_list|(
name|XMLException
decl||
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"skip fail to find timestamped snapshot pom: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

