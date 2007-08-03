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
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|model
operator|.
name|RepositoryURL
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|MalformedURLException
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * RepositoryURLTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryURLTest
extends|extends
name|TestCase
block|{
specifier|private
name|void
name|assertURL
parameter_list|(
name|String
name|actualURL
parameter_list|,
name|String
name|protocol
parameter_list|,
name|String
name|username
parameter_list|,
name|String
name|password
parameter_list|,
name|String
name|hostname
parameter_list|,
name|String
name|port
parameter_list|,
name|String
name|path
parameter_list|)
block|{
name|RepositoryURL
name|url
init|=
operator|new
name|RepositoryURL
argument_list|(
name|actualURL
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|protocol
argument_list|,
name|url
operator|.
name|getProtocol
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|username
argument_list|,
name|url
operator|.
name|getUsername
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|password
argument_list|,
name|url
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|hostname
argument_list|,
name|url
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|port
argument_list|,
name|url
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|path
argument_list|,
name|url
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProtocolHttp
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|assertURL
argument_list|(
literal|"http://localhost/path/to/resource.txt"
argument_list|,
literal|"http"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"localhost"
argument_list|,
literal|null
argument_list|,
literal|"/path/to/resource.txt"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProtocolWagonWebdav
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|assertURL
argument_list|(
literal|"dav:http://localhost/path/to/resource.txt"
argument_list|,
literal|"dav:http"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"localhost"
argument_list|,
literal|null
argument_list|,
literal|"/path/to/resource.txt"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProtocolHttpWithPort
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|assertURL
argument_list|(
literal|"http://localhost:9090/path/to/resource.txt"
argument_list|,
literal|"http"
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|,
literal|"localhost"
argument_list|,
literal|"9090"
argument_list|,
literal|"/path/to/resource.txt"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProtocolHttpWithUsername
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|assertURL
argument_list|(
literal|"http://user@localhost/path/to/resource.txt"
argument_list|,
literal|"http"
argument_list|,
literal|"user"
argument_list|,
literal|null
argument_list|,
literal|"localhost"
argument_list|,
literal|null
argument_list|,
literal|"/path/to/resource.txt"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProtocolHttpWithUsernamePassword
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|assertURL
argument_list|(
literal|"http://user:pass@localhost/path/to/resource.txt"
argument_list|,
literal|"http"
argument_list|,
literal|"user"
argument_list|,
literal|"pass"
argument_list|,
literal|"localhost"
argument_list|,
literal|null
argument_list|,
literal|"/path/to/resource.txt"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testProtocolHttpWithUsernamePasswordPort
parameter_list|()
throws|throws
name|MalformedURLException
block|{
name|assertURL
argument_list|(
literal|"http://user:pass@localhost:9090/path/to/resource.txt"
argument_list|,
literal|"http"
argument_list|,
literal|"user"
argument_list|,
literal|"pass"
argument_list|,
literal|"localhost"
argument_list|,
literal|"9090"
argument_list|,
literal|"/path/to/resource.txt"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

