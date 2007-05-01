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
name|web
operator|.
name|action
operator|.
name|admin
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
name|archiva
operator|.
name|configuration
operator|.
name|InvalidConfigurationException
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
name|rbac
operator|.
name|profile
operator|.
name|RoleProfileException
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
name|registry
operator|.
name|RegistryException
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
name|security
operator|.
name|rbac
operator|.
name|RbacManagerException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_comment
comment|/**  * SaveRepositoryAction   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="com.opensymphony.xwork.Action" role-hint="saveRepositoryAction"   */
end_comment

begin_class
specifier|public
class|class
name|SaveRepositoryAction
extends|extends
name|AbstractRepositoryAction
block|{
specifier|public
name|void
name|prepare
parameter_list|()
throws|throws
name|Exception
block|{
comment|/* nothing to do here */
block|}
specifier|public
name|String
name|save
parameter_list|()
block|{
name|String
name|mode
init|=
name|getMode
argument_list|()
decl_stmt|;
name|String
name|repoId
init|=
name|getRepository
argument_list|()
operator|.
name|getId
argument_list|()
decl_stmt|;
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"edit("
operator|+
name|mode
operator|+
literal|":"
operator|+
name|repoId
operator|+
literal|")"
argument_list|)
expr_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isBlank
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
condition|)
block|{
name|addFieldError
argument_list|(
literal|"id"
argument_list|,
literal|"A repository with a blank id cannot be saved."
argument_list|)
expr_stmt|;
return|return
name|SUCCESS
return|;
block|}
if|if
condition|(
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
literal|"edit"
argument_list|,
name|mode
argument_list|)
condition|)
block|{
name|removeRepository
argument_list|(
name|repoId
argument_list|)
expr_stmt|;
block|}
try|try
block|{
name|addRepository
argument_list|(
name|getRepository
argument_list|()
argument_list|)
expr_stmt|;
name|saveConfiguration
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"I/O Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RoleProfileException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Role Profile Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|InvalidConfigurationException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Invalid Configuration Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RbacManagerException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"RBAC Manager Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RegistryException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
literal|"Configuration Registry Exception: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|SUCCESS
return|;
block|}
block|}
end_class

end_unit

