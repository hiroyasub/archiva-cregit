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
name|scheduled
operator|.
name|executors
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|Configuration
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
name|scheduled
operator|.
name|tasks
operator|.
name|RepositoryTask
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
name|PlexusTestCase
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
name|taskqueue
operator|.
name|execution
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
name|taskqueue
operator|.
name|execution
operator|.
name|TaskExecutor
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_comment
comment|/**  * IndexerTaskExecutorTest  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id:$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaScheduledTaskExecutorTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|TaskExecutor
name|taskExecutor
decl_stmt|;
specifier|protected
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|taskExecutor
operator|=
operator|(
name|TaskExecutor
operator|)
name|lookup
argument_list|(
name|TaskExecutor
operator|.
name|class
operator|.
name|getName
argument_list|()
argument_list|,
literal|"test-archiva-task-executor"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testExecutor
parameter_list|()
throws|throws
name|TaskExecutionException
block|{
name|taskExecutor
operator|.
name|executeTask
argument_list|(
operator|new
name|TestRepositoryTask
argument_list|()
argument_list|)
expr_stmt|;
block|}
class|class
name|TestRepositoryTask
extends|extends
name|RepositoryTask
block|{
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"TestRepositoryTask"
return|;
block|}
block|}
block|}
end_class

end_unit

