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
name|web
operator|.
name|action
operator|.
name|admin
operator|.
name|repositories
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork2
operator|.
name|Action
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
name|audit
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
name|metadata
operator|.
name|repository
operator|.
name|MetadataRepository
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
name|MetadataRepositoryException
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
name|stats
operator|.
name|RepositoryStatisticsManager
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
name|IndeterminateConfigurationException
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
name|configuration
operator|.
name|ProxyConnectorConfiguration
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
name|RemoteRepositoryConfiguration
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
name|RepositoryGroupConfiguration
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
name|security
operator|.
name|ArchivaRoleConstants
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
name|web
operator|.
name|action
operator|.
name|AuditEventArgumentsMatcher
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
name|codehaus
operator|.
name|plexus
operator|.
name|redback
operator|.
name|role
operator|.
name|RoleManagerException
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
name|RegistryException
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
name|spring
operator|.
name|PlexusInSpringTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|integration
operator|.
name|interceptor
operator|.
name|SecureActionBundle
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|redback
operator|.
name|integration
operator|.
name|interceptor
operator|.
name|SecureActionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
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
name|Arrays
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
name|List
import|;
end_import

begin_comment
comment|/**  * DeleteManagedRepositoryActionTest  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DeleteManagedRepositoryActionTest
extends|extends
name|PlexusInSpringTestCase
block|{
specifier|private
name|DeleteManagedRepositoryAction
name|action
decl_stmt|;
specifier|private
name|RoleManager
name|roleManager
decl_stmt|;
specifier|private
name|MockControl
name|roleManagerControl
decl_stmt|;
specifier|private
name|MockControl
name|archivaConfigurationControl
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPO_ID
init|=
literal|"repo-ident"
decl_stmt|;
specifier|private
name|File
name|location
decl_stmt|;
specifier|private
name|MockControl
name|repositoryStatisticsManagerControl
decl_stmt|;
specifier|private
name|RepositoryStatisticsManager
name|repositoryStatisticsManager
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|action
operator|=
operator|new
name|DeleteManagedRepositoryAction
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|archivaConfigurationControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|action
operator|.
name|setArchivaConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|RoleManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|roleManager
operator|=
operator|(
name|RoleManager
operator|)
name|roleManagerControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRoleManager
argument_list|(
name|roleManager
argument_list|)
expr_stmt|;
name|location
operator|=
name|getTestFile
argument_list|(
literal|"target/test/location"
argument_list|)
expr_stmt|;
name|repositoryStatisticsManagerControl
operator|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|RepositoryStatisticsManager
operator|.
name|class
argument_list|)
expr_stmt|;
name|repositoryStatisticsManager
operator|=
operator|(
name|RepositoryStatisticsManager
operator|)
name|repositoryStatisticsManagerControl
operator|.
name|getMock
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRepositoryStatisticsManager
argument_list|(
name|repositoryStatisticsManager
argument_list|)
expr_stmt|;
name|MockControl
name|metadataRepositoryControl
init|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|MetadataRepository
operator|.
name|class
argument_list|)
decl_stmt|;
name|MetadataRepository
name|metadataRepository
init|=
operator|(
name|MetadataRepository
operator|)
name|metadataRepositoryControl
operator|.
name|getMock
argument_list|()
decl_stmt|;
name|metadataRepository
operator|.
name|deleteRepository
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|setMetadataRepository
argument_list|(
name|metadataRepository
argument_list|)
expr_stmt|;
name|metadataRepositoryControl
operator|.
name|replay
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSecureActionBundle
parameter_list|()
throws|throws
name|SecureActionException
block|{
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
operator|new
name|Configuration
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|SecureActionBundle
name|bundle
init|=
name|action
operator|.
name|getSecureActionBundle
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|bundle
operator|.
name|requiresAuthentication
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|bundle
operator|.
name|getAuthorizationTuples
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDeleteRepositoryConfirmation
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryConfiguration
name|originalRepository
init|=
name|createRepository
argument_list|()
decl_stmt|;
name|Configuration
name|configuration
init|=
name|createConfigurationForEditing
argument_list|(
name|originalRepository
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|Configuration
name|stageRepoConfiguration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|stageRepoConfiguration
operator|.
name|addManagedRepository
argument_list|(
name|createSatingRepository
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|stageRepoConfiguration
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRepoid
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO_ID
argument_list|,
name|action
operator|.
name|getRepoid
argument_list|()
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repository
argument_list|,
name|createRepository
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|execute
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|repository
operator|=
name|action
operator|.
name|getRepository
argument_list|()
expr_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repository
argument_list|,
name|createRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|originalRepository
argument_list|)
argument_list|,
name|configuration
operator|.
name|getManagedRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testDeleteRepositoryKeepContent
parameter_list|()
throws|throws
name|Exception
block|{
comment|// even when we keep the content, we don't keep the metadata at this point
name|repositoryStatisticsManager
operator|.
name|deleteStatistics
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|repositoryStatisticsManagerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|prepareRoleManagerMock
argument_list|()
expr_stmt|;
name|Configuration
name|configuration
init|=
name|prepDeletionTest
argument_list|(
name|createRepository
argument_list|()
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|MockControl
name|control
init|=
name|mockAuditListeners
argument_list|()
decl_stmt|;
name|MockControl
name|metadataRepositoryControl
init|=
name|mockMetadataRepository
argument_list|()
decl_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|deleteEntry
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|location
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryStatisticsManagerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|metadataRepositoryControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|MockControl
name|mockMetadataRepository
parameter_list|()
throws|throws
name|MetadataRepositoryException
block|{
name|MockControl
name|metadataRepositoryControl
init|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|MetadataRepository
operator|.
name|class
argument_list|)
decl_stmt|;
name|MetadataRepository
name|metadataRepository
init|=
operator|(
name|MetadataRepository
operator|)
name|metadataRepositoryControl
operator|.
name|getMock
argument_list|()
decl_stmt|;
name|metadataRepository
operator|.
name|deleteRepository
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|metadataRepositoryControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setMetadataRepository
argument_list|(
name|metadataRepository
argument_list|)
expr_stmt|;
return|return
name|metadataRepositoryControl
return|;
block|}
specifier|private
name|MockControl
name|mockAuditListeners
parameter_list|()
block|{
name|MockControl
name|control
init|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|AuditListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|AuditListener
name|listener
init|=
operator|(
name|AuditListener
operator|)
name|control
operator|.
name|getMock
argument_list|()
decl_stmt|;
name|listener
operator|.
name|auditEvent
argument_list|(
operator|new
name|AuditEvent
argument_list|(
name|REPO_ID
argument_list|,
literal|"guest"
argument_list|,
literal|null
argument_list|,
name|AuditEvent
operator|.
name|DELETE_MANAGED_REPO
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|setMatcher
argument_list|(
operator|new
name|AuditEventArgumentsMatcher
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setAuditListeners
argument_list|(
name|Arrays
operator|.
name|asList
argument_list|(
name|listener
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|control
return|;
block|}
specifier|public
name|void
name|testDeleteRepositoryDeleteContent
parameter_list|()
throws|throws
name|Exception
block|{
name|repositoryStatisticsManager
operator|.
name|deleteStatistics
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|repositoryStatisticsManagerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|prepareRoleManagerMock
argument_list|()
expr_stmt|;
name|Configuration
name|configuration
init|=
name|prepDeletionTest
argument_list|(
name|createRepository
argument_list|()
argument_list|,
literal|4
argument_list|)
decl_stmt|;
name|MockControl
name|control
init|=
name|mockAuditListeners
argument_list|()
decl_stmt|;
name|MockControl
name|metadataRepositoryControl
init|=
name|mockMetadataRepository
argument_list|()
decl_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|deleteContents
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|location
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryStatisticsManagerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|metadataRepositoryControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testDeleteRepositoryAndAssociatedProxyConnectors
parameter_list|()
throws|throws
name|Exception
block|{
name|repositoryStatisticsManager
operator|.
name|deleteStatistics
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|repositoryStatisticsManagerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|Configuration
name|configuration
init|=
name|prepDeletionTest
argument_list|(
name|createRepository
argument_list|()
argument_list|,
literal|5
argument_list|)
decl_stmt|;
name|configuration
operator|.
name|addRemoteRepository
argument_list|(
name|createRemoteRepository
argument_list|(
literal|"codehaus"
argument_list|,
literal|"http://repository.codehaus.org"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addRemoteRepository
argument_list|(
name|createRemoteRepository
argument_list|(
literal|"java.net"
argument_list|,
literal|"http://dev.java.net/maven2"
argument_list|)
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addProxyConnector
argument_list|(
name|createProxyConnector
argument_list|(
name|REPO_ID
argument_list|,
literal|"codehaus"
argument_list|)
argument_list|)
expr_stmt|;
name|prepareRoleManagerMock
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|configuration
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|MockControl
name|control
init|=
name|mockAuditListeners
argument_list|()
decl_stmt|;
name|MockControl
name|metadataRepositoryControl
init|=
name|mockMetadataRepository
argument_list|()
decl_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|deleteContents
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|configuration
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|location
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryStatisticsManagerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
name|metadataRepositoryControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testDeleteRepositoryCancelled
parameter_list|()
throws|throws
name|Exception
block|{
name|repositoryStatisticsManagerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|ManagedRepositoryConfiguration
name|originalRepository
init|=
name|createRepository
argument_list|()
decl_stmt|;
name|Configuration
name|configuration
init|=
name|prepDeletionTest
argument_list|(
name|originalRepository
argument_list|,
literal|3
argument_list|)
decl_stmt|;
name|String
name|status
init|=
name|action
operator|.
name|execute
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
name|Action
operator|.
name|SUCCESS
argument_list|,
name|status
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repository
argument_list|,
name|createRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|Collections
operator|.
name|singletonList
argument_list|(
name|originalRepository
argument_list|)
argument_list|,
name|configuration
operator|.
name|getManagedRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|location
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|repositoryStatisticsManagerControl
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Configuration
name|prepDeletionTest
parameter_list|(
name|ManagedRepositoryConfiguration
name|originalRepository
parameter_list|,
name|int
name|expectCountGetConfig
parameter_list|)
throws|throws
name|RegistryException
throws|,
name|IndeterminateConfigurationException
block|{
name|location
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|Configuration
name|configuration
init|=
name|createConfigurationForEditing
argument_list|(
name|originalRepository
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|configuration
argument_list|,
name|expectCountGetConfig
argument_list|)
expr_stmt|;
name|Configuration
name|stageRepoConfiguration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|stageRepoConfiguration
operator|.
name|addManagedRepository
argument_list|(
name|createSatingRepository
argument_list|()
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|setReturnValue
argument_list|(
name|stageRepoConfiguration
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfigurationControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|action
operator|.
name|setRepoid
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
name|REPO_ID
argument_list|,
name|action
operator|.
name|getRepoid
argument_list|()
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|action
operator|.
name|getRepository
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertRepositoryEquals
argument_list|(
name|repository
argument_list|,
name|createRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|location
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
specifier|private
name|void
name|assertRepositoryEquals
parameter_list|(
name|ManagedRepositoryConfiguration
name|expectedRepository
parameter_list|,
name|ManagedRepositoryConfiguration
name|actualRepository
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getDaysOlder
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getDaysOlder
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getIndexDir
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getIndexDir
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getLayout
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getLocation
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getName
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getRefreshCronExpression
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getRefreshCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|getRetentionCount
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|getRetentionCount
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|isDeleteReleasedSnapshots
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|isScanned
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|isReleases
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|isReleases
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedRepository
operator|.
name|isSnapshots
argument_list|()
argument_list|,
name|actualRepository
operator|.
name|isSnapshots
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|Configuration
name|createConfigurationForEditing
parameter_list|(
name|ManagedRepositoryConfiguration
name|repositoryConfiguration
parameter_list|)
block|{
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|addManagedRepository
argument_list|(
name|repositoryConfiguration
argument_list|)
expr_stmt|;
return|return
name|configuration
return|;
block|}
specifier|private
name|ManagedRepositoryConfiguration
name|createRepository
parameter_list|()
block|{
name|ManagedRepositoryConfiguration
name|r
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|r
operator|.
name|setId
argument_list|(
name|REPO_ID
argument_list|)
expr_stmt|;
name|r
operator|.
name|setName
argument_list|(
literal|"repo name"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setLocation
argument_list|(
name|location
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setRefreshCronExpression
argument_list|(
literal|"* 0/5 * * * ?"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setDaysOlder
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|r
operator|.
name|setRetentionCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|r
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r
operator|.
name|setSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r
operator|.
name|setScanned
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|r
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
specifier|private
name|ManagedRepositoryConfiguration
name|createSatingRepository
parameter_list|()
block|{
name|ManagedRepositoryConfiguration
name|r
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|r
operator|.
name|setId
argument_list|(
name|REPO_ID
operator|+
literal|"-stage"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setName
argument_list|(
literal|"repo name"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setLocation
argument_list|(
name|location
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|r
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setRefreshCronExpression
argument_list|(
literal|"* 0/5 * * * ?"
argument_list|)
expr_stmt|;
name|r
operator|.
name|setDaysOlder
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|r
operator|.
name|setRetentionCount
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|r
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r
operator|.
name|setSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|r
operator|.
name|setScanned
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|r
operator|.
name|setDeleteReleasedSnapshots
argument_list|(
literal|false
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
specifier|private
name|RemoteRepositoryConfiguration
name|createRemoteRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|RemoteRepositoryConfiguration
name|r
init|=
operator|new
name|RemoteRepositoryConfiguration
argument_list|()
decl_stmt|;
name|r
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|r
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|r
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
return|return
name|r
return|;
block|}
specifier|private
name|ProxyConnectorConfiguration
name|createProxyConnector
parameter_list|(
name|String
name|managedRepoId
parameter_list|,
name|String
name|remoteRepoId
parameter_list|)
block|{
name|ProxyConnectorConfiguration
name|connector
init|=
operator|new
name|ProxyConnectorConfiguration
argument_list|()
decl_stmt|;
name|connector
operator|.
name|setSourceRepoId
argument_list|(
name|managedRepoId
argument_list|)
expr_stmt|;
name|connector
operator|.
name|setTargetRepoId
argument_list|(
name|remoteRepoId
argument_list|)
expr_stmt|;
return|return
name|connector
return|;
block|}
specifier|private
name|RepositoryGroupConfiguration
name|createRepoGroup
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repoIds
parameter_list|,
name|String
name|repoGroupId
parameter_list|)
block|{
name|RepositoryGroupConfiguration
name|repoGroup
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|()
decl_stmt|;
name|repoGroup
operator|.
name|setId
argument_list|(
name|repoGroupId
argument_list|)
expr_stmt|;
name|repoGroup
operator|.
name|setRepositories
argument_list|(
name|repoIds
argument_list|)
expr_stmt|;
return|return
name|repoGroup
return|;
block|}
specifier|private
name|void
name|prepareRoleManagerMock
parameter_list|()
throws|throws
name|RoleManagerException
block|{
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|REPO_ID
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|.
name|setReturnValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|roleManager
operator|.
name|removeTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_OBSERVER
argument_list|,
name|REPO_ID
argument_list|)
expr_stmt|;
name|roleManager
operator|.
name|templatedRoleExists
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|REPO_ID
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|.
name|setReturnValue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|roleManager
operator|.
name|removeTemplatedRole
argument_list|(
name|ArchivaRoleConstants
operator|.
name|TEMPLATE_REPOSITORY_MANAGER
argument_list|,
name|REPO_ID
argument_list|)
expr_stmt|;
name|roleManagerControl
operator|.
name|replay
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

