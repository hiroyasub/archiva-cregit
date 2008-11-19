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
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|maven
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
name|maven
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
comment|/**  * AutoRenameConsumer  *  * @version $Id$  * @plexus.component role="org.apache.maven.archiva.consumers.KnownRepositoryContentConsumer"  * role-hint="auto-rename"  * instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|AutoRenameConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
block|{
comment|/**      * @plexus.configuration default-value="auto-rename"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Automatically rename common artifact mistakes."      */
specifier|private
name|String
name|description
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
name|File
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
argument_list|<
name|String
argument_list|>
argument_list|()
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
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|AutoRenameConsumer
parameter_list|()
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
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepositoryConfiguration
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
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|includes
return|;
block|}
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
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|repositoryDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
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
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
while|while
condition|(
name|itExtensions
operator|.
name|hasNext
argument_list|()
condition|)
block|{
name|String
name|extension
init|=
operator|(
name|String
operator|)
name|itExtensions
operator|.
name|next
argument_list|()
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
operator|(
name|String
operator|)
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
argument_list|()
operator|-
name|extension
operator|.
name|length
argument_list|()
argument_list|)
operator|+
name|fixedExtension
decl_stmt|;
name|File
name|to
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|repositoryDir
argument_list|,
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
argument_list|,
name|to
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
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
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
name|triggerConsumerInfo
argument_list|(
literal|"(Auto) Removing File: "
operator|+
name|file
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

