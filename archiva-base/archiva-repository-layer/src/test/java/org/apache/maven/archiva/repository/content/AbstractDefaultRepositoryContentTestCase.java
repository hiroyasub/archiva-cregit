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

begin_comment
comment|/**  * AbstractDefaultRepositoryContentTestCase   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractDefaultRepositoryContentTestCase
extends|extends
name|AbstractRepositoryLayerTestCase
block|{
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
comment|/**       * [MRM-432] Oddball version spec.      * Example of an oddball / unusual version spec.      * @throws LayoutException       */
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**       * [MRM-432] Oddball version spec.      * Example of an oddball / unusual version spec.      * @throws LayoutException       */
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the ejb-client type spec.      * Type specs are not a 1 to 1 map to the extension.       * This tests that effect.      * @throws LayoutException       */
comment|/* TODO: Re-enabled in the future.      public void testGoodFooEjbClient()         throws LayoutException     {         String groupId = "com.foo";         String artifactId = "foo-client";         String version = "1.0";         String classifier = null;         String type = "ejb-client"; // oddball type-spec (should result in jar extension)         String path = "com/foo/foo-client/1.0/foo-client-1.0.jar";          assertLayout( path, groupId, artifactId, version, classifier, type );     }     */
comment|/**       * [MRM-432] Oddball version spec.      * Example of an oddball / unusual version spec.      * @throws LayoutException       */
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
comment|/*           * The version id of "public_review" can cause problems. is it part of          * the version spec? or the classifier?          * Since the path spec below shows it in the path, then it is really          * part of the version spec.           */
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-486] Can not deploy artifact test.maven-arch:test-arch due to "No ArtifactID Detected"      */
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * It may seem odd, but this is a valid artifact.      */
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * It may seem odd, but this is a valid artifact.      */
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * Test the classifier, and java-source type spec.      * @throws LayoutException       */
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * A timestamped versioned artifact, should reside in a SNAPSHOT baseversion directory.      * @throws LayoutException       */
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-519] version identifiers within filename cause misidentification of version.      * Example uses "test" in artifact Id, which is also part of the versionKeyword list.      */
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
name|classifier
argument_list|,
name|type
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToArtifactOnEmptyPath
parameter_list|()
block|{
try|try
block|{
name|toArtifactReference
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
specifier|public
name|void
name|testToArtifactOnNullPath
parameter_list|()
block|{
try|try
block|{
name|toArtifactReference
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
specifier|public
name|void
name|testToArtifactReferenceOnEmptyPath
parameter_list|()
block|{
try|try
block|{
name|toArtifactReference
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
specifier|public
name|void
name|testToArtifactReferenceOnNullPath
parameter_list|()
block|{
try|try
block|{
name|toArtifactReference
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
specifier|public
name|void
name|testToPathOnNullArtifactReference
parameter_list|()
block|{
try|try
block|{
name|ArtifactReference
name|reference
init|=
literal|null
decl_stmt|;
name|toPath
argument_list|(
name|reference
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have failed due to null artifact reference."
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
comment|/**      * Perform a roundtrip through the layout routines to determine success.      */
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

