begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
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
name|model
operator|.
name|ArtifactReference
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
name|CachedFailuresPolicy
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
name|ChecksumPolicy
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
name|ReleasesPolicy
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
name|SnapshotsPolicy
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
name|maven
operator|.
name|wagon
operator|.
name|ResourceDoesNotExistException
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
name|java
operator|.
name|io
operator|.
name|File
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertFalse
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|assertNotNull
import|;
end_import

begin_comment
comment|/**  * CacheFailuresTransferTest  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|CacheFailuresTransferTest
extends|extends
name|AbstractProxyTestCase
block|{
comment|// TODO: test some hard failures (eg TransferFailedException)
comment|// TODO: test the various combinations of fetchFrom* (note: need only test when caching is enabled)
annotation|@
name|Test
specifier|public
name|void
name|testGetWithCacheFailuresOn
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-second-proxy/1.0/get-in-second-proxy-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|managedDefaultDir
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|setupTestableManagedRepository
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
name|ArtifactReference
name|artifact
init|=
name|managedDefaultRepository
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// Configure Repository (usually done within archiva.xml configuration)
name|saveRemoteRepositoryConfig
argument_list|(
literal|"badproxied1"
argument_list|,
literal|"Bad Proxied 1"
argument_list|,
literal|"test://bad.machine.com/repo/"
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|saveRemoteRepositoryConfig
argument_list|(
literal|"badproxied2"
argument_list|,
literal|"Bad Proxied 2"
argument_list|,
literal|"test://bad.machine.com/anotherrepo/"
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
literal|"badproxied1"
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|ReleasesPolicy
operator|.
name|ALWAYS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|CachedFailuresPolicy
operator|.
name|YES
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
literal|"badproxied2"
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|ReleasesPolicy
operator|.
name|ALWAYS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|CachedFailuresPolicy
operator|.
name|YES
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|wagonMock
operator|.
name|get
argument_list|(
name|path
argument_list|,
operator|new
name|File
argument_list|(
name|expectedFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|expectedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".tmp"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setMatcher
argument_list|(
name|customWagonGetMatcher
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
operator|new
name|ResourceDoesNotExistException
argument_list|(
literal|"resource does not exist."
argument_list|)
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|File
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|wagonMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
comment|// Second attempt to download same artifact use cache
name|wagonMockControl
operator|.
name|reset
argument_list|()
expr_stmt|;
name|wagonMockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|downloadedFile
operator|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertNotDownloaded
argument_list|(
name|downloadedFile
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetWithCacheFailuresOff
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-second-proxy/1.0/get-in-second-proxy-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|managedDefaultDir
operator|.
name|getAbsoluteFile
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|setupTestableManagedRepository
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
name|ArtifactReference
name|artifact
init|=
name|managedDefaultRepository
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// Configure Repository (usually done within archiva.xml configuration)
name|saveRemoteRepositoryConfig
argument_list|(
literal|"badproxied1"
argument_list|,
literal|"Bad Proxied 1"
argument_list|,
literal|"test://bad.machine.com/repo/"
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
name|saveRemoteRepositoryConfig
argument_list|(
literal|"badproxied2"
argument_list|,
literal|"Bad Proxied 2"
argument_list|,
literal|"test://bad.machine.com/anotherrepo/"
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
literal|"badproxied1"
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|ReleasesPolicy
operator|.
name|ALWAYS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|CachedFailuresPolicy
operator|.
name|NO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
literal|"badproxied2"
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|ReleasesPolicy
operator|.
name|ALWAYS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|CachedFailuresPolicy
operator|.
name|NO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|wagonMock
operator|.
name|get
argument_list|(
name|path
argument_list|,
operator|new
name|File
argument_list|(
name|expectedFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|expectedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".tmp"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setMatcher
argument_list|(
name|customWagonGetMatcher
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
operator|new
name|ResourceDoesNotExistException
argument_list|(
literal|"resource does not exist."
argument_list|)
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|File
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|wagonMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
comment|// Second attempt to download same artifact DOES NOT use cache
name|wagonMockControl
operator|.
name|reset
argument_list|()
expr_stmt|;
name|wagonMock
operator|.
name|get
argument_list|(
name|path
argument_list|,
operator|new
name|File
argument_list|(
name|expectedFile
operator|.
name|getParentFile
argument_list|()
argument_list|,
name|expectedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".tmp"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setMatcher
argument_list|(
name|customWagonGetMatcher
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
operator|new
name|ResourceDoesNotExistException
argument_list|(
literal|"resource does not exist."
argument_list|)
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|downloadedFile
operator|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
argument_list|,
name|artifact
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertNotDownloaded
argument_list|(
name|downloadedFile
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetWhenInBothProxiedButFirstCacheFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-second-proxy/1.0/get-in-second-proxy-1.0.jar"
decl_stmt|;
name|setupTestableManagedRepository
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|managedDefaultDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|ArtifactReference
name|artifact
init|=
name|managedDefaultRepository
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|expectedFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|url
init|=
name|PathUtil
operator|.
name|toUrl
argument_list|(
name|REPOPATH_PROXIED1
operator|+
literal|"/"
operator|+
name|path
argument_list|)
decl_stmt|;
comment|// Intentionally set failure on url in proxied1 (for test)
name|UrlFailureCache
name|failurlCache
init|=
name|lookupUrlFailureCache
argument_list|()
decl_stmt|;
name|failurlCache
operator|.
name|cacheFailure
argument_list|(
name|url
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
literal|"proxied1"
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|ReleasesPolicy
operator|.
name|ALWAYS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|CachedFailuresPolicy
operator|.
name|YES
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
literal|"proxied2"
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|ReleasesPolicy
operator|.
name|ALWAYS
argument_list|,
name|SnapshotsPolicy
operator|.
name|ALWAYS
argument_list|,
name|CachedFailuresPolicy
operator|.
name|YES
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|File
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
comment|// Validate that file actually came from proxied2 (as intended).
name|File
name|proxied2File
init|=
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED2
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertFileEquals
argument_list|(
name|expectedFile
argument_list|,
name|downloadedFile
argument_list|,
name|proxied2File
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Inject
name|UrlFailureCache
name|urlFailureCache
decl_stmt|;
specifier|protected
name|UrlFailureCache
name|lookupUrlFailureCache
parameter_list|()
throws|throws
name|Exception
block|{
comment|//UrlFailureCache urlFailureCache = (UrlFailureCache) lookup( "urlFailureCache" );
name|assertNotNull
argument_list|(
literal|"URL Failure Cache cannot be null."
argument_list|,
name|urlFailureCache
argument_list|)
expr_stmt|;
return|return
name|urlFailureCache
return|;
block|}
block|}
end_class

end_unit

