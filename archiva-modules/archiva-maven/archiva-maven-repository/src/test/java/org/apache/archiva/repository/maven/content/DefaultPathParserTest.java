begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|maven
operator|.
name|content
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|LayoutException
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
name|repository
operator|.
name|content
operator|.
name|ItemSelector
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
name|repository
operator|.
name|content
operator|.
name|PathParser
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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang3
operator|.
name|StringUtils
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
import|import static
name|org
operator|.
name|junit
operator|.
name|Assert
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * DefaultPathParserTest  *  * TODO: move to path translator tests  *  *  */
end_comment

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
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|DefaultPathParserTest
block|{
specifier|private
name|PathParser
name|parser
init|=
operator|new
name|DefaultPathParser
argument_list|()
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|testBadPathMissingType
parameter_list|()
block|{
comment|// TODO: should we allow this instead?
name|assertBadPath
argument_list|(
literal|"invalid/invalid/1/invalid-1"
argument_list|,
literal|"missing type"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadPathReleaseInSnapshotDir
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"invalid/invalid/1.0-SNAPSHOT/invalid-1.0.jar"
argument_list|,
literal|"non snapshot artifact inside of a snapshot dir"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadPathTimestampedSnapshotNotInSnapshotDir
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"invalid/invalid/1.0-20050611.123456-1/invalid-1.0-20050611.123456-1.jar"
argument_list|,
literal|"Timestamped Snapshot artifact not inside of an Snapshot dir"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadPathTooShort
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"invalid/invalid-1.0.jar"
argument_list|,
literal|"path is too short"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadPathVersionMismatchA
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"invalid/invalid/1.0/invalid-2.0.jar"
argument_list|,
literal|"version mismatch between path and artifact"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadPathVersionMismatchB
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"invalid/invalid/1.0/invalid-1.0b.jar"
argument_list|,
literal|"version mismatch between path and artifact"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadPathWrongArtifactId
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"org/apache/maven/test/1.0-SNAPSHOT/wrong-artifactId-1.0-20050611.112233-1.jar"
argument_list|,
literal|"wrong artifact id"
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-481] Artifact requests with a .xml.zip extension fail with a 404 Error      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodButDualExtensions
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"org.project"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"example-presentation"
decl_stmt|;
name|String
name|version
init|=
literal|"3.2"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"3.2"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"xml.zip"
decl_stmt|;
name|String
name|path
init|=
literal|"org/project/example-presentation/3.2/example-presentation-3.2.xml.zip"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoodButDualExtensionsWithClassifier
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"org.project"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"example-presentation"
decl_stmt|;
name|String
name|version
init|=
literal|"3.2"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"3.2"
decl_stmt|;
name|String
name|classifier
init|=
literal|"extras"
decl_stmt|;
name|String
name|type
init|=
literal|"xml.zip"
decl_stmt|;
name|String
name|path
init|=
literal|"org/project/example-presentation/3.2/example-presentation-3.2-extras.xml.zip"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoodButDualExtensionsTarGz
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"org.project"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"example-distribution"
decl_stmt|;
name|String
name|version
init|=
literal|"1.3"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"1.3"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"tar.gz"
decl_stmt|;
comment|// no longer using distribution-tgz / distribution-zip in maven 2
name|String
name|path
init|=
literal|"org/project/example-distribution/1.3/example-distribution-1.3.tar.gz"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoodButDualExtensionsTarGzAndClassifier
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"org.project"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"example-distribution"
decl_stmt|;
name|String
name|version
init|=
literal|"1.3"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"1.3"
decl_stmt|;
name|String
name|classifier
init|=
literal|"bin"
decl_stmt|;
name|String
name|type
init|=
literal|"tar.gz"
decl_stmt|;
comment|// no longer using distribution-tgz / distribution-zip in maven 2
name|String
name|path
init|=
literal|"org/project/example-distribution/1.3/example-distribution-1.3-bin.tar.gz"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-432] Oddball version spec.      * Example of an oddball / unusual version spec.      *      * @throws LayoutException      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodButOddVersionSpecGanymedSsh2
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"ch.ethz.ganymed"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"ganymed-ssh2"
decl_stmt|;
name|String
name|version
init|=
literal|"build210"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"build210"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"ch/ethz/ganymed/ganymed-ssh2/build210/ganymed-ssh2-build210.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-432] Oddball version spec.      * Example of an oddball / unusual version spec.      *      * @throws LayoutException      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodButOddVersionSpecJavaxComm
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"javax"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"comm"
decl_stmt|;
name|String
name|version
init|=
literal|"3.0-u1"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"3.0-u1"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"javax/comm/3.0-u1/comm-3.0-u1.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the ejb-client type spec.      * Type specs are not a 1 to 1 map to the extension.      * This tests that effect.      * @throws LayoutException      */
comment|/* TODO: Re-enabled in the future.     public void testGoodFooEjbClient()         throws LayoutException     {         String groupId = "com.foo";         String artifactId = "foo-client";         String version = "1.0";         String classifier = null;         String type = "ejb-client"; // oddball type-spec (should result in jar extension)         String path = "com/foo/foo-client/1.0/foo-client-1.0.jar";          assertLayout( path, groupId, artifactId, version, classifier, type );     }     */
comment|/**      * [MRM-432] Oddball version spec.      * Example of an oddball / unusual version spec.      *      * @throws LayoutException      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodButOddVersionSpecJavaxPersistence
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"javax.persistence"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"ejb"
decl_stmt|;
name|String
name|version
init|=
literal|"3.0-public_review"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"3.0-public_review"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"javax/persistence/ejb/3.0-public_review/ejb-3.0-public_review.jar"
decl_stmt|;
comment|/*          * The version id of "public_review" can cause problems. is it part of          * the version spec? or the classifier?          * Since the path spec below shows it in the path, then it is really          * part of the version spec.          */
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoodComFooTool
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"com.foo"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"foo-tool"
decl_stmt|;
name|String
name|version
init|=
literal|"1.0"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"1.0"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"com/foo/foo-tool/1.0/foo-tool-1.0.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoodCommonsLang
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"commons-lang"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"commons-lang"
decl_stmt|;
name|String
name|version
init|=
literal|"2.1"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"2.1"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"commons-lang/commons-lang/2.1/commons-lang-2.1.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testWindowsPathSeparator
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"commons-lang"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"commons-lang"
decl_stmt|;
name|String
name|version
init|=
literal|"2.1"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"2.1"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"commons-lang\\commons-lang/2.1\\commons-lang-2.1.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-486] Can not deploy artifact test.maven-arch:test-arch due to "No ArtifactID Detected"      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodDashedArtifactId
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"test.maven-arch"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"test-arch"
decl_stmt|;
name|String
name|version
init|=
literal|"2.0.3-SNAPSHOT"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"2.0.3-SNAPSHOT"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"pom"
decl_stmt|;
name|String
name|path
init|=
literal|"test/maven-arch/test-arch/2.0.3-SNAPSHOT/test-arch-2.0.3-SNAPSHOT.pom"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * It may seem odd, but this is a valid artifact.      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodDotNotationArtifactId
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"com.company.department"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"com.company.department"
decl_stmt|;
name|String
name|version
init|=
literal|"0.2"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"0.2"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"pom"
decl_stmt|;
name|String
name|path
init|=
literal|"com/company/department/com.company.department/0.2/com.company.department-0.2.pom"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * It may seem odd, but this is a valid artifact.      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodDotNotationSameGroupIdAndArtifactId
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"com.company.department"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"com.company.department.project"
decl_stmt|;
name|String
name|version
init|=
literal|"0.3"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"0.3"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"pom"
decl_stmt|;
name|String
name|path
init|=
literal|"com/company/department/com.company.department.project/0.3/com.company.department.project-0.3.pom"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the classifier, and java-source type spec.      *      * @throws LayoutException      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodFooLibSources
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"com.foo.lib"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"foo-lib"
decl_stmt|;
name|String
name|version
init|=
literal|"2.1-alpha-1"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"2.1-alpha-1"
decl_stmt|;
name|String
name|classifier
init|=
literal|"sources"
decl_stmt|;
name|String
name|type
init|=
literal|"java-source"
decl_stmt|;
comment|// oddball type-spec (should result in jar extension)
name|String
name|path
init|=
literal|"com/foo/lib/foo-lib/2.1-alpha-1/foo-lib-2.1-alpha-1-sources.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * A timestamped versioned artifact, should reside in a SNAPSHOT baseversion directory.      *      * @throws LayoutException      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodSnapshotMavenTest
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"org.apache.archiva.test"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"redonkulous"
decl_stmt|;
name|String
name|version
init|=
literal|"3.1-beta-1-SNAPSHOT"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"3.1-beta-1-20050831.101112-42"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"org/apache/archiva/test/redonkulous/3.1-beta-1-SNAPSHOT/redonkulous-3.1-beta-1-20050831.101112-42.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * A timestamped versioned artifact, should reside in a SNAPSHOT baseversion directory.      *      * @throws LayoutException      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodLongSnapshotMavenTest
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"a.group.id"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"artifact-id"
decl_stmt|;
name|String
name|version
init|=
literal|"1.0-abc-1.1-SNAPSHOT"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"1.0-abc-1.1-20080221.062205-9"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"pom"
decl_stmt|;
name|String
name|path
init|=
literal|"a/group/id/artifact-id/1.0-abc-1.1-SNAPSHOT/artifact-id-1.0-abc-1.1-20080221.062205-9.pom"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * A timestamped versioned artifact but without release version part. Like on axiom trunk.      */
annotation|@
name|Test
specifier|public
name|void
name|testBadSnapshotWithoutReleasePart
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"org/apache/ws/commons/axiom/axiom/SNAPSHOT/axiom-20070912.093446-2.pom"
argument_list|,
literal|"snapshot version without release part"
argument_list|)
expr_stmt|;
block|}
comment|/**      * A timestamped versioned artifact, should reside in a SNAPSHOT baseversion directory.      *      * @throws LayoutException      */
annotation|@
name|Test
specifier|public
name|void
name|testClassifiedSnapshotMavenTest
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"a.group.id"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"artifact-id"
decl_stmt|;
name|String
name|version
init|=
literal|"1.0-SNAPSHOT"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"1.0-20070219.171202-34"
decl_stmt|;
name|String
name|classifier
init|=
literal|"test-sources"
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"a/group/id/artifact-id/1.0-SNAPSHOT/artifact-id-1.0-20070219.171202-34-test-sources.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-519] version identifiers within filename cause misidentification of version.      * Example uses "test" in artifact Id, which is also part of the versionKeyword list.      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodVersionKeywordInArtifactId
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"maven"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"maven-test-plugin"
decl_stmt|;
name|String
name|version
init|=
literal|"1.8.2"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"1.8.2"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"pom"
decl_stmt|;
name|String
name|path
init|=
literal|"maven/maven-test-plugin/1.8.2/maven-test-plugin-1.8.2.pom"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-562] Artifact type "maven-plugin" is not detected correctly in .toArtifactReference() methods.      * Example uses "test" in artifact Id, which is also part of the versionKeyword list.      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodDetectMavenTestPlugin
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"maven"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"maven-test-plugin"
decl_stmt|;
name|String
name|version
init|=
literal|"1.8.2"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"1.8.2"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"maven-plugin"
decl_stmt|;
name|String
name|path
init|=
literal|"maven/maven-test-plugin/1.8.2/maven-test-plugin-1.8.2.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-562] Artifact type "maven-plugin" is not detected correctly in .toArtifactReference() methods.      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodDetectCoberturaMavenPlugin
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"org.codehaus.mojo"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"cobertura-maven-plugin"
decl_stmt|;
name|String
name|version
init|=
literal|"2.1"
decl_stmt|;
name|String
name|artifactVersion
init|=
literal|"2.1"
decl_stmt|;
name|String
name|classifier
init|=
literal|null
decl_stmt|;
name|String
name|type
init|=
literal|"maven-plugin"
decl_stmt|;
name|String
name|path
init|=
literal|"org/codehaus/mojo/cobertura-maven-plugin/2.1/cobertura-maven-plugin-2.1.jar"
decl_stmt|;
name|assertLayout
argument_list|(
name|path
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToArtifactOnEmptyPath
parameter_list|()
block|{
try|try
block|{
name|parser
operator|.
name|toItemSelector
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed due to empty path."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToArtifactOnNullPath
parameter_list|()
block|{
try|try
block|{
name|parser
operator|.
name|toItemSelector
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed due to null path."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToArtifactReferenceOnEmptyPath
parameter_list|()
block|{
try|try
block|{
name|parser
operator|.
name|toItemSelector
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed due to empty path."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
annotation|@
name|Test
specifier|public
name|void
name|testToArtifactReferenceOnNullPath
parameter_list|()
block|{
try|try
block|{
name|parser
operator|.
name|toItemSelector
argument_list|(
literal|null
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed due to null path."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
comment|/**      * Perform a path to artifact reference lookup, and verify the results.      */
specifier|private
name|void
name|assertLayout
parameter_list|(
name|String
name|path
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
name|artifactVersion
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|LayoutException
block|{
comment|// Path to Artifact Reference.
name|ItemSelector
name|testReference
init|=
name|parser
operator|.
name|toItemSelector
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|assertArtifactReference
argument_list|(
name|testReference
argument_list|,
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|artifactVersion
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertArtifactReference
parameter_list|(
name|ItemSelector
name|actualReference
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
name|artifactVersion
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
literal|"ArtifactReference - "
operator|+
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
name|actualReference
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Group ID"
argument_list|,
name|groupId
argument_list|,
name|actualReference
operator|.
name|getNamespace
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Artifact ID"
argument_list|,
name|artifactId
argument_list|,
name|actualReference
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Artifact Version"
argument_list|,
name|artifactVersion
argument_list|,
name|actualReference
operator|.
name|getArtifactVersion
argument_list|( )
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|classifier
argument_list|)
condition|)
block|{
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Classifier"
argument_list|,
name|classifier
argument_list|,
name|actualReference
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Version ID"
argument_list|,
name|version
argument_list|,
name|actualReference
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|expectedId
operator|+
literal|" - Type"
argument_list|,
name|type
argument_list|,
name|actualReference
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertBadPath
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
try|try
block|{
name|parser
operator|.
name|toItemSelector
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a LayoutException on the invalid path ["
operator|+
name|path
operator|+
literal|"] because of ["
operator|+
name|reason
operator|+
literal|"]"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|/* expected path */
block|}
block|}
block|}
end_class

end_unit

