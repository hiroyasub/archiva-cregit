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
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
operator|.
name|utils
operator|.
name|FileUtils
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
name|xml
operator|.
name|bind
operator|.
name|ValidationException
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
name|nio
operator|.
name|charset
operator|.
name|Charset
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
name|Files
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
name|StandardOpenOption
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
name|BAD_CHECKSUM_FILE_REF
import|;
end_import

begin_comment
comment|/**  * ChecksummedFile  *<p>Terminology:</p>  *<dl>  *<dt>Checksum File</dt>  *<dd>The file that contains the previously calculated checksum value for the reference file.  * This is a text file with the extension ".sha1" or ".md5", and contains a single entry  * consisting of an optional reference filename, and a checksum string.  *</dd>  *<dt>Reference File</dt>  *<dd>The file that is being referenced in the checksum file.</dd>  *</dl>  */
end_comment

begin_class
specifier|public
class|class
name|ChecksummedFile
block|{
specifier|private
specifier|static
name|Charset
name|FILE_ENCODING
init|=
name|Charset
operator|.
name|forName
argument_list|(
literal|"UTF-8"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
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
specifier|static
specifier|final
name|Pattern
name|METADATA_PATTERN
init|=
name|Pattern
operator|.
name|compile
argument_list|(
literal|"maven-metadata-\\S*.xml"
argument_list|)
decl_stmt|;
specifier|private
specifier|final
name|Path
name|referenceFile
decl_stmt|;
comment|/**      * Construct a ChecksummedFile object.      *      * @param referenceFile      */
specifier|public
name|ChecksummedFile
parameter_list|(
specifier|final
name|Path
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
specifier|public
specifier|static
name|ChecksumReference
name|getFromChecksumFile
parameter_list|(
name|Path
name|checksumFile
parameter_list|)
block|{
name|ChecksumAlgorithm
name|alg
init|=
name|ChecksumAlgorithm
operator|.
name|getByExtension
argument_list|(
name|checksumFile
argument_list|)
decl_stmt|;
name|ChecksummedFile
name|file
init|=
operator|new
name|ChecksummedFile
argument_list|(
name|getReferenceFile
argument_list|(
name|checksumFile
argument_list|)
argument_list|)
decl_stmt|;
return|return
operator|new
name|ChecksumReference
argument_list|(
name|file
argument_list|,
name|alg
argument_list|,
name|checksumFile
argument_list|)
return|;
block|}
specifier|private
specifier|static
name|Path
name|getReferenceFile
parameter_list|(
name|Path
name|checksumFile
parameter_list|)
block|{
name|String
name|fileName
init|=
name|checksumFile
operator|.
name|getFileName
argument_list|( )
operator|.
name|toString
argument_list|( )
decl_stmt|;
return|return
name|checksumFile
operator|.
name|resolveSibling
argument_list|(
name|fileName
operator|.
name|substring
argument_list|(
literal|0
argument_list|,
name|fileName
operator|.
name|lastIndexOf
argument_list|(
literal|'.'
argument_list|)
argument_list|)
argument_list|)
return|;
block|}
comment|/**      * Calculate the checksum based on a given checksum.      *      * @param checksumAlgorithm the algorithm to use.      * @return the checksum string for the file.      * @throws IOException if unable to calculate the checksum.      */
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
name|Checksum
name|checksum
init|=
operator|new
name|Checksum
argument_list|(
name|checksumAlgorithm
argument_list|)
decl_stmt|;
name|checksum
operator|.
name|update
argument_list|(
name|referenceFile
argument_list|)
expr_stmt|;
return|return
name|checksum
operator|.
name|getChecksum
argument_list|( )
return|;
block|}
comment|/**      * Creates a checksum file of the provided referenceFile.      *      * @param checksumAlgorithm the hash to use.      * @return the checksum File that was created.      * @throws IOException if there was a problem either reading the referenceFile, or writing the checksum file.      */
specifier|public
name|Path
name|createChecksum
parameter_list|(
name|ChecksumAlgorithm
name|checksumAlgorithm
parameter_list|)
throws|throws
name|IOException
block|{
name|Path
name|checksumFile
init|=
name|referenceFile
operator|.
name|resolveSibling
argument_list|(
name|referenceFile
operator|.
name|getFileName
argument_list|( )
operator|+
literal|"."
operator|+
name|checksumAlgorithm
operator|.
name|getExt
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
decl_stmt|;
name|Files
operator|.
name|deleteIfExists
argument_list|(
name|checksumFile
argument_list|)
expr_stmt|;
name|String
name|checksum
init|=
name|calculateChecksum
argument_list|(
name|checksumAlgorithm
argument_list|)
decl_stmt|;
name|Files
operator|.
name|write
argument_list|(
name|checksumFile
argument_list|,
comment|//
operator|(
name|checksum
operator|+
literal|"  "
operator|+
name|referenceFile
operator|.
name|getFileName
argument_list|( )
operator|.
name|toString
argument_list|( )
operator|)
operator|.
name|getBytes
argument_list|( )
argument_list|,
comment|//
name|StandardOpenOption
operator|.
name|CREATE_NEW
argument_list|)
expr_stmt|;
return|return
name|checksumFile
return|;
block|}
comment|/**      * Get the checksum file for the reference file and hash.      *      * @param checksumAlgorithm the hash that we are interested in.      * @return the checksum file to return      */
specifier|public
name|Path
name|getChecksumFile
parameter_list|(
name|ChecksumAlgorithm
name|checksumAlgorithm
parameter_list|)
block|{
for|for
control|(
name|String
name|ext
range|:
name|checksumAlgorithm
operator|.
name|getExt
argument_list|( )
control|)
block|{
name|Path
name|file
init|=
name|referenceFile
operator|.
name|resolveSibling
argument_list|(
name|referenceFile
operator|.
name|getFileName
argument_list|( )
operator|+
literal|"."
operator|+
name|checksumAlgorithm
operator|.
name|getExt
argument_list|( )
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|file
argument_list|)
condition|)
block|{
return|return
name|file
return|;
block|}
block|}
return|return
name|referenceFile
operator|.
name|resolveSibling
argument_list|(
name|referenceFile
operator|.
name|getFileName
argument_list|( )
operator|+
literal|"."
operator|+
name|checksumAlgorithm
operator|.
name|getExt
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
argument_list|)
return|;
block|}
comment|/**      *<p>      * Given a checksum file, check to see if the file it represents is valid according to the checksum.      *</p>      *<p>      * NOTE: Only supports single file checksums of type MD5 or SHA1.      *</p>      *      * @param algorithm the algorithms to check for.      * @return true if the checksum is valid for the file it represents. or if the checksum file does not exist.      * @throws IOException if the reading of the checksumFile or the file it refers to fails.      */
specifier|public
name|boolean
name|isValidChecksum
parameter_list|(
name|ChecksumAlgorithm
name|algorithm
parameter_list|)
throws|throws
name|ChecksumValidationException
block|{
return|return
name|isValidChecksum
argument_list|(
name|algorithm
argument_list|,
literal|false
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isValidChecksum
parameter_list|(
name|ChecksumAlgorithm
name|algorithm
parameter_list|,
name|boolean
name|throwExceptions
parameter_list|)
throws|throws
name|ChecksumValidationException
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
comment|/**      * Of any checksum files present, validate that the reference file conforms      * the to the checksum.      *      * @param algorithms the algorithms to check for.      * @return true if the checksums report that the the reference file is valid, false if invalid.      */
specifier|public
name|boolean
name|isValidChecksums
parameter_list|(
name|ChecksumAlgorithm
name|algorithms
index|[]
parameter_list|)
throws|throws
name|ChecksumValidationException
block|{
return|return
name|isValidChecksums
argument_list|(
name|algorithms
argument_list|,
literal|false
argument_list|)
return|;
block|}
comment|/**      * Checks if the checksums are valid for the referenced file.      * This method throws only exceptions, if throwExceptions is true. Otherwise false will be returned instead.      * @param algorithms The algorithms to verify      * @param throwExceptions If true, exceptions will be thrown, otherwise false will be returned, if a exception occurred.      * @return True, if it is valid, otherwise false.      * @throws ChecksumValidationException      */
specifier|public
name|boolean
name|isValidChecksums
parameter_list|(
name|ChecksumAlgorithm
name|algorithms
index|[]
parameter_list|,
name|boolean
name|throwExceptions
parameter_list|)
throws|throws
name|ChecksumValidationException
block|{
name|List
argument_list|<
name|Checksum
argument_list|>
name|checksums
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|algorithms
operator|.
name|length
argument_list|)
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
name|Path
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
name|Files
operator|.
name|exists
argument_list|(
name|checksumFile
argument_list|)
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
argument_list|( )
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
name|Checksum
operator|.
name|update
argument_list|(
name|checksums
argument_list|,
name|referenceFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ChecksumValidationException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to update checksum:{}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
if|if
condition|(
name|throwExceptions
condition|)
block|{
throw|throw
name|e
throw|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
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
argument_list|( )
decl_stmt|;
name|Path
name|checksumFile
init|=
name|getChecksumFile
argument_list|(
name|checksumAlgorithm
argument_list|)
decl_stmt|;
name|String
name|expectedChecksum
init|=
name|parseChecksum
argument_list|(
name|checksumFile
argument_list|,
name|checksumAlgorithm
argument_list|,
name|referenceFile
operator|.
name|getFileName
argument_list|( )
operator|.
name|toString
argument_list|( )
argument_list|,
name|FILE_ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|checksum
operator|.
name|compare
argument_list|(
name|expectedChecksum
argument_list|)
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
name|ChecksumValidationException
name|e
parameter_list|)
block|{
name|log
operator|.
name|warn
argument_list|(
literal|"Unable to read / parse checksum: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|( )
argument_list|)
expr_stmt|;
if|if
condition|(
name|throwExceptions
condition|)
block|{
throw|throw
name|e
throw|;
block|}
else|else
block|{
return|return
literal|false
return|;
block|}
block|}
return|return
name|valid
return|;
block|}
specifier|public
name|Path
name|getReferenceFile
parameter_list|( )
block|{
return|return
name|referenceFile
return|;
block|}
comment|/**      * Fix or create checksum files for the reference file.      *      * @param algorithms the hashes to check for.      * @return true if checksums were created successfully.      */
specifier|public
name|boolean
name|fixChecksums
parameter_list|(
name|ChecksumAlgorithm
index|[]
name|algorithms
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
argument_list|<>
argument_list|(
name|algorithms
operator|.
name|length
argument_list|)
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
argument_list|( )
condition|)
block|{
comment|// No checksum objects, no checksum files, default to is valid.
return|return
literal|true
return|;
block|}
try|try
block|{
comment|// Parse file once, for all checksums.
name|Checksum
operator|.
name|update
argument_list|(
name|checksums
argument_list|,
name|referenceFile
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|ChecksumValidationException
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
argument_list|( )
argument_list|,
name|e
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
argument_list|( )
decl_stmt|;
try|try
block|{
name|Path
name|checksumFile
init|=
name|getChecksumFile
argument_list|(
name|checksumAlgorithm
argument_list|)
decl_stmt|;
if|if
condition|(
name|Files
operator|.
name|exists
argument_list|(
name|checksumFile
argument_list|)
condition|)
block|{
name|String
name|expectedChecksum
init|=
name|parseChecksum
argument_list|(
name|checksumFile
argument_list|,
name|checksumAlgorithm
argument_list|,
name|referenceFile
operator|.
name|getFileName
argument_list|( )
operator|.
name|toString
argument_list|( )
argument_list|,
name|FILE_ENCODING
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|checksum
operator|.
name|compare
argument_list|(
name|expectedChecksum
argument_list|)
condition|)
block|{
comment|// create checksum (again)
name|writeChecksumFile
argument_list|(
name|checksumFile
argument_list|,
name|FILE_ENCODING
argument_list|,
name|checksum
operator|.
name|getChecksum
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
else|else
block|{
name|writeChecksumFile
argument_list|(
name|checksumFile
argument_list|,
name|FILE_ENCODING
argument_list|,
name|checksum
operator|.
name|getChecksum
argument_list|( )
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
name|log
operator|.
name|warn
argument_list|(
name|e
operator|.
name|getMessage
argument_list|( )
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
name|void
name|writeChecksumFile
parameter_list|(
name|Path
name|checksumFile
parameter_list|,
name|Charset
name|encoding
parameter_list|,
name|String
name|checksumHex
parameter_list|)
block|{
name|FileUtils
operator|.
name|writeStringToFile
argument_list|(
name|checksumFile
argument_list|,
name|FILE_ENCODING
argument_list|,
name|checksumHex
operator|+
literal|"  "
operator|+
name|referenceFile
operator|.
name|getFileName
argument_list|( )
operator|.
name|toString
argument_list|( )
argument_list|)
expr_stmt|;
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
name|Matcher
name|m
init|=
name|METADATA_PATTERN
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
argument_list|( )
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
comment|/**      * Parse a checksum string.      *<p>      * Validate the expected path, and expected checksum algorithm, then return      * the trimmed checksum hex string.      *</p>      *      * @param checksumFile The file where the checksum is stored      * @param expectedHash The checksum algorithm to check      * @param expectedPath The filename of the reference file      * @return      * @throws IOException      */
specifier|public
name|String
name|parseChecksum
parameter_list|(
name|Path
name|checksumFile
parameter_list|,
name|ChecksumAlgorithm
name|expectedHash
parameter_list|,
name|String
name|expectedPath
parameter_list|,
name|Charset
name|encoding
parameter_list|)
throws|throws
name|ChecksumValidationException
block|{
name|ChecksumFileContent
name|fc
init|=
name|parseChecksumFile
argument_list|(
name|checksumFile
argument_list|,
name|expectedHash
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
if|if
condition|(
name|fc
operator|.
name|isFormatMatch
argument_list|()
operator|&&
operator|!
name|isValidChecksumPattern
argument_list|(
name|fc
operator|.
name|getFileReference
argument_list|( )
argument_list|,
name|expectedPath
argument_list|)
condition|)
block|{
throw|throw
operator|new
name|ChecksumValidationException
argument_list|(
name|BAD_CHECKSUM_FILE_REF
argument_list|,
literal|"The file reference '"
operator|+
name|fc
operator|.
name|getFileReference
argument_list|( )
operator|+
literal|"' in the checksum file does not match expected file: '"
operator|+
name|expectedPath
operator|+
literal|"'"
argument_list|)
throw|;
block|}
return|return
name|fc
operator|.
name|getChecksum
argument_list|( )
return|;
block|}
specifier|public
name|ChecksumFileContent
name|parseChecksumFile
parameter_list|(
name|Path
name|checksumFile
parameter_list|,
name|ChecksumAlgorithm
name|expectedHash
parameter_list|,
name|Charset
name|encoding
parameter_list|)
block|{
name|ChecksumFileContent
name|fc
init|=
operator|new
name|ChecksumFileContent
argument_list|( )
decl_stmt|;
name|String
name|rawChecksumString
init|=
name|FileUtils
operator|.
name|readFileToString
argument_list|(
name|checksumFile
argument_list|,
name|encoding
argument_list|)
decl_stmt|;
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
argument_list|( )
decl_stmt|;
comment|// Free-BSD / openssl
name|String
name|regex
init|=
name|expectedHash
operator|.
name|getType
argument_list|( )
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
argument_list|( )
condition|)
block|{
name|fc
operator|.
name|setFileReference
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|fc
operator|.
name|setChecksum
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|fc
operator|.
name|setFormatMatch
argument_list|(
literal|true
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
argument_list|( )
condition|)
block|{
name|fc
operator|.
name|setFileReference
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|2
argument_list|)
argument_list|)
expr_stmt|;
name|fc
operator|.
name|setChecksum
argument_list|(
name|m
operator|.
name|group
argument_list|(
literal|1
argument_list|)
argument_list|)
expr_stmt|;
name|fc
operator|.
name|setFormatMatch
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|fc
operator|.
name|setFileReference
argument_list|(
literal|""
argument_list|)
expr_stmt|;
name|fc
operator|.
name|setChecksum
argument_list|(
name|trimmedChecksum
argument_list|)
expr_stmt|;
name|fc
operator|.
name|setFormatMatch
argument_list|(
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
return|return
name|fc
return|;
block|}
block|}
end_class

end_unit

