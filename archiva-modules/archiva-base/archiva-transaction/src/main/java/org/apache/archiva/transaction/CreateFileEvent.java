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
name|codehaus
operator|.
name|plexus
operator|.
name|digest
operator|.
name|Digester
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
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * Event for creating a file from a string content.  *  *  */
end_comment

begin_class
specifier|public
class|class
name|CreateFileEvent
extends|extends
name|AbstractTransactionEvent
block|{
specifier|private
specifier|final
name|Path
name|destination
decl_stmt|;
specifier|private
specifier|final
name|String
name|content
decl_stmt|;
comment|/**      *       * @param content      * @param destination      * @param checksumAlgorithms {@link List}&lt;{@link Digester}&gt; digesters to use for checksumming      */
specifier|public
name|CreateFileEvent
parameter_list|(
name|String
name|content
parameter_list|,
name|Path
name|destination
parameter_list|,
name|List
argument_list|<
name|ChecksumAlgorithm
argument_list|>
name|checksumAlgorithms
parameter_list|)
block|{
name|super
argument_list|(
name|checksumAlgorithms
argument_list|)
expr_stmt|;
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
name|this
operator|.
name|destination
operator|=
name|destination
expr_stmt|;
block|}
annotation|@
name|Override
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
name|getParent
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|Files
operator|.
name|exists
argument_list|(
name|destination
argument_list|)
condition|)
block|{
name|Files
operator|.
name|createFile
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
name|writeStringToFile
argument_list|(
name|destination
argument_list|,
name|content
argument_list|)
expr_stmt|;
name|createChecksums
argument_list|(
name|destination
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|rollback
parameter_list|()
throws|throws
name|IOException
block|{
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|destination
argument_list|)
expr_stmt|;
name|revertFilesCreated
argument_list|()
expr_stmt|;
name|revertMkDirs
argument_list|()
expr_stmt|;
name|restoreBackups
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

