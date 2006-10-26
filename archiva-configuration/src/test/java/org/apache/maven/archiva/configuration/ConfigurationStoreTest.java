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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * Test the configuration store.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @noinspection JavaDoc  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationStoreTest
extends|extends
name|PlexusTestCase
block|{
specifier|public
name|void
name|testInvalidFile
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigurationStore
name|configurationStore
init|=
operator|(
name|ConfigurationStore
operator|)
name|lookup
argument_list|(
name|ConfigurationStore
operator|.
name|ROLE
argument_list|,
literal|"invalid-file"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|configurationStore
operator|.
name|getConfigurationFromStore
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
name|assertEquals
argument_list|(
literal|"check configuration has default elements"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
name|configuration
operator|.
name|getIndexerCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"check configuration has default elements"
argument_list|,
name|configuration
operator|.
name|getIndexPath
argument_list|()
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
name|testCorruptFile
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigurationStore
name|configurationStore
init|=
operator|(
name|ConfigurationStore
operator|)
name|lookup
argument_list|(
name|ConfigurationStore
operator|.
name|ROLE
argument_list|,
literal|"corrupt-file"
argument_list|)
decl_stmt|;
try|try
block|{
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
expr_stmt|;
name|fail
argument_list|(
literal|"Configuration should not have succeeded"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ConfigurationStoreException
name|e
parameter_list|)
block|{
comment|// expected
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testGetConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigurationStore
name|configurationStore
init|=
operator|(
name|ConfigurationStore
operator|)
name|lookup
argument_list|(
name|ConfigurationStore
operator|.
name|ROLE
argument_list|,
literal|"default"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check indexPath"
argument_list|,
literal|".index"
argument_list|,
name|configuration
operator|.
name|getIndexPath
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check localRepository"
argument_list|,
literal|"local-repository"
argument_list|,
name|configuration
operator|.
name|getLocalRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|1
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
literal|"managed-repository"
argument_list|,
name|repository
operator|.
name|getDirectory
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"local"
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
literal|"local"
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
name|assertEquals
argument_list|(
literal|"check proxied repositories"
argument_list|,
literal|1
argument_list|,
name|configuration
operator|.
name|getProxiedRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|ProxiedRepositoryConfiguration
name|proxiedRepository
init|=
operator|(
name|ProxiedRepositoryConfiguration
operator|)
name|configuration
operator|.
name|getProxiedRepositories
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
literal|"check proxied repositories"
argument_list|,
literal|"local"
argument_list|,
name|proxiedRepository
operator|.
name|getManagedRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check proxied repositories"
argument_list|,
literal|"http://www.ibiblio.org/maven2/"
argument_list|,
name|proxiedRepository
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check proxied repositories"
argument_list|,
literal|"ibiblio"
argument_list|,
name|proxiedRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check proxied repositories"
argument_list|,
literal|"Ibiblio"
argument_list|,
name|proxiedRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check proxied repositories"
argument_list|,
literal|0
argument_list|,
name|proxiedRepository
operator|.
name|getSnapshotsInterval
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check proxied repositories"
argument_list|,
literal|0
argument_list|,
name|proxiedRepository
operator|.
name|getReleasesInterval
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check proxied repositories"
argument_list|,
name|proxiedRepository
operator|.
name|isUseNetworkProxy
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check synced repositories"
argument_list|,
literal|1
argument_list|,
name|configuration
operator|.
name|getSyncedRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|SyncedRepositoryConfiguration
name|syncedRepository
init|=
operator|(
name|SyncedRepositoryConfiguration
operator|)
name|configuration
operator|.
name|getSyncedRepositories
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
literal|"check synced repositories"
argument_list|,
literal|"local"
argument_list|,
name|syncedRepository
operator|.
name|getManagedRepository
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check synced repositories"
argument_list|,
literal|"apache"
argument_list|,
name|syncedRepository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check synced repositories"
argument_list|,
literal|"ASF"
argument_list|,
name|syncedRepository
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check synced repositories"
argument_list|,
literal|"0 0 * * * ?"
argument_list|,
name|syncedRepository
operator|.
name|getCronExpression
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check synced repositories"
argument_list|,
literal|"rsync"
argument_list|,
name|syncedRepository
operator|.
name|getMethod
argument_list|()
argument_list|)
expr_stmt|;
name|Properties
name|properties
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"rsyncHost"
argument_list|,
literal|"host"
argument_list|)
expr_stmt|;
name|properties
operator|.
name|setProperty
argument_list|(
literal|"rsyncMethod"
argument_list|,
literal|"ssh"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check synced repositories"
argument_list|,
name|properties
argument_list|,
name|syncedRepository
operator|.
name|getProperties
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testStoreConfiguration
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigurationStore
name|configurationStore
init|=
operator|(
name|ConfigurationStore
operator|)
name|lookup
argument_list|(
name|ConfigurationStore
operator|.
name|ROLE
argument_list|,
literal|"save-file"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|configuration
operator|.
name|setIndexPath
argument_list|(
literal|"index-path"
argument_list|)
expr_stmt|;
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
name|configurationStore
operator|.
name|storeConfiguration
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
comment|// read it back
name|configuration
operator|=
name|configurationStore
operator|.
name|getConfigurationFromStore
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check value"
argument_list|,
literal|"index-path"
argument_list|,
name|configuration
operator|.
name|getIndexPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * @noinspection JUnitTestMethodWithNoAssertions      */
specifier|public
name|void
name|testChangeListeners
parameter_list|()
throws|throws
name|Exception
block|{
name|ConfigurationStore
name|configurationStore
init|=
operator|(
name|ConfigurationStore
operator|)
name|lookup
argument_list|(
name|ConfigurationStore
operator|.
name|ROLE
argument_list|,
literal|"save-file"
argument_list|)
decl_stmt|;
name|MockControl
name|control
init|=
name|MockControl
operator|.
name|createControl
argument_list|(
name|ConfigurationChangeListener
operator|.
name|class
argument_list|)
decl_stmt|;
name|ConfigurationChangeListener
name|mock
init|=
operator|(
name|ConfigurationChangeListener
operator|)
name|control
operator|.
name|getMock
argument_list|()
decl_stmt|;
name|configurationStore
operator|.
name|addChangeListener
argument_list|(
name|mock
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|()
decl_stmt|;
name|mock
operator|.
name|notifyOfConfigurationChange
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|()
expr_stmt|;
name|configurationStore
operator|.
name|storeConfiguration
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

