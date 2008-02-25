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
name|bytecode
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
name|configuration
operator|.
name|ManagedRepositoryConfiguration
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
name|AbstractIndexCreationTestCase
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
name|RepositoryContentIndex
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
name|RepositoryContentIndexFactory
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
name|LuceneIndexHandlers
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
name|model
operator|.
name|ArchivaArtifact
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
name|Map
import|;
end_import

begin_comment
comment|/**  * BytecodeIndexTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|BytecodeIndexTest
extends|extends
name|AbstractIndexCreationTestCase
block|{
specifier|public
name|String
name|getIndexName
parameter_list|()
block|{
return|return
literal|"bytecode"
return|;
block|}
specifier|public
name|LuceneIndexHandlers
name|getIndexHandler
parameter_list|()
block|{
return|return
operator|new
name|BytecodeHandlers
argument_list|()
return|;
block|}
specifier|public
name|RepositoryContentIndex
name|createIndex
parameter_list|(
name|RepositoryContentIndexFactory
name|indexFactory
parameter_list|,
name|ManagedRepositoryConfiguration
name|repository
parameter_list|)
block|{
return|return
name|indexFactory
operator|.
name|createBytecodeIndex
argument_list|(
name|repository
argument_list|)
return|;
block|}
specifier|protected
name|LuceneRepositoryContentRecord
name|createSimpleRecord
parameter_list|()
block|{
name|Map
name|dumps
init|=
name|getArchivaArtifactDumpMap
argument_list|()
decl_stmt|;
name|ArchivaArtifact
name|artifact
init|=
operator|(
name|ArchivaArtifact
operator|)
name|dumps
operator|.
name|get
argument_list|(
literal|"archiva-common"
argument_list|)
decl_stmt|;
name|File
name|dumpFile
init|=
name|getDumpFile
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|BytecodeRecord
name|record
init|=
name|BytecodeRecordLoader
operator|.
name|loadRecord
argument_list|(
name|dumpFile
argument_list|,
name|artifact
argument_list|)
decl_stmt|;
name|record
operator|.
name|setRepositoryId
argument_list|(
literal|"test-repo"
argument_list|)
expr_stmt|;
return|return
name|record
return|;
block|}
block|}
end_class

end_unit

