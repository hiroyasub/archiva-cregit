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
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|beans
operator|.
name|factory
operator|.
name|config
operator|.
name|AbstractFactoryBean
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 2.0.2  */
end_comment

begin_class
specifier|public
class|class
name|RepositorySessionFactoryBean
extends|extends
name|AbstractFactoryBean
argument_list|<
name|RepositorySessionFactory
argument_list|>
block|{
specifier|private
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|BEAN_ID_SYS_PROPS
init|=
literal|"archiva.repositorySessionFactory.id"
decl_stmt|;
specifier|private
name|Properties
name|properties
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
specifier|public
name|RepositorySessionFactoryBean
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
comment|// we can override with system props
name|String
name|value
init|=
name|System
operator|.
name|getProperty
argument_list|(
name|BEAN_ID_SYS_PROPS
argument_list|)
decl_stmt|;
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|this
operator|.
name|properties
operator|.
name|put
argument_list|(
name|BEAN_ID_SYS_PROPS
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
name|id
operator|=
name|properties
operator|.
name|getProperty
argument_list|(
name|BEAN_ID_SYS_PROPS
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Class
argument_list|<
name|RepositorySessionFactory
argument_list|>
name|getObjectType
parameter_list|()
block|{
return|return
name|RepositorySessionFactory
operator|.
name|class
return|;
block|}
annotation|@
name|Override
specifier|protected
name|RepositorySessionFactory
name|createInstance
parameter_list|()
throws|throws
name|Exception
block|{
name|RepositorySessionFactory
name|repositorySessionFactory
init|=
name|getBeanFactory
argument_list|()
operator|.
name|getBean
argument_list|(
literal|"repositorySessionFactory#"
operator|+
name|id
argument_list|,
name|RepositorySessionFactory
operator|.
name|class
argument_list|)
decl_stmt|;
name|logger
operator|.
name|info
argument_list|(
literal|"create RepositorySessionFactory with id {} instance of {}"
argument_list|,
comment|//
name|id
argument_list|,
comment|//
name|repositorySessionFactory
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|repositorySessionFactory
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|Properties
name|getProperties
parameter_list|()
block|{
return|return
name|properties
return|;
block|}
specifier|public
name|void
name|setProperties
parameter_list|(
name|Properties
name|properties
parameter_list|)
block|{
name|this
operator|.
name|properties
operator|=
name|properties
expr_stmt|;
block|}
block|}
end_class

end_unit

