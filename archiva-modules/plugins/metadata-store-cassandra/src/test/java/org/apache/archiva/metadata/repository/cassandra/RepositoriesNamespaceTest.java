begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
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
name|cassandra
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
name|archiva
operator|.
name|metadata
operator|.
name|model
operator|.
name|ProjectMetadata
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
name|cassandra
operator|.
name|model
operator|.
name|Namespace
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
name|cassandra
operator|.
name|model
operator|.
name|Repository
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
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
block|,
literal|"classpath*:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|RepositoriesNamespaceTest
block|{
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaEntityManagerFactory#cassandra"
argument_list|)
name|CassandraArchivaManager
name|cassandraArchivaManager
decl_stmt|;
name|CassandraMetadataRepository
name|cmr
decl_stmt|;
annotation|@
name|Before
specifier|public
name|void
name|setup
parameter_list|()
throws|throws
name|Exception
block|{
name|cmr
operator|=
operator|new
name|CassandraMetadataRepository
argument_list|(
literal|null
argument_list|,
name|cassandraArchivaManager
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|cassandraArchivaManager
operator|.
name|started
argument_list|()
condition|)
block|{
name|cassandraArchivaManager
operator|.
name|start
argument_list|()
expr_stmt|;
block|}
name|CassandraMetadataRepositoryTest
operator|.
name|clearReposAndNamespace
argument_list|(
name|cassandraArchivaManager
argument_list|)
expr_stmt|;
block|}
annotation|@
name|After
specifier|public
name|void
name|shutdown
parameter_list|()
throws|throws
name|Exception
block|{
name|CassandraMetadataRepositoryTest
operator|.
name|clearReposAndNamespace
argument_list|(
name|cassandraArchivaManager
argument_list|)
expr_stmt|;
name|cassandraArchivaManager
operator|.
name|shutdown
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testMetadataRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|Repository
name|r
init|=
literal|null
decl_stmt|;
name|Namespace
name|n
init|=
literal|null
decl_stmt|;
try|try
block|{
name|cmr
operator|.
name|updateNamespace
argument_list|(
literal|null
argument_list|,
literal|"release"
argument_list|,
literal|"org"
argument_list|)
expr_stmt|;
name|r
operator|=
name|cmr
operator|.
name|getRepository
argument_list|(
literal|"release"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getNamespaces
argument_list|(
literal|"release"
argument_list|)
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|n
operator|=
name|cmr
operator|.
name|getNamespace
argument_list|(
literal|"release"
argument_list|,
literal|"org"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|n
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|n
operator|.
name|getRepository
argument_list|()
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|cmr
operator|.
name|updateNamespace
argument_list|(
literal|null
argument_list|,
literal|"release"
argument_list|,
literal|"org.apache"
argument_list|)
expr_stmt|;
name|r
operator|=
name|cmr
operator|.
name|getRepository
argument_list|(
literal|"release"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|isNotNull
argument_list|()
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getNamespaces
argument_list|(
literal|"release"
argument_list|)
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|cmr
operator|.
name|removeNamespace
argument_list|(
literal|null
argument_list|,
literal|"release"
argument_list|,
literal|"org.apache"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getNamespaces
argument_list|(
literal|"release"
argument_list|)
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getNamespaces
argument_list|(
literal|"release"
argument_list|)
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"org"
argument_list|)
expr_stmt|;
name|ProjectMetadata
name|projectMetadata
init|=
operator|new
name|ProjectMetadata
argument_list|()
decl_stmt|;
name|projectMetadata
operator|.
name|setId
argument_list|(
literal|"theproject"
argument_list|)
expr_stmt|;
name|projectMetadata
operator|.
name|setNamespace
argument_list|(
literal|"org"
argument_list|)
expr_stmt|;
name|cmr
operator|.
name|updateProject
argument_list|(
literal|null
argument_list|,
literal|"release"
argument_list|,
name|projectMetadata
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getProjects
argument_list|(
literal|null
argument_list|,
literal|"release"
argument_list|,
literal|"org"
argument_list|)
argument_list|)
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
operator|.
name|containsExactly
argument_list|(
literal|"theproject"
argument_list|)
expr_stmt|;
name|cmr
operator|.
name|removeProject
argument_list|(
literal|null
argument_list|,
literal|"release"
argument_list|,
literal|"org"
argument_list|,
literal|"theproject"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|cmr
operator|.
name|getProjects
argument_list|(
literal|null
argument_list|,
literal|"release"
argument_list|,
literal|"org"
argument_list|)
argument_list|)
operator|.
name|isEmpty
argument_list|()
expr_stmt|;
name|cmr
operator|.
name|removeRepository
argument_list|(
literal|null
argument_list|,
literal|"release"
argument_list|)
expr_stmt|;
name|r
operator|=
name|cmr
operator|.
name|getRepository
argument_list|(
literal|"release"
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|r
argument_list|)
operator|.
name|isNull
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
name|e
throw|;
block|}
finally|finally
block|{
name|CassandraMetadataRepositoryTest
operator|.
name|clearReposAndNamespace
argument_list|(
name|cassandraArchivaManager
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

