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
name|GetMethodWebRequest
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
name|WebLink
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
name|javax
operator|.
name|servlet
operator|.
name|http
operator|.
name|HttpServletResponse
import|;
end_import

begin_comment
comment|/**  * RepositoryServletBrowseTest   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletBrowseTest
extends|extends
name|AbstractRepositoryServletTestCase
block|{
annotation|@
name|Override
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
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
literal|"org/apache/archiva"
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
literal|"org/codehaus/mojo/"
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
literal|"net/sourceforge"
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
literal|"commons-lang"
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testBrowse
parameter_list|()
throws|throws
name|Exception
block|{
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
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
literal|"Response"
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
comment|// dumpResponse( response );
name|String
name|expectedLinks
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"commons-lang/"
block|,
literal|"net/"
block|,
literal|"org/"
block|}
decl_stmt|;
name|assertLinks
argument_list|(
name|expectedLinks
argument_list|,
name|response
operator|.
name|getLinks
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testBrowseSubdirectory
parameter_list|()
throws|throws
name|Exception
block|{
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/org"
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
literal|"Response"
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
name|String
name|expectedLinks
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"../"
block|,
literal|"apache/"
block|,
literal|"codehaus/"
block|}
decl_stmt|;
name|assertLinks
argument_list|(
name|expectedLinks
argument_list|,
name|response
operator|.
name|getLinks
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetDirectoryWhichHasMatchingFile
parameter_list|()
comment|//MRM-893
throws|throws
name|Exception
block|{
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
literal|"org/apache/archiva/artifactId/1.0"
argument_list|)
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
literal|"org/apache/archiva/artifactId/1.0/artifactId-1.0.jar"
argument_list|)
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/org/apache/archiva/artifactId"
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
literal|"Response"
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
name|request
operator|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/org/apache/archiva/artifactId/"
argument_list|)
expr_stmt|;
name|response
operator|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Response"
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
name|request
operator|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/org/apache/archiva/artifactId/1.0/artifactId-1.0.jar"
argument_list|)
expr_stmt|;
name|response
operator|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Response"
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
name|request
operator|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/org/apache/archiva/artifactId/1.0/artifactId-1.0.jar/"
argument_list|)
expr_stmt|;
name|response
operator|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Response"
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
specifier|private
name|void
name|assertLinks
parameter_list|(
name|String
name|expectedLinks
index|[]
parameter_list|,
name|WebLink
name|actualLinks
index|[]
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Links.length"
argument_list|,
name|expectedLinks
operator|.
name|length
argument_list|,
name|actualLinks
operator|.
name|length
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|actualLinks
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|assertEquals
argument_list|(
literal|"Link["
operator|+
name|i
operator|+
literal|"]"
argument_list|,
name|expectedLinks
index|[
name|i
index|]
argument_list|,
name|actualLinks
index|[
name|i
index|]
operator|.
name|getURLString
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

