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
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
name|RemoteRepositoryConfiguration
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

begin_import
import|import
name|javax
operator|.
name|servlet
operator|.
name|ServletContext
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
name|servlet
operator|.
name|http
operator|.
name|HttpSession
import|;
end_import

begin_comment
comment|/**  * AbstractRepositoryServletTestCase   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryServletTestCase
extends|extends
name|PlexusTestCase
block|{
specifier|protected
specifier|static
specifier|final
name|String
name|REPOID_INTERNAL
init|=
literal|"internal"
decl_stmt|;
specifier|protected
name|ServletUnitClient
name|sc
decl_stmt|;
specifier|protected
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|protected
name|File
name|repoRootInternal
decl_stmt|;
specifier|private
name|ServletRunner
name|sr
decl_stmt|;
specifier|protected
name|void
name|assertFileContents
parameter_list|(
name|String
name|expectedContents
parameter_list|,
name|File
name|repoRoot
parameter_list|,
name|String
name|path
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|actualFile
init|=
operator|new
name|File
argument_list|(
name|repoRoot
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"File<"
operator|+
name|actualFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"> should exist."
argument_list|,
name|actualFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"File<"
operator|+
name|actualFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"> should be a file (not a dir/link/device/etc)."
argument_list|,
name|actualFile
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|actualContents
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|actualFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"File Contents of<"
operator|+
name|actualFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|">"
argument_list|,
name|expectedContents
argument_list|,
name|actualContents
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertRepositoryValid
parameter_list|(
name|RepositoryServlet
name|servlet
parameter_list|,
name|String
name|repoId
parameter_list|)
block|{
name|ManagedRepositoryConfiguration
name|repository
init|=
name|servlet
operator|.
name|getRepository
argument_list|(
name|repoId
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Archiva Managed Repository id:<"
operator|+
name|repoId
operator|+
literal|"> should exist."
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|File
name|repoRoot
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Archiva Managed Repository id:<"
operator|+
name|repoId
operator|+
literal|"> should have a valid location on disk."
argument_list|,
name|repoRoot
operator|.
name|exists
argument_list|()
operator|&&
name|repoRoot
operator|.
name|isDirectory
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertResponseOK
parameter_list|(
name|WebResponse
name|response
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Should have recieved a response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have been an OK response code."
argument_list|,
name|HttpServletResponse
operator|.
name|SC_OK
argument_list|,
name|response
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertResponseNotFound
parameter_list|(
name|WebResponse
name|response
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Should have recieved a response"
argument_list|,
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Should have been an 404/Not Found response code."
argument_list|,
name|HttpServletResponse
operator|.
name|SC_NOT_FOUND
argument_list|,
name|response
operator|.
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|ManagedRepositoryConfiguration
name|createManagedRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|File
name|location
parameter_list|)
block|{
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
name|id
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|location
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
specifier|protected
name|RemoteRepositoryConfiguration
name|createRemoteRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|RemoteRepositoryConfiguration
name|repo
init|=
operator|new
name|RemoteRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
specifier|protected
name|void
name|dumpResponse
parameter_list|(
name|WebResponse
name|response
parameter_list|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"---(response)---"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|""
operator|+
name|response
operator|.
name|getResponseCode
argument_list|()
operator|+
literal|" "
operator|+
name|response
operator|.
name|getResponseMessage
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|headerNames
index|[]
init|=
name|response
operator|.
name|getHeaderFieldNames
argument_list|()
decl_stmt|;
for|for
control|(
name|String
name|headerName
range|:
name|headerNames
control|)
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"[header] "
operator|+
name|headerName
operator|+
literal|": "
operator|+
name|response
operator|.
name|getHeaderField
argument_list|(
name|headerName
argument_list|)
argument_list|)
expr_stmt|;
block|}
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"---(text)---"
argument_list|)
expr_stmt|;
try|try
block|{
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|print
argument_list|(
literal|"[Exception] : "
argument_list|)
expr_stmt|;
name|e
operator|.
name|printStackTrace
argument_list|(
name|System
operator|.
name|err
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|saveConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
name|File
name|testConf
init|=
name|getTestFile
argument_list|(
literal|"src/test/resources/repository-archiva.xml"
argument_list|)
decl_stmt|;
name|File
name|testConfDest
init|=
operator|new
name|File
argument_list|(
name|appserverBase
argument_list|,
literal|"conf/archiva.xml"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|testConf
argument_list|,
name|testConfDest
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
expr_stmt|;
name|repoRootInternal
operator|=
operator|new
name|File
argument_list|(
name|appserverBase
argument_list|,
literal|"data/repositories/internal"
argument_list|)
expr_stmt|;
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|config
operator|.
name|addManagedRepository
argument_list|(
name|createManagedRepository
argument_list|(
name|REPOID_INTERNAL
argument_list|,
literal|"Internal Test Repo"
argument_list|,
name|repoRootInternal
argument_list|)
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
name|sr
operator|=
operator|new
name|ServletRunner
argument_list|()
expr_stmt|;
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
name|HttpSession
name|session
init|=
name|sc
operator|.
name|getSession
argument_list|(
literal|true
argument_list|)
decl_stmt|;
name|ServletContext
name|servletContext
init|=
name|session
operator|.
name|getServletContext
argument_list|()
decl_stmt|;
name|servletContext
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
annotation|@
name|Override
specifier|protected
name|String
name|getConfigurationName
parameter_list|(
name|String
name|subname
parameter_list|)
throws|throws
name|Exception
block|{
return|return
literal|"org/apache/maven/archiva/web/repository/RepositoryServletTest.xml"
return|;
block|}
annotation|@
name|Override
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|release
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
if|if
condition|(
name|sc
operator|!=
literal|null
condition|)
block|{
name|sc
operator|.
name|clearContents
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|sr
operator|!=
literal|null
condition|)
block|{
name|sr
operator|.
name|shutDown
argument_list|()
expr_stmt|;
block|}
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|setupCleanRepo
parameter_list|(
name|File
name|repoRootDir
parameter_list|)
throws|throws
name|IOException
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|repoRootDir
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|repoRootDir
operator|.
name|exists
argument_list|()
condition|)
block|{
name|repoRootDir
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|assertManagedFileNotExists
parameter_list|(
name|File
name|repoRootInternal
parameter_list|,
name|String
name|resourcePath
parameter_list|)
block|{
name|File
name|repoFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|resourcePath
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Managed Repository File<"
operator|+
name|repoFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"> should not exist."
argument_list|,
name|repoFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|setupCleanInternalRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|setupCleanRepo
argument_list|(
name|repoRootInternal
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|File
name|populateRepo
parameter_list|(
name|File
name|repoRootManaged
parameter_list|,
name|String
name|path
parameter_list|,
name|String
name|contents
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|repoRootManaged
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|destFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|destFile
argument_list|,
name|contents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
return|return
name|destFile
return|;
block|}
block|}
end_class

end_unit

