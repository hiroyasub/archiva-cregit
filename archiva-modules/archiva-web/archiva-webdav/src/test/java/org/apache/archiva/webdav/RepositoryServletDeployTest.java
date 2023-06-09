begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|gargoylesoftware
operator|.
name|htmlunit
operator|.
name|WebResponse
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
name|model
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
name|webdav
operator|.
name|mock
operator|.
name|httpunit
operator|.
name|MkColMethodWebRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|After
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
name|Test
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
name|java
operator|.
name|io
operator|.
name|InputStream
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

begin_comment
comment|/**  * Deploy / Put Test cases for RepositoryServlet.    *  *  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletDeployTest
extends|extends
name|AbstractRepositoryServletTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|ARTIFACT_DEFAULT_LAYOUT
init|=
literal|"/path/to/artifact/1.0.0/artifact-1.0.0.jar"
decl_stmt|;
annotation|@
name|Before
annotation|@
name|Override
specifier|public
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
name|startRepository
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
annotation|@
name|After
specifier|public
name|void
name|tearDown
parameter_list|( )
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|( )
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPutWithMissingParentCollection
parameter_list|()
throws|throws
name|Exception
block|{
name|setupCleanRepo
argument_list|(
name|repoRootInternal
argument_list|)
expr_stmt|;
name|String
name|putUrl
init|=
literal|"http://machine.com/repository/internal"
operator|+
name|ARTIFACT_DEFAULT_LAYOUT
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
decl_stmt|;
comment|// verify that the file exists in resources-dir
name|assertNotNull
argument_list|(
literal|"artifact.jar inputstream"
argument_list|,
name|is
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertFileContents
argument_list|(
literal|"artifact.jar\n"
argument_list|,
name|repoRootInternal
argument_list|,
name|ARTIFACT_DEFAULT_LAYOUT
argument_list|)
expr_stmt|;
block|}
comment|/**      * MRM-747      * test whether trying to overwrite existing relase-artifact is blocked by returning HTTP-code 409       *       * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testReleaseArtifactsRedeploymentValidPath
parameter_list|()
throws|throws
name|Exception
block|{
name|setupCleanRepo
argument_list|(
name|repoRootInternal
argument_list|)
expr_stmt|;
name|String
name|putUrl
init|=
literal|"http://machine.com/repository/internal"
operator|+
name|ARTIFACT_DEFAULT_LAYOUT
decl_stmt|;
name|String
name|metadataUrl
init|=
literal|"http://machine.com/repository/internal/path/to/artifact/maven-metadata.xml"
decl_stmt|;
name|String
name|checksumUrl
init|=
literal|"http://machine.com/repository/internal"
operator|+
name|ARTIFACT_DEFAULT_LAYOUT
operator|+
literal|".sha1"
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
decl_stmt|;
comment|// verify that the file exists in resources-dir
name|assertNotNull
argument_list|(
literal|"artifact.jar inputstream"
argument_list|,
name|is
argument_list|)
expr_stmt|;
comment|// send request #1 and verify it's successful
name|WebRequest
name|request
init|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar.sha1"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|checksumUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|metadataUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// send request #2 and verify it's blocked
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseConflictError
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReleaseArtifactsRedeploymentIsAllowed
parameter_list|()
throws|throws
name|Exception
block|{
name|setupCleanRepo
argument_list|(
name|repoRootInternal
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|managedRepo
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|findManagedRepositoryById
argument_list|(
name|REPOID_INTERNAL
argument_list|)
decl_stmt|;
name|managedRepo
operator|.
name|setBlockRedeployments
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
name|String
name|putUrl
init|=
literal|"http://machine.com/repository/internal"
operator|+
name|ARTIFACT_DEFAULT_LAYOUT
decl_stmt|;
name|String
name|metadataUrl
init|=
literal|"http://machine.com/repository/internal/path/to/artifact/maven-metadata.xml"
decl_stmt|;
name|String
name|checksumUrl
init|=
literal|"http://machine.com/repository/internal"
operator|+
name|ARTIFACT_DEFAULT_LAYOUT
operator|+
literal|".sha1"
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
decl_stmt|;
comment|// verify that the file exists in resources-dir
name|assertNotNull
argument_list|(
literal|"artifact.jar inputstream"
argument_list|,
name|is
argument_list|)
expr_stmt|;
comment|// send request #1 and verify it's successful
name|WebRequest
name|request
init|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar.sha1"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|checksumUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|metadataUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// send request #2 and verify if it's still successful
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseNoContent
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReleaseArtifactsRedeploymentInvalidPath
parameter_list|()
throws|throws
name|Exception
block|{
name|setupCleanRepo
argument_list|(
name|repoRootInternal
argument_list|)
expr_stmt|;
name|String
name|putUrl
init|=
literal|"http://machine.com/repository/internal/artifact.jar"
decl_stmt|;
name|String
name|metadataUrl
init|=
literal|"http://machine.com/repository/internal/maven-metadata.xml"
decl_stmt|;
name|String
name|checksumUrl
init|=
literal|"http://machine.com/repository/internal/artifact.jar.sha1"
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
decl_stmt|;
comment|// verify that the file exists in resources-dir
name|assertNotNull
argument_list|(
literal|"artifact.jar inputstream"
argument_list|,
name|is
argument_list|)
expr_stmt|;
comment|// send request #1 and verify it's successful
name|WebRequest
name|request
init|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar.sha1"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|checksumUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|metadataUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// send request #2 and verify it's re-deployed
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseNoContent
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testReleaseArtifactsRedeploymentArtifactIsSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|setupCleanRepo
argument_list|(
name|repoRootInternal
argument_list|)
expr_stmt|;
name|String
name|putUrl
init|=
literal|"http://machine.com/repository/internal/path/to/artifact/1.0-SNAPSHOT/artifact-1.0-SNAPSHOT.jar"
decl_stmt|;
name|String
name|metadataUrl
init|=
literal|"http://machine.com/repository/internal/path/to/artifact/maven-metadata.xml"
decl_stmt|;
name|String
name|checksumUrl
init|=
literal|"http://machine.com/repository/internal/path/to/artifact/1.0-SNAPSHOT/artifact-1.0-SNAPSHOT.jar.sha1"
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
decl_stmt|;
comment|// verify that the file exists in resources-dir
name|assertNotNull
argument_list|(
literal|"artifact.jar inputstream"
argument_list|,
name|is
argument_list|)
expr_stmt|;
comment|// send request #1 and verify it's successful
name|WebRequest
name|request
init|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar.sha1"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|checksumUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|metadataUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// send request #2 and verify it's re-deployed
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseNoContent
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMkColWithMissingParentCollectionFails
parameter_list|()
throws|throws
name|Exception
block|{
name|setupCleanRepo
argument_list|(
name|repoRootInternal
argument_list|)
expr_stmt|;
name|String
name|putUrl
init|=
literal|"http://machine.com/repository/internal/path/to/"
decl_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|MkColMethodWebRequest
argument_list|(
name|putUrl
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|HttpServletResponse
operator|.
name|SC_CONFLICT
argument_list|,
name|response
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
name|Path
name|mkColLocalPath
init|=
name|repoRootInternal
operator|.
name|resolve
argument_list|(
literal|"path/to/"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|mkColLocalPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArtifactsDeploymentArtifactIsSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|setupCleanRepo
argument_list|(
name|repoRootInternal
argument_list|)
expr_stmt|;
name|String
name|putUrl
init|=
literal|"http://machine.com/repository/internal/path/to/artifact/SNAPSHOT/artifact-SNAPSHOT.jar"
decl_stmt|;
name|String
name|metadataUrl
init|=
literal|"http://machine.com/repository/internal/path/to/artifact/maven-metadata.xml"
decl_stmt|;
name|String
name|checksumUrl
init|=
literal|"http://machine.com/repository/internal/path/to/artifact/SNAPSHOT/artifact-SNAPSHOT.jar.sha1"
decl_stmt|;
name|InputStream
name|is
init|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
decl_stmt|;
comment|// verify that the file exists in resources-dir
name|assertNotNull
argument_list|(
literal|"artifact.jar inputstream"
argument_list|,
name|is
argument_list|)
expr_stmt|;
comment|// send request #1 and verify it's successful
name|WebRequest
name|request
init|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar.sha1"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|checksumUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/maven-metadata.xml"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|metadataUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseCreated
argument_list|(
name|response
argument_list|)
expr_stmt|;
comment|// send request #2 and verify it's re-deployed
name|is
operator|=
name|getClass
argument_list|()
operator|.
name|getResourceAsStream
argument_list|(
literal|"/artifact.jar"
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|PutMethodWebRequest
argument_list|(
name|putUrl
argument_list|,
name|is
argument_list|,
literal|"application/octet-stream"
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseNoContent
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|request
operator|=
operator|new
name|GetMethodWebRequest
argument_list|(
name|putUrl
argument_list|)
expr_stmt|;
name|response
operator|=
name|getServletUnitClient
argument_list|()
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertResponseNoContent
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
literal|"Should have been a 204/NO CONTENT response code."
argument_list|,
name|HttpServletResponse
operator|.
name|SC_NO_CONTENT
argument_list|,
name|response
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertResponseCreated
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
literal|"Should have been a 201/CREATED response code."
argument_list|,
name|HttpServletResponse
operator|.
name|SC_CREATED
argument_list|,
name|response
operator|.
name|getStatusCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

