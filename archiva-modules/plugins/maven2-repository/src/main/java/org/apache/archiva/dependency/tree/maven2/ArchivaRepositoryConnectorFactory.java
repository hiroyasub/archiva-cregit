begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|dependency
operator|.
name|tree
operator|.
name|maven2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|RepositorySystemSession
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|connector
operator|.
name|file
operator|.
name|FileRepositoryConnectorFactory
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|repository
operator|.
name|RemoteRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|spi
operator|.
name|connector
operator|.
name|ArtifactDownload
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|spi
operator|.
name|connector
operator|.
name|ArtifactUpload
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|spi
operator|.
name|connector
operator|.
name|MetadataDownload
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|spi
operator|.
name|connector
operator|.
name|MetadataUpload
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|spi
operator|.
name|connector
operator|.
name|RepositoryConnector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|sonatype
operator|.
name|aether
operator|.
name|transfer
operator|.
name|NoRepositoryConnectorException
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
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaRepositoryConnectorFactory
extends|extends
name|FileRepositoryConnectorFactory
block|{
specifier|public
name|ArchivaRepositoryConnectorFactory
parameter_list|()
block|{
comment|// no op but empty constructor needed by aether
block|}
specifier|public
name|RepositoryConnector
name|newInstance
parameter_list|(
name|RepositorySystemSession
name|session
parameter_list|,
name|RemoteRepository
name|repository
parameter_list|)
throws|throws
name|NoRepositoryConnectorException
block|{
try|try
block|{
return|return
name|super
operator|.
name|newInstance
argument_list|(
name|session
argument_list|,
name|repository
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|NoRepositoryConnectorException
name|e
parameter_list|)
block|{
block|}
return|return
operator|new
name|RepositoryConnector
argument_list|()
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|getClass
argument_list|()
argument_list|)
decl_stmt|;
specifier|public
name|void
name|get
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|ArtifactDownload
argument_list|>
name|artifactDownloads
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|MetadataDownload
argument_list|>
name|metadataDownloads
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"get"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|put
parameter_list|(
name|Collection
argument_list|<
name|?
extends|extends
name|ArtifactUpload
argument_list|>
name|artifactUploads
parameter_list|,
name|Collection
argument_list|<
name|?
extends|extends
name|MetadataUpload
argument_list|>
name|metadataUploads
parameter_list|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"put"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|close
parameter_list|()
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"close"
argument_list|)
expr_stmt|;
block|}
block|}
return|;
block|}
block|}
end_class

end_unit

