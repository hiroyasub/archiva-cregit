begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|audit
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|model
operator|.
name|facets
operator|.
name|AuditEvent
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
name|MetadataRepositoryException
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
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|events
operator|.
name|AuditListener
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

begin_comment
comment|/**  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"auditListener#metadata"
argument_list|)
specifier|public
class|class
name|MetadataAuditListener
implements|implements
name|AuditListener
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|MetadataAuditListener
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      *      */
annotation|@
name|Inject
specifier|private
name|AuditManager
name|auditManager
decl_stmt|;
comment|/**      * FIXME: this could be multiple implementations and needs to be configured.      *  It also starts a separate session to the originator of the audit event that we may rather want to pass through.      */
annotation|@
name|Inject
specifier|private
name|RepositorySessionFactory
name|repositorySessionFactory
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|auditEvent
parameter_list|(
name|AuditEvent
name|event
parameter_list|)
block|{
comment|// for now we only log upload events, some of the others are quite noisy
if|if
condition|(
name|event
operator|.
name|getAction
argument_list|()
operator|.
name|equals
argument_list|(
name|AuditEvent
operator|.
name|CREATE_FILE
argument_list|)
operator|||
name|event
operator|.
name|getAction
argument_list|()
operator|.
name|equals
argument_list|(
name|AuditEvent
operator|.
name|UPLOAD_FILE
argument_list|)
operator|||
name|event
operator|.
name|getAction
argument_list|()
operator|.
name|equals
argument_list|(
name|AuditEvent
operator|.
name|MERGING_REPOSITORIES
argument_list|)
condition|)
block|{
name|RepositorySession
name|repositorySession
init|=
name|repositorySessionFactory
operator|.
name|createSession
argument_list|()
decl_stmt|;
try|try
block|{
name|auditManager
operator|.
name|addAuditEvent
argument_list|(
name|repositorySession
operator|.
name|getRepository
argument_list|()
argument_list|,
name|event
argument_list|)
expr_stmt|;
name|repositorySession
operator|.
name|save
argument_list|()
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|MetadataRepositoryException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to write audit event to repository: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
finally|finally
block|{
name|repositorySession
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
block|}
block|}
end_class

end_unit

