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

begin_comment
comment|/**  * Auditable   *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|Auditable
block|{
comment|/**      * Add an AuditListener.      *       * @param the listener to add.      */
specifier|public
name|void
name|addAuditListener
parameter_list|(
name|AuditListener
name|auditListener
parameter_list|)
function_decl|;
comment|/**      * Remove an AuditListener.      *       * @param the listener to remove.      */
specifier|public
name|void
name|removeAuditListener
parameter_list|(
name|AuditListener
name|auditListener
parameter_list|)
function_decl|;
comment|/**      * Remove all registered {@link AuditListener} objects.      */
specifier|public
name|void
name|clearAuditListeners
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

