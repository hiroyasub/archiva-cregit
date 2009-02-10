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
name|database
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
name|model
operator|.
name|ArchivaArtifact
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
comment|/**  * ArtifactDAO   *  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|ArtifactDAO
block|{
comment|/* NOTE TO ARCHIVA DEVELOPERS.      *       * Please keep this interface clean and lean.      * We don't want a repeat of the Continuum Store.      * You should have the following methods per object type ...      *       *   (Required Methods)      *       *    DatabaseObject .createDatabaseObject( Required Params ) ;      *    List           .queryDatabaseObject( Constraint )       throws ObjectNotFoundException, DatabaseException;      *    DatabaseObject .saveDatabaseObject( DatabaseObject )    throws DatabaseException;      *          *   (Optional Methods)      *         *    DatabaseObject .getDatabaseObject( Id )                 throws ObjectNotFoundException, DatabaseException;      *    List           .getDatabaseObjects()                    throws ObjectNotFoundException, DatabaseException;      *    void           .deleteDatabaseObject( DatabaseObject )  throws DatabaseException;      *          * This is the only list of options created in this DAO.      */
specifier|public
name|ArchivaArtifact
name|createArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|repositoryId
parameter_list|)
function_decl|;
specifier|public
name|ArchivaArtifact
name|getArtifact
parameter_list|(
name|String
name|groupId
parameter_list|,
name|String
name|artifactId
parameter_list|,
name|String
name|version
parameter_list|,
name|String
name|classifier
parameter_list|,
name|String
name|type
parameter_list|,
name|String
name|repositoryId
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
function_decl|;
specifier|public
name|List
comment|/*<ArchivaArtifact>*/
name|queryArtifacts
parameter_list|(
name|Constraint
name|constraint
parameter_list|)
throws|throws
name|ObjectNotFoundException
throws|,
name|ArchivaDatabaseException
function_decl|;
specifier|public
name|ArchivaArtifact
name|saveArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ArchivaDatabaseException
function_decl|;
specifier|public
name|void
name|deleteArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ArchivaDatabaseException
function_decl|;
block|}
end_interface

end_unit

