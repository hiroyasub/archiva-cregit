begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|stagerepository
operator|.
name|merge
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryMergerException
extends|extends
name|Exception
block|{
specifier|public
name|RepositoryMergerException
parameter_list|(
name|String
name|msg
parameter_list|,
name|Exception
name|e
parameter_list|)
block|{
name|super
argument_list|(
name|msg
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

