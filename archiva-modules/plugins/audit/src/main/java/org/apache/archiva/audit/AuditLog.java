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
name|audit
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

begin_comment
comment|/**  * AuditLog - Audit Log.  *   *  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"auditListener#logging"
argument_list|)
specifier|public
class|class
name|AuditLog
implements|implements
name|AuditListener
block|{
specifier|public
specifier|static
specifier|final
name|Logger
name|logger
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
literal|"org.apache.archiva.AuditLog"
argument_list|)
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|NONE
init|=
literal|"-"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|char
name|DELIM
init|=
literal|' '
decl_stmt|;
comment|/**      * Creates a log message in the following format ...      * "{repository_id} {user_id} {remote_ip} \"{resource}\" \"{action}\""      */
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
name|StringBuilder
name|msg
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|checkNull
argument_list|(
name|event
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|DELIM
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|event
operator|.
name|getUserId
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
name|DELIM
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
name|checkNull
argument_list|(
name|event
operator|.
name|getRemoteIP
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|DELIM
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
operator|.
name|append
argument_list|(
name|checkNull
argument_list|(
name|event
operator|.
name|getResource
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
operator|.
name|append
argument_list|(
name|DELIM
argument_list|)
expr_stmt|;
name|msg
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
operator|.
name|append
argument_list|(
name|event
operator|.
name|getAction
argument_list|()
argument_list|)
operator|.
name|append
argument_list|(
literal|'\"'
argument_list|)
expr_stmt|;
name|logger
operator|.
name|info
argument_list|(
name|msg
operator|.
name|toString
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|checkNull
parameter_list|(
name|String
name|s
parameter_list|)
block|{
return|return
name|s
operator|!=
literal|null
condition|?
name|s
else|:
name|NONE
return|;
block|}
block|}
end_class

end_unit

