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
name|project
operator|.
name|writers
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
name|ProjectModelException
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
name|ProjectModelWriter
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
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|DetailedDiff
import|;
end_import

begin_import
import|import
name|org
operator|.
name|custommonkey
operator|.
name|xmlunit
operator|.
name|Diff
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
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|StringWriter
import|;
end_import

begin_comment
comment|/**  * ProjectModel400WriterTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ProjectModel400WriterTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|DEFAULT_REPOSITORY
init|=
literal|"src/test/repositories/default-repository"
decl_stmt|;
specifier|private
name|ProjectModelWriter
name|modelWriter
decl_stmt|;
annotation|@
name|Override
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
name|modelWriter
operator|=
operator|new
name|ProjectModel400Writer
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|testSimpleWrite
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaProjectModel
name|model
init|=
operator|new
name|ArchivaProjectModel
argument_list|()
decl_stmt|;
name|model
operator|.
name|setGroupId
argument_list|(
literal|"org.apache.archiva.test"
argument_list|)
expr_stmt|;
name|model
operator|.
name|setArtifactId
argument_list|(
literal|"simple-model-write"
argument_list|)
expr_stmt|;
name|model
operator|.
name|setVersion
argument_list|(
literal|"1.0"
argument_list|)
expr_stmt|;
name|String
name|actualModel
init|=
name|writeToString
argument_list|(
name|model
argument_list|)
decl_stmt|;
name|String
name|expectedModel
init|=
name|getExpectedModelString
argument_list|(
literal|"model-write-400-simple.pom"
argument_list|)
decl_stmt|;
name|assertModelSimilar
argument_list|(
name|expectedModel
argument_list|,
name|actualModel
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReadWriteSimple
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|pathToModel
init|=
name|DEFAULT_REPOSITORY
operator|+
literal|"/org/apache/maven/A/1.0/A-1.0.pom"
decl_stmt|;
name|ArchivaProjectModel
name|model
init|=
name|createArchivaProjectModel
argument_list|(
name|pathToModel
argument_list|)
decl_stmt|;
name|String
name|actualModel
init|=
name|writeToString
argument_list|(
name|model
argument_list|)
decl_stmt|;
name|String
name|expectedModel
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
operator|new
name|File
argument_list|(
name|pathToModel
argument_list|)
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|assertModelSimilar
argument_list|(
name|expectedModel
argument_list|,
name|actualModel
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReadWriteMavenParent
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaProjectModel
name|model
init|=
name|createArchivaProjectModel
argument_list|(
name|DEFAULT_REPOSITORY
operator|+
literal|"/org/apache/maven/maven-parent/4/maven-parent-4.pom"
argument_list|)
decl_stmt|;
name|String
name|actualModel
init|=
name|writeToString
argument_list|(
name|model
argument_list|)
decl_stmt|;
name|String
name|expectedModel
init|=
name|getExpectedModelString
argument_list|(
literal|"maven-parent-4.pom"
argument_list|)
decl_stmt|;
name|assertModelSimilar
argument_list|(
name|expectedModel
argument_list|,
name|actualModel
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testReadWriteCocoon
parameter_list|()
throws|throws
name|Exception
block|{
name|ArchivaProjectModel
name|model
init|=
name|createArchivaProjectModel
argument_list|(
name|DEFAULT_REPOSITORY
operator|+
literal|"/org/apache/cocoon/cocoon/1/cocoon-1.pom"
argument_list|)
decl_stmt|;
name|String
name|actualModel
init|=
name|writeToString
argument_list|(
name|model
argument_list|)
decl_stmt|;
name|String
name|expectedModel
init|=
name|getExpectedModelString
argument_list|(
literal|"cocoon-1.pom"
argument_list|)
decl_stmt|;
name|assertModelSimilar
argument_list|(
name|expectedModel
argument_list|,
name|actualModel
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertModelSimilar
parameter_list|(
name|String
name|expectedModel
parameter_list|,
name|String
name|actualModel
parameter_list|)
throws|throws
name|Exception
block|{
name|Diff
name|diff
init|=
operator|new
name|Diff
argument_list|(
name|expectedModel
argument_list|,
name|actualModel
argument_list|)
decl_stmt|;
name|DetailedDiff
name|detailedDiff
init|=
operator|new
name|DetailedDiff
argument_list|(
name|diff
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|detailedDiff
operator|.
name|similar
argument_list|()
condition|)
block|{
comment|// If it isn't similar, dump the difference.
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
name|detailedDiff
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"-- Actual Model --\n"
operator|+
name|actualModel
operator|+
literal|"\n---------------\n\n"
argument_list|)
expr_stmt|;
name|System
operator|.
name|out
operator|.
name|println
argument_list|(
literal|"-- Expected Model --\n"
operator|+
name|expectedModel
operator|+
literal|"\n---------------\n\n"
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedModel
argument_list|,
name|actualModel
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|String
name|getExpectedModelString
parameter_list|(
name|String
name|pomfilename
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|pomFile
init|=
name|getTestFile
argument_list|(
literal|"src/test/expected-poms/"
operator|+
name|pomfilename
argument_list|)
decl_stmt|;
return|return
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|pomFile
argument_list|,
literal|null
argument_list|)
return|;
block|}
specifier|private
name|ArchivaProjectModel
name|createArchivaProjectModel
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ProjectModelException
block|{
name|ProjectModelReader
name|reader
init|=
operator|new
name|ProjectModel400Reader
argument_list|()
decl_stmt|;
name|File
name|pomFile
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
return|return
name|reader
operator|.
name|read
argument_list|(
name|pomFile
argument_list|)
return|;
block|}
specifier|private
name|String
name|writeToString
parameter_list|(
name|ArchivaProjectModel
name|model
parameter_list|)
throws|throws
name|ProjectModelException
throws|,
name|IOException
block|{
name|StringWriter
name|writer
init|=
operator|new
name|StringWriter
argument_list|()
decl_stmt|;
name|modelWriter
operator|.
name|write
argument_list|(
name|model
argument_list|,
name|writer
argument_list|)
expr_stmt|;
return|return
name|writer
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

