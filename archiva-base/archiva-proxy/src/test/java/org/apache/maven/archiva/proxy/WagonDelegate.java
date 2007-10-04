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
name|proxy
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
name|apache
operator|.
name|maven
operator|.
name|wagon
operator|.
name|ConnectionException
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
name|wagon
operator|.
name|ResourceDoesNotExistException
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
name|wagon
operator|.
name|TransferFailedException
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
name|wagon
operator|.
name|Wagon
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
name|wagon
operator|.
name|authentication
operator|.
name|AuthenticationException
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
name|wagon
operator|.
name|authentication
operator|.
name|AuthenticationInfo
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
name|wagon
operator|.
name|authorization
operator|.
name|AuthorizationException
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
name|wagon
operator|.
name|events
operator|.
name|SessionListener
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
name|wagon
operator|.
name|events
operator|.
name|TransferListener
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
name|wagon
operator|.
name|proxy
operator|.
name|ProxyInfo
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
name|wagon
operator|.
name|repository
operator|.
name|Repository
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
name|logging
operator|.
name|AbstractLogEnabled
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
name|List
import|;
end_import

begin_comment
comment|/**  * A dummy wagon implementation  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|WagonDelegate
extends|extends
name|AbstractLogEnabled
implements|implements
name|Wagon
block|{
specifier|private
name|Wagon
name|delegate
decl_stmt|;
specifier|private
name|String
name|contentToGet
decl_stmt|;
specifier|public
name|void
name|get
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|File
name|destination
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|".get("
operator|+
name|resourceName
operator|+
literal|", "
operator|+
name|destination
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|delegate
operator|.
name|get
argument_list|(
name|resourceName
argument_list|,
name|destination
argument_list|)
expr_stmt|;
name|create
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getIfNewer
parameter_list|(
name|String
name|resourceName
parameter_list|,
name|File
name|destination
parameter_list|,
name|long
name|timestamp
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|".getIfNewer("
operator|+
name|resourceName
operator|+
literal|", "
operator|+
name|destination
operator|+
literal|", "
operator|+
name|timestamp
operator|+
literal|")"
argument_list|)
expr_stmt|;
name|boolean
name|result
init|=
name|delegate
operator|.
name|getIfNewer
argument_list|(
name|resourceName
argument_list|,
name|destination
argument_list|,
name|timestamp
argument_list|)
decl_stmt|;
name|createIfMissing
argument_list|(
name|destination
argument_list|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|void
name|put
parameter_list|(
name|File
name|source
parameter_list|,
name|String
name|destination
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
name|delegate
operator|.
name|put
argument_list|(
name|source
argument_list|,
name|destination
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|putDirectory
parameter_list|(
name|File
name|sourceDirectory
parameter_list|,
name|String
name|destinationDirectory
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
name|delegate
operator|.
name|putDirectory
argument_list|(
name|sourceDirectory
argument_list|,
name|destinationDirectory
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|resourceExists
parameter_list|(
name|String
name|resourceName
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|AuthorizationException
block|{
return|return
name|delegate
operator|.
name|resourceExists
argument_list|(
name|resourceName
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getFileList
parameter_list|(
name|String
name|destinationDirectory
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
return|return
name|delegate
operator|.
name|getFileList
argument_list|(
name|destinationDirectory
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|supportsDirectoryCopy
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|supportsDirectoryCopy
argument_list|()
return|;
block|}
specifier|public
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|getRepository
argument_list|()
return|;
block|}
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|source
parameter_list|)
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
name|delegate
operator|.
name|connect
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|source
parameter_list|,
name|ProxyInfo
name|proxyInfo
parameter_list|)
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
name|delegate
operator|.
name|connect
argument_list|(
name|source
argument_list|,
name|proxyInfo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|source
parameter_list|,
name|AuthenticationInfo
name|authenticationInfo
parameter_list|)
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
name|delegate
operator|.
name|connect
argument_list|(
name|source
argument_list|,
name|authenticationInfo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|source
parameter_list|,
name|AuthenticationInfo
name|authenticationInfo
parameter_list|,
name|ProxyInfo
name|proxyInfo
parameter_list|)
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
name|delegate
operator|.
name|connect
argument_list|(
name|source
argument_list|,
name|authenticationInfo
argument_list|,
name|proxyInfo
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|openConnection
parameter_list|()
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
name|delegate
operator|.
name|openConnection
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|disconnect
parameter_list|()
throws|throws
name|ConnectionException
block|{
name|delegate
operator|.
name|disconnect
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|addSessionListener
parameter_list|(
name|SessionListener
name|listener
parameter_list|)
block|{
name|delegate
operator|.
name|addSessionListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeSessionListener
parameter_list|(
name|SessionListener
name|listener
parameter_list|)
block|{
name|delegate
operator|.
name|removeSessionListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasSessionListener
parameter_list|(
name|SessionListener
name|listener
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|hasSessionListener
argument_list|(
name|listener
argument_list|)
return|;
block|}
specifier|public
name|void
name|addTransferListener
parameter_list|(
name|TransferListener
name|listener
parameter_list|)
block|{
name|delegate
operator|.
name|addTransferListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|removeTransferListener
parameter_list|(
name|TransferListener
name|listener
parameter_list|)
block|{
name|delegate
operator|.
name|removeTransferListener
argument_list|(
name|listener
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasTransferListener
parameter_list|(
name|TransferListener
name|listener
parameter_list|)
block|{
return|return
name|delegate
operator|.
name|hasTransferListener
argument_list|(
name|listener
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isInteractive
parameter_list|()
block|{
return|return
name|delegate
operator|.
name|isInteractive
argument_list|()
return|;
block|}
specifier|public
name|void
name|setInteractive
parameter_list|(
name|boolean
name|interactive
parameter_list|)
block|{
name|delegate
operator|.
name|setInteractive
argument_list|(
name|interactive
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDelegate
parameter_list|(
name|Wagon
name|delegate
parameter_list|)
block|{
name|this
operator|.
name|delegate
operator|=
name|delegate
expr_stmt|;
block|}
name|void
name|setContentToGet
parameter_list|(
name|String
name|content
parameter_list|)
block|{
name|contentToGet
operator|=
name|content
expr_stmt|;
block|}
specifier|private
name|void
name|createIfMissing
parameter_list|(
name|File
name|destination
parameter_list|)
block|{
comment|// since the mock won't actually copy a file, create an empty one to simulate file existence
if|if
condition|(
operator|!
name|destination
operator|.
name|exists
argument_list|()
condition|)
block|{
name|create
argument_list|(
name|destination
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|create
parameter_list|(
name|File
name|destination
parameter_list|)
block|{
try|try
block|{
name|destination
operator|.
name|getParentFile
argument_list|()
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
if|if
condition|(
name|contentToGet
operator|==
literal|null
condition|)
block|{
name|destination
operator|.
name|createNewFile
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
operator|new
name|File
argument_list|(
name|destination
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
argument_list|,
name|contentToGet
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|RuntimeException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit

