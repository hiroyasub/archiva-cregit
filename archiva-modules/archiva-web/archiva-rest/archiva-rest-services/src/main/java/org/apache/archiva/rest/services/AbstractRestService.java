begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
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
name|admin
operator|.
name|ArchivaAdministration
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
name|indexer
operator|.
name|search
operator|.
name|SearchResultHit
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
name|Artifact
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
name|model
operator|.
name|ArtifactMetadata
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
name|model
operator|.
name|facets
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
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
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
name|redback
operator|.
name|components
operator|.
name|taskqueue
operator|.
name|TaskQueueException
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
name|redback
operator|.
name|configuration
operator|.
name|UserConfiguration
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
name|redback
operator|.
name|configuration
operator|.
name|UserConfigurationKeys
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
name|redback
operator|.
name|rest
operator|.
name|services
operator|.
name|RedbackAuthenticationThreadLocal
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
name|redback
operator|.
name|rest
operator|.
name|services
operator|.
name|RedbackRequestInformation
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
name|redback
operator|.
name|users
operator|.
name|User
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
name|ManagedRepositoryContent
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
name|metadata
operator|.
name|audit
operator|.
name|AuditListener
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
name|rest
operator|.
name|api
operator|.
name|services
operator|.
name|ArchivaRestServiceException
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
name|rest
operator|.
name|services
operator|.
name|utils
operator|.
name|ArtifactBuilder
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
name|scheduler
operator|.
name|repository
operator|.
name|model
operator|.
name|RepositoryArchivaTaskScheduler
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
name|scheduler
operator|.
name|repository
operator|.
name|model
operator|.
name|RepositoryTask
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
name|security
operator|.
name|AccessDeniedException
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
name|security
operator|.
name|ArchivaSecurityException
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
name|security
operator|.
name|PrincipalNotFoundException
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
name|security
operator|.
name|UserRepositories
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
name|modelmapper
operator|.
name|ModelMapper
import|;
end_import

begin_import
import|import
name|org
operator|.
name|modelmapper
operator|.
name|PropertyMap
import|;
end_import

