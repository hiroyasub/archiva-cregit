begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
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
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|test
operator|.
name|utils
operator|.
name|ArchivaSpringJUnit4ClassRunner
import|;
end_import

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaSpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|FileTypesTest
extends|extends
name|TestCase
block|{
annotation|@
name|Inject
specifier|private
name|FileTypes
name|filetypes
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
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIsArtifact
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
literal|"test.maven-arch/poms/test-arch-2.0.3-SNAPSHOT.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
literal|"test/maven-arch/test-arch/2.0.3-SNAPSHOT/test-arch-2.0.3-SNAPSHOT.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.sha1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.md5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.asc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.pgp"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filetypes
operator|.
name|matchesArtifactPattern
argument_list|(
literal|"org/apache/derby/derby/maven-metadata.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testDefaultExclusions
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
literal|"repository/test/.index/nexus-maven-repository-index.gz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
literal|"repository/test/.index/nexus-maven-repository-index.zip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
literal|"repository/test/org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.sha1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
literal|"repository/test/org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.md5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
literal|"repository/test/org/apache/derby/derby/10.2.2.0/maven-metadata.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
literal|"repository/test/org/apache/derby/derby/10.2.2.0/maven-metadata.xml.sha1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
literal|"repository/test/org/apache/derby/derby/10.2.2.0/maven-metadata.xml.md5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
literal|"repository/test/org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.zip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|filetypes
operator|.
name|matchesDefaultExclusions
argument_list|(
literal|"repository/test/org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz"
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

