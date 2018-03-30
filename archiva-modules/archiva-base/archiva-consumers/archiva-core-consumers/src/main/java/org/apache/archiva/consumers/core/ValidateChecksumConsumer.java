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
name|checksum
operator|.
name|ChecksumValidationException
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
name|ChecksumValidator
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
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|ManagedRepository
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
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Paths
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|checksum
operator|.
name|ChecksumValidationException
operator|.
name|ValidationError
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * ValidateChecksumConsumer - validate the provided checksum against the file it represents.  */
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
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ValidateChecksumConsumer
operator|.
name|class
argument_list|)
decl_stmt|;
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
specifier|private
name|String
name|id
init|=
literal|"validate-checksums"
decl_stmt|;
specifier|private
name|String
name|description
init|=
literal|"Validate checksums against file."
decl_stmt|;
name|ThreadLocal
argument_list|<
name|ChecksumValidator
argument_list|>
name|validatorThreadLocal
init|=
operator|new
name|ThreadLocal
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
name|Path
name|repositoryDir
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|includes
decl_stmt|;
annotation|@
name|Override
specifier|public
name|String
name|getId
parameter_list|( )
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
parameter_list|( )
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
name|Paths
operator|.
name|get
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|( )
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
annotation|@
name|Override
specifier|public
name|void
name|completeScan
parameter_list|( )
block|{
comment|/* nothing to do */
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
argument_list|( )
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
parameter_list|( )
block|{
return|return
literal|null
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
parameter_list|( )
block|{
return|return
name|this
operator|.
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
name|ChecksumValidator
name|validator
decl_stmt|;
if|if
condition|(
operator|(
name|validator
operator|=
name|validatorThreadLocal
operator|.
name|get
argument_list|()
operator|)
operator|==
literal|null
condition|)
block|{
name|validator
operator|=
operator|new
name|ChecksumValidator
argument_list|()
expr_stmt|;
name|validatorThreadLocal
operator|.
name|set
argument_list|(
name|validator
argument_list|)
expr_stmt|;
block|}
name|Path
name|checksumFile
init|=
name|this
operator|.
name|repositoryDir
operator|.
name|resolve
argument_list|(
name|path
argument_list|)
decl_stmt|;
try|try
block|{
if|if
condition|(
operator|!
name|validator
operator|.
name|isValidChecksum
argument_list|(
name|checksumFile
argument_list|)
condition|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"The checksum for {} is invalid."
argument_list|,
name|checksumFile
argument_list|)
expr_stmt|;
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
name|ChecksumValidationException
name|e
parameter_list|)
block|{
if|if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|READ_ERROR
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Checksum read error during validation on {}"
argument_list|,
name|checksumFile
argument_list|)
expr_stmt|;
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
if|else if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|INVALID_FORMAT
operator|||
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|DIGEST_ERROR
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"Digester failure during checksum validation on {}"
argument_list|,
name|checksumFile
argument_list|)
expr_stmt|;
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
if|else if
condition|(
name|e
operator|.
name|getErrorType
argument_list|()
operator|==
name|FILE_NOT_FOUND
condition|)
block|{
name|log
operator|.
name|error
argument_list|(
literal|"File not found during checksum validation: "
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|triggerConsumerError
argument_list|(
name|CHECKSUM_NOT_FOUND
argument_list|,
literal|"File not found during checksum validation: "
operator|+
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
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
parameter_list|( )
block|{
name|ChecksumValidator
name|validator
decl_stmt|;
if|if
condition|(
operator|(
name|validator
operator|=
name|validatorThreadLocal
operator|.
name|get
argument_list|()
operator|)
operator|==
literal|null
condition|)
block|{
name|validator
operator|=
operator|new
name|ChecksumValidator
argument_list|()
expr_stmt|;
name|validatorThreadLocal
operator|.
name|set
argument_list|(
name|validator
argument_list|)
expr_stmt|;
block|}
name|Set
argument_list|<
name|String
argument_list|>
name|extensions
init|=
name|validator
operator|.
name|getSupportedExtensions
argument_list|()
decl_stmt|;
name|includes
operator|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|extensions
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
for|for
control|(
name|String
name|ext
range|:
name|extensions
control|)
block|{
name|includes
operator|.
name|add
argument_list|(
literal|"**/*."
operator|+
name|ext
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

