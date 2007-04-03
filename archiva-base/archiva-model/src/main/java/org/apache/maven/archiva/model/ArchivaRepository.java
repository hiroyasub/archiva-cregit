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
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * ArchivaRepository   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaRepository
block|{
comment|//    protected ArtifactRepositoryPolicy releases;
comment|//
comment|//    protected ArtifactRepositoryPolicy snapshots;
specifier|private
name|ArchivaRepositoryModel
name|model
decl_stmt|;
specifier|private
name|RepositoryURL
name|url
decl_stmt|;
specifier|protected
name|boolean
name|blacklisted
decl_stmt|;
comment|/**      * Construct a Repository.      *       * @param id the unique identifier for this repository.      * @param name the name for this repository.      * @param url the base URL for this repository (this should point to the top level URL for the entire repository)      * @param layout the layout technique for this repository.      */
specifier|public
name|ArchivaRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|model
operator|=
operator|new
name|ArchivaRepositoryModel
argument_list|()
expr_stmt|;
name|model
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|model
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|setUrl
argument_list|(
operator|new
name|RepositoryURL
argument_list|(
name|url
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Construct a Repository.      *       * @param id the unique identifier for this repository.      * @param name the name for this repository.      * @param url the base URL for this repository (this should point to the top level URL for the entire repository)      * @param layout the layout technique for this repository.      */
specifier|public
name|ArchivaRepository
parameter_list|(
name|ArchivaRepositoryModel
name|model
parameter_list|)
block|{
name|this
operator|.
name|model
operator|=
name|model
expr_stmt|;
name|this
operator|.
name|url
operator|=
operator|new
name|RepositoryURL
argument_list|(
name|model
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|model
operator|.
name|getId
argument_list|()
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|RepositoryURL
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|model
operator|.
name|setUrl
argument_list|(
name|url
operator|.
name|getUrl
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|RepositoryURL
name|getUrl
parameter_list|()
block|{
return|return
name|this
operator|.
name|url
return|;
block|}
specifier|public
name|ArchivaRepositoryModel
name|getModel
parameter_list|()
block|{
return|return
name|this
operator|.
name|model
return|;
block|}
specifier|public
name|boolean
name|isBlacklisted
parameter_list|()
block|{
return|return
name|blacklisted
return|;
block|}
specifier|public
name|void
name|setBlacklisted
parameter_list|(
name|boolean
name|blacklisted
parameter_list|)
block|{
name|this
operator|.
name|blacklisted
operator|=
name|blacklisted
expr_stmt|;
block|}
comment|//    public ArtifactRepositoryPolicy getReleases()
comment|//    {
comment|//        return releases;
comment|//    }
comment|//
comment|//    public void setReleases( ArtifactRepositoryPolicy releases )
comment|//    {
comment|//        this.releases = releases;
comment|//    }
comment|//
comment|//    public ArtifactRepositoryPolicy getSnapshots()
comment|//    {
comment|//        return snapshots;
comment|//    }
comment|//
comment|//    public void setSnapshots( ArtifactRepositoryPolicy snapshots )
comment|//    {
comment|//        this.snapshots = snapshots;
comment|//    }
specifier|public
name|boolean
name|isRemote
parameter_list|()
block|{
return|return
name|this
operator|.
name|url
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
literal|"file"
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isManaged
parameter_list|()
block|{
return|return
name|this
operator|.
name|url
operator|.
name|getProtocol
argument_list|()
operator|.
name|equals
argument_list|(
literal|"file"
argument_list|)
return|;
block|}
block|}
end_class

end_unit

