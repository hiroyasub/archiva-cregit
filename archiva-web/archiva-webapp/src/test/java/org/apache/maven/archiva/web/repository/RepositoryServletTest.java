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
name|repository
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
name|httpunit
operator|.
name|PutMethodWebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebResponse
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
name|ConfigurationEvent
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
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusConstants
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
name|PlexusTestCase
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
name|util
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|xml
operator|.
name|sax
operator|.
name|SAXException
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletException
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

begin_class
specifier|public
class|class
name|RepositoryServletTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ServletUnitClient
name|sc
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_PATH
init|=
literal|"http://localhost/repository/internal/path/to/artifact.jar"
decl_stmt|;
specifier|private
name|File
name|repositoryLocation
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|REPOSITORY_ID
init|=
literal|"internal"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NEW_REPOSITORY_ID
init|=
literal|"new-id"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NEW_REPOSITORY_NAME
init|=
literal|"New Repository"
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
comment|// TODO: purely to quiet logging - shouldn't be needed
name|String
name|appserverBase
init|=
name|getTestFile
argument_list|(
literal|"target/appserver-base"
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"appserver.base"
argument_list|,
name|appserverBase
argument_list|)
expr_stmt|;
name|configuration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|repositoryLocation
operator|=
operator|new
name|File
argument_list|(
name|appserverBase
argument_list|,
literal|"data/repositories/internal"
argument_list|)
expr_stmt|;
name|ServletRunner
name|sr
init|=
operator|new
name|ServletRunner
argument_list|()
decl_stmt|;
name|sr
operator|.
name|registerServlet
argument_list|(
literal|"/repository/*"
argument_list|,
name|UnauthenticatedRepositoryServlet
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|sc
operator|=
name|sr
operator|.
name|newClient
argument_list|()
expr_stmt|;
name|sc
operator|.
name|getSession
argument_list|(
literal|true
argument_list|)
operator|.
name|getServletContext
argument_list|()
operator|.
name|setAttribute
argument_list|(
name|PlexusConstants
operator|.
name|PLEXUS_KEY
argument_list|,
name|getContainer
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testPutWithMissingParentCollection
parameter_list|()
throws|throws
name|IOException
throws|,
name|SAXException
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repositoryLocation
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|REQUEST_PATH
argument_list|,
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"No response received"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"file contents"
argument_list|,
literal|"artifact.jar\n"
argument_list|,
name|FileUtils
operator|.
name|fileRead
argument_list|(
operator|new
name|File
argument_list|(
name|repositoryLocation
argument_list|,
literal|"path/to/artifact.jar"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetRepository
parameter_list|()
throws|throws
name|IOException
throws|,
name|ServletException
block|{
name|RepositoryServlet
name|servlet
init|=
operator|(
name|RepositoryServlet
operator|)
name|sc
operator|.
name|newInvocation
argument_list|(
name|REQUEST_PATH
argument_list|)
operator|.
name|getServlet
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|servlet
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|servlet
operator|.
name|getRepository
argument_list|(
name|REPOSITORY_ID
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Archiva Managed Internal Repository"
argument_list|,
name|repository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetRepositoryAfterDelete
parameter_list|()
throws|throws
name|IOException
throws|,
name|ServletException
throws|,
name|RegistryException
throws|,
name|IndeterminateConfigurationException
block|{
name|RepositoryServlet
name|servlet
init|=
operator|(
name|RepositoryServlet
operator|)
name|sc
operator|.
name|newInvocation
argument_list|(
name|REQUEST_PATH
argument_list|)
operator|.
name|getServlet
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|servlet
argument_list|)
expr_stmt|;
name|Configuration
name|c
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|c
operator|.
name|removeManagedRepository
argument_list|(
name|c
operator|.
name|findManagedRepositoryById
argument_list|(
name|REPOSITORY_ID
argument_list|)
argument_list|)
expr_stmt|;
comment|// TODO it would be better to use a mock configuration and "save" to more accurately reflect the calls made
name|servlet
operator|.
name|configurationEvent
argument_list|(
operator|new
name|ConfigurationEvent
argument_list|(
name|ConfigurationEvent
operator|.
name|SAVED
argument_list|)
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|servlet
operator|.
name|getRepository
argument_list|(
name|REPOSITORY_ID
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetRepositoryAfterAdd
parameter_list|()
throws|throws
name|IOException
throws|,
name|ServletException
throws|,
name|RegistryException
throws|,
name|IndeterminateConfigurationException
block|{
name|RepositoryServlet
name|servlet
init|=
operator|(
name|RepositoryServlet
operator|)
name|sc
operator|.
name|newInvocation
argument_list|(
name|REQUEST_PATH
argument_list|)
operator|.
name|getServlet
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|servlet
argument_list|)
expr_stmt|;
name|Configuration
name|c
init|=
name|configuration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|ManagedRepositoryConfiguration
name|repo
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|NEW_REPOSITORY_ID
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|NEW_REPOSITORY_NAME
argument_list|)
expr_stmt|;
name|c
operator|.
name|addManagedRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
comment|// TODO it would be better to use a mock configuration and "save" to more accurately reflect the calls made
name|servlet
operator|.
name|configurationEvent
argument_list|(
operator|new
name|ConfigurationEvent
argument_list|(
name|ConfigurationEvent
operator|.
name|SAVED
argument_list|)
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|servlet
operator|.
name|getRepository
argument_list|(
name|NEW_REPOSITORY_ID
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|NEW_REPOSITORY_NAME
argument_list|,
name|repository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// check other is still intact
name|repository
operator|=
name|servlet
operator|.
name|getRepository
argument_list|(
name|REPOSITORY_ID
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Archiva Managed Internal Repository"
argument_list|,
name|repository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

