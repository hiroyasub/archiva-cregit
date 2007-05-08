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
name|configuration
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|List
import|;
end_import

begin_comment
comment|/**  * Test the configuration store.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaConfigurationTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
specifier|public
name|void
name|testDefaults
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-defaults"
argument_list|)
decl_stmt|;
name|filetypes
operator|=
operator|(
name|FileTypes
operator|)
name|lookup
argument_list|(
name|FileTypes
operator|.
name|class
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
comment|// check default configuration
name|assertNotNull
argument_list|(
literal|"check configuration returned"
argument_list|,
name|configuration
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check configuration has default elements"
argument_list|,
name|configuration
operator|.
name|getRepositories
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test-configuration"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check repositories"
argument_list|,
literal|4
argument_list|,
name|configuration
operator|.
name|getRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check proxy connectors"
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getProxyConnectors
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check network proxies"
argument_list|,
literal|1
argument_list|,
name|configuration
operator|.
name|getNetworkProxies
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryScanningConfiguration
name|repoScanning
init|=
name|configuration
operator|.
name|getRepositoryScanning
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"check repository scanning"
argument_list|,
name|repoScanning
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check file types"
argument_list|,
literal|4
argument_list|,
name|repoScanning
operator|.
name|getFileTypes
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check good consumers"
argument_list|,
literal|8
argument_list|,
name|repoScanning
operator|.
name|getGoodConsumers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check bad consumers"
argument_list|,
literal|1
argument_list|,
name|repoScanning
operator|.
name|getBadConsumers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
name|patterns
init|=
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
literal|"artifacts"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"check 'artifacts' file type"
argument_list|,
name|patterns
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check 'artifacts' patterns"
argument_list|,
literal|13
argument_list|,
name|patterns
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DatabaseScanningConfiguration
name|dbScanning
init|=
name|configuration
operator|.
name|getDatabaseScanning
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"check database scanning"
argument_list|,
name|dbScanning
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check unprocessed consumers"
argument_list|,
literal|6
argument_list|,
name|dbScanning
operator|.
name|getUnprocessedConsumers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check processed consumers"
argument_list|,
literal|3
argument_list|,
name|dbScanning
operator|.
name|getProcessedConsumers
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|RepositoryConfiguration
name|repository
init|=
operator|(
name|RepositoryConfiguration
operator|)
name|configuration
operator|.
name|getRepositories
argument_list|()
operator|.
name|iterator
argument_list|()
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"file://${appserver.home}/repositories/internal"
argument_list|,
name|repository
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"Archiva Managed Internal Repository"
argument_list|,
name|repository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"internal"
argument_list|,
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"default"
argument_list|,
name|repository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isIndexed
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetConfigurationSystemOverride
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test-configuration"
argument_list|)
decl_stmt|;
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.maven.archiva.localRepository"
argument_list|,
literal|"system-repository"
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
comment|//        assertEquals( "check localRepository", "system-repository", configuration.getLocalRepository() );
comment|//        assertEquals( "check indexPath", ".index", configuration.getIndexPath() );
block|}
specifier|public
name|void
name|testStoreConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
name|getTestFile
argument_list|(
literal|"target/test/test-file.xml"
argument_list|)
decl_stmt|;
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: remove with commons-configuration 1.4
name|file
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|FileUtils
operator|.
name|fileWrite
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"<configuration/>"
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test-save"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
comment|//        configuration.setIndexPath( "index-path" );
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file exists"
argument_list|,
name|file
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
comment|//        assertEquals( "check value", "index-path", configuration.getIndexPath() );
comment|// read it back
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test-read-saved"
argument_list|)
expr_stmt|;
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
comment|//        assertEquals( "check value", "index-path", configuration.getIndexPath() );
block|}
specifier|public
name|void
name|testStoreConfigurationUser
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|baseFile
init|=
name|getTestFile
argument_list|(
literal|"target/test/test-file.xml"
argument_list|)
decl_stmt|;
name|baseFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|userFile
init|=
name|getTestFile
argument_list|(
literal|"target/test/test-file-user.xml"
argument_list|)
decl_stmt|;
name|userFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: remove with commons-configuration 1.4
name|userFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|FileUtils
operator|.
name|fileWrite
argument_list|(
name|userFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"<configuration/>"
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test-save-user"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
comment|//        configuration.setIndexPath( "index-path" );
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file exists"
argument_list|,
name|userFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check file not created"
argument_list|,
name|baseFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
comment|//        assertEquals( "check value", "index-path", configuration.getIndexPath() );
block|}
specifier|public
name|void
name|testStoreConfigurationFallback
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|baseFile
init|=
name|getTestFile
argument_list|(
literal|"target/test/test-file.xml"
argument_list|)
decl_stmt|;
name|baseFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|userFile
init|=
name|getTestFile
argument_list|(
literal|"target/test/test-file-user.xml"
argument_list|)
decl_stmt|;
name|userFile
operator|.
name|delete
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// TODO: remove with commons-configuration 1.4
name|baseFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|util
operator|.
name|FileUtils
operator|.
name|fileWrite
argument_list|(
name|baseFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"<configuration/>"
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test-save-user"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
comment|//        configuration.setIndexPath( "index-path" );
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file exists"
argument_list|,
name|baseFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check file not created"
argument_list|,
name|userFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
comment|//        assertEquals( "check value", "index-path", configuration.getIndexPath() );
block|}
specifier|public
name|void
name|testRemoveProxiedRepositoryAndStoreConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
comment|// MRM-300
name|File
name|src
init|=
name|getTestFile
argument_list|(
literal|"src/test/conf/with-proxied-repos.xml"
argument_list|)
decl_stmt|;
name|File
name|dest
init|=
name|getTestFile
argument_list|(
literal|"target/test/with-proxied-repos.xml"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|src
argument_list|,
name|dest
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test-remove-proxied-repo"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
comment|//        configuration.getProxiedRepositories().remove( 0 );
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
comment|//        assertEquals( 1, configuration.getProxiedRepositories().size() );
name|release
argument_list|(
name|archivaConfiguration
argument_list|)
expr_stmt|;
comment|// read it back
name|archivaConfiguration
operator|=
operator|(
name|ArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test-read-back-remove-proxied-repo"
argument_list|)
expr_stmt|;
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
expr_stmt|;
comment|//        assertEquals( 1, configuration.getProxiedRepositories().size() );
block|}
block|}
end_class

end_unit

