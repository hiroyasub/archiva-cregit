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
name|manager
operator|.
name|web
operator|.
name|action
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|com
operator|.
name|opensymphony
operator|.
name|xwork
operator|.
name|Action
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
name|repository
operator|.
name|manager
operator|.
name|web
operator|.
name|execution
operator|.
name|DiscovererExecution
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
name|repository
operator|.
name|manager
operator|.
name|web
operator|.
name|job
operator|.
name|DiscovererScheduler
import|;
end_import

begin_comment
comment|/**  * This is the Action class of index.jsp, which is the initial page of the web application.  * It invokes the DiscovererScheduler to set the DiscoverJob in the scheduler.  *  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="org.apache.maven.repository.manager.web.action.BaseAction"  */
end_comment

begin_class
specifier|public
class|class
name|BaseAction
implements|implements
name|Action
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|DiscovererExecution
name|execution
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|DiscovererScheduler
name|discovererScheduler
decl_stmt|;
comment|/**      * Method that executes the action      *      * @return a String that specifies if the action executed was a success or a failure      */
specifier|public
name|String
name|execute
parameter_list|()
block|{
try|try
block|{
name|execution
operator|.
name|executeDiscovererIfIndexDoesNotExist
argument_list|()
expr_stmt|;
name|discovererScheduler
operator|.
name|setSchedule
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
comment|// TODO: better exception handling!
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
return|return
name|ERROR
return|;
block|}
return|return
name|SUCCESS
return|;
block|}
block|}
end_class

end_unit

