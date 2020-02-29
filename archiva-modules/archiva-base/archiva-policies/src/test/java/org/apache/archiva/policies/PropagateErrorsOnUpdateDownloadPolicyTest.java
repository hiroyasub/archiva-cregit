begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|policies
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|policies
operator|.
name|urlcache
operator|.
name|UrlFailureCache
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
name|storage
operator|.
name|StorageAsset
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
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * CachedFailuresPolicyTest  *  *  */
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
name|PropagateErrorsOnUpdateDownloadPolicyTest
extends|extends
name|TestCase
block|{
annotation|@
name|Inject
specifier|private
name|UrlFailureCache
name|urlFailureCache
decl_stmt|;
specifier|private
name|FilesystemStorage
name|filesystemStorage
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"downloadErrorPolicy#propagate-errors-on-update"
argument_list|)
name|DownloadErrorPolicy
name|downloadPolicy
decl_stmt|;
specifier|private
name|DownloadErrorPolicy
name|lookupPolicy
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|downloadPolicy
return|;
block|}
specifier|private
name|StorageAsset
name|getFile
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|filesystemStorage
operator|==
literal|null
condition|)
block|{
name|filesystemStorage
operator|=
operator|new
name|FilesystemStorage
argument_list|(
name|Paths
operator|.
name|get
argument_list|(
literal|"target/cache-failures"
argument_list|)
argument_list|,
operator|new
name|DefaultFileLockManager
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|filesystemStorage
operator|.
name|getAsset
argument_list|(
name|getName
argument_list|()
operator|+
literal|".txt"
argument_list|)
return|;
block|}
specifier|private
name|Properties
name|createRequest
parameter_list|()
block|{
name|Properties
name|request
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
return|return
name|request
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPolicyStop
parameter_list|()
throws|throws
name|Exception
block|{
name|DownloadErrorPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|StorageAsset
name|localFile
init|=
name|getFile
argument_list|()
decl_stmt|;
name|Properties
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
name|Exception
name|ex
init|=
operator|new
name|RuntimeException
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Exception
argument_list|>
name|exMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|applyPolicy
argument_list|(
name|PropagateErrorsOnUpdateDownloadPolicy
operator|.
name|ALWAYS
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|,
name|ex
argument_list|,
name|exMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testPolicyQueue
parameter_list|()
throws|throws
name|Exception
block|{
name|DownloadErrorPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|StorageAsset
name|localFile
init|=
name|getFile
argument_list|()
decl_stmt|;
name|Properties
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
name|Exception
name|ex
init|=
operator|new
name|RuntimeException
argument_list|()
decl_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|Exception
argument_list|>
name|exMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
decl_stmt|;
if|if
condition|(
name|localFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|localFile
operator|.
name|getStorage
argument_list|()
operator|.
name|removeAsset
argument_list|(
name|localFile
argument_list|)
expr_stmt|;
block|}
name|assertTrue
argument_list|(
name|policy
operator|.
name|applyPolicy
argument_list|(
name|PropagateErrorsOnUpdateDownloadPolicy
operator|.
name|NOT_PRESENT
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|,
name|ex
argument_list|,
name|exMap
argument_list|)
argument_list|)
expr_stmt|;
name|localFile
operator|.
name|create
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|policy
operator|.
name|applyPolicy
argument_list|(
name|PropagateErrorsOnUpdateDownloadPolicy
operator|.
name|NOT_PRESENT
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|,
name|ex
argument_list|,
name|exMap
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testNamesAndDescriptions
parameter_list|()
throws|throws
name|Exception
block|{
name|DownloadErrorPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Propagate Errors on Update Policy"
argument_list|,
name|policy
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|getDescription
argument_list|(
name|Locale
operator|.
name|US
argument_list|)
operator|.
name|contains
argument_list|(
literal|"during download of an artifact that exists already"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Propagate always"
argument_list|,
name|policy
operator|.
name|getOptionName
argument_list|(
name|Locale
operator|.
name|US
argument_list|,
name|DownloadErrorOption
operator|.
name|ALWAYS
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Propagate only, if not exists"
argument_list|,
name|policy
operator|.
name|getOptionName
argument_list|(
name|Locale
operator|.
name|US
argument_list|,
name|DownloadErrorOption
operator|.
name|NOT_PRESENT
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|getOptionDescription
argument_list|(
name|Locale
operator|.
name|US
argument_list|,
name|DownloadErrorOption
operator|.
name|ALWAYS
argument_list|)
operator|.
name|contains
argument_list|(
literal|"even if the file exists"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|policy
operator|.
name|getOptionDescription
argument_list|(
name|Locale
operator|.
name|US
argument_list|,
name|DownloadErrorOption
operator|.
name|NOT_PRESENT
argument_list|)
operator|.
name|contains
argument_list|(
literal|"if the file does not exist"
argument_list|)
argument_list|)
expr_stmt|;
try|try
block|{
name|policy
operator|.
name|getOptionName
argument_list|(
name|Locale
operator|.
name|US
argument_list|,
name|StandardOption
operator|.
name|NOOP
argument_list|)
expr_stmt|;
comment|// Exception should be thrown
name|assertTrue
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MissingResourceException
name|e
parameter_list|)
block|{
comment|//
block|}
block|}
block|}
end_class

end_unit

