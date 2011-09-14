begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
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
comment|/**  * SearchResults  *  * @version $Id: SearchResults.java 742859 2009-02-10 05:35:05Z jdumay $  */
end_comment

begin_class
specifier|public
class|class
name|SearchResults
block|{
specifier|private
name|Map
argument_list|<
name|String
argument_list|,
name|SearchResultHit
argument_list|>
name|hits
init|=
operator|new
name|HashMap
argument_list|<
name|String
argument_list|,
name|SearchResultHit
argument_list|>
argument_list|()
decl_stmt|;
specifier|private
name|int
name|totalHits
decl_stmt|;
specifier|private
name|int
name|returnedHitsCount
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
comment|// for new RepositorySearch
specifier|public
name|void
name|addHit
parameter_list|(
name|String
name|id
parameter_list|,
name|SearchResultHit
name|hit
parameter_list|)
block|{
name|hits
operator|.
name|put
argument_list|(
name|id
argument_list|,
name|hit
argument_list|)
expr_stmt|;
block|}
comment|/**      * Get the list of {@link SearchResultHit} objects.      *      * @return the list of {@link SearchResultHit} objects.      */
specifier|public
name|List
argument_list|<
name|SearchResultHit
argument_list|>
name|getHits
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<
name|SearchResultHit
argument_list|>
argument_list|(
name|hits
operator|.
name|values
argument_list|()
argument_list|)
return|;
block|}
comment|/**      * see SearchUtil on how to generate the key      *      * @param key      * @return      */
specifier|public
name|SearchResultHit
name|getSearchResultHit
parameter_list|(
name|String
name|key
parameter_list|)
block|{
return|return
name|hits
operator|.
name|get
argument_list|(
name|key
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|SearchResultHit
argument_list|>
name|getHitsMap
parameter_list|()
block|{
return|return
name|hits
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
comment|/**      * @return      * @since 1.4      */
specifier|public
name|int
name|getReturnedHitsCount
parameter_list|()
block|{
return|return
name|returnedHitsCount
return|;
block|}
comment|/**      * @param returnedHitsCount      * @since 1.4      */
specifier|public
name|void
name|setReturnedHitsCount
parameter_list|(
name|int
name|returnedHitsCount
parameter_list|)
block|{
name|this
operator|.
name|returnedHitsCount
operator|=
name|returnedHitsCount
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
return|return
literal|"SearchResults{"
operator|+
literal|"hits="
operator|+
name|hits
operator|+
literal|", totalHits="
operator|+
name|totalHits
operator|+
literal|", returnedHitsCount="
operator|+
name|returnedHitsCount
operator|+
literal|", limits="
operator|+
name|limits
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit

