begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|indexing
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * This class is the object type contained in the list returned by the DefaultRepositoryIndexSearcher  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryIndexSearchHit
block|{
specifier|private
name|Object
name|obj
decl_stmt|;
specifier|private
name|boolean
name|isHashMap
decl_stmt|;
specifier|private
name|boolean
name|isMetadata
decl_stmt|;
specifier|private
name|boolean
name|isModel
decl_stmt|;
comment|/**      * Class constructor      *      * @param isHashMap  indicates whether the object is a HashMap object      * @param isMetadata indicates whether the object is a RepositoryMetadata object      * @param isModel    indicates whether the object is a Model object      */
specifier|public
name|RepositoryIndexSearchHit
parameter_list|(
name|boolean
name|isHashMap
parameter_list|,
name|boolean
name|isMetadata
parameter_list|,
name|boolean
name|isModel
parameter_list|)
block|{
name|this
operator|.
name|isHashMap
operator|=
name|isHashMap
expr_stmt|;
name|this
operator|.
name|isMetadata
operator|=
name|isMetadata
expr_stmt|;
name|this
operator|.
name|isModel
operator|=
name|isModel
expr_stmt|;
block|}
comment|/**      * Getter method for obj variable      *      * @return the Object      */
specifier|public
name|Object
name|getObject
parameter_list|()
block|{
return|return
name|obj
return|;
block|}
comment|/**      * Setter method for obj variable      *      * @param obj      */
specifier|public
name|void
name|setObject
parameter_list|(
name|Object
name|obj
parameter_list|)
block|{
name|this
operator|.
name|obj
operator|=
name|obj
expr_stmt|;
block|}
comment|/**      * Method that indicates if the object is a HashMap      *      * @return boolean      */
specifier|public
name|boolean
name|isHashMap
parameter_list|()
block|{
return|return
name|isHashMap
return|;
block|}
comment|/**      * Method that indicates if the object is a RepositoryMetadata      *      * @return boolean      */
specifier|public
name|boolean
name|isMetadata
parameter_list|()
block|{
return|return
name|isMetadata
return|;
block|}
comment|/**      * Method that indicates if the object is a Model      *      * @return boolean      */
specifier|public
name|boolean
name|isModel
parameter_list|()
block|{
return|return
name|isModel
return|;
block|}
block|}
end_class

end_unit

