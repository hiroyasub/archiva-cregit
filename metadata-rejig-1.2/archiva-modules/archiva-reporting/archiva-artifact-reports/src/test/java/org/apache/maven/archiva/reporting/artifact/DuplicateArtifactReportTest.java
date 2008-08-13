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
operator|.
name|artifact
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
name|ArchivaConfiguration
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
name|consumers
operator|.
name|ArchivaArtifactConsumer
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
name|model
operator|.
name|ArchivaArtifact
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
name|RepositoryProblem
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
name|reporting
operator|.
name|DynamicReportSource
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
name|Date
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * DuplicateArtifactReportTest  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DuplicateArtifactReportTest
extends|extends
name|AbstractArtifactReportsTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|TESTABLE_REPO
init|=
literal|"testable"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HASH3
init|=
literal|"f3f653289f3217c65324830ab3415bc92feddefa"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HASH2
init|=
literal|"a49810ad3eba8651677ab57cd40a0f76fdef9538"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|HASH1
init|=
literal|"232f01b24b1617c46a3d4b0ab3415bc9237dcdec"
decl_stmt|;
specifier|private
name|ArtifactDAO
name|artifactDao
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
name|artifactDao
operator|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
expr_stmt|;
name|ArchivaConfiguration
name|config
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
literal|"default"
argument_list|)
decl_stmt|;
name|ManagedRepositoryConfiguration
name|repoConfig
init|=
operator|new
name|ManagedRepositoryConfiguration
argument_list|()
decl_stmt|;
name|repoConfig
operator|.
name|setId
argument_list|(
name|TESTABLE_REPO
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLayout
argument_list|(
literal|"default"
argument_list|)
expr_stmt|;
name|File
name|testRepoDir
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"target/test-repository"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|forceMkdir
argument_list|(
name|testRepoDir
argument_list|)
expr_stmt|;
name|repoConfig
operator|.
name|setLocation
argument_list|(
name|testRepoDir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|config
operator|.
name|getConfiguration
argument_list|()
operator|.
name|addManagedRepository
argument_list|(
name|repoConfig
argument_list|)
expr_stmt|;
block|}
specifier|public
name|ArchivaArtifact
name|createArtifact
parameter_list|(
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
literal|"org.apache.maven.archiva.test"
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
literal|""
argument_list|,
literal|"jar"
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
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setRepositoryId
argument_list|(
name|TESTABLE_REPO
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
specifier|public
name|void
name|testSimpleReport
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
literal|"test-one"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumSHA1
argument_list|(
name|HASH1
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
literal|"test-one"
argument_list|,
literal|"1.1"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumSHA1
argument_list|(
name|HASH1
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
literal|"test-one"
argument_list|,
literal|"1.2"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumSHA1
argument_list|(
name|HASH1
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
literal|"test-two"
argument_list|,
literal|"1.0"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumSHA1
argument_list|(
name|HASH1
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
literal|"test-two"
argument_list|,
literal|"2.0"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumSHA1
argument_list|(
name|HASH3
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
literal|"test-two"
argument_list|,
literal|"2.1"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumSHA1
argument_list|(
name|HASH2
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
literal|"test-two"
argument_list|,
literal|"3.0"
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setChecksumSHA1
argument_list|(
name|HASH2
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
comment|// Setup entries for bad/duplicate in problem DB.
name|pretendToRunDuplicateArtifactsConsumer
argument_list|()
expr_stmt|;
name|List
name|allArtifacts
init|=
name|artifactDao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Total Artifact Count"
argument_list|,
literal|7
argument_list|,
name|allArtifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|DuplicateArtifactReport
name|report
init|=
operator|(
name|DuplicateArtifactReport
operator|)
name|lookup
argument_list|(
name|DynamicReportSource
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"duplicate-artifacts"
argument_list|)
decl_stmt|;
name|List
name|results
init|=
name|report
operator|.
name|getData
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"Results.size: "
operator|+
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|int
name|i
init|=
literal|0
decl_stmt|;
name|Iterator
name|it
init|=
name|results
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|RepositoryProblem
name|problem
init|=
operator|(
name|RepositoryProblem
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"["
operator|+
operator|(
name|i
operator|++
operator|)
operator|+
literal|"] "
operator|+
name|problem
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|int
name|hash1Count
init|=
literal|4
decl_stmt|;
name|int
name|hash2Count
init|=
literal|2
decl_stmt|;
name|int
name|hash3Count
init|=
literal|1
decl_stmt|;
name|int
name|totals
init|=
operator|(
operator|(
name|hash1Count
operator|*
name|hash1Count
operator|)
operator|-
name|hash1Count
operator|)
operator|+
operator|(
operator|(
name|hash2Count
operator|*
name|hash2Count
operator|)
operator|-
name|hash2Count
operator|)
operator|+
operator|(
operator|(
name|hash3Count
operator|*
name|hash3Count
operator|)
operator|-
name|hash3Count
operator|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Total report hits."
argument_list|,
name|totals
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|pretendToRunDuplicateArtifactsConsumer
parameter_list|()
throws|throws
name|Exception
block|{
name|List
name|artifacts
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|ArchivaArtifactConsumer
name|consumer
init|=
operator|(
name|ArchivaArtifactConsumer
operator|)
name|lookup
argument_list|(
name|ArchivaArtifactConsumer
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"duplicate-artifacts"
argument_list|)
decl_stmt|;
name|consumer
operator|.
name|beginScan
argument_list|()
expr_stmt|;
try|try
block|{
name|Iterator
name|it
init|=
name|artifacts
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|ArchivaArtifact
name|artifact
init|=
operator|(
name|ArchivaArtifact
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|consumer
operator|.
name|processArchivaArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
block|}
finally|finally
block|{
name|consumer
operator|.
name|completeScan
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

