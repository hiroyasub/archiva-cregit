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
name|policies
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|ArrayList
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
name|Properties
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
name|commons
operator|.
name|lang
operator|.
name|StringUtils
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
name|stereotype
operator|.
name|Service
import|;
end_import

begin_comment
comment|/**  * ChecksumPolicy - a policy applied after the download to see if the file has been downloaded  * successfully and completely (or not).  *  * @version $Id$  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"postDownloadPolicy#checksum"
argument_list|)
specifier|public
class|class
name|ChecksumPolicy
implements|implements
name|PostDownloadPolicy
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ChecksumPolicy
operator|.
name|class
argument_list|)
decl_stmt|;
comment|/**      * The IGNORE policy indicates that if the checksum policy is ignored, and      * the state of, contents of, or validity of the checksum files are not      * checked.      */
specifier|public
specifier|static
specifier|final
name|String
name|IGNORE
init|=
literal|"ignore"
decl_stmt|;
comment|/**      * The FAIL policy indicates that if the checksum does not match the      * downloaded file, then remove the downloaded artifact, and checksum      * files, and fail the transfer to the client side.      */
specifier|public
specifier|static
specifier|final
name|String
name|FAIL
init|=
literal|"fail"
decl_stmt|;
comment|/**      * The FIX policy indicates that if the checksum does not match the      * downloaded file, then fix the checksum file locally, and return      * to the client side the corrected checksum.      */
specifier|public
specifier|static
specifier|final
name|String
name|FIX
init|=
literal|"fix"
decl_stmt|;
specifier|private
name|ChecksumAlgorithm
index|[]
name|algorithms
init|=
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|ChecksumAlgorithm
operator|.
name|SHA1
block|,
name|ChecksumAlgorithm
operator|.
name|MD5
block|}
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|options
init|=
operator|new
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
specifier|public
name|ChecksumPolicy
parameter_list|()
block|{
name|options
operator|.
name|add
argument_list|(
name|FAIL
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|FIX
argument_list|)
expr_stmt|;
name|options
operator|.
name|add
argument_list|(
name|IGNORE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|applyPolicy
parameter_list|(
name|String
name|policySetting
parameter_list|,
name|Properties
name|request
parameter_list|,
name|File
name|localFile
parameter_list|)
throws|throws
name|PolicyViolationException
throws|,
name|PolicyConfigurationException
block|{
if|if
condition|(
literal|"resource"
operator|.
name|equals
argument_list|(
name|request
operator|.
name|getProperty
argument_list|(
literal|"filetype"
argument_list|)
argument_list|)
condition|)
block|{
return|return;
block|}
if|if
condition|(
operator|!
name|options
operator|.
name|contains
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Not a valid code.
throw|throw
operator|new
name|PolicyConfigurationException
argument_list|(
literal|"Unknown checksum policy setting ["
operator|+
name|policySetting
operator|+
literal|"], valid settings are ["
operator|+
name|StringUtils
operator|.
name|join
argument_list|(
name|options
operator|.
name|iterator
argument_list|()
argument_list|,
literal|","
argument_list|)
operator|+
literal|"]"
argument_list|)
throw|;
block|}
if|if
condition|(
name|IGNORE
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Ignore.
name|log
operator|.
name|debug
argument_list|(
literal|"Checksum policy set to IGNORE."
argument_list|)
expr_stmt|;
return|return;
block|}
if|if
condition|(
operator|!
name|localFile
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// Local File does not exist.
throw|throw
operator|new
name|PolicyViolationException
argument_list|(
literal|"Checksum policy failure, local file "
operator|+
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" does not exist to check."
argument_list|)
throw|;
block|}
if|if
condition|(
name|FAIL
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
name|ChecksummedFile
name|checksum
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|localFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|checksum
operator|.
name|isValidChecksums
argument_list|(
name|algorithms
argument_list|)
condition|)
block|{
return|return;
block|}
for|for
control|(
name|ChecksumAlgorithm
name|algorithm
range|:
name|algorithms
control|)
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"."
operator|+
name|algorithm
operator|.
name|getExt
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|file
operator|.
name|exists
argument_list|()
condition|)
block|{
name|file
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
block|}
name|localFile
operator|.
name|delete
argument_list|()
expr_stmt|;
throw|throw
operator|new
name|PolicyViolationException
argument_list|(
literal|"Checksums do not match, policy set to FAIL, "
operator|+
literal|"deleting checksum files and local file "
operator|+
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"."
argument_list|)
throw|;
block|}
if|if
condition|(
name|FIX
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
name|ChecksummedFile
name|checksum
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|localFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|checksum
operator|.
name|fixChecksums
argument_list|(
name|algorithms
argument_list|)
condition|)
block|{
name|log
operator|.
name|debug
argument_list|(
literal|"Checksum policy set to FIX, checksum files have been updated."
argument_list|)
expr_stmt|;
return|return;
block|}
else|else
block|{
throw|throw
operator|new
name|PolicyViolationException
argument_list|(
literal|"Checksum policy set to FIX, "
operator|+
literal|"yet unable to update checksums for local file "
operator|+
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"."
argument_list|)
throw|;
block|}
block|}
throw|throw
operator|new
name|PolicyConfigurationException
argument_list|(
literal|"Unable to process checksum policy of ["
operator|+
name|policySetting
operator|+
literal|"], please file a bug report."
argument_list|)
throw|;
block|}
specifier|public
name|String
name|getDefaultOption
parameter_list|()
block|{
return|return
name|FIX
return|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
literal|"checksum"
return|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
literal|"Checksum"
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getOptions
parameter_list|()
block|{
return|return
name|options
return|;
block|}
block|}
end_class

end_unit

