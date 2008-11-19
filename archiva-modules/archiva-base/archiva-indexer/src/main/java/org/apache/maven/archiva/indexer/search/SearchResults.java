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
name|search
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
name|maven
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|bytecode
operator|.
name|BytecodeRecord
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
name|filecontent
operator|.
name|FileContentRecord
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
name|hashcodes
operator|.
name|HashcodesRecord
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
name|HashMap
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
name|Map
import|;
end_import

begin_comment
comment|/**  * SearchResults   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|SearchResults
block|{
specifier|private
name|List
name|repositories
init|=
operator|new
name|ArrayList
argument_list|()
decl_stmt|;
specifier|private
name|Map
name|hits
init|=
operator|new
name|HashMap
argument_list|()
decl_stmt|;
specifier|private
name|int
name|totalHits
decl_stmt|;
specifier|private
name|SearchResultLimits
name|limits
decl_stmt|;
specifier|public
name|SearchResults
parameter_list|()
block|{
comment|/* do nothing */
block|}
specifier|public
name|void
name|addHit
parameter_list|(
name|LuceneRepositoryContentRecord
name|record
parameter_list|)
block|{
if|if
condition|(
name|record
operator|instanceof
name|FileContentRecord
condition|)
block|{
name|FileContentRecord
name|filecontent
init|=
operator|(
name|FileContentRecord
operator|)
name|record
decl_stmt|;
name|addFileContentHit
argument_list|(
name|filecontent
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|record
operator|instanceof
name|HashcodesRecord
condition|)
block|{
name|HashcodesRecord
name|hashcodes
init|=
operator|(
name|HashcodesRecord
operator|)
name|record
decl_stmt|;
name|addHashcodeHit
argument_list|(
name|hashcodes
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|record
operator|instanceof
name|BytecodeRecord
condition|)
block|{
name|BytecodeRecord
name|bytecode
init|=
operator|(
name|BytecodeRecord
operator|)
name|record
decl_stmt|;
name|addBytecodeHit
argument_list|(
name|bytecode
argument_list|)
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|addBytecodeHit
parameter_list|(
name|BytecodeRecord
name|bytecode
parameter_list|)
block|{
name|String
name|key
init|=
name|toKey
argument_list|(
name|bytecode
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|SearchResultHit
name|hit
init|=
operator|(
name|SearchResultHit
operator|)
name|this
operator|.
name|hits
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|hit
operator|==
literal|null
condition|)
block|{
name|hit
operator|=
operator|new
name|SearchResultHit
argument_list|()
expr_stmt|;
block|}
name|hit
operator|.
name|setRepositoryId
argument_list|(
name|bytecode
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|hit
operator|.
name|addArtifact
argument_list|(
name|bytecode
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
name|hit
operator|.
name|setContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// TODO: provide context on why this is a valuable hit.
name|this
operator|.
name|hits
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|hit
argument_list|)
expr_stmt|;
block|}
specifier|private
name|String
name|toKey
parameter_list|(
name|ArchivaArtifact
name|artifact
parameter_list|)
block|{
name|StringBuffer
name|key
init|=
operator|new
name|StringBuffer
argument_list|()
decl_stmt|;
name|key
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getModel
argument_list|()
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getGroupId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getArtifactId
argument_list|()
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|":"
argument_list|)
expr_stmt|;
name|key
operator|.
name|append
argument_list|(
name|StringUtils
operator|.
name|defaultString
argument_list|(
name|artifact
operator|.
name|getVersion
argument_list|()
argument_list|)
argument_list|)
expr_stmt|;
return|return
name|key
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|private
name|void
name|addHashcodeHit
parameter_list|(
name|HashcodesRecord
name|hashcodes
parameter_list|)
block|{
name|String
name|key
init|=
name|toKey
argument_list|(
name|hashcodes
operator|.
name|getArtifact
argument_list|()
argument_list|)
decl_stmt|;
name|SearchResultHit
name|hit
init|=
operator|(
name|SearchResultHit
operator|)
name|this
operator|.
name|hits
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|hit
operator|==
literal|null
condition|)
block|{
name|hit
operator|=
operator|new
name|SearchResultHit
argument_list|()
expr_stmt|;
block|}
name|hit
operator|.
name|addArtifact
argument_list|(
name|hashcodes
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
name|hit
operator|.
name|setContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// TODO: provide context on why this is a valuable hit.
name|this
operator|.
name|hits
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|hit
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addFileContentHit
parameter_list|(
name|FileContentRecord
name|filecontent
parameter_list|)
block|{
name|String
name|key
init|=
name|filecontent
operator|.
name|getPrimaryKey
argument_list|()
decl_stmt|;
name|SearchResultHit
name|hit
init|=
operator|(
name|SearchResultHit
operator|)
name|this
operator|.
name|hits
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
if|if
condition|(
name|hit
operator|==
literal|null
condition|)
block|{
comment|// Only need to worry about this hit if it is truely new.
name|hit
operator|=
operator|new
name|SearchResultHit
argument_list|()
expr_stmt|;
name|hit
operator|.
name|setRepositoryId
argument_list|(
name|filecontent
operator|.
name|getRepositoryId
argument_list|()
argument_list|)
expr_stmt|;
name|hit
operator|.
name|setUrl
argument_list|(
name|filecontent
operator|.
name|getRepositoryId
argument_list|()
operator|+
literal|"/"
operator|+
name|filecontent
operator|.
name|getFilename
argument_list|()
argument_list|)
expr_stmt|;
name|hit
operator|.
name|setContext
argument_list|(
literal|null
argument_list|)
expr_stmt|;
comment|// TODO: handle context + highlight later.
comment|// Test for possible artifact reference ...
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
name|hit
operator|.
name|addArtifact
argument_list|(
name|filecontent
operator|.
name|getArtifact
argument_list|()
argument_list|)
expr_stmt|;
block|}
name|this
operator|.
name|hits
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|hit
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Get the list of {@link SearchResultHit} objects.      *       * @return the list of {@link SearchResultHit} objects.      */
specifier|public
name|List
name|getHits
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|(
name|hits
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
specifier|public
name|List
name|getRepositories
parameter_list|()
block|{
return|return
name|repositories
return|;
block|}
specifier|public
name|boolean
name|isEmpty
parameter_list|()
block|{
return|return
name|hits
operator|.
name|isEmpty
argument_list|()
return|;
block|}
specifier|public
name|void
name|setRepositories
parameter_list|(
name|List
name|repositories
parameter_list|)
block|{
name|this
operator|.
name|repositories
operator|=
name|repositories
expr_stmt|;
block|}
specifier|public
name|SearchResultLimits
name|getLimits
parameter_list|()
block|{
return|return
name|limits
return|;
block|}
specifier|public
name|void
name|setLimits
parameter_list|(
name|SearchResultLimits
name|limits
parameter_list|)
block|{
name|this
operator|.
name|limits
operator|=
name|limits
expr_stmt|;
block|}
specifier|public
name|int
name|getTotalHits
parameter_list|()
block|{
return|return
name|totalHits
return|;
block|}
specifier|public
name|void
name|setTotalHits
parameter_list|(
name|int
name|totalHits
parameter_list|)
block|{
name|this
operator|.
name|totalHits
operator|=
name|totalHits
expr_stmt|;
block|}
block|}
end_class

end_unit

