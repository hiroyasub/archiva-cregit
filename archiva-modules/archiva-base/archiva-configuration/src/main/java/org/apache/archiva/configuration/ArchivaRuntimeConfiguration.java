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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  *   *         The runtime configuration.  *         *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|ArchivaRuntimeConfiguration
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
block|{
comment|//--------------------------/
comment|//- Class/Member Variables -/
comment|//--------------------------/
comment|/**      * the url failure cache configuration.      */
specifier|private
name|CacheConfiguration
name|urlFailureCacheConfiguration
decl_stmt|;
comment|/**      * the FileLocking configuration.      */
specifier|private
name|FileLockConfiguration
name|fileLockConfiguration
decl_stmt|;
comment|/**      * The base directory where the archiva data is stored. If not      * set, the appserver.base is used.      */
specifier|private
name|String
name|dataDirectory
decl_stmt|;
comment|/**      * The base directory for local storage of repository data. If      * not set, it's ${dataDirectory}/repositories.      */
specifier|private
name|String
name|repositoryBaseDirectory
decl_stmt|;
comment|/**      * The base directory for local storage of remote repository      * data. If not set, it's ${dataDirectory}/remotes.      */
specifier|private
name|String
name|remoteRepositoryBaseDirectory
decl_stmt|;
comment|/**      * The default language used for setting internationalized      * strings.      */
specifier|private
name|String
name|defaultLanguage
init|=
literal|"en-US"
decl_stmt|;
comment|/**      * Comma separated list of language patterns. Sorted by      * priority descending. Used for display of internationalized      * strings.      */
specifier|private
name|String
name|languageRange
init|=
literal|"en,fr,de"
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get the base directory where the archiva data is stored. If      * not set, the appserver.base is used.      *       * @return String      */
specifier|public
name|String
name|getDataDirectory
parameter_list|()
block|{
return|return
name|this
operator|.
name|dataDirectory
return|;
block|}
comment|//-- String getDataDirectory()
comment|/**      * Get the default language used for setting internationalized      * strings.      *       * @return String      */
specifier|public
name|String
name|getDefaultLanguage
parameter_list|()
block|{
return|return
name|this
operator|.
name|defaultLanguage
return|;
block|}
comment|//-- String getDefaultLanguage()
comment|/**      * Get the FileLocking configuration.      *       * @return FileLockConfiguration      */
specifier|public
name|FileLockConfiguration
name|getFileLockConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|fileLockConfiguration
return|;
block|}
comment|//-- FileLockConfiguration getFileLockConfiguration()
comment|/**      * Get comma separated list of language patterns. Sorted by      * priority descending. Used for display of internationalized      * strings.      *       * @return String      */
specifier|public
name|String
name|getLanguageRange
parameter_list|()
block|{
return|return
name|this
operator|.
name|languageRange
return|;
block|}
comment|//-- String getLanguageRange()
comment|/**      * Get the base directory for local storage of remote      * repository data. If not set, it's ${dataDirectory}/remotes.      *       * @return String      */
specifier|public
name|String
name|getRemoteRepositoryBaseDirectory
parameter_list|()
block|{
return|return
name|this
operator|.
name|remoteRepositoryBaseDirectory
return|;
block|}
comment|//-- String getRemoteRepositoryBaseDirectory()
comment|/**      * Get the base directory for local storage of repository data.      * If not set, it's ${dataDirectory}/repositories.      *       * @return String      */
specifier|public
name|String
name|getRepositoryBaseDirectory
parameter_list|()
block|{
return|return
name|this
operator|.
name|repositoryBaseDirectory
return|;
block|}
comment|//-- String getRepositoryBaseDirectory()
comment|/**      * Get the url failure cache configuration.      *       * @return CacheConfiguration      */
specifier|public
name|CacheConfiguration
name|getUrlFailureCacheConfiguration
parameter_list|()
block|{
return|return
name|this
operator|.
name|urlFailureCacheConfiguration
return|;
block|}
comment|//-- CacheConfiguration getUrlFailureCacheConfiguration()
comment|/**      * Set the base directory where the archiva data is stored. If      * not set, the appserver.base is used.      *       * @param dataDirectory      */
specifier|public
name|void
name|setDataDirectory
parameter_list|(
name|String
name|dataDirectory
parameter_list|)
block|{
name|this
operator|.
name|dataDirectory
operator|=
name|dataDirectory
expr_stmt|;
block|}
comment|//-- void setDataDirectory( String )
comment|/**      * Set the default language used for setting internationalized      * strings.      *       * @param defaultLanguage      */
specifier|public
name|void
name|setDefaultLanguage
parameter_list|(
name|String
name|defaultLanguage
parameter_list|)
block|{
name|this
operator|.
name|defaultLanguage
operator|=
name|defaultLanguage
expr_stmt|;
block|}
comment|//-- void setDefaultLanguage( String )
comment|/**      * Set the FileLocking configuration.      *       * @param fileLockConfiguration      */
specifier|public
name|void
name|setFileLockConfiguration
parameter_list|(
name|FileLockConfiguration
name|fileLockConfiguration
parameter_list|)
block|{
name|this
operator|.
name|fileLockConfiguration
operator|=
name|fileLockConfiguration
expr_stmt|;
block|}
comment|//-- void setFileLockConfiguration( FileLockConfiguration )
comment|/**      * Set comma separated list of language patterns. Sorted by      * priority descending. Used for display of internationalized      * strings.      *       * @param languageRange      */
specifier|public
name|void
name|setLanguageRange
parameter_list|(
name|String
name|languageRange
parameter_list|)
block|{
name|this
operator|.
name|languageRange
operator|=
name|languageRange
expr_stmt|;
block|}
comment|//-- void setLanguageRange( String )
comment|/**      * Set the base directory for local storage of remote      * repository data. If not set, it's ${dataDirectory}/remotes.      *       * @param remoteRepositoryBaseDirectory      */
specifier|public
name|void
name|setRemoteRepositoryBaseDirectory
parameter_list|(
name|String
name|remoteRepositoryBaseDirectory
parameter_list|)
block|{
name|this
operator|.
name|remoteRepositoryBaseDirectory
operator|=
name|remoteRepositoryBaseDirectory
expr_stmt|;
block|}
comment|//-- void setRemoteRepositoryBaseDirectory( String )
comment|/**      * Set the base directory for local storage of repository data.      * If not set, it's ${dataDirectory}/repositories.      *       * @param repositoryBaseDirectory      */
specifier|public
name|void
name|setRepositoryBaseDirectory
parameter_list|(
name|String
name|repositoryBaseDirectory
parameter_list|)
block|{
name|this
operator|.
name|repositoryBaseDirectory
operator|=
name|repositoryBaseDirectory
expr_stmt|;
block|}
comment|//-- void setRepositoryBaseDirectory( String )
comment|/**      * Set the url failure cache configuration.      *       * @param urlFailureCacheConfiguration      */
specifier|public
name|void
name|setUrlFailureCacheConfiguration
parameter_list|(
name|CacheConfiguration
name|urlFailureCacheConfiguration
parameter_list|)
block|{
name|this
operator|.
name|urlFailureCacheConfiguration
operator|=
name|urlFailureCacheConfiguration
expr_stmt|;
block|}
comment|//-- void setUrlFailureCacheConfiguration( CacheConfiguration )
block|}
end_class

end_unit

