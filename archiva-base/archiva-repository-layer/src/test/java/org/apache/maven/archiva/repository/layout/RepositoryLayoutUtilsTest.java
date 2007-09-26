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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * RepositoryLayoutUtilsTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryLayoutUtilsTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSplitFilenameBasic
parameter_list|()
throws|throws
name|LayoutException
block|{
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-2.1.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.1"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSplitFilenameMavenTestPlugin
parameter_list|()
throws|throws
name|LayoutException
block|{
comment|// Using maven 2 logic (artifactId is present in full path)
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"maven-test-plugin-1.8.2.jar"
argument_list|,
literal|"maven-test-plugin"
argument_list|)
argument_list|,
literal|"maven-test-plugin"
argument_list|,
literal|"1.8.2"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
comment|// Using maven 1 logic (artifactId is unknown)
comment|// [MRM-519] fail to resolve artifactId for libs that contain versionKeyword in artifactId, like "maven-test-plugin"
comment|/*         assertFilenameParts( RepositoryLayoutUtils.splitFilename( "maven-test-plugin-1.8.2.jar", null ),                              "maven-test-plugin", "1.8.2", null, "jar" );          */
block|}
specifier|public
name|void
name|testSplitFilenameAlphaVersion
parameter_list|()
throws|throws
name|LayoutException
block|{
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-2.0-alpha-1.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.0-alpha-1"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSplitFilenameSnapshot
parameter_list|()
throws|throws
name|LayoutException
block|{
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"foo-2.0-SNAPSHOT.jar"
argument_list|,
literal|"foo"
argument_list|)
argument_list|,
literal|"foo"
argument_list|,
literal|"2.0-SNAPSHOT"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSplitFilenameUniqueSnapshot
parameter_list|()
throws|throws
name|LayoutException
block|{
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"fletch-2.0-20060822-123456-35.tar.gz"
argument_list|,
literal|"fletch"
argument_list|)
argument_list|,
literal|"fletch"
argument_list|,
literal|"2.0-20060822-123456-35"
argument_list|,
literal|null
argument_list|,
literal|"tar.gz"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSplitFilenameBasicClassifier
parameter_list|()
throws|throws
name|LayoutException
block|{
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-2.1-sources.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.1"
argument_list|,
literal|"sources"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-2.1-javadoc.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.1"
argument_list|,
literal|"javadoc"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSplitFilenameAlphaClassifier
parameter_list|()
throws|throws
name|LayoutException
block|{
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-2.0-alpha-1-sources.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.0-alpha-1"
argument_list|,
literal|"sources"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-2.0-alpha-1-javadoc.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.0-alpha-1"
argument_list|,
literal|"javadoc"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSplitFilenameSnapshotClassifier
parameter_list|()
throws|throws
name|LayoutException
block|{
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-3.1-SNAPSHOT-sources.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"3.1-SNAPSHOT"
argument_list|,
literal|"sources"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-3.1-SNAPSHOT-javadoc.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"3.1-SNAPSHOT"
argument_list|,
literal|"javadoc"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSplitFilenameUniqueSnapshotClassifier
parameter_list|()
throws|throws
name|LayoutException
block|{
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-3.1-SNAPSHOT-sources.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"3.1-SNAPSHOT"
argument_list|,
literal|"sources"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang-3.1-SNAPSHOT-javadoc.jar"
argument_list|,
literal|"commons-lang"
argument_list|)
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"3.1-SNAPSHOT"
argument_list|,
literal|"javadoc"
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSplitFilenameApacheIncubator
parameter_list|()
throws|throws
name|LayoutException
block|{
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"cxf-common-2.0-incubator-M1.pom"
argument_list|,
literal|null
argument_list|)
argument_list|,
literal|"cxf-common"
argument_list|,
literal|"2.0-incubator-M1"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
name|assertFilenameParts
argument_list|(
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commonj-api_r1.1-1.0-incubator-M2.jar"
argument_list|,
literal|null
argument_list|)
argument_list|,
literal|"commonj-api_r1.1"
argument_list|,
literal|"1.0-incubator-M2"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSplitFilenameBlankInputs
parameter_list|()
block|{
try|try
block|{
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an IllegalArgumentException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should have thrown an IllegalArgumentException."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|""
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an IllegalArgumentException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should have thrown an IllegalArgumentException."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"   "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an IllegalArgumentException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should have thrown an IllegalArgumentException."
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|" \t  \n  "
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an IllegalArgumentException."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IllegalArgumentException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
name|fail
argument_list|(
literal|"Should have thrown an IllegalArgumentException."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|testSplitFilenameBadInputs
parameter_list|()
block|{
try|try
block|{
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"commons-lang.jar"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a LayoutException (No Version)."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* Expected Path */
block|}
try|try
block|{
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"geronimo-store"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a LayoutException (No Extension)."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* Expected Path */
block|}
try|try
block|{
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"The Sixth Sick Sheiks Sixth Sheep is Sick."
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a LayoutException (No Extension)."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* Expected Path */
block|}
try|try
block|{
name|RepositoryLayoutUtils
operator|.
name|splitFilename
argument_list|(
literal|"1.0.jar"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a LayoutException (No Artifact ID)."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* Expected Path */
block|}
block|}
specifier|private
name|void
name|assertFilenameParts
parameter_list|(
name|FilenameParts
name|actualParts
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
name|extension
parameter_list|)
block|{
name|assertEquals
argument_list|(
literal|"Split - artifactId"
argument_list|,
name|artifactId
argument_list|,
name|actualParts
operator|.
name|artifactId
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Split - version"
argument_list|,
name|version
argument_list|,
name|actualParts
operator|.
name|version
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Split - classifier"
argument_list|,
name|classifier
argument_list|,
name|actualParts
operator|.
name|classifier
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Split - extension"
argument_list|,
name|extension
argument_list|,
name|actualParts
operator|.
name|extension
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

