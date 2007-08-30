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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * SnapshotTransferTest   *  * @author Brett Porter  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|SnapshotTransferTest
extends|extends
name|AbstractProxyTestCase
block|{
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
name|getFutureDate
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
comment|/**      * TODO: Has problems with wagon implementation not preserving timestamp.      */
comment|/*     public void testNewerTimestampDrivenSnapshotOnSecondRepoThanFirstNotPresentAlready()         throws Exception     {         String path = "org/apache/maven/test/get-timestamped-snapshot-in-both/1.0-SNAPSHOT/get-timestamped-snapshot-in-both-1.0-SNAPSHOT.jar";         setupTestableManagedRepository( path );                  File expectedFile = new File( managedDefaultDir, path );         ArtifactReference artifact = createArtifactReference( "default", path );          expectedFile.delete();         assertFalse( expectedFile.exists() );          // Create customized proxy / target repository         File targetProxyDir = saveTargetedRepositoryConfig( ID_PROXIED1_TARGET, REPOPATH_PROXIED1,                                                             REPOPATH_PROXIED1_TARGET, "default" );          new File( targetProxyDir, path ).setLastModified( getPastDate().getTime() );          // Configure Connector (usually done within archiva.xml configuration)         saveConnector( ID_DEFAULT_MANAGED, ID_PROXIED1_TARGET, ChecksumPolicy.IGNORED, ReleasesPolicy.IGNORED,                        SnapshotsPolicy.IGNORED, CachedFailuresPolicy.IGNORED );         saveConnector( ID_DEFAULT_MANAGED, ID_PROXIED2, ChecksumPolicy.IGNORED, ReleasesPolicy.IGNORED,                        SnapshotsPolicy.IGNORED, CachedFailuresPolicy.IGNORED );          File downloadedFile = proxyHandler.fetchFromProxies( managedDefaultRepository, artifact );          // Should have downloaded the content from proxy2, as proxy1 has an old (by file.lastModified check) version.         File proxiedFile = new File( REPOPATH_PROXIED2, path );         assertFileEquals( expectedFile, downloadedFile, proxiedFile );         assertNoTempFiles( expectedFile );     }       public void testOlderTimestampDrivenSnapshotOnSecondRepoThanFirstNotPresentAlready()         throws Exception     {         String path = "org/apache/maven/test/get-timestamped-snapshot-in-both/1.0-SNAPSHOT/get-timestamped-snapshot-in-both-1.0-SNAPSHOT.jar";         setupTestableManagedRepository( path );                  File expectedFile = new File( managedDefaultDir, path );         ArtifactReference artifact = createArtifactReference( "default", path );          expectedFile.delete();         assertFalse( expectedFile.exists() );          // Create customized proxy / target repository         File targetProxyDir = saveTargetedRepositoryConfig( ID_PROXIED2_TARGET, REPOPATH_PROXIED2,                                                             REPOPATH_PROXIED2_TARGET, "default" );          new File( targetProxyDir, path ).setLastModified( getPastDate().getTime() );          // Configure Connector (usually done within archiva.xml configuration)         saveConnector( ID_DEFAULT_MANAGED, ID_PROXIED1, ChecksumPolicy.IGNORED, ReleasesPolicy.IGNORED,                        SnapshotsPolicy.IGNORED, CachedFailuresPolicy.IGNORED );         saveConnector( ID_DEFAULT_MANAGED, ID_PROXIED2_TARGET, ChecksumPolicy.IGNORED, ReleasesPolicy.IGNORED,                        SnapshotsPolicy.IGNORED, CachedFailuresPolicy.IGNORED );          File downloadedFile = proxyHandler.fetchFromProxies( managedDefaultRepository, artifact );          File proxiedFile = new File( REPOPATH_PROXIED1_TARGET, path );         assertFileEquals( expectedFile, downloadedFile, proxiedFile );         assertNoTempFiles( expectedFile );     } */
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
name|proxiedFile
operator|.
name|setLastModified
argument_list|(
name|getFutureDate
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
name|expectedFile
operator|.
name|setLastModified
argument_list|(
name|proxiedFile
operator|.
name|lastModified
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
name|CACHED
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
block|}
end_class

end_unit

