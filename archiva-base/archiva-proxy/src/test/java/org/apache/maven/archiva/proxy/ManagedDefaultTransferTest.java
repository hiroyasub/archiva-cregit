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
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
comment|/**  * ManagedDefaultTransferTest   *  * @author Brett Porter  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ManagedDefaultTransferTest
extends|extends
name|AbstractProxyTestCase
block|{
specifier|public
name|void
name|testGetDefaultLayoutNotPresent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout/1.0/get-default-layout-1.0.jar"
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
name|createArtifactReference
argument_list|(
literal|"default"
argument_list|,
name|path
argument_list|)
decl_stmt|;
comment|// Ensure file isn't present first.
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
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|ReleasesPolicy
operator|.
name|ONCE
argument_list|,
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|CachedFailuresPolicy
operator|.
name|IGNORED
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
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
name|File
name|sourceFile
init|=
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED1
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
name|sourceFile
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * The attempt here should result in no file being transferred.      *       * The file exists locally, and the policy is ONCE.      *       * @throws Exception      */
specifier|public
name|void
name|testGetDefaultLayoutAlreadyPresentPolicyOnce
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar"
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
name|createArtifactReference
argument_list|(
literal|"default"
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|ReleasesPolicy
operator|.
name|ONCE
argument_list|,
name|SnapshotsPolicy
operator|.
name|ONCE
argument_list|,
name|CachedFailuresPolicy
operator|.
name|IGNORED
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
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
name|assertFileEquals
argument_list|(
name|expectedFile
argument_list|,
name|downloadedFile
argument_list|,
name|expectedFile
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * The attempt here should result in file being transferred.      *       * The file exists locally, and the policy is IGNORE.      *       * @throws Exception      */
specifier|public
name|void
name|testGetDefaultLayoutAlreadyPresentPolicyIgnored
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar"
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
name|long
name|originalModificationTime
init|=
name|expectedFile
operator|.
name|lastModified
argument_list|()
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
name|assertTrue
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
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
comment|// Attempt the proxy fetch.
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
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED1
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
name|proxiedFile
argument_list|)
expr_stmt|;
name|long
name|proxiedLastModified
init|=
name|proxiedFile
operator|.
name|lastModified
argument_list|()
decl_stmt|;
name|long
name|downloadedLastModified
init|=
name|downloadedFile
operator|.
name|lastModified
argument_list|()
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Check file timestamp is not that of proxy:"
argument_list|,
name|proxiedLastModified
operator|==
name|downloadedLastModified
argument_list|)
expr_stmt|;
if|if
condition|(
name|originalModificationTime
operator|!=
name|downloadedLastModified
condition|)
block|{
comment|/* On some systems the timestamp functions are not accurate enough.              * This delta is the amount of milliseconds of 'fudge factor' we allow for              * the unit test to still be considered 'passed'.              */
name|int
name|delta
init|=
literal|1100
decl_stmt|;
name|long
name|hirange
init|=
name|originalModificationTime
operator|+
operator|(
name|delta
operator|/
literal|2
operator|)
decl_stmt|;
name|long
name|lorange
init|=
name|originalModificationTime
operator|-
operator|(
name|delta
operator|/
literal|2
operator|)
decl_stmt|;
if|if
condition|(
operator|(
name|downloadedLastModified
operator|<
name|lorange
operator|)
operator|||
operator|(
name|downloadedLastModified
operator|>
name|hirange
operator|)
condition|)
block|{
name|fail
argument_list|(
literal|"Check file timestamp is that of original managed file: expected within range lo:<"
operator|+
name|lorange
operator|+
literal|"> hi:<"
operator|+
name|hirange
operator|+
literal|"> but was:<"
operator|+
name|downloadedLastModified
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
block|}
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * The attempt here should result in file being transferred.      *       * The file exists locally, is over 6 years old, and the policy is DAILY.      *       * @throws Exception      */
specifier|public
name|void
name|testGetDefaultLayoutRemoteUpdate
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar"
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
name|createArtifactReference
argument_list|(
literal|"default"
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|expectedFile
operator|.
name|setLastModified
argument_list|(
name|getPastDate
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
argument_list|,
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|ReleasesPolicy
operator|.
name|DAILY
argument_list|,
name|SnapshotsPolicy
operator|.
name|DAILY
argument_list|,
name|CachedFailuresPolicy
operator|.
name|IGNORED
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
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
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED1
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
name|proxiedFile
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetWhenInBothProxiedRepos
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-in-both-proxies/1.0/get-in-both-proxies-1.0.jar"
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
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
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
name|ID_PROXIED2
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
comment|// Attempt the proxy fetch.
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
name|File
name|proxied1File
init|=
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED1
argument_list|,
name|path
argument_list|)
decl_stmt|;
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
name|proxied1File
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
comment|// TODO: is this check even needed if it passes above?
name|String
name|actualContents
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|downloadedFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|String
name|badContents
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|proxied2File
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"Downloaded file contents should not be that of proxy 2"
argument_list|,
name|StringUtils
operator|.
name|equals
argument_list|(
name|actualContents
argument_list|,
name|badContents
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetInSecondProxiedRepo
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
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
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
name|ID_PROXIED2
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
comment|// Attempt the proxy fetch.
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
specifier|public
name|void
name|testNotFoundInAnyProxies
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/does-not-exist/1.0/does-not-exist-1.0.jar"
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
name|createArtifactReference
argument_list|(
literal|"default"
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
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
name|ID_PROXIED2
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
name|ID_LEGACY_PROXIED
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
comment|// Attempt the proxy fetch.
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
name|assertNull
argument_list|(
literal|"File returned was: "
operator|+
name|downloadedFile
operator|+
literal|"; should have got a not found exception"
argument_list|,
name|downloadedFile
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetInSecondProxiedRepoFirstFails
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
name|saveRepositoryConfig
argument_list|(
literal|"badproxied"
argument_list|,
literal|"Bad Proxied"
argument_list|,
literal|"test://bad.machine.com/repo/"
argument_list|,
literal|"default"
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
name|getAbsolutePath
argument_list|()
operator|+
literal|".tmp"
argument_list|)
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
operator|new
name|TransferFailedException
argument_list|(
literal|"transfer failed"
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
literal|"badproxied"
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
name|ID_PROXIED2
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
comment|// Attempt the proxy fetch.
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
specifier|public
name|void
name|testGetAllRepositoriesFail
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
name|saveRepositoryConfig
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
name|saveRepositoryConfig
argument_list|(
literal|"badproxied2"
argument_list|,
literal|"Bad Proxied 2"
argument_list|,
literal|"test://dead.machine.com/repo/"
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
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
operator|new
name|TransferFailedException
argument_list|(
literal|"transfer failed"
argument_list|)
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
name|wagonMockControl
operator|.
name|setThrowable
argument_list|(
operator|new
name|TransferFailedException
argument_list|(
literal|"transfer failed"
argument_list|)
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
comment|// TODO: do not want failures to present as a not found!
comment|// TODO: How much information on each failure should we pass back to the user vs. logging in the proxy?
block|}
specifier|public
name|void
name|testLegacyProxyRepoGetAlreadyPresent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar"
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
name|createArtifactReference
argument_list|(
literal|"default"
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|expectedFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_LEGACY_PROXIED
argument_list|,
name|ChecksumPolicy
operator|.
name|IGNORED
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
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED_LEGACY
argument_list|,
literal|"org.apache.maven.test/jars/get-default-layout-present-1.0.jar"
argument_list|)
decl_stmt|;
name|assertFileEquals
argument_list|(
name|expectedFile
argument_list|,
name|downloadedFile
argument_list|,
name|proxiedFile
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLegacyRequestConvertedToDefaultPathInManagedRepo
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Check that a Maven1 legacy request is translated to a maven2 path in
comment|// the managed repository.
name|String
name|legacyPath
init|=
literal|"org.apache.maven.test/jars/get-default-layout-present-1.0.jar"
decl_stmt|;
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar"
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
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_LEGACY_PROXIED
argument_list|,
name|ChecksumPolicy
operator|.
name|IGNORED
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
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED_LEGACY
argument_list|,
name|legacyPath
argument_list|)
decl_stmt|;
name|assertFileEquals
argument_list|(
name|expectedFile
argument_list|,
name|downloadedFile
argument_list|,
name|proxiedFile
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLegacyProxyRepoGetNotPresent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout/1.0/get-default-layout-1.0.jar"
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
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_LEGACY_PROXIED
argument_list|,
name|ChecksumPolicy
operator|.
name|IGNORED
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
name|File
name|proxiedFile
init|=
operator|new
name|File
argument_list|(
name|REPOPATH_PROXIED_LEGACY
argument_list|,
literal|"org.apache.maven.test/jars/get-default-layout-1.0.jar"
argument_list|)
decl_stmt|;
name|assertFileEquals
argument_list|(
name|expectedFile
argument_list|,
name|downloadedFile
argument_list|,
name|proxiedFile
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
comment|// TODO: timestamp preservation requires support for that in wagon
comment|//    assertEquals( "Check file timestamp", proxiedFile.lastModified(), file.lastModified() );
block|}
block|}
end_class

end_unit

