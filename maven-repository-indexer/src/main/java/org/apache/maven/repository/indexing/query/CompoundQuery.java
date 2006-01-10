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
comment|/**  * Class to hold multiple SinglePhraseQueries and/or other CompoundQueries.  *  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|CompoundQuery
implements|implements
name|Query
block|{
specifier|protected
name|List
name|queries
decl_stmt|;
comment|/**      * Class constructor      */
specifier|public
name|CompoundQuery
parameter_list|()
block|{
name|queries
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
comment|/**      * Appends a required Query object to this Query object. The Query object will be encapsulated inside an      * AndQueryTerm object.      *      * @param query the Query object to be appended to this Query object      */
specifier|public
name|void
name|and
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|queries
operator|.
name|add
argument_list|(
operator|new
name|AndQueryTerm
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends an optional Query object to this Query object. The Query object will be encapsulated inside an      * OrQueryTerm object.      *      * @param query the Query object to be appended to this Query object      */
specifier|public
name|void
name|or
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|queries
operator|.
name|add
argument_list|(
operator|new
name|OrQueryTerm
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Appends a prohibited Query object to this Query object. The Query object will be encapsulated inside an      * NotQueryTerm object.      *      * @param query the Query object to be appended to this Query object      */
specifier|public
name|void
name|not
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|queries
operator|.
name|add
argument_list|(
operator|new
name|NotQueryTerm
argument_list|(
name|query
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Method to get the List of Queries appended into this      *      * @return List of all Queries added to this Query      */
specifier|public
name|List
name|getQueries
parameter_list|()
block|{
return|return
name|queries
return|;
block|}
block|}
end_class

end_unit

