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
name|consumers
operator|.
name|core
operator|.
name|repository
package|;
end_package

begin_comment
comment|/* * Licensed to the Apache Software Foundation (ASF) under one * or more contributor license agreements.  See the NOTICE file * distributed with this work for additional information * regarding copyright ownership.  The ASF licenses this file * to you under the Apache License, Version 2.0 (the * "License"); you may not use this file except in compliance * with the License.  You may obtain a copy of the License at * *  http://www.apache.org/licenses/LICENSE-2.0 * * Unless required by applicable law or agreed to in writing, * software distributed under the License is distributed on an * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY * KIND, either express or implied.  See the License for the * specific language governing permissions and limitations * under the License. */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusTestCase
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|jdo
operator|.
name|DefaultConfigurableJdoFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|jdo
operator|.
name|JdoFactory
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
name|RepositoryConfiguration
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|layout
operator|.
name|BidirectionalRepositoryLayout
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
name|repository
operator|.
name|layout
operator|.
name|LayoutException
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
name|repository
operator|.
name|layout
operator|.
name|DefaultBidirectionalRepositoryLayout
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
name|ArchivaDAO
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
name|ArtifactDAO
import|;
end_import

begin_import
import|import
name|org
operator|.
name|jpox
operator|.
name|SchemaTool
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|PersistenceManagerFactory
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|PersistenceManager
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
name|ArrayList
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URL
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
comment|/**  * @author<a href="mailto:oching@apache.org">Maria Odea Ching</a>  */
end_comment

