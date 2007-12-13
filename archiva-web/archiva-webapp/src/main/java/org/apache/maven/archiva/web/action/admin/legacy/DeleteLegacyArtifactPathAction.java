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
operator|.
name|legacy
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
name|Iterator
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
name|configuration
operator|.
name|IndeterminateConfigurationException
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
name|LegacyArtifactPath
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
name|xwork
operator|.
name|action
operator|.
name|PlexusActionSupport
import|;
end_import

begin_comment
comment|/**  * Delete a LegacyArtifactPath to archiva configuration  *  *  * @since 1.1  * @plexus.component role="com.opensymphony.xwork.Action" role-hint="deleteLegacyArtifactPathAction"  */
end_comment

begin_class
specifier|public
class|class
name|DeleteLegacyArtifactPathAction
extends|extends
name|PlexusActionSupport
block|{
comment|/**      * @plexus.requirement      */
specifier|private
name|ArchivaConfiguration
name|archivaConfiguration
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
specifier|public
name|String
name|delete
parameter_list|()
block|{
name|getLogger
argument_list|()
operator|.
name|info
argument_list|(
literal|"remove ["
operator|+
name|path
operator|+
literal|"] from legacy artifact path resolution"
argument_list|)
expr_stmt|;
name|Configuration
name|configuration
init|=
name|archivaConfiguration
operator|.
name|getConfiguration
argument_list|()
decl_stmt|;
for|for
control|(
name|Iterator
name|iterator
init|=
name|configuration
operator|.
name|getLegacyArtifactPaths
argument_list|()
operator|.
name|iterator
argument_list|()
init|;
name|iterator
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|LegacyArtifactPath
name|legacyArtifactPath
init|=
operator|(
name|LegacyArtifactPath
operator|)
name|iterator
operator|.
name|next
argument_list|()
decl_stmt|;
if|if
condition|(
name|legacyArtifactPath
operator|.
name|match
argument_list|(
name|path
argument_list|)
condition|)
block|{
name|iterator
operator|.
name|remove
argument_list|()
expr_stmt|;
block|}
block|}
return|return
name|saveConfiguration
argument_list|(
name|configuration
argument_list|)
return|;
block|}
specifier|protected
name|String
name|saveConfiguration
parameter_list|(
name|Configuration
name|configuration
parameter_list|)
block|{
try|try
block|{
name|archivaConfiguration
operator|.
name|save
argument_list|(
name|configuration
argument_list|)
expr_stmt|;
name|addActionMessage
argument_list|(
literal|"Successfully saved configuration"
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IndeterminateConfigurationException
name|e
parameter_list|)
block|{
name|addActionError
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|INPUT
return|;
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
return|return
name|INPUT
return|;
block|}
return|return
name|SUCCESS
return|;
block|}
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
block|}
end_class

end_unit

