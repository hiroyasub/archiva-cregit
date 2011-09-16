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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|model
operator|.
name|ArtifactReference
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
name|AbstractRepositoryLayerTestCase
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
name|layout
operator|.
name|LayoutException
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
comment|/**  * AbstractLegacyRepositoryContentTestCase  *  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractLegacyRepositoryContentTestCase
extends|extends
name|AbstractRepositoryLayerTestCase
block|{
annotation|@
name|Test
specifier|public
name|void
name|testBadPathArtifactIdMissingA
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"groupId/jars/-1.0.jar"
argument_list|,
literal|"artifactId is missing"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadPathArtifactIdMissingB
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"groupId/jars/1.0.jar"
argument_list|,
literal|"artifactId is missing"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testBadPathMissingType
parameter_list|()
block|{
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
name|testBadPathTooShort
parameter_list|()
block|{
comment|// NEW
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
name|testBadPathWrongPackageExtension
parameter_list|()
block|{
name|assertBadPath
argument_list|(
literal|"org.apache.maven.test/jars/artifactId-1.0.war"
argument_list|,
literal|"wrong package extension"
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-432] Oddball version spec.      * Example of an oddball / unusual version spec.      * @throws LayoutException      */
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
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"ch.ethz.ganymed/jars/ganymed-ssh2-build210.jar"
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
literal|null
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-432] Oddball version spec.      * Example of an oddball / unusual version spec.      * @throws LayoutException      */
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
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"javax/jars/comm-3.0-u1.jar"
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
literal|null
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-432] Oddball version spec.      * Example of an oddball / unusual version spec.      * @throws LayoutException      */
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
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"javax.persistence/jars/ejb-3.0-public_review.jar"
decl_stmt|;
comment|/*          * The version id of "public_review" can cause problems. is it part of          * the version spec? or the classifier?          */
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
literal|null
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
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"commons-lang/jars/commons-lang-2.1.jar"
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
literal|null
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoodDerby
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"org.apache.derby"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"derby"
decl_stmt|;
name|String
name|version
init|=
literal|"10.2.2.0"
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"org.apache.derby/jars/derby-10.2.2.0.jar"
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
literal|null
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the ejb-client type spec.      * Type specs are not a 1 to 1 map to the extension.      * This tests that effect.      * @throws LayoutException      */
comment|/* TODO: Re-enabled in the future.     public void testGoodFooEjbClient()         throws LayoutException     {         String groupId = "com.foo";         String artifactId = "foo-client";         String version = "1.0";         String type = "ejb"; // oddball type-spec (should result in jar extension)         String path = "com.foo/ejbs/foo-client-1.0.jar";          assertLayout( path, groupId, artifactId, version, classifier, type );     }     */
comment|/**      * Test the classifier.      * @throws LayoutException      */
annotation|@
name|Test
specifier|public
name|void
name|testGoodFooLibJavadoc
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
name|type
init|=
literal|"javadoc"
decl_stmt|;
name|String
name|classifier
init|=
literal|"javadoc"
decl_stmt|;
name|String
name|path
init|=
literal|"com.foo.lib/javadoc.jars/foo-lib-2.1-alpha-1-javadoc.jar"
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the classifier, and java-source type spec.      * @throws LayoutException      */
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
name|type
init|=
literal|"java-source"
decl_stmt|;
comment|// oddball type-spec (should result in jar extension)
name|String
name|classifier
init|=
literal|"sources"
decl_stmt|;
name|String
name|path
init|=
literal|"com.foo.lib/java-sources/foo-lib-2.1-alpha-1-sources.jar"
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
name|testGoodFooTool
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
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"com.foo/jars/foo-tool-1.0.jar"
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
literal|null
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoodGeronimoEjbSpec
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"org.apache.geronimo.specs"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"geronimo-ejb_2.1_spec"
decl_stmt|;
name|String
name|version
init|=
literal|"1.0.1"
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"org.apache.geronimo.specs/jars/geronimo-ejb_2.1_spec-1.0.1.jar"
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
literal|null
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testGoodLdapClientsPom
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"directory-clients"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"ldap-clients"
decl_stmt|;
name|String
name|version
init|=
literal|"0.9.1-SNAPSHOT"
decl_stmt|;
name|String
name|type
init|=
literal|"pom"
decl_stmt|;
name|String
name|path
init|=
literal|"directory-clients/poms/ldap-clients-0.9.1-SNAPSHOT.pom"
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
literal|null
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * A timestamped versioned artifact, should reside in a SNAPSHOT baseversion directory.      * @throws LayoutException      */
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
literal|"3.1-beta-1-20050831.101112-42"
decl_stmt|;
name|String
name|type
init|=
literal|"jar"
decl_stmt|;
name|String
name|path
init|=
literal|"org.apache.archiva.test/jars/redonkulous-3.1-beta-1-20050831.101112-42.jar"
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
literal|null
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
name|type
init|=
literal|"pom"
decl_stmt|;
name|String
name|path
init|=
literal|"maven/poms/maven-test-plugin-1.8.2.pom"
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
literal|null
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
name|testGoodDetectPluginMavenTest
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
name|type
init|=
literal|"maven-one-plugin"
decl_stmt|;
name|String
name|path
init|=
literal|"maven/plugins/maven-test-plugin-1.8.2.jar"
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
literal|null
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
name|testGoodDetectPluginAvalonMeta
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"avalon-meta"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"avalon-meta-plugin"
decl_stmt|;
name|String
name|version
init|=
literal|"1.1"
decl_stmt|;
name|String
name|type
init|=
literal|"maven-one-plugin"
decl_stmt|;
name|String
name|path
init|=
literal|"avalon-meta/plugins/avalon-meta-plugin-1.1.jar"
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
literal|null
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
name|testGoodDetectPluginCactusMaven
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"cactus"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"cactus-maven"
decl_stmt|;
name|String
name|version
init|=
literal|"1.7dev-20040815"
decl_stmt|;
name|String
name|type
init|=
literal|"maven-one-plugin"
decl_stmt|;
name|String
name|path
init|=
literal|"cactus/plugins/cactus-maven-1.7dev-20040815.jar"
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
literal|null
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
name|testGoodDetectPluginGeronimoPackaging
parameter_list|()
throws|throws
name|LayoutException
block|{
name|String
name|groupId
init|=
literal|"geronimo"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"geronimo-packaging-plugin"
decl_stmt|;
name|String
name|version
init|=
literal|"1.0.1"
decl_stmt|;
name|String
name|type
init|=
literal|"maven-one-plugin"
decl_stmt|;
name|String
name|path
init|=
literal|"geronimo/plugins/geronimo-packaging-plugin-1.0.1.jar"
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
literal|null
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/** 	 * [MRM-768] Artifact type "maven-plugin" does not distinguish maven1 and maven2 plugins.  	 * This produces conflicts when m2 plugins are stored in legacy-layout repository 	 */
annotation|@
name|Test
specifier|public
name|void
name|testMaven1Maven2PluginTypeDistinc
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|groupId
init|=
literal|"com.sun.tools.xjc.maven2"
decl_stmt|;
name|String
name|artifactId
init|=
literal|"maven-jaxb-plugin"
decl_stmt|;
name|String
name|version
init|=
literal|"1.1"
decl_stmt|;
name|String
name|type
init|=
literal|"maven-plugin"
decl_stmt|;
name|String
name|path
init|=
literal|"com.sun.tools.xjc.maven2/maven-plugins/maven-jaxb-plugin-1.1.jar"
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
literal|null
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Perform a roundtrip through the layout routines to determine success.      * @param classifier TODO      */
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
name|classifier
parameter_list|,
name|String
name|type
parameter_list|)
throws|throws
name|LayoutException
block|{
name|ArtifactReference
name|expectedArtifact
init|=
name|createArtifact
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
comment|// --- Artifact Tests.
comment|// Artifact to Path
name|assertEquals
argument_list|(
literal|"Artifact<"
operator|+
name|expectedArtifact
operator|+
literal|"> to path:"
argument_list|,
name|path
argument_list|,
name|toPath
argument_list|(
name|expectedArtifact
argument_list|)
argument_list|)
expr_stmt|;
comment|// --- Artifact Reference Tests
comment|// Path to Artifact Reference.
name|ArtifactReference
name|testReference
init|=
name|toArtifactReference
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
comment|// And back again, using test Reference from previous step.
name|assertEquals
argument_list|(
literal|"Artifact<"
operator|+
name|expectedArtifact
operator|+
literal|"> to path:"
argument_list|,
name|path
argument_list|,
name|toPath
argument_list|(
name|testReference
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertArtifactReference
parameter_list|(
name|ArtifactReference
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
name|getGroupId
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
literal|" - classifier"
argument_list|,
name|classifier
argument_list|,
name|actualReference
operator|.
name|getClassifier
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
specifier|protected
name|ArtifactReference
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
name|ArtifactReference
name|artifact
init|=
operator|new
name|ArtifactReference
argument_list|()
decl_stmt|;
name|artifact
operator|.
name|setGroupId
argument_list|(
name|groupId
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setArtifactId
argument_list|(
name|artifactId
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setVersion
argument_list|(
name|version
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setClassifier
argument_list|(
name|classifier
argument_list|)
expr_stmt|;
name|artifact
operator|.
name|setType
argument_list|(
name|type
argument_list|)
expr_stmt|;
name|assertNotNull
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
return|return
name|artifact
return|;
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
name|toArtifactReference
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
specifier|protected
specifier|abstract
name|ArtifactReference
name|toArtifactReference
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|LayoutException
function_decl|;
specifier|protected
specifier|abstract
name|String
name|toPath
parameter_list|(
name|ArtifactReference
name|reference
parameter_list|)
function_decl|;
block|}
end_class

end_unit

