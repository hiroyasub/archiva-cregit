begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|scheduler
operator|.
name|database
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
comment|/**  * DataRefreshTask - task for discovering changes in the repository   * and updating all associated data.   *  * @version $Id: DataRefreshTask.java 525176 2007-04-03 15:21:33Z joakime $  */
end_comment

begin_class
specifier|public
class|class
name|DatabaseTask
implements|implements
name|Task
block|{
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"DatabaseTask"
return|;
block|}
specifier|public
name|long
name|getMaxExecutionTime
parameter_list|()
block|{
return|return
literal|0
return|;
block|}
block|}
end_class

end_unit

