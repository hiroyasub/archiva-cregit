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
name|common
operator|.
name|utils
operator|.
name|FileUtil
import|;
end_import

begin_comment
comment|/**  * AbstractChecksumTestCase  *  * @version $Id$  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractChecksumTestCase
extends|extends
name|TestCase
block|{
specifier|public
name|File
name|getTestOutputDir
parameter_list|()
block|{
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"target/test-output/"
operator|+
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|dir
operator|.
name|exists
argument_list|()
operator|==
literal|false
condition|)
block|{
if|if
condition|(
name|dir
operator|.
name|mkdirs
argument_list|()
operator|==
literal|false
condition|)
block|{
name|fail
argument_list|(
literal|"Unable to create test output directory: "
operator|+
name|dir
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|dir
return|;
block|}
specifier|public
name|File
name|getTestResource
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|File
name|dir
init|=
operator|new
name|File
argument_list|(
name|FileUtil
operator|.
name|getBasedir
argument_list|()
argument_list|,
literal|"src/test/resources"
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|dir
argument_list|,
name|filename
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|==
literal|false
condition|)
block|{
name|fail
argument_list|(
literal|"Test Resource does not exist: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|file
return|;
block|}
block|}
end_class

end_unit

