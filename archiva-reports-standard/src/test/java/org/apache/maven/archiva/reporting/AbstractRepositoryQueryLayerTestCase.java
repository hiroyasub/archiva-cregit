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
name|reporting
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
name|archiva
operator|.
name|layer
operator|.
name|CachedRepositoryQueryLayer
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
name|layer
operator|.
name|RepositoryQueryLayerException
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
name|Artifact
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
name|factory
operator|.
name|ArtifactFactory
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
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|Snapshot
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
comment|/**  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryQueryLayerTestCase
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|ArtifactFactory
name|artifactFactory
decl_stmt|;
specifier|protected
name|ArtifactRepository
name|repository
decl_stmt|;
specifier|protected
name|CachedRepositoryQueryLayer
name|queryLayer
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
name|File
name|repositoryDirectory
init|=
name|getTestFile
argument_list|(
literal|"src/test/repository"
argument_list|)
decl_stmt|;
name|artifactFactory
operator|=
operator|(
name|ArtifactFactory
operator|)
name|lookup
argument_list|(
name|ArtifactFactory
operator|.
name|ROLE
argument_list|)
expr_stmt|;
name|ArtifactRepositoryFactory
name|factory
init|=
operator|(
name|ArtifactRepositoryFactory
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryFactory
operator|.
name|ROLE
argument_list|)
decl_stmt|;
name|ArtifactRepositoryLayout
name|layout
init|=
operator|(
name|ArtifactRepositoryLayout
operator|)
name|lookup
argument_list|(
name|ArtifactRepositoryLayout
operator|.
name|ROLE
argument_list|,
literal|"default"
argument_list|)
decl_stmt|;
name|repository
operator|=
name|factory
operator|.
name|createArtifactRepository
argument_list|(
literal|"test"
argument_list|,
name|repositoryDirectory
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
block|}
specifier|public
name|void
name|testContainsArtifactTrue
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-alpha-1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check artifact"
argument_list|,
name|queryLayer
operator|.
name|containsArtifact
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testContainsArtifactFalse
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"1.0-beta-1"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"check non-existent artifact"
argument_list|,
name|queryLayer
operator|.
name|containsArtifact
argument_list|(
name|artifact
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testContainsSnapshotArtifactTrue
parameter_list|()
block|{
name|Snapshot
name|snapshot
init|=
operator|new
name|Snapshot
argument_list|()
decl_stmt|;
name|snapshot
operator|.
name|setTimestamp
argument_list|(
literal|"20050611.202024"
argument_list|)
expr_stmt|;
name|snapshot
operator|.
name|setBuildNumber
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"snapshot-artifact"
argument_list|,
literal|"1.0-alpha-1-SNAPSHOT"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check for snapshot artifact"
argument_list|,
name|queryLayer
operator|.
name|containsArtifact
argument_list|(
name|artifact
argument_list|,
name|snapshot
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testContainsSnapshotArtifactFalse
parameter_list|()
block|{
name|Snapshot
name|snapshot
init|=
operator|new
name|Snapshot
argument_list|()
decl_stmt|;
name|snapshot
operator|.
name|setTimestamp
argument_list|(
literal|"20050611.202024"
argument_list|)
expr_stmt|;
name|snapshot
operator|.
name|setBuildNumber
argument_list|(
literal|2
argument_list|)
expr_stmt|;
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"snapshot-artifact"
argument_list|,
literal|"1.0-alpha-1-SNAPSHOT"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"check for non-existent snapshot artifact"
argument_list|,
name|queryLayer
operator|.
name|containsArtifact
argument_list|(
name|artifact
argument_list|,
name|snapshot
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactVersionsTrue
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"ignored"
argument_list|)
decl_stmt|;
name|List
name|versions
init|=
name|queryLayer
operator|.
name|getVersions
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check version 1.0-alpha-1"
argument_list|,
name|versions
operator|.
name|contains
argument_list|(
literal|"1.0-alpha-1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check version 1.0-alpha-2"
argument_list|,
name|versions
operator|.
name|contains
argument_list|(
literal|"1.0-alpha-2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check version 1.0-alpha-3"
argument_list|,
name|versions
operator|.
name|contains
argument_list|(
literal|"1.0-alpha-3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactVersionsFalse
parameter_list|()
throws|throws
name|Exception
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"artifactId"
argument_list|,
literal|"ignored"
argument_list|)
decl_stmt|;
name|List
name|versions
init|=
name|queryLayer
operator|.
name|getVersions
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check version 1.0-alpha-1"
argument_list|,
name|versions
operator|.
name|contains
argument_list|(
literal|"1.0-alpha-1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"check version 1.0-alpha-2"
argument_list|,
name|versions
operator|.
name|contains
argument_list|(
literal|"1.0-alpha-2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"check version 1.0-alpha-3"
argument_list|,
name|versions
operator|.
name|contains
argument_list|(
literal|"1.0-alpha-3"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactVersionsError
parameter_list|()
block|{
name|Artifact
name|artifact
init|=
name|getArtifact
argument_list|(
literal|"groupId"
argument_list|,
literal|"none"
argument_list|,
literal|"ignored"
argument_list|)
decl_stmt|;
try|try
block|{
name|queryLayer
operator|.
name|getVersions
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"expected error not thrown"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryQueryLayerException
name|e
parameter_list|)
block|{
comment|//expected
block|}
block|}
specifier|private
name|Artifact
name|getArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|)
block|{
return|return
name|artifactFactory
operator|.
name|createBuildArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|"pom"
argument_list|)
return|;
block|}
specifier|protected
name|void
name|tearDown
parameter_list|()
throws|throws
name|Exception
block|{
name|release
argument_list|(
name|artifactFactory
argument_list|)
expr_stmt|;
name|super
operator|.
name|tearDown
argument_list|()
expr_stmt|;
name|artifactFactory
operator|=
literal|null
expr_stmt|;
name|repository
operator|=
literal|null
expr_stmt|;
block|}
block|}
end_class

end_unit

