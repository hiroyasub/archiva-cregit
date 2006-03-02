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
name|indexing
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
name|Map
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_comment
comment|/**  * This is the object type contained in the list that will be returned by the  * RepositoryIndexSearchLayer to the action class  */
end_comment

begin_class
specifier|public
class|class
name|SearchResult
block|{
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|private
name|Map
name|fieldMatches
decl_stmt|;
comment|/**      * Class constructor      */
specifier|public
name|SearchResult
parameter_list|()
block|{
name|fieldMatches
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
comment|/**      * Getter method for artifact      *      * @return Artifact      */
specifier|public
name|Artifact
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
comment|/**      * Setter method for artifact      *      * @param artifact      */
specifier|public
name|void
name|setArtifact
parameter_list|(
name|Artifact
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
block|}
comment|/**      * Getter method for fieldMatches      *      * @return Map      */
specifier|public
name|Map
name|getFieldMatches
parameter_list|()
block|{
return|return
name|fieldMatches
return|;
block|}
comment|/**      * Setter method for fieldMatches      *      * @param fieldMatches      */
specifier|public
name|void
name|setFieldMatches
parameter_list|(
name|Map
name|fieldMatches
parameter_list|)
block|{
name|this
operator|.
name|fieldMatches
operator|=
name|fieldMatches
expr_stmt|;
block|}
comment|/**      * Getter method for derived value MapEntrySet      *      * @return Map      */
specifier|public
name|Set
name|getFieldMatchesEntrySet
parameter_list|()
block|{
return|return
name|this
operator|.
name|fieldMatches
operator|.
name|entrySet
argument_list|()
return|;
block|}
block|}
end_class

end_unit

