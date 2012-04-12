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
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|common
operator|.
name|utils
operator|.
name|FileUtil
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
name|redback
operator|.
name|components
operator|.
name|registry
operator|.
name|RegistryException
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
name|codehaus
operator|.
name|redback
operator|.
name|components
operator|.
name|springutils
operator|.
name|ComponentContainer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|XMLAssert
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

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Test the configuration store.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|ArchivaConfigurationTest
extends|extends
name|TestCase
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|( )
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|ComponentContainer
name|componentContainer
decl_stmt|;
specifier|public
specifier|static
name|File
name|getTestFile
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|( )
argument_list|,
name|path
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|String
name|hint
parameter_list|)
block|{
return|return
name|componentContainer
operator|.
name|getComponent
argument_list|(
name|clazz
argument_list|,
name|hint
argument_list|)
return|;
block|}
specifier|protected
parameter_list|<
name|T
parameter_list|>
name|T
name|lookup
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|)
block|{
return|return
name|componentContainer
operator|.
name|getComponent
argument_list|(
name|clazz
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationFromRegistryWithASingleNamedConfigurationResource
parameter_list|( )
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
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
argument_list|( )
decl_stmt|;
name|assertConfiguration
argument_list|(
name|configuration
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|,
literal|2
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
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"${appserver.base}/repositories/internal"
argument_list|,
name|repository
operator|.
name|getLocation
argument_list|( )
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
argument_list|( )
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
argument_list|( )
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isScanned
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationFromDefaults
parameter_list|( )
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
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertConfiguration
argument_list|(
name|configuration
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check network proxies"
argument_list|,
literal|0
argument_list|,
name|configuration
operator|.
name|getNetworkProxies
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
operator|(
name|ManagedRepositoryConfiguration
operator|)
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"${appserver.base}/data/repositories/internal"
argument_list|,
name|repository
operator|.
name|getLocation
argument_list|( )
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
argument_list|( )
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
argument_list|( )
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isScanned
argument_list|( )
argument_list|)
expr_stmt|;
block|}
comment|// test for [MRM-789]
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationFromDefaultsWithDefaultRepoLocationAlreadyExisting
parameter_list|( )
throws|throws
name|Exception
block|{
name|File
name|repo
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|( )
argument_list|,
literal|"/target/test-classes/existing_snapshots"
argument_list|)
decl_stmt|;
name|repo
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|repo
operator|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|( )
argument_list|,
literal|"/target/test-classes/existing_internal"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|String
name|existingTestDefaultArchivaConfigFile
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|getTestFile
argument_list|(
literal|"target/test-classes/org/apache/archiva/configuration/test-default-archiva.xml"
argument_list|)
argument_list|)
decl_stmt|;
name|existingTestDefaultArchivaConfigFile
operator|=
name|StringUtils
operator|.
name|replace
argument_list|(
name|existingTestDefaultArchivaConfigFile
argument_list|,
literal|"${appserver.base}"
argument_list|,
name|FileUtil
operator|.
name|getBasedir
argument_list|( )
argument_list|)
expr_stmt|;
name|File
name|generatedTestDefaultArchivaConfigFile
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|( )
argument_list|,
literal|"target/test-classes/org/apache/archiva/configuration/default-archiva.xml"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|generatedTestDefaultArchivaConfigFile
argument_list|,
name|existingTestDefaultArchivaConfigFile
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-defaults-default-repo-location-exists"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertConfiguration
argument_list|(
name|configuration
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
operator|(
name|ManagedRepositoryConfiguration
operator|)
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|getLocation
argument_list|( )
operator|.
name|endsWith
argument_list|(
literal|"data/repositories/internal"
argument_list|)
argument_list|)
expr_stmt|;
name|generatedTestDefaultArchivaConfigFile
operator|.
name|delete
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|generatedTestDefaultArchivaConfigFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
throws|throws
name|Exception
block|{
name|assertConfiguration
argument_list|(
name|configuration
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|,
literal|1
argument_list|)
expr_stmt|;
block|}
comment|/**      * Ensures that the provided configuration matches the details present in the archiva-default.xml file.      */
specifier|private
name|void
name|assertConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|,
name|int
name|managedExpected
parameter_list|,
name|int
name|remoteExpected
parameter_list|,
name|int
name|proxyConnectorExpected
parameter_list|)
throws|throws
name|Exception
block|{
name|FileTypes
name|filetypes
init|=
name|lookup
argument_list|(
name|FileTypes
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories: "
operator|+
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
argument_list|,
name|managedExpected
argument_list|,
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check remote repositories: "
operator|+
name|configuration
operator|.
name|getRemoteRepositories
argument_list|( )
argument_list|,
name|remoteExpected
argument_list|,
name|configuration
operator|.
name|getRemoteRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check proxy connectors:"
operator|+
name|configuration
operator|.
name|getProxyConnectors
argument_list|( )
argument_list|,
name|proxyConnectorExpected
argument_list|,
name|configuration
operator|.
name|getProxyConnectors
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|RepositoryScanningConfiguration
name|repoScanning
init|=
name|configuration
operator|.
name|getRepositoryScanning
argument_list|( )
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
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check known consumers"
argument_list|,
literal|9
argument_list|,
name|repoScanning
operator|.
name|getKnownContentConsumers
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check invalid consumers"
argument_list|,
literal|1
argument_list|,
name|repoScanning
operator|.
name|getInvalidContentConsumers
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
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
argument_list|( )
argument_list|)
expr_stmt|;
name|WebappConfiguration
name|webapp
init|=
name|configuration
operator|.
name|getWebapp
argument_list|( )
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"check webapp"
argument_list|,
name|webapp
argument_list|)
expr_stmt|;
name|UserInterfaceOptions
name|ui
init|=
name|webapp
operator|.
name|getUi
argument_list|( )
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"check webapp ui"
argument_list|,
name|ui
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check showFindArtifacts"
argument_list|,
name|ui
operator|.
name|isShowFindArtifacts
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check appletFindEnabled"
argument_list|,
name|ui
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationFromRegistryWithTwoConfigurationResources
parameter_list|( )
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-configuration-both"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
comment|// from base
name|assertEquals
argument_list|(
literal|"check repositories"
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check repositories"
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getRemoteRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
comment|// from user
name|assertEquals
argument_list|(
literal|"check proxy connectors"
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getProxyConnectors
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|WebappConfiguration
name|webapp
init|=
name|configuration
operator|.
name|getWebapp
argument_list|( )
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"check webapp"
argument_list|,
name|webapp
argument_list|)
expr_stmt|;
name|UserInterfaceOptions
name|ui
init|=
name|webapp
operator|.
name|getUi
argument_list|( )
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"check webapp ui"
argument_list|,
name|ui
argument_list|)
expr_stmt|;
comment|// from base
name|assertFalse
argument_list|(
literal|"check showFindArtifacts"
argument_list|,
name|ui
operator|.
name|isShowFindArtifacts
argument_list|( )
argument_list|)
expr_stmt|;
comment|// from user
name|assertFalse
argument_list|(
literal|"check appletFindEnabled"
argument_list|,
name|ui
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationSystemOverride
parameter_list|( )
throws|throws
name|Exception
block|{
name|System
operator|.
name|setProperty
argument_list|(
literal|"org.apache.archiva.webapp.ui.appletFindEnabled"
argument_list|,
literal|"false"
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-configuration"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
try|try
block|{
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertFalse
argument_list|(
literal|"check boolean"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|System
operator|.
name|getProperties
argument_list|( )
operator|.
name|remove
argument_list|(
literal|"org.apache.archiva.webapp.ui.appletFindEnabled"
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreConfiguration
parameter_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|file
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
comment|// TODO: remove with commons-configuration 1.4
comment|//file.getParentFile().mkdirs();
comment|//FileUtils.writeStringToFile( file, "<configuration/>", null );
name|DefaultArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|DefaultArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-save"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setVersion
argument_list|(
literal|"1"
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|setWebapp
argument_list|(
operator|new
name|WebappConfiguration
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|setUi
argument_list|(
operator|new
name|UserInterfaceOptions
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|setAppletFindEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// add a change listener
name|MockControl
name|control
init|=
name|createConfigurationListenerMockControl
argument_list|( )
decl_stmt|;
name|ConfigurationListener
name|listener
init|=
operator|(
name|ConfigurationListener
operator|)
name|control
operator|.
name|getMock
argument_list|( )
decl_stmt|;
name|archivaConfiguration
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|configurationEvent
argument_list|(
operator|new
name|ConfigurationEvent
argument_list|(
name|ConfigurationEvent
operator|.
name|SAVED
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|setVoidCallable
argument_list|( )
expr_stmt|;
name|control
operator|.
name|replay
argument_list|( )
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|( )
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file exists"
argument_list|,
name|file
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
comment|// read it back
name|archivaConfiguration
operator|=
operator|(
name|DefaultArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-read-saved"
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|MockControl
name|createConfigurationListenerMockControl
parameter_list|( )
block|{
return|return
name|MockControl
operator|.
name|createControl
argument_list|(
name|ConfigurationListener
operator|.
name|class
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreConfigurationUser
parameter_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|userFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|userFile
argument_list|,
literal|"<configuration/>"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-save-user"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setWebapp
argument_list|(
operator|new
name|WebappConfiguration
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|setUi
argument_list|(
operator|new
name|UserInterfaceOptions
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|setAppletFindEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check file not created"
argument_list|,
name|baseFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreConfigurationLoadedFromDefaults
parameter_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-save-user-defaults"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setWebapp
argument_list|(
operator|new
name|WebappConfiguration
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|setUi
argument_list|(
operator|new
name|UserInterfaceOptions
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|setAppletFindEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
comment|// add a change listener
name|MockControl
name|control
init|=
name|createConfigurationListenerMockControl
argument_list|( )
decl_stmt|;
name|ConfigurationListener
name|listener
init|=
operator|(
name|ConfigurationListener
operator|)
name|control
operator|.
name|getMock
argument_list|( )
decl_stmt|;
name|archivaConfiguration
operator|.
name|addListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|listener
operator|.
name|configurationEvent
argument_list|(
operator|new
name|ConfigurationEvent
argument_list|(
name|ConfigurationEvent
operator|.
name|SAVED
argument_list|)
argument_list|)
expr_stmt|;
name|control
operator|.
name|setVoidCallable
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|control
operator|.
name|replay
argument_list|( )
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|control
operator|.
name|verify
argument_list|( )
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file exists"
argument_list|,
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check file not created"
argument_list|,
name|baseFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultUserConfigFilename
parameter_list|( )
throws|throws
name|Exception
block|{
name|DefaultArchivaConfiguration
name|archivaConfiguration
init|=
operator|(
name|DefaultArchivaConfiguration
operator|)
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"default"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|assertEquals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"user.home"
argument_list|)
operator|+
literal|"/.m2/archiva.xml"
argument_list|,
name|archivaConfiguration
operator|.
name|getUserConfigFilename
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|System
operator|.
name|getProperty
argument_list|(
literal|"appserver.base"
argument_list|,
literal|"${appserver.base}"
argument_list|)
operator|+
literal|"/conf/archiva.xml"
argument_list|,
name|archivaConfiguration
operator|.
name|getAltConfigFilename
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreConfigurationFallback
parameter_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|baseFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|baseFile
argument_list|,
literal|"<configuration/>"
argument_list|,
literal|null
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
argument_list|,
literal|"test-save-user-fallback"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
operator|new
name|Configuration
argument_list|( )
decl_stmt|;
name|configuration
operator|.
name|setWebapp
argument_list|(
operator|new
name|WebappConfiguration
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|setUi
argument_list|(
operator|new
name|UserInterfaceOptions
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|setAppletFindEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check file not created"
argument_list|,
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreConfigurationFailsWhenReadFromBothLocationsNoLists
parameter_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|baseFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|baseFile
argument_list|,
literal|"<configuration/>"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|userFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|userFile
argument_list|,
literal|"<configuration/>"
argument_list|,
literal|null
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
argument_list|,
literal|"test-save-user"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|setAppletFindEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check base file is unchanged"
argument_list|,
literal|"<configuration/>"
argument_list|,
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|baseFile
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file exists"
argument_list|,
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check base file is changed"
argument_list|,
literal|"<configuration/>"
operator|.
name|equals
argument_list|(
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|userFile
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreConfigurationFailsWhenReadFromBothLocationsUserHasLists
parameter_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|userFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|getTestFile
argument_list|(
literal|"src/test/conf/conf-user.xml"
argument_list|)
argument_list|,
name|userFile
argument_list|)
expr_stmt|;
name|baseFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|baseFile
argument_list|,
literal|"<configuration/>"
argument_list|,
literal|null
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
argument_list|,
literal|"test-save-user"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isShowFindArtifacts
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|setShowFindArtifacts
argument_list|(
literal|false
argument_list|)
expr_stmt|;
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check base file is unchanged"
argument_list|,
literal|"<configuration/>"
argument_list|,
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|baseFile
argument_list|,
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check file exists"
argument_list|,
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check base file is changed"
argument_list|,
literal|"<configuration/>"
operator|.
name|equals
argument_list|(
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|userFile
argument_list|,
literal|null
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
comment|// check it
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isShowFindArtifacts
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testStoreConfigurationFailsWhenReadFromBothLocationsAppserverHasLists
parameter_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|baseFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|getTestFile
argument_list|(
literal|"src/test/conf/conf-base.xml"
argument_list|)
argument_list|,
name|baseFile
argument_list|)
expr_stmt|;
name|userFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|userFile
argument_list|,
literal|"<configuration/>"
argument_list|,
literal|null
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
argument_list|,
literal|"test-save-user"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|setAppletFindEnabled
argument_list|(
literal|false
argument_list|)
expr_stmt|;
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Configuration saving should not succeed if it was loaded from two locations"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndeterminateConfigurationException
name|e
parameter_list|)
block|{
comment|// check it was reverted
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check value"
argument_list|,
name|configuration
operator|.
name|getWebapp
argument_list|( )
operator|.
name|getUi
argument_list|( )
operator|.
name|isAppletFindEnabled
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadConfigurationFromInvalidBothLocationsOnDisk
parameter_list|( )
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
literal|"test-not-allowed-to-write-to-both"
argument_list|)
decl_stmt|;
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a RegistryException because the configuration can't be saved."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
comment|/* expected exception */
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLoadConfigurationFromInvalidUserLocationOnDisk
parameter_list|( )
throws|throws
name|Exception
block|{
name|File
name|testConfDir
init|=
name|getTestFile
argument_list|(
literal|"target/test-appserver-base/conf/"
argument_list|)
decl_stmt|;
name|testConfDir
operator|.
name|mkdirs
argument_list|( )
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
argument_list|,
literal|"test-not-allowed-to-write-to-user"
argument_list|)
decl_stmt|;
name|Configuration
name|config
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|config
argument_list|)
expr_stmt|;
comment|// No Exception == test passes.
comment|// Expected Path is: Should not have thrown an exception.
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConfigurationUpgradeFrom09
parameter_list|( )
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-upgrade-09"
argument_list|)
decl_stmt|;
comment|// we just use the defaults when upgrading from 0.9 at this point.
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
comment|// test-upgrade-09 contains a managed with id: local so it's 3 managed
name|assertConfiguration
argument_list|(
name|configuration
argument_list|,
literal|3
argument_list|,
literal|1
argument_list|,
literal|1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check network proxies"
argument_list|,
literal|0
argument_list|,
name|configuration
operator|.
name|getNetworkProxies
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"${appserver.base}/data/repositories/internal"
argument_list|,
name|repository
operator|.
name|getLocation
argument_list|( )
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
argument_list|( )
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
argument_list|( )
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isScanned
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testConfigurationUpgradeFrom13
parameter_list|( )
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-upgrade-1.3"
argument_list|)
decl_stmt|;
comment|// we just use the defaults when upgrading from 1.3 at this point.
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertConfiguration
argument_list|(
name|configuration
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check network proxies"
argument_list|,
literal|0
argument_list|,
name|configuration
operator|.
name|getNetworkProxies
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"${appserver.base}/data/repositories/internal"
argument_list|,
name|repository
operator|.
name|getLocation
argument_list|( )
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
argument_list|( )
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
argument_list|( )
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isScanned
argument_list|( )
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"knowContentConsumers "
operator|+
name|configuration
operator|.
name|getRepositoryScanning
argument_list|( )
operator|.
name|getKnownContentConsumers
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|configuration
operator|.
name|getRepositoryScanning
argument_list|( )
operator|.
name|getKnownContentConsumers
argument_list|( )
operator|.
name|contains
argument_list|(
literal|"update-db-artifact"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|configuration
operator|.
name|getRepositoryScanning
argument_list|( )
operator|.
name|getKnownContentConsumers
argument_list|( )
operator|.
name|contains
argument_list|(
literal|"update-db-repository-metadata"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getRepositoryScanning
argument_list|( )
operator|.
name|getKnownContentConsumers
argument_list|( )
operator|.
name|contains
argument_list|(
literal|"create-archiva-metadata"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getRepositoryScanning
argument_list|( )
operator|.
name|getKnownContentConsumers
argument_list|( )
operator|.
name|contains
argument_list|(
literal|"duplicate-artifacts"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testAutoDetectV1
parameter_list|( )
throws|throws
name|Exception
block|{
comment|// Setup the autodetect-v1.xml file in the target directory (so we can save/load it)
name|File
name|userFile
init|=
name|getTestFile
argument_list|(
literal|"target/test-autodetect-v1/archiva-user.xml"
argument_list|)
decl_stmt|;
name|userFile
operator|.
name|delete
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|userFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|getTestFile
argument_list|(
literal|"src/test/conf/autodetect-v1.xml"
argument_list|)
argument_list|,
name|userFile
argument_list|)
expr_stmt|;
comment|// Load the original (unconverted) archiva.xml
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-autodetect-v1"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertConfiguration
argument_list|(
name|configuration
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|,
literal|2
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
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"${appserver.base}/repositories/internal"
argument_list|,
name|repository
operator|.
name|getLocation
argument_list|( )
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
argument_list|( )
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
argument_list|( )
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isScanned
argument_list|( )
argument_list|)
expr_stmt|;
comment|// Test that only 1 set of repositories exist.
name|assertEquals
argument_list|(
literal|"check managed repositories size."
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check remote repositories size."
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getRemoteRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check v1 repositories size."
argument_list|,
literal|0
argument_list|,
name|configuration
operator|.
name|getRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
comment|// Save the file.
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
comment|// Release existing
comment|//release( archivaConfiguration );
comment|// Reload.
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
argument_list|,
literal|"test-autodetect-v1"
argument_list|)
expr_stmt|;
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
comment|// Test that only 1 set of repositories exist.
name|assertEquals
argument_list|(
literal|"check managed repositories size."
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories size."
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getManagedRepositoriesAsMap
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check remote repositories size."
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getRemoteRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check remote repositories size."
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getRemoteRepositoriesAsMap
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check v1 repositories size."
argument_list|,
literal|0
argument_list|,
name|configuration
operator|.
name|getRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|String
name|actualXML
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|userFile
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|XMLAssert
operator|.
name|assertXpathNotExists
argument_list|(
literal|"//configuration/repositories/repository"
argument_list|,
name|actualXML
argument_list|)
expr_stmt|;
name|XMLAssert
operator|.
name|assertXpathNotExists
argument_list|(
literal|"//configuration/repositories"
argument_list|,
name|actualXML
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testArchivaV1
parameter_list|( )
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-archiva-v1"
argument_list|)
decl_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertConfiguration
argument_list|(
name|configuration
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|,
literal|2
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
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check v1 repositories size."
argument_list|,
literal|0
argument_list|,
name|configuration
operator|.
name|getRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepositoryConfiguration
argument_list|>
name|map
init|=
name|configuration
operator|.
name|getManagedRepositoriesAsMap
argument_list|( )
decl_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
name|map
operator|.
name|get
argument_list|(
literal|"internal"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"${appserver.base}/repositories/internal"
argument_list|,
name|repository
operator|.
name|getLocation
argument_list|( )
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
argument_list|( )
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
argument_list|( )
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isScanned
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isSnapshots
argument_list|( )
argument_list|)
expr_stmt|;
name|repository
operator|=
name|map
operator|.
name|get
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"${appserver.base}/repositories/snapshots"
argument_list|,
name|repository
operator|.
name|getLocation
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"Archiva Managed Snapshot Repository"
argument_list|,
name|repository
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check managed repositories"
argument_list|,
literal|"snapshots"
argument_list|,
name|repository
operator|.
name|getId
argument_list|( )
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
argument_list|( )
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isScanned
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check managed repositories"
argument_list|,
name|repository
operator|.
name|isSnapshots
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testCronExpressionsWithComma
parameter_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|baseFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|getTestFile
argument_list|(
literal|"src/test/conf/escape-cron-expressions.xml"
argument_list|)
argument_list|,
name|baseFile
argument_list|)
expr_stmt|;
name|userFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|userFile
argument_list|,
literal|"<configuration/>"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
specifier|final
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-cron-expressions"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|ManagedRepositoryConfiguration
name|repository
init|=
operator|(
name|ManagedRepositoryConfiguration
operator|)
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check cron expression"
argument_list|,
literal|"0 0,30 * * * ?"
argument_list|,
name|repository
operator|.
name|getRefreshCronExpression
argument_list|( )
operator|.
name|trim
argument_list|( )
argument_list|)
expr_stmt|;
comment|// add a test listener to confirm it doesn't see the escaped format. We don't need to test the number of calls,
comment|// etc. as it's done in other tests
name|archivaConfiguration
operator|.
name|addListener
argument_list|(
operator|new
name|ConfigurationListener
argument_list|( )
block|{
specifier|public
name|void
name|configurationEvent
parameter_list|(
name|ConfigurationEvent
name|event
parameter_list|)
block|{
name|assertEquals
argument_list|(
name|ConfigurationEvent
operator|.
name|SAVED
argument_list|,
name|event
operator|.
name|getType
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
comment|// test for the escape character '\' showing up on repositories.jsp
name|repository
operator|.
name|setRefreshCronExpression
argument_list|(
literal|"0 0,20 0 * * ?"
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|repository
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
operator|.
name|findManagedRepositoryById
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check cron expression"
argument_list|,
literal|"0 0,20 0 * * ?"
argument_list|,
name|repository
operator|.
name|getRefreshCronExpression
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testRemoveLastElements
parameter_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|baseFile
operator|.
name|exists
argument_list|( )
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
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|userFile
operator|.
name|exists
argument_list|( )
argument_list|)
expr_stmt|;
name|baseFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|getTestFile
argument_list|(
literal|"src/test/conf/conf-single-list-elements.xml"
argument_list|)
argument_list|,
name|baseFile
argument_list|)
expr_stmt|;
name|userFile
operator|.
name|getParentFile
argument_list|( )
operator|.
name|mkdirs
argument_list|( )
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|userFile
argument_list|,
literal|"<configuration/>"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-remove-central"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|RepositoryGroupConfiguration
name|repositoryGroup
init|=
operator|(
name|RepositoryGroupConfiguration
operator|)
name|configuration
operator|.
name|getRepositoryGroups
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|removeRepositoryGroup
argument_list|(
name|repositoryGroup
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getRepositoryGroups
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
name|repository
init|=
name|configuration
operator|.
name|getRemoteRepositoriesAsMap
argument_list|( )
operator|.
name|get
argument_list|(
literal|"central"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|removeRemoteRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getRemoteRepositories
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|ManagedRepositoryConfiguration
name|managedRepository
init|=
name|configuration
operator|.
name|getManagedRepositoriesAsMap
argument_list|( )
operator|.
name|get
argument_list|(
literal|"snapshots"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|removeManagedRepository
argument_list|(
name|managedRepository
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getManagedRepositories
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|ProxyConnectorConfiguration
name|proxyConnector
init|=
name|configuration
operator|.
name|getProxyConnectors
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|proxyConnector
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|removeProxyConnector
argument_list|(
name|proxyConnector
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getProxyConnectors
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|NetworkProxyConfiguration
name|networkProxy
init|=
name|configuration
operator|.
name|getNetworkProxiesAsMap
argument_list|( )
operator|.
name|get
argument_list|(
literal|"proxy"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|networkProxy
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|removeNetworkProxy
argument_list|(
name|networkProxy
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getNetworkProxies
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|LegacyArtifactPath
name|path
init|=
name|configuration
operator|.
name|getLegacyArtifactPaths
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|configuration
operator|.
name|removeLegacyArtifactPath
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getLegacyArtifactPaths
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|RepositoryScanningConfiguration
name|scanning
init|=
name|configuration
operator|.
name|getRepositoryScanning
argument_list|( )
decl_stmt|;
name|String
name|consumer
init|=
name|scanning
operator|.
name|getKnownContentConsumers
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|scanning
operator|.
name|removeKnownContentConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
comment|// default values
name|assertFalse
argument_list|(
name|scanning
operator|.
name|getKnownContentConsumers
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|consumer
operator|=
name|scanning
operator|.
name|getInvalidContentConsumers
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|scanning
operator|.
name|removeInvalidContentConsumer
argument_list|(
name|consumer
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|scanning
operator|.
name|getInvalidContentConsumers
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|archivaConfiguration
operator|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-read-saved"
argument_list|)
expr_stmt|;
name|configuration
operator|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
expr_stmt|;
name|assertNull
argument_list|(
name|configuration
operator|.
name|getRemoteRepositoriesAsMap
argument_list|( )
operator|.
name|get
argument_list|(
literal|"central"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getRepositoryGroups
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|configuration
operator|.
name|getManagedRepositoriesAsMap
argument_list|( )
operator|.
name|get
argument_list|(
literal|"snapshots"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getProxyConnectors
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|configuration
operator|.
name|getNetworkProxiesAsMap
argument_list|( )
operator|.
name|get
argument_list|(
literal|"proxy"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|configuration
operator|.
name|getLegacyArtifactPaths
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|scanning
operator|=
name|configuration
operator|.
name|getRepositoryScanning
argument_list|( )
expr_stmt|;
name|assertFalse
argument_list|(
name|scanning
operator|.
name|getKnownContentConsumers
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|scanning
operator|.
name|getInvalidContentConsumers
argument_list|( )
operator|.
name|isEmpty
argument_list|( )
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-582] Remote Repositories with empty<username> and<password> fields shouldn't be created in configuration.      */
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationFixEmptyRemoteRepoUsernamePassword
parameter_list|( )
throws|throws
name|Exception
block|{
name|ArchivaConfiguration
name|archivaConfiguration
init|=
name|lookup
argument_list|(
name|ArchivaConfiguration
operator|.
name|class
argument_list|,
literal|"test-configuration"
argument_list|)
decl_stmt|;
name|archivaConfiguration
operator|.
name|reload
argument_list|( )
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|( )
decl_stmt|;
name|assertConfiguration
argument_list|(
name|configuration
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|,
literal|2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check remote repositories"
argument_list|,
literal|2
argument_list|,
name|configuration
operator|.
name|getRemoteRepositories
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|RemoteRepositoryConfiguration
name|repository
init|=
name|configuration
operator|.
name|getRemoteRepositoriesAsMap
argument_list|( )
operator|.
name|get
argument_list|(
literal|"maven2-repository.dev.java.net"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"remote repository.url"
argument_list|,
literal|"https://maven2-repository.dev.java.net/nonav/repository"
argument_list|,
name|repository
operator|.
name|getUrl
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"remote repository.name"
argument_list|,
literal|"Java.net Repository for Maven 2"
argument_list|,
name|repository
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"remote repository.id"
argument_list|,
literal|"maven2-repository.dev.java.net"
argument_list|,
name|repository
operator|.
name|getId
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"remote repository.layout"
argument_list|,
literal|"default"
argument_list|,
name|repository
operator|.
name|getLayout
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"remote repository.username == null"
argument_list|,
name|repository
operator|.
name|getUsername
argument_list|( )
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"remote repository.password == null"
argument_list|,
name|repository
operator|.
name|getPassword
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

