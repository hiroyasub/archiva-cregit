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
name|web
operator|.
name|action
operator|.
name|admin
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|RepositoryTaskScheduler
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|TaskExecutionException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|xwork
operator|.
name|action
operator|.
name|PlexusActionSupport
import|;
end_import

begin_comment
comment|/**  * Configures the application.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="runRepositoryTaskAction"  */
end_comment

begin_class
specifier|public
class|class
name|RunRepositoryTaskAction
extends|extends
name|PlexusActionSupport
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryTaskScheduler
name|taskScheduler
decl_stmt|;
specifier|public
name|String
name|runIndexer
parameter_list|()
throws|throws
name|TaskExecutionException
block|{
name|taskScheduler
operator|.
name|runIndexer
argument_list|()
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
block|}
end_class

end_unit

