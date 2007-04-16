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
name|ProjectReference
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
comment|/**  * MetadataTransferTest   *  * @author Brett Porter  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|MetadataTransferTest
extends|extends
name|AbstractProxyTestCase
block|{
specifier|public
name|void
name|testGetMetadataNotPresent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/dummy-artifact/1.0/maven-metadata.xml"
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
name|ProjectReference
name|metadata
init|=
name|createMetadataReference
argument_list|(
literal|"default"
argument_list|,
name|path
argument_list|)
decl_stmt|;
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
name|metadata
argument_list|)
decl_stmt|;
name|assertNotDownloaded
argument_list|(
name|downloadedFile
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/dummy-artifact/1.0/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        try
comment|//        {
comment|//            File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//            fail( "Found file: " + file + "; but was expecting a failure" );
comment|//        }
comment|//        catch ( ResourceDoesNotExistException e )
comment|//        {
comment|//            // expected
comment|//
comment|//            assertFalse( expectedFile.exists() );
comment|//        }
block|}
specifier|public
name|void
name|testGetMetadataProxied
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-default-metadata/1.0/maven-metadata.xml"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-default-metadata/1.0/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        FileUtils.deleteDirectory( expectedFile.getParentFile() );
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        String expectedContents = getExpectedMetadata( "get-default-metadata", "1.0" );
comment|//        assertEquals( "Check content matches", expectedContents, FileUtils.readFileToString( file, null ) );
block|}
specifier|public
name|void
name|testGetMetadataMergeRepos
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-merged-metadata/maven-metadata.xml"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-merged-metadata/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//
comment|//        String expectedContents = getExpectedMetadata( "get-merged-metadata", getVersioning(
comment|//            Arrays.asList( new String[]{"0.9", "1.0", "2.0", "3.0", "5.0", "4.0"} ), file ) );
comment|//
comment|//        assertEquals( "Check content matches", expectedContents, FileUtils.readFileToString( file, null ) );
block|}
specifier|public
name|void
name|testGetMetadataRemovedFromProxies
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-removed-metadata/1.0/maven-metadata.xml"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-removed-metadata/1.0/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String expectedContents =
comment|//            FileUtils.readFileToString( new File( defaultManagedRepository.getBasedir(), path ), null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check content matches", expectedContents, FileUtils.readFileToString( file, null ) );
block|}
specifier|public
name|void
name|testGetReleaseMetadataNotExpired
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-updated-metadata/maven-metadata.xml"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-updated-metadata/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String expectedContents =
comment|//            FileUtils.readFileToString( new File( defaultManagedRepository.getBasedir(), path ), null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        new File( expectedFile.getParentFile(), ".metadata-proxied1" ).setLastModified( getPastDate().getTime() );
comment|//
comment|//        proxiedRepository1.getReleases().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER );
comment|//        proxiedRepository1.getSnapshots().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS );
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check content matches", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//
comment|//        String unexpectedContents =
comment|//            FileUtils.readFileToString( new File( proxiedRepository1.getBasedir(), path ), null );
comment|//        assertFalse( "Check content doesn't match proxy version",
comment|//                     unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testGetSnapshotMetadataNotExpired
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-updated-metadata/1.0-SNAPSHOT/maven-metadata.xml"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-updated-metadata/1.0-SNAPSHOT/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String expectedContents =
comment|//            FileUtils.readFileToString( new File( defaultManagedRepository.getBasedir(), path ), null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        new File( expectedFile.getParentFile(), ".metadata-proxied1" ).setLastModified( getPastDate().getTime() );
comment|//
comment|//        proxiedRepository1.getReleases().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS );
comment|//        proxiedRepository1.getSnapshots().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER );
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check content matches", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//
comment|//        String unexpectedContents =
comment|//            FileUtils.readFileToString( new File( proxiedRepository1.getBasedir(), path ), null );
comment|//        assertFalse( "Check content doesn't match proxy version",
comment|//                     unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testGetReleaseMetadataExpired
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-updated-metadata/maven-metadata.xml"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-updated-metadata/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String unexpectedContents =
comment|//            FileUtils.readFileToString( new File( defaultManagedRepository.getBasedir(), path ), null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        new File( expectedFile.getParentFile(), ".metadata-proxied1" ).setLastModified( getPastDate().getTime() );
comment|//
comment|//        proxiedRepository1.getReleases().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS );
comment|//        proxiedRepository1.getSnapshots().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER );
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//
comment|//        String expectedContents = getExpectedMetadata( "get-updated-metadata", getVersioning(
comment|//            Arrays.asList( new String[]{"1.0", "2.0"} ), file ) );
comment|//
comment|//        assertEquals( "Check content matches", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//        assertFalse( "Check content doesn't match proxy version",
comment|//                     unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testGetSnapshotMetadataExpired
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-updated-metadata/1.0-SNAPSHOT/maven-metadata.xml"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-updated-metadata/1.0-SNAPSHOT/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String unexpectedContents =
comment|//            FileUtils.readFileToString( new File( defaultManagedRepository.getBasedir(), path ), null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        new File( expectedFile.getParentFile(), ".metadata-proxied1" ).setLastModified( getPastDate().getTime() );
comment|//
comment|//        proxiedRepository1.getReleases().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_NEVER );
comment|//        proxiedRepository1.getSnapshots().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS );
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//
comment|//        String expectedContents =
comment|//            getExpectedMetadata( "get-updated-metadata", "1.0-SNAPSHOT", getVersioning( "20050831.111213", 2, file ) );
comment|//
comment|//        assertEquals( "Check content matches", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//        assertFalse( "Check content doesn't match proxy version",
comment|//                     unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testGetMetadataNotUpdated
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-updated-metadata/maven-metadata.xml"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-updated-metadata/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String expectedContents =
comment|//            FileUtils.readFileToString( new File( defaultManagedRepository.getBasedir(), path ), null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        new File( expectedFile.getParentFile(), ".metadata-proxied1" ).setLastModified( proxiedFile.lastModified() );
comment|//
comment|//        proxiedRepository1.getReleases().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS );
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check content matches", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check content doesn't match proxy version",
comment|//                     unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testGetMetadataUpdated
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/get-updated-metadata/maven-metadata.xml"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-updated-metadata/maven-metadata.xml";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String unexpectedContents =
comment|//            FileUtils.readFileToString( new File( defaultManagedRepository.getBasedir(), path ), null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        new File( expectedFile.getParentFile(), ".metadata-proxied1" ).setLastModified( getPastDate().getTime() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//
comment|//        String expectedContents = getExpectedMetadata( "get-updated-metadata", getVersioning(
comment|//            Arrays.asList( new String[]{"1.0", "2.0"} ), file ) );
comment|//        assertEquals( "Check content matches", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//        assertFalse( "Check content doesn't match old version",
comment|//                     unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
block|}
end_class

end_unit

