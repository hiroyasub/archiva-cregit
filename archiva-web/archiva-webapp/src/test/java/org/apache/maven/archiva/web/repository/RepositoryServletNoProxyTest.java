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
name|web
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|GetMethodWebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebRequest
import|;
end_import

begin_import
import|import
name|com
operator|.
name|meterware
operator|.
name|httpunit
operator|.
name|WebResponse
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
name|io
operator|.
name|FileUtils
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
comment|/**  * RepositoryServletTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryServletNoProxyTest
extends|extends
name|AbstractRepositoryServletTestCase
block|{
specifier|public
name|void
name|testGetNoProxyChecksumDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangSha1
init|=
literal|"commons-lang/commons-lang/2.1/commons-lang-2.1.jar.sha1"
decl_stmt|;
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangSha1
argument_list|)
decl_stmt|;
name|checksumFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|checksumFile
argument_list|,
literal|"dummy-checksum"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
name|commonsLangSha1
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
literal|"dummy-checksum"
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetNoProxyChecksumLegacyLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangSha1
init|=
literal|"commons-lang/commons-lang/2.1/commons-lang-2.1.jar.sha1"
decl_stmt|;
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangSha1
argument_list|)
decl_stmt|;
name|checksumFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|checksumFile
argument_list|,
literal|"dummy-checksum"
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
literal|"commons-lang/jars/commons-lang-2.1.jar.sha1"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
literal|"dummy-checksum"
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetNoProxyVersionedMetadataDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangMetadata
init|=
literal|"commons-lang/commons-lang/2.1/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadataContents
init|=
literal|"dummy-versioned-metadata"
decl_stmt|;
name|File
name|metadataFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|metadataFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|metadataFile
argument_list|,
name|expectedMetadataContents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedMetadataContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetNoProxyProjectMetadataDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangMetadata
init|=
literal|"commons-lang/commons-lang/maven-metadata.xml"
decl_stmt|;
name|String
name|expectedMetadataContents
init|=
literal|"dummy-project-metadata"
decl_stmt|;
name|File
name|metadataFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|metadataFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|metadataFile
argument_list|,
name|expectedMetadataContents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
name|commonsLangMetadata
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedMetadataContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetNoProxyArtifactDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangJar
init|=
literal|"commons-lang/commons-lang/2.1/commons-lang-2.1.jar"
decl_stmt|;
name|String
name|expectedArtifactContents
init|=
literal|"dummy-commons-lang-artifact"
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangJar
argument_list|)
decl_stmt|;
name|artifactFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|artifactFile
argument_list|,
name|expectedArtifactContents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
name|commonsLangJar
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedArtifactContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetNoProxyArtifactLegacyLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangJar
init|=
literal|"commons-lang/commons-lang/2.1/commons-lang-2.1.jar"
decl_stmt|;
name|String
name|expectedArtifactContents
init|=
literal|"dummy-commons-lang-artifact"
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangJar
argument_list|)
decl_stmt|;
name|artifactFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|artifactFile
argument_list|,
name|expectedArtifactContents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
literal|"commons-lang/jars/commons-lang-2.1.jar"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedArtifactContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetNoProxySnapshotArtifactDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangJar
init|=
literal|"commons-lang/commons-lang/2.1-SNAPSHOT/commons-lang-2.1-SNAPSHOT.jar"
decl_stmt|;
name|String
name|expectedArtifactContents
init|=
literal|"dummy-commons-lang-snapshot-artifact"
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangJar
argument_list|)
decl_stmt|;
name|artifactFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|artifactFile
argument_list|,
name|expectedArtifactContents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
name|commonsLangJar
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedArtifactContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetNoProxySnapshotArtifactLegacyLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangJar
init|=
literal|"commons-lang/commons-lang/2.1-SNAPSHOT/commons-lang-2.1-SNAPSHOT.jar"
decl_stmt|;
name|String
name|expectedArtifactContents
init|=
literal|"dummy-commons-lang-snapshot-artifact"
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangJar
argument_list|)
decl_stmt|;
name|artifactFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|artifactFile
argument_list|,
name|expectedArtifactContents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
literal|"commons-lang/jars/commons-lang-2.1-SNAPSHOT.jar"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedArtifactContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetNoProxyTimestampedSnapshotArtifactDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangJar
init|=
literal|"commons-lang/commons-lang/2.1-SNAPSHOT/commons-lang-2.1-20050821.023400-1.jar"
decl_stmt|;
name|String
name|expectedArtifactContents
init|=
literal|"dummy-commons-lang-snapshot-artifact"
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangJar
argument_list|)
decl_stmt|;
name|artifactFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|artifactFile
argument_list|,
name|expectedArtifactContents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
name|commonsLangJar
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedArtifactContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetNoProxyTimestampedSnapshotArtifactLegacyLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|commonsLangJar
init|=
literal|"commons-lang/commons-lang/2.1-SNAPSHOT/commons-lang-2.1-20050821.023400-1.jar"
decl_stmt|;
name|String
name|expectedArtifactContents
init|=
literal|"dummy-commons-lang-snapshot-artifact"
decl_stmt|;
name|File
name|artifactFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|commonsLangJar
argument_list|)
decl_stmt|;
name|artifactFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|artifactFile
argument_list|,
name|expectedArtifactContents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
literal|"commons-lang/jars/commons-lang-2.1-20050821.023400-1.jar"
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedArtifactContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
comment|/**      * [MRM-481] Artifact requests with a .xml.zip extension fail with a 404 Error      */
specifier|public
name|void
name|testGetNoProxyDualExtensionDefaultLayout
parameter_list|()
throws|throws
name|Exception
block|{
name|String
name|expectedContents
init|=
literal|"the-contents-of-the-dual-extension"
decl_stmt|;
name|String
name|dualExtensionPath
init|=
literal|"org/project/example-presentation/3.2/example-presentation-3.2.xml.zip"
decl_stmt|;
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|repoRootInternal
argument_list|,
name|dualExtensionPath
argument_list|)
decl_stmt|;
name|checksumFile
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|checksumFile
argument_list|,
name|expectedContents
argument_list|,
literal|null
argument_list|)
expr_stmt|;
name|WebRequest
name|request
init|=
operator|new
name|GetMethodWebRequest
argument_list|(
literal|"http://machine.com/repository/internal/"
operator|+
name|dualExtensionPath
argument_list|)
decl_stmt|;
name|WebResponse
name|response
init|=
name|sc
operator|.
name|getResponse
argument_list|(
name|request
argument_list|)
decl_stmt|;
name|assertResponseOK
argument_list|(
name|response
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected file contents"
argument_list|,
name|expectedContents
argument_list|,
name|response
operator|.
name|getText
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

