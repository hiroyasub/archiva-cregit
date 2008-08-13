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
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  * ChecksumAlgorithmTest  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ChecksumAlgorithmTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testGetHashByExtensionSha1
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|,
name|ChecksumAlgorithm
operator|.
name|getByExtension
argument_list|(
operator|new
name|File
argument_list|(
literal|"something.jar.sha1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|,
name|ChecksumAlgorithm
operator|.
name|getByExtension
argument_list|(
operator|new
name|File
argument_list|(
literal|"OTHER.JAR.SHA1"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetHashByExtensionMd5
parameter_list|()
block|{
name|assertEquals
argument_list|(
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|,
name|ChecksumAlgorithm
operator|.
name|getByExtension
argument_list|(
operator|new
name|File
argument_list|(
literal|"something.jar.md5"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|,
name|ChecksumAlgorithm
operator|.
name|getByExtension
argument_list|(
operator|new
name|File
argument_list|(
literal|"OTHER.JAR.MD5"
argument_list|)
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetHashByExtensionInvalid
parameter_list|()
block|{
try|try
block|{
name|ChecksumAlgorithm
operator|.
name|getByExtension
argument_list|(
operator|new
name|File
argument_list|(
literal|"something.jar"
argument_list|)
argument_list|)
expr_stmt|;
name|fail
argument_list|(
literal|"Expected "
operator|+
name|IllegalArgumentException
operator|.
name|class
operator|.
name|getName
argument_list|()
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
block|}
end_class

end_unit

