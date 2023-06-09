begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|core
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
name|archiva
operator|.
name|consumers
operator|.
name|AbstractMonitoredConsumer
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
name|consumers
operator|.
name|ConsumerException
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
name|consumers
operator|.
name|KnownRepositoryContentConsumer
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
name|repository
operator|.
name|ManagedRepository
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
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
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
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
comment|/**  * AutoRenameConsumer  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#auto-rename"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|AutoRenameConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AutoRenameConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
name|String
name|id
init|=
literal|"auto-rename"
decl_stmt|;
specifier|private
name|String
name|description
init|=
literal|"Automatically rename common artifact mistakes."
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|RENAME_FAILURE
init|=
literal|"rename_failure"
decl_stmt|;
specifier|private
name|Path
name|repositoryDir
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
literal|3
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extensionRenameMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
specifier|public
name|AutoRenameConsumer
parameter_list|( )
block|{
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.distribution-tgz"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.distribution-zip"
argument_list|)
expr_stmt|;
name|includes
operator|.
name|add
argument_list|(
literal|"**/*.plugin"
argument_list|)
expr_stmt|;
name|extensionRenameMap
operator|.
name|put
argument_list|(
literal|".distribution-tgz"
argument_list|,
literal|".tar.gz"
argument_list|)
expr_stmt|;
name|extensionRenameMap
operator|.
name|put
argument_list|(
literal|".distribution-zip"
argument_list|,
literal|".zip"
argument_list|)
expr_stmt|;
name|extensionRenameMap
operator|.
name|put
argument_list|(
literal|".plugin"
argument_list|,
literal|".jar"
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|( )
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getDescription
parameter_list|( )
block|{
return|return
name|this
operator|.
name|description
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|this
operator|.
name|repositoryDir
operator|=
name|Paths
operator|.
name|get
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|beginScan
argument_list|(
name|repository
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeScan
parameter_list|( )
block|{
comment|/* do nothing */
block|}
annotation|@
name|Override
specifier|public
name|void
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|completeScan
argument_list|( )
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|( )
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|( )
block|{
return|return
name|includes
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|Path
name|file
init|=
name|this
operator|.
name|repositoryDir
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|file
argument_list|)
condition|)
block|{
name|Iterator
argument_list|<
name|String
argument_list|>
name|itExtensions
init|=
name|this
operator|.
name|extensionRenameMap
operator|.
name|keySet
argument_list|( )
operator|.
name|iterator
argument_list|( )
decl_stmt|;
while|while
condition|(
name|itExtensions
operator|.
name|hasNext
argument_list|( )
condition|)
block|{
name|String
name|extension
init|=
name|itExtensions
operator|.
name|next
argument_list|( )
decl_stmt|;
if|if
condition|(
name|path
operator|.
name|endsWith
argument_list|(
name|extension
argument_list|)
condition|)
block|{
name|String
name|fixedExtension
init|=
name|this
operator|.
name|extensionRenameMap
operator|.
name|get
argument_list|(
name|extension
argument_list|)
decl_stmt|;
name|String
name|correctedPath
init|=
name|path
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|path
operator|.
name|length
argument_list|( )
operator|-
name|extension
operator|.
name|length
argument_list|( )
argument_list|)
operator|+
name|fixedExtension
decl_stmt|;
name|Path
name|to
init|=
name|repositoryDir
operator|.
name|resolve
argument_list|(
name|correctedPath
argument_list|)
decl_stmt|;
try|try
block|{
comment|// Rename the file.
name|FileUtils
operator|.
name|moveFile
argument_list|(
name|file
operator|.
name|toFile
argument_list|()
argument_list|,
name|to
operator|.
name|toFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to rename {} to {} :"
argument_list|,
name|path
argument_list|,
name|correctedPath
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|triggerConsumerWarning
argument_list|(
name|RENAME_FAILURE
argument_list|,
literal|"Unable to rename "
operator|+
name|path
operator|+
literal|" to "
operator|+
name|correctedPath
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|log
operator|.
name|info
argument_list|(
literal|"(Auto) Removing File: {} "
argument_list|,
name|file
operator|.
name|toAbsolutePath
argument_list|( )
argument_list|)
expr_stmt|;
name|triggerConsumerInfo
argument_list|(
literal|"(Auto) Removing File: "
operator|+
name|file
operator|.
name|toAbsolutePath
argument_list|( )
argument_list|)
expr_stmt|;
try|try
block|{
name|Files
operator|.
name|delete
argument_list|(
name|file
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Could not delete file {}: {}"
argument_list|,
name|file
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
throw|throw
operator|new
name|ConsumerException
argument_list|(
literal|"File deletion failed "
operator|+
name|file
argument_list|)
throw|;
block|}
block|}
block|}
annotation|@
name|Override
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

