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
name|indexer
operator|.
name|filecontent
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
name|apache
operator|.
name|lucene
operator|.
name|document
operator|.
name|Document
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
name|ArtifactKeys
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
name|LuceneDocumentMaker
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
name|LuceneEntryConverter
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
name|LuceneRepositoryContentRecord
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
name|model
operator|.
name|ArchivaArtifact
import|;
end_import

begin_import
import|import
name|java
operator|.
name|text
operator|.
name|ParseException
import|;
end_import

begin_comment
comment|/**  * FileContentConverter   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|FileContentConverter
implements|implements
name|LuceneEntryConverter
block|{
specifier|public
name|Document
name|convert
parameter_list|(
name|LuceneRepositoryContentRecord
name|record
parameter_list|)
block|{
if|if
condition|(
operator|!
operator|(
name|record
operator|instanceof
name|FileContentRecord
operator|)
condition|)
block|{
throw|throw
operator|new
name|ClassCastException
argument_list|(
literal|"Unable to convert type "
operator|+
name|record
operator|.
name|getClass
argument_list|()
operator|.
name|getName
argument_list|()
operator|+
literal|" to "
operator|+
name|FileContentRecord
operator|.
name|class
operator|.
name|getName
argument_list|()
operator|+
literal|"."
argument_list|)
throw|;
block|}
name|FileContentRecord
name|filecontent
init|=
operator|(
name|FileContentRecord
operator|)
name|record
decl_stmt|;
name|LuceneDocumentMaker
name|doc
init|=
operator|new
name|LuceneDocumentMaker
argument_list|(
name|filecontent
argument_list|)
decl_stmt|;
if|if
condition|(
name|filecontent
operator|.
name|getArtifact
argument_list|()
operator|!=
literal|null
condition|)
block|{
comment|// Artifact Reference
name|doc
operator|.
name|addFieldTokenized
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID
argument_list|,
name|filecontent
operator|.
name|getArtifact
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addFieldExact
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID_EXACT
argument_list|,
name|filecontent
operator|.
name|getArtifact
argument_list|()
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addFieldTokenized
argument_list|(
name|ArtifactKeys
operator|.
name|ARTIFACTID
argument_list|,
name|filecontent
operator|.
name|getArtifact
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
comment|//, 2.0f);
name|doc
operator|.
name|addFieldExact
argument_list|(
name|ArtifactKeys
operator|.
name|ARTIFACTID_EXACT
argument_list|,
name|filecontent
operator|.
name|getArtifact
argument_list|()
operator|.
name|getArtifactId
argument_list|()
argument_list|,
literal|2.0f
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addFieldTokenized
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION
argument_list|,
name|filecontent
operator|.
name|getArtifact
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addFieldExact
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION_EXACT
argument_list|,
name|filecontent
operator|.
name|getArtifact
argument_list|()
operator|.
name|getVersion
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addFieldTokenized
argument_list|(
name|ArtifactKeys
operator|.
name|TYPE
argument_list|,
name|filecontent
operator|.
name|getArtifact
argument_list|()
operator|.
name|getType
argument_list|()
argument_list|)
expr_stmt|;
name|doc
operator|.
name|addFieldUntokenized
argument_list|(
name|ArtifactKeys
operator|.
name|CLASSIFIER
argument_list|,
name|filecontent
operator|.
name|getArtifact
argument_list|()
operator|.
name|getClassifier
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|doc
operator|.
name|addFieldTokenized
argument_list|(
name|FileContentKeys
operator|.
name|FILENAME
argument_list|,
name|filecontent
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
return|return
name|doc
operator|.
name|getDocument
argument_list|()
return|;
block|}
specifier|public
name|LuceneRepositoryContentRecord
name|convert
parameter_list|(
name|Document
name|document
parameter_list|)
throws|throws
name|ParseException
block|{
name|FileContentRecord
name|record
init|=
operator|new
name|FileContentRecord
argument_list|()
decl_stmt|;
name|record
operator|.
name|setRepositoryId
argument_list|(
name|document
operator|.
name|get
argument_list|(
name|LuceneDocumentMaker
operator|.
name|REPOSITORY_ID
argument_list|)
argument_list|)
expr_stmt|;
comment|// Artifact Reference
name|String
name|groupId
init|=
name|document
operator|.
name|get
argument_list|(
name|ArtifactKeys
operator|.
name|GROUPID
argument_list|)
decl_stmt|;
name|String
name|artifactId
init|=
name|document
operator|.
name|get
argument_list|(
name|ArtifactKeys
operator|.
name|ARTIFACTID
argument_list|)
decl_stmt|;
name|String
name|version
init|=
name|document
operator|.
name|get
argument_list|(
name|ArtifactKeys
operator|.
name|VERSION
argument_list|)
decl_stmt|;
name|String
name|classifier
init|=
name|document
operator|.
name|get
argument_list|(
name|ArtifactKeys
operator|.
name|CLASSIFIER
argument_list|)
decl_stmt|;
name|String
name|type
init|=
name|document
operator|.
name|get
argument_list|(
name|ArtifactKeys
operator|.
name|TYPE
argument_list|)
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|groupId
argument_list|)
operator|&&
name|StringUtils
operator|.
name|isNotBlank
argument_list|(
name|artifactId
argument_list|)
condition|)
block|{
name|ArchivaArtifact
name|artifact
init|=
operator|new
name|ArchivaArtifact
argument_list|(
name|groupId
argument_list|,
name|artifactId
argument_list|,
name|version
argument_list|,
name|classifier
argument_list|,
name|type
argument_list|)
decl_stmt|;
name|record
operator|.
name|setArtifact
argument_list|(
name|artifact
argument_list|)
expr_stmt|;
block|}
comment|// Filecontent Specifics
name|record
operator|.
name|setFilename
argument_list|(
name|document
operator|.
name|get
argument_list|(
name|FileContentKeys
operator|.
name|FILENAME
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|record
return|;
block|}
block|}
end_class

end_unit

