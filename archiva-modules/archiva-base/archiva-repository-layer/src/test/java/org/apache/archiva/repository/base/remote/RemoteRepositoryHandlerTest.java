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
name|remote
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
name|configuration
operator|.
name|provider
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
name|model
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
name|model
operator|.
name|RemoteRepositoryConfiguration
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
name|IndexManagerFactory
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
name|EditableRemoteRepository
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
name|RemoteRepository
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
name|RepositoryContentFactory
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
literal|"classpath:/spring-context-remote.xml"
block|}
argument_list|)
class|class
name|RemoteRepositoryHandlerTest
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
annotation|@
name|Named
argument_list|(
literal|"repositoryContentFactory#default"
argument_list|)
name|RepositoryContentFactory
name|repositoryContentFactory
decl_stmt|;
annotation|@
name|Inject
name|IndexManagerFactory
name|indexManagerFactory
decl_stmt|;
annotation|@
name|Inject
name|ConfigurationHandler
name|configurationHandler
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
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
literal|"src/test/resources/archiva-remote.xml"
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
literal|"target/test-classes/archiva-remote.xml"
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
comment|// Helper method that removes a repo from the configuration
specifier|private
name|void
name|removeRepositoryFromConfig
parameter_list|(
name|String
name|id
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
name|RemoteRepositoryConfiguration
argument_list|>
name|iter
init|=
name|configuration
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|iter
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|RemoteRepositoryConfiguration
name|repo
init|=
name|iter
operator|.
name|next
argument_list|( )
decl_stmt|;
if|if
condition|(
name|id
operator|.
name|equals
argument_list|(
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|iter
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
literal|"Could not remove repo from config "
operator|+
name|id
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|boolean
name|hasRepositoryInConfig
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|assertNotNull
argument_list|(
name|configurationHandler
operator|.
name|getBaseConfiguration
argument_list|( )
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|configurationHandler
operator|.
name|getBaseConfiguration
argument_list|( )
operator|.
name|getRemoteRepositories
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
return|;
block|}
specifier|private
name|RemoteRepositoryHandler
name|createHandler
parameter_list|( )
block|{
name|RemoteRepositoryHandler
name|repositoryHandler
init|=
operator|new
name|RemoteRepositoryHandler
argument_list|(
name|repositoryRegistry
argument_list|,
name|configurationHandler
argument_list|,
name|indexManagerFactory
argument_list|,
name|repositoryContentFactory
argument_list|)
decl_stmt|;
name|repositoryHandler
operator|.
name|init
argument_list|( )
expr_stmt|;
return|return
name|repositoryHandler
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
literal|"remote"
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
name|EditableRemoteRepository
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
name|BasicRemoteRepository
name|repo
init|=
operator|new
name|BasicRemoteRepository
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
name|EditableRemoteRepository
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
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|repoHandler
operator|.
name|initializeFromConfig
argument_list|( )
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|repoHandler
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
name|repoHandler
operator|.
name|get
argument_list|(
literal|"test-repo-01"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Test Remote Repository"
argument_list|,
name|repoHandler
operator|.
name|get
argument_list|(
literal|"test-repo-01"
argument_list|)
operator|.
name|getName
argument_list|()
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
literal|"test-repo-02"
decl_stmt|;
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|RemoteRepository
name|repo
init|=
name|repoHandler
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
name|repoHandler
operator|.
name|activateRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasRepositoryInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|repoHandler
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
name|newInstancesFromConfig
parameter_list|( )
throws|throws
name|RepositoryException
block|{
specifier|final
name|String
name|id
init|=
literal|"test-repo-01"
decl_stmt|;
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|( )
decl_stmt|;
name|repoHandler
operator|.
name|remove
argument_list|(
literal|"test-repo-01"
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|RemoteRepository
argument_list|>
name|instances
init|=
name|repoHandler
operator|.
name|newInstancesFromConfig
argument_list|( )
decl_stmt|;
name|assertFalse
argument_list|(
name|repoHandler
operator|.
name|hasRepository
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|instances
operator|.
name|containsKey
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
name|instances
operator|.
name|get
argument_list|(
name|id
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
literal|"test-repo-03"
decl_stmt|;
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|RemoteRepository
name|instance
init|=
name|repoHandler
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
name|repoHandler
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
name|hasRepositoryInConfig
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
literal|"test-repo-04"
decl_stmt|;
try|try
block|{
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|EditableRemoteRepository
name|repository
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
name|repoHandler
operator|.
name|put
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|RemoteRepository
name|storedRepository
init|=
name|repoHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|storedRepository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|storedRepository
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
name|storedRepository
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|EditableRemoteRepository
name|repository2
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
name|repoHandler
operator|.
name|put
argument_list|(
name|repository2
argument_list|)
expr_stmt|;
name|storedRepository
operator|=
name|repoHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|storedRepository
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|id
argument_list|,
name|storedRepository
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
name|storedRepository
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasRepositoryInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|removeRepositoryFromConfig
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
literal|"test-repo-05"
decl_stmt|;
try|try
block|{
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|RemoteRepositoryConfiguration
name|configuration
init|=
operator|new
name|RemoteRepositoryConfiguration
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
name|repoHandler
operator|.
name|put
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|RemoteRepository
name|repo
init|=
name|repoHandler
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
name|assertTrue
argument_list|(
name|hasRepositoryInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|removeRepositoryFromConfig
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
specifier|final
name|String
name|id
init|=
literal|"test-repo-06"
decl_stmt|;
name|RemoteRepositoryHandler
name|repoHandler
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
name|RemoteRepositoryConfiguration
name|configuration
init|=
operator|new
name|RemoteRepositoryConfiguration
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
name|repoHandler
operator|.
name|put
argument_list|(
name|configuration
argument_list|,
name|aCfg
argument_list|)
expr_stmt|;
name|RemoteRepository
name|repo
init|=
name|repoHandler
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
name|hasRepositoryInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|aCfg
operator|.
name|getRemoteRepositories
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
literal|"test-repo-07"
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
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|BasicRemoteRepositoryValidator
name|checker
init|=
operator|new
name|BasicRemoteRepositoryValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|checker
operator|.
name|setRepositoryRegistry
argument_list|(
name|repositoryRegistry
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
name|configuration
init|=
operator|new
name|RemoteRepositoryConfiguration
argument_list|()
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
name|CheckedResult
argument_list|<
name|RemoteRepository
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
name|repoHandler
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
name|repoHandler
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
literal|2
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
name|hasRepositoryInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasRepositoryInConfig
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|removeRepositoryFromConfig
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
specifier|final
name|String
name|id
init|=
literal|"test-repo-08"
decl_stmt|;
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|RemoteRepositoryConfiguration
name|configuration
init|=
operator|new
name|RemoteRepositoryConfiguration
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
name|repoHandler
operator|.
name|put
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|hasRepositoryInConfig
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|repoHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|repoHandler
operator|.
name|remove
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|repoHandler
operator|.
name|get
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|hasRepositoryInConfig
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
specifier|final
name|String
name|id
init|=
literal|"test-repo-09"
decl_stmt|;
name|RemoteRepositoryHandler
name|repoHandler
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
name|RemoteRepositoryConfiguration
name|configuration
init|=
operator|new
name|RemoteRepositoryConfiguration
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
name|repoHandler
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
name|getRemoteRepositories
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
name|repoHandler
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
name|repoHandler
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
name|getRemoteRepositories
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
name|repoHandler
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
specifier|final
name|String
name|id
init|=
literal|"test-repo-10"
decl_stmt|;
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|EditableRemoteRepository
name|repository
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
name|CheckedResult
argument_list|<
name|RemoteRepository
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
name|repoHandler
operator|.
name|validateRepository
argument_list|(
name|repository
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
name|repository
operator|=
name|createRepository
argument_list|(
name|id
argument_list|,
literal|"n-test-repo-10###"
argument_list|)
expr_stmt|;
name|result
operator|=
name|repoHandler
operator|.
name|validateRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
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
literal|"test-repo-11"
decl_stmt|;
try|try
block|{
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|EditableRemoteRepository
name|repository
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
name|repoHandler
operator|.
name|put
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|CheckedResult
argument_list|<
name|RemoteRepository
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
name|repoHandler
operator|.
name|validateRepository
argument_list|(
name|repository
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
name|removeRepositoryFromConfig
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
literal|"test-repo-12"
decl_stmt|;
try|try
block|{
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|EditableRemoteRepository
name|repository
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
name|repoHandler
operator|.
name|put
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|CheckedResult
argument_list|<
name|RemoteRepository
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
name|repoHandler
operator|.
name|validateRepositoryForUpdate
argument_list|(
name|repository
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
name|removeRepositoryFromConfig
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
literal|"test-repo-13"
decl_stmt|;
try|try
block|{
name|RemoteRepositoryHandler
name|repoHandler
init|=
name|createHandler
argument_list|( )
decl_stmt|;
name|EditableRemoteRepository
name|repository
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
name|assertFalse
argument_list|(
name|repoHandler
operator|.
name|hasRepository
argument_list|(
name|id
argument_list|)
argument_list|)
expr_stmt|;
name|repoHandler
operator|.
name|put
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoHandler
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
name|removeRepositoryFromConfig
argument_list|(
name|id
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

