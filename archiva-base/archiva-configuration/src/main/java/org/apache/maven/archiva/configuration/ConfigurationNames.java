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
name|configuration
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Utility methods for testing the configuration property name.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ConfigurationNames
block|{
specifier|public
specifier|static
name|boolean
name|isNetworkProxy
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|startsWith
argument_list|(
literal|"networkProxies."
argument_list|,
name|propertyName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isRepositoryScanning
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|startsWith
argument_list|(
literal|"repositoryScanning."
argument_list|,
name|propertyName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isRepositories
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|startsWith
argument_list|(
literal|"repositories."
argument_list|,
name|propertyName
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isProxyConnector
parameter_list|(
name|String
name|propertyName
parameter_list|)
block|{
return|return
name|startsWith
argument_list|(
literal|"proxyConnectors."
argument_list|,
name|propertyName
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|boolean
name|startsWith
parameter_list|(
name|String
name|prefix
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|name
operator|==
literal|null
condition|)
block|{
return|return
literal|false
return|;
block|}
if|if
condition|(
name|name
operator|.
name|length
argument_list|()
operator|<=
literal|0
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
name|name
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
return|;
block|}
block|}
end_class

end_unit

