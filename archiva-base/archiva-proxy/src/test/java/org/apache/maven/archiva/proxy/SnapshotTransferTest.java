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
name|wagon
operator|.
name|ResourceDoesNotExistException
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
name|text
operator|.
name|ParseException
import|;
end_import

begin_comment
comment|/**  * SnapshotTransferTest   *  * @author Brett Porter  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
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
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/does-not-exist/1.0-SNAPSHOT/does-not-exist-1.0-SNAPSHOT.jar"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/does-not-exist/1.0-SNAPSHOT/does-not-exist-1.0-SNAPSHOT.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        try
comment|//        {
comment|//            File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//            fail( "File returned was: " + file + "; should have got a not found exception" );
comment|//        }
comment|//        catch ( ResourceDoesNotExistException e )
comment|//        {
comment|//            // expected, but check file was not created
comment|//            assertFalse( expectedFile.exists() );
comment|//        }
block|}
specifier|public
name|void
name|testTimestampDrivenSnapshotNotPresentAlready
parameter_list|()
block|{
name|String
name|path
init|=
literal|"org/apache/maven/test/does-not-exist/1.0-SNAPSHOT/does-not-exist-1.0-SNAPSHOT.jar"
decl_stmt|;
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path =
comment|//            "org/apache/maven/test/get-timestamped-snapshot/1.0-SNAPSHOT/get-timestamped-snapshot-1.0-SNAPSHOT.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        expectedFile.delete();
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
block|}
specifier|public
name|void
name|testNewerTimestampDrivenSnapshotOnFirstRepo
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
throws|,
name|ParseException
block|{
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-present-timestamped-snapshot/1.0-SNAPSHOT/get-present-timestamped-snapshot-1.0-SNAPSHOT.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        expectedFile.setLastModified( getPastDate().getTime() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
block|}
specifier|public
name|void
name|testOlderTimestampDrivenSnapshotOnFirstRepo
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
block|{
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-present-timestamped-snapshot/1.0-SNAPSHOT/get-present-timestamped-snapshot-1.0-SNAPSHOT.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( expectedFile, null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        expectedFile.setLastModified( getFutureDate().getTime() );
comment|//
comment|//        proxiedRepository1.getSnapshots().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS );
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check file contents", unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testNewerTimestampDrivenSnapshotOnSecondRepoThanFirstNotPresentAlready
parameter_list|()
throws|throws
name|Exception
block|{
comment|// TODO: wagon may not support timestamps (yet)
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-timestamped-snapshot-in-both/1.0-SNAPSHOT/get-timestamped-snapshot-in-both-1.0-SNAPSHOT.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        File repoLocation = getTestFile( "target/test-repository/proxied1" );
comment|//        FileUtils.deleteDirectory( repoLocation );
comment|//        copyDirectoryStructure( getTestFile( "src/test/repositories/proxied1" ), repoLocation );
comment|//        proxiedRepository1 = createRepository( "proxied1", repoLocation );
comment|//
comment|//        new File( proxiedRepository1.getBasedir(), path ).setLastModified( getPastDate().getTime() );
comment|//
comment|//        proxiedRepositories.clear();
comment|//        proxiedRepositories.add( createProxiedRepository( proxiedRepository1 ) );
comment|//        proxiedRepositories.add( createProxiedRepository( proxiedRepository2 ) );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//
comment|//        File proxiedFile = new File( proxiedRepository2.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//
comment|//        proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check file contents", unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testOlderTimestampDrivenSnapshotOnSecondRepoThanFirstNotPresentAlready
parameter_list|()
throws|throws
name|Exception
block|{
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-timestamped-snapshot-in-both/1.0-SNAPSHOT/get-timestamped-snapshot-in-both-1.0-SNAPSHOT.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        expectedFile.delete();
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        File repoLocation = getTestFile( "target/test-repository/proxied2" );
comment|//        FileUtils.deleteDirectory( repoLocation );
comment|//        copyDirectoryStructure( getTestFile( "src/test/repositories/proxied2" ), repoLocation );
comment|//        proxiedRepository2 = createRepository( "proxied2", repoLocation );
comment|//
comment|//        new File( proxiedRepository2.getBasedir(), path ).setLastModified( getPastDate().getTime() );
comment|//
comment|//        proxiedRepositories.clear();
comment|//        proxiedRepositories.add( createProxiedRepository( proxiedRepository1 ) );
comment|//        proxiedRepositories.add( createProxiedRepository( proxiedRepository2 ) );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//
comment|//        proxiedFile = new File( proxiedRepository2.getBasedir(), path );
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check file contents", unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testTimestampDrivenSnapshotNotExpired
parameter_list|()
throws|throws
name|Exception
block|{
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-present-timestamped-snapshot/1.0-SNAPSHOT/get-present-timestamped-snapshot-1.0-SNAPSHOT.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        proxiedFile.setLastModified( getFutureDate().getTime() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        String expectedContents = FileUtils.readFileToString( expectedFile, null );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check file contents", unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testTimestampDrivenSnapshotNotUpdated
parameter_list|()
throws|throws
name|Exception
block|{
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-present-timestamped-snapshot/1.0-SNAPSHOT/get-present-timestamped-snapshot-1.0-SNAPSHOT.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( expectedFile, null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        expectedFile.setLastModified( proxiedFile.lastModified() );
comment|//
comment|//        proxiedRepository1.getSnapshots().setUpdatePolicy( ArtifactRepositoryPolicy.UPDATE_POLICY_ALWAYS );
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check file contents", unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
specifier|public
name|void
name|testTimestampDrivenSnapshotNotPresentAlreadyExpiredCacheFailure
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
block|{
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-timestamped-snapshot/1.0-SNAPSHOT/get-timestamped-snapshot-1.0-SNAPSHOT.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        expectedFile.delete();
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        proxiedRepositories.clear();
comment|//        ProxiedArtifactRepository proxiedArtifactRepository = createProxiedRepository( proxiedRepository1 );
comment|//        proxiedArtifactRepository.addFailure( path, ALWAYS_UPDATE_POLICY );
comment|//        proxiedRepositories.add( proxiedArtifactRepository );
comment|//        proxiedRepositories.add( createProxiedRepository( proxiedRepository2 ) );
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//
comment|//        assertFalse( "Check failure", proxiedArtifactRepository.isCachedFailure( path ) );
block|}
specifier|public
name|void
name|testMetadataDrivenSnapshotNotPresentAlready
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
block|{
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|//        String path = "org/apache/maven/test/get-metadata-snapshot/1.0-SNAPSHOT/get-metadata-snapshot-1.0-20050831.101112-1.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        expectedFile.delete();
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
block|}
specifier|public
name|void
name|testGetMetadataDrivenSnapshotRemoteUpdate
parameter_list|()
throws|throws
name|ResourceDoesNotExistException
throws|,
name|ProxyException
throws|,
name|IOException
throws|,
name|ParseException
block|{
name|fail
argument_list|(
literal|"Implemented "
operator|+
name|getName
argument_list|()
argument_list|)
expr_stmt|;
comment|// Metadata driven snapshots (using a full timestamp) are treated like a release. It is the timing of the
comment|// updates to the metadata files that triggers which will be downloaded
comment|//        String path = "org/apache/maven/test/get-present-metadata-snapshot/1.0-SNAPSHOT/get-present-metadata-snapshot-1.0-20050831.101112-1.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( expectedFile, null );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        expectedFile.setLastModified( getPastDate().getTime() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(), path );
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check file contents", unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
block|}
block|}
end_class

end_unit

