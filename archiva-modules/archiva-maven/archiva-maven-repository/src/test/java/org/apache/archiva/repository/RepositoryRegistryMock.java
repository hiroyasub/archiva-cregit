begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|TreeMap
import|;
end_import

begin_class
specifier|public
class|class
name|RepositoryRegistryMock
extends|extends
name|RepositoryRegistry
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|ManagedRepository
argument_list|>
name|managedRepositories
init|=
operator|new
name|TreeMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
name|ManagedRepository
name|putRepository
parameter_list|(
name|ManagedRepository
name|managedRepository
parameter_list|)
throws|throws
name|RepositoryException
block|{
name|managedRepositories
operator|.
name|put
argument_list|(
name|managedRepository
operator|.
name|getId
argument_list|()
argument_list|,
name|managedRepository
argument_list|)
expr_stmt|;
return|return
name|managedRepository
return|;
block|}
annotation|@
name|Override
specifier|public
name|ManagedRepository
name|getManagedRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
return|return
name|managedRepositories
operator|.
name|get
argument_list|(
name|repoId
argument_list|)
return|;
block|}
annotation|@
name|Override
specifier|public
name|Repository
name|getRepository
parameter_list|(
name|String
name|repoId
parameter_list|)
block|{
if|if
condition|(
name|managedRepositories
operator|.
name|containsKey
argument_list|(
name|repoId
argument_list|)
condition|)
block|{
return|return
name|managedRepositories
operator|.
name|get
argument_list|(
name|repoId
argument_list|)
return|;
block|}
else|else
block|{
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

