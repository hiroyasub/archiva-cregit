begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|storage
operator|.
name|maven2
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
name|proxy
operator|.
name|ProxyInfoProvider
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

begin_class
specifier|public
class|class
name|MockWagon
implements|implements
name|Wagon
block|{
annotation|@
name|Override
specifier|public
name|void
name|get
parameter_list|(
name|String
name|s
parameter_list|,
name|File
name|file
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
name|String
name|sourceFile
init|=
name|getBasedir
argument_list|()
operator|+
literal|"/target/test-classes/"
operator|+
name|s
decl_stmt|;
try|try
block|{
name|FileUtils
operator|.
name|copyFile
argument_list|(
operator|new
name|File
argument_list|(
name|sourceFile
argument_list|)
argument_list|,
name|file
argument_list|)
expr_stmt|;
assert|assert
operator|(
name|file
operator|.
name|exists
argument_list|()
operator|)
assert|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ResourceDoesNotExistException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|getIfNewer
parameter_list|(
name|String
name|s
parameter_list|,
name|File
name|file
parameter_list|,
name|long
name|l
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|put
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|s
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|putDirectory
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|s
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|resourceExists
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|AuthorizationException
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|List
name|getFileList
parameter_list|(
name|String
name|s
parameter_list|)
throws|throws
name|TransferFailedException
throws|,
name|ResourceDoesNotExistException
throws|,
name|AuthorizationException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsDirectoryCopy
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|Repository
name|getRepository
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|ProxyInfo
name|proxyInfo
parameter_list|)
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|ProxyInfoProvider
name|proxyInfoProvider
parameter_list|)
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|AuthenticationInfo
name|authenticationInfo
parameter_list|)
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|repository
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
block|}
annotation|@
name|Override
specifier|public
name|void
name|connect
parameter_list|(
name|Repository
name|repository
parameter_list|,
name|AuthenticationInfo
name|authenticationInfo
parameter_list|,
name|ProxyInfoProvider
name|proxyInfoProvider
parameter_list|)
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|openConnection
parameter_list|()
throws|throws
name|ConnectionException
throws|,
name|AuthenticationException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|disconnect
parameter_list|()
throws|throws
name|ConnectionException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|i
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setReadTimeout
parameter_list|(
name|int
name|timeoutValue
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|int
name|getReadTimeout
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addSessionListener
parameter_list|(
name|SessionListener
name|sessionListener
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeSessionListener
parameter_list|(
name|SessionListener
name|sessionListener
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasSessionListener
parameter_list|(
name|SessionListener
name|sessionListener
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addTransferListener
parameter_list|(
name|TransferListener
name|transferListener
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeTransferListener
parameter_list|(
name|TransferListener
name|transferListener
parameter_list|)
block|{
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|hasTransferListener
parameter_list|(
name|TransferListener
name|transferListener
parameter_list|)
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|isInteractive
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setInteractive
parameter_list|(
name|boolean
name|b
parameter_list|)
block|{
block|}
specifier|public
name|String
name|getBasedir
parameter_list|()
block|{
name|String
name|basedir
init|=
name|System
operator|.
name|getProperty
argument_list|(
literal|"basedir"
argument_list|)
decl_stmt|;
if|if
condition|(
name|basedir
operator|==
literal|null
condition|)
block|{
name|basedir
operator|=
operator|new
name|File
argument_list|(
literal|""
argument_list|)
operator|.
name|getAbsolutePath
argument_list|()
expr_stmt|;
block|}
return|return
name|basedir
return|;
block|}
block|}
end_class

end_unit

