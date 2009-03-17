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
name|database
operator|.
name|constraints
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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|AbstractArchivaDatabaseTestCase
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
name|database
operator|.
name|ArchivaDAO
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
name|database
operator|.
name|ArtifactDAO
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
name|database
operator|.
name|SimpleConstraint
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
name|ArchivaArtifact
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
name|Date
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
comment|/**  * UniqueVersionConstraintTest   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|UniqueVersionConstraintTest
extends|extends
name|AbstractArchivaDatabaseTestCase
block|{
specifier|private
name|ArtifactDAO
name|artifactDao
decl_stmt|;
specifier|public
name|void
name|testConstraintGroupIdArtifactIdCommonsLang
parameter_list|()
throws|throws
name|Exception
block|{
name|setupArtifacts
argument_list|()
expr_stmt|;
name|assertConstraint
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"2.0"
block|,
literal|"2.1"
block|}
argument_list|,
operator|new
name|UniqueVersionConstraint
argument_list|(
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstraintGroupIdArtifactIdInvalid
parameter_list|()
throws|throws
name|Exception
block|{
name|setupArtifacts
argument_list|()
expr_stmt|;
name|assertConstraint
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|,
operator|new
name|UniqueVersionConstraint
argument_list|(
literal|"org.apache"
argument_list|,
literal|"invalid"
argument_list|)
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|,
operator|new
name|UniqueVersionConstraint
argument_list|(
literal|"org.apache.test"
argument_list|,
literal|"invalid"
argument_list|)
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
operator|new
name|String
index|[]
block|{}
argument_list|,
operator|new
name|UniqueVersionConstraint
argument_list|(
literal|"invalid"
argument_list|,
literal|"test-two"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstraintGroupIdArtifactIdMavenSharedTestTwo
parameter_list|()
throws|throws
name|Exception
block|{
name|setupArtifacts
argument_list|()
expr_stmt|;
name|assertConstraint
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"2.0"
block|,
literal|"2.1-SNAPSHOT"
block|,
literal|"2.1.1"
block|,
literal|"2.1-alpha-1"
block|}
argument_list|,
operator|new
name|UniqueVersionConstraint
argument_list|(
literal|"org.apache.maven.shared"
argument_list|,
literal|"test-two"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstraintGroupIdArtifactIdMavenSharedTestTwoCentralOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|setupArtifacts
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositories
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|observableRepositories
operator|.
name|add
argument_list|(
literal|"central"
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"2.0"
block|,
literal|"2.1.1"
block|,
literal|"2.1-alpha-1"
block|}
argument_list|,
operator|new
name|UniqueVersionConstraint
argument_list|(
name|observableRepositories
argument_list|,
literal|"org.apache.maven.shared"
argument_list|,
literal|"test-two"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstraintGroupIdArtifactIdMavenSharedTestTwoSnapshotsOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|setupArtifacts
argument_list|()
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|observableRepositories
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|observableRepositories
operator|.
name|add
argument_list|(
literal|"snapshots"
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"2.1-SNAPSHOT"
block|}
argument_list|,
operator|new
name|UniqueVersionConstraint
argument_list|(
name|observableRepositories
argument_list|,
literal|"org.apache.maven.shared"
argument_list|,
literal|"test-two"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstraintGroupIdArtifactIdMavenTestOne
parameter_list|()
throws|throws
name|Exception
block|{
name|setupArtifacts
argument_list|()
expr_stmt|;
name|assertConstraint
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"1.2"
block|}
argument_list|,
operator|new
name|UniqueVersionConstraint
argument_list|(
literal|"org.apache.maven.test"
argument_list|,
literal|"test-one"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstraintGroupIdArtifactIdModelloLong
parameter_list|()
throws|throws
name|Exception
block|{
name|setupArtifacts
argument_list|()
expr_stmt|;
name|assertConstraint
argument_list|(
operator|new
name|String
index|[]
block|{
literal|"3.0"
block|}
argument_list|,
operator|new
name|UniqueVersionConstraint
argument_list|(
literal|"org.codehaus.modello"
argument_list|,
literal|"modellong"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
specifier|private
name|void
name|assertConstraint
parameter_list|(
name|String
index|[]
name|versions
parameter_list|,
name|SimpleConstraint
name|constraint
parameter_list|)
block|{
name|String
name|prefix
init|=
literal|"Unique Versions: "
decl_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|results
init|=
operator|(
name|List
argument_list|<
name|String
argument_list|>
operator|)
name|dao
operator|.
name|query
argument_list|(
name|constraint
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|prefix
operator|+
literal|"Not Null"
argument_list|,
name|results
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|prefix
operator|+
literal|"Results.size"
argument_list|,
name|versions
operator|.
name|length
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|String
argument_list|>
name|expectedVersions
init|=
name|Arrays
operator|.
name|asList
argument_list|(
name|versions
argument_list|)
decl_stmt|;
for|for
control|(
name|String
name|actualVersion
range|:
name|results
control|)
block|{
name|assertTrue
argument_list|(
name|prefix
operator|+
literal|"version result should not be blank."
argument_list|,
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|actualVersion
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|prefix
operator|+
literal|"version result<"
operator|+
name|actualVersion
operator|+
literal|"> exists in expected versions."
argument_list|,
name|expectedVersions
operator|.
name|contains
argument_list|(
name|actualVersion
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|ArchivaArtifact
name|createArtifact
parameter_list|(
name|String
name|repoId
parameter_list|,
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
name|ArchivaArtifact
name|artifact
init|=
name|artifactDao
operator|.
name|createArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"testrepo"
argument_list|)
decl_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setLastModified
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
comment|// mandatory field.
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setRepositoryId
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
specifier|private
name|void
name|setupArtifacts
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaArtifact
name|artifact
decl_stmt|;
comment|// Setup artifacts in fresh DB.
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"central"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"central"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.1"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"central"
argument_list|,
literal|"org.apache.maven.test"
argument_list|,
literal|"test-one"
argument_list|,
literal|"1.2"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"central"
argument_list|,
literal|"org.apache.maven.test.foo"
argument_list|,
literal|"test-two"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"central"
argument_list|,
literal|"org.apache.maven.shared"
argument_list|,
literal|"test-two"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"central"
argument_list|,
literal|"org.apache.maven.shared"
argument_list|,
literal|"test-two"
argument_list|,
literal|"2.1.1"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"central"
argument_list|,
literal|"org.apache.maven.shared"
argument_list|,
literal|"test-two"
argument_list|,
literal|"2.1-alpha-1"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"central"
argument_list|,
literal|"org.apache.maven.shared"
argument_list|,
literal|"test-bar"
argument_list|,
literal|"2.1"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"central"
argument_list|,
literal|"org.codehaus.modello"
argument_list|,
literal|"modellong"
argument_list|,
literal|"3.0"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
comment|// Snapshots repository artifacts
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"snapshots"
argument_list|,
literal|"org.apache.maven.shared"
argument_list|,
literal|"test-two"
argument_list|,
literal|"2.1-SNAPSHOT"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"snapshots"
argument_list|,
literal|"org.codehaus.modello"
argument_list|,
literal|"test-three"
argument_list|,
literal|"1.0-SNAPSHOT"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"snapshots"
argument_list|,
literal|"org.codehaus.mojo"
argument_list|,
literal|"testable-maven-plugin"
argument_list|,
literal|"2.1-SNAPSHOT"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|=
name|createArtifact
argument_list|(
literal|"snapshots"
argument_list|,
literal|"org.apache.archiva"
argument_list|,
literal|"testable"
argument_list|,
literal|"1.1-alpha-1-20070822.033400-43"
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
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
name|ArchivaDAO
name|dao
init|=
operator|(
name|ArchivaDAO
operator|)
name|lookup
argument_list|(
name|ArchivaDAO
operator|.
name|ROLE
argument_list|,
literal|"jdo"
argument_list|)
decl_stmt|;
name|artifactDao
operator|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

