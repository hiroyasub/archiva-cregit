begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|mock
operator|.
name|MockAuditListener
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
name|proxyconnectorrule
operator|.
name|ProxyConnectorRuleAdmin
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
name|redback
operator|.
name|role
operator|.
name|RoleManager
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
name|redback
operator|.
name|users
operator|.
name|memory
operator|.
name|SimpleUser
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
name|test
operator|.
name|utils
operator|.
name|ArchivaSpringJUnit4ClassRunner
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
name|junit
operator|.
name|Before
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|BeforeClass
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
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
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
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
name|Locale
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
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryAdminTest
extends|extends
name|TestCase
block|{
specifier|protected
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
specifier|public
specifier|static
specifier|final
name|String
name|APPSERVER_BASE_PATH
init|=
name|AbstractRepositoryAdminTest
operator|.
name|fixPath
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"appserver.base"
argument_list|)
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|MockAuditListener
name|mockAuditListener
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|RoleManager
name|roleManager
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|RemoteRepositoryAdmin
name|remoteRepositoryAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ManagedRepositoryAdmin
name|managedRepositoryAdmin
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
name|ProxyConnectorRuleAdmin
name|proxyConnectorRuleAdmin
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|Path
name|confFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|APPSERVER_BASE_PATH
argument_list|,
literal|"conf/archiva.xml"
argument_list|)
decl_stmt|;
try|try
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|confFile
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|protected
name|AuditInformation
name|getFakeAuditInformation
parameter_list|()
block|{
name|AuditInformation
name|auditInformation
init|=
operator|new
name|AuditInformation
argument_list|(
name|getFakeUser
argument_list|()
argument_list|,
literal|"archiva-localhost"
argument_list|)
decl_stmt|;
return|return
name|auditInformation
return|;
block|}
comment|// make a nice repo path to allow unit test to run
specifier|private
specifier|static
name|String
name|fixPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|String
name|SPACE
init|=
literal|" "
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|contains
argument_list|(
name|SPACE
argument_list|)
condition|)
block|{
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractRepositoryAdminTest
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
operator|.
name|error
argument_list|(
literal|"You are building and testing  with {appserver.base}: \n {}"
operator|+
literal|" containing space. Consider relocating."
argument_list|,
name|path
argument_list|)
expr_stmt|;
block|}
return|return
name|path
operator|.
name|replaceAll
argument_list|(
name|SPACE
argument_list|,
literal|"&amp;20"
argument_list|)
return|;
block|}
specifier|protected
name|User
name|getFakeUser
parameter_list|()
block|{
name|SimpleUser
name|user
init|=
operator|new
name|SimpleUser
argument_list|()
decl_stmt|;
name|user
operator|.
name|setUsername
argument_list|(
literal|"root"
argument_list|)
expr_stmt|;
name|user
operator|.
name|setFullName
argument_list|(
literal|"The top user"
argument_list|)
expr_stmt|;
return|return
name|user
return|;
block|}
specifier|protected
name|ManagedRepository
name|getTestManagedRepository
parameter_list|(
name|String
name|repoId
parameter_list|,
name|String
name|repoLocation
parameter_list|)
block|{
name|String
name|repoLocationStr
init|=
name|Paths
operator|.
name|get
argument_list|(
name|repoLocation
argument_list|,
literal|".index"
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
return|return
operator|new
name|ManagedRepository
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|( )
argument_list|,
name|repoId
argument_list|,
literal|"test repo"
argument_list|,
name|repoLocation
argument_list|,
literal|"default"
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
name|repoLocationStr
argument_list|,
literal|false
argument_list|,
literal|1
argument_list|,
literal|2
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|protected
name|Path
name|clearRepoLocation
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|Exception
block|{
name|Path
name|repoDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|repoDir
argument_list|)
condition|)
block|{
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
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repoDir
argument_list|)
expr_stmt|;
block|}
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|repoDir
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|repoDir
return|;
block|}
specifier|protected
name|ManagedRepository
name|findManagedRepoById
parameter_list|(
name|List
argument_list|<
name|ManagedRepository
argument_list|>
name|repos
parameter_list|,
name|String
name|id
parameter_list|)
block|{
for|for
control|(
name|ManagedRepository
name|repo
range|:
name|repos
control|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|equals
argument_list|(
name|id
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
return|return
name|repo
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
specifier|protected
name|RemoteRepository
name|getRemoteRepository
parameter_list|()
block|{
return|return
name|getRemoteRepository
argument_list|(
literal|"foo"
argument_list|)
return|;
block|}
specifier|protected
name|RemoteRepository
name|getRemoteRepository
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|RemoteRepository
name|remoteRepository
init|=
operator|new
name|RemoteRepository
argument_list|(
name|Locale
operator|.
name|getDefault
argument_list|()
argument_list|)
decl_stmt|;
name|remoteRepository
operator|.
name|setUrl
argument_list|(
literal|"http://foo.com/maven-it-rocks"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setTimeout
argument_list|(
literal|10
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setName
argument_list|(
literal|"maven foo"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setUserName
argument_list|(
literal|"foo-name"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setPassword
argument_list|(
literal|"toto"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setRemoteDownloadNetworkProxyId
argument_list|(
literal|"foo"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setDescription
argument_list|(
literal|"cool apache repo"
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|extraParameters
operator|.
name|put
argument_list|(
literal|"foo"
argument_list|,
literal|"bar"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setExtraParameters
argument_list|(
name|extraParameters
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|extraHeaders
operator|.
name|put
argument_list|(
literal|"beer"
argument_list|,
literal|"wine"
argument_list|)
expr_stmt|;
name|remoteRepository
operator|.
name|setExtraHeaders
argument_list|(
name|extraHeaders
argument_list|)
expr_stmt|;
return|return
name|remoteRepository
return|;
block|}
block|}
end_class

end_unit

