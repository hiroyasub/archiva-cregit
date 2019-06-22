begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|features
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
name|archiva
operator|.
name|repository
operator|.
name|Repository
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|RepositoryEventListener
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
operator|.
name|StorageAsset
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
name|java
operator|.
name|net
operator|.
name|URI
import|;
end_import

begin_import
import|import
name|java
operator|.
name|net
operator|.
name|URISyntaxException
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

begin_comment
comment|/**  *  * This feature provides some information about index creation.  *  */
end_comment

begin_class
specifier|public
class|class
name|IndexCreationFeature
extends|extends
name|AbstractFeature
implements|implements
name|RepositoryFeature
argument_list|<
name|IndexCreationFeature
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_INDEX_PATH
init|=
literal|".indexer"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|DEFAULT_PACKED_INDEX_PATH
init|=
literal|".index"
decl_stmt|;
specifier|private
name|boolean
name|skipPackedIndexCreation
init|=
literal|false
decl_stmt|;
specifier|private
name|URI
name|indexPath
decl_stmt|;
specifier|private
name|URI
name|packedIndexPath
decl_stmt|;
specifier|private
name|StorageAsset
name|localIndexPath
decl_stmt|;
specifier|private
name|StorageAsset
name|localPackedIndexPath
decl_stmt|;
specifier|private
name|Repository
name|repo
decl_stmt|;
specifier|public
name|IndexCreationFeature
parameter_list|(
name|Repository
name|repoId
parameter_list|,
name|RepositoryEventListener
name|listener
parameter_list|)
block|{
name|super
argument_list|(
name|listener
argument_list|)
expr_stmt|;
name|this
operator|.
name|repo
operator|=
name|repoId
expr_stmt|;
try|try
block|{
name|this
operator|.
name|indexPath
operator|=
operator|new
name|URI
argument_list|(
name|DEFAULT_INDEX_PATH
argument_list|)
expr_stmt|;
name|this
operator|.
name|packedIndexPath
operator|=
operator|new
name|URI
argument_list|(
name|DEFAULT_PACKED_INDEX_PATH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// Does not happen
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
specifier|public
name|IndexCreationFeature
parameter_list|(
name|boolean
name|skipPackedIndexCreation
parameter_list|)
block|{
name|this
operator|.
name|skipPackedIndexCreation
operator|=
name|skipPackedIndexCreation
expr_stmt|;
try|try
block|{
name|this
operator|.
name|indexPath
operator|=
operator|new
name|URI
argument_list|(
name|DEFAULT_INDEX_PATH
argument_list|)
expr_stmt|;
name|this
operator|.
name|packedIndexPath
operator|=
operator|new
name|URI
argument_list|(
name|DEFAULT_PACKED_INDEX_PATH
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|URISyntaxException
name|e
parameter_list|)
block|{
comment|// Does not happen
name|e
operator|.
name|printStackTrace
argument_list|()
expr_stmt|;
block|}
block|}
annotation|@
name|Override
specifier|public
name|IndexCreationFeature
name|get
parameter_list|()
block|{
return|return
name|this
return|;
block|}
comment|/**      * Returns true, if no packed index files should be created.      * @return True, if no packed index files are created, otherwise false.      */
specifier|public
name|boolean
name|isSkipPackedIndexCreation
parameter_list|()
block|{
return|return
name|skipPackedIndexCreation
return|;
block|}
comment|/**      * Sets the flag for packed index creation.      *      * @param skipPackedIndexCreation      */
specifier|public
name|void
name|setSkipPackedIndexCreation
parameter_list|(
name|boolean
name|skipPackedIndexCreation
parameter_list|)
block|{
name|this
operator|.
name|skipPackedIndexCreation
operator|=
name|skipPackedIndexCreation
expr_stmt|;
block|}
comment|/**      * Returns the path that is used to store the index.      * @return the uri (may be relative or absolute)      */
specifier|public
name|URI
name|getIndexPath
parameter_list|( )
block|{
return|return
name|indexPath
return|;
block|}
comment|/**      * Sets the path that is used to store the index.      * @param indexPath the uri to the index path (may be relative)      */
specifier|public
name|void
name|setIndexPath
parameter_list|(
name|URI
name|indexPath
parameter_list|)
block|{
name|URI
name|oldVal
init|=
name|this
operator|.
name|indexPath
decl_stmt|;
name|this
operator|.
name|indexPath
operator|=
name|indexPath
expr_stmt|;
name|raiseEvent
argument_list|(
name|IndexCreationEvent
operator|.
name|indexUriChange
argument_list|(
name|repo
argument_list|,
name|oldVal
argument_list|,
name|this
operator|.
name|indexPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|hasIndex
parameter_list|()
block|{
return|return
name|this
operator|.
name|indexPath
operator|!=
literal|null
operator|&&
operator|!
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|this
operator|.
name|indexPath
operator|.
name|getPath
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * Returns the path where the index is stored physically.      *      * @return      */
specifier|public
name|StorageAsset
name|getLocalIndexPath
parameter_list|()
block|{
return|return
name|localIndexPath
return|;
block|}
comment|/**      * Sets the path where the index is stored physically. This method should only be used by the      * MavenIndexProvider implementations.      *      * @param localIndexPath      */
specifier|public
name|void
name|setLocalIndexPath
parameter_list|(
name|StorageAsset
name|localIndexPath
parameter_list|)
block|{
name|this
operator|.
name|localIndexPath
operator|=
name|localIndexPath
expr_stmt|;
block|}
comment|/**      * Returns the path of the packed index.      * @return      */
specifier|public
name|URI
name|getPackedIndexPath
parameter_list|()
block|{
return|return
name|packedIndexPath
return|;
block|}
comment|/**      * Sets the path (relative or absolute) of the packed index.      * @param packedIndexPath      */
specifier|public
name|void
name|setPackedIndexPath
parameter_list|(
name|URI
name|packedIndexPath
parameter_list|)
block|{
name|URI
name|oldVal
init|=
name|this
operator|.
name|packedIndexPath
decl_stmt|;
name|this
operator|.
name|packedIndexPath
operator|=
name|packedIndexPath
expr_stmt|;
name|raiseEvent
argument_list|(
name|IndexCreationEvent
operator|.
name|packedIndexUriChange
argument_list|(
name|repo
argument_list|,
name|oldVal
argument_list|,
name|this
operator|.
name|packedIndexPath
argument_list|)
argument_list|)
expr_stmt|;
block|}
comment|/**      * Returns the directory where the packed index is stored.      * @return      */
specifier|public
name|StorageAsset
name|getLocalPackedIndexPath
parameter_list|()
block|{
return|return
name|localPackedIndexPath
return|;
block|}
comment|/**      * Sets the path where the packed index is stored physically. This method should only be used by the      * MavenIndexProvider implementations.      *      * @param localPackedIndexPath      */
specifier|public
name|void
name|setLocalPackedIndexPath
parameter_list|(
name|StorageAsset
name|localPackedIndexPath
parameter_list|)
block|{
name|this
operator|.
name|localPackedIndexPath
operator|=
name|localPackedIndexPath
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"IndexCreationFeature:{"
argument_list|)
operator|.
name|append
argument_list|(
literal|"skipPackedIndexCreation="
argument_list|)
operator|.
name|append
argument_list|(
name|skipPackedIndexCreation
argument_list|)
operator|.
name|append
argument_list|(
literal|",indexPath="
argument_list|)
operator|.
name|append
argument_list|(
name|indexPath
argument_list|)
operator|.
name|append
argument_list|(
literal|",packedIndexPath="
argument_list|)
operator|.
name|append
argument_list|(
name|packedIndexPath
argument_list|)
operator|.
name|append
argument_list|(
literal|"}"
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

