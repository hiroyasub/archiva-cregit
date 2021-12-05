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
name|repository
operator|.
name|EditableManagedRepository
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
name|RepositoryRegistry
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
name|base
operator|.
name|managed
operator|.
name|BasicManagedRepository
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
name|RepositoryHandlerDependencies
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
name|mock
operator|.
name|ManagedRepositoryContentMock
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
name|ValidationResponse
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
name|AfterEach
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
name|net
operator|.
name|URISyntaxException
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
name|SpringExtension
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
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
class|class
name|BasicRepositoryGroupValidatorTest
block|{
annotation|@
name|Inject
name|ConfigurationHandler
name|configurationHandler
decl_stmt|;
annotation|@
name|Inject
name|RepositoryRegistry
name|repositoryRegistry
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|Inject
name|RepositoryGroupHandler
name|repositoryGroupHandler
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unused"
argument_list|)
annotation|@
name|Inject
name|RepositoryHandlerDependencies
name|repositoryHandlerDependencies
decl_stmt|;
name|Path
name|repoBaseDir
decl_stmt|;
annotation|@
name|AfterEach
name|void
name|cleanup
parameter_list|()
throws|throws
name|RepositoryException
block|{
name|repositoryRegistry
operator|.
name|removeRepository
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|EditableManagedRepository
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
name|BasicManagedRepository
name|repo
init|=
name|BasicManagedRepository
operator|.
name|newFilesystemInstance
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|location
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
name|repo
operator|.
name|setContent
argument_list|(
operator|new
name|ManagedRepositoryContentMock
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
specifier|protected
name|EditableRepositoryGroup
name|createGroup
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
return|return
name|BasicRepositoryGroup
operator|.
name|newFilesystemInstance
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|getRepoBaseDir
argument_list|( )
operator|.
name|resolve
argument_list|(
name|id
argument_list|)
argument_list|)
return|;
block|}
specifier|private
name|Path
name|getRepoBaseDir
parameter_list|()
block|{
if|if
condition|(
name|repoBaseDir
operator|==
literal|null
condition|)
block|{
try|try
block|{
name|repoBaseDir
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|Thread
operator|.
name|currentThread
argument_list|( )
operator|.
name|getContextClassLoader
argument_list|( )
operator|.
name|getResource
argument_list|(
literal|"repositories"
argument_list|)
operator|.
name|toURI
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Could not retrieve repository base directory"
argument_list|)
throw|;
block|}
block|}
return|return
name|repoBaseDir
return|;
block|}
annotation|@
name|Test
name|void
name|apply
parameter_list|( )
throws|throws
name|IOException
block|{
name|BasicRepositoryGroupValidator
name|validator
init|=
operator|new
name|BasicRepositoryGroupValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|EditableRepositoryGroup
name|group
init|=
name|createGroup
argument_list|(
literal|"test"
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|group
operator|.
name|setMergedIndexTTL
argument_list|(
literal|360
argument_list|)
expr_stmt|;
name|ValidationResponse
argument_list|<
name|RepositoryGroup
argument_list|>
name|result
init|=
name|validator
operator|.
name|apply
argument_list|(
name|group
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|isValid
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|applyWithExisting
parameter_list|( )
throws|throws
name|IOException
throws|,
name|RepositoryException
block|{
name|BasicRepositoryGroupValidator
name|validator
init|=
operator|new
name|BasicRepositoryGroupValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|validator
operator|.
name|setRepositoryRegistry
argument_list|(
name|repositoryRegistry
argument_list|)
expr_stmt|;
name|EditableRepositoryGroup
name|group
init|=
name|createGroup
argument_list|(
literal|"test"
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|group
operator|.
name|setMergedIndexTTL
argument_list|(
literal|360
argument_list|)
expr_stmt|;
name|repositoryRegistry
operator|.
name|putRepositoryGroup
argument_list|(
name|group
argument_list|)
expr_stmt|;
name|EditableRepositoryGroup
name|group2
init|=
name|createGroup
argument_list|(
literal|"test"
argument_list|,
literal|"test2"
argument_list|)
decl_stmt|;
name|group2
operator|.
name|setMergedIndexTTL
argument_list|(
literal|360
argument_list|)
expr_stmt|;
name|ValidationResponse
argument_list|<
name|RepositoryGroup
argument_list|>
name|result
init|=
name|validator
operator|.
name|apply
argument_list|(
name|group2
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|isValid
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"group_exists"
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
block|}
annotation|@
name|Test
name|void
name|applyWithBadTTL
parameter_list|( )
throws|throws
name|IOException
block|{
name|BasicRepositoryGroupValidator
name|validator
init|=
operator|new
name|BasicRepositoryGroupValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|EditableRepositoryGroup
name|group
init|=
name|createGroup
argument_list|(
literal|"test"
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|group
operator|.
name|setMergedIndexTTL
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ValidationResponse
argument_list|<
name|RepositoryGroup
argument_list|>
name|result
init|=
name|validator
operator|.
name|apply
argument_list|(
name|group
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|isValid
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|containsKey
argument_list|(
literal|"merged_index_ttl"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"repository_group"
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"merged_index_ttl"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCategory
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"min"
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"merged_index_ttl"
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
name|assertEquals
argument_list|(
literal|"merged_index_ttl"
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"merged_index_ttl"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|applyWithNullObject
parameter_list|( )
throws|throws
name|IOException
block|{
name|BasicRepositoryGroupValidator
name|validator
init|=
operator|new
name|BasicRepositoryGroupValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|EditableRepositoryGroup
name|group
init|=
name|createGroup
argument_list|(
literal|""
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|group
operator|.
name|setMergedIndexTTL
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ValidationResponse
argument_list|<
name|RepositoryGroup
argument_list|>
name|result
init|=
name|validator
operator|.
name|apply
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|isValid
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|containsKey
argument_list|(
literal|"object"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"repository_group"
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"object"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getCategory
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"isnull"
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"object"
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
name|assertEquals
argument_list|(
literal|"object"
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"object"
argument_list|)
operator|.
name|get
argument_list|(
literal|0
argument_list|)
operator|.
name|getAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|applyWithEmptyId
parameter_list|( )
throws|throws
name|IOException
block|{
name|BasicRepositoryGroupValidator
name|validator
init|=
operator|new
name|BasicRepositoryGroupValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|EditableRepositoryGroup
name|group
init|=
name|createGroup
argument_list|(
literal|""
argument_list|,
literal|"test"
argument_list|)
decl_stmt|;
name|group
operator|.
name|setMergedIndexTTL
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|ValidationResponse
argument_list|<
name|RepositoryGroup
argument_list|>
name|result
init|=
name|validator
operator|.
name|apply
argument_list|(
name|group
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|result
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|isValid
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|containsKey
argument_list|(
literal|"id"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"repository_group"
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
name|getCategory
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"empty"
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
name|assertEquals
argument_list|(
literal|"id"
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
name|getAttribute
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|applyWithBadName
parameter_list|( )
throws|throws
name|IOException
block|{
name|BasicRepositoryGroupValidator
name|validator
init|=
operator|new
name|BasicRepositoryGroupValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|validator
operator|.
name|setRepositoryRegistry
argument_list|(
name|repositoryRegistry
argument_list|)
expr_stmt|;
name|EditableRepositoryGroup
name|group
init|=
name|createGroup
argument_list|(
literal|"test"
argument_list|,
literal|"badtest\\name"
argument_list|)
decl_stmt|;
name|group
operator|.
name|setMergedIndexTTL
argument_list|(
literal|360
argument_list|)
expr_stmt|;
name|ValidationResponse
argument_list|<
name|RepositoryGroup
argument_list|>
name|result
init|=
name|validator
operator|.
name|apply
argument_list|(
name|group
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|result
operator|.
name|isValid
argument_list|( )
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
name|assertEquals
argument_list|(
literal|"invalid_chars"
argument_list|,
name|result
operator|.
name|getResult
argument_list|( )
operator|.
name|get
argument_list|(
literal|"name"
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
block|}
annotation|@
name|Test
name|void
name|getFlavour
parameter_list|( )
block|{
name|BasicRepositoryGroupValidator
name|validator
init|=
operator|new
name|BasicRepositoryGroupValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|RepositoryGroup
operator|.
name|class
argument_list|,
name|validator
operator|.
name|getFlavour
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
name|void
name|isFlavour
parameter_list|()
block|{
name|BasicRepositoryGroupValidator
name|validator
init|=
operator|new
name|BasicRepositoryGroupValidator
argument_list|(
name|configurationHandler
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|validator
operator|.
name|isFlavour
argument_list|(
name|RepositoryGroup
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|validator
operator|.
name|isFlavour
argument_list|(
name|ManagedRepository
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|validator
operator|.
name|isFlavour
argument_list|(
name|BasicRepositoryGroup
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit
