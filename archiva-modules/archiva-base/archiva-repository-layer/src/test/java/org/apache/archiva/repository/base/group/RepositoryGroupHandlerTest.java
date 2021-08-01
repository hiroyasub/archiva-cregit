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
name|base
operator|.
name|group
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|filelock
operator|.
name|DefaultFileLockManager
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
name|common
operator|.
name|filelock
operator|.
name|FileLockManager
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
name|common
operator|.
name|utils
operator|.
name|FileUtils
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
name|common
operator|.
name|utils
operator|.
name|PathUtil
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
name|components
operator|.
name|registry
operator|.
name|RegistryException
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|archiva
operator|.
name|configuration
operator|.
name|IndeterminateConfigurationException
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
name|configuration
operator|.
name|RepositoryGroupConfiguration
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
name|indexer
operator|.
name|merger
operator|.
name|MergedRemoteIndexesScheduler
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
name|repository
operator|.
name|EditableRepositoryGroup
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
name|repository
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
name|repository
operator|.
name|RepositoryException
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
name|repository
operator|.
name|RepositoryGroup
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
name|repository
operator|.
name|RepositoryState
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
name|repository
operator|.
name|RepositoryType
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
name|repository
operator|.
name|base
operator|.
name|ArchivaRepositoryRegistry
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
name|repository
operator|.
name|base
operator|.
name|ConfigurationHandler
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
name|repository
operator|.
name|storage
operator|.
name|fs
operator|.
name|FilesystemStorage
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
name|repository
operator|.
name|validation
operator|.
name|CheckedResult
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
name|repository
operator|.
name|validation
operator|.
name|RepositoryValidator
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
name|repository
operator|.
name|validation
operator|.
name|ValidationError
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|BeforeAll
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
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
name|jupiter
operator|.
name|api
operator|.
name|extension
operator|.
name|ExtendWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|Mock
import|;
end_import

