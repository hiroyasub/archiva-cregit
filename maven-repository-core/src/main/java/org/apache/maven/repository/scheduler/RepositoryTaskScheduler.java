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
name|scheduler
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_comment
comment|/**  * The component that takes care of scheduling in the application.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryTaskScheduler
block|{
comment|/**      * The Plexus component role.      */
name|String
name|ROLE
init|=
name|RepositoryTaskScheduler
operator|.
name|class
operator|.
name|getName
argument_list|()
decl_stmt|;
name|void
name|runIndexer
parameter_list|()
throws|throws
name|TaskExecutionException
function_decl|;
block|}
end_interface

end_unit

