begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|base
operator|.
name|remote
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|EditableRemoteRepository
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
name|RemoteRepositoryContent
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
name|RepositoryCredentials
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
name|RepositoryType
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
name|base
operator|.
name|AbstractRepository
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
name|storage
operator|.
name|RepositoryStorage
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|Duration
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
name|Locale
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
comment|/**  * Abstract implementation of a remote repository. Abstract classes must implement the  * features and capabilities by themselves.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractRemoteRepository
extends|extends
name|AbstractRepository
implements|implements
name|EditableRemoteRepository
block|{
specifier|private
name|RepositoryCredentials
name|credentials
decl_stmt|;
specifier|private
name|String
name|checkPath
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraParameters
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|uExtraParameters
init|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|extraParameters
argument_list|)
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraHeaders
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|uExtraHeaders
init|=
name|Collections
operator|.
name|unmodifiableMap
argument_list|(
name|extraHeaders
argument_list|)
decl_stmt|;
specifier|private
name|Duration
name|timeout
init|=
name|Duration
operator|.
name|ofSeconds
argument_list|(
literal|60
argument_list|)
decl_stmt|;
specifier|private
name|String
name|proxyId
decl_stmt|;
specifier|private
name|RemoteRepositoryContent
name|content
decl_stmt|;
specifier|public
name|AbstractRemoteRepository
parameter_list|(
name|RepositoryType
name|type
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|RepositoryStorage
name|storage
parameter_list|)
block|{
name|super
argument_list|(
name|type
argument_list|,
name|id
argument_list|,
name|name
argument_list|,
name|storage
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractRemoteRepository
parameter_list|(
name|Locale
name|primaryLocale
parameter_list|,
name|RepositoryType
name|type
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|RepositoryStorage
name|storage
parameter_list|)
block|{
name|super
argument_list|(
name|primaryLocale
argument_list|,
name|type
argument_list|,
name|id
argument_list|,
name|name
argument_list|,
name|storage
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setCredentials
parameter_list|(
name|RepositoryCredentials
name|credentials
parameter_list|)
block|{
name|this
operator|.
name|credentials
operator|=
name|credentials
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setCheckPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|checkPath
operator|=
name|path
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setExtraParameters
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|params
parameter_list|)
block|{
name|this
operator|.
name|extraParameters
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|extraParameters
operator|.
name|putAll
argument_list|(
name|params
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addExtraParameter
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|extraParameters
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setExtraHeaders
parameter_list|(
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|headers
parameter_list|)
block|{
name|this
operator|.
name|extraHeaders
operator|.
name|clear
argument_list|()
expr_stmt|;
name|this
operator|.
name|extraHeaders
operator|.
name|putAll
argument_list|(
name|headers
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|addExtraHeader
parameter_list|(
name|String
name|header
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|extraHeaders
operator|.
name|put
argument_list|(
name|header
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setTimeout
parameter_list|(
name|Duration
name|duration
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|duration
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|RemoteRepositoryContent
name|getContent
parameter_list|( )
block|{
return|return
name|content
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setContent
parameter_list|(
name|RemoteRepositoryContent
name|content
parameter_list|)
block|{
name|this
operator|.
name|content
operator|=
name|content
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|RepositoryCredentials
name|getLoginCredentials
parameter_list|( )
block|{
return|return
name|credentials
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|getCheckPath
parameter_list|( )
block|{
return|return
name|checkPath
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraParameters
parameter_list|( )
block|{
return|return
name|uExtraParameters
return|;
block|}
annotation|@
name|Override
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraHeaders
parameter_list|( )
block|{
return|return
name|uExtraHeaders
return|;
block|}
annotation|@
name|Override
specifier|public
name|Duration
name|getTimeout
parameter_list|( )
block|{
return|return
name|timeout
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|str
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
return|return
name|str
operator|.
name|append
argument_list|(
literal|"checkPath="
argument_list|)
operator|.
name|append
argument_list|(
name|checkPath
argument_list|)
operator|.
name|append
argument_list|(
literal|",creds:"
argument_list|)
operator|.
name|append
argument_list|(
name|credentials
argument_list|)
operator|.
name|toString
argument_list|()
return|;
block|}
annotation|@
name|Override
specifier|public
name|void
name|setLocation
parameter_list|(
name|URI
name|location
parameter_list|)
block|{
comment|// Location of remote repositories is not for the local filestore
name|super
operator|.
name|location
operator|=
name|location
expr_stmt|;
block|}
block|}
end_class

end_unit

