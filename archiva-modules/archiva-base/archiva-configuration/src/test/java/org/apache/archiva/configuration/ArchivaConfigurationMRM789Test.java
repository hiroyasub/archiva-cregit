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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|test
operator|.
name|utils
operator|.
name|ArchivaSpringJUnit4ClassRunner
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
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
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

begin_comment
comment|/**  * Test the configuration store.  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
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
name|ArchivaConfigurationMRM789Test
block|{
annotation|@
name|Inject
specifier|protected
name|ApplicationContext
name|applicationContext
decl_stmt|;
annotation|@
name|Inject
name|FileTypes
name|filetypes
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
argument_list|()
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
operator|(
name|T
operator|)
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"archivaConfiguration#"
operator|+
name|hint
argument_list|,
name|ArchivaConfiguration
operator|.
name|class
argument_list|)
return|;
block|}
comment|// test for [MRM-789]
annotation|@
name|Test
specifier|public
name|void
name|testGetConfigurationFromDefaultsWithDefaultRepoLocationAlreadyExisting
parameter_list|()
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
argument_list|()
argument_list|,
literal|"target/test-classes/existing_snapshots"
argument_list|)
decl_stmt|;
name|repo
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|repo
operator|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"target/test-classes/existing_internal"
argument_list|)
expr_stmt|;
name|repo
operator|.
name|mkdirs
argument_list|()
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
argument_list|()
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
argument_list|()
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
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
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
argument_list|()
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
name|ManagedRepositoryConfiguration
name|repository
init|=
name|configuration
operator|.
name|getManagedRepositories
argument_list|()
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
argument_list|()
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
argument_list|()
expr_stmt|;
name|assertFalse
argument_list|(
name|generatedTestDefaultArchivaConfigFile
operator|.
name|exists
argument_list|()
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
name|assertEquals
argument_list|(
literal|"check managed repositories: "
operator|+
name|configuration
operator|.
name|getManagedRepositories
argument_list|()
argument_list|,
name|managedExpected
argument_list|,
name|configuration
operator|.
name|getManagedRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check remote repositories: "
operator|+
name|configuration
operator|.
name|getRemoteRepositories
argument_list|()
argument_list|,
name|remoteExpected
argument_list|,
name|configuration
operator|.
name|getRemoteRepositories
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check proxy connectors:"
operator|+
name|configuration
operator|.
name|getProxyConnectors
argument_list|()
argument_list|,
name|proxyConnectorExpected
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
literal|"check known consumers"
argument_list|,
literal|9
argument_list|,
name|repoScanning
operator|.
name|getKnownContentConsumers
argument_list|()
operator|.
name|size
argument_list|()
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
argument_list|()
operator|.
name|size
argument_list|()
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
argument_list|()
argument_list|)
expr_stmt|;
name|WebappConfiguration
name|webapp
init|=
name|configuration
operator|.
name|getWebapp
argument_list|()
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
argument_list|()
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
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check appletFindEnabled"
argument_list|,
name|ui
operator|.
name|isAppletFindEnabled
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

