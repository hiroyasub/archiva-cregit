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
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0    *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

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

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|Cache
block|{
specifier|private
name|Map
name|cache
decl_stmt|;
specifier|private
name|DblLinkedList
name|mostRecent
decl_stmt|;
specifier|private
name|double
name|cacheHitRatio
init|=
literal|0
decl_stmt|;
specifier|private
name|long
name|cacheMaxSize
init|=
literal|0
decl_stmt|;
specifier|private
name|long
name|cacheHits
init|=
literal|0
decl_stmt|;
specifier|private
name|long
name|cacheMiss
init|=
literal|0
decl_stmt|;
specifier|public
name|Cache
parameter_list|(
name|double
name|cacheHitRatio
parameter_list|)
block|{
name|this
argument_list|(
name|cacheHitRatio
argument_list|,
literal|0
argument_list|)
expr_stmt|;
block|}
specifier|public
name|Cache
parameter_list|(
name|double
name|cacheHitRatio
parameter_list|,
name|long
name|cacheMaxSize
parameter_list|)
block|{
name|this
operator|.
name|cacheHitRatio
operator|=
name|cacheHitRatio
expr_stmt|;
name|this
operator|.
name|cacheMaxSize
operator|=
name|cacheMaxSize
expr_stmt|;
name|cache
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
specifier|public
name|Object
name|get
parameter_list|(
name|Object
name|key
parameter_list|)
block|{
name|Object
name|retValue
init|=
literal|null
decl_stmt|;
if|if
condition|(
name|cache
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|DblLinkedList
name|cacheEntry
init|=
operator|(
name|DblLinkedList
operator|)
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
decl_stmt|;
name|makeMostRecent
argument_list|(
name|cacheEntry
argument_list|)
expr_stmt|;
name|retValue
operator|=
name|cacheEntry
operator|.
name|cacheValue
expr_stmt|;
name|cacheHits
operator|++
expr_stmt|;
block|}
else|else
block|{
name|cacheMiss
operator|++
expr_stmt|;
block|}
return|return
name|retValue
return|;
block|}
specifier|public
name|void
name|put
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|DblLinkedList
name|entry
decl_stmt|;
if|if
condition|(
operator|!
name|cache
operator|.
name|containsKey
argument_list|(
name|key
argument_list|)
condition|)
block|{
name|entry
operator|=
operator|new
name|DblLinkedList
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|entry
argument_list|)
expr_stmt|;
name|manageCache
argument_list|()
expr_stmt|;
block|}
else|else
block|{
name|entry
operator|=
operator|(
name|DblLinkedList
operator|)
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
name|makeMostRecent
argument_list|(
name|entry
argument_list|)
expr_stmt|;
block|}
specifier|public
name|double
name|getHitRate
parameter_list|()
block|{
return|return
operator|(
name|cacheHits
operator|==
literal|0
operator|&&
name|cacheMiss
operator|==
literal|0
operator|)
condition|?
literal|0
else|:
operator|(
operator|(
name|double
operator|)
name|cacheHits
operator|)
operator|/
operator|(
name|double
operator|)
operator|(
name|cacheHits
operator|+
name|cacheMiss
operator|)
return|;
block|}
specifier|public
name|long
name|size
parameter_list|()
block|{
return|return
name|cache
operator|.
name|size
argument_list|()
return|;
block|}
specifier|public
name|void
name|flush
parameter_list|()
block|{
while|while
condition|(
name|cache
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
name|trimCache
argument_list|()
expr_stmt|;
name|cacheHits
operator|=
literal|0
expr_stmt|;
name|cacheMiss
operator|=
literal|0
expr_stmt|;
name|cache
operator|=
operator|new
name|HashMap
argument_list|()
expr_stmt|;
block|}
specifier|private
name|void
name|makeMostRecent
parameter_list|(
name|DblLinkedList
name|list
parameter_list|)
block|{
if|if
condition|(
name|mostRecent
operator|!=
name|list
condition|)
block|{
name|removeFromLinks
argument_list|(
name|list
argument_list|)
expr_stmt|;
if|if
condition|(
name|mostRecent
operator|!=
literal|null
condition|)
block|{
name|list
operator|.
name|next
operator|=
name|mostRecent
expr_stmt|;
name|mostRecent
operator|.
name|prev
operator|=
name|list
expr_stmt|;
block|}
name|mostRecent
operator|=
name|list
expr_stmt|;
block|}
block|}
specifier|private
name|void
name|removeFromLinks
parameter_list|(
name|DblLinkedList
name|list
parameter_list|)
block|{
if|if
condition|(
name|list
operator|.
name|prev
operator|!=
literal|null
condition|)
name|list
operator|.
name|prev
operator|.
name|next
operator|=
name|list
operator|.
name|next
expr_stmt|;
if|if
condition|(
name|list
operator|.
name|next
operator|!=
literal|null
condition|)
name|list
operator|.
name|next
operator|.
name|prev
operator|=
name|list
operator|.
name|prev
expr_stmt|;
name|list
operator|.
name|prev
operator|=
literal|null
expr_stmt|;
name|list
operator|.
name|next
operator|=
literal|null
expr_stmt|;
block|}
specifier|private
name|void
name|manageCache
parameter_list|()
block|{
if|if
condition|(
name|cacheMaxSize
operator|==
literal|0
condition|)
block|{
comment|//if desired HitRatio is reached, we can trim the cache to conserve memory
if|if
condition|(
name|cacheHitRatio
operator|<=
name|getHitRate
argument_list|()
condition|)
block|{
name|trimCache
argument_list|()
expr_stmt|;
block|}
block|}
if|else if
condition|(
name|cache
operator|.
name|size
argument_list|()
operator|>
name|cacheMaxSize
condition|)
block|{
comment|//trim cache regardless of cacheHitRatio
while|while
condition|(
name|cache
operator|.
name|size
argument_list|()
operator|>
name|cacheMaxSize
condition|)
block|{
name|trimCache
argument_list|()
expr_stmt|;
block|}
block|}
block|}
specifier|private
name|void
name|trimCache
parameter_list|()
block|{
name|DblLinkedList
name|leastRecent
init|=
name|getLeastRecent
argument_list|()
decl_stmt|;
name|cache
operator|.
name|remove
argument_list|(
name|leastRecent
operator|.
name|cacheKey
argument_list|)
expr_stmt|;
if|if
condition|(
name|cache
operator|.
name|size
argument_list|()
operator|>
literal|0
condition|)
block|{
name|removeFromLinks
argument_list|(
name|leastRecent
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|mostRecent
operator|=
literal|null
expr_stmt|;
block|}
block|}
specifier|private
name|DblLinkedList
name|getLeastRecent
parameter_list|()
block|{
name|DblLinkedList
name|trail
init|=
name|mostRecent
decl_stmt|;
while|while
condition|(
name|trail
operator|.
name|next
operator|!=
literal|null
condition|)
name|trail
operator|=
name|trail
operator|.
name|next
expr_stmt|;
return|return
name|trail
return|;
block|}
specifier|private
class|class
name|DblLinkedList
block|{
name|Object
name|cacheKey
decl_stmt|;
name|Object
name|cacheValue
decl_stmt|;
name|DblLinkedList
name|prev
decl_stmt|;
name|DblLinkedList
name|next
decl_stmt|;
specifier|public
name|DblLinkedList
parameter_list|(
name|Object
name|key
parameter_list|,
name|Object
name|value
parameter_list|)
block|{
name|this
operator|.
name|cacheKey
operator|=
name|key
expr_stmt|;
name|this
operator|.
name|cacheValue
operator|=
name|value
expr_stmt|;
block|}
block|}
block|}
end_class

end_unit

