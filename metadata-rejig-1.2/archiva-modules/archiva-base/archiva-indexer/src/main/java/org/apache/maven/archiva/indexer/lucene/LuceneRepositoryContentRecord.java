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
name|indexer
operator|.
name|lucene
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * A repository content index record.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|LuceneRepositoryContentRecord
block|{
comment|/**      * Get the primary key used to identify the record uniquely in the index.      *      * @return the primary key      */
specifier|public
name|String
name|getPrimaryKey
parameter_list|()
function_decl|;
comment|/**      * Get the repository that this record belongs to.      *       * @return the repository id for this record.      */
specifier|public
name|String
name|getRepositoryId
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

