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
name|scheduler
operator|.
name|task
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|taskqueue
operator|.
name|Task
import|;
end_import

begin_comment
comment|/**  * Task for discovering changes in the repository and updating the index accordingly.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_class
specifier|public
class|class
name|IndexerTask
implements|implements
name|Task
block|{
specifier|private
name|String
name|jobName
decl_stmt|;
specifier|public
name|long
name|getMaxExecutionTime
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
specifier|public
name|String
name|getJobName
parameter_list|()
block|{
return|return
name|jobName
return|;
block|}
specifier|public
name|void
name|setJobName
parameter_list|(
name|String
name|jobName
parameter_list|)
block|{
name|this
operator|.
name|jobName
operator|=
name|jobName
expr_stmt|;
block|}
block|}
end_class

end_unit

