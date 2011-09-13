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
name|repository
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
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|managed
operator|.
name|ManagedRepository
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
name|admin
operator|.
name|model
operator|.
name|remote
operator|.
name|RemoteRepository
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
name|ManagedRepositoryConfiguration
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
name|RemoteRepositoryConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|Rule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TestName
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|RunWith
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|ApplicationContext
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|ContextConfiguration
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|test
operator|.
name|context
operator|.
name|junit4
operator|.
name|SpringJUnit4ClassRunner
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

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_comment
comment|/**  * AbstractRepositoryLayerTestCase  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|RunWith
argument_list|(
name|SpringJUnit4ClassRunner
operator|.
name|class
argument_list|)
annotation|@
name|ContextConfiguration
argument_list|(
name|locations
operator|=
block|{
literal|"classpath*:/META-INF/spring-context.xml"
block|,
literal|"classpath:/spring-context.xml"
block|}
argument_list|)
specifier|public
specifier|abstract
class|class
name|AbstractRepositoryLayerTestCase
block|{
annotation|@
name|Rule
specifier|public
name|TestName
name|name
init|=
operator|new
name|TestName
argument_list|()
decl_stmt|;
annotation|@
name|Inject
specifier|protected
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|protected
name|ManagedRepository
name|createRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|File
name|location
parameter_list|)
block|{
name|ManagedRepository
name|repo
init|=
operator|new
name|ManagedRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|location
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
specifier|protected
name|RemoteRepository
name|createRemoteRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|RemoteRepository
name|repo
init|=
operator|new
name|RemoteRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
return|return
name|repo
return|;
block|}
specifier|protected
name|ManagedRepositoryContent
name|createManagedRepositoryContent
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|File
name|location
parameter_list|,
name|String
name|layout
parameter_list|)
throws|throws
name|Exception
block|{
name|ManagedRepository
name|repo
init|=
operator|new
name|ManagedRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLocation
argument_list|(
name|location
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|ManagedRepositoryContent
name|repoContent
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"managedRepositoryContent#"
operator|+
name|layout
argument_list|,
name|ManagedRepositoryContent
operator|.
name|class
argument_list|)
decl_stmt|;
name|repoContent
operator|.
name|setRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
return|return
name|repoContent
return|;
block|}
specifier|protected
name|RemoteRepositoryContent
name|createRemoteRepositoryContent
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|url
parameter_list|,
name|String
name|layout
parameter_list|)
throws|throws
name|Exception
block|{
name|RemoteRepository
name|repo
init|=
operator|new
name|RemoteRepository
argument_list|()
decl_stmt|;
name|repo
operator|.
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setUrl
argument_list|(
name|url
argument_list|)
expr_stmt|;
name|repo
operator|.
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
name|RemoteRepositoryContent
name|repoContent
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
literal|"remoteRepositoryContent#"
operator|+
name|layout
argument_list|,
name|RemoteRepositoryContent
operator|.
name|class
argument_list|)
decl_stmt|;
name|repoContent
operator|.
name|setRepository
argument_list|(
name|repo
argument_list|)
expr_stmt|;
return|return
name|repoContent
return|;
block|}
block|}
end_class

end_unit

