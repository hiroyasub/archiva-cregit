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
name|database
operator|.
name|jdo
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
name|database
operator|.
name|AbstractArchivaDatabaseTestCase
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
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|RepositoryDAO
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
name|model
operator|.
name|ArchivaRepository
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

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|JDOHelper
import|;
end_import

begin_comment
comment|/**  * JdoRepositoryDAOTest   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|JdoRepositoryDAOTest
extends|extends
name|AbstractArchivaDatabaseTestCase
block|{
specifier|public
name|void
name|testRepositoryCRUD
parameter_list|()
throws|throws
name|ArchivaDatabaseException
block|{
name|RepositoryDAO
name|repoDao
init|=
name|dao
operator|.
name|getRepositoryDAO
argument_list|()
decl_stmt|;
comment|// Create it
name|ArchivaRepository
name|repo
init|=
name|repoDao
operator|.
name|createRepository
argument_list|(
literal|"testRepo"
argument_list|,
literal|"Test Repository"
argument_list|,
literal|"http://localhost:8080/repository/foo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
comment|// Set some mandatory values
name|repo
operator|.
name|getModel
argument_list|()
operator|.
name|setCreationSource
argument_list|(
literal|"Test Case"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|getModel
argument_list|()
operator|.
name|setLayoutName
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
comment|// Save it.
name|ArchivaRepository
name|repoSaved
init|=
name|repoDao
operator|.
name|saveRepository
argument_list|(
name|repo
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repoSaved
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repoSaved
operator|.
name|getModel
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testRepo"
argument_list|,
name|JDOHelper
operator|.
name|getObjectId
argument_list|(
name|repoSaved
operator|.
name|getModel
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that something has been saved.
name|List
name|repos
init|=
name|repoDao
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repos
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that retreived object is what we expect.
name|ArchivaRepository
name|firstRepo
init|=
operator|(
name|ArchivaRepository
operator|)
name|repos
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|firstRepo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testRepo"
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test Repository"
argument_list|,
name|repo
operator|.
name|getModel
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test Case"
argument_list|,
name|repo
operator|.
name|getModel
argument_list|()
operator|.
name|getCreationSource
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"default"
argument_list|,
name|repo
operator|.
name|getModel
argument_list|()
operator|.
name|getLayoutName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Change value and save.
name|repoSaved
operator|.
name|getModel
argument_list|()
operator|.
name|setCreationSource
argument_list|(
literal|"Changed"
argument_list|)
expr_stmt|;
name|repoDao
operator|.
name|saveRepository
argument_list|(
name|repoSaved
argument_list|)
expr_stmt|;
comment|// Test that only 1 object is saved.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repoDao
operator|.
name|getRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Get the specific repo.
name|ArchivaRepository
name|actualRepo
init|=
name|repoDao
operator|.
name|getRepository
argument_list|(
literal|"testRepo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|actualRepo
argument_list|)
expr_stmt|;
comment|// Test expected values.
name|assertEquals
argument_list|(
literal|"testRepo"
argument_list|,
name|actualRepo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://localhost:8080/repository/foo"
argument_list|,
name|actualRepo
operator|.
name|getUrl
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Changed"
argument_list|,
name|actualRepo
operator|.
name|getModel
argument_list|()
operator|.
name|getCreationSource
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that only 1 object is saved.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repoDao
operator|.
name|getRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Delete object.
name|repoDao
operator|.
name|deleteRepository
argument_list|(
name|actualRepo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repoDao
operator|.
name|getRepositories
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

