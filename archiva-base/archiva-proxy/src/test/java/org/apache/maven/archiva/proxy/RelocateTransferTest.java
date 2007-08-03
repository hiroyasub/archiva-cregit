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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|RepositoryConfiguration
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
name|ArchivaRepository
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
name|ResourceDoesNotExistException
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|Wagon
import|;
end_import

begin_import
import|import
name|org
operator|.
name|easymock
operator|.
name|MockControl
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_comment
comment|/**  * RelocateTransferTest   *  * @author Brett Porter  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RelocateTransferTest
extends|extends
name|AbstractProxyTestCase
block|{
specifier|public
name|void
name|testRelocateMaven1Request
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
comment|//        String path = "org.apache.maven.test/jars/get-relocated-artefact-1.0.jar";
comment|//        String relocatedPath = "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), relocatedPath );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
block|}
specifier|public
name|void
name|testDoublyRelocateMaven1Request
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
comment|//        String path = "org.apache.maven.test/jars/get-doubly-relocated-artefact-1.0.jar";
comment|//        String relocatedPath = "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), relocatedPath );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
block|}
specifier|public
name|void
name|testRelocateMaven1PomRequest
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
comment|//        String path = "org.apache.maven.test/poms/get-relocated-artefact-with-pom-1.0.pom";
comment|//        String relocatedPath = "org/apache/maven/test/get-default-layout-present-with-pom/1.0/get-default-layout-present-with-pom-1.0.pom";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), relocatedPath );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//
comment|//        assertTrue( expectedFile.exists() );
block|}
specifier|public
name|void
name|testRelocateMaven1PomRequestMissingTarget
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
comment|//        String path = "org.apache.maven.test/poms/get-relocated-artefact-1.0.pom";
comment|//        String relocatedPath = "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.pom";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), relocatedPath );
comment|//
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        try
comment|//        {
comment|//            requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//            fail( "Should have failed to find target POM" );
comment|//        }
comment|//        catch ( ResourceDoesNotExistException e )
comment|//        {
comment|//            assertTrue( true );
comment|//        }
block|}
specifier|public
name|void
name|testRelocateMaven1ChecksumRequest
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
comment|//        String path = "org.apache.maven.test/jars/get-relocated-artefact-1.0.jar.md5";
comment|//        String relocatedPath = "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar.md5";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), relocatedPath );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        path = "org.apache.maven.test/jars/get-relocated-artefact-1.0.jar.sha1";
comment|//        relocatedPath = "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar.sha1";
comment|//        expectedFile = new File( defaultManagedRepository.getBasedir(), relocatedPath );
comment|//
comment|//        assertFalse( expectedFile.exists() );
comment|//
comment|//        try
comment|//        {
comment|//            requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//            fail( "Checksum was not present, should not be found" );
comment|//        }
comment|//        catch ( ResourceDoesNotExistException e )
comment|//        {
comment|//            assertTrue( true );
comment|//        }
block|}
specifier|public
name|void
name|testRelocateMaven2Request
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
comment|//        String path = "org/apache/maven/test/get-relocated-artefact/1.0/get-relocated-artefact-1.0.jar";
comment|//        String relocatedPath = "org/apache/maven/test/get-default-layout-present/1.0/get-default-layout-present-1.0.jar";
comment|//        File expectedFile = new File( defaultManagedRepository.getBasedir(), relocatedPath );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, defaultManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
block|}
specifier|public
name|void
name|testRelocateMaven2RequestInLegacyManagedRepo
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
comment|//        String path = "org/apache/maven/test/get-relocated-artefact/1.0/get-relocated-artefact-1.0.jar";
comment|//        String relocatedPath = "org.apache.maven.test/jars/get-default-layout-present-1.0.jar";
comment|//        File expectedFile = new File( legacyManagedRepository.getBasedir(), relocatedPath );
comment|//
comment|//        assertTrue( expectedFile.exists() );
comment|//
comment|//        File file = requestHandler.get( path, proxiedRepositories, legacyManagedRepository );
comment|//
comment|//        assertEquals( "Check file matches", expectedFile, file );
block|}
block|}
end_class

end_unit

