begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
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
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|VersionComparator
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
name|archiva
operator|.
name|configuration
operator|.
name|FileType
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
name|configuration
operator|.
name|FileTypes
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
name|archiva
operator|.
name|model
operator|.
name|VersionedReference
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
name|repository
operator|.
name|EditableManagedRepository
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
name|repository
operator|.
name|ManagedRepositoryContent
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
name|repository
operator|.
name|content
operator|.
name|maven2
operator|.
name|ManagedDefaultRepositoryContent
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
name|repository
operator|.
name|layout
operator|.
name|LayoutException
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
name|repository
operator|.
name|maven2
operator|.
name|MavenManagedRepository
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
name|nio
operator|.
name|file
operator|.
name|Paths
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
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Set
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
name|assertEquals
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
name|fail
import|;
end_import

begin_comment
comment|/**  * ManagedDefaultRepositoryContentTest  */
end_comment

begin_class
specifier|public
class|class
name|ManagedDefaultRepositoryContentTest
extends|extends
name|AbstractDefaultRepositoryContentTestCase
block|{
specifier|private
name|ManagedDefaultRepositoryContent
name|repoContent
decl_stmt|;
annotation|@
name|Inject
name|FileTypes
name|fileTypes
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
literal|"archivaConfiguration#default"
argument_list|)
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
annotation|@
name|Inject
name|List
argument_list|<
name|?
extends|extends
name|ArtifactMappingProvider
argument_list|>
name|artifactMappingProviders
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
name|Path
name|repoDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/repositories/default-repository"
argument_list|)
decl_stmt|;
name|MavenManagedRepository
name|repository
init|=
name|createRepository
argument_list|(
literal|"testRepo"
argument_list|,
literal|"Unit Test Repo"
argument_list|,
name|repoDir
argument_list|)
decl_stmt|;
name|FileType
name|fileType
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
operator|.
name|getRepositoryScanning
argument_list|()
operator|.
name|getFileTypes
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|fileType
operator|.
name|addPattern
argument_list|(
literal|"**/*.xml"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|,
name|fileType
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|fileTypes
operator|.
name|afterConfigurationChange
argument_list|(
literal|null
argument_list|,
literal|"fileType"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|repoContent
operator|=
operator|new
name|ManagedDefaultRepositoryContent
argument_list|(
name|artifactMappingProviders
argument_list|,
name|fileTypes
argument_list|)
expr_stmt|;
comment|//repoContent = (ManagedRepositoryContent) lookup( ManagedRepositoryContent.class, "default" );
name|repoContent
operator|.
name|setRepository
argument_list|(
name|repository
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetVersionsBadArtifact
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetVersions
argument_list|(
literal|"bad_artifact"
argument_list|,
name|Collections
operator|.
expr|<
name|String
operator|>
name|emptyList
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetVersionsMissingMultipleVersions
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetVersions
argument_list|(
literal|"missing_metadata_b"
argument_list|,
name|Arrays
operator|.
name|asList
argument_list|(
literal|"1.0"
argument_list|,
literal|"1.0.1"
argument_list|,
literal|"2.0"
argument_list|,
literal|"2.0.1"
argument_list|,
literal|"2.0-20070821-dev"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetVersionsSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|assertVersions
argument_list|(
literal|"proxied_multi"
argument_list|,
literal|"2.1"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"2.1"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetVersionsSimpleYetIncomplete
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetVersions
argument_list|(
literal|"incomplete_metadata_a"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"1.0"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetVersionsSimpleYetMissing
parameter_list|()
throws|throws
name|Exception
block|{
name|assertGetVersions
argument_list|(
literal|"missing_metadata_a"
argument_list|,
name|Collections
operator|.
name|singletonList
argument_list|(
literal|"1.0"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetVersionsSnapshotA
parameter_list|()
throws|throws
name|Exception
block|{
name|assertVersions
argument_list|(
literal|"snap_shots_a"
argument_list|,
literal|"1.0-alpha-11-SNAPSHOT"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0-alpha-11-SNAPSHOT"
block|,
literal|"1.0-alpha-11-20070221.194724-2"
block|,
literal|"1.0-alpha-11-20070302.212723-3"
block|,
literal|"1.0-alpha-11-20070303.152828-4"
block|,
literal|"1.0-alpha-11-20070305.215149-5"
block|,
literal|"1.0-alpha-11-20070307.170909-6"
block|,
literal|"1.0-alpha-11-20070314.211405-9"
block|,
literal|"1.0-alpha-11-20070316.175232-11"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToMetadataPathFromProjectReference
parameter_list|()
block|{
name|ProjectReference
name|reference
init|=
operator|new
name|ProjectReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
literal|"com.foo"
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
literal|"foo-tool"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"com/foo/foo-tool/maven-metadata.xml"
argument_list|,
name|repoContent
operator|.
name|toMetadataPath
argument_list|(
name|reference
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToMetadataPathFromVersionReference
parameter_list|()
block|{
name|VersionedReference
name|reference
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
literal|"com.foo"
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
literal|"foo-tool"
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setVersion
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"com/foo/foo-tool/1.0/maven-metadata.xml"
argument_list|,
name|repoContent
operator|.
name|toMetadataPath
argument_list|(
name|reference
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
annotation|@
name|Override
specifier|public
name|void
name|testToPathOnNullArtifactReference
parameter_list|()
block|{
try|try
block|{
name|ArtifactReference
name|reference
init|=
literal|null
decl_stmt|;
name|repoContent
operator|.
name|toPath
argument_list|(
name|reference
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed due to null artifact reference."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testExcludeMetadataFile
parameter_list|()
throws|throws
name|Exception
block|{
name|assertVersions
argument_list|(
literal|"include_xml"
argument_list|,
literal|"1.0"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"1.0"
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertGetVersions
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|List
argument_list|<
name|String
argument_list|>
name|expectedVersions
parameter_list|)
throws|throws
name|Exception
block|{
name|ProjectReference
name|reference
init|=
operator|new
name|ProjectReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
literal|"org.apache.archiva.metadata.tests"
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
comment|// Use the test metadata-repository, which is already setup for
comment|// These kind of version tests.
name|Path
name|repoDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/repositories/metadata-repository"
argument_list|)
decl_stmt|;
operator|(
operator|(
name|EditableManagedRepository
operator|)
name|repoContent
operator|.
name|getRepository
argument_list|()
operator|)
operator|.
name|setLocation
argument_list|(
name|repoDir
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// Request the versions.
name|Set
argument_list|<
name|String
argument_list|>
name|testedVersionSet
init|=
name|repoContent
operator|.
name|getVersions
argument_list|(
name|reference
argument_list|)
decl_stmt|;
comment|// Sort the list (for asserts)
name|List
argument_list|<
name|String
argument_list|>
name|testedVersions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|testedVersions
operator|.
name|addAll
argument_list|(
name|testedVersionSet
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|testedVersions
argument_list|,
operator|new
name|VersionComparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test the expected array of versions, to the actual tested versions
name|assertEquals
argument_list|(
literal|"available versions"
argument_list|,
name|expectedVersions
argument_list|,
name|testedVersions
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertVersions
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
index|[]
name|expectedVersions
parameter_list|)
throws|throws
name|Exception
block|{
name|VersionedReference
name|reference
init|=
operator|new
name|VersionedReference
argument_list|()
decl_stmt|;
name|reference
operator|.
name|setGroupId
argument_list|(
literal|"org.apache.archiva.metadata.tests"
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
comment|// Use the test metadata-repository, which is already setup for
comment|// These kind of version tests.
name|Path
name|repoDir
init|=
name|Paths
operator|.
name|get
argument_list|(
literal|"src/test/repositories/metadata-repository"
argument_list|)
decl_stmt|;
operator|(
operator|(
name|EditableManagedRepository
operator|)
name|repoContent
operator|.
name|getRepository
argument_list|()
operator|)
operator|.
name|setLocation
argument_list|(
name|repoDir
operator|.
name|toAbsolutePath
argument_list|()
operator|.
name|toUri
argument_list|()
argument_list|)
expr_stmt|;
comment|// Request the versions.
name|Set
argument_list|<
name|String
argument_list|>
name|testedVersionSet
init|=
name|repoContent
operator|.
name|getVersions
argument_list|(
name|reference
argument_list|)
decl_stmt|;
comment|// Sort the list (for asserts later)
name|List
argument_list|<
name|String
argument_list|>
name|testedVersions
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|testedVersions
operator|.
name|addAll
argument_list|(
name|testedVersionSet
argument_list|)
expr_stmt|;
name|Collections
operator|.
name|sort
argument_list|(
name|testedVersions
argument_list|,
operator|new
name|VersionComparator
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test the expected array of versions, to the actual tested versions
name|assertEquals
argument_list|(
literal|"Assert Versions: length/size"
argument_list|,
name|expectedVersions
operator|.
name|length
argument_list|,
name|testedVersions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|expectedVersions
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|String
name|actualVersion
init|=
name|testedVersions
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Versions["
operator|+
name|i
operator|+
literal|"]"
argument_list|,
name|expectedVersions
index|[
name|i
index|]
argument_list|,
name|actualVersion
argument_list|)
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|protected
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
block|{
return|return
name|repoContent
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|protected
name|String
name|toPath
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
block|{
return|return
name|repoContent
operator|.
name|toPath
argument_list|(
name|reference
argument_list|)
return|;
block|}
block|}
end_class

end_unit

