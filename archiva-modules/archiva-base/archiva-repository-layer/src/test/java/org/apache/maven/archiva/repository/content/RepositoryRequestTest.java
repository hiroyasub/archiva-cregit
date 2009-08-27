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
name|ManagedRepositoryContent
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
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * RepositoryRequestTest  *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryRequestTest
extends|extends
name|AbstractRepositoryLayerTestCase
block|{
specifier|public
name|void
name|testInvalidRequestNoArtifactId
parameter_list|()
block|{
name|assertInvalidRequest
argument_list|(
literal|"groupId/jars/-1.0.jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidLegacyRequestBadLocation
parameter_list|()
block|{
name|assertInvalidRequest
argument_list|(
literal|"org.apache.maven.test/jars/artifactId-1.0.war"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidRequestTooShort
parameter_list|()
block|{
name|assertInvalidRequest
argument_list|(
literal|"org.apache.maven.test/artifactId-2.0.jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testInvalidDefaultRequestBadLocation
parameter_list|()
block|{
name|assertInvalidRequest
argument_list|(
literal|"invalid/invalid/1.0-20050611.123456-1/invalid-1.0-20050611.123456-1.jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyGanymed
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"ch.ethz.ganymed/jars/ganymed-ssh2-build210.jar"
argument_list|,
literal|"ch.ethz.ganymed"
argument_list|,
literal|"ganymed-ssh2"
argument_list|,
literal|"build210"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultGanymed
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"ch/ethz/ganymed/ganymed-ssh2/build210/ganymed-ssh2-build210.jar"
argument_list|,
literal|"ch.ethz.ganymed"
argument_list|,
literal|"ganymed-ssh2"
argument_list|,
literal|"build210"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyJavaxComm
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"javax/jars/comm-3.0-u1.jar"
argument_list|,
literal|"javax"
argument_list|,
literal|"comm"
argument_list|,
literal|"3.0-u1"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultJavaxComm
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"javax/comm/3.0-u1/comm-3.0-u1.jar"
argument_list|,
literal|"javax"
argument_list|,
literal|"comm"
argument_list|,
literal|"3.0-u1"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyJavaxPersistence
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"javax.persistence/jars/ejb-3.0-public_review.jar"
argument_list|,
literal|"javax.persistence"
argument_list|,
literal|"ejb"
argument_list|,
literal|"3.0-public_review"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultJavaxPersistence
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"javax/persistence/ejb/3.0-public_review/ejb-3.0-public_review.jar"
argument_list|,
literal|"javax.persistence"
argument_list|,
literal|"ejb"
argument_list|,
literal|"3.0-public_review"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyMavenTestPlugin
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"maven/jars/maven-test-plugin-1.8.2.jar"
argument_list|,
literal|"maven"
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
block|}
specifier|public
name|void
name|testValidDefaultMavenTestPlugin
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"maven/maven-test-plugin/1.8.2/maven-test-plugin-1.8.2.pom"
argument_list|,
literal|"maven"
argument_list|,
literal|"maven-test-plugin"
argument_list|,
literal|"1.8.2"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyCommonsLangJavadoc
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"commons-lang/javadoc.jars/commons-lang-2.1-javadoc.jar"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.1"
argument_list|,
literal|"javadoc"
argument_list|,
literal|"javadoc"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultCommonsLangJavadoc
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"commons-lang/commons-lang/2.1/commons-lang-2.1-javadoc.jar"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"commons-lang"
argument_list|,
literal|"2.1"
argument_list|,
literal|"javadoc"
argument_list|,
literal|"javadoc"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyDerbyPom
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"org.apache.derby/poms/derby-10.2.2.0.pom"
argument_list|,
literal|"org.apache.derby"
argument_list|,
literal|"derby"
argument_list|,
literal|"10.2.2.0"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
comment|// Starting slash should not prevent detection.
name|assertValid
argument_list|(
literal|"/org.apache.derby/poms/derby-10.2.2.0.pom"
argument_list|,
literal|"org.apache.derby"
argument_list|,
literal|"derby"
argument_list|,
literal|"10.2.2.0"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultDerbyPom
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0.pom"
argument_list|,
literal|"org.apache.derby"
argument_list|,
literal|"derby"
argument_list|,
literal|"10.2.2.0"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyGeronimoEjbSpec
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"org.apache.geronimo.specs/jars/geronimo-ejb_2.1_spec-1.0.1.jar"
argument_list|,
literal|"org.apache.geronimo.specs"
argument_list|,
literal|"geronimo-ejb_2.1_spec"
argument_list|,
literal|"1.0.1"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultGeronimoEjbSpec
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"org/apache/geronimo/specs/geronimo-ejb_2.1_spec/1.0.1/geronimo-ejb_2.1_spec-1.0.1.jar"
argument_list|,
literal|"org.apache.geronimo.specs"
argument_list|,
literal|"geronimo-ejb_2.1_spec"
argument_list|,
literal|"1.0.1"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyLdapSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"directory-clients/poms/ldap-clients-0.9.1-SNAPSHOT.pom"
argument_list|,
literal|"directory-clients"
argument_list|,
literal|"ldap-clients"
argument_list|,
literal|"0.9.1-SNAPSHOT"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultLdapSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"directory-clients/ldap-clients/0.9.1-SNAPSHOT/ldap-clients-0.9.1-SNAPSHOT.pom"
argument_list|,
literal|"directory-clients"
argument_list|,
literal|"ldap-clients"
argument_list|,
literal|"0.9.1-SNAPSHOT"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyTestArchSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"test.maven-arch/poms/test-arch-2.0.3-SNAPSHOT.pom"
argument_list|,
literal|"test.maven-arch"
argument_list|,
literal|"test-arch"
argument_list|,
literal|"2.0.3-SNAPSHOT"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultTestArchSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"test/maven-arch/test-arch/2.0.3-SNAPSHOT/test-arch-2.0.3-SNAPSHOT.pom"
argument_list|,
literal|"test.maven-arch"
argument_list|,
literal|"test-arch"
argument_list|,
literal|"2.0.3-SNAPSHOT"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyOddDottedArtifactId
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"com.company.department/poms/com.company.department.project-0.2.pom"
argument_list|,
literal|"com.company.department"
argument_list|,
literal|"com.company.department.project"
argument_list|,
literal|"0.2"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultOddDottedArtifactId
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"com/company/department/com.company.department.project/0.2/com.company.department.project-0.2.pom"
argument_list|,
literal|"com.company.department"
argument_list|,
literal|"com.company.department.project"
argument_list|,
literal|"0.2"
argument_list|,
literal|null
argument_list|,
literal|"pom"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidLegacyTimestampedSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"org.apache.archiva.test/jars/redonkulous-3.1-beta-1-20050831.101112-42.jar"
argument_list|,
literal|"org.apache.archiva.test"
argument_list|,
literal|"redonkulous"
argument_list|,
literal|"3.1-beta-1-20050831.101112-42"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testValidDefaultTimestampedSnapshot
parameter_list|()
throws|throws
name|Exception
block|{
name|assertValid
argument_list|(
literal|"org/apache/archiva/test/redonkulous/3.1-beta-1-SNAPSHOT/redonkulous-3.1-beta-1-20050831.101112-42.jar"
argument_list|,
literal|"org.apache.archiva.test"
argument_list|,
literal|"redonkulous"
argument_list|,
literal|"3.1-beta-1-20050831.101112-42"
argument_list|,
literal|null
argument_list|,
literal|"jar"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsSupportFile
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.sha1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.md5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.asc"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.pgp"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml.sha1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml.md5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"test.maven-arch/poms/test-arch-2.0.3-SNAPSHOT.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"test/maven-arch/test-arch/2.0.3-SNAPSHOT/test-arch-2.0.3-SNAPSHOT.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/archiva/archiva-api/1.0/archiva-api-1.0.xml.zip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isSupportFile
argument_list|(
literal|"org/apache/derby/derby/maven-metadata.xml"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsMetadata
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isMetadata
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isMetadata
argument_list|(
literal|"org/apache/derby/derby/maven-metadata.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isMetadata
argument_list|(
literal|"test.maven-arch/poms/test-arch-2.0.3-SNAPSHOT.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isMetadata
argument_list|(
literal|"test/maven-arch/test-arch/2.0.3-SNAPSHOT/test-arch-2.0.3-SNAPSHOT.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isMetadata
argument_list|(
literal|"org/apache/archiva/archiva-api/1.0/archiva-api-1.0.xml.zip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isMetadata
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isMetadata
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.pgp"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isMetadata
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml.sha1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsDefault
parameter_list|()
block|{
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"test.maven-arch/poms/test-arch-2.0.3-SNAPSHOT.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"directory-clients/poms/ldap-clients-0.9.1-SNAPSHOT.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"commons-lang/jars/commons-lang-2.1-javadoc.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"test/maven-arch/test-arch/2.0.3-SNAPSHOT/test-arch-2.0.3-SNAPSHOT.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"org/apache/archiva/archiva-api/1.0/archiva-api-1.0.xml.zip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.pgp"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml.sha1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"eclipse/jdtcore/maven-metadata.xml"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"eclipse/jdtcore/maven-metadata.xml.sha1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"eclipse/jdtcore/maven-metadata.xml.md5"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"foo"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isDefault
argument_list|(
literal|"some.short/path"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsLegacy
parameter_list|()
block|{
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|"test.maven-arch/poms/test-arch-2.0.3-SNAPSHOT.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|"directory-clients/poms/ldap-clients-0.9.1-SNAPSHOT.pom"
argument_list|)
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|"commons-lang/jars/commons-lang-2.1-javadoc.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|"test/maven-arch/test-arch/2.0.3-SNAPSHOT/test-arch-2.0.3-SNAPSHOT.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|"org/apache/archiva/archiva-api/1.0/archiva-api-1.0.xml.zip"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0-bin.tar.gz.pgp"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml.sha1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|""
argument_list|)
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|repoRequest
operator|.
name|isLegacy
argument_list|(
literal|"some.short/path"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|ManagedRepositoryContent
name|createManagedRepo
parameter_list|(
name|String
name|layout
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|repoRoot
init|=
name|getTestFile
argument_list|(
literal|"target/test-repo"
argument_list|)
decl_stmt|;
return|return
name|createManagedRepositoryContent
argument_list|(
literal|"test-internal"
argument_list|,
literal|"Internal Test Repo"
argument_list|,
name|repoRoot
argument_list|,
name|layout
argument_list|)
return|;
block|}
comment|/**      * [MRM-481] Artifact requests with a .xml.zip extension fail with a 404 Error      */
specifier|public
name|void
name|testToNativePathArtifactDefaultToDefaultDualExtension
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test (artifact) default to default - dual extension
name|assertEquals
argument_list|(
literal|"org/project/example-presentation/3.2/example-presentation-3.2.xml.zip"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org/project/example-presentation/3.2/example-presentation-3.2.xml.zip"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-481] Artifact requests with a .xml.zip extension fail with a 404 Error      */
specifier|public
name|void
name|testToNativePathArtifactLegacyToDefaultDualExtension
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test (artifact) legacy to default - dual extension
comment|// NOTE: The detection of a dual extension is flawed.
name|assertEquals
argument_list|(
literal|"org/project/example-presentation/3.2.xml/example-presentation-3.2.xml.zip"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org.project/zips/example-presentation-3.2.xml.zip"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testToNativePathMetadataDefaultToDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test (metadata) default to default
name|assertEquals
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml.sha1"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml.sha1"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNativePathPomLegacyToDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test (pom) legacy to default
name|assertEquals
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0.pom"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org.apache.derby/poms/derby-10.2.2.0.pom"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNativePathPomLegacyToLegacy
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"legacy"
argument_list|)
decl_stmt|;
comment|// Test (pom) legacy to default
name|assertEquals
argument_list|(
literal|"org.apache.derby/poms/derby-10.2.2.0.pom"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org.apache.derby/poms/derby-10.2.2.0.pom"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNativePathPomLegacyToDefaultEjb
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test (pom) legacy to default
name|assertEquals
argument_list|(
literal|"mygroup/myejb/1.0/myejb-1.0.jar"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"mygroup/ejbs/myejb-1.0.jar"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNativePathPomLegacyToLegacyEjb
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"legacy"
argument_list|)
decl_stmt|;
comment|// Test (pom) legacy to default
name|assertEquals
argument_list|(
literal|"mygroup/ejbs/myejb-1.0.jar"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"mygroup/ejbs/myejb-1.0.jar"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNativePathPomLegacyToLegacyStrutsModule
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"legacy"
argument_list|)
decl_stmt|;
comment|// Test (pom) legacy to default
name|assertEquals
argument_list|(
literal|"WebPortal/struts-modules/eventsDB-1.2.3.struts-module"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"WebPortal/struts-modules/eventsDB-1.2.3.struts-module"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNativePathSupportFileLegacyToDefault
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test (supportfile) legacy to default
name|assertEquals
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0.jar.sha1"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org.apache.derby/jars/derby-10.2.2.0.jar.sha1"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNativePathBadRequestTooShort
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test bad request path (too short)
try|try
block|{
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org.apache.derby/license.txt"
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception about a too short path."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|// expected path.
block|}
block|}
specifier|public
name|void
name|testNativePathBadRequestBlank
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test bad request path (too short)
try|try
block|{
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|""
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception about an blank request."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|// expected path.
block|}
block|}
specifier|public
name|void
name|testNativePathBadRequestNull
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test bad request path (too short)
try|try
block|{
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|null
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception about an null request."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|// expected path.
block|}
block|}
specifier|public
name|void
name|testNativePathBadRequestUnknownType
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"default"
argument_list|)
decl_stmt|;
comment|// Test bad request path (too short)
try|try
block|{
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/license.txt"
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown an exception about an invalid type."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|// expected path.
block|}
block|}
specifier|public
name|void
name|testToNativePathLegacyMetadataDefaultToLegacy
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"legacy"
argument_list|)
decl_stmt|;
comment|// Test (metadata) default to legacy
comment|// Special Case: This direction is not supported, should throw a LayoutException.
try|try
block|{
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/maven-metadata.xml"
argument_list|,
name|repository
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Should have thrown a LayoutException, can't translate a maven-metadata.xml to a legacy layout."
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|LayoutException
name|e
parameter_list|)
block|{
comment|// expected path.
block|}
block|}
specifier|public
name|void
name|testNativePathPomDefaultToLegacy
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"legacy"
argument_list|)
decl_stmt|;
comment|// Test (pom) default to legacy
name|assertEquals
argument_list|(
literal|"org.apache.derby/poms/derby-10.2.2.0.pom"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0.pom"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testNativePathSupportFileDefaultToLegacy
parameter_list|()
throws|throws
name|Exception
block|{
name|ManagedRepositoryContent
name|repository
init|=
name|createManagedRepo
argument_list|(
literal|"legacy"
argument_list|)
decl_stmt|;
comment|// Test (supportfile) default to legacy
name|assertEquals
argument_list|(
literal|"org.apache.derby/jars/derby-10.2.2.0.jar.sha1"
argument_list|,
name|repoRequest
operator|.
name|toNativePath
argument_list|(
literal|"org/apache/derby/derby/10.2.2.0/derby-10.2.2.0.jar.sha1"
argument_list|,
name|repository
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertValid
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
name|Exception
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
operator|(
name|classifier
operator|!=
literal|null
condition|?
name|classifier
operator|+
literal|":"
else|:
literal|""
operator|)
operator|+
name|type
decl_stmt|;
name|ArtifactReference
name|reference
init|=
name|repoRequest
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
name|expectedId
operator|+
literal|" - Should not be null."
argument_list|,
name|reference
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
name|reference
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
name|reference
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
name|reference
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
name|reference
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
name|reference
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertInvalidRequest
parameter_list|(
name|String
name|path
parameter_list|)
block|{
try|try
block|{
name|repoRequest
operator|.
name|toArtifactReference
argument_list|(
name|path
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected a LayoutException on an invalid path ["
operator|+
name|path
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
specifier|private
name|RepositoryRequest
name|repoRequest
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
name|repoRequest
operator|=
operator|(
name|RepositoryRequest
operator|)
name|lookup
argument_list|(
name|RepositoryRequest
operator|.
name|class
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

