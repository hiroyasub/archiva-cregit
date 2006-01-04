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
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0    *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
comment|/**  *  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractCompoundQuery
block|{
specifier|protected
name|List
name|queries
decl_stmt|;
specifier|public
name|AbstractCompoundQuery
parameter_list|()
block|{
name|queries
operator|=
operator|new
name|ArrayList
argument_list|()
expr_stmt|;
block|}
specifier|public
name|void
name|add
parameter_list|(
name|Query
name|query
parameter_list|)
block|{
name|queries
operator|.
name|add
argument_list|(
name|query
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
name|getQueryList
parameter_list|()
block|{
return|return
name|queries
return|;
block|}
block|}
end_class

end_unit

