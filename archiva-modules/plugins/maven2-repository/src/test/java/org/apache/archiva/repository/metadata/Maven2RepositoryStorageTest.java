begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|metadata
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|storage
operator|.
name|RepositoryStorage
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
name|ArchivaSpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Assert
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

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|Maven2RepositoryStorageTest
block|{
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"repositoryStorage#maven2"
argument_list|)
name|RepositoryStorage
name|repositoryStorage
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testGetLogicalPath
parameter_list|()
block|{
name|String
name|href
init|=
literal|"/repository/internal/org/apache/maven/someartifact.jar"
decl_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"/org/apache/maven/someartifact.jar"
argument_list|,
name|repositoryStorage
operator|.
name|getFilePath
argument_list|(
name|href
argument_list|,
operator|new
name|ManagedRepository
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|href
operator|=
literal|"repository/internal/org/apache/maven/someartifact.jar"
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"/org/apache/maven/someartifact.jar"
argument_list|,
name|repositoryStorage
operator|.
name|getFilePath
argument_list|(
name|href
argument_list|,
operator|new
name|ManagedRepository
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|href
operator|=
literal|"repository/internal/org/apache/maven/"
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"/org/apache/maven/"
argument_list|,
name|repositoryStorage
operator|.
name|getFilePath
argument_list|(
name|href
argument_list|,
operator|new
name|ManagedRepository
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
name|href
operator|=
literal|"mypath"
expr_stmt|;
name|Assert
operator|.
name|assertEquals
argument_list|(
literal|"/"
argument_list|,
name|repositoryStorage
operator|.
name|getFilePath
argument_list|(
name|href
argument_list|,
operator|new
name|ManagedRepository
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

