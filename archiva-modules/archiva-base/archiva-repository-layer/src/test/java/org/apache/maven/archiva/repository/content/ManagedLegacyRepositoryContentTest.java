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
name|repository
operator|.
name|content
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|VersionComparator
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
name|maven
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
name|maven
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
name|maven
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
name|maven
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

begin_comment
comment|/**  * ManagedLegacyRepositoryContentTest  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ManagedLegacyRepositoryContentTest
extends|extends
name|AbstractLegacyRepositoryContentTestCase
block|{
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"managedRepositoryContent#legacy"
argument_list|)
specifier|private
name|ManagedRepositoryContent
name|repoContent
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
name|File
name|repoDir
init|=
operator|new
name|File
argument_list|(
literal|"src/test/repositories/legacy-repository"
argument_list|)
decl_stmt|;
name|ManagedRepository
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
name|repository
operator|.
name|setLayout
argument_list|(
literal|"legacy"
argument_list|)
expr_stmt|;
comment|//repoContent = (ManagedRepositoryContent) lookup( ManagedRepositoryContent.class, "legacy" );
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
name|testGetVersionsFromProjectReference
parameter_list|()
throws|throws
name|Exception
block|{
name|assertVersions
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
operator|new
name|String
index|[]
block|{
literal|"UNKNOWN"
block|,
comment|//            "1.0-javadoc",
comment|//            "1.0-sources",
literal|"1.0"
block|,
literal|"1.0-20050611.112233-1"
block|}
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGetVersionsFromVersionedReference
parameter_list|()
throws|throws
name|Exception
block|{
name|assertVersions
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
literal|"1.0"
argument_list|,
operator|new
name|String
index|[]
block|{
comment|//            "1.0-javadoc",
comment|//            "1.0-sources",
literal|"1.0"
block|,
literal|"1.0-20050611.112233-1"
block|}
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertVersions
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
index|[]
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
name|groupId
argument_list|)
expr_stmt|;
name|reference
operator|.
name|setArtifactId
argument_list|(
name|artifactId
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
argument_list|<
name|String
argument_list|>
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
literal|"Assert (Project) Versions: length/size"
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
literal|"(Project) Versions["
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
specifier|private
name|void
name|assertVersions
parameter_list|(
name|String
name|groupId
parameter_list|,
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
name|groupId
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
argument_list|<
name|String
argument_list|>
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
literal|"Assert (Project) Versions: length/size"
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
literal|"(Project) Versions["
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
name|Test
specifier|public
name|void
name|testGetRelatedArtifacts
parameter_list|()
throws|throws
name|Exception
block|{
name|ArtifactReference
name|reference
init|=
name|createArtifact
argument_list|(
literal|"org.apache.maven"
argument_list|,
literal|"testing"
argument_list|,
literal|"1.0"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
decl_stmt|;
name|Set
argument_list|<
name|ArtifactReference
argument_list|>
name|related
init|=
name|repoContent
operator|.
name|getRelatedArtifacts
argument_list|(
name|reference
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|related
argument_list|)
expr_stmt|;
name|String
name|expected
index|[]
init|=
operator|new
name|String
index|[]
block|{
literal|"org.apache.maven/jars/testing-1.0.jar"
block|,
literal|"org.apache.maven/java-sources/testing-1.0-sources.jar"
block|,
literal|"org.apache.maven/jars/testing-1.0-20050611.112233-1.jar"
block|,
literal|"org.apache.maven/poms/testing-1.0.pom"
block|,
literal|"org.apache.maven/distributions/testing-1.0.tar.gz"
block|,
literal|"org.apache.maven/distributions/testing-1.0.zip"
block|,
literal|"org.apache.maven/javadoc.jars/testing-1.0-javadoc.jar"
block|}
decl_stmt|;
name|StringBuffer
name|relatedDebugString
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|relatedDebugString
operator|.
name|append
argument_list|(
literal|"["
argument_list|)
expr_stmt|;
for|for
control|(
name|ArtifactReference
name|ref
range|:
name|related
control|)
block|{
name|String
name|actualPath
init|=
name|repoContent
operator|.
name|toPath
argument_list|(
name|ref
argument_list|)
decl_stmt|;
name|relatedDebugString
operator|.
name|append
argument_list|(
name|actualPath
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
block|}
name|relatedDebugString
operator|.
name|append
argument_list|(
literal|"]"
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|expectedPath
range|:
name|expected
control|)
block|{
name|boolean
name|found
init|=
literal|false
decl_stmt|;
for|for
control|(
name|ArtifactReference
name|actualRef
range|:
name|related
control|)
block|{
name|String
name|actualPath
init|=
name|repoContent
operator|.
name|toPath
argument_list|(
name|actualRef
argument_list|)
decl_stmt|;
if|if
condition|(
name|actualPath
operator|.
name|endsWith
argument_list|(
name|expectedPath
argument_list|)
condition|)
block|{
name|found
operator|=
literal|true
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
operator|!
name|found
condition|)
block|{
name|fail
argument_list|(
literal|"Unable to find expected artifact ["
operator|+
name|expectedPath
operator|+
literal|"] in list of related artifacts. "
operator|+
literal|"Related<"
operator|+
name|relatedDebugString
operator|+
literal|">"
argument_list|)
expr_stmt|;
block|}
block|}
name|assertEquals
argument_list|(
literal|"Related<"
operator|+
name|relatedDebugString
operator|+
literal|">:"
argument_list|,
name|expected
operator|.
name|length
argument_list|,
name|related
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
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

