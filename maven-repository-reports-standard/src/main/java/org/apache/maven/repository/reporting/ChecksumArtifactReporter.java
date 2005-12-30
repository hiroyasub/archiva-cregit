begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|reporting
package|;
end_package

begin_comment
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|Artifact
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|ArtifactRepository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|artifact
operator|.
name|repository
operator|.
name|metadata
operator|.
name|RepositoryMetadata
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|model
operator|.
name|Model
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
name|util
operator|.
name|FileUtils
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
name|FileInputStream
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
name|io
operator|.
name|InputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|MessageDigest
import|;
end_import

begin_import
import|import
name|java
operator|.
name|security
operator|.
name|NoSuchAlgorithmException
import|;
end_import

begin_comment
comment|/**  * This class reports invalid and mismatched checksums of artifacts and metadata files.  * It validates MD5 and SHA-1 checksums.  *  * @todo remove stateful parts, change to singleton instantiation  * @plexus.component role="org.apache.maven.repository.reporting.ArtifactReportProcessor" role-hint="checksum"  */
end_comment

begin_class
specifier|public
class|class
name|ChecksumArtifactReporter
implements|implements
name|ArtifactReportProcessor
block|{
specifier|private
specifier|static
specifier|final
name|int
name|BYTE_MASK
init|=
literal|0xFF
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|int
name|CHECKSUM_BUFFER_SIZE
init|=
literal|16384
decl_stmt|;
comment|/**      * Validate the checksum of the specified artifact.      *      * @param model      * @param artifact      * @param reporter      * @param repository      */
specifier|public
name|void
name|processArtifact
parameter_list|(
name|Model
name|model
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|ArtifactReporter
name|reporter
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|"file"
operator|.
name|equals
argument_list|(
name|repository
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
comment|// We can't check other types of URLs yet. Need to use Wagon, with an exists() method.
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Can't process repository '"
operator|+
name|repository
operator|.
name|getUrl
argument_list|()
operator|+
literal|"'. Only file based repositories are supported"
argument_list|)
throw|;
block|}
comment|//check if checksum files exist
name|String
name|path
init|=
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|File
name|md5File
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|validateChecksum
argument_list|(
name|file
argument_list|,
name|md5File
argument_list|,
literal|"MD5"
argument_list|)
condition|)
block|{
name|reporter
operator|.
name|addSuccess
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"MD5 checksum does not match."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"Unable to read MD5: "
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
name|IOException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"Unable to read MD5: "
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
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"MD5 checksum file does not exist."
argument_list|)
expr_stmt|;
block|}
name|File
name|sha1File
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|validateChecksum
argument_list|(
name|file
argument_list|,
name|sha1File
argument_list|,
literal|"SHA-1"
argument_list|)
condition|)
block|{
name|reporter
operator|.
name|addSuccess
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"SHA-1 checksum does not match."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"Unable to read SHA-1: "
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
name|IOException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"Unable to read SHA-1: "
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
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
literal|"SHA-1 checksum file does not exist."
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validate the checksums of the metadata. Get the metadata file from the      * repository then validate the checksum.      */
specifier|public
name|void
name|processMetadata
parameter_list|(
name|RepositoryMetadata
name|metadata
parameter_list|,
name|ArtifactRepository
name|repository
parameter_list|,
name|ArtifactReporter
name|reporter
parameter_list|)
block|{
if|if
condition|(
operator|!
literal|"file"
operator|.
name|equals
argument_list|(
name|repository
operator|.
name|getProtocol
argument_list|()
argument_list|)
condition|)
block|{
comment|// We can't check other types of URLs yet. Need to use Wagon, with an exists() method.
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|(
literal|"Can't process repository '"
operator|+
name|repository
operator|.
name|getUrl
argument_list|()
operator|+
literal|"'. Only file based repositories are supported"
argument_list|)
throw|;
block|}
comment|//check if checksum files exist
name|String
name|path
init|=
name|repository
operator|.
name|pathOfRemoteRepositoryMetadata
argument_list|(
name|metadata
argument_list|)
decl_stmt|;
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
argument_list|)
decl_stmt|;
name|File
name|md5File
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|validateChecksum
argument_list|(
name|file
argument_list|,
name|md5File
argument_list|,
literal|"MD5"
argument_list|)
condition|)
block|{
name|reporter
operator|.
name|addSuccess
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"MD5 checksum does not match."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"Unable to read MD5: "
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
name|IOException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"Unable to read MD5: "
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
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"MD5 checksum file does not exist."
argument_list|)
expr_stmt|;
block|}
name|File
name|sha1File
init|=
operator|new
name|File
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
argument_list|,
name|path
operator|+
literal|".sha1"
argument_list|)
decl_stmt|;
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
if|if
condition|(
name|validateChecksum
argument_list|(
name|file
argument_list|,
name|sha1File
argument_list|,
literal|"SHA-1"
argument_list|)
condition|)
block|{
name|reporter
operator|.
name|addSuccess
argument_list|(
name|metadata
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"SHA-1 checksum does not match."
argument_list|)
expr_stmt|;
block|}
block|}
catch|catch
parameter_list|(
name|NoSuchAlgorithmException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"Unable to read SHA1: "
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
name|IOException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"Unable to read SHA1: "
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
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
literal|"SHA-1 checksum file does not exist."
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Validate the checksum of the file.      *      * @param file         The file to be validated.      * @param checksumFile the checksum to validate against      * @param algo         The checksum algorithm used.      */
specifier|private
name|boolean
name|validateChecksum
parameter_list|(
name|File
name|file
parameter_list|,
name|File
name|checksumFile
parameter_list|,
name|String
name|algo
parameter_list|)
throws|throws
name|NoSuchAlgorithmException
throws|,
name|IOException
block|{
name|boolean
name|valid
init|=
literal|false
decl_stmt|;
comment|//Create checksum for jar file
name|byte
index|[]
name|chk1
init|=
name|createChecksum
argument_list|(
name|file
argument_list|,
name|algo
argument_list|)
decl_stmt|;
if|if
condition|(
name|chk1
operator|!=
literal|null
condition|)
block|{
comment|//read the checksum file
name|String
name|checksum
init|=
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|checksumFile
argument_list|)
decl_stmt|;
name|valid
operator|=
name|checksum
operator|.
name|toUpperCase
argument_list|()
operator|.
name|equals
argument_list|(
name|byteArrayToHexStr
argument_list|(
name|chk1
argument_list|)
operator|.
name|toUpperCase
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|valid
return|;
block|}
comment|/**      * Create a checksum from the specified metadata file.      *      * @param file The file that will be created a checksum.      * @param algo The algorithm to be used (MD5, SHA-1)      * @return      * @throws FileNotFoundException      * @throws NoSuchAlgorithmException      * @throws IOException      * @todo move to utility class      */
specifier|private
specifier|static
name|byte
index|[]
name|createChecksum
parameter_list|(
name|File
name|file
parameter_list|,
name|String
name|algo
parameter_list|)
throws|throws
name|FileNotFoundException
throws|,
name|NoSuchAlgorithmException
throws|,
name|IOException
block|{
name|MessageDigest
name|digest
init|=
name|MessageDigest
operator|.
name|getInstance
argument_list|(
name|algo
argument_list|)
decl_stmt|;
name|InputStream
name|fis
init|=
operator|new
name|FileInputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
try|try
block|{
name|byte
index|[]
name|buffer
init|=
operator|new
name|byte
index|[
name|CHECKSUM_BUFFER_SIZE
index|]
decl_stmt|;
name|int
name|numRead
decl_stmt|;
do|do
block|{
name|numRead
operator|=
name|fis
operator|.
name|read
argument_list|(
name|buffer
argument_list|)
expr_stmt|;
if|if
condition|(
name|numRead
operator|>
literal|0
condition|)
block|{
name|digest
operator|.
name|update
argument_list|(
name|buffer
argument_list|,
literal|0
argument_list|,
name|numRead
argument_list|)
expr_stmt|;
block|}
block|}
do|while
condition|(
name|numRead
operator|!=
operator|-
literal|1
condition|)
do|;
block|}
finally|finally
block|{
name|fis
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
return|return
name|digest
operator|.
name|digest
argument_list|()
return|;
block|}
comment|/**      * Convert an incoming array of bytes into a string that represents each of      * the bytes as two hex characters.      *      * @param data      * @todo move to utilities      */
specifier|public
specifier|static
name|String
name|byteArrayToHexStr
parameter_list|(
name|byte
index|[]
name|data
parameter_list|)
block|{
name|String
name|output
init|=
literal|""
decl_stmt|;
for|for
control|(
name|int
name|cnt
init|=
literal|0
init|;
name|cnt
operator|<
name|data
operator|.
name|length
condition|;
name|cnt
operator|++
control|)
block|{
comment|//Deposit a byte into the 8 lsb of an int.
name|int
name|tempInt
init|=
name|data
index|[
name|cnt
index|]
operator|&
name|BYTE_MASK
decl_stmt|;
comment|//Get hex representation of the int as a string.
name|String
name|tempStr
init|=
name|Integer
operator|.
name|toHexString
argument_list|(
name|tempInt
argument_list|)
decl_stmt|;
comment|//Append a leading 0 if necessary so that each hex string will contain 2 characters.
if|if
condition|(
name|tempStr
operator|.
name|length
argument_list|()
operator|==
literal|1
condition|)
block|{
name|tempStr
operator|=
literal|"0"
operator|+
name|tempStr
expr_stmt|;
block|}
comment|//Concatenate the two characters to the output string.
name|output
operator|=
name|output
operator|+
name|tempStr
expr_stmt|;
block|}
return|return
name|output
operator|.
name|toUpperCase
argument_list|()
return|;
block|}
block|}
end_class

end_unit

