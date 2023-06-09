begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|transaction
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
name|apache
operator|.
name|archiva
operator|.
name|checksum
operator|.
name|ChecksumAlgorithm
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
name|ArchivaBlockJUnit4ClassRunner
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
name|junit
operator|.
name|Before
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
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|charset
operator|.
name|Charset
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|ArchivaBlockJUnit4ClassRunner
operator|.
name|class
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractFileEventTest
extends|extends
name|TestCase
block|{
specifier|protected
name|List
argument_list|<
name|ChecksumAlgorithm
argument_list|>
name|checksumAlgorithms
decl_stmt|;
annotation|@
name|SuppressWarnings
argument_list|(
literal|"unchecked"
argument_list|)
annotation|@
name|Before
annotation|@
name|Override
specifier|public
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
name|checksumAlgorithms
operator|=
name|Arrays
operator|.
name|asList
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA256
argument_list|,
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|,
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertChecksumExists
parameter_list|(
name|Path
name|file
parameter_list|,
name|String
name|algorithm
parameter_list|)
block|{
name|assertChecksum
argument_list|(
name|file
argument_list|,
name|algorithm
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertChecksumDoesNotExist
parameter_list|(
name|Path
name|file
parameter_list|,
name|String
name|algorithm
parameter_list|)
block|{
name|assertChecksum
argument_list|(
name|file
argument_list|,
name|algorithm
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|assertChecksum
parameter_list|(
name|Path
name|file
parameter_list|,
name|String
name|algorithm
parameter_list|,
name|boolean
name|exist
parameter_list|)
block|{
name|String
name|msg
init|=
name|exist
condition|?
literal|"exists"
else|:
literal|"does not exist"
decl_stmt|;
name|Path
name|checksumFile
init|=
name|Paths
operator|.
name|get
argument_list|(
name|file
operator|.
name|toAbsolutePath
argument_list|()
operator|+
literal|"."
operator|+
name|algorithm
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Test file "
operator|+
name|algorithm
operator|+
literal|" checksum "
operator|+
name|msg
argument_list|,
name|exist
argument_list|,
name|Files
operator|.
name|exists
argument_list|(
name|checksumFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertChecksumCommit
parameter_list|(
name|Path
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|assertChecksumExists
argument_list|(
name|file
argument_list|,
literal|"md5"
argument_list|)
expr_stmt|;
name|assertChecksumExists
argument_list|(
name|file
argument_list|,
literal|"sha1"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|void
name|assertChecksumRollback
parameter_list|(
name|Path
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|assertChecksumDoesNotExist
argument_list|(
name|file
argument_list|,
literal|"md5"
argument_list|)
expr_stmt|;
name|assertChecksumDoesNotExist
argument_list|(
name|file
argument_list|,
literal|"sha1"
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|String
name|readFile
parameter_list|(
name|Path
name|file
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|file
operator|.
name|toFile
argument_list|()
argument_list|,
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
argument_list|)
return|;
block|}
specifier|protected
name|void
name|writeFile
parameter_list|(
name|Path
name|file
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|IOException
block|{
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
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|file
argument_list|,
name|Charset
operator|.
name|defaultCharset
argument_list|()
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

