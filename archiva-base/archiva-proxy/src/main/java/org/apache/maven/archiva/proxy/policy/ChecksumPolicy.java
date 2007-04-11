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
name|proxy
operator|.
name|policy
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

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
name|codehaus
operator|.
name|plexus
operator|.
name|logging
operator|.
name|AbstractLogEnabled
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
name|HashSet
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

begin_comment
comment|/**  * ChecksumPolicy   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role-hint="checksum"  */
end_comment

begin_class
specifier|public
class|class
name|ChecksumPolicy
extends|extends
name|AbstractLogEnabled
implements|implements
name|PostfetchPolicy
block|{
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
comment|/**      * The IGNORE policy indicates that if the checksum is never tested      * and even bad downloads and checksum files are left in place      * on the local repository.      */
specifier|public
specifier|static
specifier|final
name|String
name|IGNORE
init|=
literal|"ignore"
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="sha1"      */
specifier|private
name|Digester
name|digestSha1
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="md5"      */
specifier|private
name|Digester
name|digestMd5
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|ChecksumFile
name|checksumFile
decl_stmt|;
specifier|private
name|Set
name|validPolicyCodes
init|=
operator|new
name|HashSet
argument_list|()
decl_stmt|;
specifier|public
name|ChecksumPolicy
parameter_list|()
block|{
name|validPolicyCodes
operator|.
name|add
argument_list|(
name|FAIL
argument_list|)
expr_stmt|;
name|validPolicyCodes
operator|.
name|add
argument_list|(
name|FIX
argument_list|)
expr_stmt|;
name|validPolicyCodes
operator|.
name|add
argument_list|(
name|IGNORE
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|applyPolicy
parameter_list|(
name|String
name|policyCode
parameter_list|,
name|File
name|localFile
parameter_list|)
block|{
if|if
condition|(
operator|!
name|validPolicyCodes
operator|.
name|contains
argument_list|(
name|policyCode
argument_list|)
condition|)
block|{
comment|// No valid code? false it is then.
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Unknown policyCode ["
operator|+
name|policyCode
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
if|if
condition|(
name|IGNORE
operator|.
name|equals
argument_list|(
name|policyCode
argument_list|)
condition|)
block|{
comment|// Ignore.
return|return
literal|true
return|;
block|}
name|File
name|sha1File
init|=
operator|new
name|File
argument_list|(
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
name|File
name|md5File
init|=
operator|new
name|File
argument_list|(
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
if|if
condition|(
name|FAIL
operator|.
name|equals
argument_list|(
name|policyCode
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|sha1File
operator|.
name|exists
argument_list|()
operator|&&
operator|!
name|md5File
operator|.
name|exists
argument_list|()
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"File "
operator|+
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" has no checksum files (sha1 or md5)."
argument_list|)
expr_stmt|;
name|localFile
operator|.
name|delete
argument_list|()
expr_stmt|;
return|return
literal|false
return|;
block|}
comment|// Test for sha1 first, then md5
if|if
condition|(
name|sha1File
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
return|return
name|checksumFile
operator|.
name|isValidChecksum
argument_list|(
name|sha1File
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to find sha1 file: "
operator|+
name|sha1File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to process sha1 file: "
operator|+
name|sha1File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to process sha1 file: "
operator|+
name|sha1File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
name|md5File
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
return|return
name|checksumFile
operator|.
name|isValidChecksum
argument_list|(
name|md5File
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to find md5 file: "
operator|+
name|md5File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to process md5 file: "
operator|+
name|md5File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to process md5 file: "
operator|+
name|md5File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
if|if
condition|(
name|FIX
operator|.
name|equals
argument_list|(
name|policyCode
argument_list|)
condition|)
block|{
if|if
condition|(
operator|!
name|sha1File
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|checksumFile
operator|.
name|createChecksum
argument_list|(
name|localFile
argument_list|,
name|digestSha1
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to create sha1 file: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to create sha1 file: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
try|try
block|{
name|checksumFile
operator|.
name|isValidChecksum
argument_list|(
name|sha1File
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to find sha1 file: "
operator|+
name|sha1File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to process sha1 file: "
operator|+
name|sha1File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to process sha1 file: "
operator|+
name|sha1File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
if|if
condition|(
operator|!
name|md5File
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|checksumFile
operator|.
name|createChecksum
argument_list|(
name|localFile
argument_list|,
name|digestMd5
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to create md5 file: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to create md5 file: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
else|else
block|{
try|try
block|{
return|return
name|checksumFile
operator|.
name|isValidChecksum
argument_list|(
name|md5File
argument_list|)
return|;
block|}
catch|catch
parameter_list|(
name|FileNotFoundException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to find md5 file: "
operator|+
name|md5File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to process md5 file: "
operator|+
name|md5File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"Unable to process md5 file: "
operator|+
name|md5File
operator|.
name|getAbsolutePath
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
block|}
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Unhandled policyCode ["
operator|+
name|policyCode
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
block|}
end_class

end_unit

