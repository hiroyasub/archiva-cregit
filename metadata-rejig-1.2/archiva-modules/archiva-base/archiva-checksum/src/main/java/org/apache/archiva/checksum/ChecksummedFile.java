begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|checksum
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|io
operator|.
name|FileInputStream
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
name|regex
operator|.
name|Matcher
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|regex
operator|.
name|Pattern
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
name|io
operator|.
name|FileUtils
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
name|io
operator|.
name|IOUtils
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

begin_comment
comment|/**  * ChecksummedFile  *  *<dl>  *<lh>Terminology:</lh>  *<dt>Checksum File</dt>  *<dd>The file that contains the previously calculated checksum value for the reference file.  *       This is a text file with the extension ".sha1" or ".md5", and contains a single entry  *       consisting of an optional reference filename, and a checksum string.  *</dd>  *<dt>Reference File</dt>  *<dd>The file that is being referenced in the checksum file.</dd>  *</dl>  *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ChecksummedFile
block|{
specifier|private
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|ChecksummedFile
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|File
name|referenceFile
decl_stmt|;
comment|/**      * Construct a ChecksummedFile object.      *       * @param referenceFile      */
specifier|public
name|ChecksummedFile
parameter_list|(
specifier|final
name|File
name|referenceFile
parameter_list|)
block|{
name|this
operator|.
name|referenceFile
operator|=
name|referenceFile
expr_stmt|;
block|}
comment|/**      * Calculate the checksum based on a given checksum.      *       * @param checksumAlgorithm the algorithm to use.      * @return the checksum string for the file.      * @throws IOException if unable to calculate the checksum.      */
specifier|public
name|String
name|calculateChecksum
parameter_list|(
name|ChecksumAlgorithm
name|checksumAlgorithm
parameter_list|)
throws|throws
name|IOException
block|{
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|Checksum
name|checksum
init|=
operator|new
name|Checksum
argument_list|(
name|checksumAlgorithm
argument_list|)
decl_stmt|;
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|referenceFile
argument_list|)
expr_stmt|;
name|checksum
operator|.
name|update
argument_list|(
name|fis
argument_list|)
expr_stmt|;
return|return
name|checksum
operator|.
name|getChecksum
argument_list|()
return|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Creates a checksum file of the provided referenceFile.      * @param checksumAlgorithm the hash to use.      *       * @return the checksum File that was created.      * @throws IOException if there was a problem either reading the referenceFile, or writing the checksum file.      */
specifier|public
name|File
name|createChecksum
parameter_list|(
name|ChecksumAlgorithm
name|checksumAlgorithm
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|checksumFile
init|=
operator|new
name|File
argument_list|(
name|referenceFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"."
operator|+
name|checksumAlgorithm
operator|.
name|getExt
argument_list|()
argument_list|)
decl_stmt|;
name|String
name|checksum
init|=
name|calculateChecksum
argument_list|(
name|checksumAlgorithm
argument_list|)
decl_stmt|;
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|checksumFile
argument_list|,
name|checksum
operator|+
literal|"  "
operator|+
name|referenceFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|checksumFile
return|;
block|}
comment|/**      * Get the checksum file for the reference file and hash.      *       * @param checksumAlgorithm the hash that we are interested in.      * @return the checksum file to return      */
specifier|public
name|File
name|getChecksumFile
parameter_list|(
name|ChecksumAlgorithm
name|checksumAlgorithm
parameter_list|)
block|{
return|return
operator|new
name|File
argument_list|(
name|referenceFile
operator|.
name|getAbsolutePath
argument_list|()
operator|+
literal|"."
operator|+
name|checksumAlgorithm
operator|.
name|getExt
argument_list|()
argument_list|)
return|;
block|}
comment|/**      *<p>      * Given a checksum file, check to see if the file it represents is valid according to the checksum.      *</p>      *       *<p>      * NOTE: Only supports single file checksums of type MD5 or SHA1.      *</p>      *       * @param checksumFile the algorithms to check for.      * @return true if the checksum is valid for the file it represents. or if the checksum file does not exist.      * @throws IOException if the reading of the checksumFile or the file it refers to fails.      */
specifier|public
name|boolean
name|isValidChecksum
parameter_list|(
name|ChecksumAlgorithm
name|algorithm
parameter_list|)
throws|throws
name|IOException
block|{
return|return
name|isValidChecksums
argument_list|(
operator|new
name|ChecksumAlgorithm
index|[]
block|{
name|algorithm
block|}
argument_list|)
return|;
block|}
comment|/**      * Of any checksum files present, validate that the reference file conforms      * the to the checksum.         *       * @param algorithms the algorithms to check for.      * @return true if the checksums report that the the reference file is valid, false if invalid.      */
specifier|public
name|boolean
name|isValidChecksums
parameter_list|(
name|ChecksumAlgorithm
name|algorithms
index|[]
parameter_list|)
block|{
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
try|try
block|{
name|List
argument_list|<
name|Checksum
argument_list|>
name|checksums
init|=
operator|new
name|ArrayList
argument_list|<
name|Checksum
argument_list|>
argument_list|()
decl_stmt|;
comment|// Create checksum object for each algorithm.
for|for
control|(
name|ChecksumAlgorithm
name|checksumAlgorithm
range|:
name|algorithms
control|)
block|{
name|File
name|checksumFile
init|=
name|getChecksumFile
argument_list|(
name|checksumAlgorithm
argument_list|)
decl_stmt|;
comment|// Only add algorithm if checksum file exists.
if|if
condition|(
name|checksumFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|checksums
operator|.
name|add
argument_list|(
operator|new
name|Checksum
argument_list|(
name|checksumAlgorithm
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|// Any checksums?
if|if
condition|(
name|checksums
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// No checksum objects, no checksum files, default to is invalid.
return|return
literal|false
return|;
block|}
comment|// Parse file once, for all checksums.
try|try
block|{
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|referenceFile
argument_list|)
expr_stmt|;
name|Checksum
operator|.
name|update
argument_list|(
name|checksums
argument_list|,
name|fis
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
name|warn
argument_list|(
literal|"Unable to update checksum:"
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
name|boolean
name|valid
init|=
literal|true
decl_stmt|;
comment|// check the checksum files
try|try
block|{
for|for
control|(
name|Checksum
name|checksum
range|:
name|checksums
control|)
block|{
name|ChecksumAlgorithm
name|checksumAlgorithm
init|=
name|checksum
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
name|File
name|checksumFile
init|=
name|getChecksumFile
argument_list|(
name|checksumAlgorithm
argument_list|)
decl_stmt|;
name|String
name|rawChecksum
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|checksumFile
argument_list|)
decl_stmt|;
name|String
name|expectedChecksum
init|=
name|parseChecksum
argument_list|(
name|rawChecksum
argument_list|,
name|checksumAlgorithm
argument_list|,
name|referenceFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|expectedChecksum
argument_list|,
name|checksum
operator|.
name|getChecksum
argument_list|()
argument_list|)
operator|==
literal|false
condition|)
block|{
name|valid
operator|=
literal|false
expr_stmt|;
block|}
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
name|warn
argument_list|(
literal|"Unable to read / parse checksum: "
operator|+
name|e
operator|.
name|getMessage
argument_list|()
argument_list|)
expr_stmt|;
return|return
literal|false
return|;
block|}
return|return
name|valid
return|;
block|}
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Fix or create checksum files for the reference file.      *       * @param algorithms the hashes to check for.      * @return true if checksums were created successfully.      */
specifier|public
name|boolean
name|fixChecksums
parameter_list|(
name|ChecksumAlgorithm
name|algorithms
index|[]
parameter_list|)
block|{
name|List
argument_list|<
name|Checksum
argument_list|>
name|checksums
init|=
operator|new
name|ArrayList
argument_list|<
name|Checksum
argument_list|>
argument_list|()
decl_stmt|;
comment|// Create checksum object for each algorithm.
for|for
control|(
name|ChecksumAlgorithm
name|checksumAlgorithm
range|:
name|algorithms
control|)
block|{
name|checksums
operator|.
name|add
argument_list|(
operator|new
name|Checksum
argument_list|(
name|checksumAlgorithm
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|// Any checksums?
if|if
condition|(
name|checksums
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
comment|// No checksum objects, no checksum files, default to is valid.
return|return
literal|true
return|;
block|}
name|FileInputStream
name|fis
init|=
literal|null
decl_stmt|;
try|try
block|{
comment|// Parse file once, for all checksums.
name|fis
operator|=
operator|new
name|FileInputStream
argument_list|(
name|referenceFile
argument_list|)
expr_stmt|;
name|Checksum
operator|.
name|update
argument_list|(
name|checksums
argument_list|,
name|fis
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
name|warn
argument_list|(
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
finally|finally
block|{
name|IOUtils
operator|.
name|closeQuietly
argument_list|(
name|fis
argument_list|)
expr_stmt|;
block|}
name|boolean
name|valid
init|=
literal|true
decl_stmt|;
comment|// check the hash files
for|for
control|(
name|Checksum
name|checksum
range|:
name|checksums
control|)
block|{
name|ChecksumAlgorithm
name|checksumAlgorithm
init|=
name|checksum
operator|.
name|getAlgorithm
argument_list|()
decl_stmt|;
try|try
block|{
name|File
name|checksumFile
init|=
name|getChecksumFile
argument_list|(
name|checksumAlgorithm
argument_list|)
decl_stmt|;
name|String
name|actualChecksum
init|=
name|checksum
operator|.
name|getChecksum
argument_list|()
decl_stmt|;
if|if
condition|(
name|checksumFile
operator|.
name|exists
argument_list|()
condition|)
block|{
name|String
name|rawChecksum
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|checksumFile
argument_list|)
decl_stmt|;
name|String
name|expectedChecksum
init|=
name|parseChecksum
argument_list|(
name|rawChecksum
argument_list|,
name|checksumAlgorithm
argument_list|,
name|referenceFile
operator|.
name|getName
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|equalsIgnoreCase
argument_list|(
name|expectedChecksum
argument_list|,
name|actualChecksum
argument_list|)
operator|==
literal|false
condition|)
block|{
comment|// create checksum (again)
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|checksumFile
argument_list|,
name|actualChecksum
operator|+
literal|"  "
operator|+
name|referenceFile
operator|.
name|getName
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|checksumFile
argument_list|,
name|actualChecksum
operator|+
literal|"  "
operator|+
name|referenceFile
operator|.
name|getName
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
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
name|valid
operator|=
literal|false
expr_stmt|;
block|}
block|}
return|return
name|valid
return|;
block|}
specifier|private
name|boolean
name|isValidChecksumPattern
parameter_list|(
name|String
name|filename
parameter_list|,
name|String
name|path
parameter_list|)
block|{
comment|// check if it is a remote metadata file
name|Pattern
name|pattern
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"maven-metadata-\\S*.xml"
argument_list|)
decl_stmt|;
name|Matcher
name|m
init|=
name|pattern
operator|.
name|matcher
argument_list|(
name|path
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
return|return
name|filename
operator|.
name|endsWith
argument_list|(
name|path
argument_list|)
operator|||
operator|(
literal|"-"
operator|.
name|equals
argument_list|(
name|filename
argument_list|)
operator|)
operator|||
name|filename
operator|.
name|endsWith
argument_list|(
literal|"maven-metadata.xml"
argument_list|)
return|;
block|}
return|return
name|filename
operator|.
name|endsWith
argument_list|(
name|path
argument_list|)
operator|||
operator|(
literal|"-"
operator|.
name|equals
argument_list|(
name|filename
argument_list|)
operator|)
return|;
block|}
comment|/**      * Parse a checksum string.      *       * Validate the expected path, and expected checksum algorithm, then return      * the trimmed checksum hex string.       *       * @param rawChecksumString      * @param expectedHash      * @param expectedPath      * @return      * @throws IOException      */
specifier|public
name|String
name|parseChecksum
parameter_list|(
name|String
name|rawChecksumString
parameter_list|,
name|ChecksumAlgorithm
name|expectedHash
parameter_list|,
name|String
name|expectedPath
parameter_list|)
throws|throws
name|IOException
block|{
name|String
name|trimmedChecksum
init|=
name|rawChecksumString
operator|.
name|replace
argument_list|(
literal|'\n'
argument_list|,
literal|' '
argument_list|)
operator|.
name|trim
argument_list|()
decl_stmt|;
comment|// Free-BSD / openssl
name|String
name|regex
init|=
name|expectedHash
operator|.
name|getType
argument_list|()
operator|+
literal|"\\s*\\(([^)]*)\\)\\s*=\\s*([a-fA-F0-9]+)"
decl_stmt|;
name|Matcher
name|m
init|=
name|Pattern
operator|.
name|compile
argument_list|(
name|regex
argument_list|)
operator|.
name|matcher
argument_list|(
name|trimmedChecksum
argument_list|)
decl_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|filename
init|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isValidChecksumPattern
argument_list|(
name|filename
argument_list|,
name|expectedPath
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Supplied checksum file '"
operator|+
name|filename
operator|+
literal|"' does not match expected file: '"
operator|+
name|expectedPath
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|trimmedChecksum
operator|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
expr_stmt|;
block|}
else|else
block|{
comment|// GNU tools
name|m
operator|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"([a-fA-F0-9]+)\\s+\\*?(.+)"
argument_list|)
operator|.
name|matcher
argument_list|(
name|trimmedChecksum
argument_list|)
expr_stmt|;
if|if
condition|(
name|m
operator|.
name|matches
argument_list|()
condition|)
block|{
name|String
name|filename
init|=
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isValidChecksumPattern
argument_list|(
name|filename
argument_list|,
name|expectedPath
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|IOException
argument_list|(
literal|"Supplied checksum file '"
operator|+
name|filename
operator|+
literal|"' does not match expected file: '"
operator|+
name|expectedPath
operator|+
literal|"'"
argument_list|)
throw|;
block|}
name|trimmedChecksum
operator|=
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|trimmedChecksum
return|;
block|}
block|}
end_class

end_unit

