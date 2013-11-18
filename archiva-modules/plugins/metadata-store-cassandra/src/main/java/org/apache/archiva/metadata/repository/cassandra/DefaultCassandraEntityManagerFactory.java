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
name|com
operator|.
name|google
operator|.
name|common
operator|.
name|collect
operator|.
name|ImmutableMap
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|AstyanaxContext
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|Keyspace
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|connectionpool
operator|.
name|NodeDiscoveryType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|connectionpool
operator|.
name|exceptions
operator|.
name|ConnectionException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|connectionpool
operator|.
name|exceptions
operator|.
name|NotFoundException
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|connectionpool
operator|.
name|impl
operator|.
name|ConnectionPoolConfigurationImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|connectionpool
operator|.
name|impl
operator|.
name|ConnectionPoolType
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|connectionpool
operator|.
name|impl
operator|.
name|Slf4jConnectionPoolMonitorImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|ddl
operator|.
name|KeyspaceDefinition
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|entitystore
operator|.
name|DefaultEntityManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|entitystore
operator|.
name|EntityManager
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|impl
operator|.
name|AstyanaxConfigurationImpl
import|;
end_import

begin_import
import|import
name|com
operator|.
name|netflix
operator|.
name|astyanax
operator|.
name|thrift
operator|.
name|ThriftFamilyFactory
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
name|ArtifactMetadataModel
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
name|MetadataFacetModel
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
name|Project
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
name|ProjectVersionMetadataModel
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
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|annotation
operator|.
name|PreDestroy
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
name|persistence
operator|.
name|PersistenceException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * FIXME make all configuration not hardcoded :-)  *  * @author Olivier Lamy  * @since 2.0.0  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaEntityManagerFactory#cassandra"
argument_list|)
specifier|public
class|class
name|DefaultCassandraEntityManagerFactory
implements|implements
name|CassandraEntityManagerFactory
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
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CLUSTER_NAME
init|=
literal|"archiva"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|KEYSPACE_NAME
init|=
literal|"ArchivaKeySpace"
decl_stmt|;
specifier|private
name|AstyanaxContext
argument_list|<
name|Keyspace
argument_list|>
name|keyspaceContext
decl_stmt|;
specifier|private
name|Keyspace
name|keyspace
decl_stmt|;
specifier|private
name|boolean
name|started
init|=
literal|false
decl_stmt|;
specifier|private
name|EntityManager
argument_list|<
name|Repository
argument_list|,
name|String
argument_list|>
name|repositoryEntityManager
decl_stmt|;
specifier|private
name|EntityManager
argument_list|<
name|Namespace
argument_list|,
name|String
argument_list|>
name|namespaceEntityManager
decl_stmt|;
specifier|private
name|EntityManager
argument_list|<
name|Project
argument_list|,
name|String
argument_list|>
name|projectEntityManager
decl_stmt|;
specifier|private
name|EntityManager
argument_list|<
name|ArtifactMetadataModel
argument_list|,
name|String
argument_list|>
name|artifactMetadataModelEntityManager
decl_stmt|;
specifier|private
name|EntityManager
argument_list|<
name|MetadataFacetModel
argument_list|,
name|String
argument_list|>
name|metadataFacetModelEntityManager
decl_stmt|;
specifier|private
name|EntityManager
argument_list|<
name|ProjectVersionMetadataModel
argument_list|,
name|String
argument_list|>
name|projectVersionMetadataModelEntityManager
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|ConnectionException
block|{
name|String
name|cassandraHost
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"cassandraHost"
argument_list|,
literal|"localhost"
argument_list|)
decl_stmt|;
name|String
name|cassandraPort
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"cassandraPort"
argument_list|)
decl_stmt|;
name|keyspaceContext
operator|=
operator|new
name|AstyanaxContext
operator|.
name|Builder
argument_list|()
operator|.
name|forCluster
argument_list|(
name|CLUSTER_NAME
argument_list|)
operator|.
name|forKeyspace
argument_list|(
name|KEYSPACE_NAME
argument_list|)
operator|.
name|withAstyanaxConfiguration
argument_list|(
operator|new
name|AstyanaxConfigurationImpl
argument_list|()
operator|.
name|setDiscoveryType
argument_list|(
name|NodeDiscoveryType
operator|.
name|RING_DESCRIBE
argument_list|)
operator|.
name|setConnectionPoolType
argument_list|(
name|ConnectionPoolType
operator|.
name|TOKEN_AWARE
argument_list|)
argument_list|)
operator|.
name|withConnectionPoolConfiguration
argument_list|(
operator|new
name|ConnectionPoolConfigurationImpl
argument_list|(
name|CLUSTER_NAME
operator|+
literal|"_"
operator|+
name|KEYSPACE_NAME
argument_list|)
operator|.
name|setSocketTimeout
argument_list|(
literal|30000
argument_list|)
operator|.
name|setMaxTimeoutWhenExhausted
argument_list|(
literal|2000
argument_list|)
operator|.
name|setMaxConnsPerHost
argument_list|(
literal|20
argument_list|)
operator|.
name|setInitConnsPerHost
argument_list|(
literal|10
argument_list|)
operator|.
name|setSeeds
argument_list|(
name|cassandraHost
operator|+
literal|":"
operator|+
name|cassandraPort
argument_list|)
argument_list|)
operator|.
name|withConnectionPoolMonitor
argument_list|(
operator|new
name|Slf4jConnectionPoolMonitorImpl
argument_list|()
argument_list|)
operator|.
name|buildKeyspace
argument_list|(
name|ThriftFamilyFactory
operator|.
name|getInstance
argument_list|()
argument_list|)
expr_stmt|;
name|this
operator|.
name|start
argument_list|()
expr_stmt|;
name|keyspace
operator|=
name|keyspaceContext
operator|.
name|getClient
argument_list|()
expr_stmt|;
comment|//Partitioner partitioner = keyspace.getPartitioner();
name|ImmutableMap
argument_list|<
name|String
argument_list|,
name|Object
argument_list|>
name|options
init|=
name|ImmutableMap
operator|.
expr|<
name|String
decl_stmt|,
name|Object
decl|>
name|builder
argument_list|()
decl|.
name|put
argument_list|(
literal|"strategy_options"
argument_list|,
name|ImmutableMap
operator|.
expr|<
name|String
argument_list|,
name|Object
operator|>
name|builder
argument_list|()
operator|.
name|put
argument_list|(
literal|"replication_factor"
argument_list|,
literal|"1"
argument_list|)
operator|.
name|build
argument_list|()
argument_list|)
decl|.
name|put
argument_list|(
literal|"strategy_class"
argument_list|,
literal|"SimpleStrategy"
argument_list|)
decl|.
name|build
argument_list|()
decl_stmt|;
comment|// test if the namespace already exists if exception or null create it
name|boolean
name|keyspaceExists
init|=
literal|false
decl_stmt|;
try|try
block|{
name|KeyspaceDefinition
name|keyspaceDefinition
init|=
name|keyspace
operator|.
name|describeKeyspace
argument_list|()
decl_stmt|;
if|if
condition|(
name|keyspaceDefinition
operator|!=
literal|null
condition|)
block|{
name|keyspaceExists
operator|=
literal|true
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|ConnectionException
name|e
parameter_list|)
block|{
block|}
if|if
condition|(
operator|!
name|keyspaceExists
condition|)
block|{
name|keyspace
operator|.
name|createKeyspace
argument_list|(
name|options
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|Properties
name|properties
init|=
name|keyspace
operator|.
name|getKeyspaceProperties
argument_list|()
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"keyspace properties: {}"
argument_list|,
name|properties
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConnectionException
name|e
parameter_list|)
block|{
comment|// FIXME better logging !
name|logger
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|repositoryEntityManager
operator|=
operator|new
name|DefaultEntityManager
operator|.
name|Builder
argument_list|<
name|Repository
argument_list|,
name|String
argument_list|>
argument_list|()
operator|.
name|withEntityType
argument_list|(
name|Repository
operator|.
name|class
argument_list|)
operator|.
name|withKeyspace
argument_list|(
name|keyspace
argument_list|)
operator|.
name|withAutoCommit
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|boolean
name|exists
init|=
name|columnFamilyExists
argument_list|(
literal|"repository"
argument_list|)
decl_stmt|;
comment|// TODO very basic test we must test model change too
if|if
condition|(
operator|!
name|exists
condition|)
block|{
name|repositoryEntityManager
operator|.
name|createStorage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|namespaceEntityManager
operator|=
operator|new
name|DefaultEntityManager
operator|.
name|Builder
argument_list|<
name|Namespace
argument_list|,
name|String
argument_list|>
argument_list|()
operator|.
name|withEntityType
argument_list|(
name|Namespace
operator|.
name|class
argument_list|)
operator|.
name|withKeyspace
argument_list|(
name|keyspace
argument_list|)
operator|.
name|withAutoCommit
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|exists
operator|=
name|columnFamilyExists
argument_list|(
literal|"namespace"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exists
condition|)
block|{
name|namespaceEntityManager
operator|.
name|createStorage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|projectEntityManager
operator|=
operator|new
name|DefaultEntityManager
operator|.
name|Builder
argument_list|<
name|Project
argument_list|,
name|String
argument_list|>
argument_list|()
operator|.
name|withEntityType
argument_list|(
name|Project
operator|.
name|class
argument_list|)
operator|.
name|withKeyspace
argument_list|(
name|keyspace
argument_list|)
operator|.
name|withAutoCommit
argument_list|(
literal|true
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|exists
operator|=
name|columnFamilyExists
argument_list|(
literal|"project"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exists
condition|)
block|{
name|projectEntityManager
operator|.
name|createStorage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|artifactMetadataModelEntityManager
operator|=
operator|new
name|DefaultEntityManager
operator|.
name|Builder
argument_list|<
name|ArtifactMetadataModel
argument_list|,
name|String
argument_list|>
argument_list|()
operator|.
name|withEntityType
argument_list|(
name|ArtifactMetadataModel
operator|.
name|class
argument_list|)
operator|.
name|withAutoCommit
argument_list|(
literal|true
argument_list|)
operator|.
name|withKeyspace
argument_list|(
name|keyspace
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|exists
operator|=
name|columnFamilyExists
argument_list|(
literal|"artifactmetadatamodel"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exists
condition|)
block|{
name|artifactMetadataModelEntityManager
operator|.
name|createStorage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|metadataFacetModelEntityManager
operator|=
operator|new
name|DefaultEntityManager
operator|.
name|Builder
argument_list|<
name|MetadataFacetModel
argument_list|,
name|String
argument_list|>
argument_list|()
operator|.
name|withEntityType
argument_list|(
name|MetadataFacetModel
operator|.
name|class
argument_list|)
operator|.
name|withAutoCommit
argument_list|(
literal|true
argument_list|)
operator|.
name|withKeyspace
argument_list|(
name|keyspace
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|exists
operator|=
name|columnFamilyExists
argument_list|(
literal|"metadatafacetmodel"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exists
condition|)
block|{
name|metadataFacetModelEntityManager
operator|.
name|createStorage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
name|projectVersionMetadataModelEntityManager
operator|=
operator|new
name|DefaultEntityManager
operator|.
name|Builder
argument_list|<
name|ProjectVersionMetadataModel
argument_list|,
name|String
argument_list|>
argument_list|()
operator|.
name|withEntityType
argument_list|(
name|ProjectVersionMetadataModel
operator|.
name|class
argument_list|)
operator|.
name|withAutoCommit
argument_list|(
literal|true
argument_list|)
operator|.
name|withKeyspace
argument_list|(
name|keyspace
argument_list|)
operator|.
name|build
argument_list|()
expr_stmt|;
name|exists
operator|=
name|columnFamilyExists
argument_list|(
literal|"projectversionmetadatamodel"
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|exists
condition|)
block|{
name|projectVersionMetadataModelEntityManager
operator|.
name|createStorage
argument_list|(
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|PersistenceException
name|e
parameter_list|)
block|{
comment|// FIXME report exception
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
block|}
catch|catch
parameter_list|(
name|ConnectionException
name|e
parameter_list|)
block|{
comment|// FIXME report exception
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
block|}
block|}
specifier|public
name|void
name|start
parameter_list|()
block|{
name|keyspaceContext
operator|.
name|start
argument_list|()
expr_stmt|;
name|started
operator|=
literal|true
expr_stmt|;
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
if|if
condition|(
name|keyspaceContext
operator|!=
literal|null
condition|)
block|{
name|keyspaceContext
operator|.
name|shutdown
argument_list|()
expr_stmt|;
name|started
operator|=
literal|false
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|started
parameter_list|()
block|{
return|return
name|started
return|;
block|}
specifier|private
name|boolean
name|columnFamilyExists
parameter_list|(
name|String
name|columnFamilyName
parameter_list|)
throws|throws
name|ConnectionException
block|{
try|try
block|{
name|Properties
name|properties
init|=
name|keyspace
operator|.
name|getColumnFamilyProperties
argument_list|(
name|columnFamilyName
argument_list|)
decl_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"getColumnFamilyProperties for {}: {}"
argument_list|,
name|columnFamilyName
argument_list|,
name|properties
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
catch|catch
parameter_list|(
name|NotFoundException
name|e
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|Keyspace
name|getKeyspace
parameter_list|()
block|{
return|return
name|keyspace
return|;
block|}
specifier|public
name|EntityManager
argument_list|<
name|Repository
argument_list|,
name|String
argument_list|>
name|getRepositoryEntityManager
parameter_list|()
block|{
return|return
name|repositoryEntityManager
return|;
block|}
specifier|public
name|void
name|setRepositoryEntityManager
parameter_list|(
name|EntityManager
argument_list|<
name|Repository
argument_list|,
name|String
argument_list|>
name|repositoryEntityManager
parameter_list|)
block|{
name|this
operator|.
name|repositoryEntityManager
operator|=
name|repositoryEntityManager
expr_stmt|;
block|}
specifier|public
name|EntityManager
argument_list|<
name|Namespace
argument_list|,
name|String
argument_list|>
name|getNamespaceEntityManager
parameter_list|()
block|{
return|return
name|namespaceEntityManager
return|;
block|}
specifier|public
name|void
name|setNamespaceEntityManager
parameter_list|(
name|EntityManager
argument_list|<
name|Namespace
argument_list|,
name|String
argument_list|>
name|namespaceEntityManager
parameter_list|)
block|{
name|this
operator|.
name|namespaceEntityManager
operator|=
name|namespaceEntityManager
expr_stmt|;
block|}
specifier|public
name|EntityManager
argument_list|<
name|Project
argument_list|,
name|String
argument_list|>
name|getProjectEntityManager
parameter_list|()
block|{
return|return
name|projectEntityManager
return|;
block|}
specifier|public
name|void
name|setProjectEntityManager
parameter_list|(
name|EntityManager
argument_list|<
name|Project
argument_list|,
name|String
argument_list|>
name|projectEntityManager
parameter_list|)
block|{
name|this
operator|.
name|projectEntityManager
operator|=
name|projectEntityManager
expr_stmt|;
block|}
specifier|public
name|EntityManager
argument_list|<
name|ArtifactMetadataModel
argument_list|,
name|String
argument_list|>
name|getArtifactMetadataModelEntityManager
parameter_list|()
block|{
return|return
name|artifactMetadataModelEntityManager
return|;
block|}
specifier|public
name|void
name|setArtifactMetadataModelEntityManager
parameter_list|(
name|EntityManager
argument_list|<
name|ArtifactMetadataModel
argument_list|,
name|String
argument_list|>
name|artifactMetadataModelEntityManager
parameter_list|)
block|{
name|this
operator|.
name|artifactMetadataModelEntityManager
operator|=
name|artifactMetadataModelEntityManager
expr_stmt|;
block|}
specifier|public
name|EntityManager
argument_list|<
name|MetadataFacetModel
argument_list|,
name|String
argument_list|>
name|getMetadataFacetModelEntityManager
parameter_list|()
block|{
return|return
name|metadataFacetModelEntityManager
return|;
block|}
specifier|public
name|void
name|setMetadataFacetModelEntityManager
parameter_list|(
name|EntityManager
argument_list|<
name|MetadataFacetModel
argument_list|,
name|String
argument_list|>
name|metadataFacetModelEntityManager
parameter_list|)
block|{
name|this
operator|.
name|metadataFacetModelEntityManager
operator|=
name|metadataFacetModelEntityManager
expr_stmt|;
block|}
specifier|public
name|EntityManager
argument_list|<
name|ProjectVersionMetadataModel
argument_list|,
name|String
argument_list|>
name|getProjectVersionMetadataModelEntityManager
parameter_list|()
block|{
return|return
name|projectVersionMetadataModelEntityManager
return|;
block|}
specifier|public
name|void
name|setProjectVersionMetadataModelEntityManager
parameter_list|(
name|EntityManager
argument_list|<
name|ProjectVersionMetadataModel
argument_list|,
name|String
argument_list|>
name|projectVersionMetadataModelEntityManager
parameter_list|)
block|{
name|this
operator|.
name|projectVersionMetadataModelEntityManager
operator|=
name|projectVersionMetadataModelEntityManager
expr_stmt|;
block|}
block|}
end_class

end_unit

