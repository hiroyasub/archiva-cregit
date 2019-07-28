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
name|wagon
operator|.
name|ResourceDoesNotExistException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|EasyMock
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
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|nio
operator|.
name|file
operator|.
name|attribute
operator|.
name|FileTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|TimeUnit
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
name|*
import|;
end_import

begin_comment
comment|/**  * ManagedDefaultTransferTest  */
end_comment

begin_class
specifier|public
class|class
name|ManagedDefaultTransferTest
extends|extends
name|AbstractProxyTestCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testGetDefaultLayoutNotPresentConnectorOffline
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
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
comment|// Ensure file isn't present first.
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
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
name|NO
argument_list|,
literal|true
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
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
literal|"File should not have been downloaded"
argument_list|,
name|downloadedFile
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
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
comment|// Ensure file isn't present first.
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
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
name|NO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
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
name|Path
name|sourceFile
init|=
name|Paths
operator|.
name|get
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
operator|.
name|getFilePath
argument_list|()
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
annotation|@
name|Test
specifier|public
name|void
name|testGetDefaultLayoutNotPresentPassthrough
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout/1.0/get-default-layout-1.0.jar.asc"
decl_stmt|;
name|setupTestableManagedRepository
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
comment|// Ensure file isn't present first.
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
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
name|NO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|Path
name|sourceFile
init|=
name|Paths
operator|.
name|get
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
operator|.
name|getFilePath
argument_list|()
argument_list|,
name|sourceFile
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|downloadedFile
operator|.
name|getParent
argument_list|()
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
name|downloadedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".sha1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|downloadedFile
operator|.
name|getParent
argument_list|()
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
name|downloadedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".md5"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|downloadedFile
operator|.
name|getParent
argument_list|()
operator|.
name|getFilePath
argument_list|()
operator|.
name|resolve
argument_list|(
name|downloadedFile
operator|.
name|getName
argument_list|()
operator|+
literal|".asc"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
comment|/**      * The attempt here should result in no file being transferred.      *<p/>      * The file exists locally, and the policy is ONCE.      *      * @throws Exception      */
annotation|@
name|Test
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
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
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|expectedFile
argument_list|)
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
name|NO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
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
operator|.
name|getFilePath
argument_list|()
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
comment|/**      * The attempt here should result in no file being transferred.      *<p/>      * The file exists locally, and the policy is ONCE.      *      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testGetDefaultLayoutAlreadyPresentPassthrough
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar.asc"
decl_stmt|;
name|setupTestableManagedRepository
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Path
name|remoteFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|REPOPATH_PROXIED1
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|expectedFile
argument_list|)
argument_list|)
expr_stmt|;
comment|// Set the managed File to be newer than local.
name|setManagedOlderThanRemote
argument_list|(
name|expectedFile
argument_list|,
name|remoteFile
argument_list|)
expr_stmt|;
name|long
name|originalModificationTime
init|=
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|expectedFile
argument_list|)
operator|.
name|toMillis
argument_list|()
decl_stmt|;
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
name|NO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|assertNotDownloaded
argument_list|(
name|downloadedFile
operator|.
name|getFilePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotModified
argument_list|(
name|expectedFile
argument_list|,
name|originalModificationTime
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>      * Request a file, that exists locally, and remotely.      *</p>      *<p>      * All policies are set to IGNORE.      *</p>      *<p>      * Managed file is newer than remote file.      *</p>      *<p>      * Transfer should not have occured, as managed file is newer.      *</p>      *      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testGetDefaultLayoutAlreadyPresentNewerThanRemotePolicyIgnored
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Path
name|remoteFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|REPOPATH_PROXIED1
argument_list|,
name|path
argument_list|)
decl_stmt|;
comment|// Set the managed File to be newer than local.
name|setManagedNewerThanRemote
argument_list|(
name|expectedFile
argument_list|,
name|remoteFile
argument_list|)
expr_stmt|;
name|long
name|originalModificationTime
init|=
name|Files
operator|.
name|getLastModifiedTime
argument_list|(
name|expectedFile
argument_list|)
operator|.
name|toMillis
argument_list|()
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
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|expectedFile
argument_list|)
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
comment|// Attempt the proxy fetch.
name|StorageAsset
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
operator|.
name|getFilePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertNotModified
argument_list|(
name|expectedFile
argument_list|,
name|originalModificationTime
argument_list|)
expr_stmt|;
name|assertNoTempFiles
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
block|}
comment|/**      *<p>      * Request a file, that exists locally, and remotely.      *</p>      *<p>      * All policies are set to IGNORE.      *</p>      *<p>      * Managed file is older than Remote file.      *</p>      *<p>      * Transfer should have occured, as managed file is older than remote.      *</p>      *      * @throws Exception      */
annotation|@
name|Test
specifier|public
name|void
name|testGetDefaultLayoutAlreadyPresentOlderThanRemotePolicyIgnored
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Path
name|remoteFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|REPOPATH_PROXIED1
argument_list|,
name|path
argument_list|)
decl_stmt|;
comment|// Set the managed file to be newer than remote file.
name|setManagedOlderThanRemote
argument_list|(
name|expectedFile
argument_list|,
name|remoteFile
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
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|expectedFile
argument_list|)
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
comment|// Attempt the proxy fetch.
name|StorageAsset
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
name|Path
name|proxiedFile
init|=
name|Paths
operator|.
name|get
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
operator|.
name|getFilePath
argument_list|()
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
comment|/**      * The attempt here should result in file being transferred.      *<p/>      * The file exists locally, is over 6 years old, and the policy is DAILY.      *      * @throws Exception      */
annotation|@
name|Test
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
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
name|assertTrue
argument_list|(
name|Files
operator|.
name|exists
argument_list|(
name|expectedFile
argument_list|)
argument_list|)
expr_stmt|;
name|Files
operator|.
name|setLastModifiedTime
argument_list|(
name|expectedFile
argument_list|,
name|FileTime
operator|.
name|from
argument_list|(
name|getPastDate
argument_list|()
operator|.
name|getTime
argument_list|()
argument_list|,
name|TimeUnit
operator|.
name|MILLISECONDS
argument_list|)
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
name|NO
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
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
name|Path
name|proxiedFile
init|=
name|Paths
operator|.
name|get
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
operator|.
name|getFilePath
argument_list|()
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
annotation|@
name|Test
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
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
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
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
name|Path
name|proxied1File
init|=
name|Paths
operator|.
name|get
argument_list|(
name|REPOPATH_PROXIED1
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|Path
name|proxied2File
init|=
name|Paths
operator|.
name|get
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
operator|.
name|getFilePath
argument_list|()
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
operator|.
name|getFilePath
argument_list|()
operator|.
name|toFile
argument_list|()
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
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
operator|.
name|toFile
argument_list|()
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
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
annotation|@
name|Test
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
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
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
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
name|Path
name|proxied2File
init|=
name|Paths
operator|.
name|get
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
operator|.
name|getFilePath
argument_list|()
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
name|Test
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
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
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
comment|// Configure Connector (usually done within archiva.xml configuration)
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED1
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
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
annotation|@
name|Test
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
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
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
comment|// Configure Repository (usually done within archiva.xml configuration)
name|saveRemoteRepositoryConfig
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
name|get
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|path
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|anyObject
argument_list|(
name|File
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andThrow
argument_list|(
operator|new
name|ResourceDoesNotExistException
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
literal|false
argument_list|)
expr_stmt|;
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
name|ID_PROXIED2
argument_list|,
literal|false
argument_list|)
expr_stmt|;
comment|// Attempt the proxy fetch.
name|StorageAsset
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
name|Path
name|proxied2File
init|=
name|Paths
operator|.
name|get
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
operator|.
name|getFilePath
argument_list|()
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
name|Test
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
name|Path
name|expectedFile
init|=
name|managedDefaultDir
operator|.
name|resolve
argument_list|(
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
name|assertNotExistsInManagedDefaultRepo
argument_list|(
name|expectedFile
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
literal|false
argument_list|)
expr_stmt|;
name|saveConnector
argument_list|(
name|ID_DEFAULT_MANAGED
argument_list|,
literal|"badproxied2"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
name|Path
name|tmpFile
init|=
name|expectedFile
operator|.
name|getParent
argument_list|()
operator|.
name|resolve
argument_list|(
name|expectedFile
operator|.
name|getFileName
argument_list|()
operator|+
literal|".tmp"
argument_list|)
decl_stmt|;
name|wagonMock
operator|.
name|get
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|path
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|anyObject
argument_list|(
name|File
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andThrow
argument_list|(
operator|new
name|ResourceDoesNotExistException
argument_list|(
literal|"Can't find resource."
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMock
operator|.
name|get
argument_list|(
name|EasyMock
operator|.
name|eq
argument_list|(
name|path
argument_list|)
argument_list|,
name|EasyMock
operator|.
name|anyObject
argument_list|(
name|File
operator|.
name|class
argument_list|)
argument_list|)
expr_stmt|;
name|EasyMock
operator|.
name|expectLastCall
argument_list|()
operator|.
name|andThrow
argument_list|(
operator|new
name|ResourceDoesNotExistException
argument_list|(
literal|"Can't find resource."
argument_list|)
argument_list|)
expr_stmt|;
name|wagonMockControl
operator|.
name|replay
argument_list|()
expr_stmt|;
name|StorageAsset
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
operator|.
name|getFilePath
argument_list|()
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
comment|// TODO: do not want failures to present as a not found [MRM-492]
comment|// TODO: How much information on each failure should we pass back to the user vs. logging in the proxy?
block|}
block|}
end_class

end_unit

