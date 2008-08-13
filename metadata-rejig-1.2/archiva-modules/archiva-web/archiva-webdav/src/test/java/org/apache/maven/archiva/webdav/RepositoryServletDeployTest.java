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
name|InputStream
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|webdav
operator|.
name|httpunit
operator|.
name|MkColMethodWebRequest
import|;
end_import

begin_comment
comment|/**  * Deploy / Put Test cases for RepositoryServlet.    *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletDeployTest
extends|extends
name|AbstractRepositoryServletTestCase
block|{
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
literal|"http://machine.com/repository/internal/path/to/artifact.jar"
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
name|sc
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
literal|"path/to/artifact.jar"
argument_list|)
expr_stmt|;
block|}
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
name|sc
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
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|mkColLocalPath
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
literal|"path/to/"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|mkColLocalPath
operator|.
name|exists
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
name|getResponseCode
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

