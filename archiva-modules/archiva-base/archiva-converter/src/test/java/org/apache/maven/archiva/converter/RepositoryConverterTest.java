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
name|converter
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
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|converter
operator|.
name|legacy
operator|.
name|LegacyRepositoryConverter
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
name|ArtifactRepository
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
name|ArtifactRepositoryFactory
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
name|ArtifactRepositoryLayout
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
name|javax
operator|.
name|inject
operator|.
name|Named
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

begin_comment
comment|/**  * Test the repository converter.  *  * @todo what about deletions from the source repository?  * @todo use artifact-test instead  * @todo should reject if dependencies are missing - rely on reporting?  * @todo group metadata  */
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
name|RepositoryConverterTest
extends|extends
name|TestCase
block|{
specifier|private
name|ArtifactRepository
name|sourceRepository
decl_stmt|;
specifier|private
name|ManagedRepositoryConfiguration
name|targetRepository
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"legacyRepositoryConverter#default"
argument_list|)
specifier|private
name|LegacyRepositoryConverter
name|repositoryConverter
decl_stmt|;
annotation|@
name|Inject
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
annotation|@
name|Before
specifier|public
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
name|ArtifactRepositoryFactory
name|factory
init|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|ArtifactRepositoryFactory
operator|.
name|class
argument_list|)
decl_stmt|;
comment|//(ArtifactRepositoryFactory) lookup( ArtifactRepositoryFactory.ROLE );
name|ArtifactRepositoryLayout
name|layout
init|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|ArtifactRepositoryLayout
operator|.
name|class
argument_list|,
literal|"legacy"
argument_list|)
decl_stmt|;
comment|//(ArtifactRepositoryLayout) lookup( ArtifactRepositoryLayout.ROLE, "legacy" );
name|File
name|sourceBase
init|=
operator|new
name|File
argument_list|(
literal|"src/test/source-repository"
argument_list|)
decl_stmt|;
name|sourceRepository
operator|=
name|factory
operator|.
name|createArtifactRepository
argument_list|(
literal|"source"
argument_list|,
name|sourceBase
operator|.
name|toURL
argument_list|()
operator|.
name|toString
argument_list|()
argument_list|,
name|layout
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|layout
operator|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|ArtifactRepositoryLayout
operator|.
name|class
argument_list|,
literal|"default"
argument_list|)
expr_stmt|;
comment|//(ArtifactRepositoryLayout) lookup( ArtifactRepositoryLayout.ROLE, "default" );
name|File
name|targetBase
init|=
operator|new
name|File
argument_list|(
literal|"target/test-target-repository"
argument_list|)
decl_stmt|;
name|copyDirectoryStructure
argument_list|(
operator|new
name|File
argument_list|(
literal|"src/test/target-repository"
argument_list|)
argument_list|,
name|targetBase
argument_list|)
expr_stmt|;
name|targetRepository
operator|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
expr_stmt|;
name|targetRepository
operator|.
name|setId
argument_list|(
literal|"target"
argument_list|)
expr_stmt|;
name|targetRepository
operator|.
name|setName
argument_list|(
literal|"Target Repo"
argument_list|)
expr_stmt|;
name|targetRepository
operator|.
name|setLocation
argument_list|(
name|targetBase
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|targetRepository
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
comment|//repositoryConverter = (LegacyRepositoryConverter) lookup( LegacyRepositoryConverter.ROLE, "default" );
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|copyDirectoryStructure
parameter_list|(
name|File
name|sourceDirectory
parameter_list|,
name|File
name|destinationDirectory
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
operator|!
name|sourceDirectory
operator|.
name|exists
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Source directory doesn't exists ("
operator|+
name|sourceDirectory
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|")."
argument_list|)
throw|;
block|}
name|File
index|[]
name|files
init|=
name|sourceDirectory
operator|.
name|listFiles
argument_list|()
decl_stmt|;
name|String
name|sourcePath
init|=
name|sourceDirectory
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|files
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|File
name|file
init|=
name|files
index|[
name|i
index|]
decl_stmt|;
name|String
name|dest
init|=
name|file
operator|.
name|getAbsolutePath
argument_list|()
decl_stmt|;
name|dest
operator|=
name|dest
operator|.
name|substring
argument_list|(
name|sourcePath
operator|.
name|length
argument_list|()
operator|+
literal|1
argument_list|)
expr_stmt|;
name|File
name|destination
init|=
operator|new
name|File
argument_list|(
name|destinationDirectory
argument_list|,
name|dest
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|destination
operator|=
name|destination
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|copyFileToDirectory
argument_list|(
name|file
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|file
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
literal|".svn"
operator|.
name|equals
argument_list|(
name|file
operator|.
name|getName
argument_list|()
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|destination
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|destination
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Could not create destination directory '"
operator|+
name|destination
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"'."
argument_list|)
throw|;
block|}
name|copyDirectoryStructure
argument_list|(
name|file
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Unknown file type: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testLegacyConversion
parameter_list|()
throws|throws
name|IOException
throws|,
name|RepositoryConversionException
block|{
name|File
name|legacyRepoDir
init|=
operator|new
name|File
argument_list|(
name|sourceRepository
operator|.
name|getBasedir
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|destRepoDir
init|=
operator|new
name|File
argument_list|(
name|targetRepository
operator|.
name|getLocation
argument_list|()
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|excludes
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|repositoryConverter
operator|.
name|convertLegacyRepository
argument_list|(
name|legacyRepoDir
argument_list|,
name|destRepoDir
argument_list|,
name|excludes
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

