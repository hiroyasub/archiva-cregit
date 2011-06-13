begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|memory
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|RepositorySession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|RepositorySessionFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_class
annotation|@
name|Service
argument_list|(
literal|"repositorySessionFactory#test"
argument_list|)
specifier|public
class|class
name|TestRepositorySessionFactory
implements|implements
name|RepositorySessionFactory
block|{
specifier|private
name|RepositorySession
name|repositorySession
decl_stmt|;
specifier|public
name|void
name|setRepositorySession
parameter_list|(
name|RepositorySession
name|repositorySession
parameter_list|)
block|{
name|this
operator|.
name|repositorySession
operator|=
name|repositorySession
expr_stmt|;
block|}
specifier|public
name|RepositorySession
name|createSession
parameter_list|()
block|{
return|return
name|repositorySession
operator|!=
literal|null
condition|?
name|repositorySession
else|:
operator|new
name|RepositorySession
argument_list|(
operator|new
name|TestMetadataRepository
argument_list|()
argument_list|,
operator|new
name|TestMetadataResolver
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

