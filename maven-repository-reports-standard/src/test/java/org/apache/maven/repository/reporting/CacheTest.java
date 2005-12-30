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
comment|/*  * Copyright 2005-2006 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_comment
comment|/**  *  */
end_comment

begin_class
specifier|public
class|class
name|CacheTest
extends|extends
name|TestCase
block|{
specifier|private
name|Cache
name|cache
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|double
name|CACHE_HIT_RATIO
init|=
literal|0.5
decl_stmt|;
specifier|private
specifier|static
specifier|final
name|double
name|CACHE_HIT_RATIO_THRESHOLD
init|=
literal|0.75
decl_stmt|;
specifier|public
name|void
name|testCacheManagementBasedOnHitsRatio
parameter_list|()
block|{
name|cache
operator|=
operator|new
name|Cache
argument_list|(
name|CACHE_HIT_RATIO
argument_list|)
expr_stmt|;
name|newCacheObjectTests
argument_list|()
expr_stmt|;
name|String
name|key
init|=
literal|"key"
decl_stmt|;
name|String
name|value
init|=
literal|"value"
decl_stmt|;
for|for
control|(
name|int
name|ctr
init|=
literal|1
init|;
name|ctr
operator|<
literal|10
condition|;
name|ctr
operator|++
control|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
operator|+
name|ctr
argument_list|,
name|value
operator|+
name|ctr
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|cache
operator|.
name|getHitRate
argument_list|()
operator|<
name|CACHE_HIT_RATIO_THRESHOLD
condition|)
block|{
name|cache
operator|.
name|get
argument_list|(
literal|"key2"
argument_list|)
expr_stmt|;
block|}
name|cache
operator|.
name|put
argument_list|(
literal|"key10"
argument_list|,
literal|"value10"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"first key must be expired"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCacheManagementBasedOnCacheSize
parameter_list|()
block|{
name|cache
operator|=
operator|new
name|Cache
argument_list|(
literal|9
argument_list|)
expr_stmt|;
name|newCacheObjectTests
argument_list|()
expr_stmt|;
name|String
name|key
init|=
literal|"key"
decl_stmt|;
name|String
name|value
init|=
literal|"value"
decl_stmt|;
for|for
control|(
name|int
name|ctr
init|=
literal|1
init|;
name|ctr
operator|<
literal|10
condition|;
name|ctr
operator|++
control|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
operator|+
name|ctr
argument_list|,
name|value
operator|+
name|ctr
argument_list|)
expr_stmt|;
block|}
name|cache
operator|.
name|put
argument_list|(
literal|"key10"
argument_list|,
literal|"value10"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"first key must be expired"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check cache size to be max size"
argument_list|,
literal|9
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCacheManagementBasedOnCacheSizeAndHitRate
parameter_list|()
block|{
name|cache
operator|=
operator|new
name|Cache
argument_list|(
name|CACHE_HIT_RATIO
argument_list|,
literal|9
argument_list|)
expr_stmt|;
name|newCacheObjectTests
argument_list|()
expr_stmt|;
name|String
name|key
init|=
literal|"key"
decl_stmt|;
name|String
name|value
init|=
literal|"value"
decl_stmt|;
for|for
control|(
name|int
name|ctr
init|=
literal|1
init|;
name|ctr
operator|<
literal|5
condition|;
name|ctr
operator|++
control|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
operator|+
name|ctr
argument_list|,
name|value
operator|+
name|ctr
argument_list|)
expr_stmt|;
block|}
while|while
condition|(
name|cache
operator|.
name|getHitRate
argument_list|()
operator|<
name|CACHE_HIT_RATIO
condition|)
block|{
name|cache
operator|.
name|get
argument_list|(
literal|"key3"
argument_list|)
expr_stmt|;
block|}
name|cache
operator|.
name|put
argument_list|(
literal|"key10"
argument_list|,
literal|"value10"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"first key must be expired"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"key1"
argument_list|)
argument_list|)
expr_stmt|;
while|while
condition|(
name|cache
operator|.
name|getHitRate
argument_list|()
operator|>=
name|CACHE_HIT_RATIO
condition|)
block|{
name|cache
operator|.
name|get
argument_list|(
literal|"key11"
argument_list|)
expr_stmt|;
block|}
for|for
control|(
name|int
name|ctr
init|=
literal|5
init|;
name|ctr
operator|<
literal|10
condition|;
name|ctr
operator|++
control|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
operator|+
name|ctr
argument_list|,
name|value
operator|+
name|ctr
argument_list|)
expr_stmt|;
block|}
name|cache
operator|.
name|put
argument_list|(
literal|"key11"
argument_list|,
literal|"value11"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"second key must be expired"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"key2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check cache size to be max size"
argument_list|,
literal|9
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCacheOnRedundantData
parameter_list|()
block|{
name|cache
operator|=
operator|new
name|Cache
argument_list|(
name|CACHE_HIT_RATIO
argument_list|,
literal|9
argument_list|)
expr_stmt|;
name|newCacheObjectTests
argument_list|()
expr_stmt|;
name|String
name|key
init|=
literal|"key"
decl_stmt|;
name|String
name|value
init|=
literal|"value"
decl_stmt|;
for|for
control|(
name|int
name|ctr
init|=
literal|1
init|;
name|ctr
operator|<
literal|10
condition|;
name|ctr
operator|++
control|)
block|{
name|cache
operator|.
name|put
argument_list|(
name|key
operator|+
name|ctr
argument_list|,
name|value
operator|+
name|ctr
argument_list|)
expr_stmt|;
block|}
name|cache
operator|.
name|put
argument_list|(
literal|"key1"
argument_list|,
literal|"value1"
argument_list|)
expr_stmt|;
name|cache
operator|.
name|put
argument_list|(
literal|"key10"
argument_list|,
literal|"value10"
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"second key must be gone"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"key2"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check cache size to be max size"
argument_list|,
literal|9
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
name|void
name|newCacheObjectTests
parameter_list|()
block|{
name|assertEquals
argument_list|(
operator|(
name|double
operator|)
literal|0
argument_list|,
name|cache
operator|.
name|getHitRate
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check cache size"
argument_list|,
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|String
name|value
init|=
literal|"value"
decl_stmt|;
name|String
name|key
init|=
literal|"key"
decl_stmt|;
name|cache
operator|.
name|put
argument_list|(
name|key
argument_list|,
name|value
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check cache hit"
argument_list|,
name|value
argument_list|,
name|cache
operator|.
name|get
argument_list|(
name|key
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|double
operator|)
literal|1
argument_list|,
name|cache
operator|.
name|getHitRate
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check cache size"
argument_list|,
literal|1
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"check cache miss"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"none"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|CACHE_HIT_RATIO
argument_list|,
name|cache
operator|.
name|getHitRate
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|cache
operator|.
name|flush
argument_list|()
expr_stmt|;
name|assertNull
argument_list|(
literal|"check flushed object"
argument_list|,
name|cache
operator|.
name|get
argument_list|(
literal|"key"
argument_list|)
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
operator|(
name|double
operator|)
literal|0
argument_list|,
name|cache
operator|.
name|getHitRate
argument_list|()
argument_list|,
literal|0
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check flushed cache size"
argument_list|,
literal|0
argument_list|,
name|cache
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|cache
operator|.
name|flush
argument_list|()
expr_stmt|;
block|}
block|}
end_class

end_unit

