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
name|checksum
operator|.
name|ChecksumAlgorithm
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
name|checksum
operator|.
name|ChecksummedFile
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
name|configuration
operator|.
name|ArchivaConfiguration
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
name|configuration
operator|.
name|ConfigurationNames
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
name|configuration
operator|.
name|FileTypes
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
name|registry
operator|.
name|Registry
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
name|registry
operator|.
name|RegistryListener
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
comment|/**  * ArtifactMissingChecksumsConsumer - Create missing and/or fix invalid checksums for the artifact.  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"knownRepositoryContentConsumer#create-missing-checksums"
argument_list|)
annotation|@
name|Scope
argument_list|(
literal|"prototype"
argument_list|)
specifier|public
class|class
name|ArtifactMissingChecksumsConsumer
extends|extends
name|AbstractMonitoredConsumer
implements|implements
name|KnownRepositoryContentConsumer
implements|,
name|RegistryListener
block|{
specifier|private
name|String
name|id
init|=
literal|"create-missing-checksums"
decl_stmt|;
specifier|private
name|String
name|description
init|=
literal|"Create Missing and/or Fix Invalid Checksums (.sha1, .md5)"
decl_stmt|;
specifier|private
name|ArchivaConfiguration
name|configuration
decl_stmt|;
specifier|private
name|FileTypes
name|filetypes
decl_stmt|;
specifier|private
name|ChecksummedFile
name|checksum
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_CHECKSUM_NOT_FILE
init|=
literal|"checksum-bad-not-file"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_CHECKSUM_CANNOT_CALC
init|=
literal|"checksum-calc-failure"
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|TYPE_CHECKSUM_CANNOT_CREATE
init|=
literal|"checksum-create-failure"
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
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
literal|0
argument_list|)
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|ArtifactMissingChecksumsConsumer
parameter_list|(
name|ArchivaConfiguration
name|configuration
parameter_list|,
name|FileTypes
name|filetypes
parameter_list|)
block|{
name|this
operator|.
name|configuration
operator|=
name|configuration
expr_stmt|;
name|this
operator|.
name|filetypes
operator|=
name|filetypes
expr_stmt|;
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|initIncludes
argument_list|()
expr_stmt|;
block|}
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
name|repo
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
name|repo
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
name|repo
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
name|repo
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
comment|/* do nothing */
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
name|getDefaultArtifactExclusions
argument_list|()
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
name|createFixChecksum
argument_list|(
name|path
argument_list|,
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|ChecksumAlgorithm
operator|.
name|SHA1
block|}
argument_list|)
expr_stmt|;
name|createFixChecksum
argument_list|(
name|path
argument_list|,
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|ChecksumAlgorithm
operator|.
name|MD5
block|}
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|processFile
parameter_list|(
name|String
name|path
parameter_list|,
name|boolean
name|executeOnEntireRepo
parameter_list|)
throws|throws
name|ConsumerException
block|{
name|processFile
argument_list|(
name|path
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|createFixChecksum
parameter_list|(
name|String
name|path
parameter_list|,
name|ChecksumAlgorithm
name|checksumAlgorithm
index|[]
parameter_list|)
block|{
name|File
name|artifactFile
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
operator|+
name|checksumAlgorithm
index|[
literal|0
index|]
operator|.
name|getExt
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|checksumFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|checksum
operator|=
operator|new
name|ChecksummedFile
argument_list|(
name|artifactFile
argument_list|)
expr_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|checksum
operator|.
name|isValidChecksum
argument_list|(
name|checksumAlgorithm
index|[
literal|0
index|]
argument_list|)
condition|)
block|{
name|checksum
operator|.
name|fixChecksums
argument_list|(
name|checksumAlgorithm
argument_list|)
expr_stmt|;
name|triggerConsumerInfo
argument_list|(
literal|"Fixed checksum file "
operator|+
name|checksumFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|triggerConsumerError
argument_list|(
name|TYPE_CHECKSUM_CANNOT_CALC
argument_list|,
literal|"Cannot calculate checksum for file "
operator|+
name|checksumFile
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
operator|!
name|checksumFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|checksum
operator|=
operator|new
name|ChecksummedFile
argument_list|(
name|artifactFile
argument_list|)
expr_stmt|;
try|try
block|{
name|checksum
operator|.
name|createChecksum
argument_list|(
name|checksumAlgorithm
index|[
literal|0
index|]
argument_list|)
expr_stmt|;
name|triggerConsumerInfo
argument_list|(
literal|"Created missing checksum file "
operator|+
name|checksumFile
operator|.
name|getAbsolutePath
argument_list|()
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
name|TYPE_CHECKSUM_CANNOT_CREATE
argument_list|,
literal|"Cannot create checksum for file "
operator|+
name|checksumFile
operator|+
literal|": "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|triggerConsumerWarning
argument_list|(
name|TYPE_CHECKSUM_NOT_FILE
argument_list|,
literal|"Checksum file "
operator|+
name|checksumFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" is not a file."
argument_list|)
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|afterConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
if|if
condition|(
name|ConfigurationNames
operator|.
name|isRepositoryScanning
argument_list|(
name|propertyName
argument_list|)
condition|)
block|{
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|void
name|beforeConfigurationChange
parameter_list|(
name|Registry
name|registry
parameter_list|,
name|String
name|propertyName
parameter_list|,
name|Object
name|propertyValue
parameter_list|)
block|{
comment|/* do nothing */
block|}
specifier|private
name|void
name|initIncludes
parameter_list|()
block|{
name|includes
operator|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|(
name|filetypes
operator|.
name|getFileTypePatterns
argument_list|(
name|FileTypes
operator|.
name|ARTIFACTS
argument_list|)
argument_list|)
expr_stmt|;
block|}
annotation|@
name|PostConstruct
specifier|public
name|void
name|initialize
parameter_list|()
block|{
name|configuration
operator|.
name|addChangeListener
argument_list|(
name|this
argument_list|)
expr_stmt|;
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

