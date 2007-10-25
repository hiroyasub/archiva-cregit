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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * RepositoryServletTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletTest
extends|extends
name|AbstractRepositoryServletTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|REQUEST_PATH
init|=
literal|"http://machine.com/repository/internal/"
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
specifier|public
name|void
name|testGetRepository
parameter_list|()
throws|throws
name|Exception
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
name|assertRepositoryValid
argument_list|(
name|servlet
argument_list|,
name|REPOID_INTERNAL
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetRepositoryAfterDelete
parameter_list|()
throws|throws
name|Exception
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
name|archivaConfiguration
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
name|REPOID_INTERNAL
argument_list|)
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|servlet
operator|.
name|getRepository
argument_list|(
name|REPOID_INTERNAL
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
name|Exception
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
name|archivaConfiguration
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
name|File
name|repoRoot
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"target/test-repository-root"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|repoRoot
operator|.
name|exists
argument_list|()
condition|)
block|{
name|repoRoot
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|repo
operator|.
name|setLocation
argument_list|(
name|repoRoot
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|c
operator|.
name|addManagedRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
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
name|assertRepositoryValid
argument_list|(
name|servlet
argument_list|,
name|REPOID_INTERNAL
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

