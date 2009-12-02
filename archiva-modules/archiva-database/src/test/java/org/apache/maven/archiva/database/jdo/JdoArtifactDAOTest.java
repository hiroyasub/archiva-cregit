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
name|jdo
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|JDOHelper
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|spi
operator|.
name|JDOImplHelper
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
name|ArchivaArtifactModel
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
name|jpox
operator|.
name|ArchivaArtifactModelKey
import|;
end_import

begin_comment
comment|/**  * JdoArtifactDAOTest   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|JdoArtifactDAOTest
extends|extends
name|AbstractArchivaDatabaseTestCase
block|{
specifier|public
name|void
name|testArtifactKey
parameter_list|()
block|{
name|Object
name|o
init|=
name|JDOImplHelper
operator|.
name|getInstance
argument_list|()
operator|.
name|newObjectIdInstance
argument_list|(
name|ArchivaArtifactModel
operator|.
name|class
argument_list|,
literal|"foo:bar:1.0::jar:testrepo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Key should not be null."
argument_list|,
name|o
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Key should be an instance of "
operator|+
name|ArchivaArtifactModelKey
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|(
name|o
operator|instanceof
name|ArchivaArtifactModelKey
operator|)
argument_list|)
expr_stmt|;
name|ArchivaArtifactModelKey
name|key
init|=
operator|(
name|ArchivaArtifactModelKey
operator|)
name|o
decl_stmt|;
name|assertEquals
argument_list|(
literal|"foo"
argument_list|,
name|key
operator|.
name|groupId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"bar"
argument_list|,
name|key
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|key
operator|.
name|version
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|key
operator|.
name|classifier
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
name|key
operator|.
name|type
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"testrepo"
argument_list|,
name|key
operator|.
name|repositoryId
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testArtifactCRUD
parameter_list|()
throws|throws
name|Exception
block|{
name|ArtifactDAO
name|artiDao
init|=
name|dao
operator|.
name|getArtifactDAO
argument_list|()
decl_stmt|;
comment|// Create it
name|ArchivaArtifact
name|artifact
init|=
name|artiDao
operator|.
name|createArtifact
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-test-module"
argument_list|,
literal|"1.0"
argument_list|,
literal|""
argument_list|,
literal|"jar"
argument_list|,
literal|"testrepo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
comment|// Set some mandatory values
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
comment|// Save it.
name|ArchivaArtifact
name|savedArtifact
init|=
name|artiDao
operator|.
name|saveArtifact
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|savedArtifact
argument_list|)
expr_stmt|;
name|String
name|savedKeyId
init|=
name|JDOHelper
operator|.
name|getObjectId
argument_list|(
name|savedArtifact
operator|.
name|getModel
argument_list|()
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven.archiva:archiva-test-module:1.0::jar:testrepo"
argument_list|,
name|savedKeyId
argument_list|)
expr_stmt|;
comment|// Test that something has been saved.
name|List
argument_list|<
name|ArchivaArtifact
argument_list|>
name|artifacts
init|=
name|artiDao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|artifacts
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artifacts
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that retrieved object is what we expect.
name|ArchivaArtifact
name|firstArtifact
init|=
operator|(
name|ArchivaArtifact
operator|)
name|artifacts
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|firstArtifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
name|firstArtifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-test-module"
argument_list|,
name|firstArtifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|firstArtifact
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|""
argument_list|,
name|firstArtifact
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
name|firstArtifact
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
comment|// Change value and save.
name|savedArtifact
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
name|artiDao
operator|.
name|saveArtifact
argument_list|(
name|savedArtifact
argument_list|)
expr_stmt|;
comment|// Test that only 1 object is saved.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artiDao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Get the specific artifact.
name|ArchivaArtifact
name|actualArtifact
init|=
name|artiDao
operator|.
name|getArtifact
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-test-module"
argument_list|,
literal|"1.0"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|,
literal|"testrepo"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|actualArtifact
argument_list|)
expr_stmt|;
comment|// Test expected values.
name|assertEquals
argument_list|(
literal|"archiva-test-module"
argument_list|,
name|actualArtifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that only 1 object is saved.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|artiDao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Delete object.
name|artiDao
operator|.
name|deleteArtifact
argument_list|(
name|actualArtifact
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|artiDao
operator|.
name|queryArtifacts
argument_list|(
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

