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
name|indexer
operator|.
name|query
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * Query for a single term.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|SingleTermQuery
implements|implements
name|Query
block|{
comment|/**      * The term to query for.      */
specifier|private
specifier|final
name|QueryTerm
name|term
decl_stmt|;
comment|/**      * Constructor.      *      * @param term the term to query      */
specifier|public
name|SingleTermQuery
parameter_list|(
name|QueryTerm
name|term
parameter_list|)
block|{
name|this
operator|.
name|term
operator|=
name|term
expr_stmt|;
block|}
comment|/**      * Shorthand constructor - create a single term query from a field and value      *      * @param field the field name      * @param value the value to check for      */
specifier|public
name|SingleTermQuery
parameter_list|(
name|String
name|field
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|term
operator|=
operator|new
name|QueryTerm
argument_list|(
name|field
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getField
parameter_list|()
block|{
return|return
name|term
operator|.
name|getField
argument_list|()
return|;
block|}
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|term
operator|.
name|getValue
argument_list|()
return|;
block|}
block|}
end_class

end_unit

