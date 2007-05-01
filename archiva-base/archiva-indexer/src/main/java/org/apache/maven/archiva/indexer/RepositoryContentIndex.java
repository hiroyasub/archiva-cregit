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
name|queryParser
operator|.
name|QueryParser
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
name|indexer
operator|.
name|lucene
operator|.
name|LuceneRepositoryContentRecord
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
name|indexer
operator|.
name|query
operator|.
name|Query
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collection
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
comment|/**  * Common access methods for a Repository Content index.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryContentIndex
block|{
comment|/**      * Indexes the records.      *      * @param records list of {@link LuceneRepositoryContentRecord} objects.      * @throws RepositoryIndexException if there is a problem indexing the records.      */
name|void
name|indexRecords
parameter_list|(
name|Collection
name|records
parameter_list|)
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Modify (potentially) existing records in the index.      *       * @param records the collection of {@link LuceneRepositoryContentRecord} objects to modify in the index.      * @throws RepositoryIndexException if there is a problem modifying the records.      */
specifier|public
name|void
name|modifyRecords
parameter_list|(
name|Collection
name|records
parameter_list|)
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Modify an existing (potential) record in the index.      *        * @param record the record to modify.      * @throws RepositoryIndexException if there is a problem modifying the record.      */
specifier|public
name|void
name|modifyRecord
parameter_list|(
name|LuceneRepositoryContentRecord
name|record
parameter_list|)
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Search the index based on the search criteria specified. Returns a list of index records.      *      * @param query The query that contains the search criteria      * @return the index records found      * @throws RepositoryIndexSearchException if there is a problem searching      * @todo should it return "SearchResult" instances that contain the index record and other search data (like score?)      */
name|List
name|search
parameter_list|(
name|Query
name|query
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
function_decl|;
comment|/**      * Check if the index already exists.      *      * @return true if the index already exists      * @throws RepositoryIndexException if the index location is not valid      */
name|boolean
name|exists
parameter_list|()
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Delete records from the index. Simply ignore the request any did not exist.      *      * @param records the records to delete      * @throws RepositoryIndexException if there is a problem removing the record      */
name|void
name|deleteRecords
parameter_list|(
name|Collection
name|records
parameter_list|)
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Retrieve all records in the index.      *      * @return the collection of {@link LuceneRepositoryContentRecord} objects.      * @throws RepositoryIndexSearchException if there was an error searching the index      */
name|Collection
name|getAllRecords
parameter_list|()
throws|throws
name|RepositoryIndexSearchException
function_decl|;
comment|/**      * Retrieve all primary keys of records in the index.      *      * @return the keys      * @throws RepositoryIndexException if there was an error searching the index      */
name|Collection
name|getAllRecordKeys
parameter_list|()
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Get the index directory.      *       * @return the index directory.      */
name|File
name|getIndexDirectory
parameter_list|()
function_decl|;
comment|/**      * Get the {@link QueryParser} appropriate for searches within this index.      *       * @return the query parser;      */
name|QueryParser
name|getQueryParser
parameter_list|()
function_decl|;
comment|/**      * Get the id of index.      *       * @return the id of index.      */
name|String
name|getId
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

