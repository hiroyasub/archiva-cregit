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
name|ByteArrayInputStream
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
name|util
operator|.
name|ArrayList
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
comment|/**  * ChecksumTest  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ChecksumTest
extends|extends
name|AbstractChecksumTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
name|UNSET_SHA1
init|=
literal|"da39a3ee5e6b4b0d3255bfef95601890afd80709"
decl_stmt|;
specifier|public
name|void
name|testConstructSha1
parameter_list|()
block|{
name|Checksum
name|checksum
init|=
operator|new
name|Checksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum.algorithm"
argument_list|,
name|checksum
operator|.
name|getAlgorithm
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
argument_list|,
name|ChecksumAlgorithm
operator|.
name|SHA1
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testConstructMd5
parameter_list|()
block|{
name|Checksum
name|checksum
init|=
operator|new
name|Checksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum.algorithm"
argument_list|,
name|checksum
operator|.
name|getAlgorithm
argument_list|()
operator|.
name|getAlgorithm
argument_list|()
argument_list|,
name|ChecksumAlgorithm
operator|.
name|MD5
operator|.
name|getAlgorithm
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdate
parameter_list|()
block|{
name|Checksum
name|checksum
init|=
operator|new
name|Checksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
decl_stmt|;
name|byte
name|buf
index|[]
init|=
operator|(
literal|"You know, I'm sick of following my dreams, man. "
operator|+
literal|"I'm just going to ask where they're going and hook up with 'em later. - Mitch Hedberg"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|checksum
operator|.
name|update
argument_list|(
name|buf
argument_list|,
literal|0
argument_list|,
name|buf
operator|.
name|length
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum"
argument_list|,
literal|"e396119ae0542e85a74759602fd2f81e5d36d762"
argument_list|,
name|checksum
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateMany
parameter_list|()
throws|throws
name|IOException
block|{
name|Checksum
name|checksumSha1
init|=
operator|new
name|Checksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
decl_stmt|;
name|Checksum
name|checksumMd5
init|=
operator|new
name|Checksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|)
decl_stmt|;
name|List
argument_list|<
name|Checksum
argument_list|>
name|checksums
init|=
operator|new
name|ArrayList
argument_list|<
name|Checksum
argument_list|>
argument_list|()
decl_stmt|;
name|checksums
operator|.
name|add
argument_list|(
name|checksumSha1
argument_list|)
expr_stmt|;
name|checksums
operator|.
name|add
argument_list|(
name|checksumMd5
argument_list|)
expr_stmt|;
name|byte
name|buf
index|[]
init|=
operator|(
literal|"You know, I'm sick of following my dreams, man. "
operator|+
literal|"I'm just going to ask where they're going and hook up with 'em later. - Mitch Hedberg"
operator|)
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|ByteArrayInputStream
name|stream
init|=
operator|new
name|ByteArrayInputStream
argument_list|(
name|buf
argument_list|)
decl_stmt|;
name|Checksum
operator|.
name|update
argument_list|(
name|checksums
argument_list|,
name|stream
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum SHA1"
argument_list|,
literal|"e396119ae0542e85a74759602fd2f81e5d36d762"
argument_list|,
name|checksumSha1
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum MD5"
argument_list|,
literal|"21c2c5ca87ec018adacb2e2fb3432219"
argument_list|,
name|checksumMd5
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testUpdateWholeUpdatePartial
parameter_list|()
block|{
name|Checksum
name|checksum
init|=
operator|new
name|Checksum
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum unset"
argument_list|,
name|UNSET_SHA1
argument_list|,
name|checksum
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|expected
init|=
literal|"066c2cbbc8cdaecb8ff97dcb84502462d6f575f3"
decl_stmt|;
name|byte
name|reesepieces
index|[]
init|=
literal|"eatagramovabits"
operator|.
name|getBytes
argument_list|()
decl_stmt|;
name|checksum
operator|.
name|update
argument_list|(
name|reesepieces
argument_list|,
literal|0
argument_list|,
name|reesepieces
operator|.
name|length
argument_list|)
expr_stmt|;
name|String
name|actual
init|=
name|checksum
operator|.
name|getChecksum
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Expected"
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
comment|// Reset the checksum.
name|checksum
operator|.
name|reset
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Checksum unset"
argument_list|,
name|UNSET_SHA1
argument_list|,
name|checksum
operator|.
name|getChecksum
argument_list|()
argument_list|)
expr_stmt|;
comment|// Now parse it again in 3 pieces.
name|checksum
operator|.
name|update
argument_list|(
name|reesepieces
argument_list|,
literal|0
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|checksum
operator|.
name|update
argument_list|(
name|reesepieces
argument_list|,
literal|5
argument_list|,
literal|5
argument_list|)
expr_stmt|;
name|checksum
operator|.
name|update
argument_list|(
name|reesepieces
argument_list|,
literal|10
argument_list|,
name|reesepieces
operator|.
name|length
operator|-
literal|10
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Expected"
argument_list|,
name|expected
argument_list|,
name|actual
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

