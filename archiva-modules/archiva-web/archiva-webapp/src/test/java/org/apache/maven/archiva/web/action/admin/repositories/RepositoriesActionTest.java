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
name|meterware
operator|.
name|servletunit
operator|.
name|ServletRunner
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|servletunit
operator|.
name|ServletUnitClient
import|;
end_import

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
name|admin
operator|.
name|repository
operator|.
name|remote
operator|.
name|DefaultRemoteRepositoryAdmin
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
name|RepositorySession
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
name|memory
operator|.
name|TestRepositorySessionFactory
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
name|RepositoryStatistics
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|struts2
operator|.
name|StrutsSpringTestCase
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|mock
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|when
import|;
end_import

begin_comment
comment|/**  * Test the repositories action returns the correct data.  */
end_comment

begin_class
specifier|public
class|class
name|RepositoriesActionTest
extends|extends
name|StrutsSpringTestCase
block|{
specifier|private
name|RepositoriesAction
name|action
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
operator|(
name|RepositoriesAction
operator|)
name|getActionProxy
argument_list|(
literal|"/admin/index.action"
argument_list|)
operator|.
name|getAction
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
specifier|protected
name|String
index|[]
name|getContextLocations
parameter_list|()
block|{
return|return
operator|new
name|String
index|[]
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath*:/spring-context.xml"
block|}
return|;
block|}
specifier|public
name|void
name|testGetRepositories
parameter_list|()
throws|throws
name|Exception
block|{
name|MockControl
name|control
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
name|control
operator|.
name|getMock
argument_list|()
decl_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|metadataRepository
operator|.
name|getMetadataFacets
argument_list|(
literal|"internal"
argument_list|,
name|RepositoryStatistics
operator|.
name|FACET_ID
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"20091125.123456.678"
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|metadataRepository
operator|.
name|getMetadataFacet
argument_list|(
literal|"internal"
argument_list|,
name|RepositoryStatistics
operator|.
name|FACET_ID
argument_list|,
literal|"20091125.123456.678"
argument_list|)
argument_list|,
operator|new
name|RepositoryStatistics
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|metadataRepository
operator|.
name|getMetadataFacets
argument_list|(
literal|"snapshots"
argument_list|,
name|RepositoryStatistics
operator|.
name|FACET_ID
argument_list|)
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"20091112.012345.012"
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|expectAndReturn
argument_list|(
name|metadataRepository
operator|.
name|getMetadataFacet
argument_list|(
literal|"snapshots"
argument_list|,
name|RepositoryStatistics
operator|.
name|FACET_ID
argument_list|,
literal|"20091112.012345.012"
argument_list|)
argument_list|,
operator|new
name|RepositoryStatistics
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|RepositorySession
name|session
init|=
name|mock
argument_list|(
name|RepositorySession
operator|.
name|class
argument_list|)
decl_stmt|;
name|when
argument_list|(
name|session
operator|.
name|getRepository
argument_list|()
argument_list|)
operator|.
name|thenReturn
argument_list|(
name|metadataRepository
argument_list|)
expr_stmt|;
name|TestRepositorySessionFactory
name|factory
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"repositorySessionFactory#test"
argument_list|,
name|TestRepositorySessionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|factory
operator|.
name|setRepositorySession
argument_list|(
name|session
argument_list|)
expr_stmt|;
name|ServletRunner
name|sr
init|=
operator|new
name|ServletRunner
argument_list|()
decl_stmt|;
name|ServletUnitClient
name|sc
init|=
name|sr
operator|.
name|newClient
argument_list|()
decl_stmt|;
name|action
operator|.
name|setServletRequest
argument_list|(
name|sc
operator|.
name|newInvocation
argument_list|(
literal|"http://localhost/admin/repositories.action"
argument_list|)
operator|.
name|getRequest
argument_list|()
argument_list|)
expr_stmt|;
name|action
operator|.
name|prepare
argument_list|()
expr_stmt|;
name|String
name|result
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
name|result
argument_list|)
expr_stmt|;
comment|// TODO: for some reason servletunit is not populating the port of the servlet request
name|assertEquals
argument_list|(
literal|"http://localhost:0/repository"
argument_list|,
name|action
operator|.
name|getBaseUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
operator|.
name|getManagedRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|action
operator|.
name|getRepositoryStatistics
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|action
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|action
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|action
operator|.
name|getRepositoryStatistics
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
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
block|}
end_class

end_unit

