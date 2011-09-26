begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"repositoryGroup"
argument_list|)
specifier|public
class|class
name|RemoteRepository
extends|extends
name|AbstractRepository
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|String
name|userName
decl_stmt|;
specifier|private
name|String
name|password
decl_stmt|;
specifier|private
name|int
name|timeout
init|=
literal|60
decl_stmt|;
comment|/**      * Activate download of remote index if remoteIndexUrl is set too.      */
specifier|private
name|boolean
name|downloadRemoteIndex
init|=
literal|false
decl_stmt|;
comment|/**      * Remote Index Url : if not starting with http will be relative to the remote repository url.      */
specifier|private
name|String
name|remoteIndexUrl
init|=
literal|".index"
decl_stmt|;
specifier|private
name|String
name|remoteDownloadNetworkProxyId
decl_stmt|;
specifier|public
name|RemoteRepository
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|RemoteRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|layout
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|layout
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|RemoteRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|layout
parameter_list|,
name|String
name|userName
parameter_list|,
name|String
name|password
parameter_list|,
name|int
name|timeout
parameter_list|)
block|{
name|super
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|layout
argument_list|)
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getUserName
parameter_list|()
block|{
return|return
name|userName
return|;
block|}
specifier|public
name|void
name|setUserName
parameter_list|(
name|String
name|userName
parameter_list|)
block|{
name|this
operator|.
name|userName
operator|=
name|userName
expr_stmt|;
block|}
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|password
return|;
block|}
specifier|public
name|void
name|setPassword
parameter_list|(
name|String
name|password
parameter_list|)
block|{
name|this
operator|.
name|password
operator|=
name|password
expr_stmt|;
block|}
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|timeout
return|;
block|}
specifier|public
name|void
name|setTimeout
parameter_list|(
name|int
name|timeout
parameter_list|)
block|{
name|this
operator|.
name|timeout
operator|=
name|timeout
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDownloadRemoteIndex
parameter_list|()
block|{
return|return
name|downloadRemoteIndex
return|;
block|}
specifier|public
name|void
name|setDownloadRemoteIndex
parameter_list|(
name|boolean
name|downloadRemoteIndex
parameter_list|)
block|{
name|this
operator|.
name|downloadRemoteIndex
operator|=
name|downloadRemoteIndex
expr_stmt|;
block|}
specifier|public
name|String
name|getRemoteIndexUrl
parameter_list|()
block|{
return|return
name|remoteIndexUrl
return|;
block|}
specifier|public
name|void
name|setRemoteIndexUrl
parameter_list|(
name|String
name|remoteIndexUrl
parameter_list|)
block|{
name|this
operator|.
name|remoteIndexUrl
operator|=
name|remoteIndexUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getRemoteDownloadNetworkProxyId
parameter_list|()
block|{
return|return
name|remoteDownloadNetworkProxyId
return|;
block|}
specifier|public
name|void
name|setRemoteDownloadNetworkProxyId
parameter_list|(
name|String
name|remoteDownloadNetworkProxyId
parameter_list|)
block|{
name|this
operator|.
name|remoteDownloadNetworkProxyId
operator|=
name|remoteDownloadNetworkProxyId
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
name|super
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"RemoteRepository"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{url='"
argument_list|)
operator|.
name|append
argument_list|(
name|url
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", userName='"
argument_list|)
operator|.
name|append
argument_list|(
name|userName
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", password='"
argument_list|)
operator|.
name|append
argument_list|(
name|password
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", timeout="
argument_list|)
operator|.
name|append
argument_list|(
name|timeout
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", downloadRemoteIndex="
argument_list|)
operator|.
name|append
argument_list|(
name|downloadRemoteIndex
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", remoteIndexUrl='"
argument_list|)
operator|.
name|append
argument_list|(
name|remoteIndexUrl
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", remoteDownloadNetworkProxyId='"
argument_list|)
operator|.
name|append
argument_list|(
name|remoteDownloadNetworkProxyId
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

