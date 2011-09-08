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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|repository
operator|.
name|RepositoryAdminException
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
name|repository
operator|.
name|admin
operator|.
name|ArchivaAdministration
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
name|web
operator|.
name|action
operator|.
name|AbstractActionSupport
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
name|annotation
operator|.
name|Scope
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
name|Controller
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
comment|/**  * Delete a LegacyArtifactPath to archiva configuration  *  * @since 1.1  */
end_comment

begin_class
annotation|@
name|Controller
argument_list|(
literal|"deleteLegacyArtifactPathAction"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|DeleteLegacyArtifactPathAction
extends|extends
name|AbstractActionSupport
block|{
annotation|@
name|Inject
specifier|private
name|ArchivaAdministration
name|archivaAdministration
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
name|log
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
try|try
block|{
name|getArchivaAdministration
argument_list|()
operator|.
name|deleteLegacyArtifactPath
argument_list|(
name|path
argument_list|,
name|getAuditInformation
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|RepositoryAdminException
name|e
parameter_list|)
block|{
name|log
operator|.
name|error
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|addActionError
argument_list|(
literal|"Exception during delete "
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
specifier|public
name|ArchivaAdministration
name|getArchivaAdministration
parameter_list|()
block|{
return|return
name|archivaAdministration
return|;
block|}
specifier|public
name|void
name|setArchivaAdministration
parameter_list|(
name|ArchivaAdministration
name|archivaAdministration
parameter_list|)
block|{
name|this
operator|.
name|archivaAdministration
operator|=
name|archivaAdministration
expr_stmt|;
block|}
block|}
end_class

end_unit

