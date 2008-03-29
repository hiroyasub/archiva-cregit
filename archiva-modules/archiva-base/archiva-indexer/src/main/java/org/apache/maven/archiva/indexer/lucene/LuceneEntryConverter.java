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

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Document
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_comment
comment|/**  * A converter for {@link LuceneRepositoryContentRecord} to Lucene {@link Document} objects and back.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|LuceneEntryConverter
block|{
comment|/**      * Convert an index record to a Lucene document.      *      * @param record the record      * @return the document      */
name|Document
name|convert
parameter_list|(
name|LuceneRepositoryContentRecord
name|record
parameter_list|)
function_decl|;
comment|/**      * Convert a Lucene document to an index record.      *      * @param document the document      * @return the record      * @throws java.text.ParseException if there is a problem parsing a field (specifically, dates)      */
name|LuceneRepositoryContentRecord
name|convert
parameter_list|(
name|Document
name|document
parameter_list|)
throws|throws
name|ParseException
function_decl|;
block|}
end_interface

end_unit

