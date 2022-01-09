begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Class RemoteRepositoryConfiguration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|RemoteRepositoryConfiguration
extends|extends
name|AbstractRepositoryConfiguration
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
implements|,
name|ConfigurationModel
block|{
comment|//--------------------------/
comment|//- Class/Member Variables -/
comment|//--------------------------/
comment|/**      *       *             The URL for this repository.      *                 */
specifier|private
name|String
name|url
decl_stmt|;
comment|/**      *       *             The Username for this repository.      *                 */
specifier|private
name|String
name|username
decl_stmt|;
comment|/**      *       *             The Password for this repository.      *                 */
specifier|private
name|String
name|password
decl_stmt|;
comment|/**      *       *             Timeout in seconds for connections to this      * repository      *           .      */
specifier|private
name|int
name|timeout
init|=
literal|60
decl_stmt|;
comment|/**      *       *             When to run the refresh task.      *             Default is every sunday at 8H00.      *                 */
specifier|private
name|String
name|refreshCronExpression
init|=
literal|"0 0 08 ? * SUN"
decl_stmt|;
comment|/**      *       *             Activate download of remote index if      * remoteIndexUrl is set too.      *                 */
specifier|private
name|boolean
name|downloadRemoteIndex
init|=
literal|false
decl_stmt|;
comment|/**      *       *             Remote Index Url : if not starting with http      * will be relative to the remote repository url.      *                 */
specifier|private
name|String
name|remoteIndexUrl
decl_stmt|;
comment|/**      *       *             Id of the networkProxy to use when downloading      * remote index.      *                 */
specifier|private
name|String
name|remoteDownloadNetworkProxyId
decl_stmt|;
comment|/**      *       *             Timeout in seconds for download remote index.      * Default is more long than artifact download.      *                 */
specifier|private
name|int
name|remoteDownloadTimeout
init|=
literal|300
decl_stmt|;
comment|/**      *       *             Schedule download of remote index when archiva      * start      *           .      */
specifier|private
name|boolean
name|downloadRemoteIndexOnStartup
init|=
literal|false
decl_stmt|;
comment|/**      * Field extraParameters.      */
specifier|private
name|java
operator|.
name|util
operator|.
name|Map
name|extraParameters
decl_stmt|;
comment|/**      * Field extraHeaders.      */
specifier|private
name|java
operator|.
name|util
operator|.
name|Map
name|extraHeaders
decl_stmt|;
comment|/**      * The path to check the repository availability (relative to      * the repository URL). Some repositories do not allow      * browsing, so a certain artifact must be checked.      */
specifier|private
name|String
name|checkPath
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Method addExtraHeader.      *       * @param key      * @param value      */
specifier|public
name|void
name|addExtraHeader
parameter_list|(
name|Object
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
comment|//-- void addExtraHeader( Object, String )
comment|/**      * Method addExtraParameter.      *       * @param key      * @param value      */
specifier|public
name|void
name|addExtraParameter
parameter_list|(
name|Object
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
comment|//-- void addExtraParameter( Object, String )
comment|/**      * Get the path to check the repository availability (relative      * to the repository URL). Some repositories do not allow      * browsing, so a certain artifact must be checked.      *       * @return String      */
specifier|public
name|String
name|getCheckPath
parameter_list|()
block|{
return|return
name|this
operator|.
name|checkPath
return|;
block|}
comment|//-- String getCheckPath()
comment|/**      * Method getExtraHeaders.      *       * @return Map      */
specifier|public
name|java
operator|.
name|util
operator|.
name|Map
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
name|java
operator|.
name|util
operator|.
name|HashMap
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|extraHeaders
return|;
block|}
comment|//-- java.util.Map getExtraHeaders()
comment|/**      * Method getExtraParameters.      *       * @return Map      */
specifier|public
name|java
operator|.
name|util
operator|.
name|Map
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
name|java
operator|.
name|util
operator|.
name|HashMap
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|extraParameters
return|;
block|}
comment|//-- java.util.Map getExtraParameters()
comment|/**      * Get the Password for this repository.      *       * @return String      */
specifier|public
name|String
name|getPassword
parameter_list|()
block|{
return|return
name|this
operator|.
name|password
return|;
block|}
comment|//-- String getPassword()
comment|/**      * Get when to run the refresh task.      *             Default is every sunday at 8H00.      *       * @return String      */
specifier|public
name|String
name|getRefreshCronExpression
parameter_list|()
block|{
return|return
name|this
operator|.
name|refreshCronExpression
return|;
block|}
comment|//-- String getRefreshCronExpression()
comment|/**      * Get id of the networkProxy to use when downloading remote      * index.      *       * @return String      */
specifier|public
name|String
name|getRemoteDownloadNetworkProxyId
parameter_list|()
block|{
return|return
name|this
operator|.
name|remoteDownloadNetworkProxyId
return|;
block|}
comment|//-- String getRemoteDownloadNetworkProxyId()
comment|/**      * Get timeout in seconds for download remote index. Default is      * more long than artifact download.      *       * @return int      */
specifier|public
name|int
name|getRemoteDownloadTimeout
parameter_list|()
block|{
return|return
name|this
operator|.
name|remoteDownloadTimeout
return|;
block|}
comment|//-- int getRemoteDownloadTimeout()
comment|/**      * Get remote Index Url : if not starting with http will be      * relative to the remote repository url.      *       * @return String      */
specifier|public
name|String
name|getRemoteIndexUrl
parameter_list|()
block|{
return|return
name|this
operator|.
name|remoteIndexUrl
return|;
block|}
comment|//-- String getRemoteIndexUrl()
comment|/**      * Get timeout in seconds for connections to this repository.      *       * @return int      */
specifier|public
name|int
name|getTimeout
parameter_list|()
block|{
return|return
name|this
operator|.
name|timeout
return|;
block|}
comment|//-- int getTimeout()
comment|/**      * Get the URL for this repository.      *       * @return String      */
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|this
operator|.
name|url
return|;
block|}
comment|//-- String getUrl()
comment|/**      * Get the Username for this repository.      *       * @return String      */
specifier|public
name|String
name|getUsername
parameter_list|()
block|{
return|return
name|this
operator|.
name|username
return|;
block|}
comment|//-- String getUsername()
comment|/**      * Get activate download of remote index if remoteIndexUrl is      * set too.      *       * @return boolean      */
specifier|public
name|boolean
name|isDownloadRemoteIndex
parameter_list|()
block|{
return|return
name|this
operator|.
name|downloadRemoteIndex
return|;
block|}
comment|//-- boolean isDownloadRemoteIndex()
comment|/**      * Get schedule download of remote index when archiva start.      *       * @return boolean      */
specifier|public
name|boolean
name|isDownloadRemoteIndexOnStartup
parameter_list|()
block|{
return|return
name|this
operator|.
name|downloadRemoteIndexOnStartup
return|;
block|}
comment|//-- boolean isDownloadRemoteIndexOnStartup()
comment|/**      * Set the path to check the repository availability (relative      * to the repository URL). Some repositories do not allow      * browsing, so a certain artifact must be checked.      *       * @param checkPath      */
specifier|public
name|void
name|setCheckPath
parameter_list|(
name|String
name|checkPath
parameter_list|)
block|{
name|this
operator|.
name|checkPath
operator|=
name|checkPath
expr_stmt|;
block|}
comment|//-- void setCheckPath( String )
comment|/**      * Set activate download of remote index if remoteIndexUrl is      * set too.      *       * @param downloadRemoteIndex      */
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
comment|//-- void setDownloadRemoteIndex( boolean )
comment|/**      * Set schedule download of remote index when archiva start.      *       * @param downloadRemoteIndexOnStartup      */
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
comment|//-- void setDownloadRemoteIndexOnStartup( boolean )
comment|/**      * Set additional http headers to add to url when requesting      * remote repositories.      *       * @param extraHeaders      */
specifier|public
name|void
name|setExtraHeaders
parameter_list|(
name|java
operator|.
name|util
operator|.
name|Map
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
comment|//-- void setExtraHeaders( java.util.Map )
comment|/**      * Set additionnal request parameters to add to url when      * requesting remote repositories.      *       * @param extraParameters      */
specifier|public
name|void
name|setExtraParameters
parameter_list|(
name|java
operator|.
name|util
operator|.
name|Map
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
comment|//-- void setExtraParameters( java.util.Map )
comment|/**      * Set the Password for this repository.      *       * @param password      */
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
comment|//-- void setPassword( String )
comment|/**      * Set when to run the refresh task.      *             Default is every sunday at 8H00.      *       * @param refreshCronExpression      */
specifier|public
name|void
name|setRefreshCronExpression
parameter_list|(
name|String
name|refreshCronExpression
parameter_list|)
block|{
name|this
operator|.
name|refreshCronExpression
operator|=
name|refreshCronExpression
expr_stmt|;
block|}
comment|//-- void setRefreshCronExpression( String )
comment|/**      * Set id of the networkProxy to use when downloading remote      * index.      *       * @param remoteDownloadNetworkProxyId      */
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
comment|//-- void setRemoteDownloadNetworkProxyId( String )
comment|/**      * Set timeout in seconds for download remote index. Default is      * more long than artifact download.      *       * @param remoteDownloadTimeout      */
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
comment|//-- void setRemoteDownloadTimeout( int )
comment|/**      * Set remote Index Url : if not starting with http will be      * relative to the remote repository url.      *       * @param remoteIndexUrl      */
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
comment|//-- void setRemoteIndexUrl( String )
comment|/**      * Set timeout in seconds for connections to this repository.      *       * @param timeout      */
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
comment|//-- void setTimeout( int )
comment|/**      * Set the URL for this repository.      *       * @param url      */
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
comment|//-- void setUrl( String )
comment|/**      * Set the Username for this repository.      *       * @param username      */
specifier|public
name|void
name|setUsername
parameter_list|(
name|String
name|username
parameter_list|)
block|{
name|this
operator|.
name|username
operator|=
name|username
expr_stmt|;
block|}
comment|//-- void setUsername( String )
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"RemoteRepositoryConfiguration id:'"
operator|+
name|getId
argument_list|()
operator|+
literal|"',name:'"
operator|+
name|getName
argument_list|()
operator|+
literal|"'"
return|;
block|}
block|}
end_class

end_unit

