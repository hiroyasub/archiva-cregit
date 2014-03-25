begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|services
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|ArtifactContentEntry
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
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|JUnit4
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
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

begin_import
import|import static
name|org
operator|.
name|assertj
operator|.
name|core
operator|.
name|api
operator|.
name|Assertions
operator|.
name|assertThat
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|JUnit4
operator|.
name|class
argument_list|)
specifier|public
class|class
name|ArtifactContentEntriesTests
extends|extends
name|TestCase
block|{
specifier|protected
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
name|DefaultBrowseService
name|browseService
init|=
operator|new
name|DefaultBrowseService
argument_list|()
decl_stmt|;
specifier|public
name|String
name|getBasedir
parameter_list|()
block|{
return|return
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readArtifactContentEntriesRootPathNull
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi/commons-logging/commons-logging/1.1/commons-logging-1.1.jar"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactContentEntry
argument_list|>
name|artifactContentEntries
init|=
name|browseService
operator|.
name|readFileEntries
argument_list|(
name|file
argument_list|,
literal|null
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifactContentEntries: {}"
argument_list|,
name|artifactContentEntries
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artifactContentEntries
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"org"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"META-INF"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readArtifactContentEntriesRootPathEmpty
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi/commons-logging/commons-logging/1.1/commons-logging-1.1.jar"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactContentEntry
argument_list|>
name|artifactContentEntries
init|=
name|browseService
operator|.
name|readFileEntries
argument_list|(
name|file
argument_list|,
literal|""
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifactContentEntries: {}"
argument_list|,
name|artifactContentEntries
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artifactContentEntries
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"org"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"META-INF"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readArtifactContentEntriesRootSlash
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi/commons-logging/commons-logging/1.1/commons-logging-1.1.jar"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactContentEntry
argument_list|>
name|artifactContentEntries
init|=
name|browseService
operator|.
name|readFileEntries
argument_list|(
name|file
argument_list|,
literal|"/"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifactContentEntries: {}"
argument_list|,
name|artifactContentEntries
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artifactContentEntries
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|2
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"org"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"META-INF"
argument_list|,
literal|false
argument_list|,
literal|0
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readArtifactContentEntriesSecondDepthOnlyOneDirectory
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi/commons-logging/commons-logging/1.1/commons-logging-1.1.jar"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactContentEntry
argument_list|>
name|artifactContentEntries
init|=
name|browseService
operator|.
name|readFileEntries
argument_list|(
name|file
argument_list|,
literal|"org"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifactContentEntries: {}"
argument_list|,
name|artifactContentEntries
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artifactContentEntries
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|1
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"org/apache"
argument_list|,
literal|false
argument_list|,
literal|1
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readArtifactContentEntriesOnlyFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi/commons-logging/commons-logging/1.1/commons-logging-1.1.jar"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactContentEntry
argument_list|>
name|artifactContentEntries
init|=
name|browseService
operator|.
name|readFileEntries
argument_list|(
name|file
argument_list|,
literal|"org/apache/commons/logging/impl/"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifactContentEntries: {}"
argument_list|,
name|artifactContentEntries
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artifactContentEntries
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|16
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"org/apache/commons/logging/impl/AvalonLogger.class"
argument_list|,
literal|true
argument_list|,
literal|5
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|readArtifactContentEntriesDirectoryAndFiles
parameter_list|()
throws|throws
name|Exception
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/repo-with-osgi/commons-logging/commons-logging/1.1/commons-logging-1.1.jar"
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|ArtifactContentEntry
argument_list|>
name|artifactContentEntries
init|=
name|browseService
operator|.
name|readFileEntries
argument_list|(
name|file
argument_list|,
literal|"org/apache/commons/logging/"
argument_list|,
literal|"foo"
argument_list|)
decl_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"artifactContentEntries: {}"
argument_list|,
name|artifactContentEntries
argument_list|)
expr_stmt|;
name|assertThat
argument_list|(
name|artifactContentEntries
argument_list|)
operator|.
name|isNotNull
argument_list|()
operator|.
name|isNotEmpty
argument_list|()
operator|.
name|hasSize
argument_list|(
literal|10
argument_list|)
operator|.
name|contains
argument_list|(
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"org/apache/commons/logging/impl"
argument_list|,
literal|false
argument_list|,
literal|4
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
operator|new
name|ArtifactContentEntry
argument_list|(
literal|"org/apache/commons/logging/LogSource.class"
argument_list|,
literal|true
argument_list|,
literal|4
argument_list|,
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

