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
name|archiva
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
name|apache
operator|.
name|maven
operator|.
name|archiva
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
name|IOException
import|;
end_import

begin_comment
comment|/**  * This class reports invalid and mismatched checksums of artifacts and metadata files.  * It validates MD5 and SHA-1 checksums.  *  * @plexus.component role="org.apache.maven.archiva.reporting.MetadataReportProcessor" role-hint="checksum-metadata"  */
end_comment

begin_class
specifier|public
class|class
name|ChecksumMetadataReportProcessor
implements|implements
name|MetadataReportProcessor
block|{
comment|/**      * @plexus.requirement role-hint="sha1"      */
specifier|private
name|Digester
name|sha1Digester
decl_stmt|;
comment|/**      * @plexus.requirement role-hint="md5"      */
specifier|private
name|Digester
name|md5Digester
decl_stmt|;
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
name|ReportingDatabase
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
name|verifyChecksum
argument_list|(
name|repository
argument_list|,
name|path
operator|+
literal|".md5"
argument_list|,
name|file
argument_list|,
name|md5Digester
argument_list|,
name|reporter
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
name|verifyChecksum
argument_list|(
name|repository
argument_list|,
name|path
operator|+
literal|".sha1"
argument_list|,
name|file
argument_list|,
name|sha1Digester
argument_list|,
name|reporter
argument_list|,
name|metadata
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|verifyChecksum
parameter_list|(
name|ArtifactRepository
name|repository
parameter_list|,
name|String
name|path
parameter_list|,
name|File
name|file
parameter_list|,
name|Digester
name|digester
parameter_list|,
name|ReportingDatabase
name|reporter
parameter_list|,
name|RepositoryMetadata
name|metadata
parameter_list|)
block|{
name|File
name|checksumFile
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
if|if
condition|(
name|checksumFile
operator|.
name|exists
argument_list|()
condition|)
block|{
try|try
block|{
name|digester
operator|.
name|verify
argument_list|(
name|file
argument_list|,
name|FileUtils
operator|.
name|fileRead
argument_list|(
name|checksumFile
argument_list|)
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|reporter
operator|.
name|addFailure
argument_list|(
name|metadata
argument_list|,
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
literal|"Read file error: "
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
name|digester
operator|.
name|getAlgorithm
argument_list|()
operator|+
literal|" checksum file does not exist."
argument_list|)
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

