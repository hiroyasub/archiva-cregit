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
name|MetadataFacetFactory
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
name|AbstractMetadataRepositoryTest
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
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|CassandraMetadataRepositoryTest
extends|extends
name|AbstractMetadataRepositoryTest
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
name|File
name|directory
init|=
operator|new
name|File
argument_list|(
literal|"target/test-repositories"
argument_list|)
decl_stmt|;
if|if
condition|(
name|directory
operator|.
name|exists
argument_list|()
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
name|Map
argument_list|<
name|String
argument_list|,
name|MetadataFacetFactory
argument_list|>
name|factories
init|=
name|createTestMetadataFacetFactories
argument_list|()
decl_stmt|;
name|this
operator|.
name|cmr
operator|=
operator|new
name|CassandraMetadataRepository
argument_list|(
name|factories
argument_list|,
literal|null
argument_list|,
name|cassandraArchivaManager
argument_list|)
expr_stmt|;
name|this
operator|.
name|repository
operator|=
name|this
operator|.
name|cmr
expr_stmt|;
name|clearReposAndNamespace
argument_list|()
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
name|clearReposAndNamespace
argument_list|()
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|clearReposAndNamespace
parameter_list|()
throws|throws
name|Exception
block|{
name|cassandraArchivaManager
operator|.
name|getCluster
argument_list|()
operator|.
name|truncate
argument_list|(
name|cassandraArchivaManager
operator|.
name|getKeyspace
argument_list|()
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"project"
argument_list|)
expr_stmt|;
name|cassandraArchivaManager
operator|.
name|getCluster
argument_list|()
operator|.
name|truncate
argument_list|(
name|cassandraArchivaManager
operator|.
name|getKeyspace
argument_list|()
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"namespace"
argument_list|)
expr_stmt|;
name|cassandraArchivaManager
operator|.
name|getCluster
argument_list|()
operator|.
name|truncate
argument_list|(
name|cassandraArchivaManager
operator|.
name|getKeyspace
argument_list|()
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"repository"
argument_list|)
expr_stmt|;
name|cassandraArchivaManager
operator|.
name|getCluster
argument_list|()
operator|.
name|truncate
argument_list|(
name|cassandraArchivaManager
operator|.
name|getKeyspace
argument_list|()
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"projectversionmetadatamodel"
argument_list|)
expr_stmt|;
name|cassandraArchivaManager
operator|.
name|getCluster
argument_list|()
operator|.
name|truncate
argument_list|(
name|cassandraArchivaManager
operator|.
name|getKeyspace
argument_list|()
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"artifactmetadatamodel"
argument_list|)
expr_stmt|;
name|cassandraArchivaManager
operator|.
name|getCluster
argument_list|()
operator|.
name|truncate
argument_list|(
name|cassandraArchivaManager
operator|.
name|getKeyspace
argument_list|()
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"metadatafacetmodel"
argument_list|)
expr_stmt|;
name|cassandraArchivaManager
operator|.
name|getCluster
argument_list|()
operator|.
name|truncate
argument_list|(
name|cassandraArchivaManager
operator|.
name|getKeyspace
argument_list|()
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"metadatafacetmodel"
argument_list|)
expr_stmt|;
name|cassandraArchivaManager
operator|.
name|getCluster
argument_list|()
operator|.
name|truncate
argument_list|(
name|cassandraArchivaManager
operator|.
name|getKeyspace
argument_list|()
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"mailinglist"
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

