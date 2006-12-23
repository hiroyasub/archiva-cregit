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
name|reporting
operator|.
name|reporter
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
name|maven
operator|.
name|archiva
operator|.
name|reporting
operator|.
name|AbstractRepositoryReportsTestCase
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
name|java
operator|.
name|io
operator|.
name|BufferedOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|BufferedReader
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
name|FileOutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|FileReader
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
name|OutputStream
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|OutputStreamWriter
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|jar
operator|.
name|JarOutputStream
import|;
end_import

begin_comment
comment|/**  * This class creates the artifact and metadata files used for testing the ChecksumArtifactReportProcessor.  * It is extended by ChecksumArtifactReporterTest class.  */
end_comment

begin_class
specifier|public
specifier|abstract
class|class
name|AbstractChecksumArtifactReporterTestCase
extends|extends
name|AbstractRepositoryReportsTestCase
block|{
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|validArtifactChecksumJars
init|=
block|{
literal|"validArtifact-1.0"
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
index|[]
name|invalidArtifactChecksumJars
init|=
block|{
literal|"invalidArtifact-1.0"
block|}
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|metadataChecksumFilename
init|=
literal|"maven-metadata"
decl_stmt|;
specifier|private
name|Digester
name|sha1Digest
decl_stmt|;
specifier|private
name|Digester
name|md5Digest
decl_stmt|;
specifier|public
name|void
name|setUp
parameter_list|()
throws|throws
name|Exception
block|{
name|super
operator|.
name|setUp
argument_list|()
expr_stmt|;
name|sha1Digest
operator|=
operator|(
name|Digester
operator|)
name|lookup
argument_list|(
name|Digester
operator|.
name|ROLE
argument_list|,
literal|"sha1"
argument_list|)
expr_stmt|;
name|md5Digest
operator|=
operator|(
name|Digester
operator|)
name|lookup
argument_list|(
name|Digester
operator|.
name|ROLE
argument_list|,
literal|"md5"
argument_list|)
expr_stmt|;
block|}
comment|/**      * Create checksum files.      *      * @param type The type of checksum file to be created.      */
specifier|protected
name|void
name|createChecksumFile
parameter_list|(
name|String
name|type
parameter_list|)
throws|throws
name|DigesterException
throws|,
name|IOException
block|{
comment|//loop through the valid artifact names..
if|if
condition|(
literal|"VALID"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|validArtifactChecksumJars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|writeChecksumFile
argument_list|(
literal|"checksumTest/"
argument_list|,
name|validArtifactChecksumJars
index|[
name|i
index|]
argument_list|,
literal|"jar"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
if|else if
condition|(
literal|"INVALID"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|invalidArtifactChecksumJars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|writeChecksumFile
argument_list|(
literal|"checksumTest/"
argument_list|,
name|invalidArtifactChecksumJars
index|[
name|i
index|]
argument_list|,
literal|"jar"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
block|}
comment|/**      * Create checksum files for metadata.      *      * @param type The type of checksum to be created. (Valid or invalid)      */
specifier|protected
name|void
name|createMetadataFile
parameter_list|(
name|String
name|type
parameter_list|)
throws|throws
name|DigesterException
throws|,
name|IOException
block|{
comment|//loop through the valid artifact names..
if|if
condition|(
literal|"VALID"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|writeMetadataFile
argument_list|(
literal|"checksumTest/validArtifact/1.0/"
argument_list|,
name|metadataChecksumFilename
argument_list|,
literal|"xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|writeMetadataFile
argument_list|(
literal|"checksumTest/validArtifact/"
argument_list|,
name|metadataChecksumFilename
argument_list|,
literal|"xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
name|writeMetadataFile
argument_list|(
literal|"checksumTest/"
argument_list|,
name|metadataChecksumFilename
argument_list|,
literal|"xml"
argument_list|,
literal|true
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
literal|"INVALID"
operator|.
name|equals
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|writeMetadataFile
argument_list|(
literal|"checksumTest/invalidArtifact/1.0/"
argument_list|,
name|metadataChecksumFilename
argument_list|,
literal|"xml"
argument_list|,
literal|false
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Create artifact together with its checksums.      *      * @param relativePath The groupId      * @param filename     The filename of the artifact to be created.      * @param type         The file type (JAR)      * @param isValid      Indicates whether the checksum to be created is valid or not.      */
specifier|private
name|void
name|writeChecksumFile
parameter_list|(
name|String
name|relativePath
parameter_list|,
name|String
name|filename
parameter_list|,
name|String
name|type
parameter_list|,
name|boolean
name|isValid
parameter_list|)
throws|throws
name|IOException
throws|,
name|DigesterException
block|{
comment|//Initialize variables for creating jar files
name|String
name|repoUrl
init|=
name|repository
operator|.
name|getBasedir
argument_list|()
decl_stmt|;
name|String
name|dirs
init|=
name|filename
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'/'
argument_list|)
decl_stmt|;
comment|//create the group level directory of the artifact
name|File
name|dirFiles
init|=
operator|new
name|File
argument_list|(
name|repoUrl
operator|+
name|relativePath
operator|+
name|dirs
argument_list|)
decl_stmt|;
if|if
condition|(
name|dirFiles
operator|.
name|mkdirs
argument_list|()
condition|)
block|{
comment|// create a jar file
name|String
name|path
init|=
name|repoUrl
operator|+
name|relativePath
operator|+
name|dirs
operator|+
literal|"/"
operator|+
name|filename
operator|+
literal|"."
operator|+
name|type
decl_stmt|;
name|FileOutputStream
name|f
init|=
operator|new
name|FileOutputStream
argument_list|(
name|path
argument_list|)
decl_stmt|;
name|JarOutputStream
name|out
init|=
operator|new
name|JarOutputStream
argument_list|(
operator|new
name|BufferedOutputStream
argument_list|(
name|f
argument_list|)
argument_list|)
decl_stmt|;
comment|// jar sample.txt
name|String
name|filename1
init|=
name|repoUrl
operator|+
name|relativePath
operator|+
name|dirs
operator|+
literal|"/sample.txt"
decl_stmt|;
name|createSampleFile
argument_list|(
name|filename1
argument_list|)
expr_stmt|;
name|BufferedReader
name|in
init|=
operator|new
name|BufferedReader
argument_list|(
operator|new
name|FileReader
argument_list|(
name|filename1
argument_list|)
argument_list|)
decl_stmt|;
name|out
operator|.
name|putNextEntry
argument_list|(
operator|new
name|JarEntry
argument_list|(
name|filename1
argument_list|)
argument_list|)
expr_stmt|;
name|IOUtils
operator|.
name|copy
argument_list|(
name|in
argument_list|,
name|out
argument_list|)
expr_stmt|;
name|in
operator|.
name|close
argument_list|()
expr_stmt|;
name|out
operator|.
name|close
argument_list|()
expr_stmt|;
comment|//Create md5 and sha-1 checksum files..
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|path
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|OutputStreamWriter
name|osw
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|String
name|sum
init|=
name|md5Digest
operator|.
name|calc
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isValid
condition|)
block|{
name|osw
operator|.
name|write
argument_list|(
name|sum
operator|+
literal|"1"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|osw
operator|.
name|write
argument_list|(
name|sum
argument_list|)
expr_stmt|;
block|}
name|osw
operator|.
name|close
argument_list|()
expr_stmt|;
name|file
operator|=
operator|new
name|File
argument_list|(
name|path
operator|+
literal|".sha1"
argument_list|)
expr_stmt|;
name|os
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|osw
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|String
name|sha1sum
init|=
name|sha1Digest
operator|.
name|calc
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isValid
condition|)
block|{
name|osw
operator|.
name|write
argument_list|(
name|sha1sum
operator|+
literal|"2"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|osw
operator|.
name|write
argument_list|(
name|sha1sum
argument_list|)
expr_stmt|;
block|}
name|osw
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
block|}
comment|/**      * Create metadata file together with its checksums.      *      * @param relativePath The groupId      * @param filename     The filename of the artifact to be created.      * @param type         The file type (JAR)      * @param isValid      Indicates whether the checksum to be created is valid or not.      */
specifier|private
name|void
name|writeMetadataFile
parameter_list|(
name|String
name|relativePath
parameter_list|,
name|String
name|filename
parameter_list|,
name|String
name|type
parameter_list|,
name|boolean
name|isValid
parameter_list|)
throws|throws
name|IOException
throws|,
name|DigesterException
block|{
comment|//create checksum for the metadata file..
name|String
name|repoUrl
init|=
name|repository
operator|.
name|getBasedir
argument_list|()
decl_stmt|;
name|String
name|url
init|=
name|repository
operator|.
name|getBasedir
argument_list|()
operator|+
literal|"/"
operator|+
name|filename
operator|+
literal|"."
operator|+
name|type
decl_stmt|;
name|String
name|path
init|=
name|repoUrl
operator|+
name|relativePath
operator|+
name|filename
operator|+
literal|"."
operator|+
name|type
decl_stmt|;
name|FileUtils
operator|.
name|copyFile
argument_list|(
operator|new
name|File
argument_list|(
name|url
argument_list|)
argument_list|,
operator|new
name|File
argument_list|(
name|path
argument_list|)
argument_list|)
expr_stmt|;
comment|//Create md5 and sha-1 checksum files..
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|path
operator|+
literal|".md5"
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|OutputStreamWriter
name|osw
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|String
name|md5sum
init|=
name|md5Digest
operator|.
name|calc
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isValid
condition|)
block|{
name|osw
operator|.
name|write
argument_list|(
name|md5sum
operator|+
literal|"1"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|osw
operator|.
name|write
argument_list|(
name|md5sum
argument_list|)
expr_stmt|;
block|}
name|osw
operator|.
name|close
argument_list|()
expr_stmt|;
name|file
operator|=
operator|new
name|File
argument_list|(
name|path
operator|+
literal|".sha1"
argument_list|)
expr_stmt|;
name|os
operator|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
expr_stmt|;
name|osw
operator|=
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|)
expr_stmt|;
name|String
name|sha1sum
init|=
name|sha1Digest
operator|.
name|calc
argument_list|(
operator|new
name|File
argument_list|(
name|path
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|isValid
condition|)
block|{
name|osw
operator|.
name|write
argument_list|(
name|sha1sum
operator|+
literal|"2"
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|osw
operator|.
name|write
argument_list|(
name|sha1sum
argument_list|)
expr_stmt|;
block|}
name|osw
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Create the sample file that will be included in the jar.      *      * @param filename      */
specifier|private
name|void
name|createSampleFile
parameter_list|(
name|String
name|filename
parameter_list|)
throws|throws
name|IOException
block|{
name|File
name|file
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|OutputStream
name|os
init|=
operator|new
name|FileOutputStream
argument_list|(
name|file
argument_list|)
decl_stmt|;
name|OutputStreamWriter
name|osw
init|=
operator|new
name|OutputStreamWriter
argument_list|(
name|os
argument_list|)
decl_stmt|;
name|osw
operator|.
name|write
argument_list|(
literal|"This is the content of the sample file that will be included in the jar file."
argument_list|)
expr_stmt|;
name|osw
operator|.
name|close
argument_list|()
expr_stmt|;
block|}
comment|/**      * Delete the test directory created in the repository.      *      * @param dir The directory to be deleted.      */
specifier|protected
name|void
name|deleteTestDirectory
parameter_list|(
name|File
name|dir
parameter_list|)
block|{
try|try
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|dir
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|IOException
name|e
parameter_list|)
block|{
comment|// ignore
block|}
block|}
specifier|private
name|void
name|deleteFile
parameter_list|(
name|String
name|filename
parameter_list|)
block|{
name|File
name|f
init|=
operator|new
name|File
argument_list|(
name|filename
argument_list|)
decl_stmt|;
name|f
operator|.
name|delete
argument_list|()
expr_stmt|;
block|}
specifier|protected
name|void
name|deleteChecksumFiles
parameter_list|(
name|String
name|type
parameter_list|)
block|{
comment|//delete valid checksum files of artifacts created
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|validArtifactChecksumJars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|deleteFile
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
operator|+
literal|"checksumTest/"
operator|+
name|validArtifactChecksumJars
index|[
name|i
index|]
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|validArtifactChecksumJars
index|[
name|i
index|]
operator|+
literal|"."
operator|+
name|type
operator|+
literal|".md5"
argument_list|)
expr_stmt|;
name|deleteFile
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
operator|+
literal|"checksumTest/"
operator|+
name|validArtifactChecksumJars
index|[
name|i
index|]
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|validArtifactChecksumJars
index|[
name|i
index|]
operator|+
literal|"."
operator|+
name|type
operator|+
literal|".sha1"
argument_list|)
expr_stmt|;
block|}
comment|//delete valid checksum files of metadata file
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|validArtifactChecksumJars
operator|.
name|length
condition|;
name|i
operator|++
control|)
block|{
name|deleteFile
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
operator|+
literal|"checksumTest/"
operator|+
name|validArtifactChecksumJars
index|[
name|i
index|]
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|metadataChecksumFilename
operator|+
literal|".xml.md5"
argument_list|)
expr_stmt|;
name|deleteFile
argument_list|(
name|repository
operator|.
name|getBasedir
argument_list|()
operator|+
literal|"checksumTest/"
operator|+
name|validArtifactChecksumJars
index|[
name|i
index|]
operator|.
name|replace
argument_list|(
literal|'-'
argument_list|,
literal|'/'
argument_list|)
operator|+
literal|"/"
operator|+
name|metadataChecksumFilename
operator|+
literal|".xml.sha1"
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

