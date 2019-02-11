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
comment|/**  * Class AbstractRepositoryConfiguration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|AbstractRepositoryConfiguration
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
comment|/**      *       *             The repository identifier.      *                 */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      *       *             The repository type. Currently only MAVEN type      * is known.      *                 */
specifier|private
name|String
name|type
init|=
literal|"MAVEN"
decl_stmt|;
comment|/**      *       *             The descriptive name of the repository.      *                 */
specifier|private
name|String
name|name
decl_stmt|;
comment|/**      *       *             The layout of the repository. Valid values are      * "default" and "legacy".      *                 */
specifier|private
name|String
name|layout
init|=
literal|"default"
decl_stmt|;
comment|/**      *       *             The directory for the indexes of this      * repository.      *                 */
specifier|private
name|String
name|indexDir
init|=
literal|""
decl_stmt|;
comment|/**      *       *             The directory for the packed indexes of this      * repository.      *                 */
specifier|private
name|String
name|packedIndexDir
init|=
literal|""
decl_stmt|;
comment|/**      *       *             The description of this repository.      *                 */
specifier|private
name|String
name|description
init|=
literal|""
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Get the description of this repository.      *       * @return String      */
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
return|;
block|}
comment|//-- String getDescription()
comment|/**      * Get the repository identifier.      *       * @return String      */
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
comment|/**      * Get the directory for the indexes of this repository.      *       * @return String      */
specifier|public
name|String
name|getIndexDir
parameter_list|()
block|{
return|return
name|this
operator|.
name|indexDir
return|;
block|}
comment|//-- String getIndexDir()
comment|/**      * Get the layout of the repository. Valid values are "default"      * and "legacy".      *       * @return String      */
specifier|public
name|String
name|getLayout
parameter_list|()
block|{
return|return
name|this
operator|.
name|layout
return|;
block|}
comment|//-- String getLayout()
comment|/**      * Get the descriptive name of the repository.      *       * @return String      */
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|this
operator|.
name|name
return|;
block|}
comment|//-- String getName()
comment|/**      * Get the directory for the packed indexes of this repository.      *       * @return String      */
specifier|public
name|String
name|getPackedIndexDir
parameter_list|()
block|{
return|return
name|this
operator|.
name|packedIndexDir
return|;
block|}
comment|//-- String getPackedIndexDir()
comment|/**      * Get the repository type. Currently only MAVEN type is known.      *       * @return String      */
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|this
operator|.
name|type
return|;
block|}
comment|//-- String getType()
comment|/**      * Set the description of this repository.      *       * @param description      */
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|description
operator|=
name|description
expr_stmt|;
block|}
comment|//-- void setDescription( String )
comment|/**      * Set the repository identifier.      *       * @param id      */
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
comment|/**      * Set the directory for the indexes of this repository.      *       * @param indexDir      */
specifier|public
name|void
name|setIndexDir
parameter_list|(
name|String
name|indexDir
parameter_list|)
block|{
name|this
operator|.
name|indexDir
operator|=
name|indexDir
expr_stmt|;
block|}
comment|//-- void setIndexDir( String )
comment|/**      * Set the layout of the repository. Valid values are "default"      * and "legacy".      *       * @param layout      */
specifier|public
name|void
name|setLayout
parameter_list|(
name|String
name|layout
parameter_list|)
block|{
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
block|}
comment|//-- void setLayout( String )
comment|/**      * Set the descriptive name of the repository.      *       * @param name      */
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
comment|//-- void setName( String )
comment|/**      * Set the directory for the packed indexes of this repository.      *       * @param packedIndexDir      */
specifier|public
name|void
name|setPackedIndexDir
parameter_list|(
name|String
name|packedIndexDir
parameter_list|)
block|{
name|this
operator|.
name|packedIndexDir
operator|=
name|packedIndexDir
expr_stmt|;
block|}
comment|//-- void setPackedIndexDir( String )
comment|/**      * Set the repository type. Currently only MAVEN type is known.      *       * @param type      */
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
comment|//-- void setType( String )
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|17
decl_stmt|;
name|result
operator|=
literal|37
operator|*
name|result
operator|+
operator|(
name|id
operator|!=
literal|null
condition|?
name|id
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|other
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|other
operator|instanceof
name|AbstractRepositoryConfiguration
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AbstractRepositoryConfiguration
name|that
init|=
operator|(
name|AbstractRepositoryConfiguration
operator|)
name|other
decl_stmt|;
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|result
operator|=
name|result
operator|&&
operator|(
name|getId
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getId
argument_list|()
operator|==
literal|null
else|:
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getId
argument_list|()
argument_list|)
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
block|}
end_class

end_unit

