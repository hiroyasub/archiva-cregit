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
comment|/**  * ArtifactMissingChecksumsConsumer - Create missing and/or fix invalid checksums for the artifact.  *  *  */
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
comment|// it's prototype bean so we assume configuration won't change during a run
comment|//, RegistryListener
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ArtifactMissingChecksumsConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
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
argument_list|<>
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
comment|//configuration.addChangeListener( this );
name|initIncludes
argument_list|()
expr_stmt|;
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
specifier|public
name|void
name|completeScan
parameter_list|()
block|{
comment|/* do nothing */
block|}
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
annotation|@
name|Override
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
name|ChecksumAlgorithm
operator|.
name|SHA1
argument_list|)
expr_stmt|;
name|createFixChecksum
argument_list|(
name|path
argument_list|,
name|ChecksumAlgorithm
operator|.
name|MD5
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
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
operator|.
name|getExt
argument_list|()
argument_list|)
decl_stmt|;
comment|//+ "."
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
argument_list|)
condition|)
block|{
name|checksum
operator|.
name|fixChecksums
argument_list|(
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|checksumAlgorithm
block|}
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Fixed checksum file {}"
argument_list|,
name|checksumFile
operator|.
name|getAbsolutePath
argument_list|()
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
name|log
operator|.
name|error
argument_list|(
literal|"Cannot calculate checksum for file {} :"
argument_list|,
name|checksumFile
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
argument_list|)
expr_stmt|;
name|log
operator|.
name|info
argument_list|(
literal|"Created missing checksum file {}"
argument_list|,
name|checksumFile
operator|.
name|getAbsolutePath
argument_list|()
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
name|log
operator|.
name|error
argument_list|(
literal|"Cannot create checksum for file {} :"
argument_list|,
name|checksumFile
argument_list|,
name|e
argument_list|)
expr_stmt|;
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
name|log
operator|.
name|warn
argument_list|(
literal|"Checksum file {} is not a file. "
argument_list|,
name|checksumFile
operator|.
name|getAbsolutePath
argument_list|()
argument_list|)
expr_stmt|;
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
comment|/*     @Override     public void afterConfigurationChange( Registry registry, String propertyName, Object propertyValue )     {         if ( ConfigurationNames.isRepositoryScanning( propertyName ) )         {             initIncludes();         }     }       @Override     public void beforeConfigurationChange( Registry registry, String propertyName, Object propertyValue )     {         // do nothing     }      */
specifier|private
name|void
name|initIncludes
parameter_list|()
block|{
name|includes
operator|=
operator|new
name|ArrayList
argument_list|<>
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
comment|//configuration.addChangeListener( this );
name|initIncludes
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

