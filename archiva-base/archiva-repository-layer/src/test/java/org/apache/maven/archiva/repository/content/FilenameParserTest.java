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
name|content
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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

begin_comment
comment|/**  * FilenameParserTest  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|FilenameParserTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testNameExtensionJar
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"maven-test-plugin-1.8.3.jar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-test-plugin-1.8.3"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNameExtensionTarGz
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"archiva-1.0-beta-2-bin.tar.gz"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-1.0-beta-2-bin"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tar.gz"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNameExtensionTarBz2
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"archiva-1.0-SNAPSHOT-src.tar.bz2"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"archiva-1.0-SNAPSHOT-src"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"tar.bz2"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNameExtensionCapitolizedTarGz
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"ARCHIVA-1.0-BETA-2-BIN.TAR.GZ"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ARCHIVA-1.0-BETA-2-BIN"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"TAR.GZ"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNext
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"maven-test-plugin-1.8.3.jar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-test-plugin-1.8.3"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"maven"
argument_list|,
name|parser
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test"
argument_list|,
name|parser
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"plugin"
argument_list|,
name|parser
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.8.3"
argument_list|,
name|parser
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|parser
operator|.
name|next
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExpect
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"maven-test-plugin-1.8.3.jar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-test-plugin-1.8.3"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"maven-test-plugin"
argument_list|,
name|parser
operator|.
name|expect
argument_list|(
literal|"maven-test-plugin"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.8.3"
argument_list|,
name|parser
operator|.
name|expect
argument_list|(
literal|"1.8.3"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|parser
operator|.
name|expect
argument_list|(
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExpectWithRemaining
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"ganymede-ssh2-build250-sources.jar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ganymede-ssh2-build250-sources"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ganymede-ssh2"
argument_list|,
name|parser
operator|.
name|expect
argument_list|(
literal|"ganymede-ssh2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"build250"
argument_list|,
name|parser
operator|.
name|expect
argument_list|(
literal|"build250"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'-'
argument_list|,
name|parser
operator|.
name|seperator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"sources"
argument_list|,
name|parser
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|parser
operator|.
name|expect
argument_list|(
literal|"jar"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExpectWithRemainingDualExtensions
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"example-presentation-3.2.xml.zip"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"example-presentation-3.2.xml"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"zip"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"example-presentation"
argument_list|,
name|parser
operator|.
name|expect
argument_list|(
literal|"example-presentation"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"3.2"
argument_list|,
name|parser
operator|.
name|expect
argument_list|(
literal|"3.2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|'.'
argument_list|,
name|parser
operator|.
name|seperator
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"xml"
argument_list|,
name|parser
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNextNonVersion
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"maven-test-plugin-1.8.3.jar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-test-plugin"
argument_list|,
name|parser
operator|.
name|nextNonVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.8.3"
argument_list|,
name|parser
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNextArbitraryNonVersion
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"maven-jdk-1.4-plugin-1.0-20070828.123456-42.jar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"maven-jdk-1.4-plugin"
argument_list|,
name|parser
operator|.
name|nextNonVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0-20070828.123456-42"
argument_list|,
name|parser
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNextJython
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"jython-20020827-no-oro.jar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"jython"
argument_list|,
name|parser
operator|.
name|nextNonVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"20020827"
argument_list|,
name|parser
operator|.
name|nextVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"no-oro"
argument_list|,
name|parser
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testLongExtension
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"libfobs4jmf-0.4.1.4-20080217.211715-4.jnilib"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"libfobs4jmf-0.4.1.4-20080217.211715-4"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jnilib"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInterveningVersion
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"artifact-id-1.0-abc-1.1-20080221.062205-9.pom"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artifact-id"
argument_list|,
name|parser
operator|.
name|nextNonVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0-abc-1.1-20080221.062205-9"
argument_list|,
name|parser
operator|.
name|expect
argument_list|(
literal|"1.0-abc-1.1-SNAPSHOT"
argument_list|)
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|null
argument_list|,
name|parser
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artifact-id-1.0-abc-1.1-20080221.062205-9"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"pom"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExpectWrongSnapshot
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"artifact-id-1.0-20080221.062205-9.pom"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artifact-id"
argument_list|,
name|parser
operator|.
name|nextNonVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
name|parser
operator|.
name|expect
argument_list|(
literal|"2.0-SNAPSHOT"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testClassifier
parameter_list|()
block|{
name|FilenameParser
name|parser
init|=
operator|new
name|FilenameParser
argument_list|(
literal|"artifact-id-1.0-20070219.171202-34-test-sources.jar"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"artifact-id"
argument_list|,
name|parser
operator|.
name|nextNonVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"1.0-20070219.171202-34"
argument_list|,
name|parser
operator|.
name|nextVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"test-sources"
argument_list|,
name|parser
operator|.
name|remaining
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"artifact-id-1.0-20070219.171202-34-test-sources"
argument_list|,
name|parser
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"jar"
argument_list|,
name|parser
operator|.
name|getExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

