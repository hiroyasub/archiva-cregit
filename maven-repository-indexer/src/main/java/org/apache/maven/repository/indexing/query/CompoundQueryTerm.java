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
operator|.
name|query
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * Base of all query terms.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|CompoundQueryTerm
block|{
comment|/**      * The query to add to the compound query.      */
specifier|private
specifier|final
name|Query
name|query
decl_stmt|;
comment|/**      * Whether the term is required (an AND).      */
specifier|private
specifier|final
name|boolean
name|required
decl_stmt|;
comment|/**      * Whether the term is prohibited (a NOT).      */
specifier|private
specifier|final
name|boolean
name|prohibited
decl_stmt|;
comment|/**      * Class constructor      *      * @param query      the subquery to add      * @param required   whether the term is required (an AND)      * @param prohibited whether the term is prohibited (a NOT)      */
specifier|private
name|CompoundQueryTerm
parameter_list|(
name|Query
name|query
parameter_list|,
name|boolean
name|required
parameter_list|,
name|boolean
name|prohibited
parameter_list|)
block|{
name|this
operator|.
name|query
operator|=
name|query
expr_stmt|;
name|this
operator|.
name|prohibited
operator|=
name|prohibited
expr_stmt|;
name|this
operator|.
name|required
operator|=
name|required
expr_stmt|;
block|}
comment|/**      * Method to test if the Query is a search requirement      *      * @return true if this Query is a search requirement, otherwise returns false      */
specifier|public
name|boolean
name|isRequired
parameter_list|()
block|{
return|return
name|required
return|;
block|}
comment|/**      * Method to test if the Query is prohibited in the search result      *      * @return true if this Query is prohibited in the search result      */
specifier|public
name|boolean
name|isProhibited
parameter_list|()
block|{
return|return
name|prohibited
return|;
block|}
comment|/**      * The subquery to execute.      *      * @return the query      */
specifier|public
name|Query
name|getQuery
parameter_list|()
block|{
return|return
name|query
return|;
block|}
specifier|static
name|CompoundQueryTerm
name|and
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
return|return
operator|new
name|CompoundQueryTerm
argument_list|(
name|query
argument_list|,
literal|true
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|static
name|CompoundQueryTerm
name|or
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
return|return
operator|new
name|CompoundQueryTerm
argument_list|(
name|query
argument_list|,
literal|false
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|static
name|CompoundQueryTerm
name|not
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
return|return
operator|new
name|CompoundQueryTerm
argument_list|(
name|query
argument_list|,
literal|false
argument_list|,
literal|true
argument_list|)
return|;
block|}
block|}
end_class

end_unit

