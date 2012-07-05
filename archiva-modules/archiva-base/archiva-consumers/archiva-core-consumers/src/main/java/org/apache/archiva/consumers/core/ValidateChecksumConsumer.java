begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|consumers
operator|.
name|core
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
name|admin
operator|.
name|model
operator|.
name|beans
operator|.
name|ManagedRepository
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
name|common
operator|.
name|plexusbridge
operator|.
name|DigesterUtils
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridge
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
name|common
operator|.
name|plexusbridge
operator|.
name|PlexusSisuBridgeException
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
name|archiva
operator|.
name|consumers
operator|.
name|KnownRepositoryContentConsumer
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|digest
operator|.
name|ChecksumFile
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|digest
operator|.
name|Digester
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|digest
operator|.
name|DigesterException
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|context
operator|.
name|annotation
operator|.
name|Scope
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
name|javax
operator|.
name|annotation
operator|.
name|PostConstruct
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
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
name|io
operator|.
name|FileNotFoundException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|ArrayList
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
comment|/**  * ValidateChecksumConsumer - validate the provided checksum against the file it represents.  *  *  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#validate-checksums"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|ValidateChecksumConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
block|{
specifier|private
specifier|static
specifier|final
name|String
name|NOT_VALID_CHECKSUM
init|=
literal|"checksum-not-valid"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CHECKSUM_NOT_FOUND
init|=
literal|"checksum-not-found"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CHECKSUM_DIGESTER_FAILURE
init|=
literal|"checksum-digester-failure"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|CHECKSUM_IO_ERROR
init|=
literal|"checksum-io-error"
decl_stmt|;
comment|/**      * default-value="validate-checksums"      */
specifier|private
name|String
name|id
init|=
literal|"validate-checksums"
decl_stmt|;
comment|/**      * default-value="Validate checksums against file."      */
specifier|private
name|String
name|description
init|=
literal|"Validate checksums against file."
decl_stmt|;
comment|/**      *      */
specifier|private
name|ChecksumFile
name|checksum
decl_stmt|;
comment|/**      *      */
specifier|private
name|List
argument_list|<
name|Digester
argument_list|>
name|allDigesters
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|PlexusSisuBridge
name|plexusSisuBridge
decl_stmt|;
annotation|@
name|Inject
specifier|private
name|DigesterUtils
name|digesterUtils
decl_stmt|;
specifier|private
name|File
name|repositoryDir
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
decl_stmt|;
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|this
operator|.
name|id
return|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
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
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|this
operator|.
name|repositoryDir
operator|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|beginScan
parameter_list|(
name|ManagedRepository
name|repository
parameter_list|,
name|Date
name|whenGathered
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|beginScan
argument_list|(
name|repository
argument_list|,
name|whenGathered
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* nothing to do */
block|}
specifier|public
name|void
name|completeScan
parameter_list|(
name|boolean
name|executeOnEntireRepo
parameter_list|)
block|{
name|completeScan
argument_list|()
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getExcludes
parameter_list|()
block|{
return|return
literal|null
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIncludes
parameter_list|()
block|{
return|return
name|this
operator|.
name|includes
return|;
block|}
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|this
operator|.
name|repositoryDir
argument_list|,
name|path
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|checksum
operator|.
name|isValidChecksum
argument_list|(
name|checksumFile
argument_list|)
condition|)
block|{
name|triggerConsumerWarning
argument_list|(
name|NOT_VALID_CHECKSUM
argument_list|,
literal|"The checksum for "
operator|+
name|checksumFile
operator|+
literal|" is invalid."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|CHECKSUM_NOT_FOUND
argument_list|,
literal|"File not found during checksum validation: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|CHECKSUM_DIGESTER_FAILURE
argument_list|,
literal|"Digester failure during checksum validation on "
operator|+
name|checksumFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|CHECKSUM_IO_ERROR
argument_list|,
literal|"Checksum I/O error during validation on "
operator|+
name|checksumFile
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|executeOnEntireReDpo
parameter_list|)
throws|throws
name|Exception
block|{
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
throws|throws
name|PlexusSisuBridgeException
block|{
name|checksum
operator|=
name|plexusSisuBridge
operator|.
name|lookup
argument_list|(
name|ChecksumFile
operator|.
name|class
argument_list|)
expr_stmt|;
name|List
argument_list|<
name|Digester
argument_list|>
name|allDigesters
init|=
operator|new
name|ArrayList
argument_list|<
name|Digester
argument_list|>
argument_list|(
name|digesterUtils
operator|.
name|getAllDigesters
argument_list|()
argument_list|)
decl_stmt|;
name|includes
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|allDigesters
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|Digester
name|digester
range|:
name|allDigesters
control|)
block|{
name|includes
operator|.
name|add
argument_list|(
literal|"**/*"
operator|+
name|digester
operator|.
name|getFilenameExtension
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

