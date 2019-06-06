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
name|List
import|;
end_import

begin_comment
comment|/**  * Class RepositoryGroupConfiguration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|RepositoryGroupConfiguration
implements|implements
name|Serializable
block|{
comment|//--------------------------/
comment|//- Class/Member Variables -/
comment|//--------------------------/
comment|/**      * The id of the repository group.      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * The name of the repository group      */
specifier|private
name|String
name|name
decl_stmt|;
comment|/**      *      *             The repository type. Currently only MAVEN type      * is known.      *      */
specifier|private
name|String
name|type
init|=
literal|"MAVEN"
decl_stmt|;
comment|/**      * The path of the merged index.      */
specifier|private
name|String
name|mergedIndexPath
init|=
literal|".indexer"
decl_stmt|;
comment|/**      * The time to live of the merged index of the repository group.      */
specifier|private
name|int
name|mergedIndexTtl
init|=
literal|30
decl_stmt|;
comment|/**      *       *           When to run the index merging for this group.      *           No default value.      *                 */
specifier|private
name|String
name|cronExpression
init|=
literal|""
decl_stmt|;
comment|/**      * Field repositories.      */
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|repositories
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Method addRepository.      *       * @param string      */
specifier|public
name|void
name|addRepository
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getRepositories
argument_list|()
operator|.
name|add
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|//-- void addRepository( String )
comment|/**      * Get when to run the index merging for this group.      *           No default value.      *       * @return String      */
specifier|public
name|String
name|getCronExpression
parameter_list|()
block|{
return|return
name|this
operator|.
name|cronExpression
return|;
block|}
comment|//-- String getCronExpression()
comment|/**      * Get the id of the repository group.      *       * @return String      */
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
comment|//-- String getId()
comment|/**      * Get the path of the merged index.      *       * @return String      */
specifier|public
name|String
name|getMergedIndexPath
parameter_list|()
block|{
return|return
name|this
operator|.
name|mergedIndexPath
return|;
block|}
comment|//-- String getMergedIndexPath()
comment|/**      * Get the time to live of the merged index of the repository      * group.      *       * @return int      */
specifier|public
name|int
name|getMergedIndexTtl
parameter_list|()
block|{
return|return
name|this
operator|.
name|mergedIndexTtl
return|;
block|}
comment|//-- int getMergedIndexTtl()
comment|/**      * Method getRepositories.      *       * @return List      */
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getRepositories
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|repositories
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|repositories
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|repositories
return|;
block|}
comment|//-- java.util.List<String> getRepositories()
comment|/**      * Method removeRepository.      *       * @param string      */
specifier|public
name|void
name|removeRepository
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getRepositories
argument_list|()
operator|.
name|remove
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|//-- void removeRepository( String )
comment|/**      * Set when to run the index merging for this group.      *           No default value.      *       * @param cronExpression      */
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
comment|//-- void setCronExpression( String )
comment|/**      * Set the id of the repository group.      *       * @param id      */
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
comment|//-- void setId( String )
comment|/**      * Set the path of the merged index.      *       * @param mergedIndexPath      */
specifier|public
name|void
name|setMergedIndexPath
parameter_list|(
name|String
name|mergedIndexPath
parameter_list|)
block|{
name|this
operator|.
name|mergedIndexPath
operator|=
name|mergedIndexPath
expr_stmt|;
block|}
comment|//-- void setMergedIndexPath( String )
comment|/**      * Set the time to live of the merged index of the repository      * group.      *       * @param mergedIndexTtl      */
specifier|public
name|void
name|setMergedIndexTtl
parameter_list|(
name|int
name|mergedIndexTtl
parameter_list|)
block|{
name|this
operator|.
name|mergedIndexTtl
operator|=
name|mergedIndexTtl
expr_stmt|;
block|}
comment|//-- void setMergedIndexTtl( int )
comment|/**      * Set the list of repository ids under the group.      *       * @param repositories      */
specifier|public
name|void
name|setRepositories
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|repositories
parameter_list|)
block|{
name|this
operator|.
name|repositories
operator|=
name|repositories
expr_stmt|;
block|}
comment|//-- void setRepositories( java.util.List )
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
block|}
end_class

end_unit

