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
name|database
operator|.
name|browsing
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
name|collections
operator|.
name|CollectionUtils
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
comment|/**  * BrowsingResults   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|BrowsingResults
block|{
specifier|private
name|String
name|selectedGroupId
decl_stmt|;
specifier|private
name|String
name|selectedArtifactId
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepositoryIds
init|=
literal|null
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|groupIds
init|=
literal|null
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|artifacts
init|=
literal|null
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|versions
init|=
literal|null
decl_stmt|;
specifier|public
name|BrowsingResults
parameter_list|()
block|{
comment|/* do nothing, this is the results of the root */
block|}
specifier|public
name|BrowsingResults
parameter_list|(
name|String
name|groupId
parameter_list|)
block|{
name|this
operator|.
name|selectedGroupId
operator|=
name|groupId
expr_stmt|;
block|}
specifier|public
name|BrowsingResults
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
block|{
name|this
operator|.
name|selectedGroupId
operator|=
name|groupId
expr_stmt|;
name|this
operator|.
name|selectedArtifactId
operator|=
name|artifactId
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getArtifacts
parameter_list|()
block|{
return|return
name|artifacts
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getGroupIds
parameter_list|()
block|{
return|return
name|groupIds
return|;
block|}
specifier|public
name|String
name|getSelectedArtifactId
parameter_list|()
block|{
return|return
name|selectedArtifactId
return|;
block|}
specifier|public
name|String
name|getSelectedGroupId
parameter_list|()
block|{
return|return
name|selectedGroupId
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getVersions
parameter_list|()
block|{
return|return
name|versions
return|;
block|}
specifier|public
name|boolean
name|hasArtifacts
parameter_list|()
block|{
return|return
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|artifacts
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasGroupIds
parameter_list|()
block|{
return|return
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|groupIds
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|hasVersions
parameter_list|()
block|{
return|return
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|versions
argument_list|)
return|;
block|}
specifier|public
name|void
name|setArtifacts
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|artifacts
parameter_list|)
block|{
name|this
operator|.
name|artifacts
operator|=
name|artifacts
expr_stmt|;
block|}
specifier|public
name|void
name|setGroupIds
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|groupIds
parameter_list|)
block|{
name|this
operator|.
name|groupIds
operator|=
name|groupIds
expr_stmt|;
block|}
specifier|public
name|void
name|setVersions
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|versions
parameter_list|)
block|{
name|this
operator|.
name|versions
operator|=
name|versions
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getSelectedRepositoryIds
parameter_list|()
block|{
return|return
name|selectedRepositoryIds
return|;
block|}
specifier|public
name|void
name|setSelectedRepositoryIds
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|selectedRepositoryIds
parameter_list|)
block|{
name|this
operator|.
name|selectedRepositoryIds
operator|=
name|selectedRepositoryIds
expr_stmt|;
block|}
block|}
end_class

end_unit

