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
operator|.
name|configuration
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
name|artifact
operator|.
name|repository
operator|.
name|layout
operator|.
name|ArtifactRepositoryLayout
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
name|artifact
operator|.
name|repository
operator|.
name|layout
operator|.
name|DefaultRepositoryLayout
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
name|artifact
operator|.
name|repository
operator|.
name|layout
operator|.
name|LegacyRepositoryLayout
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
name|repository
operator|.
name|proxy
operator|.
name|repository
operator|.
name|ProxyRepository
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
name|proxy
operator|.
name|ProxyInfo
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_class
specifier|public
class|class
name|ProxyConfigurationTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ProxyConfiguration
name|config
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|config
operator|=
operator|(
name|ProxyConfiguration
operator|)
name|container
operator|.
name|lookup
argument_list|(
name|ProxyConfiguration
operator|.
name|ROLE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRepositoryCache
parameter_list|()
block|{
name|File
name|cacheFile
init|=
operator|new
name|File
argument_list|(
literal|"target/proxy-cache"
argument_list|)
decl_stmt|;
name|config
operator|.
name|setRepositoryCachePath
argument_list|(
name|cacheFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|config
operator|.
name|getRepositoryCachePath
argument_list|()
argument_list|,
name|cacheFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRepositories
parameter_list|()
block|{
name|ArtifactRepositoryLayout
name|defLayout
init|=
operator|new
name|DefaultRepositoryLayout
argument_list|()
decl_stmt|;
name|ProxyRepository
name|repo1
init|=
operator|new
name|ProxyRepository
argument_list|(
literal|"repo1"
argument_list|,
literal|"http://www.ibiblio.org/maven2"
argument_list|,
name|defLayout
argument_list|)
decl_stmt|;
name|repo1
operator|.
name|setCacheFailures
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|repo1
operator|.
name|setCachePeriod
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|repo1
operator|.
name|setHardfail
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|addRepository
argument_list|(
name|repo1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|config
operator|.
name|getRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ArtifactRepositoryLayout
name|legacyLayout
init|=
operator|new
name|LegacyRepositoryLayout
argument_list|()
decl_stmt|;
name|ProxyRepository
name|repo2
init|=
operator|new
name|ProxyRepository
argument_list|(
literal|"repo2"
argument_list|,
literal|"http://www.ibiblio.org/maven"
argument_list|,
name|legacyLayout
argument_list|)
decl_stmt|;
name|repo2
operator|.
name|setCacheFailures
argument_list|(
literal|false
argument_list|)
expr_stmt|;
name|repo2
operator|.
name|setCachePeriod
argument_list|(
literal|3600
argument_list|)
expr_stmt|;
name|repo2
operator|.
name|setProxied
argument_list|(
literal|true
argument_list|)
expr_stmt|;
name|config
operator|.
name|setHttpProxy
argument_list|(
literal|"some.local.proxy"
argument_list|,
literal|80
argument_list|,
literal|"username"
argument_list|,
literal|"password"
argument_list|)
expr_stmt|;
name|config
operator|.
name|addRepository
argument_list|(
name|repo2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|config
operator|.
name|getRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|repositories
init|=
name|config
operator|.
name|getRepositories
argument_list|()
decl_stmt|;
name|ProxyRepository
name|repo
init|=
operator|(
name|ProxyRepository
operator|)
name|repositories
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"repo1"
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.ibiblio.org/maven2"
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|isCacheFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|isHardfail
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|repo
operator|.
name|getCachePeriod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|repo1
argument_list|,
name|repo
argument_list|)
expr_stmt|;
name|repo
operator|=
operator|(
name|ProxyRepository
operator|)
name|repositories
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"repo2"
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"http://www.ibiblio.org/maven"
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repo
operator|.
name|isCacheFailures
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repo
operator|.
name|isHardfail
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3600
argument_list|,
name|repo
operator|.
name|getCachePeriod
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|repo2
argument_list|,
name|repo
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repo
operator|.
name|isProxied
argument_list|()
argument_list|)
expr_stmt|;
name|ProxyInfo
name|proxyInfo
init|=
name|config
operator|.
name|getHttpProxy
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
name|proxyInfo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"some.local.proxy"
argument_list|,
name|proxyInfo
operator|.
name|getHost
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|80
argument_list|,
name|proxyInfo
operator|.
name|getPort
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"username"
argument_list|,
name|proxyInfo
operator|.
name|getUserName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"password"
argument_list|,
name|proxyInfo
operator|.
name|getPassword
argument_list|()
argument_list|)
expr_stmt|;
try|try
block|{
name|repositories
operator|.
name|add
argument_list|(
operator|new
name|ProxyRepository
argument_list|(
literal|"repo"
argument_list|,
literal|"url"
argument_list|,
name|defLayout
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected UnsupportedOperationException not thrown."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|java
operator|.
name|lang
operator|.
name|UnsupportedOperationException
name|e
parameter_list|)
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
name|repositories
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
name|repositories
operator|.
name|add
argument_list|(
name|repo1
argument_list|)
expr_stmt|;
name|repositories
operator|.
name|add
argument_list|(
name|repo2
argument_list|)
expr_stmt|;
name|config
operator|.
name|setRepositories
argument_list|(
name|repositories
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|repositories
argument_list|,
name|config
operator|.
name|getRepositories
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|//    public void testLoadValidMavenProxyConfiguration()
comment|//        throws ValidationException, IOException
comment|//    {
comment|//        //must create the test directory bec configuration is using relative path which varies
comment|//        FileUtils.mkdir( "target/remote-repo1" );
comment|//
comment|//        try
comment|//        {
comment|//            File confFile = getTestFile( "src/test/conf/maven-proxy-complete.conf" );
comment|//
comment|//            config.loadMavenProxyConfiguration( confFile );
comment|//
comment|//            assertTrue( "cache path changed", config.getRepositoryCachePath().endsWith( "target" ) );
comment|//
comment|//            assertEquals( "Count repositories", 4, config.getRepositories().size() );
comment|//
comment|//            int idx = 0;
comment|//            for ( Iterator repos = config.getRepositories().iterator(); repos.hasNext(); )
comment|//            {
comment|//                idx++;
comment|//
comment|//                ProxyRepository repo = (ProxyRepository) repos.next();
comment|//
comment|//                //switch is made to check for ordering
comment|//                switch ( idx )
comment|//                {
comment|//                    case 1:
comment|//                        assertEquals( "Repository name not as expected", "local-repo", repo.getKey() );
comment|//                        assertEquals( "Repository url does not match its name", "file:///./target/remote-repo1",
comment|//                                      repo.getUrl() );
comment|//                        assertEquals( "Repository cache period check failed", 0, repo.getCachePeriod() );
comment|//                        assertFalse( "Repository failure caching check failed", repo.isCacheFailures() );
comment|//                        break;
comment|//                    case 2:
comment|//                        assertEquals( "Repository name not as expected", "www-ibiblio-org", repo.getKey() );
comment|//                        assertEquals( "Repository url does not match its name", "http://www.ibiblio.org/maven2",
comment|//                                      repo.getUrl() );
comment|//                        assertEquals( "Repository cache period check failed", 3600, repo.getCachePeriod() );
comment|//                        assertTrue( "Repository failure caching check failed", repo.isCacheFailures() );
comment|//                        break;
comment|//                    case 3:
comment|//                        assertEquals( "Repository name not as expected", "dist-codehaus-org", repo.getKey() );
comment|//                        assertEquals( "Repository url does not match its name", "http://dist.codehaus.org",
comment|//                                      repo.getUrl() );
comment|//                        assertEquals( "Repository cache period check failed", 3600, repo.getCachePeriod() );
comment|//                        assertTrue( "Repository failure caching check failed", repo.isCacheFailures() );
comment|//                        break;
comment|//                    case 4:
comment|//                        assertEquals( "Repository name not as expected", "private-example-com", repo.getKey() );
comment|//                        assertEquals( "Repository url does not match its name", "http://private.example.com/internal",
comment|//                                      repo.getUrl() );
comment|//                        assertEquals( "Repository cache period check failed", 3600, repo.getCachePeriod() );
comment|//                        assertFalse( "Repository failure caching check failed", repo.isCacheFailures() );
comment|//                        break;
comment|//                    default:
comment|//                        fail( "Unexpected order count" );
comment|//                }
comment|//            }
comment|//        }
comment|//        //make sure to delete the test directory after tests
comment|//        finally
comment|//        {
comment|//            FileUtils.deleteDirectory( "target/remote-repo1" );
comment|//        }
comment|//    }
comment|//
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|config
operator|=
literal|null
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

