begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|checksum
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * ChecksummedFileTest  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ChecksummedFileTest
extends|extends
name|AbstractChecksumTestCase
block|{
comment|/**  SHA1 checksum from www.ibiblio.org/maven2, incuding file path */
specifier|private
specifier|static
specifier|final
name|String
name|SERVLETAPI_SHA1
init|=
literal|"bcc82975c0f9c681fcb01cc38504c992553e93ba"
decl_stmt|;
specifier|private
name|File
name|createTestableJar
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|srcFile
init|=
name|getTestResource
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|getTestOutputDir
argument_list|()
argument_list|,
name|srcFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|srcFile
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
return|return
name|destFile
return|;
block|}
specifier|private
name|File
name|createTestableJar
parameter_list|(
name|String
name|filename
parameter_list|,
name|boolean
name|copySha1
parameter_list|,
name|boolean
name|copyMd5
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|srcFile
init|=
name|getTestResource
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|File
name|jarFile
init|=
operator|new
name|File
argument_list|(
name|getTestOutputDir
argument_list|()
argument_list|,
name|srcFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|srcFile
argument_list|,
name|jarFile
argument_list|)
expr_stmt|;
if|if
condition|(
name|copySha1
condition|)
block|{
name|File
name|srcSha1
init|=
operator|new
name|File
argument_list|(
name|srcFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
name|File
name|sha1File
init|=
operator|new
name|File
argument_list|(
name|jarFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|srcSha1
argument_list|,
name|sha1File
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|copyMd5
condition|)
block|{
name|File
name|srcMd5
init|=
operator|new
name|File
argument_list|(
name|srcFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
name|File
name|md5File
init|=
operator|new
name|File
argument_list|(
name|jarFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|srcMd5
argument_list|,
name|md5File
argument_list|)
expr_stmt|;
block|}
return|return
name|jarFile
return|;
block|}
specifier|public
name|void
name|testCalculateChecksumMd5
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|testfile
init|=
name|getTestResource
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|testfile
argument_list|)
decl_stmt|;
name|String
name|expectedChecksum
init|=
literal|"f42047fe2e177ac04d0df7aa44d408be"
decl_stmt|;
name|String
name|actualChecksum
init|=
name|checksummedFile
operator|.
name|calculateChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedChecksum
argument_list|,
name|actualChecksum
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCalculateChecksumSha1
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|testfile
init|=
name|getTestResource
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|testfile
argument_list|)
decl_stmt|;
name|String
name|expectedChecksum
init|=
literal|"2bb14b388973351b0a4dfe11d171965f59cc61a1"
decl_stmt|;
name|String
name|actualChecksum
init|=
name|checksummedFile
operator|.
name|calculateChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|expectedChecksum
argument_list|,
name|actualChecksum
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCreateChecksum
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|testableJar
init|=
name|createTestableJar
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|testableJar
argument_list|)
decl_stmt|;
name|checksummedFile
operator|.
name|createChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
expr_stmt|;
name|File
name|hashFile
init|=
name|checksummedFile
operator|.
name|getChecksumFile
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"ChecksumAlgorithm file should exist."
argument_list|,
name|hashFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|hashContents
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|hashFile
argument_list|)
decl_stmt|;
name|hashContents
operator|=
name|StringUtils
operator|.
name|trim
argument_list|(
name|hashContents
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"2bb14b388973351b0a4dfe11d171965f59cc61a1  redback-authz-open.jar"
argument_list|,
name|hashContents
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testFixChecksum
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|jarFile
init|=
name|createTestableJar
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|)
decl_stmt|;
name|File
name|sha1File
init|=
operator|new
name|File
argument_list|(
name|jarFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
comment|// A typical scenario seen in the wild.
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|sha1File
argument_list|,
literal|"sha1sum: redback-authz-open.jar: No such file or directory"
argument_list|)
expr_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|jarFile
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"ChecksummedFile.isValid(SHA1) == false"
argument_list|,
name|checksummedFile
operator|.
name|isValidChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
argument_list|)
expr_stmt|;
name|boolean
name|fixed
init|=
name|checksummedFile
operator|.
name|fixChecksums
argument_list|(
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|ChecksumAlgorithm
operator|.
name|SHA1
block|}
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"ChecksummedFile.fixChecksums() == true"
argument_list|,
name|fixed
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"ChecksummedFile.isValid(SHA1) == true"
argument_list|,
name|checksummedFile
operator|.
name|isValidChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetChecksumFile
parameter_list|()
block|{
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
operator|new
name|File
argument_list|(
literal|"test.jar"
argument_list|)
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"test.jar.sha1"
argument_list|,
name|checksummedFile
operator|.
name|getChecksumFile
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsValidChecksum
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|jarFile
init|=
name|createTestableJar
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|jarFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"ChecksummedFile.isValid(SHA1)"
argument_list|,
name|checksummedFile
operator|.
name|isValidChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsValidChecksumInvalidSha1Format
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|jarFile
init|=
name|createTestableJar
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|)
decl_stmt|;
name|File
name|sha1File
init|=
operator|new
name|File
argument_list|(
name|jarFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
comment|// A typical scenario seen in the wild.
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|sha1File
argument_list|,
literal|"sha1sum: redback-authz-open.jar: No such file or directory"
argument_list|)
expr_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|jarFile
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"ChecksummedFile.isValid(SHA1)"
argument_list|,
name|checksummedFile
operator|.
name|isValidChecksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsValidChecksumNoChecksumFiles
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|jarFile
init|=
name|createTestableJar
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|jarFile
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
literal|"ChecksummedFile.isValid(SHA1,MD5)"
argument_list|,
name|checksummedFile
operator|.
name|isValidChecksums
argument_list|(
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|ChecksumAlgorithm
operator|.
name|SHA1
block|,
name|ChecksumAlgorithm
operator|.
name|MD5
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsValidChecksumSha1AndMd5
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|jarFile
init|=
name|createTestableJar
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|,
literal|true
argument_list|,
literal|true
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|jarFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"ChecksummedFile.isValid(SHA1,MD5)"
argument_list|,
name|checksummedFile
operator|.
name|isValidChecksums
argument_list|(
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|ChecksumAlgorithm
operator|.
name|SHA1
block|,
name|ChecksumAlgorithm
operator|.
name|MD5
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testIsValidChecksumSha1NoMd5
parameter_list|()
throws|throws
name|IOException
block|{
name|File
name|jarFile
init|=
name|createTestableJar
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|jarFile
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"ChecksummedFile.isValid(SHA1)"
argument_list|,
name|checksummedFile
operator|.
name|isValidChecksums
argument_list|(
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|ChecksumAlgorithm
operator|.
name|SHA1
block|,
name|ChecksumAlgorithm
operator|.
name|MD5
block|}
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testParseChecksum
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|expected
init|=
name|SERVLETAPI_SHA1
operator|+
literal|"  /home/projects/maven/repository-staging/to-ibiblio/maven2/servletapi/servletapi/2.4/servletapi-2.4.pom"
decl_stmt|;
name|File
name|testfile
init|=
name|getTestResource
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|testfile
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|checksummedFile
operator|.
name|parseChecksum
argument_list|(
name|expected
argument_list|,
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|,
literal|"servletapi/servletapi/2.4/servletapi-2.4.pom"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum doesn't match"
argument_list|,
name|SERVLETAPI_SHA1
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testParseChecksumAltDash1
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|expected
init|=
name|SERVLETAPI_SHA1
operator|+
literal|"  -"
decl_stmt|;
name|File
name|testfile
init|=
name|getTestResource
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|testfile
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|checksummedFile
operator|.
name|parseChecksum
argument_list|(
name|expected
argument_list|,
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|,
literal|"servletapi/servletapi/2.4/servletapi-2.4.pom"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum doesn't match"
argument_list|,
name|SERVLETAPI_SHA1
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testParseChecksumAltDash2
parameter_list|()
throws|throws
name|IOException
block|{
name|String
name|expected
init|=
literal|"SHA1(-)="
operator|+
name|SERVLETAPI_SHA1
decl_stmt|;
name|File
name|testfile
init|=
name|getTestResource
argument_list|(
literal|"examples/redback-authz-open.jar"
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|checksummedFile
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|testfile
argument_list|)
decl_stmt|;
name|String
name|s
init|=
name|checksummedFile
operator|.
name|parseChecksum
argument_list|(
name|expected
argument_list|,
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|,
literal|"servletapi/servletapi/2.4/servletapi-2.4.pom"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum doesn't match"
argument_list|,
name|SERVLETAPI_SHA1
argument_list|,
name|s
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

