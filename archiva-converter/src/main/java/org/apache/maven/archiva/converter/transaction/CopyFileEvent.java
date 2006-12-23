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
name|converter
operator|.
name|transaction
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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

begin_comment
comment|/**  * Event to copy a file.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|CopyFileEvent
extends|extends
name|AbstractTransactionEvent
block|{
specifier|private
specifier|final
name|File
name|source
decl_stmt|;
specifier|private
specifier|final
name|File
name|destination
decl_stmt|;
specifier|public
name|CopyFileEvent
parameter_list|(
name|File
name|source
parameter_list|,
name|File
name|destination
parameter_list|)
block|{
name|this
operator|.
name|source
operator|=
name|source
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
block|}
specifier|public
name|void
name|commit
parameter_list|()
throws|throws
name|IOException
block|{
name|createBackup
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|mkDirs
argument_list|(
name|destination
operator|.
name|getParentFile
argument_list|()
argument_list|)
expr_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|source
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|rollback
parameter_list|()
throws|throws
name|IOException
block|{
name|destination
operator|.
name|delete
argument_list|()
expr_stmt|;
name|revertMkDirs
argument_list|()
expr_stmt|;
name|restoreBackup
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

