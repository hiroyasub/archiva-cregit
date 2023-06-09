begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|services
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|tags
operator|.
name|Tag
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
name|redback
operator|.
name|authorization
operator|.
name|RedbackAuthorization
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|ActionStatus
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
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|ValidationStatus
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|GET
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|Produces
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|QueryParam
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|ws
operator|.
name|rs
operator|.
name|core
operator|.
name|MediaType
import|;
end_import

begin_comment
comment|/**  * contains some "free" services (i18n)  *  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_interface
annotation|@
name|Path
argument_list|(
literal|"/commonServices/"
argument_list|)
annotation|@
name|Tag
argument_list|(
name|name
operator|=
literal|"ArchivaCommon"
argument_list|)
specifier|public
interface|interface
name|CommonServices
block|{
comment|/**      * will return properties available in org/apache/archiva/i18n/default.properties      * load default (en) then override with locale used so at least en are returned if no      * translation in the locale asked.      */
annotation|@
name|Path
argument_list|(
literal|"getI18nResources"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|TEXT_PLAIN
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noRestriction
operator|=
literal|true
argument_list|)
name|String
name|getI18nResources
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"locale"
argument_list|)
name|String
name|locale
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
comment|/**      * will return properties available in org/apache/archiva/i18n/default.properties      * load default (en) then override with locale used so at least en are returned if no      * translation in the locale asked.      * This method will add redback resources too. note Archva wins      */
annotation|@
name|Path
argument_list|(
literal|"getAllI18nResources"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|TEXT_PLAIN
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noRestriction
operator|=
literal|true
argument_list|)
name|String
name|getAllI18nResources
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"locale"
argument_list|)
name|String
name|locale
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
annotation|@
name|Path
argument_list|(
literal|"validateCronExpression"
argument_list|)
annotation|@
name|GET
annotation|@
name|Produces
argument_list|(
block|{
name|MediaType
operator|.
name|APPLICATION_JSON
block|,
name|MediaType
operator|.
name|APPLICATION_XML
block|}
argument_list|)
annotation|@
name|RedbackAuthorization
argument_list|(
name|noRestriction
operator|=
literal|true
argument_list|)
name|ValidationStatus
name|validateCronExpression
parameter_list|(
annotation|@
name|QueryParam
argument_list|(
literal|"cronExpression"
argument_list|)
name|String
name|cronExpression
parameter_list|)
throws|throws
name|ArchivaRestServiceException
function_decl|;
block|}
end_interface

end_unit

