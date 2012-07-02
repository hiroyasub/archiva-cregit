begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|test
operator|.
name|utils
operator|.
name|ArchivaBlockJUnit4ClassRunner
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
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_comment
comment|/**  * RepositoryURLTest   *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|RepositoryURLTest
extends|extends
name|TestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NO_HOST
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NO_PORT
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NO_USER
init|=
literal|null
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NO_PASS
init|=
literal|null
decl_stmt|;
specifier|private
name|void
name|assertURL
parameter_list|(
name|String
name|url
parameter_list|,
name|String
name|expectedProtocol
parameter_list|,
name|String
name|expectedHost
parameter_list|,
name|String
name|expectedPort
parameter_list|,
name|String
name|expectedPath
parameter_list|,
name|String
name|expectedUsername
parameter_list|,
name|String
name|expectedPassword
parameter_list|)
block|{
name|RepositoryURL
name|rurl
init|=
operator|new
name|RepositoryURL
argument_list|(
name|url
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Protocol"
argument_list|,
name|expectedProtocol
argument_list|,
name|rurl
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Host"
argument_list|,
name|expectedHost
argument_list|,
name|rurl
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Port"
argument_list|,
name|expectedPort
argument_list|,
name|rurl
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Path"
argument_list|,
name|expectedPath
argument_list|,
name|rurl
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Username"
argument_list|,
name|expectedUsername
argument_list|,
name|rurl
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Password"
argument_list|,
name|expectedPassword
argument_list|,
name|rurl
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileUrlNormal
parameter_list|()
block|{
name|assertURL
argument_list|(
literal|"file:///home/joakim/code/test/this/"
argument_list|,
literal|"file"
argument_list|,
name|NO_HOST
argument_list|,
name|NO_PORT
argument_list|,
literal|"/home/joakim/code/test/this/"
argument_list|,
name|NO_USER
argument_list|,
name|NO_PASS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFileUrlShort
parameter_list|()
block|{
name|assertURL
argument_list|(
literal|"file:/home/joakim/code/test/this/"
argument_list|,
literal|"file"
argument_list|,
name|NO_HOST
argument_list|,
name|NO_PORT
argument_list|,
literal|"/home/joakim/code/test/this/"
argument_list|,
name|NO_USER
argument_list|,
name|NO_PASS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpUrlPathless
parameter_list|()
block|{
name|assertURL
argument_list|(
literal|"http://machine"
argument_list|,
literal|"http"
argument_list|,
literal|"machine"
argument_list|,
name|NO_PORT
argument_list|,
literal|"/"
argument_list|,
name|NO_USER
argument_list|,
name|NO_PASS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpUrlWithPort
parameter_list|()
block|{
name|assertURL
argument_list|(
literal|"http://machine:8080/"
argument_list|,
literal|"http"
argument_list|,
literal|"machine"
argument_list|,
literal|"8080"
argument_list|,
literal|"/"
argument_list|,
name|NO_USER
argument_list|,
name|NO_PASS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpUrlWithUsernamePassword
parameter_list|()
block|{
name|assertURL
argument_list|(
literal|"http://user:pass@machine/secured/"
argument_list|,
literal|"http"
argument_list|,
literal|"machine"
argument_list|,
name|NO_PORT
argument_list|,
literal|"/secured/"
argument_list|,
literal|"user"
argument_list|,
literal|"pass"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpUrlWithUsernameNoPassword
parameter_list|()
block|{
name|assertURL
argument_list|(
literal|"http://user@machine/secured/"
argument_list|,
literal|"http"
argument_list|,
literal|"machine"
argument_list|,
name|NO_PORT
argument_list|,
literal|"/secured/"
argument_list|,
literal|"user"
argument_list|,
name|NO_PASS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testHttpUrlWithUsernamePasswordAndPort
parameter_list|()
block|{
name|assertURL
argument_list|(
literal|"http://user:pass@machine:9090/secured/"
argument_list|,
literal|"http"
argument_list|,
literal|"machine"
argument_list|,
literal|"9090"
argument_list|,
literal|"/secured/"
argument_list|,
literal|"user"
argument_list|,
literal|"pass"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBogusWithPath
parameter_list|()
block|{
comment|// This should not fail.  The intent of RepositoryURL is to have it support oddball protocols that
comment|// are used by maven-scm and maven-wagon (unlike java.net.URL)
name|assertURL
argument_list|(
literal|"bogus://a.machine.name.com/path/to/resource/file.txt"
argument_list|,
literal|"bogus"
argument_list|,
literal|"a.machine.name.com"
argument_list|,
name|NO_PORT
argument_list|,
literal|"/path/to/resource/file.txt"
argument_list|,
name|NO_USER
argument_list|,
name|NO_PASS
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

