begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|mock
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
name|indexer
operator|.
name|ArchivaIndexManager
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
name|indexer
operator|.
name|ArchivaIndexingContext
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
name|indexer
operator|.
name|IndexCreationFailedException
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
name|indexer
operator|.
name|IndexUpdateFailedException
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
name|Repository
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
name|RepositoryType
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
name|java
operator|.
name|net
operator|.
name|URI
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

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaIndexManager#maven"
argument_list|)
specifier|public
class|class
name|ArchivaIndexManagerMock
implements|implements
name|ArchivaIndexManager
block|{
annotation|@
name|Override
specifier|public
name|void
name|pack
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|)
throws|throws
name|IndexUpdateFailedException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|scan
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|)
throws|throws
name|IndexUpdateFailedException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|update
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|,
name|boolean
name|fullUpdate
parameter_list|)
throws|throws
name|IndexUpdateFailedException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|addArtifactsToIndex
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|,
name|Collection
argument_list|<
name|URI
argument_list|>
name|artifactReference
parameter_list|)
throws|throws
name|IndexUpdateFailedException
block|{
block|}
annotation|@
name|Override
specifier|public
name|void
name|removeArtifactsFromIndex
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|,
name|Collection
argument_list|<
name|URI
argument_list|>
name|artifactReference
parameter_list|)
throws|throws
name|IndexUpdateFailedException
block|{
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|supportsRepository
parameter_list|(
name|RepositoryType
name|type
parameter_list|)
block|{
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|ArchivaIndexingContext
name|createContext
parameter_list|(
name|Repository
name|repository
parameter_list|)
throws|throws
name|IndexCreationFailedException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ArchivaIndexingContext
name|reset
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|)
throws|throws
name|IndexUpdateFailedException
block|{
return|return
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|ArchivaIndexingContext
name|move
parameter_list|(
name|ArchivaIndexingContext
name|context
parameter_list|,
name|Repository
name|repo
parameter_list|)
throws|throws
name|IndexCreationFailedException
block|{
return|return
literal|null
return|;
block|}
block|}
end_class

end_unit

