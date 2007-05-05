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

begin_comment
comment|/**  * ChecksumPolicy   *  * @author<a href="mailto:joakim@erdfelt.com">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component role="org.apache.maven.archiva.policies.PostDownloadPolicy"  *                   role-hint="checksum"  */
end_comment

begin_class
specifier|public
class|class
name|ChecksumPolicy
extends|extends
name|AbstractLogEnabled
implements|implements
name|PostDownloadPolicy
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
name|List
name|options
init|=
operator|new
name|ArrayList
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
name|IGNORED
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
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
block|{
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
comment|// No valid code? false it is then.
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Unknown checksum policyCode ["
operator|+
name|policySetting
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
name|IGNORED
operator|.
name|equals
argument_list|(
name|policySetting
argument_list|)
condition|)
block|{
comment|// Ignore.
return|return
literal|true
return|;
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
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Local file "
operator|+
name|localFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|" does not exist."
argument_list|)
expr_stmt|;
return|return
literal|false
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
name|policySetting
argument_list|)
condition|)
block|{
name|boolean
name|checksPass
init|=
literal|true
decl_stmt|;
comment|// Both files missing is a failure.
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
name|getPath
argument_list|()
operator|+
literal|" has no checksum files (sha1 or md5)."
argument_list|)
expr_stmt|;
name|checksPass
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
name|sha1File
operator|.
name|exists
argument_list|()
condition|)
block|{
comment|// Bad sha1 checksum is a failure.
if|if
condition|(
operator|!
name|validateChecksum
argument_list|(
name|sha1File
argument_list|,
literal|"sha1"
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"SHA1 is incorrect for "
operator|+
name|localFile
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|checksPass
operator|=
literal|false
expr_stmt|;
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
comment|// Bad md5 checksum is a failure.
if|if
condition|(
operator|!
name|validateChecksum
argument_list|(
name|md5File
argument_list|,
literal|"md5"
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|warn
argument_list|(
literal|"MD5 is incorrect for "
operator|+
name|localFile
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
name|checksPass
operator|=
literal|false
expr_stmt|;
block|}
block|}
if|if
condition|(
operator|!
name|checksPass
condition|)
block|{
comment|// On failure. delete files.
if|if
condition|(
name|sha1File
operator|.
name|exists
argument_list|()
condition|)
block|{
name|sha1File
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
if|if
condition|(
name|md5File
operator|.
name|exists
argument_list|()
condition|)
block|{
name|md5File
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
name|localFile
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
return|return
name|checksPass
return|;
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
name|boolean
name|checksPass
init|=
literal|true
decl_stmt|;
if|if
condition|(
operator|!
name|fixChecksum
argument_list|(
name|localFile
argument_list|,
name|sha1File
argument_list|,
name|digestSha1
argument_list|)
condition|)
block|{
name|checksPass
operator|=
literal|false
expr_stmt|;
block|}
if|if
condition|(
operator|!
name|fixChecksum
argument_list|(
name|localFile
argument_list|,
name|md5File
argument_list|,
name|digestMd5
argument_list|)
condition|)
block|{
name|checksPass
operator|=
literal|false
expr_stmt|;
block|}
return|return
name|checksPass
return|;
block|}
name|getLogger
argument_list|()
operator|.
name|error
argument_list|(
literal|"Unhandled policyCode ["
operator|+
name|policySetting
operator|+
literal|"]"
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
specifier|private
name|boolean
name|createChecksum
parameter_list|(
name|File
name|localFile
parameter_list|,
name|Digester
name|digester
parameter_list|)
block|{
try|try
block|{
name|checksumFile
operator|.
name|createChecksum
argument_list|(
name|localFile
argument_list|,
name|digester
argument_list|)
expr_stmt|;
return|return
literal|true
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
literal|"Unable to create "
operator|+
name|digester
operator|.
name|getFilenameExtension
argument_list|()
operator|+
literal|" file: "
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
literal|"Unable to create "
operator|+
name|digester
operator|.
name|getFilenameExtension
argument_list|()
operator|+
literal|" file: "
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
specifier|private
name|boolean
name|fixChecksum
parameter_list|(
name|File
name|localFile
parameter_list|,
name|File
name|hashFile
parameter_list|,
name|Digester
name|digester
parameter_list|)
block|{
name|String
name|ext
init|=
name|digester
operator|.
name|getFilenameExtension
argument_list|()
decl_stmt|;
if|if
condition|(
operator|!
name|hashFile
operator|.
name|getPath
argument_list|()
operator|.
name|endsWith
argument_list|(
name|ext
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"Cannot fix "
operator|+
name|hashFile
operator|.
name|getPath
argument_list|()
operator|+
literal|" using "
operator|+
name|ext
operator|+
literal|" digester."
argument_list|)
throw|;
block|}
comment|// If hashfile doesn't exist, create it.
if|if
condition|(
operator|!
name|hashFile
operator|.
name|exists
argument_list|()
condition|)
block|{
return|return
name|createChecksum
argument_list|(
name|localFile
argument_list|,
name|digester
argument_list|)
return|;
block|}
comment|// Validate checksum, if bad, recreate it.
try|try
block|{
if|if
condition|(
name|checksumFile
operator|.
name|isValidChecksum
argument_list|(
name|hashFile
argument_list|)
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Valid checksum: "
operator|+
name|hashFile
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|true
return|;
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Not valid checksum: "
operator|+
name|hashFile
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|createChecksum
argument_list|(
name|localFile
argument_list|,
name|digester
argument_list|)
return|;
block|}
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
literal|"Unable to find "
operator|+
name|ext
operator|+
literal|" file: "
operator|+
name|hashFile
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
literal|"Unable to process "
operator|+
name|ext
operator|+
literal|" file: "
operator|+
name|hashFile
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
literal|"Unable to process "
operator|+
name|ext
operator|+
literal|" file: "
operator|+
name|hashFile
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
specifier|private
name|boolean
name|validateChecksum
parameter_list|(
name|File
name|hashFile
parameter_list|,
name|String
name|type
parameter_list|)
block|{
try|try
block|{
name|boolean
name|validity
init|=
name|checksumFile
operator|.
name|isValidChecksum
argument_list|(
name|hashFile
argument_list|)
decl_stmt|;
if|if
condition|(
name|validity
condition|)
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Valid checksum: "
operator|+
name|hashFile
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|getLogger
argument_list|()
operator|.
name|debug
argument_list|(
literal|"Not valid checksum: "
operator|+
name|hashFile
operator|.
name|getPath
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|validity
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
literal|"Unable to find "
operator|+
name|type
operator|+
literal|" file: "
operator|+
name|hashFile
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
literal|"Unable to process "
operator|+
name|type
operator|+
literal|" file: "
operator|+
name|hashFile
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
literal|"Unable to process "
operator|+
name|type
operator|+
literal|" file: "
operator|+
name|hashFile
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
name|List
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

