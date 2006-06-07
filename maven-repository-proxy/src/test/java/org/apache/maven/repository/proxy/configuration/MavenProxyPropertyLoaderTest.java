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
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusTestCase
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
name|util
operator|.
name|FileUtils
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
name|FileInputStream
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

begin_comment
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|MavenProxyPropertyLoaderTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
specifier|static
specifier|final
name|int
name|DEFAULT_CACHE_PERIOD
init|=
literal|3600
decl_stmt|;
specifier|public
name|void
name|testLoadValidMavenProxyConfiguration
parameter_list|()
throws|throws
name|ValidationException
throws|,
name|IOException
block|{
name|MavenProxyPropertyLoader
name|loader
init|=
operator|new
name|MavenProxyPropertyLoader
argument_list|()
decl_stmt|;
comment|//must create the test directory bec configuration is using relative path which varies
name|FileUtils
operator|.
name|mkdir
argument_list|(
literal|"target/remote-repo1"
argument_list|)
expr_stmt|;
try|try
block|{
name|File
name|confFile
init|=
name|getTestFile
argument_list|(
literal|"src/test/conf/maven-proxy-complete.conf"
argument_list|)
decl_stmt|;
name|ProxyConfiguration
name|config
init|=
name|loader
operator|.
name|load
argument_list|(
operator|new
name|FileInputStream
argument_list|(
name|confFile
argument_list|)
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"cache path changed"
argument_list|,
name|config
operator|.
name|getRepositoryCachePath
argument_list|()
operator|.
name|endsWith
argument_list|(
literal|"target"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Count repositories"
argument_list|,
literal|4
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
name|ProxyRepository
name|repo
init|=
operator|(
name|ProxyRepository
operator|)
name|config
operator|.
name|getRepositories
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Repository name not as expected"
argument_list|,
literal|"local-repo"
argument_list|,
name|repo
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository url does not match its name"
argument_list|,
literal|"file://target"
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository cache period check failed"
argument_list|,
literal|0
argument_list|,
name|repo
operator|.
name|getCachePeriod
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Repository failure caching check failed"
argument_list|,
name|repo
operator|.
name|isCacheFailures
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|=
operator|(
name|ProxyRepository
operator|)
name|config
operator|.
name|getRepositories
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository name not as expected"
argument_list|,
literal|"www-ibiblio-org"
argument_list|,
name|repo
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository url does not match its name"
argument_list|,
literal|"http://www.ibiblio.org/maven2"
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository cache period check failed"
argument_list|,
name|DEFAULT_CACHE_PERIOD
argument_list|,
name|repo
operator|.
name|getCachePeriod
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Repository failure caching check failed"
argument_list|,
name|repo
operator|.
name|isCacheFailures
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|=
operator|(
name|ProxyRepository
operator|)
name|config
operator|.
name|getRepositories
argument_list|()
operator|.
name|get
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository name not as expected"
argument_list|,
literal|"dist-codehaus-org"
argument_list|,
name|repo
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository url does not match its name"
argument_list|,
literal|"http://dist.codehaus.org"
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository cache period check failed"
argument_list|,
name|DEFAULT_CACHE_PERIOD
argument_list|,
name|repo
operator|.
name|getCachePeriod
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Repository failure caching check failed"
argument_list|,
name|repo
operator|.
name|isCacheFailures
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|=
operator|(
name|ProxyRepository
operator|)
name|config
operator|.
name|getRepositories
argument_list|()
operator|.
name|get
argument_list|(
literal|3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository name not as expected"
argument_list|,
literal|"private-example-com"
argument_list|,
name|repo
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository url does not match its name"
argument_list|,
literal|"http://private.example.com/internal"
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository cache period check failed"
argument_list|,
name|DEFAULT_CACHE_PERIOD
argument_list|,
name|repo
operator|.
name|getCachePeriod
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Repository failure caching check failed"
argument_list|,
name|repo
operator|.
name|isCacheFailures
argument_list|()
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
comment|//make sure to delete the test directory after tests
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
literal|"target/remote-repo1"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

