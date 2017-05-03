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
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

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
name|HashMap
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"remoteRepository"
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
comment|/**      * default model value daily : every sunday at 8H00      */
specifier|private
name|String
name|cronExpression
init|=
literal|"0 0 08 ? * SUN"
decl_stmt|;
specifier|private
name|int
name|remoteDownloadTimeout
init|=
literal|300
decl_stmt|;
comment|/**      * @since 1.4-M2      */
specifier|private
name|boolean
name|downloadRemoteIndexOnStartup
init|=
literal|false
decl_stmt|;
comment|/**      * extraParameters.      *      * @since 1.4-M4      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraParameters
decl_stmt|;
comment|/**      * field to ease json mapping wrapper on<code>extraParameters</code> field      *      * @since 1.4-M4      */
specifier|private
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|extraParametersEntries
decl_stmt|;
comment|/**      * extraHeaders.      *      * @since 1.4-M4      */
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|extraHeaders
decl_stmt|;
comment|/**      * field to ease json mapping wrapper on<code>extraHeaders</code> field      *      * @since 1.4-M4      */
specifier|private
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|extraHeadersEntries
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
name|StringUtils
operator|.
name|stripEnd
argument_list|(
name|url
argument_list|,
literal|"/"
argument_list|)
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
comment|/**      * @since 1.4-M3      */
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
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
argument_list|(
name|id
argument_list|,
name|name
argument_list|,
name|url
argument_list|,
name|layout
argument_list|,
name|userName
argument_list|,
name|password
argument_list|,
name|timeout
argument_list|)
expr_stmt|;
name|setDescription
argument_list|(
name|description
argument_list|)
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
name|StringUtils
operator|.
name|stripEnd
argument_list|(
name|url
argument_list|,
literal|"/"
argument_list|)
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
specifier|public
name|String
name|getCronExpression
parameter_list|()
block|{
return|return
name|cronExpression
return|;
block|}
specifier|public
name|void
name|setCronExpression
parameter_list|(
name|String
name|cronExpression
parameter_list|)
block|{
name|this
operator|.
name|cronExpression
operator|=
name|cronExpression
expr_stmt|;
block|}
specifier|public
name|int
name|getRemoteDownloadTimeout
parameter_list|()
block|{
return|return
name|remoteDownloadTimeout
return|;
block|}
specifier|public
name|void
name|setRemoteDownloadTimeout
parameter_list|(
name|int
name|remoteDownloadTimeout
parameter_list|)
block|{
name|this
operator|.
name|remoteDownloadTimeout
operator|=
name|remoteDownloadTimeout
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDownloadRemoteIndexOnStartup
parameter_list|()
block|{
return|return
name|downloadRemoteIndexOnStartup
return|;
block|}
specifier|public
name|void
name|setDownloadRemoteIndexOnStartup
parameter_list|(
name|boolean
name|downloadRemoteIndexOnStartup
parameter_list|)
block|{
name|this
operator|.
name|downloadRemoteIndexOnStartup
operator|=
name|downloadRemoteIndexOnStartup
expr_stmt|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraParameters
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|extraParameters
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|extraParameters
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|extraParameters
return|;
block|}
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
name|extraParameters
parameter_list|)
block|{
name|this
operator|.
name|extraParameters
operator|=
name|extraParameters
expr_stmt|;
block|}
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
name|getExtraParameters
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|getExtraParametersEntries
parameter_list|()
block|{
name|this
operator|.
name|extraParametersEntries
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|getExtraParameters
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|this
operator|.
name|extraParametersEntries
operator|.
name|add
argument_list|(
operator|new
name|PropertyEntry
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|extraParametersEntries
return|;
block|}
specifier|public
name|void
name|setExtraParametersEntries
parameter_list|(
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|extraParametersEntries
parameter_list|)
block|{
if|if
condition|(
name|extraParametersEntries
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|extraParametersEntries
operator|=
name|extraParametersEntries
expr_stmt|;
for|for
control|(
name|PropertyEntry
name|propertyEntry
range|:
name|extraParametersEntries
control|)
block|{
name|this
operator|.
name|addExtraParameter
argument_list|(
name|propertyEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|propertyEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getExtraHeaders
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|extraHeaders
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|extraHeaders
operator|=
operator|new
name|HashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
return|return
name|extraHeaders
return|;
block|}
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
name|extraHeaders
parameter_list|)
block|{
name|this
operator|.
name|extraHeaders
operator|=
name|extraHeaders
expr_stmt|;
block|}
specifier|public
name|void
name|addExtraHeader
parameter_list|(
name|String
name|key
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|getExtraHeaders
argument_list|()
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|getExtraHeadersEntries
parameter_list|()
block|{
name|this
operator|.
name|extraHeadersEntries
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
expr_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|getExtraHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|this
operator|.
name|extraHeadersEntries
operator|.
name|add
argument_list|(
operator|new
name|PropertyEntry
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
block|}
return|return
name|this
operator|.
name|extraHeadersEntries
return|;
block|}
specifier|public
name|void
name|setExtraHeadersEntries
parameter_list|(
name|List
argument_list|<
name|PropertyEntry
argument_list|>
name|extraHeadersEntries
parameter_list|)
block|{
if|if
condition|(
name|extraHeadersEntries
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|this
operator|.
name|extraHeadersEntries
operator|=
name|extraHeadersEntries
expr_stmt|;
for|for
control|(
name|PropertyEntry
name|propertyEntry
range|:
name|extraHeadersEntries
control|)
block|{
name|this
operator|.
name|addExtraHeader
argument_list|(
name|propertyEntry
operator|.
name|getKey
argument_list|()
argument_list|,
name|propertyEntry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
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
literal|", cronExpression='"
argument_list|)
operator|.
name|append
argument_list|(
name|cronExpression
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
literal|", remoteDownloadTimeout="
argument_list|)
operator|.
name|append
argument_list|(
name|remoteDownloadTimeout
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", downloadRemoteIndexOnStartup="
argument_list|)
operator|.
name|append
argument_list|(
name|downloadRemoteIndexOnStartup
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", extraParameters="
argument_list|)
operator|.
name|append
argument_list|(
name|extraParameters
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", extraHeaders="
argument_list|)
operator|.
name|append
argument_list|(
name|extraHeaders
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

