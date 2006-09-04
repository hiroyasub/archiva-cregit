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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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
name|indexer
operator|.
name|query
operator|.
name|Query
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
name|record
operator|.
name|RepositoryIndexRecordFactory
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
comment|/**  * Maintain an artifact index on the repository.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  */
end_comment

begin_interface
specifier|public
interface|interface
name|RepositoryArtifactIndex
block|{
comment|/**      * Indexes the artifacts found within the specified list of index records. If the artifacts are already in the      * repository they are updated.      *      * @param records the records to index      * @throws RepositoryIndexException if there is a problem indexing the records      */
name|void
name|indexRecords
parameter_list|(
name|Collection
name|records
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
comment|/**      * Retrieve all records in the index.      *      * @return the records      * @throws RepositoryIndexSearchException if there was an error searching the index      */
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
comment|/**      * Indexes the artifacts found within the specified list. If the artifacts are already in the      * repository they are updated. This method should use less memory than indexRecords as the records can be      * created and disposed of on the fly.      *      * @param artifacts the artifacts to index      * @param factory   the artifact to record factory      * @throws RepositoryIndexException if there is a problem indexing the artifacts      */
name|void
name|indexArtifacts
parameter_list|(
name|List
name|artifacts
parameter_list|,
name|RepositoryIndexRecordFactory
name|factory
parameter_list|)
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Get all the group IDs in the index.      *      * @return list of groups as strings      * @throws RepositoryIndexException if there is a problem searching for the group ID      */
name|List
name|getAllGroupIds
parameter_list|()
throws|throws
name|RepositoryIndexException
function_decl|;
comment|/**      * Get the list of artifact IDs in a group in the index.      *      * @param groupId the group ID to search      * @return the list of artifact ID strings      * @throws RepositoryIndexSearchException if there is a problem searching for the group ID      */
name|List
name|getArtifactIds
parameter_list|(
name|String
name|groupId
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
function_decl|;
comment|/**      * Get the list of available versions for a given artifact.      *      * @param groupId    the group ID to search for      * @param artifactId the artifact ID to search for      * @return the list of version strings      * @throws RepositoryIndexSearchException if there is a problem searching for the artifact      */
name|List
name|getVersions
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|)
throws|throws
name|RepositoryIndexSearchException
function_decl|;
comment|/**      * Get the time when the index was last updated. Note that this does not monitor external processes or multiple      * instances of the index.      *      * @return the last updated time, or 0 if it has not been updated since the class was instantiated.      */
name|long
name|getLastUpdatedTime
parameter_list|()
function_decl|;
block|}
end_interface

end_unit

