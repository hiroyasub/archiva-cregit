begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|proxy
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusTestCase
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
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|ProxyRequestHandlerTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ProxyRequestHandler
name|requestHandler
decl_stmt|;
specifier|public
name|void
name|testdummy
parameter_list|()
block|{
block|}
comment|/* TODO!     protected void setUp()         throws Exception     {         super.setUp();          requestHandler = (ProxyRequestHandler) container.lookup( ProxyRequestHandler.ROLE );     }      public void testArtifactDownload()         throws Exception     {         //test download         String s = "/commons-logging/commons-logging/1.0/commons-logging-1.0.jar";         File file = get( s );         assertTrue( "File must be downloaded.", file.exists() );         assertTrue( "Downloaded file should be present in the cache.",                     file.getAbsolutePath().startsWith( managedRepository.getBasedir() ) );          //test cache         get( "/commons-logging/commons-logging/1.0/commons-logging-1.0.jar" );          try         {             get( "/commons-logging/commons-logging/2.0/commons-logging-2.0.jar" );             fail( "Expected ResourceDoesNotExistException exception not thrown" );         }         catch ( ResourceDoesNotExistException e )         {             assertTrue( true );         }     }      private File get( String s )         throws ProxyException, ResourceDoesNotExistException     {         return requestHandler.get( s, proxiedRepositories, managedRepository );     }      public void testArtifactChecksum()         throws Exception     {         //force the downlod from the remote repository, use getAlways()         File file = requestHandler.getAlways( "/commons-logging/commons-logging/1.0/commons-logging-1.0.jar.md5" );         assertTrue( "File must be downloaded.", file.exists() );         assertTrue( "Downloaded file should be present in the cache.",                     file.getAbsolutePath().startsWith( managedRepository.getBasedir() ) );     }      public void testNonArtifactWithNoChecksum()         throws Exception     {         File file = get( "/not-standard/repository/file.txt" );         assertTrue( "File must be downloaded.", file.exists() );         assertTrue( "Downloaded file should be present in the cache.",                     file.getAbsolutePath().startsWith( managedRepository.getBasedir() ) );     }      public void testNonArtifactWithMD5Checksum()         throws Exception     {         File file = get( "/checksumed-md5/repository/file.txt" );         assertTrue( "File must be downloaded.", file.exists() );         assertTrue( "Downloaded file should be present in the cache.",                     file.getAbsolutePath().startsWith( managedRepository.getBasedir() ) );     }      public void testNonArtifactWithSHA1Checksum()         throws Exception     {         File file = get( "/checksumed-sha1/repository/file.txt" );         assertTrue( "File must be downloaded.", file.exists() );         assertTrue( "Downloaded file should be present in the cache.",                     file.getAbsolutePath().startsWith( managedRepository.getBasedir() ) );     }      private ProxyConfiguration getProxyConfiguration()         throws ComponentLookupException     {         ProxyConfiguration config = new ProxyConfiguration();          config.setRepositoryCachePath( "target/requestHandler-cache" );          ArtifactRepositoryLayout defLayout = new DefaultRepositoryLayout();          File repo1File = getTestFile( "src/test/remote-repo1" );          ProxyRepository repo1 = new ProxyRepository( "test-repo", "file://" + repo1File.getAbsolutePath(), defLayout );          config.addRepository( repo1 );          return config;     } */
block|}
end_class

end_unit

