begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|test
operator|.
name|utils
operator|.
name|ArchivaBlockJUnit4ClassRunner
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

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Before
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
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
argument_list|)
specifier|public
class|class
name|MavenProxyPropertyLoaderTest
block|{
specifier|private
name|MavenProxyPropertyLoader
name|loader
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testLoadValidMavenProxyConfiguration
parameter_list|()
throws|throws
name|IOException
throws|,
name|InvalidConfigurationException
block|{
name|Path
name|confFile
init|=
name|ArchivaConfigurationTest
operator|.
name|getTestFile
argument_list|(
literal|"src/test/conf/maven-proxy-complete.conf"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|NetworkProxyConfiguration
name|proxy
init|=
operator|new
name|NetworkProxyConfiguration
argument_list|()
decl_stmt|;
name|proxy
operator|.
name|setHost
argument_list|(
literal|"original-host"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|addNetworkProxy
argument_list|(
name|proxy
argument_list|)
expr_stmt|;
comment|// overwritten
name|loader
operator|.
name|load
argument_list|(
name|Files
operator|.
name|newInputStream
argument_list|(
name|confFile
argument_list|)
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepositoryConfiguration
argument_list|>
name|repositoryIdMap
init|=
name|configuration
operator|.
name|getManagedRepositoriesAsMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Count repositories"
argument_list|,
literal|1
argument_list|,
name|repositoryIdMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertRepositoryExists
argument_list|(
literal|"maven-proxy"
argument_list|,
literal|"target"
argument_list|,
name|repositoryIdMap
operator|.
name|get
argument_list|(
literal|"maven-proxy"
argument_list|)
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|RemoteRepositoryConfiguration
argument_list|>
name|remoteRepositoryMap
init|=
name|configuration
operator|.
name|getRemoteRepositoriesAsMap
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Count repositories"
argument_list|,
literal|4
argument_list|,
name|remoteRepositoryMap
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertRepositoryExists
argument_list|(
literal|"local-repo"
argument_list|,
literal|"file://target"
argument_list|,
name|remoteRepositoryMap
operator|.
name|get
argument_list|(
literal|"local-repo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertRepositoryExists
argument_list|(
literal|"www-ibiblio-org"
argument_list|,
literal|"http://www.ibiblio.org/maven2"
argument_list|,
name|remoteRepositoryMap
operator|.
name|get
argument_list|(
literal|"www-ibiblio-org"
argument_list|)
argument_list|)
expr_stmt|;
name|assertRepositoryExists
argument_list|(
literal|"dist-codehaus-org"
argument_list|,
literal|"http://dist.codehaus.org"
argument_list|,
name|remoteRepositoryMap
operator|.
name|get
argument_list|(
literal|"dist-codehaus-org"
argument_list|)
argument_list|)
expr_stmt|;
name|assertRepositoryExists
argument_list|(
literal|"private-example-com"
argument_list|,
literal|"http://private.example.com/internal"
argument_list|,
name|remoteRepositoryMap
operator|.
name|get
argument_list|(
literal|"private-example-com"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertRepositoryExists
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|expectedLocation
parameter_list|,
name|ManagedRepositoryConfiguration
name|repo
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Repository id ["
operator|+
name|id
operator|+
literal|"] should not be null"
argument_list|,
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository id"
argument_list|,
name|id
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository url"
argument_list|,
name|expectedLocation
argument_list|,
name|repo
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertRepositoryExists
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|expectedUrl
parameter_list|,
name|RemoteRepositoryConfiguration
name|repo
parameter_list|)
block|{
name|assertNotNull
argument_list|(
literal|"Repository id ["
operator|+
name|id
operator|+
literal|"] should not be null"
argument_list|,
name|repo
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository id"
argument_list|,
name|id
argument_list|,
name|repo
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Repository url"
argument_list|,
name|expectedUrl
argument_list|,
name|repo
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
argument_list|(
name|expected
operator|=
name|InvalidConfigurationException
operator|.
name|class
argument_list|)
specifier|public
name|void
name|testInvalidConfiguration
parameter_list|()
throws|throws
name|InvalidConfigurationException
block|{
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|loader
operator|.
name|load
argument_list|(
operator|new
name|Properties
argument_list|()
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
comment|//fail( "Incomplete config should have failed" );
block|}
annotation|@
name|Before
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|loader
operator|=
operator|new
name|MavenProxyPropertyLoader
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

