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
name|policies
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
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtil
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
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
name|rules
operator|.
name|TestName
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
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|io
operator|.
name|FileReader
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
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
name|javax
operator|.
name|inject
operator|.
name|Named
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
comment|/**  * ChecksumPolicyTest  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|value
operator|=
name|SpringJUnit4ClassRunner
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
block|,
literal|"classpath*:/spring-context.xml"
block|}
argument_list|)
specifier|public
class|class
name|ChecksumPolicyTest
block|{
specifier|private
specifier|static
specifier|final
name|String
name|GOOD
init|=
literal|"good"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BAD
init|=
literal|"bad"
decl_stmt|;
annotation|@
name|Inject
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"postDownloadPolicy#checksum"
argument_list|)
name|PostDownloadPolicy
name|downloadPolicy
decl_stmt|;
annotation|@
name|Rule
specifier|public
name|TestName
name|name
init|=
operator|new
name|TestName
argument_list|()
decl_stmt|;
specifier|private
name|PostDownloadPolicy
name|lookupPolicy
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|downloadPolicy
return|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailOnFileOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailSetting
argument_list|(
literal|false
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailOnFileWithBadMd5AndBadSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailSetting
argument_list|(
literal|false
argument_list|,
name|BAD
argument_list|,
name|BAD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailOnFileWithBadMd5AndGoodSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailSetting
argument_list|(
literal|false
argument_list|,
name|BAD
argument_list|,
name|GOOD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailOnFileWithBadMd5Only
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailSetting
argument_list|(
literal|false
argument_list|,
name|BAD
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailOnFileWithBadSha1Only
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailSetting
argument_list|(
literal|false
argument_list|,
literal|null
argument_list|,
name|BAD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailOnFileWithGoodMd5AndBadSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailSetting
argument_list|(
literal|false
argument_list|,
name|GOOD
argument_list|,
name|BAD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailOnFileWithGoodMd5AndGoodSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailSetting
argument_list|(
literal|true
argument_list|,
name|GOOD
argument_list|,
name|GOOD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailOnFileWithGoodMd5Only
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailSetting
argument_list|(
literal|true
argument_list|,
name|GOOD
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFailOnFileWithGoodSha1Only
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFailSetting
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
name|GOOD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixOnFileOnly
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFixSetting
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixOnFileWithBadMd5AndBadSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFixSetting
argument_list|(
literal|true
argument_list|,
name|BAD
argument_list|,
name|BAD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixOnFileWithBadMd5AndGoodSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFixSetting
argument_list|(
literal|true
argument_list|,
name|BAD
argument_list|,
name|GOOD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixOnFileWithBadMd5Only
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFixSetting
argument_list|(
literal|true
argument_list|,
name|BAD
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixOnFileWithBadSha1Only
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFixSetting
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
name|BAD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixOnFileWithGoodMd5AndBadSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFixSetting
argument_list|(
literal|true
argument_list|,
name|GOOD
argument_list|,
name|BAD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixOnFileWithGoodMd5AndGoodSha1
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFixSetting
argument_list|(
literal|true
argument_list|,
name|GOOD
argument_list|,
name|GOOD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixOnFileWithGoodMd5Only
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFixSetting
argument_list|(
literal|true
argument_list|,
name|GOOD
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testFixOnFileWithGoodSha1Only
parameter_list|()
throws|throws
name|Exception
block|{
name|assertFixSetting
argument_list|(
literal|true
argument_list|,
literal|null
argument_list|,
name|GOOD
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|testIgnore
parameter_list|()
throws|throws
name|Exception
block|{
name|PostDownloadPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|File
name|localFile
init|=
name|createTestableFiles
argument_list|(
literal|null
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Properties
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
name|policy
operator|.
name|applyPolicy
argument_list|(
name|ChecksumPolicy
operator|.
name|IGNORE
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertFailSetting
parameter_list|(
name|boolean
name|expectedResult
parameter_list|,
name|String
name|md5State
parameter_list|,
name|String
name|sha1State
parameter_list|)
throws|throws
name|Exception
block|{
name|PostDownloadPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|File
name|localFile
init|=
name|createTestableFiles
argument_list|(
name|md5State
argument_list|,
name|sha1State
argument_list|)
decl_stmt|;
name|Properties
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
name|boolean
name|actualResult
decl_stmt|;
try|try
block|{
name|policy
operator|.
name|applyPolicy
argument_list|(
name|ChecksumPolicy
operator|.
name|FAIL
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|)
expr_stmt|;
name|actualResult
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyViolationException
name|e
parameter_list|)
block|{
name|actualResult
operator|=
literal|false
expr_stmt|;
name|String
name|msg
init|=
name|createMessage
argument_list|(
name|ChecksumPolicy
operator|.
name|FAIL
argument_list|,
name|md5State
argument_list|,
name|sha1State
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|msg
operator|+
literal|" local file should not exist:"
argument_list|,
name|localFile
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|md5File
init|=
operator|new
name|File
argument_list|(
name|localFile
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
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
name|assertFalse
argument_list|(
name|msg
operator|+
literal|" local md5 file should not exist:"
argument_list|,
name|md5File
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
name|msg
operator|+
literal|" local sha1 file should not exist:"
argument_list|,
name|sha1File
operator|.
name|exists
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|createMessage
argument_list|(
name|ChecksumPolicy
operator|.
name|FAIL
argument_list|,
name|md5State
argument_list|,
name|sha1State
argument_list|)
argument_list|,
name|expectedResult
argument_list|,
name|actualResult
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertFixSetting
parameter_list|(
name|boolean
name|expectedResult
parameter_list|,
name|String
name|md5State
parameter_list|,
name|String
name|sha1State
parameter_list|)
throws|throws
name|Exception
block|{
name|PostDownloadPolicy
name|policy
init|=
name|lookupPolicy
argument_list|()
decl_stmt|;
name|File
name|localFile
init|=
name|createTestableFiles
argument_list|(
name|md5State
argument_list|,
name|sha1State
argument_list|)
decl_stmt|;
name|Properties
name|request
init|=
name|createRequest
argument_list|()
decl_stmt|;
name|boolean
name|actualResult
decl_stmt|;
try|try
block|{
name|policy
operator|.
name|applyPolicy
argument_list|(
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|request
argument_list|,
name|localFile
argument_list|)
expr_stmt|;
name|actualResult
operator|=
literal|true
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|PolicyViolationException
name|e
parameter_list|)
block|{
name|actualResult
operator|=
literal|false
expr_stmt|;
block|}
name|assertEquals
argument_list|(
name|createMessage
argument_list|(
name|ChecksumPolicy
operator|.
name|FIX
argument_list|,
name|md5State
argument_list|,
name|sha1State
argument_list|)
argument_list|,
name|expectedResult
argument_list|,
name|actualResult
argument_list|)
expr_stmt|;
comment|// End result should be legitimate SHA1 and MD5 files.
name|File
name|md5File
init|=
operator|new
name|File
argument_list|(
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
name|File
name|sha1File
init|=
operator|new
name|File
argument_list|(
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"ChecksumPolicy.apply(FIX) md5 should exist."
argument_list|,
name|md5File
operator|.
name|exists
argument_list|()
operator|&&
name|md5File
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"ChecksumPolicy.apply(FIX) sha1 should exist."
argument_list|,
name|sha1File
operator|.
name|exists
argument_list|()
operator|&&
name|sha1File
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|actualMd5Contents
init|=
name|readChecksumFile
argument_list|(
name|md5File
argument_list|)
decl_stmt|;
name|String
name|actualSha1Contents
init|=
name|readChecksumFile
argument_list|(
name|sha1File
argument_list|)
decl_stmt|;
name|String
name|expectedMd5Contents
init|=
literal|"360ccd01d8a0a2d94b86f9802c2fc548  artifact.jar"
decl_stmt|;
name|String
name|expectedSha1Contents
init|=
literal|"7dd8929150664f182db60ad15f20359d875f059f  artifact.jar"
decl_stmt|;
name|assertEquals
argument_list|(
literal|"ChecksumPolicy.apply(FIX) md5 contents:"
argument_list|,
name|expectedMd5Contents
argument_list|,
name|actualMd5Contents
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"ChecksumPolicy.apply(FIX) sha1 contents:"
argument_list|,
name|expectedSha1Contents
argument_list|,
name|actualSha1Contents
argument_list|)
expr_stmt|;
block|}
comment|/**      * Read the first line from the checksum file, and return it (trimmed).      */
specifier|private
name|String
name|readChecksumFile
parameter_list|(
name|File
name|checksumFile
parameter_list|)
throws|throws
name|Exception
block|{
name|FileReader
name|freader
init|=
literal|null
decl_stmt|;
name|BufferedReader
name|buf
init|=
literal|null
decl_stmt|;
try|try
block|{
name|freader
operator|=
operator|new
name|FileReader
argument_list|(
name|checksumFile
argument_list|)
expr_stmt|;
name|buf
operator|=
operator|new
name|BufferedReader
argument_list|(
name|freader
argument_list|)
expr_stmt|;
return|return
name|buf
operator|.
name|readLine
argument_list|()
return|;
block|}
finally|finally
block|{
if|if
condition|(
name|buf
operator|!=
literal|null
condition|)
block|{
name|buf
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|freader
operator|!=
literal|null
condition|)
block|{
name|freader
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|String
name|createMessage
parameter_list|(
name|String
name|settingType
parameter_list|,
name|String
name|md5State
parameter_list|,
name|String
name|sha1State
parameter_list|)
block|{
name|StringBuffer
name|msg
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|"Expected result of ChecksumPolicy.apply("
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|settingType
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|") when working with "
argument_list|)
expr_stmt|;
if|if
condition|(
name|md5State
operator|==
literal|null
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"NO"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"a "
argument_list|)
operator|.
name|append
argument_list|(
name|md5State
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|" MD5 and "
argument_list|)
expr_stmt|;
if|if
condition|(
name|sha1State
operator|==
literal|null
condition|)
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"NO"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|msg
operator|.
name|append
argument_list|(
literal|"a "
argument_list|)
operator|.
name|append
argument_list|(
name|sha1State
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|msg
operator|.
name|append
argument_list|(
literal|" SHA1:"
argument_list|)
expr_stmt|;
return|return
name|msg
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|Properties
name|createRequest
parameter_list|()
block|{
name|Properties
name|request
init|=
operator|new
name|Properties
argument_list|()
decl_stmt|;
name|request
operator|.
name|setProperty
argument_list|(
literal|"url"
argument_list|,
literal|"http://a.bad.hostname.maven.org/path/to/resource.txt"
argument_list|)
expr_stmt|;
return|return
name|request
return|;
block|}
specifier|private
name|File
name|createTestableFiles
parameter_list|(
name|String
name|md5State
parameter_list|,
name|String
name|sha1State
parameter_list|)
throws|throws
name|Exception
block|{
name|File
name|sourceDir
init|=
name|getTestFile
argument_list|(
literal|"src/test/resources/checksums/"
argument_list|)
decl_stmt|;
name|File
name|destDir
init|=
name|getTestFile
argument_list|(
literal|"target/checksum-tests/"
operator|+
name|name
operator|.
name|getMethodName
argument_list|()
operator|+
literal|"/"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFileToDirectory
argument_list|(
operator|new
name|File
argument_list|(
name|sourceDir
argument_list|,
literal|"artifact.jar"
argument_list|)
argument_list|,
name|destDir
argument_list|)
expr_stmt|;
if|if
condition|(
name|md5State
operator|!=
literal|null
condition|)
block|{
name|File
name|md5File
init|=
operator|new
name|File
argument_list|(
name|sourceDir
argument_list|,
literal|"artifact.jar.md5-"
operator|+
name|md5State
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Testable file exists: "
operator|+
name|md5File
operator|.
name|getName
argument_list|()
operator|+
literal|":"
argument_list|,
name|md5File
operator|.
name|exists
argument_list|()
operator|&&
name|md5File
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
literal|"artifact.jar.md5"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|md5File
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|sha1State
operator|!=
literal|null
condition|)
block|{
name|File
name|sha1File
init|=
operator|new
name|File
argument_list|(
name|sourceDir
argument_list|,
literal|"artifact.jar.sha1-"
operator|+
name|sha1State
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
literal|"Testable file exists: "
operator|+
name|sha1File
operator|.
name|getName
argument_list|()
operator|+
literal|":"
argument_list|,
name|sha1File
operator|.
name|exists
argument_list|()
operator|&&
name|sha1File
operator|.
name|isFile
argument_list|()
argument_list|)
expr_stmt|;
name|File
name|destFile
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
literal|"artifact.jar.sha1"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|sha1File
argument_list|,
name|destFile
argument_list|)
expr_stmt|;
block|}
name|File
name|localFile
init|=
operator|new
name|File
argument_list|(
name|destDir
argument_list|,
literal|"artifact.jar"
argument_list|)
decl_stmt|;
return|return
name|localFile
return|;
block|}
specifier|public
specifier|static
name|File
name|getTestFile
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
return|;
block|}
block|}
end_class

end_unit

