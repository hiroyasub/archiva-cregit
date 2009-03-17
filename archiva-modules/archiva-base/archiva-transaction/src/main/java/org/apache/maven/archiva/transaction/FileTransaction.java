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
name|transaction
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

begin_comment
comment|/**  * Implement commit/rollback semantics for a set of files.  *  */
end_comment

begin_class
specifier|public
class|class
name|FileTransaction
block|{
specifier|private
name|List
argument_list|<
name|AbstractTransactionEvent
argument_list|>
name|events
init|=
operator|new
name|ArrayList
argument_list|<
name|AbstractTransactionEvent
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|void
name|commit
parameter_list|()
throws|throws
name|TransactionException
block|{
name|List
argument_list|<
name|TransactionEvent
argument_list|>
name|toRollback
init|=
operator|new
name|ArrayList
argument_list|<
name|TransactionEvent
argument_list|>
argument_list|(
name|events
operator|.
name|size
argument_list|()
argument_list|)
decl_stmt|;
for|for
control|(
name|TransactionEvent
name|event
range|:
name|events
control|)
block|{
try|try
block|{
name|event
operator|.
name|commit
argument_list|()
expr_stmt|;
name|toRollback
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
try|try
block|{
name|rollback
argument_list|(
name|toRollback
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|TransactionException
argument_list|(
literal|"Unable to commit file transaction"
argument_list|,
name|e
argument_list|)
throw|;
block|}
catch|catch
parameter_list|(
name|IOException
name|ioe
parameter_list|)
block|{
throw|throw
operator|new
name|TransactionException
argument_list|(
literal|"Unable to commit file transaction, and rollback failed with error: '"
operator|+
name|ioe
operator|.
name|getMessage
argument_list|()
operator|+
literal|"'"
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
block|}
specifier|private
name|void
name|rollback
parameter_list|(
name|List
argument_list|<
name|TransactionEvent
argument_list|>
name|toRollback
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|TransactionEvent
name|event
range|:
name|toRollback
control|)
block|{
name|event
operator|.
name|rollback
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * @param source      * @param destination      * @param digesters   {@link List}&lt;{@link org.codehaus.plexus.digest.Digester}> digesters to use for checksumming      */
specifier|public
name|void
name|copyFile
parameter_list|(
name|File
name|source
parameter_list|,
name|File
name|destination
parameter_list|,
name|List
argument_list|<
name|Digester
argument_list|>
name|digesters
parameter_list|)
block|{
name|events
operator|.
name|add
argument_list|(
operator|new
name|CopyFileEvent
argument_list|(
name|source
argument_list|,
name|destination
argument_list|,
name|digesters
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * @param content      * @param destination      * @param digesters   {@link List}&lt;{@link org.codehaus.plexus.digest.Digester}> digesters to use for checksumming      */
specifier|public
name|void
name|createFile
parameter_list|(
name|String
name|content
parameter_list|,
name|File
name|destination
parameter_list|,
name|List
argument_list|<
name|Digester
argument_list|>
name|digesters
parameter_list|)
block|{
name|events
operator|.
name|add
argument_list|(
operator|new
name|CreateFileEvent
argument_list|(
name|content
argument_list|,
name|destination
argument_list|,
name|digesters
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

