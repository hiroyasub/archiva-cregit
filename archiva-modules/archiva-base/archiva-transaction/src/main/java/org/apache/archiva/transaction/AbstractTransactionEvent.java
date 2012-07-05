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
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|digest
operator|.
name|DigesterException
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
name|Collections
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|HashMap
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_comment
comment|/**  * Abstract class for the TransactionEvents  *  *  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractTransactionEvent
implements|implements
name|TransactionEvent
block|{
specifier|private
name|Map
argument_list|<
name|File
argument_list|,
name|File
argument_list|>
name|backups
init|=
operator|new
name|HashMap
argument_list|<
name|File
argument_list|,
name|File
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|File
argument_list|>
name|createdDirs
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|List
argument_list|<
name|File
argument_list|>
name|createdFiles
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
comment|/**      * {@link List}&lt;{@link Digester}>      */
specifier|private
name|List
argument_list|<
name|?
extends|extends
name|Digester
argument_list|>
name|digesters
decl_stmt|;
specifier|protected
name|AbstractTransactionEvent
parameter_list|()
block|{
name|this
argument_list|(
operator|new
name|ArrayList
argument_list|<
name|Digester
argument_list|>
argument_list|(
literal|0
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|protected
name|AbstractTransactionEvent
parameter_list|(
name|List
argument_list|<
name|?
extends|extends
name|Digester
argument_list|>
name|digesters
parameter_list|)
block|{
name|this
operator|.
name|digesters
operator|=
name|digesters
expr_stmt|;
block|}
specifier|protected
name|List
argument_list|<
name|?
extends|extends
name|Digester
argument_list|>
name|getDigesters
parameter_list|()
block|{
return|return
name|digesters
return|;
block|}
comment|/**      * Method that creates a directory as well as all the parent directories needed      *      * @param dir The File directory to be created      * @throws IOException when an unrecoverable error occurred      */
specifier|protected
name|void
name|mkDirs
parameter_list|(
name|File
name|dir
parameter_list|)
throws|throws
name|IOException
block|{
name|List
argument_list|<
name|File
argument_list|>
name|createDirs
init|=
operator|new
name|ArrayList
argument_list|<
name|File
argument_list|>
argument_list|()
decl_stmt|;
name|File
name|parent
init|=
name|dir
decl_stmt|;
while|while
condition|(
operator|!
name|parent
operator|.
name|exists
argument_list|()
operator|||
operator|!
name|parent
operator|.
name|isDirectory
argument_list|()
condition|)
block|{
name|createDirs
operator|.
name|add
argument_list|(
name|parent
argument_list|)
expr_stmt|;
name|parent
operator|=
name|parent
operator|.
name|getParentFile
argument_list|()
expr_stmt|;
block|}
while|while
condition|(
operator|!
name|createDirs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|File
name|directory
init|=
operator|(
name|File
operator|)
name|createDirs
operator|.
name|remove
argument_list|(
name|createDirs
operator|.
name|size
argument_list|()
operator|-
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
name|directory
operator|.
name|mkdir
argument_list|()
condition|)
block|{
name|createdDirs
operator|.
name|add
argument_list|(
name|directory
argument_list|)
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Failed to create directory: "
operator|+
name|directory
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
throw|;
block|}
block|}
block|}
specifier|protected
name|void
name|revertMkDirs
parameter_list|()
throws|throws
name|IOException
block|{
if|if
condition|(
name|createdDirs
operator|!=
literal|null
condition|)
block|{
name|Collections
operator|.
name|reverse
argument_list|(
name|createdDirs
argument_list|)
expr_stmt|;
while|while
condition|(
operator|!
name|createdDirs
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
name|File
name|dir
init|=
operator|(
name|File
operator|)
name|createdDirs
operator|.
name|remove
argument_list|(
literal|0
argument_list|)
decl_stmt|;
if|if
condition|(
name|dir
operator|.
name|isDirectory
argument_list|()
operator|&&
name|dir
operator|.
name|list
argument_list|()
operator|.
name|length
operator|==
literal|0
condition|)
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|//cannot rollback created directory if it still contains files
break|break;
block|}
block|}
block|}
block|}
specifier|protected
name|void
name|revertFilesCreated
parameter_list|()
throws|throws
name|IOException
block|{
name|Iterator
argument_list|<
name|File
argument_list|>
name|it
init|=
name|createdFiles
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|it
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|File
name|file
init|=
operator|(
name|File
operator|)
name|it
operator|.
name|next
argument_list|()
decl_stmt|;
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
name|it
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|createBackup
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
operator|&&
name|file
operator|.
name|isFile
argument_list|()
condition|)
block|{
name|File
name|backup
init|=
name|File
operator|.
name|createTempFile
argument_list|(
literal|"temp-"
argument_list|,
literal|".backup"
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|file
argument_list|,
name|backup
argument_list|)
expr_stmt|;
name|backup
operator|.
name|deleteOnExit
argument_list|()
expr_stmt|;
name|backups
operator|.
name|put
argument_list|(
name|file
argument_list|,
name|backup
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|restoreBackups
parameter_list|()
throws|throws
name|IOException
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|File
argument_list|,
name|File
argument_list|>
name|entry
range|:
name|backups
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|entry
operator|.
name|getValue
argument_list|()
argument_list|,
name|entry
operator|.
name|getKey
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|protected
name|void
name|restoreBackup
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|backup
init|=
operator|(
name|File
operator|)
name|backups
operator|.
name|get
argument_list|(
name|file
argument_list|)
decl_stmt|;
if|if
condition|(
name|backup
operator|!=
literal|null
condition|)
block|{
name|FileUtils
operator|.
name|copyFile
argument_list|(
name|backup
argument_list|,
name|file
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create checksums of file using all digesters defined at construction time.      *      * @param file      * @param force whether existing checksums should be overwritten or not      * @throws IOException      */
specifier|protected
name|void
name|createChecksums
parameter_list|(
name|File
name|file
parameter_list|,
name|boolean
name|force
parameter_list|)
throws|throws
name|IOException
block|{
for|for
control|(
name|Digester
name|digester
range|:
name|getDigesters
argument_list|()
control|)
block|{
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|file
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"."
operator|+
name|getDigesterFileExtension
argument_list|(
name|digester
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
name|checksumFile
operator|.
name|exists
argument_list|()
condition|)
block|{
if|if
condition|(
operator|!
name|force
condition|)
block|{
continue|continue;
block|}
name|createBackup
argument_list|(
name|checksumFile
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|createdFiles
operator|.
name|add
argument_list|(
name|checksumFile
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|writeStringToFile
argument_list|(
name|checksumFile
argument_list|,
name|digester
operator|.
name|calc
argument_list|(
name|file
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
throw|throw
operator|(
name|IOException
operator|)
name|e
operator|.
name|getCause
argument_list|()
throw|;
block|}
block|}
block|}
comment|/**      * TODO: Remove in favor of using FileUtils directly.      */
specifier|protected
name|void
name|writeStringToFile
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|content
parameter_list|)
throws|throws
name|IOException
block|{
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|file
argument_list|,
name|content
argument_list|)
expr_stmt|;
block|}
comment|/**      * File extension for checksums      * TODO should be moved to plexus-digester ?      */
specifier|protected
name|String
name|getDigesterFileExtension
parameter_list|(
name|Digester
name|digester
parameter_list|)
block|{
return|return
name|digester
operator|.
name|getAlgorithm
argument_list|()
operator|.
name|toLowerCase
argument_list|()
operator|.
name|replaceAll
argument_list|(
literal|"-"
argument_list|,
literal|""
argument_list|)
return|;
block|}
block|}
end_class

end_unit

