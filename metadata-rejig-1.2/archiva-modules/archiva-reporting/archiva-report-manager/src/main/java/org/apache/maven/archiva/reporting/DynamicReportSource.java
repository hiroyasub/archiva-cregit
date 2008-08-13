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
name|reporting
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
name|maven
operator|.
name|archiva
operator|.
name|database
operator|.
name|ArchivaDatabaseException
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
name|database
operator|.
name|ObjectNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|List
import|;
end_import

begin_comment
comment|/**  * DynamicReportSource   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|DynamicReportSource
block|{
comment|/**      * The human readable name of this report.      *       * @return the name of the report.      */
specifier|public
name|String
name|getName
parameter_list|()
function_decl|;
comment|/**      * Get the entire list of values for this report.      *       * @return the complete List of objects for this report.      * @throws ArchivaDatabaseException if there was a fundamental issue with accessing the database.      * @throws ObjectNotFoundException  if no records were found.      */
specifier|public
name|List
name|getData
parameter_list|()
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
function_decl|;
comment|/**      * Get the entire list of values for this report.      *       * @param limits the limits on the data to fetch. (NOTE: This object is       * updated by the underlying implementation of this interface with      * the current values appropriate for the limits object).      * @return the complete List of objects for this report.      * @throws ArchivaDatabaseException if there was a fundamental issue with accessing the database.      * @throws ObjectNotFoundException  if no records were found.      */
specifier|public
name|List
name|getData
parameter_list|(
name|DataLimits
name|limits
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
function_decl|;
block|}
end_interface

end_unit

