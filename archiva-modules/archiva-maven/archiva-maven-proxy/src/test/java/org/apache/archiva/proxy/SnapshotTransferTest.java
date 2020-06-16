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
name|BaseRepositoryContentLayout
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
name|content
operator|.
name|Artifact
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
name|junit
operator|.
name|Test
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
comment|/**  * SnapshotTransferTest   *  *  */
end_comment

begin_class
specifier|public
class|class
name|SnapshotTransferTest
extends|extends
name|AbstractProxyTestCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testSnapshotNonExistant
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/does-not-exist/1.0-SNAPSHOT/does-not-exist-1.0-SNAPSHOT.jar"
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
name|BaseRepositoryContentLayout
name|layout
init|=
name|managedDefaultRepository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
name|assertFalse
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
literal|false
argument_list|)
expr_stmt|;
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
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
name|testTimestampDrivenSnapshotNotPresentAlready
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-timestamped-snapshot/1.0-SNAPSHOT/get-timestamped-snapshot-1.0-SNAPSHOT.jar"
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
name|BaseRepositoryContentLayout
name|layout
init|=
name|managedDefaultRepository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
name|assertFalse
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
literal|false
argument_list|)
expr_stmt|;
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
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
name|testNewerTimestampDrivenSnapshotOnFirstRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-present-timestamped-snapshot/1.0-SNAPSHOT/get-present-timestamped-snapshot-1.0-SNAPSHOT.jar"
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
name|BaseRepositoryContentLayout
name|layout
init|=
name|managedDefaultRepository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
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
literal|false
argument_list|)
expr_stmt|;
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
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
name|testOlderTimestampDrivenSnapshotOnFirstRepo
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-present-timestamped-snapshot/1.0-SNAPSHOT/get-present-timestamped-snapshot-1.0-SNAPSHOT.jar"
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
name|setManagedNewerThanRemote
argument_list|(
name|expectedFile
argument_list|,
name|remoteFile
argument_list|)
expr_stmt|;
name|BaseRepositoryContentLayout
name|layout
init|=
name|managedDefaultRepository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
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
comment|// Attempt to download.
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
comment|// Should not have downloaded as managed is newer than remote.
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
comment|/**      * TODO: Has problems with wagon implementation not preserving timestamp.      */
comment|/*     public void testNewerTimestampDrivenSnapshotOnSecondRepoThanFirstNotPresentAlready()         throws Exception     {         String path = "org/apache/maven/test/get-timestamped-snapshot-in-both/1.0-SNAPSHOT/get-timestamped-snapshot-in-both-1.0-SNAPSHOT.jar";         setupTestableManagedRepository( path );                  Path expectedFile = managedDefaultDir.resolve(path);         ArtifactReference artifact = createArtifactReference( "default", path );          Files.delete(expectedFile);         assertFalse( Files.exists(expectedFile) );          // Create customized proxy / target repository         File targetProxyDir = saveTargetedRepositoryConfig( ID_PROXIED1_TARGET, REPOPATH_PROXIED1,                                                             REPOPATH_PROXIED1_TARGET, "default" );          new File( targetProxyDir, path ).setLastModified( getPastDate().getTime() );          // Configure Connector (usually done within archiva.xml configuration)         saveConnector( ID_DEFAULT_MANAGED, ID_PROXIED1_TARGET, ChecksumPolicy.IGNORED, ReleasesPolicy.IGNORED,                        SnapshotsPolicy.IGNORED, CachedFailuresPolicy.IGNORED );         saveConnector( ID_DEFAULT_MANAGED, ID_PROXIED2, ChecksumPolicy.IGNORED, ReleasesPolicy.IGNORED,                        SnapshotsPolicy.IGNORED, CachedFailuresPolicy.IGNORED );          File downloadedFile = proxyHandler.fetchFromProxies( managedDefaultRepository, artifact );          // Should have downloaded the content from proxy2, as proxy1 has an old (by file.lastModified check) version.         Path proxiedFile = Paths.get(REPOPATH_PROXIED2, path);         assertFileEquals( expectedFile, downloadedFile, proxiedFile );         assertNoTempFiles( expectedFile );     }       public void testOlderTimestampDrivenSnapshotOnSecondRepoThanFirstNotPresentAlready()         throws Exception     {         String path = "org/apache/maven/test/get-timestamped-snapshot-in-both/1.0-SNAPSHOT/get-timestamped-snapshot-in-both-1.0-SNAPSHOT.jar";         setupTestableManagedRepository( path );                  Path expectedFile = managedDefaultDir.resolve(path);         ArtifactReference artifact = createArtifactReference( "default", path );          Files.delete(expectedFile);         assertFalse( Files.exists(expectedFile) );          // Create customized proxy / target repository         File targetProxyDir = saveTargetedRepositoryConfig( ID_PROXIED2_TARGET, REPOPATH_PROXIED2,                                                             REPOPATH_PROXIED2_TARGET, "default" );          new File( targetProxyDir, path ).setLastModified( getPastDate().getTime() );          // Configure Connector (usually done within archiva.xml configuration)         saveConnector( ID_DEFAULT_MANAGED, ID_PROXIED1, ChecksumPolicy.IGNORED, ReleasesPolicy.IGNORED,                        SnapshotsPolicy.IGNORED, CachedFailuresPolicy.IGNORED );         saveConnector( ID_DEFAULT_MANAGED, ID_PROXIED2_TARGET, ChecksumPolicy.IGNORED, ReleasesPolicy.IGNORED,                        SnapshotsPolicy.IGNORED, CachedFailuresPolicy.IGNORED );          File downloadedFile = proxyHandler.fetchFromProxies( managedDefaultRepository, artifact );          File proxiedFile = new File( REPOPATH_PROXIED1_TARGET, path );         assertFileEquals( expectedFile, downloadedFile, proxiedFile );         assertNoTempFiles( expectedFile );     } */
annotation|@
name|Test
specifier|public
name|void
name|testTimestampDrivenSnapshotNotExpired
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-present-timestamped-snapshot/1.0-SNAPSHOT/get-present-timestamped-snapshot-1.0-SNAPSHOT.jar"
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
name|BaseRepositoryContentLayout
name|layout
init|=
name|managedDefaultRepository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
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
name|Files
operator|.
name|setLastModifiedTime
argument_list|(
name|proxiedFile
argument_list|,
name|FileTime
operator|.
name|from
argument_list|(
name|getFutureDate
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
literal|false
argument_list|)
expr_stmt|;
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
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
name|testTimestampDrivenSnapshotNotUpdated
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-present-timestamped-snapshot/1.0-SNAPSHOT/get-present-timestamped-snapshot-1.0-SNAPSHOT.jar"
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
name|setManagedNewerThanRemote
argument_list|(
name|expectedFile
argument_list|,
name|remoteFile
argument_list|,
literal|12000000
argument_list|)
expr_stmt|;
name|long
name|expectedTimestamp
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
name|BaseRepositoryContentLayout
name|layout
init|=
name|managedDefaultRepository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
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
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|assertNotDownloaded
argument_list|(
name|downloadedFile
argument_list|)
expr_stmt|;
name|assertNotModified
argument_list|(
name|expectedFile
argument_list|,
name|expectedTimestamp
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
name|testTimestampDrivenSnapshotNotPresentAlreadyExpiredCacheFailure
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-timestamped-snapshot/1.0-SNAPSHOT/get-timestamped-snapshot-1.0-SNAPSHOT.jar"
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
name|BaseRepositoryContentLayout
name|layout
init|=
name|managedDefaultRepository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
name|assertFalse
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
name|IGNORE
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
name|ID_PROXIED2
argument_list|,
name|ChecksumPolicy
operator|.
name|IGNORE
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
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
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
name|testMetadataDrivenSnapshotNotPresentAlready
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-metadata-snapshot/1.0-SNAPSHOT/get-metadata-snapshot-1.0-20050831.101112-1.jar"
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
name|BaseRepositoryContentLayout
name|layout
init|=
name|managedDefaultRepository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|expectedFile
argument_list|)
expr_stmt|;
name|assertFalse
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
literal|false
argument_list|)
expr_stmt|;
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
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
name|assertNotNull
argument_list|(
name|downloadedFile
argument_list|)
expr_stmt|;
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
name|testGetMetadataDrivenSnapshotRemoteUpdate
parameter_list|()
throws|throws
name|Exception
block|{
comment|// Metadata driven snapshots (using a full timestamp) are treated like a release. It is the timing of the
comment|// updates to the metadata files that triggers which will be downloaded
name|String
name|path
init|=
literal|"org/apache/maven/test/get-present-metadata-snapshot/1.0-SNAPSHOT/get-present-metadata-snapshot-1.0-20050831.101112-1.jar"
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
name|BaseRepositoryContentLayout
name|layout
init|=
name|managedDefaultRepository
operator|.
name|getLayout
argument_list|(
name|BaseRepositoryContentLayout
operator|.
name|class
argument_list|)
decl_stmt|;
name|Artifact
name|artifact
init|=
name|layout
operator|.
name|getArtifact
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
literal|false
argument_list|)
expr_stmt|;
name|StorageAsset
name|downloadedFile
init|=
name|proxyHandler
operator|.
name|fetchFromProxies
argument_list|(
name|managedDefaultRepository
operator|.
name|getRepository
argument_list|()
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
name|assertNotNull
argument_list|(
name|downloadedFile
argument_list|)
expr_stmt|;
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
block|}
end_class

end_unit

