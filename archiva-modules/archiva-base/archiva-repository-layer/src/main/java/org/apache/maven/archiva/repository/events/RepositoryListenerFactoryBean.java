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
operator|.
name|events
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
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
name|consumers
operator|.
name|InvalidRepositoryContentConsumer
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
name|BeansException
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
name|FactoryBean
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
name|context
operator|.
name|ApplicationContextAware
import|;
end_import

begin_comment
comment|/**  * @todo though we will eventually remove this altogether, an interim cleanup would be to genericise this  * and replace the calls in RepositoryContentConsumers with calls to the same thing  */
end_comment

begin_class
specifier|public
class|class
name|RepositoryListenerFactoryBean
implements|implements
name|FactoryBean
implements|,
name|ApplicationContextAware
block|{
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
specifier|public
name|void
name|setApplicationContext
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
throws|throws
name|BeansException
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
specifier|public
name|Object
name|getObject
parameter_list|()
throws|throws
name|Exception
block|{
return|return
name|applicationContext
operator|.
name|getBeansOfType
argument_list|(
name|RepositoryListener
operator|.
name|class
argument_list|)
operator|.
name|values
argument_list|()
return|;
block|}
specifier|public
name|Class
name|getObjectType
parameter_list|()
block|{
return|return
name|List
operator|.
name|class
return|;
block|}
specifier|public
name|boolean
name|isSingleton
parameter_list|()
block|{
return|return
literal|true
return|;
block|}
block|}
end_class

end_unit

