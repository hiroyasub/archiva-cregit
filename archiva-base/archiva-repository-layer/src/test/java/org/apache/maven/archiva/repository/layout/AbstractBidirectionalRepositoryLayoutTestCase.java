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
name|layout
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
name|ArchivaRepository
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

begin_comment
comment|/**  * AbstractBidirectionalRepositoryLayoutTestCase   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|AbstractBidirectionalRepositoryLayoutTestCase
extends|extends
name|PlexusTestCase
block|{
specifier|protected
name|ArchivaRepository
name|repository
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
name|repository
operator|=
name|createTestRepository
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|ArchivaRepository
name|createTestRepository
parameter_list|()
block|{
name|File
name|targetDir
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"target"
argument_list|)
decl_stmt|;
name|File
name|testRepo
init|=
operator|new
name|File
argument_list|(
name|targetDir
argument_list|,
literal|"test-repo"
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|testRepo
operator|.
name|exists
argument_list|()
condition|)
block|{
name|testRepo
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
block|}
name|String
name|repoUri
init|=
literal|"file://"
operator|+
name|StringUtils
operator|.
name|replace
argument_list|(
name|testRepo
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
literal|"\\"
argument_list|,
literal|"/"
argument_list|)
decl_stmt|;
name|ArchivaRepository
name|repo
init|=
operator|new
name|ArchivaRepository
argument_list|(
literal|"testRepo"
argument_list|,
literal|"Test Repository"
argument_list|,
name|repoUri
argument_list|)
decl_stmt|;
return|return
name|repo
return|;
block|}
specifier|protected
name|ArchivaArtifact
name|createArtifact
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
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|ArchivaArtifact
name|artifact
init|=
operator|new
name|ArchivaArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|setRepositoryId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
block|}
specifier|protected
name|void
name|assertArtifact
parameter_list|(
name|ArchivaArtifact
name|actualArtifact
parameter_list|,
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
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|String
name|expectedId
init|=
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
operator|+
literal|":"
operator|+
name|classifier
operator|+
literal|":"
operator|+
name|type
decl_stmt|;
name|assertNotNull
argument_list|(
name|expectedId
operator|+
literal|" - Should not be null."
argument_list|,
name|actualArtifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Group ID"
argument_list|,
name|actualArtifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Artifact ID"
argument_list|,
name|actualArtifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Version ID"
argument_list|,
name|actualArtifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Classifier"
argument_list|,
name|actualArtifact
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Type"
argument_list|,
name|actualArtifact
operator|.
name|getType
argument_list|()
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertSnapshotArtifact
parameter_list|(
name|ArchivaArtifact
name|actualArtifact
parameter_list|,
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
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
block|{
name|String
name|expectedId
init|=
name|groupId
operator|+
literal|":"
operator|+
name|artifactId
operator|+
literal|":"
operator|+
name|version
operator|+
literal|":"
operator|+
name|classifier
operator|+
literal|":"
operator|+
name|type
decl_stmt|;
name|assertNotNull
argument_list|(
name|expectedId
operator|+
literal|" - Should not be null."
argument_list|,
name|actualArtifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Group ID"
argument_list|,
name|actualArtifact
operator|.
name|getGroupId
argument_list|()
argument_list|,
name|groupId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Artifact ID"
argument_list|,
name|actualArtifact
operator|.
name|getArtifactId
argument_list|()
argument_list|,
name|artifactId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Version ID"
argument_list|,
name|actualArtifact
operator|.
name|getVersion
argument_list|()
argument_list|,
name|version
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Classifier"
argument_list|,
name|actualArtifact
operator|.
name|getClassifier
argument_list|()
argument_list|,
name|classifier
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Type"
argument_list|,
name|actualArtifact
operator|.
name|getType
argument_list|()
argument_list|,
name|type
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|expectedId
operator|+
literal|" - Snapshot"
argument_list|,
name|actualArtifact
operator|.
name|isSnapshot
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

