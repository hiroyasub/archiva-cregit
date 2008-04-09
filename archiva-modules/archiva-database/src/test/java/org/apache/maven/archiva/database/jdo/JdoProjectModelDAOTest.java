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
name|commons
operator|.
name|beanutils
operator|.
name|PropertyUtils
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
name|repository
operator|.
name|project
operator|.
name|ProjectModelReader
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
name|readers
operator|.
name|ProjectModel400Reader
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

begin_import
import|import
name|javax
operator|.
name|jdo
operator|.
name|JDOHelper
import|;
end_import

begin_comment
comment|/**  * JdoProjectModelDAOTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
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
operator|new
name|ProjectModel400Reader
argument_list|()
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
comment|/* NOTE: We are intentionally using a basic project model in this unit test.          *       The expansion of expressions, resolving of dependencies, and merging          *       of parent poms is *NOT* performed to keep this unit test simple.          */
comment|// Fill in mandatory/missing fields
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
name|List
name|exprs
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"parentProject.groupId"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"organization.name"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"issueManagement.system"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"ciManagement.system"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"scm.url"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"individuals[0].name"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"dependencies[0].groupId"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"dependencyManagement[0].artifactId"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"repositories[0].id"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"plugins[0].artifactId"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"reports[0].artifactId"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"buildExtensions[0].artifactId"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"licenses[0].url"
argument_list|)
expr_stmt|;
name|exprs
operator|.
name|add
argument_list|(
literal|"mailingLists[0].name"
argument_list|)
expr_stmt|;
name|Iterator
name|it
init|=
name|exprs
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
name|String
name|expr
init|=
operator|(
name|String
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
try|try
block|{
name|Object
name|obj
init|=
name|PropertyUtils
operator|.
name|getProperty
argument_list|(
name|model
argument_list|,
name|expr
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Expr \""
operator|+
name|expr
operator|+
literal|"\" != null"
argument_list|,
name|obj
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Expr \""
operator|+
name|expr
operator|+
literal|"\" should be a String."
argument_list|,
operator|(
name|obj
operator|instanceof
name|String
operator|)
argument_list|)
expr_stmt|;
name|String
name|value
init|=
operator|(
name|String
operator|)
name|obj
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Expr \""
operator|+
name|expr
operator|+
literal|"\" value should not be blank."
argument_list|,
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndexOutOfBoundsException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Expr \""
operator|+
name|expr
operator|+
literal|"\" unable to get indexed property: "
operator|+
name|e
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

