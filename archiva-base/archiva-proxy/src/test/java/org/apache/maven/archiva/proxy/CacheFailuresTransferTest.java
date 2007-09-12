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
name|maven
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
name|maven
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
name|maven
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
name|maven
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
name|maven
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
name|maven
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
name|maven
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
name|TransferFailedException
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
comment|/**  * CacheFailuresTransferTest  *  * @author Brett Porter  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|CacheFailuresTransferTest
extends|extends
name|AbstractProxyTestCase
block|{
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
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|ArtifactReference
name|artifact
init|=
name|createArtifactReference
argument_list|(
literal|"default"
argument_list|,
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
literal|"test://bad.machine.com/repo/"
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
name|IGNORED
argument_list|,
name|SnapshotsPolicy
operator|.
name|IGNORED
argument_list|,
name|CachedFailuresPolicy
operator|.
name|CACHED
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
name|IGNORED
argument_list|,
name|SnapshotsPolicy
operator|.
name|IGNORED
argument_list|,
name|CachedFailuresPolicy
operator|.
name|CACHED
argument_list|)
expr_stmt|;
name|wagonMock
operator|.
name|getIfNewer
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
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|TransferFailedException
name|failedException
init|=
operator|new
name|TransferFailedException
argument_list|(
literal|"transfer failed"
argument_list|)
decl_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
name|failedException
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
name|assertNotDownloaded
argument_list|(
name|downloadedFile
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
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
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|ArtifactReference
name|artifact
init|=
name|createArtifactReference
argument_list|(
literal|"default"
argument_list|,
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
literal|"test://bad.machine.com/repo/"
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
name|IGNORED
argument_list|,
name|SnapshotsPolicy
operator|.
name|IGNORED
argument_list|,
name|CachedFailuresPolicy
operator|.
name|IGNORED
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
name|IGNORED
argument_list|,
name|SnapshotsPolicy
operator|.
name|IGNORED
argument_list|,
name|CachedFailuresPolicy
operator|.
name|IGNORED
argument_list|)
expr_stmt|;
name|wagonMock
operator|.
name|getIfNewer
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
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|TransferFailedException
name|failedException
init|=
operator|new
name|TransferFailedException
argument_list|(
literal|"transfer failed"
argument_list|)
decl_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
name|failedException
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
name|assertNotDownloaded
argument_list|(
name|downloadedFile
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|verify
argument_list|()
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
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
name|createArtifactReference
argument_list|(
literal|"default"
argument_list|,
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
name|IGNORED
argument_list|,
name|SnapshotsPolicy
operator|.
name|IGNORED
argument_list|,
name|CachedFailuresPolicy
operator|.
name|CACHED
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
name|IGNORED
argument_list|,
name|SnapshotsPolicy
operator|.
name|IGNORED
argument_list|,
name|CachedFailuresPolicy
operator|.
name|CACHED
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
block|}
end_class

end_unit

