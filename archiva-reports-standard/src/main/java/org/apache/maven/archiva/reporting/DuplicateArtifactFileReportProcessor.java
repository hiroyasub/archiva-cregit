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
name|lucene
operator|.
name|index
operator|.
name|Term
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|TermQuery
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
name|indexer
operator|.
name|RepositoryArtifactIndex
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
name|indexer
operator|.
name|RepositoryArtifactIndexFactory
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
name|indexer
operator|.
name|RepositoryIndexSearchException
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
name|indexer
operator|.
name|lucene
operator|.
name|LuceneQuery
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
name|indexer
operator|.
name|record
operator|.
name|StandardArtifactIndexRecord
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
name|indexer
operator|.
name|record
operator|.
name|StandardIndexRecordFields
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
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
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
comment|/**  * Validates an artifact file for duplicates within the same groupId based from what's available in a repository index.  *  * @author Edwin Punzalan  * @plexus.component role="org.apache.maven.archiva.reporting.ArtifactReportProcessor" role-hint="duplicate"  */
end_comment

begin_class
specifier|public
class|class
name|DuplicateArtifactFileReportProcessor
implements|implements
name|ArtifactReportProcessor
block|{
comment|/**      * @plexus.requirement role-hint="md5"      */
specifier|private
name|Digester
name|digester
decl_stmt|;
comment|/**      * @plexus.requirement      */
specifier|private
name|RepositoryArtifactIndexFactory
name|indexFactory
decl_stmt|;
comment|/**      * @plexus.configuration      */
specifier|private
name|String
name|indexDirectory
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|String
name|ROLE_HINT
init|=
literal|"duplicate"
decl_stmt|;
specifier|public
name|void
name|processArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|,
name|Model
name|model
parameter_list|,
name|ReportingDatabase
name|reporter
parameter_list|)
block|{
name|ArtifactRepository
name|repository
init|=
name|artifact
operator|.
name|getRepository
argument_list|()
decl_stmt|;
if|if
condition|(
name|artifact
operator|.
name|getFile
argument_list|()
operator|!=
literal|null
condition|)
block|{
name|RepositoryArtifactIndex
name|index
init|=
name|indexFactory
operator|.
name|createStandardIndex
argument_list|(
operator|new
name|File
argument_list|(
name|indexDirectory
argument_list|)
argument_list|)
decl_stmt|;
name|String
name|checksum
init|=
literal|null
decl_stmt|;
try|try
block|{
name|checksum
operator|=
name|digester
operator|.
name|calc
argument_list|(
name|artifact
operator|.
name|getFile
argument_list|()
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|DigesterException
name|e
parameter_list|)
block|{
name|addWarning
argument_list|(
name|reporter
argument_list|,
name|artifact
argument_list|,
literal|null
argument_list|,
literal|"Unable to generate checksum for "
operator|+
name|artifact
operator|.
name|getFile
argument_list|()
operator|+
literal|": "
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|checksum
operator|!=
literal|null
condition|)
block|{
try|try
block|{
name|List
name|results
init|=
name|index
operator|.
name|search
argument_list|(
operator|new
name|LuceneQuery
argument_list|(
operator|new
name|TermQuery
argument_list|(
operator|new
name|Term
argument_list|(
name|StandardIndexRecordFields
operator|.
name|MD5
argument_list|,
name|checksum
operator|.
name|toLowerCase
argument_list|()
argument_list|)
argument_list|)
argument_list|)
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|results
operator|.
name|isEmpty
argument_list|()
condition|)
block|{
for|for
control|(
name|Iterator
name|i
init|=
name|results
operator|.
name|iterator
argument_list|()
init|;
name|i
operator|.
name|hasNext
argument_list|()
condition|;
control|)
block|{
name|StandardArtifactIndexRecord
name|result
init|=
operator|(
name|StandardArtifactIndexRecord
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
comment|//make sure it is not the same artifact
if|if
condition|(
operator|!
name|result
operator|.
name|getFilename
argument_list|()
operator|.
name|equals
argument_list|(
name|repository
operator|.
name|pathOf
argument_list|(
name|artifact
argument_list|)
argument_list|)
condition|)
block|{
comment|//report only duplicates from the same groupId
name|String
name|groupId
init|=
name|artifact
operator|.
name|getGroupId
argument_list|()
decl_stmt|;
if|if
condition|(
name|groupId
operator|.
name|equals
argument_list|(
name|result
operator|.
name|getGroupId
argument_list|()
argument_list|)
condition|)
block|{
name|addFailure
argument_list|(
name|reporter
argument_list|,
name|artifact
argument_list|,
literal|"duplicate"
argument_list|,
literal|"Found duplicate for "
operator|+
name|artifact
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
catch|catch
parameter_list|(
name|RepositoryIndexSearchException
name|e
parameter_list|)
block|{
name|addWarning
argument_list|(
name|reporter
argument_list|,
name|artifact
argument_list|,
literal|null
argument_list|,
literal|"Failed to search in index"
operator|+
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
else|else
block|{
name|addWarning
argument_list|(
name|reporter
argument_list|,
name|artifact
argument_list|,
literal|null
argument_list|,
literal|"Artifact file is null"
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
specifier|static
name|void
name|addFailure
parameter_list|(
name|ReportingDatabase
name|reporter
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|String
name|problem
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
comment|// TODO: reason could be an i18n key derived from the processor and the problem ID and the
name|reporter
operator|.
name|addFailure
argument_list|(
name|artifact
argument_list|,
name|ROLE_HINT
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|void
name|addWarning
parameter_list|(
name|ReportingDatabase
name|reporter
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|String
name|problem
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
comment|// TODO: reason could be an i18n key derived from the processor and the problem ID and the
name|reporter
operator|.
name|addWarning
argument_list|(
name|artifact
argument_list|,
name|ROLE_HINT
argument_list|,
name|problem
argument_list|,
name|reason
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