begin_class
specifier|public
class|class
name|AbstractRepositoryPurgeTest
extends|extends
name|PlexusTestCase
block|{
specifier|public
specifier|static
specifier|final
name|String
name|TEST_REPO_ID
init|=
literal|"test-repo"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_REPO_NAME
init|=
literal|"Test Repository"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|TEST_REPO_URL
init|=
literal|"file://"
operator|+
name|getBasedir
argument_list|()
operator|+
literal|"/target/test-classes/test-repo/"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TEST_RETENTION_COUNT
init|=
literal|2
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|int
name|TEST_DAYS_OLDER
init|=
literal|30
decl_stmt|;
specifier|private
name|RepositoryConfiguration
name|config
decl_stmt|;
specifier|private
name|ArchivaRepository
name|repo
decl_stmt|;
specifier|private
name|BidirectionalRepositoryLayout
name|layout
decl_stmt|;
specifier|protected
name|ArtifactDAO
name|dao
decl_stmt|;
specifier|protected
name|RepositoryPurge
name|repoPurge
decl_stmt|;
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
name|DefaultConfigurableJdoFactory
name|jdoFactory
init|=
operator|(
name|DefaultConfigurableJdoFactory
operator|)
name|lookup
argument_list|(
name|JdoFactory
operator|.
name|ROLE
argument_list|,
literal|"archiva"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|DefaultConfigurableJdoFactory
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
name|jdoFactory
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setPersistenceManagerFactoryClass
argument_list|(
literal|"org.jpox.PersistenceManagerFactoryImpl"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setDriverName
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jdo.test.driver"
argument_list|,
literal|"org.hsqldb.jdbcDriver"
argument_list|)
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setUrl
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jdo.test.url"
argument_list|,
literal|"jdbc:hsqldb:mem:testdb"
argument_list|)
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setUserName
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jdo.test.user"
argument_list|,
literal|"sa"
argument_list|)
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setPassword
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"jdo.test.pass"
argument_list|,
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.transactionIsolation"
argument_list|,
literal|"READ_COMMITTED"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.poid.transactionIsolation"
argument_list|,
literal|"READ_COMMITTED"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.autoCreateSchema"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"javax.jdo.option.RetainValues"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"javax.jdo.option.RestoreValues"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
comment|// jdoFactory.setProperty( "org.jpox.autoCreateColumns", "true" );
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.validateTables"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.validateColumns"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|jdoFactory
operator|.
name|setProperty
argument_list|(
literal|"org.jpox.validateConstraints"
argument_list|,
literal|"true"
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
name|jdoFactory
operator|.
name|getProperties
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|it
init|=
name|properties
operator|.
name|entrySet
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|it
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|Map
operator|.
name|Entry
name|entry
init|=
operator|(
name|Map
operator|.
name|Entry
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
operator|(
name|String
operator|)
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
operator|(
name|String
operator|)
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|URL
name|jdoFileUrls
index|[]
init|=
operator|new
name|URL
index|[]
block|{
name|getClass
argument_list|()
operator|.
name|getResource
argument_list|(
literal|"/org/apache/maven/archiva/model/package.jdo"
argument_list|)
block|}
decl_stmt|;
if|if
condition|(
operator|(
name|jdoFileUrls
operator|==
literal|null
operator|)
operator|||
operator|(
name|jdoFileUrls
index|[
literal|0
index|]
operator|==
literal|null
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Unable to process test "
operator|+
name|getName
argument_list|()
operator|+
literal|" - missing package.jdo."
argument_list|)
expr_stmt|;
block|}
name|File
name|propsFile
init|=
literal|null
decl_stmt|;
comment|// intentional
name|boolean
name|verbose
init|=
literal|true
decl_stmt|;
name|SchemaTool
operator|.
name|deleteSchemaTables
argument_list|(
name|jdoFileUrls
argument_list|,
operator|new
name|URL
index|[]
block|{}
argument_list|,
name|propsFile
argument_list|,
name|verbose
argument_list|)
expr_stmt|;
name|SchemaTool
operator|.
name|createSchemaTables
argument_list|(
name|jdoFileUrls
argument_list|,
operator|new
name|URL
index|[]
block|{}
argument_list|,
name|propsFile
argument_list|,
name|verbose
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|PersistenceManagerFactory
name|pmf
init|=
name|jdoFactory
operator|.
name|getPersistenceManagerFactory
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|pmf
argument_list|)
expr_stmt|;
name|PersistenceManager
name|pm
init|=
name|pmf
operator|.
name|getPersistenceManager
argument_list|()
decl_stmt|;
name|pm
operator|.
name|close
argument_list|()
expr_stmt|;
name|dao
operator|=
operator|(
name|ArtifactDAO
operator|)
name|lookup
argument_list|(
name|ArtifactDAO
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"jdo"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RepositoryConfiguration
name|getRepoConfiguration
parameter_list|()
block|{
if|if
condition|(
name|config
operator|==
literal|null
condition|)
block|{
name|config
operator|=
operator|new
name|RepositoryConfiguration
argument_list|()
expr_stmt|;
block|}
name|config
operator|.
name|setId
argument_list|(
name|TEST_REPO_ID
argument_list|)
expr_stmt|;
name|config
operator|.
name|setName
argument_list|(
name|TEST_REPO_NAME
argument_list|)
expr_stmt|;
name|config
operator|.
name|setDaysOlder
argument_list|(
name|TEST_DAYS_OLDER
argument_list|)
expr_stmt|;
name|config
operator|.
name|setUrl
argument_list|(
name|TEST_REPO_URL
argument_list|)
expr_stmt|;
name|config
operator|.
name|setReleases
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setSnapshots
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setRetentionCount
argument_list|(
name|TEST_RETENTION_COUNT
argument_list|)
expr_stmt|;
return|return
name|config
return|;
block|}
specifier|public
name|ArchivaRepository
name|getRepository
parameter_list|()
block|{
if|if
condition|(
name|repo
operator|==
literal|null
condition|)
block|{
name|repo
operator|=
operator|new
name|ArchivaRepository
argument_list|(
name|TEST_REPO_ID
argument_list|,
name|TEST_REPO_NAME
argument_list|,
name|TEST_REPO_URL
argument_list|)
expr_stmt|;
block|}
return|return
name|repo
return|;
block|}
specifier|public
name|BidirectionalRepositoryLayout
name|getLayout
parameter_list|()
throws|throws
name|LayoutException
block|{
if|if
condition|(
name|layout
operator|==
literal|null
condition|)
block|{
name|layout
operator|=
operator|new
name|DefaultBidirectionalRepositoryLayout
argument_list|()
expr_stmt|;
block|}
return|return
name|layout
return|;
block|}
block|}
end_class

end_unit

