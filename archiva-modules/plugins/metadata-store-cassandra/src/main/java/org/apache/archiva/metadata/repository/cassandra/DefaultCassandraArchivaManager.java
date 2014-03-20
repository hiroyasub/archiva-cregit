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
name|me
operator|.
name|prettyprint
operator|.
name|cassandra
operator|.
name|model
operator|.
name|BasicColumnDefinition
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|cassandra
operator|.
name|model
operator|.
name|ConfigurableConsistencyLevel
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|cassandra
operator|.
name|serializers
operator|.
name|StringSerializer
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|cassandra
operator|.
name|service
operator|.
name|CassandraHostConfigurator
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|cassandra
operator|.
name|service
operator|.
name|ThriftKsDef
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|Cluster
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|HConsistencyLevel
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|Keyspace
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|ddl
operator|.
name|ColumnFamilyDefinition
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|ddl
operator|.
name|ColumnIndexType
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|ddl
operator|.
name|ComparatorType
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|factory
operator|.
name|HFactory
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
name|java
operator|.
name|util
operator|.
name|ArrayList
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
name|DefaultCassandraArchivaManager
implements|implements
name|CassandraArchivaManager
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
name|boolean
name|started
decl_stmt|;
specifier|private
name|Cluster
name|cluster
decl_stmt|;
specifier|private
name|Keyspace
name|keyspace
decl_stmt|;
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
comment|// FIXME must come from configuration not sys props
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
name|int
name|maxActive
init|=
name|Integer
operator|.
name|getInteger
argument_list|(
literal|"cassandra.maxActive"
argument_list|,
literal|20
argument_list|)
decl_stmt|;
name|String
name|readConsistencyLevel
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"cassandra.readConsistencyLevel"
argument_list|,
name|HConsistencyLevel
operator|.
name|QUORUM
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|writeConsistencyLevel
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"cassandra.readConsistencyLevel"
argument_list|,
name|HConsistencyLevel
operator|.
name|QUORUM
operator|.
name|name
argument_list|()
argument_list|)
decl_stmt|;
name|int
name|replicationFactor
init|=
name|Integer
operator|.
name|getInteger
argument_list|(
literal|"cassandra.replicationFactor"
argument_list|,
literal|1
argument_list|)
decl_stmt|;
name|String
name|keyspaceName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"cassandra.keyspace.name"
argument_list|,
name|KEYSPACE_NAME
argument_list|)
decl_stmt|;
name|String
name|clusterName
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"cassandra.cluster.name"
argument_list|,
name|CLUSTER_NAME
argument_list|)
decl_stmt|;
specifier|final
name|CassandraHostConfigurator
name|configurator
init|=
operator|new
name|CassandraHostConfigurator
argument_list|(
name|cassandraHost
operator|+
literal|":"
operator|+
name|cassandraPort
argument_list|)
decl_stmt|;
name|configurator
operator|.
name|setMaxActive
argument_list|(
name|maxActive
argument_list|)
expr_stmt|;
name|cluster
operator|=
name|HFactory
operator|.
name|getOrCreateCluster
argument_list|(
name|clusterName
argument_list|,
name|configurator
argument_list|)
expr_stmt|;
specifier|final
name|ConfigurableConsistencyLevel
name|consistencyLevelPolicy
init|=
operator|new
name|ConfigurableConsistencyLevel
argument_list|()
decl_stmt|;
name|consistencyLevelPolicy
operator|.
name|setDefaultReadConsistencyLevel
argument_list|(
name|HConsistencyLevel
operator|.
name|valueOf
argument_list|(
name|readConsistencyLevel
argument_list|)
argument_list|)
expr_stmt|;
name|consistencyLevelPolicy
operator|.
name|setDefaultWriteConsistencyLevel
argument_list|(
name|HConsistencyLevel
operator|.
name|valueOf
argument_list|(
name|writeConsistencyLevel
argument_list|)
argument_list|)
expr_stmt|;
name|keyspace
operator|=
name|HFactory
operator|.
name|createKeyspace
argument_list|(
name|keyspaceName
argument_list|,
name|cluster
argument_list|,
name|consistencyLevelPolicy
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|ColumnFamilyDefinition
argument_list|>
name|cfds
init|=
operator|new
name|ArrayList
argument_list|<
name|ColumnFamilyDefinition
argument_list|>
argument_list|()
decl_stmt|;
comment|// namespace table
block|{
specifier|final
name|ColumnFamilyDefinition
name|namespaces
init|=
name|HFactory
operator|.
name|createColumnFamilyDefinition
argument_list|(
name|keyspace
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"namespace"
argument_list|,
name|ComparatorType
operator|.
name|UTF8TYPE
argument_list|)
decl_stmt|;
name|cfds
operator|.
name|add
argument_list|(
name|namespaces
argument_list|)
expr_stmt|;
comment|// creating indexes for cql query
name|BasicColumnDefinition
name|nameColumn
init|=
operator|new
name|BasicColumnDefinition
argument_list|()
decl_stmt|;
name|nameColumn
operator|.
name|setName
argument_list|(
name|StringSerializer
operator|.
name|get
argument_list|()
operator|.
name|toByteBuffer
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|nameColumn
operator|.
name|setIndexName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|nameColumn
operator|.
name|setIndexType
argument_list|(
name|ColumnIndexType
operator|.
name|KEYS
argument_list|)
expr_stmt|;
name|nameColumn
operator|.
name|setValidationClass
argument_list|(
name|ComparatorType
operator|.
name|UTF8TYPE
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|addColumnDefinition
argument_list|(
name|nameColumn
argument_list|)
expr_stmt|;
name|BasicColumnDefinition
name|repositoryIdColumn
init|=
operator|new
name|BasicColumnDefinition
argument_list|()
decl_stmt|;
name|repositoryIdColumn
operator|.
name|setName
argument_list|(
name|StringSerializer
operator|.
name|get
argument_list|()
operator|.
name|toByteBuffer
argument_list|(
literal|"repositoryId"
argument_list|)
argument_list|)
expr_stmt|;
name|repositoryIdColumn
operator|.
name|setIndexName
argument_list|(
literal|"repositoryId"
argument_list|)
expr_stmt|;
name|repositoryIdColumn
operator|.
name|setIndexType
argument_list|(
name|ColumnIndexType
operator|.
name|KEYS
argument_list|)
expr_stmt|;
name|repositoryIdColumn
operator|.
name|setValidationClass
argument_list|(
name|ComparatorType
operator|.
name|UTF8TYPE
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|namespaces
operator|.
name|addColumnDefinition
argument_list|(
name|repositoryIdColumn
argument_list|)
expr_stmt|;
block|}
block|{
specifier|final
name|ColumnFamilyDefinition
name|repository
init|=
name|HFactory
operator|.
name|createColumnFamilyDefinition
argument_list|(
name|keyspace
operator|.
name|getKeyspaceName
argument_list|()
argument_list|,
literal|"repository"
argument_list|,
name|ComparatorType
operator|.
name|UTF8TYPE
argument_list|)
decl_stmt|;
name|cfds
operator|.
name|add
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|BasicColumnDefinition
name|nameColumn
init|=
operator|new
name|BasicColumnDefinition
argument_list|()
decl_stmt|;
name|nameColumn
operator|.
name|setName
argument_list|(
name|StringSerializer
operator|.
name|get
argument_list|()
operator|.
name|toByteBuffer
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
name|nameColumn
operator|.
name|setIndexName
argument_list|(
literal|"name"
argument_list|)
expr_stmt|;
name|nameColumn
operator|.
name|setIndexType
argument_list|(
name|ColumnIndexType
operator|.
name|KEYS
argument_list|)
expr_stmt|;
name|nameColumn
operator|.
name|setValidationClass
argument_list|(
name|ComparatorType
operator|.
name|UTF8TYPE
operator|.
name|getClassName
argument_list|()
argument_list|)
expr_stmt|;
name|repository
operator|.
name|addColumnDefinition
argument_list|(
name|nameColumn
argument_list|)
expr_stmt|;
block|}
block|{
comment|// ensure keyspace exists, here if the keyspace doesn't exist we suppose nothing exist
if|if
condition|(
name|cluster
operator|.
name|describeKeyspace
argument_list|(
name|keyspaceName
argument_list|)
operator|==
literal|null
condition|)
block|{
name|logger
operator|.
name|info
argument_list|(
literal|"Creating Archiva Cassandra '"
operator|+
name|keyspaceName
operator|+
literal|"' keyspace."
argument_list|)
expr_stmt|;
name|cluster
operator|.
name|addKeyspace
argument_list|(
name|HFactory
operator|.
name|createKeyspaceDefinition
argument_list|(
name|keyspaceName
argument_list|,
comment|//
name|ThriftKsDef
operator|.
name|DEF_STRATEGY_CLASS
argument_list|,
comment|//
name|replicationFactor
argument_list|,
comment|//
name|cfds
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
block|}
specifier|public
name|void
name|start
parameter_list|()
block|{
block|}
annotation|@
name|PreDestroy
specifier|public
name|void
name|shutdown
parameter_list|()
block|{
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
name|Cluster
name|getCluster
parameter_list|()
block|{
return|return
name|cluster
return|;
block|}
block|}
end_class

end_unit

