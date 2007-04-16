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
comment|/**  * ManagedLegacyTransferTest   *  * @author Brett Porter  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ManagedLegacyTransferTest
extends|extends
name|AbstractProxyTestCase
block|{
specifier|public
name|void
name|testLegacyManagedRepoGetNotPresent
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|path
init|=
literal|"org.apache.maven.test/jars/get-default-layout-1.0.jar"
decl_stmt|;
name|File
name|expectedFile
init|=
operator|new
name|File
argument_list|(
name|managedLegacyDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|ArtifactReference
name|artifact
init|=
name|createArtifactReference
argument_list|(
literal|"legacy"
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
name|ID_LEGACY_MANAGED
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
literal|"org/apache/maven/test/get-default-layout/1.0/get-default-layout-1.0.jar"
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
comment|// TODO: timestamp preservation requires support for that in wagon
comment|//    assertEquals( "Check file timestamp", proxiedFile.lastModified(), file.lastModified() );
block|}
specifier|public
name|void
name|testLegacyManagedRepoGetAlreadyPresent
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
comment|//        String path = "org.apache.maven.test/jars/get-default-layout-present-1.0.jar";
comment|//        File expectedFile = new File( legacyManagedRepository.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( expectedFile, null );
comment|//        long originalModificationTime = expectedFile.lastModified();
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, legacyManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//        File proxiedFile = new File( proxiedRepository1.getBasedir(),
comment|//                                     "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar" );
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check file contents", unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
comment|//        assertFalse( "Check file timestamp is not that of proxy", proxiedFile.lastModified() == file.lastModified() );
comment|//        assertEquals( "Check file timestamp is that of original managed file", originalModificationTime, file
comment|//            .lastModified() );
block|}
specifier|public
name|void
name|testLegacyProxyRepoGetNotPresent
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
comment|//        String path = "org/apache/maven/test/get-default-layout/1.0/get-default-layout-1.0.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//
comment|//        expectedFile.delete();
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, legacyProxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        File proxiedFile = new File( legacyProxiedRepository.getBasedir(),
comment|//                                     "org.apache.maven.test/jars/get-default-layout-1.0.jar" );
comment|//        String expectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|// TODO: timestamp preservation requires support for that in wagon
comment|//    assertEquals( "Check file timestamp", proxiedFile.lastModified(), file.lastModified() );
block|}
specifier|public
name|void
name|testLegacyProxyRepoGetAlreadyPresent
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
comment|//        String path = "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( expectedFile, null );
comment|//        long originalModificationTime = expectedFile.lastModified();
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, legacyProxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//        File proxiedFile = new File( legacyProxiedRepository.getBasedir(),
comment|//                                     "org.apache.maven.test/jars/get-default-layout-present-1.0.jar" );
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check file contents", unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
comment|//        assertFalse( "Check file timestamp is not that of proxy", proxiedFile.lastModified() == file.lastModified() );
comment|//        assertEquals( "Check file timestamp is that of original managed file", originalModificationTime, file
comment|//            .lastModified() );
block|}
specifier|public
name|void
name|testLegacyManagedAndProxyRepoGetNotPresent
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
comment|//        String path = "org.apache.maven.test/jars/get-default-layout-1.0.jar";
comment|//        File expectedFile = new File( legacyManagedRepository.getBasedir(), path );
comment|//
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, legacyProxiedRepositories, legacyManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        File proxiedFile = new File( legacyProxiedRepository.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|// TODO: timestamp preservation requires support for that in wagon
comment|//    assertEquals( "Check file timestamp", proxiedFile.lastModified(), file.lastModified() );
block|}
specifier|public
name|void
name|testLegacyManagedAndProxyRepoGetAlreadyPresent
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
comment|//        String path = "org.apache.maven.test/jars/get-default-layout-present-1.0.jar";
comment|//        File expectedFile = new File( legacyManagedRepository.getBasedir(), path );
comment|//        String expectedContents = FileUtils.readFileToString( expectedFile, null );
comment|//        long originalModificationTime = expectedFile.lastModified();
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, legacyProxiedRepositories, legacyManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//        assertTrue( "Check file created", file.exists() );
comment|//        assertEquals( "Check file contents", expectedContents, FileUtils.readFileToString( file, null ) );
comment|//        File proxiedFile = new File( legacyProxiedRepository.getBasedir(), path );
comment|//        String unexpectedContents = FileUtils.readFileToString( proxiedFile, null );
comment|//        assertFalse( "Check file contents", unexpectedContents.equals( FileUtils.readFileToString( file, null ) ) );
comment|//        assertFalse( "Check file timestamp is not that of proxy", proxiedFile.lastModified() == file.lastModified() );
comment|//        assertEquals( "Check file timestamp is that of original managed file", originalModificationTime, file
comment|//            .lastModified() );
block|}
specifier|public
name|void
name|testLegacyRequestConvertedToDefaultPathInManagedRepo
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
comment|// Check that a Maven1 legacy request is translated to a maven2 path in
comment|// the managed repository.
comment|//        String legacyPath = "org.apache.maven.test/jars/get-default-layout-present-1.0.jar";
comment|//        String path = "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), path );
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( legacyPath, legacyProxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
block|}
specifier|public
name|void
name|testDefaultRequestConvertedToLegacyPathInManagedRepo
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
comment|// Check that a Maven2 default request is translated to a legacy path in
comment|// the managed repository.
comment|//        String legacyPath = "org.apache.maven.test/jars/get-default-layout-present-1.0.jar";
comment|//        String path = "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar";
comment|//        File expectedFile = new File( legacyManagedRepository.getBasedir(), legacyPath );
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, legacyManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
block|}
block|}
end_class

end_unit

