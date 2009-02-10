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
name|consumers
operator|.
name|lucene
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
name|consumers
operator|.
name|AbstractMonitoredConsumer
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
name|consumers
operator|.
name|ConsumerException
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
name|updater
operator|.
name|DatabaseCleanupConsumer
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
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepositoryContent
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
name|repository
operator|.
name|RepositoryContentFactory
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
name|repository
operator|.
name|RepositoryException
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
name|List
import|;
end_import

begin_comment
comment|/**  * LuceneCleanupRemoveIndexedConsumer  *   * @version $Id$  * @plexus.component role="org.apache.maven.archiva.database.updater.DatabaseCleanupConsumer"  *                   role-hint="not-present-remove-indexed" instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|LuceneCleanupRemoveIndexedConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|DatabaseCleanupConsumer
block|{
comment|/**      * @plexus.configuration default-value="not-present-remove-indexed"      */
specifier|private
name|String
name|id
decl_stmt|;
comment|/**      * @plexus.configuration default-value="Remove indexed content if not present on filesystem."      */
specifier|private
name|String
name|description
decl_stmt|;
comment|//
comment|//    /**
comment|//     * @plexus.requirement role-hint="lucene"
comment|//     */
comment|//    private RepositoryContentIndexFactory repoIndexFactory;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryContentFactory
name|repoFactory
decl_stmt|;
specifier|public
name|void
name|beginScan
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludedTypes
parameter_list|()
block|{
comment|// TODO Auto-generated method stub
return|return
literal|null
return|;
block|}
specifier|public
name|void
name|processArchivaArtifact
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
throws|throws
name|ConsumerException
block|{
try|try
block|{
name|ManagedRepositoryContent
name|repoContent
init|=
name|repoFactory
operator|.
name|getManagedRepositoryContent
argument_list|(
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repoContent
operator|.
name|getRepoRoot
argument_list|()
argument_list|,
name|repoContent
operator|.
name|toPath
argument_list|(
name|artifact
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|//                RepositoryContentIndex bytecodeIndex = repoIndexFactory.createBytecodeIndex( repoContent.getRepository() );
comment|//                RepositoryContentIndex hashcodesIndex = repoIndexFactory.createHashcodeIndex( repoContent.getRepository() );
comment|//                RepositoryContentIndex fileContentIndex =
comment|//                    repoIndexFactory.createFileContentIndex( repoContent.getRepository() );
comment|//                FileContentRecord fileContentRecord = new FileContentRecord();
comment|//                fileContentRecord.setFilename( repoContent.toPath( artifact ) );
comment|//                fileContentIndex.deleteRecord( fileContentRecord );
comment|//
comment|//                HashcodesRecord hashcodesRecord = new HashcodesRecord();
comment|//                hashcodesRecord.setArtifact( artifact );
comment|//                hashcodesIndex.deleteRecord( hashcodesRecord );
comment|//
comment|//                BytecodeRecord bytecodeRecord = new BytecodeRecord();
comment|//                bytecodeRecord.setArtifact( artifact );
comment|//                bytecodeIndex.deleteRecord( bytecodeRecord );
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryException
name|e
parameter_list|)
block|{
throw|throw
operator|new
name|ConsumerException
argument_list|(
literal|"Can't run index cleanup consumer: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
throw|;
block|}
comment|//        catch ( RepositoryIndexException e )
comment|//        {
comment|//            throw new ConsumerException( e.getMessage() );
comment|//        }
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|description
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|boolean
name|isPermanent
parameter_list|()
block|{
return|return
literal|false
return|;
block|}
specifier|public
name|void
name|setRepositoryContentFactory
parameter_list|(
name|RepositoryContentFactory
name|repoFactory
parameter_list|)
block|{
name|this
operator|.
name|repoFactory
operator|=
name|repoFactory
expr_stmt|;
block|}
block|}
end_class

end_unit

