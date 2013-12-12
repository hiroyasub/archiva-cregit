begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|filelock
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|FileNotFoundException
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_interface
specifier|public
interface|interface
name|FileLockManager
block|{
name|Lock
name|writeFileLock
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileLockException
throws|,
name|FileNotFoundException
function_decl|;
name|Lock
name|readFileLock
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileLockException
throws|,
name|FileNotFoundException
function_decl|;
name|void
name|release
parameter_list|(
name|Lock
name|lock
parameter_list|)
throws|throws
name|FileLockException
throws|,
name|FileNotFoundException
function_decl|;
name|int
name|getTimeout
parameter_list|()
function_decl|;
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