begin_import
import|import
name|org
operator|.
name|mockito
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|MockitoExtension
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
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|SpringExtension
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
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|Iterator
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

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|validation
operator|.
name|ErrorKeys
operator|.
name|ISEMPTY
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|*
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|any
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|ArgumentMatchers
operator|.
name|eq
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|mockito
operator|.
name|Mockito
operator|.
name|verify
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|ExtendWith
argument_list|(
block|{
name|MockitoExtension
operator|.
name|class
block|,
name|SpringExtension
operator|.
name|class
block|}
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath:/spring-context-group.xml"
block|}
argument_list|)
class|class
name|RepositoryGroupHandlerTest
block|{
static|static
block|{
name|initialize
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
literal|"repositoryRegistry"
argument_list|)
name|ArchivaRepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|Inject
name|ConfigurationHandler
name|configurationHandler
decl_stmt|;
annotation|@
name|Mock
comment|// @Named( "mergedRemoteIndexesScheduler#default" )
name|MergedRemoteIndexesScheduler
name|mergedRemoteIndexesScheduler
decl_stmt|;
annotation|@
name|Inject
name|List
argument_list|<
name|RepositoryValidator
argument_list|<
name|?
extends|extends
name|Repository
argument_list|>
argument_list|>
name|repositoryValidatorList
decl_stmt|;
annotation|@
name|Inject
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
name|Path
name|repoBaseDir
decl_stmt|;
specifier|private
specifier|static
name|void
name|initialize
parameter_list|()
block|{
name|Path
name|baseDir
init|=
name|Paths
operator|.
name|get
argument_list|(
name|FileUtils
operator|.
name|getBasedir
argument_list|( )
argument_list|)
decl_stmt|;
name|Path
name|config
init|=
name|baseDir
operator|.
name|resolve
argument_list|(
literal|"src/test/resources/archiva-group.xml"
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|config
argument_list|)
condition|)
block|{
name|Path
name|destConfig
init|=
name|baseDir
operator|.
name|resolve
argument_list|(
literal|"target/test-classes/archiva-group.xml"
argument_list|)
decl_stmt|;
try|try
block|{
name|Files
operator|.
name|copy
argument_list|(
name|config
argument_list|,
name|destConfig
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Could not copy file: "
operator|+
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|// Helper method that removes a group from the configuration
specifier|private
name|void
name|removeGroupFromConfig
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|Configuration
name|configuration
init|=
name|configurationHandler
operator|.
name|getBaseConfiguration
argument_list|( )
decl_stmt|;
name|Iterator
argument_list|<
name|RepositoryGroupConfiguration
argument_list|>
name|groupIter
init|=
name|configuration
operator|.
name|getRepositoryGroups
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|groupIter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|RepositoryGroupConfiguration
name|group
init|=
name|groupIter
operator|.
name|next
argument_list|( )
decl_stmt|;
if|if
condition|(
name|groupId
operator|.
name|equals
argument_list|(
name|group
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|groupIter
operator|.
name|remove
argument_list|()
expr_stmt|;
break|break;
block|}
block|}
try|try
block|{
name|configurationHandler
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
name|System
operator|.
name|err
operator|.
name|println
argument_list|(
literal|"Could not remove repo group from config "
operator|+
name|groupId
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|hasGroupInConfig
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|configurationHandler
operator|.
name|getBaseConfiguration
argument_list|( )
operator|.
name|getRepositoryGroups
argument_list|( )
argument_list|)
expr_stmt|;
return|return
name|configurationHandler
operator|.
name|getBaseConfiguration
argument_list|( )
operator|.
name|getRepositoryGroups
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|anyMatch
argument_list|(
name|g
lambda|->
name|g
operator|!=
literal|null
operator|&&
name|groupId
operator|.
name|equals
argument_list|(
name|g
operator|.
name|getId
argument_list|( )
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|RepositoryGroupHandler
name|createHandler
parameter_list|( )
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
operator|new
name|RepositoryGroupHandler
argument_list|(
name|repositoryRegistry
argument_list|,
name|configurationHandler
argument_list|,
name|mergedRemoteIndexesScheduler
argument_list|,
name|repositoryValidatorList
argument_list|)
decl_stmt|;
name|groupHandler
operator|.
name|init
argument_list|( )
expr_stmt|;
return|return
name|groupHandler
return|;
block|}
specifier|private
name|Path
name|getRepoBaseDir
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|repoBaseDir
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|repoBaseDir
operator|=
name|archivaConfiguration
operator|.
name|getRepositoryBaseDir
argument_list|( )
operator|.
name|resolve
argument_list|(
literal|"group"
argument_list|)
expr_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|this
operator|.
name|repoBaseDir
argument_list|)
expr_stmt|;
block|}
return|return
name|repoBaseDir
return|;
block|}
specifier|protected
name|EditableRepositoryGroup
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|Path
name|location
parameter_list|)
throws|throws
name|IOException
block|{
name|FileLockManager
name|lockManager
init|=
operator|new
name|DefaultFileLockManager
argument_list|()
decl_stmt|;
name|FilesystemStorage
name|storage
init|=
operator|new
name|FilesystemStorage
argument_list|(
name|location
operator|.
name|toAbsolutePath
argument_list|()
argument_list|,
name|lockManager
argument_list|)
decl_stmt|;
name|BasicRepositoryGroup
name|repo
init|=
operator|new
name|BasicRepositoryGroup
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|storage
argument_list|)
decl_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|location
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
specifier|protected
name|EditableRepositoryGroup
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|dir
init|=
name|getRepoBaseDir
argument_list|( )
operator|.
name|resolve
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|Files
operator|.
name|createDirectories
argument_list|(
name|dir
argument_list|)
expr_stmt|;
return|return
name|createRepository
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|dir
argument_list|)
return|;
block|}
annotation|@
name|Test
name|void
name|initializeFromConfig
parameter_list|( )
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|groupHandler
operator|.
name|initializeFromConfig
argument_list|( )
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|groupHandler
operator|.
name|getAll
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|groupHandler
operator|.
name|get
argument_list|(
literal|"test-group-01"
argument_list|)
operator|.
name|getRepositories
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"internal"
argument_list|,
name|groupHandler
operator|.
name|get
argument_list|(
literal|"test-group-01"
argument_list|)
operator|.
name|getRepositories
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|activateRepository
parameter_list|( )
throws|throws
name|RepositoryException
block|{
name|String
name|id
init|=
literal|"test-group-02"
decl_stmt|;
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|RepositoryGroup
name|repo
init|=
name|groupHandler
operator|.
name|newInstance
argument_list|(
name|RepositoryType
operator|.
name|MAVEN
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|groupHandler
operator|.
name|activateRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|verify
argument_list|(
name|mergedRemoteIndexesScheduler
argument_list|)
operator|.
name|schedule
argument_list|(
name|eq
argument_list|(
name|repo
argument_list|)
argument_list|,
name|any
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RepositoryState
operator|.
name|INITIALIZED
argument_list|,
name|repo
operator|.
name|getLastState
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasGroupInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|newInstancesFromConfig
parameter_list|( )
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
operator|new
name|RepositoryGroupHandler
argument_list|(
name|repositoryRegistry
argument_list|,
name|configurationHandler
argument_list|,
name|mergedRemoteIndexesScheduler
argument_list|,
name|repositoryValidatorList
argument_list|)
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|RepositoryGroup
argument_list|>
name|instances
init|=
name|groupHandler
operator|.
name|newInstancesFromConfig
argument_list|( )
decl_stmt|;
name|assertFalse
argument_list|(
name|groupHandler
operator|.
name|hasRepository
argument_list|(
literal|"test-group-01"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|instances
operator|.
name|containsKey
argument_list|(
literal|"test-group-01"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RepositoryState
operator|.
name|REFERENCES_SET
argument_list|,
name|instances
operator|.
name|get
argument_list|(
literal|"test-group-01"
argument_list|)
operator|.
name|getLastState
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|newInstance
parameter_list|( )
throws|throws
name|RepositoryException
block|{
name|String
name|id
init|=
literal|"test-group-03"
decl_stmt|;
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|RepositoryGroup
name|instance
init|=
name|groupHandler
operator|.
name|newInstance
argument_list|(
name|RepositoryType
operator|.
name|MAVEN
argument_list|,
name|id
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|instance
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|instance
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|groupHandler
operator|.
name|hasRepository
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|RepositoryState
operator|.
name|REFERENCES_SET
argument_list|,
name|instance
operator|.
name|getLastState
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasGroupInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|put
parameter_list|( )
throws|throws
name|IOException
throws|,
name|RepositoryException
block|{
specifier|final
name|String
name|id
init|=
literal|"test-group-04"
decl_stmt|;
try|try
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|EditableRepositoryGroup
name|repositoryGroup
init|=
name|createRepository
argument_list|(
name|id
argument_list|,
literal|"n-"
operator|+
name|id
argument_list|)
decl_stmt|;
name|groupHandler
operator|.
name|put
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
name|RepositoryGroup
name|storedGroup
init|=
name|groupHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|storedGroup
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|storedGroup
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"n-"
operator|+
name|id
argument_list|,
name|storedGroup
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|EditableRepositoryGroup
name|repositoryGroup2
init|=
name|createRepository
argument_list|(
name|id
argument_list|,
literal|"n2-"
operator|+
name|id
argument_list|)
decl_stmt|;
name|groupHandler
operator|.
name|put
argument_list|(
name|repositoryGroup2
argument_list|)
expr_stmt|;
name|storedGroup
operator|=
name|groupHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|storedGroup
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|storedGroup
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"n2-"
operator|+
name|id
argument_list|,
name|storedGroup
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasGroupInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|removeGroupFromConfig
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
name|void
name|putWithConfiguration
parameter_list|( )
throws|throws
name|RepositoryException
block|{
name|String
name|id
init|=
literal|"test-group-05"
decl_stmt|;
try|try
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|RepositoryGroupConfiguration
name|configuration
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setName
argument_list|(
literal|"n-"
operator|+
name|id
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|repos
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|( )
decl_stmt|;
name|repos
operator|.
name|add
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRepositories
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|groupHandler
operator|.
name|put
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|RepositoryGroup
name|repo
init|=
name|groupHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|repo
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"n-"
operator|+
name|id
argument_list|,
name|repo
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repo
operator|.
name|getRepositories
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|repo
operator|.
name|getRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"internal"
argument_list|,
name|repo
operator|.
name|getRepositories
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasGroupInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|removeGroupFromConfig
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
name|void
name|testPutWithoutRegister
parameter_list|( )
throws|throws
name|RepositoryException
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|Configuration
name|aCfg
init|=
operator|new
name|Configuration
argument_list|( )
decl_stmt|;
name|RepositoryGroupConfiguration
name|configuration
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|( )
decl_stmt|;
specifier|final
name|String
name|id
init|=
literal|"test-group-06"
decl_stmt|;
name|configuration
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setName
argument_list|(
literal|"n-"
operator|+
name|id
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|repos
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|( )
decl_stmt|;
name|repos
operator|.
name|add
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRepositories
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|groupHandler
operator|.
name|put
argument_list|(
name|configuration
argument_list|,
name|aCfg
argument_list|)
expr_stmt|;
name|RepositoryGroup
name|repo
init|=
name|groupHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasGroupInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aCfg
operator|.
name|getRepositoryGroups
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|anyMatch
argument_list|(
name|g
lambda|->
name|g
operator|!=
literal|null
operator|&&
name|id
operator|.
name|equals
argument_list|(
name|g
operator|.
name|getId
argument_list|( )
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|putWithCheck_invalid
parameter_list|( )
throws|throws
name|RepositoryException
block|{
specifier|final
name|String
name|id
init|=
literal|"test-group-07"
decl_stmt|;
specifier|final
name|String
name|name
init|=
literal|"n-"
operator|+
name|id
decl_stmt|;
try|try
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|BasicRepositoryGroupValidator
name|checker
init|=
operator|new
name|BasicRepositoryGroupValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|RepositoryGroupConfiguration
name|configuration
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setId
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|repos
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|( )
decl_stmt|;
name|repos
operator|.
name|add
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRepositories
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|CheckedResult
argument_list|<
name|RepositoryGroup
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
argument_list|>
argument_list|>
name|result
init|=
name|groupHandler
operator|.
name|putWithCheck
argument_list|(
name|configuration
argument_list|,
name|checker
argument_list|)
decl_stmt|;
name|assertNull
argument_list|(
name|groupHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getResult
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ISEMPTY
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"id"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getType
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasGroupInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasGroupInConfig
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|removeGroupFromConfig
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
name|void
name|remove
parameter_list|( )
throws|throws
name|RepositoryException
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|RepositoryGroupConfiguration
name|configuration
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|( )
decl_stmt|;
specifier|final
name|String
name|id
init|=
literal|"test-group-08"
decl_stmt|;
name|configuration
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setName
argument_list|(
literal|"n-"
operator|+
name|id
argument_list|)
expr_stmt|;
name|groupHandler
operator|.
name|put
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasGroupInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|groupHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|groupHandler
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|groupHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasGroupInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|removeWithoutSave
parameter_list|( )
throws|throws
name|RepositoryException
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|Configuration
name|aCfg
init|=
operator|new
name|Configuration
argument_list|( )
decl_stmt|;
name|RepositoryGroupConfiguration
name|configuration
init|=
operator|new
name|RepositoryGroupConfiguration
argument_list|( )
decl_stmt|;
specifier|final
name|String
name|id
init|=
literal|"test-group-09"
decl_stmt|;
name|configuration
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setName
argument_list|(
literal|"n-"
operator|+
name|id
argument_list|)
expr_stmt|;
name|ArrayList
argument_list|<
name|String
argument_list|>
name|repos
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|( )
decl_stmt|;
name|repos
operator|.
name|add
argument_list|(
literal|"internal"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setRepositories
argument_list|(
name|repos
argument_list|)
expr_stmt|;
name|groupHandler
operator|.
name|put
argument_list|(
name|configuration
argument_list|,
name|aCfg
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aCfg
operator|.
name|getRepositoryGroups
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|anyMatch
argument_list|(
name|g
lambda|->
name|g
operator|!=
literal|null
operator|&&
name|id
operator|.
name|equals
argument_list|(
name|g
operator|.
name|getId
argument_list|( )
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|groupHandler
operator|.
name|remove
argument_list|(
name|id
argument_list|,
name|aCfg
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|groupHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aCfg
operator|.
name|getRepositoryGroups
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|noneMatch
argument_list|(
name|g
lambda|->
name|g
operator|!=
literal|null
operator|&&
name|id
operator|.
name|equals
argument_list|(
name|g
operator|.
name|getId
argument_list|( )
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|groupHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|validateRepository
parameter_list|( )
throws|throws
name|IOException
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
specifier|final
name|String
name|id
init|=
literal|"test-group-10"
decl_stmt|;
name|EditableRepositoryGroup
name|repositoryGroup
init|=
name|createRepository
argument_list|(
name|id
argument_list|,
literal|"n-"
operator|+
name|id
argument_list|)
decl_stmt|;
name|repositoryGroup
operator|.
name|setMergedIndexTTL
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|CheckedResult
argument_list|<
name|RepositoryGroup
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
argument_list|>
argument_list|>
name|result
init|=
name|groupHandler
operator|.
name|validateRepository
argument_list|(
name|repositoryGroup
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|repositoryGroup
operator|=
name|createRepository
argument_list|(
name|id
argument_list|,
literal|"n-test-group-10###"
argument_list|)
expr_stmt|;
name|result
operator|=
name|groupHandler
operator|.
name|validateRepository
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getResult
argument_list|()
operator|.
name|get
argument_list|(
literal|"merged_index_ttl"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|result
operator|.
name|getResult
argument_list|()
operator|.
name|get
argument_list|(
literal|"name"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|validateRepositoryIfExisting
parameter_list|( )
throws|throws
name|IOException
throws|,
name|RepositoryException
block|{
specifier|final
name|String
name|id
init|=
literal|"test-group-11"
decl_stmt|;
try|try
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|EditableRepositoryGroup
name|repositoryGroup
init|=
name|createRepository
argument_list|(
name|id
argument_list|,
literal|"n-"
operator|+
name|id
argument_list|)
decl_stmt|;
name|repositoryGroup
operator|.
name|setMergedIndexTTL
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|groupHandler
operator|.
name|put
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
name|CheckedResult
argument_list|<
name|RepositoryGroup
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
argument_list|>
argument_list|>
name|result
init|=
name|groupHandler
operator|.
name|validateRepository
argument_list|(
name|repositoryGroup
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|removeGroupFromConfig
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
name|void
name|validateRepositoryForUpdate
parameter_list|( )
throws|throws
name|IOException
throws|,
name|RepositoryException
block|{
specifier|final
name|String
name|id
init|=
literal|"test-group-12"
decl_stmt|;
try|try
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|EditableRepositoryGroup
name|repositoryGroup
init|=
name|createRepository
argument_list|(
name|id
argument_list|,
literal|"n-"
operator|+
name|id
argument_list|)
decl_stmt|;
name|repositoryGroup
operator|.
name|setMergedIndexTTL
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|groupHandler
operator|.
name|put
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
name|CheckedResult
argument_list|<
name|RepositoryGroup
argument_list|,
name|Map
argument_list|<
name|String
argument_list|,
name|List
argument_list|<
name|ValidationError
argument_list|>
argument_list|>
argument_list|>
name|result
init|=
name|groupHandler
operator|.
name|validateRepositoryForUpdate
argument_list|(
name|repositoryGroup
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|removeGroupFromConfig
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
name|void
name|has
parameter_list|( )
throws|throws
name|IOException
throws|,
name|RepositoryException
block|{
specifier|final
name|String
name|id
init|=
literal|"test-group-13"
decl_stmt|;
try|try
block|{
name|RepositoryGroupHandler
name|groupHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|EditableRepositoryGroup
name|repositoryGroup
init|=
name|createRepository
argument_list|(
name|id
argument_list|,
literal|"n-"
operator|+
name|id
argument_list|)
decl_stmt|;
name|repositoryGroup
operator|.
name|setMergedIndexTTL
argument_list|(
literal|5
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|groupHandler
operator|.
name|hasRepository
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|groupHandler
operator|.
name|put
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|groupHandler
operator|.
name|hasRepository
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|removeGroupFromConfig
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

