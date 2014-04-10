begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
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
name|AuditInformation
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
name|beans
operator|.
name|ProxyConnectorRule
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
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepositoryAdmin
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
name|admin
operator|.
name|model
operator|.
name|proxyconnector
operator|.
name|ProxyConnectorOrderComparator
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
name|remote
operator|.
name|RemoteRepositoryAdmin
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
name|index
operator|.
name|context
operator|.
name|IndexingContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|modelmapper
operator|.
name|ModelMapper
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
name|HashMap
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
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|Service
specifier|public
class|class
name|MockRepoAdmin
implements|implements
name|RemoteRepositoryAdmin
implements|,
name|ManagedRepositoryAdmin
implements|,
name|ProxyConnectorAdmin
implements|,
name|NetworkProxyAdmin
block|{
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaConfiguration#test"
argument_list|)
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|public
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|getRemoteRepositories
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|RemoteRepository
argument_list|>
name|remoteRepositories
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|RemoteRepositoryConfiguration
name|repositoryConfiguration
range|:
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRemoteRepositories
argument_list|()
control|)
block|{
name|RemoteRepository
name|remoteRepository
init|=
operator|new
name|RemoteRepository
argument_list|(
name|repositoryConfiguration
operator|.
name|getId
argument_list|()
argument_list|,
name|repositoryConfiguration
operator|.
name|getName
argument_list|()
argument_list|,
name|repositoryConfiguration
operator|.
name|getUrl
argument_list|()
argument_list|,
name|repositoryConfiguration
operator|.
name|getLayout
argument_list|()
argument_list|,
name|repositoryConfiguration
operator|.
name|getUsername
argument_list|()
argument_list|,
name|repositoryConfiguration
operator|.
name|getPassword
argument_list|()
argument_list|,
name|repositoryConfiguration
operator|.
name|getTimeout
argument_list|()
argument_list|)
decl_stmt|;
name|remoteRepository
operator|.
name|setDownloadRemoteIndex
argument_list|(
name|repositoryConfiguration
operator|.
name|isDownloadRemoteIndex
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setRemoteIndexUrl
argument_list|(
name|repositoryConfiguration
operator|.
name|getRemoteIndexUrl
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setCronExpression
argument_list|(
name|repositoryConfiguration
operator|.
name|getRefreshCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setIndexDirectory
argument_list|(
name|repositoryConfiguration
operator|.
name|getIndexDir
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setRemoteDownloadNetworkProxyId
argument_list|(
name|repositoryConfiguration
operator|.
name|getRemoteDownloadNetworkProxyId
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setRemoteDownloadTimeout
argument_list|(
name|repositoryConfiguration
operator|.
name|getRemoteDownloadTimeout
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setDownloadRemoteIndexOnStartup
argument_list|(
name|repositoryConfiguration
operator|.
name|isDownloadRemoteIndexOnStartup
argument_list|()
argument_list|)
expr_stmt|;
name|remoteRepositories
operator|.
name|add
argument_list|(
name|remoteRepository
argument_list|)
expr_stmt|;
block|}
return|return
name|remoteRepositories
return|;
block|}
specifier|public
name|RemoteRepository
name|getRemoteRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
for|for
control|(
name|RemoteRepository
name|remoteRepository
range|:
name|getRemoteRepositories
argument_list|()
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|repositoryId
argument_list|,
name|remoteRepository
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|remoteRepository
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|deleteRemoteRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Boolean
name|addRemoteRepository
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Boolean
name|updateRemoteRepository
parameter_list|(
name|RemoteRepository
name|remoteRepository
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|RemoteRepository
argument_list|>
name|getRemoteRepositoriesAsMap
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|IndexingContext
name|createIndexContext
parameter_list|(
name|RemoteRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|getManagedRepositories
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepository
argument_list|>
name|getManagedRepositoriesAsMap
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|ManagedRepository
name|getManagedRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
for|for
control|(
name|ManagedRepositoryConfiguration
name|repoConfig
range|:
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getManagedRepositories
argument_list|()
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|repositoryId
argument_list|,
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
operator|new
name|ManagedRepository
argument_list|(
name|repoConfig
operator|.
name|getId
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getName
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getLocation
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getLayout
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isSnapshots
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isReleases
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isBlockRedeployments
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getRefreshCronExpression
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getIndexDir
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isScanned
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getDaysOlder
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|repoConfig
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|,
literal|false
argument_list|)
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|public
name|Boolean
name|deleteManagedRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|,
name|boolean
name|deleteContent
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Boolean
name|addManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|,
name|boolean
name|needStageRepo
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Boolean
name|updateManagedRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|,
name|boolean
name|needStageRepo
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|,
name|boolean
name|resetStats
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|IndexingContext
name|createIndexContext
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|getProxyConnectors
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|List
argument_list|<
name|ProxyConnectorConfiguration
argument_list|>
name|proxyConnectorConfigurations
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getProxyConnectors
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|proxyConnectors
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|proxyConnectorConfigurations
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ProxyConnectorConfiguration
name|configuration
range|:
name|proxyConnectorConfigurations
control|)
block|{
name|proxyConnectors
operator|.
name|add
argument_list|(
name|getProxyConnector
argument_list|(
name|configuration
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|Collections
operator|.
name|sort
argument_list|(
name|proxyConnectors
argument_list|,
name|ProxyConnectorOrderComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|proxyConnectors
return|;
block|}
specifier|public
name|ProxyConnector
name|getProxyConnector
parameter_list|(
name|String
name|sourceRepoId
parameter_list|,
name|String
name|targetRepoId
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Boolean
name|addProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Boolean
name|deleteProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|Boolean
name|updateProxyConnector
parameter_list|(
name|ProxyConnector
name|proxyConnector
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|getProxyConnectorAsMap
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|proxyConnectorMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|Iterator
argument_list|<
name|ProxyConnector
argument_list|>
name|it
init|=
name|getProxyConnectors
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ProxyConnector
name|proxyConfig
init|=
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|String
name|key
init|=
name|proxyConfig
operator|.
name|getSourceRepoId
argument_list|()
decl_stmt|;
name|List
argument_list|<
name|ProxyConnector
argument_list|>
name|connectors
init|=
name|proxyConnectorMap
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|connectors
operator|==
literal|null
condition|)
block|{
name|connectors
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|proxyConnectorMap
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|connectors
argument_list|)
expr_stmt|;
block|}
name|connectors
operator|.
name|add
argument_list|(
name|proxyConfig
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|connectors
argument_list|,
name|ProxyConnectorOrderComparator
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|proxyConnectorMap
return|;
block|}
specifier|public
name|List
argument_list|<
name|NetworkProxy
argument_list|>
name|getNetworkProxies
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|NetworkProxy
name|getNetworkProxy
parameter_list|(
name|String
name|networkProxyId
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|addNetworkProxy
parameter_list|(
name|NetworkProxy
name|networkProxy
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|updateNetworkProxy
parameter_list|(
name|NetworkProxy
name|networkProxy
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|public
name|void
name|deleteNetworkProxy
parameter_list|(
name|String
name|networkProxyId
parameter_list|,
name|AuditInformation
name|auditInformation
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
comment|//To change body of implemented methods use File | Settings | File Templates.
block|}
specifier|protected
name|ProxyConnector
name|getProxyConnector
parameter_list|(
name|ProxyConnectorConfiguration
name|proxyConnectorConfiguration
parameter_list|)
block|{
return|return
name|proxyConnectorConfiguration
operator|==
literal|null
condition|?
literal|null
else|:
operator|new
name|ModelMapper
argument_list|()
operator|.
name|map
argument_list|(
name|proxyConnectorConfiguration
argument_list|,
name|ProxyConnector
operator|.
name|class
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|ProxyConnectorRule
argument_list|>
name|getProxyConnectorRules
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|addProxyConnectorRule
parameter_list|(
name|ProxyConnectorRule
name|proxyConnectorRule
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
block|}
specifier|public
name|void
name|deleteProxyConnectorRule
parameter_list|(
name|ProxyConnectorRule
name|proxyConnectorRule
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
block|}
specifier|public
name|void
name|updateProxyConnectorRule
parameter_list|(
name|ProxyConnectorRule
name|proxyConnectorRule
parameter_list|)
throws|throws
name|RepositoryAdminException
block|{
block|}
block|}
end_class

end_unit

