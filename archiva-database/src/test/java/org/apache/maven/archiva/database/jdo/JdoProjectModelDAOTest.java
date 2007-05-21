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
name|ProjectModelDAO
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
name|ArchivaProjectModel
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
name|ArchivaProjectModelKey
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
name|project
operator|.
name|ProjectModelReader
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

begin_comment
comment|/**  * JdoProjectModelDAOTest   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|JdoProjectModelDAOTest
extends|extends
name|AbstractArchivaDatabaseTestCase
block|{
specifier|public
name|void
name|testProjectModelKey
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
name|ArchivaProjectModel
operator|.
name|class
argument_list|,
literal|"foo:bar:1.0"
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
name|ArchivaProjectModelKey
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
operator|(
name|o
operator|instanceof
name|ArchivaProjectModelKey
operator|)
argument_list|)
expr_stmt|;
name|ArchivaProjectModelKey
name|key
init|=
operator|(
name|ArchivaProjectModelKey
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
block|}
specifier|public
name|void
name|testProjectModelCRUD
parameter_list|()
throws|throws
name|Exception
block|{
name|ProjectModelDAO
name|projectDao
init|=
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
decl_stmt|;
comment|// Create it
name|ArchivaProjectModel
name|model
init|=
name|projectDao
operator|.
name|createProjectModel
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-test-module"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|model
argument_list|)
expr_stmt|;
comment|// Set some mandatory values
name|model
operator|.
name|setPackaging
argument_list|(
literal|"pom"
argument_list|)
expr_stmt|;
name|model
operator|.
name|setWhenIndexed
argument_list|(
operator|new
name|Date
argument_list|()
argument_list|)
expr_stmt|;
name|model
operator|.
name|setOrigin
argument_list|(
literal|"test"
argument_list|)
expr_stmt|;
comment|// Save it.
name|ArchivaProjectModel
name|savedModel
init|=
name|projectDao
operator|.
name|saveProjectModel
argument_list|(
name|model
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|savedModel
argument_list|)
expr_stmt|;
name|String
name|savedKeyId
init|=
name|JDOHelper
operator|.
name|getObjectId
argument_list|(
name|savedModel
argument_list|)
operator|.
name|toString
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven.archiva:archiva-test-module:1.0"
argument_list|,
name|savedKeyId
argument_list|)
expr_stmt|;
comment|// Test that something has been saved.
name|List
name|projects
init|=
name|projectDao
operator|.
name|queryProjectModels
argument_list|(
literal|null
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|projects
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|projects
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that retrieved object is what we expect.
name|ArchivaProjectModel
name|firstModel
init|=
operator|(
name|ArchivaProjectModel
operator|)
name|projects
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|firstModel
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
name|firstModel
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-test-module"
argument_list|,
name|firstModel
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0"
argument_list|,
name|firstModel
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
comment|// Change value and save.
name|savedModel
operator|.
name|setOrigin
argument_list|(
literal|"changed"
argument_list|)
expr_stmt|;
name|projectDao
operator|.
name|saveProjectModel
argument_list|(
name|savedModel
argument_list|)
expr_stmt|;
comment|// Test that only 1 object is saved.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|projectDao
operator|.
name|queryProjectModels
argument_list|(
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Get the specific artifact.
name|ArchivaProjectModel
name|actualModel
init|=
name|projectDao
operator|.
name|getProjectModel
argument_list|(
literal|"org.apache.maven.archiva"
argument_list|,
literal|"archiva-test-module"
argument_list|,
literal|"1.0"
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|actualModel
argument_list|)
expr_stmt|;
comment|// Test expected values.
name|assertEquals
argument_list|(
literal|"archiva-test-module"
argument_list|,
name|actualModel
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"changed"
argument_list|,
name|actualModel
operator|.
name|getOrigin
argument_list|()
argument_list|)
expr_stmt|;
comment|// Test that only 1 object is saved.
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|projectDao
operator|.
name|queryProjectModels
argument_list|(
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
comment|// Delete object.
name|projectDao
operator|.
name|deleteProjectModel
argument_list|(
name|actualModel
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|projectDao
operator|.
name|queryProjectModels
argument_list|(
literal|null
argument_list|)
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSaveGetRealProjectModel
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|groupId
init|=
literal|"org.apache.maven.shared"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"maven-shared-jar"
decl_stmt|;
name|String
name|version
init|=
literal|"1.0-SNAPSHOT"
decl_stmt|;
name|ProjectModelDAO
name|projectDao
init|=
name|dao
operator|.
name|getProjectModelDAO
argument_list|()
decl_stmt|;
name|ProjectModelReader
name|modelReader
init|=
operator|(
name|ProjectModelReader
operator|)
name|lookup
argument_list|(
name|ProjectModelReader
operator|.
name|class
argument_list|,
literal|"model400"
argument_list|)
decl_stmt|;
name|File
name|pomFile
init|=
name|getTestFile
argument_list|(
literal|"src/test/resources/projects/maven-shared-jar-1.0-SNAPSHOT.pom"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"pom file should exist: "
operator|+
name|pomFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|pomFile
operator|.
name|exists
argument_list|()
operator|&&
name|pomFile
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|modelReader
operator|.
name|read
argument_list|(
name|pomFile
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Model should not be null."
argument_list|,
name|model
argument_list|)
expr_stmt|;
comment|// Fill in missing (mandatory) fields
name|model
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|model
operator|.
name|setOrigin
argument_list|(
literal|"testcase"
argument_list|)
expr_stmt|;
name|projectDao
operator|.
name|saveProjectModel
argument_list|(
name|model
argument_list|)
expr_stmt|;
name|ArchivaProjectModel
name|savedModel
init|=
name|projectDao
operator|.
name|getProjectModel
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Project model should not be null."
argument_list|,
name|savedModel
argument_list|)
expr_stmt|;
comment|// Test proper detachment of sub-objects.
name|assertNotNull
argument_list|(
literal|"model.parent != null"
argument_list|,
name|savedModel
operator|.
name|getParentProject
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

