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
comment|/**  * Term in a compound query.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|CompoundQueryTerm
block|{
name|boolean
name|isRequired
parameter_list|()
function_decl|;
name|boolean
name|isProhibited
parameter_list|()
function_decl|;
name|Query
name|getQuery
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

