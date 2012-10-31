begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|proxy
operator|.
name|common
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
name|lang
operator|.
name|StringUtils
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
name|wagon
operator|.
name|Wagon
import|;
end_import

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
name|BeansException
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
name|stereotype
operator|.
name|Service
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

begin_import
import|import
name|java
operator|.
name|lang
operator|.
name|reflect
operator|.
name|Method
import|;
end_import

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
name|Properties
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"wagonFactory"
argument_list|)
specifier|public
class|class
name|DefaultWagonFactory
implements|implements
name|WagonFactory
block|{
specifier|private
name|ApplicationContext
name|applicationContext
decl_stmt|;
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
name|DebugTransferListener
name|debugTransferListener
init|=
operator|new
name|DebugTransferListener
argument_list|()
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|DefaultWagonFactory
parameter_list|(
name|ApplicationContext
name|applicationContext
parameter_list|)
block|{
name|this
operator|.
name|applicationContext
operator|=
name|applicationContext
expr_stmt|;
block|}
specifier|public
name|Wagon
name|getWagon
parameter_list|(
name|WagonFactoryRequest
name|wagonFactoryRequest
parameter_list|)
throws|throws
name|WagonFactoryException
block|{
try|try
block|{
name|String
name|protocol
init|=
name|StringUtils
operator|.
name|startsWith
argument_list|(
name|wagonFactoryRequest
operator|.
name|getProtocol
argument_list|()
argument_list|,
literal|"wagon#"
argument_list|)
condition|?
name|wagonFactoryRequest
operator|.
name|getProtocol
argument_list|()
else|:
literal|"wagon#"
operator|+
name|wagonFactoryRequest
operator|.
name|getProtocol
argument_list|()
decl_stmt|;
comment|// if it's a ntlm proxy we have to lookup the wagon light which support thats
comment|// wagon http client doesn't support that
if|if
condition|(
name|wagonFactoryRequest
operator|.
name|getNetworkProxy
argument_list|()
operator|!=
literal|null
operator|&&
name|wagonFactoryRequest
operator|.
name|getNetworkProxy
argument_list|()
operator|.
name|isUseNtlm
argument_list|()
condition|)
block|{
name|protocol
operator|=
name|protocol
operator|+
literal|"-ntlm"
expr_stmt|;
block|}
name|Wagon
name|wagon
init|=
name|applicationContext
operator|.
name|getBean
argument_list|(
name|protocol
argument_list|,
name|Wagon
operator|.
name|class
argument_list|)
decl_stmt|;
name|wagon
operator|.
name|addTransferListener
argument_list|(
name|debugTransferListener
argument_list|)
expr_stmt|;
name|configureUserAgent
argument_list|(
name|wagon
argument_list|,
name|wagonFactoryRequest
argument_list|)
expr_stmt|;
return|return
name|wagon
return|;
block|}
catch|catch
parameter_list|(
name|BeansException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|WagonFactoryException
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
throw|;
block|}
block|}
specifier|protected
name|void
name|configureUserAgent
parameter_list|(
name|Wagon
name|wagon
parameter_list|,
name|WagonFactoryRequest
name|wagonFactoryRequest
parameter_list|)
block|{
try|try
block|{
name|Class
name|clazz
init|=
name|wagon
operator|.
name|getClass
argument_list|()
decl_stmt|;
name|Method
name|getHttpHeaders
init|=
name|clazz
operator|.
name|getMethod
argument_list|(
literal|"getHttpHeaders"
argument_list|,
literal|null
argument_list|)
decl_stmt|;
name|Properties
name|headers
init|=
operator|(
name|Properties
operator|)
name|getHttpHeaders
operator|.
name|invoke
argument_list|(
name|wagon
argument_list|,
literal|null
argument_list|)
decl_stmt|;
if|if
condition|(
name|headers
operator|==
literal|null
condition|)
block|{
name|headers
operator|=
operator|new
name|Properties
argument_list|()
expr_stmt|;
block|}
name|headers
operator|.
name|put
argument_list|(
literal|"User-Agent"
argument_list|,
name|wagonFactoryRequest
operator|.
name|getUserAgent
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
operator|!
name|wagonFactoryRequest
operator|.
name|getHeaders
argument_list|()
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|entry
range|:
name|wagonFactoryRequest
operator|.
name|getHeaders
argument_list|()
operator|.
name|entrySet
argument_list|()
control|)
block|{
name|headers
operator|.
name|put
argument_list|(
name|entry
operator|.
name|getKey
argument_list|()
argument_list|,
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
name|Method
name|setHttpHeaders
init|=
name|clazz
operator|.
name|getMethod
argument_list|(
literal|"setHttpHeaders"
argument_list|,
operator|new
name|Class
index|[]
block|{
name|Properties
operator|.
name|class
block|}
argument_list|)
decl_stmt|;
name|setHttpHeaders
operator|.
name|invoke
argument_list|(
name|wagon
argument_list|,
name|headers
argument_list|)
expr_stmt|;
name|logger
operator|.
name|debug
argument_list|(
literal|"http headers set to: {}"
argument_list|,
name|headers
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Exception
name|e
parameter_list|)
block|{
name|logger
operator|.
name|warn
argument_list|(
literal|"fail to configure User-Agent: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

