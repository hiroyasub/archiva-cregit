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

begin_comment
comment|/**  * SearchResultLimits - used to provide the search some limits on how the results are returned.  * This can provide paging for the result  */
end_comment

begin_class
specifier|public
class|class
name|SearchResultLimits
block|{
comment|/**      * Constant to use for {@link #setSelectedPage(int)} to indicate a desire to get ALL PAGES.      * USE WITH CAUTION!!      */
specifier|public
specifier|static
specifier|final
name|int
name|ALL_PAGES
init|=
operator|(
operator|-
literal|1
operator|)
decl_stmt|;
specifier|private
name|int
name|pageSize
init|=
literal|30
decl_stmt|;
specifier|private
name|int
name|selectedPage
init|=
literal|0
decl_stmt|;
comment|/**      * @param selectedPage page selected use -1 for all pages      */
specifier|public
name|SearchResultLimits
parameter_list|(
name|int
name|selectedPage
parameter_list|)
block|{
name|this
operator|.
name|selectedPage
operator|=
name|selectedPage
expr_stmt|;
block|}
comment|/**      * @param pageSize     number of groupId:artifact per page      * @param selectedPage page selected use -1 for all pages      * @since 1.4-M4      */
specifier|public
name|SearchResultLimits
parameter_list|(
name|int
name|pageSize
parameter_list|,
name|int
name|selectedPage
parameter_list|)
block|{
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
name|this
operator|.
name|selectedPage
operator|=
name|selectedPage
expr_stmt|;
block|}
specifier|public
name|int
name|getPageSize
parameter_list|()
block|{
return|return
name|pageSize
return|;
block|}
comment|/**      * Set page size for maximum # of hits to return per page.      *      * @param pageSize size of page by # of hits.      */
specifier|public
name|void
name|setPageSize
parameter_list|(
name|int
name|pageSize
parameter_list|)
block|{
name|this
operator|.
name|pageSize
operator|=
name|pageSize
expr_stmt|;
block|}
specifier|public
name|int
name|getSelectedPage
parameter_list|()
block|{
return|return
name|selectedPage
return|;
block|}
specifier|public
name|void
name|setSelectedPage
parameter_list|(
name|int
name|selectedPage
parameter_list|)
block|{
name|this
operator|.
name|selectedPage
operator|=
name|selectedPage
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
literal|"SearchResultLimits{"
operator|+
literal|"pageSize="
operator|+
name|pageSize
operator|+
literal|", selectedPage="
operator|+
name|selectedPage
operator|+
literal|'}'
return|;
block|}
block|}
end_class

end_unit