begin_import
import|import
name|org
operator|.
name|modelmapper
operator|.
name|convention
operator|.
name|MatchingStrategies
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
name|context
operator|.
name|ApplicationContext
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletRequest
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Context
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|Response
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
name|Collection
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
comment|/**  * abstract class with common utilities methods  *  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRestService
block|{
specifier|protected
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|List
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|UserRepositories
name|userRepositories
decl_stmt|;
comment|/**      * FIXME: this could be multiple implementations and needs to be configured.      */
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositorySessionFactory"
argument_list|)
specifier|protected
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ArchivaAdministration
name|archivaAdministration
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ProxyConnectorAdmin
name|proxyConnectorAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaTaskScheduler#repository"
argument_list|)
specifier|protected
name|RepositoryArchivaTaskScheduler
name|repositoryTaskScheduler
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"userConfiguration#default"
argument_list|)
specifier|protected
name|UserConfiguration
name|config
decl_stmt|;
annotation|@
name|Context
specifier|protected
name|HttpServletRequest
name|httpServletRequest
decl_stmt|;
annotation|@
name|Context
specifier|protected
name|HttpServletResponse
name|httpServletResponse
decl_stmt|;
specifier|protected
name|AuditInformation
name|getAuditInformation
parameter_list|()
block|{
name|RedbackRequestInformation
name|redbackRequestInformation
init|=
name|RedbackAuthenticationThreadLocal
operator|.
name|get
argument_list|()
decl_stmt|;
name|User
name|user
init|=
name|redbackRequestInformation
operator|==
literal|null
condition|?
literal|null
else|:
name|redbackRequestInformation
operator|.
name|getUser
argument_list|()
decl_stmt|;
name|String
name|remoteAddr
init|=
name|redbackRequestInformation
operator|==
literal|null
condition|?
literal|null
else|:
name|redbackRequestInformation
operator|.
name|getRemoteAddr
argument_list|()
decl_stmt|;
return|return
operator|new
name|AuditInformation
argument_list|(
name|user
argument_list|,
name|remoteAddr
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|AuditListener
argument_list|>
name|getAuditListeners
parameter_list|()
block|{
return|return
name|auditListeners
return|;
block|}
specifier|public
name|void
name|setAuditListeners
parameter_list|(
name|List
argument_list|<
name|AuditListener
argument_list|>
name|auditListeners
parameter_list|)
block|{
name|this
operator|.
name|auditListeners
operator|=
name|auditListeners
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|String
argument_list|>
name|getObservableRepos
parameter_list|()
block|{
try|try
block|{
name|List
argument_list|<
name|String
argument_list|>
name|ids
init|=
name|userRepositories
operator|.
name|getObservableRepositoryIds
argument_list|(
name|getPrincipal
argument_list|()
argument_list|)
decl_stmt|;
return|return
name|ids
operator|==
literal|null
condition|?
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
else|:
name|ids
return|;
block|}
catch|catch
parameter_list|(
name|PrincipalNotFoundException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
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
name|AccessDeniedException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
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
name|ArchivaSecurityException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
specifier|protected
name|String
name|getPrincipal
parameter_list|()
block|{
name|RedbackRequestInformation
name|redbackRequestInformation
init|=
name|RedbackAuthenticationThreadLocal
operator|.
name|get
argument_list|()
decl_stmt|;
return|return
name|redbackRequestInformation
operator|==
literal|null
condition|?
name|config
operator|.
name|getString
argument_list|(
name|UserConfigurationKeys
operator|.
name|DEFAULT_GUEST
argument_list|)
else|:
operator|(
name|redbackRequestInformation
operator|.
name|getUser
argument_list|()
operator|==
literal|null
condition|?
name|config
operator|.
name|getString
argument_list|(
name|UserConfigurationKeys
operator|.
name|DEFAULT_GUEST
argument_list|)
else|:
name|redbackRequestInformation
operator|.
name|getUser
argument_list|()
operator|.
name|getUsername
argument_list|()
operator|)
return|;
block|}
specifier|protected
name|String
name|getBaseUrl
parameter_list|()
throws|throws
name|RepositoryAdminException
block|{
name|String
name|applicationUrl
init|=
name|archivaAdministration
operator|.
name|getUiConfiguration
argument_list|()
operator|.
name|getApplicationUrl
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|applicationUrl
argument_list|)
condition|)
block|{
return|return
name|applicationUrl
return|;
block|}
return|return
name|httpServletRequest
operator|.
name|getScheme
argument_list|()
operator|+
literal|"://"
operator|+
name|httpServletRequest
operator|.
name|getServerName
argument_list|()
operator|+
operator|(
name|httpServletRequest
operator|.
name|getServerPort
argument_list|()
operator|==
literal|80
condition|?
literal|""
else|:
literal|":"
operator|+
name|httpServletRequest
operator|.
name|getServerPort
argument_list|()
operator|)
operator|+
name|httpServletRequest
operator|.
name|getContextPath
argument_list|()
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|getBeansOfType
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|,
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
comment|//TODO do some caching here !!!
comment|// olamy : with plexus we get only roleHint
comment|// as per convention we named spring bean role#hint remove role# if exists
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|springBeans
init|=
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|clazz
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|beans
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(
name|springBeans
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|T
argument_list|>
name|entry
range|:
name|springBeans
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|String
name|key
init|=
name|StringUtils
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|'#'
argument_list|)
condition|?
name|StringUtils
operator|.
name|substringAfterLast
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
literal|"#"
argument_list|)
else|:
name|entry
operator|.
name|getKey
argument_list|()
decl_stmt|;
name|beans
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|beans
return|;
block|}
specifier|protected
name|void
name|triggerAuditEvent
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|String
name|filePath
parameter_list|,
name|String
name|action
parameter_list|)
block|{
name|AuditEvent
name|auditEvent
init|=
operator|new
name|AuditEvent
argument_list|(
name|repositoryId
argument_list|,
name|getPrincipal
argument_list|()
argument_list|,
name|filePath
argument_list|,
name|action
argument_list|)
decl_stmt|;
name|AuditInformation
name|auditInformation
init|=
name|getAuditInformation
argument_list|()
decl_stmt|;
name|auditEvent
operator|.
name|setUserId
argument_list|(
name|auditInformation
operator|.
name|getUser
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|auditInformation
operator|.
name|getUser
argument_list|()
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|auditEvent
operator|.
name|setRemoteIP
argument_list|(
name|auditInformation
operator|.
name|getRemoteAddr
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|AuditListener
name|auditListener
range|:
name|getAuditListeners
argument_list|()
control|)
block|{
name|auditListener
operator|.
name|auditEvent
argument_list|(
name|auditEvent
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * @param artifact      * @return      */
specifier|protected
name|String
name|getArtifactUrl
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
return|return
name|getArtifactUrl
argument_list|(
name|artifact
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|protected
name|String
name|getArtifactUrl
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
if|if
condition|(
name|httpServletRequest
operator|==
literal|null
condition|)
block|{
return|return
literal|null
return|;
block|}
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
name|getBaseUrl
argument_list|()
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"/repository"
argument_list|)
expr_stmt|;
comment|// when artifact come from a remote repository when have here the remote repo id
comment|// we must replace it with a valid managed one available for the user.
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
name|List
argument_list|<
name|String
argument_list|>
name|userRepos
init|=
name|userRepositories
operator|.
name|getObservableRepositoryIds
argument_list|(
name|getPrincipal
argument_list|()
argument_list|)
decl_stmt|;
comment|// is it a good one? if yes nothing to
comment|// if not search the repo who is proxy for this remote
if|if
condition|(
operator|!
name|userRepos
operator|.
name|contains
argument_list|(
name|artifact
operator|.
name|getContext
argument_list|()
argument_list|)
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ProxyConnector
argument_list|>
argument_list|>
name|entry
range|:
name|proxyConnectorAdmin
operator|.
name|getProxyConnectorAsMap
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
for|for
control|(
name|ProxyConnector
name|proxyConnector
range|:
name|entry
operator|.
name|getValue
argument_list|()
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
literal|"remote-"
operator|+
name|proxyConnector
operator|.
name|getTargetRepoId
argument_list|()
argument_list|,
name|artifact
operator|.
name|getContext
argument_list|()
argument_list|)
comment|//
operator|&&
name|userRepos
operator|.
name|contains
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
operator|.
name|getContext
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|replaceChars
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
literal|'.'
argument_list|,
literal|'/'
argument_list|)
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'/'
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
if|if
condition|(
name|VersionUtil
operator|.
name|isSnapshot
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'/'
argument_list|)
operator|.
name|append
argument_list|(
name|VersionUtil
operator|.
name|getBaseVersion
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'/'
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
block|}
name|sb
operator|.
name|append
argument_list|(
literal|'/'
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
name|sb
operator|.
name|append
argument_list|(
literal|'-'
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
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'-'
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|// maven-plugin packaging is a jar
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
literal|"maven-plugin"
argument_list|,
name|artifact
operator|.
name|getPackaging
argument_list|()
argument_list|)
condition|)
block|{
name|sb
operator|.
name|append
argument_list|(
literal|"jar"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|sb
operator|.
name|append
argument_list|(
literal|'.'
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
operator|.
name|getFileExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|Response
operator|.
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|List
argument_list|<
name|Artifact
argument_list|>
name|buildArtifacts
parameter_list|(
name|Collection
argument_list|<
name|ArtifactMetadata
argument_list|>
name|artifactMetadatas
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ArchivaRestServiceException
block|{
try|try
block|{
if|if
condition|(
name|artifactMetadatas
operator|!=
literal|null
operator|&&
operator|!
name|artifactMetadatas
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|List
argument_list|<
name|Artifact
argument_list|>
name|artifacts
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|artifactMetadatas
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|ArtifactMetadata
name|artifact
range|:
name|artifactMetadatas
control|)
block|{
name|String
name|repoId
init|=
name|repositoryId
operator|!=
literal|null
condition|?
name|repositoryId
else|:
name|artifact
operator|.
name|getRepositoryId
argument_list|()
decl_stmt|;
if|if
condition|(
name|repoId
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|IllegalStateException
argument_list|(
literal|"Repository Id is null"
argument_list|)
throw|;
block|}
name|ManagedRepository
name|repo
init|=
name|repositoryRegistry
operator|.
name|getManagedRepository
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
if|if
condition|(
name|repo
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|RepositoryException
argument_list|(
literal|"Repository not found "
operator|+
name|repoId
argument_list|)
throw|;
block|}
name|ManagedRepositoryContent
name|content
init|=
name|repo
operator|.
name|getContent
argument_list|( )
decl_stmt|;
name|ArtifactBuilder
name|builder
init|=
operator|new
name|ArtifactBuilder
argument_list|()
operator|.
name|forArtifactMetadata
argument_list|(
name|artifact
argument_list|)
operator|.
name|withManagedRepositoryContent
argument_list|(
name|content
argument_list|)
decl_stmt|;
name|Artifact
name|art
init|=
name|builder
operator|.
name|build
argument_list|()
decl_stmt|;
name|art
operator|.
name|setUrl
argument_list|(
name|getArtifactUrl
argument_list|(
name|art
argument_list|,
name|repositoryId
argument_list|)
argument_list|)
expr_stmt|;
name|artifacts
operator|.
name|add
argument_list|(
name|art
argument_list|)
expr_stmt|;
block|}
return|return
name|artifacts
return|;
block|}
return|return
name|Collections
operator|.
name|emptyList
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
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
name|ArchivaRestServiceException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|Response
operator|.
name|Status
operator|.
name|INTERNAL_SERVER_ERROR
operator|.
name|getStatusCode
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|Boolean
name|doScanRepository
parameter_list|(
name|String
name|repositoryId
parameter_list|,
name|boolean
name|fullScan
parameter_list|)
block|{
if|if
condition|(
name|repositoryTaskScheduler
operator|.
name|isProcessingRepositoryTask
argument_list|(
name|repositoryId
argument_list|)
condition|)
block|{
name|log
operator|.
name|info
argument_list|(
literal|"scanning of repository with id {} already scheduled"
argument_list|,
name|repositoryId
argument_list|)
expr_stmt|;
return|return
name|Boolean
operator|.
name|FALSE
return|;
block|}
name|RepositoryTask
name|task
init|=
operator|new
name|RepositoryTask
argument_list|()
decl_stmt|;
name|task
operator|.
name|setRepositoryId
argument_list|(
name|repositoryId
argument_list|)
expr_stmt|;
name|task
operator|.
name|setScanAll
argument_list|(
name|fullScan
argument_list|)
expr_stmt|;
try|try
block|{
name|repositoryTaskScheduler
operator|.
name|queueTask
argument_list|(
name|task
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|TaskQueueException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"failed to schedule scanning of repo with id {}"
argument_list|,
name|repositoryId
argument_list|,
name|e
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
specifier|static
class|class
name|ModelMapperHolder
block|{
specifier|private
specifier|static
name|ModelMapper
name|MODEL_MAPPER
init|=
operator|new
name|ModelMapper
argument_list|()
decl_stmt|;
static|static
block|{
name|MODEL_MAPPER
operator|.
name|addMappings
argument_list|(
operator|new
name|SearchResultHitMap
argument_list|()
argument_list|)
expr_stmt|;
name|MODEL_MAPPER
operator|.
name|getConfiguration
argument_list|()
operator|.
name|setMatchingStrategy
argument_list|(
name|MatchingStrategies
operator|.
name|STRICT
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
class|class
name|SearchResultHitMap
extends|extends
name|PropertyMap
argument_list|<
name|SearchResultHit
argument_list|,
name|Artifact
argument_list|>
block|{
annotation|@
name|Override
specifier|protected
name|void
name|configure
parameter_list|()
block|{
name|skip
argument_list|()
operator|.
name|setId
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
empty_stmt|;
specifier|protected
name|ModelMapper
name|getModelMapper
parameter_list|()
block|{
return|return
name|ModelMapperHolder
operator|.
name|MODEL_MAPPER
return|;
block|}
block|}
end_class

end_unit

