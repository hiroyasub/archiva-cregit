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
name|Constraint
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
name|Calendar
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
comment|/**  * OlderArtifactsByAgeConstraintTest   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|OlderSnapshotArtifactsByAgeConstraintTest
extends|extends
name|AbstractArchivaDatabaseTestCase
block|{
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
specifier|public
name|ArchivaArtifact
name|createArtifact
parameter_list|(
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|int
name|daysOld
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
name|Calendar
name|cal
init|=
name|Calendar
operator|.
name|getInstance
argument_list|()
decl_stmt|;
name|cal
operator|.
name|add
argument_list|(
name|Calendar
operator|.
name|DAY_OF_MONTH
argument_list|,
operator|(
operator|-
literal|1
operator|)
operator|*
name|daysOld
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setLastModified
argument_list|(
name|cal
operator|.
name|getTime
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
literal|"testable_repo"
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
specifier|public
name|void
name|testConstraint
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
argument_list|,
literal|200
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
literal|"1.1-SNAPSHOT"
argument_list|,
literal|110
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
argument_list|,
literal|100
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
literal|"1.2-20060923.005752-2"
argument_list|,
literal|55
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
literal|"1.2-SNAPSHOT"
argument_list|,
literal|52
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
argument_list|,
literal|50
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
literal|"1.0-20060828.144210-1"
argument_list|,
literal|220
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
literal|"1.0-SNAPSHOT"
argument_list|,
literal|210
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
argument_list|,
literal|200
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
argument_list|,
literal|150
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
argument_list|,
literal|100
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
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|artifactDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
literal|5
argument_list|,
operator|new
name|OlderSnapshotArtifactsByAgeConstraint
argument_list|(
literal|7
argument_list|)
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
literal|3
argument_list|,
operator|new
name|OlderSnapshotArtifactsByAgeConstraint
argument_list|(
literal|90
argument_list|)
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
literal|3
argument_list|,
operator|new
name|OlderSnapshotArtifactsByAgeConstraint
argument_list|(
literal|100
argument_list|)
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
literal|2
argument_list|,
operator|new
name|OlderSnapshotArtifactsByAgeConstraint
argument_list|(
literal|150
argument_list|)
argument_list|)
expr_stmt|;
name|assertConstraint
argument_list|(
literal|0
argument_list|,
operator|new
name|OlderSnapshotArtifactsByAgeConstraint
argument_list|(
literal|500
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertConstraint
parameter_list|(
name|int
name|expectedHits
parameter_list|,
name|Constraint
name|constraint
parameter_list|)
throws|throws
name|Exception
block|{
name|List
name|results
init|=
name|artifactDao
operator|.
name|queryArtifacts
argument_list|(
name|constraint
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Older Snapshot Artifacts By Age: Not Null"
argument_list|,
name|results
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Older Snapshot Artifacts By Age: Results.size"
argument_list|,
name|expectedHits
argument_list|,
name|results
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

