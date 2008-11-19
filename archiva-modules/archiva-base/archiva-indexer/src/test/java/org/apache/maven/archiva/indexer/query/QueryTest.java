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
name|query
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Iterator
import|;
end_import

begin_comment
comment|/**  */
end_comment

begin_class
specifier|public
class|class
name|QueryTest
extends|extends
name|TestCase
block|{
specifier|private
name|QueryTerm
name|term1
init|=
operator|new
name|QueryTerm
argument_list|(
literal|"field1"
argument_list|,
literal|"value1"
argument_list|)
decl_stmt|;
specifier|private
name|QueryTerm
name|term2
init|=
operator|new
name|QueryTerm
argument_list|(
literal|"field2"
argument_list|,
literal|"value2"
argument_list|)
decl_stmt|;
specifier|private
name|QueryTerm
name|term3
init|=
operator|new
name|QueryTerm
argument_list|(
literal|"field3"
argument_list|,
literal|"value3"
argument_list|)
decl_stmt|;
specifier|public
name|void
name|testQueryTerm
parameter_list|()
block|{
name|QueryTerm
name|query
init|=
operator|new
name|QueryTerm
argument_list|(
literal|"Field"
argument_list|,
literal|"Value"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check field setting"
argument_list|,
literal|"Field"
argument_list|,
name|query
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check value setting"
argument_list|,
literal|"Value"
argument_list|,
name|query
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testSingleTermQuery
parameter_list|()
block|{
name|SingleTermQuery
name|query
init|=
operator|new
name|SingleTermQuery
argument_list|(
literal|"Field"
argument_list|,
literal|"Value"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"check field setting"
argument_list|,
literal|"Field"
argument_list|,
name|query
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check value setting"
argument_list|,
literal|"Value"
argument_list|,
name|query
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|=
operator|new
name|SingleTermQuery
argument_list|(
name|term1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check field setting"
argument_list|,
literal|"field1"
argument_list|,
name|query
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"check value setting"
argument_list|,
literal|"value1"
argument_list|,
name|query
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRangeQueryOpen
parameter_list|()
block|{
name|RangeQuery
name|rangeQuery
init|=
name|RangeQuery
operator|.
name|createOpenRange
argument_list|()
decl_stmt|;
name|assertNull
argument_list|(
literal|"Check range has no start"
argument_list|,
name|rangeQuery
operator|.
name|getBegin
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Check range has no end"
argument_list|,
name|rangeQuery
operator|.
name|getEnd
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRangeQueryExclusive
parameter_list|()
block|{
name|RangeQuery
name|rangeQuery
init|=
name|RangeQuery
operator|.
name|createExclusiveRange
argument_list|(
name|term1
argument_list|,
name|term2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check range start"
argument_list|,
name|term1
argument_list|,
name|rangeQuery
operator|.
name|getBegin
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check range end"
argument_list|,
name|term2
argument_list|,
name|rangeQuery
operator|.
name|getEnd
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check exclusive"
argument_list|,
name|rangeQuery
operator|.
name|isInclusive
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRangeQueryInclusive
parameter_list|()
block|{
name|RangeQuery
name|rangeQuery
init|=
name|RangeQuery
operator|.
name|createInclusiveRange
argument_list|(
name|term1
argument_list|,
name|term2
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check range start"
argument_list|,
name|term1
argument_list|,
name|rangeQuery
operator|.
name|getBegin
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check range end"
argument_list|,
name|term2
argument_list|,
name|rangeQuery
operator|.
name|getEnd
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check inclusive"
argument_list|,
name|rangeQuery
operator|.
name|isInclusive
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testRangeQueryOpenEnded
parameter_list|()
block|{
name|RangeQuery
name|rangeQuery
init|=
name|RangeQuery
operator|.
name|createGreaterThanOrEqualToRange
argument_list|(
name|term1
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check range start"
argument_list|,
name|term1
argument_list|,
name|rangeQuery
operator|.
name|getBegin
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Check range end"
argument_list|,
name|rangeQuery
operator|.
name|getEnd
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check inclusive"
argument_list|,
name|rangeQuery
operator|.
name|isInclusive
argument_list|()
argument_list|)
expr_stmt|;
name|rangeQuery
operator|=
name|RangeQuery
operator|.
name|createGreaterThanRange
argument_list|(
name|term1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check range start"
argument_list|,
name|term1
argument_list|,
name|rangeQuery
operator|.
name|getBegin
argument_list|()
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Check range end"
argument_list|,
name|rangeQuery
operator|.
name|getEnd
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check exclusive"
argument_list|,
name|rangeQuery
operator|.
name|isInclusive
argument_list|()
argument_list|)
expr_stmt|;
name|rangeQuery
operator|=
name|RangeQuery
operator|.
name|createLessThanOrEqualToRange
argument_list|(
name|term1
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Check range start"
argument_list|,
name|rangeQuery
operator|.
name|getBegin
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check range end"
argument_list|,
name|term1
argument_list|,
name|rangeQuery
operator|.
name|getEnd
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check inclusive"
argument_list|,
name|rangeQuery
operator|.
name|isInclusive
argument_list|()
argument_list|)
expr_stmt|;
name|rangeQuery
operator|=
name|RangeQuery
operator|.
name|createLessThanRange
argument_list|(
name|term1
argument_list|)
expr_stmt|;
name|assertNull
argument_list|(
literal|"Check range start"
argument_list|,
name|rangeQuery
operator|.
name|getBegin
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check range end"
argument_list|,
name|term1
argument_list|,
name|rangeQuery
operator|.
name|getEnd
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check exclusive"
argument_list|,
name|rangeQuery
operator|.
name|isInclusive
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testCompundQuery
parameter_list|()
block|{
name|CompoundQuery
name|query
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|assertTrue
argument_list|(
literal|"check query is empty"
argument_list|,
name|query
operator|.
name|getCompoundQueryTerms
argument_list|()
operator|.
name|isEmpty
argument_list|()
argument_list|)
expr_stmt|;
name|query
operator|.
name|and
argument_list|(
name|term1
argument_list|)
expr_stmt|;
name|query
operator|.
name|or
argument_list|(
name|term2
argument_list|)
expr_stmt|;
name|query
operator|.
name|not
argument_list|(
name|term3
argument_list|)
expr_stmt|;
name|Iterator
name|i
init|=
name|query
operator|.
name|getCompoundQueryTerms
argument_list|()
operator|.
name|iterator
argument_list|()
decl_stmt|;
name|CompoundQueryTerm
name|term
init|=
operator|(
name|CompoundQueryTerm
operator|)
name|i
operator|.
name|next
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"Check first term"
argument_list|,
literal|"field1"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check first term"
argument_list|,
literal|"value1"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check first term"
argument_list|,
name|term
operator|.
name|isRequired
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check first term"
argument_list|,
name|term
operator|.
name|isProhibited
argument_list|()
argument_list|)
expr_stmt|;
name|term
operator|=
operator|(
name|CompoundQueryTerm
operator|)
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check second term"
argument_list|,
literal|"field2"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check second term"
argument_list|,
literal|"value2"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check second term"
argument_list|,
name|term
operator|.
name|isRequired
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check second term"
argument_list|,
name|term
operator|.
name|isProhibited
argument_list|()
argument_list|)
expr_stmt|;
name|term
operator|=
operator|(
name|CompoundQueryTerm
operator|)
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check third term"
argument_list|,
literal|"field3"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check third term"
argument_list|,
literal|"value3"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check third term"
argument_list|,
name|term
operator|.
name|isRequired
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check third term"
argument_list|,
name|term
operator|.
name|isProhibited
argument_list|()
argument_list|)
expr_stmt|;
name|CompoundQuery
name|query2
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|query2
operator|.
name|and
argument_list|(
name|query
argument_list|)
expr_stmt|;
name|query2
operator|.
name|or
argument_list|(
operator|new
name|SingleTermQuery
argument_list|(
name|term2
argument_list|)
argument_list|)
expr_stmt|;
name|query2
operator|.
name|not
argument_list|(
operator|new
name|SingleTermQuery
argument_list|(
name|term3
argument_list|)
argument_list|)
expr_stmt|;
name|i
operator|=
name|query2
operator|.
name|getCompoundQueryTerms
argument_list|()
operator|.
name|iterator
argument_list|()
expr_stmt|;
name|term
operator|=
operator|(
name|CompoundQueryTerm
operator|)
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check first term"
argument_list|,
name|query
argument_list|,
name|term
operator|.
name|getQuery
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check first term"
argument_list|,
name|term
operator|.
name|isRequired
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check first term"
argument_list|,
name|term
operator|.
name|isProhibited
argument_list|()
argument_list|)
expr_stmt|;
name|term
operator|=
operator|(
name|CompoundQueryTerm
operator|)
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check second term"
argument_list|,
literal|"field2"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check second term"
argument_list|,
literal|"value2"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check second term"
argument_list|,
name|term
operator|.
name|isRequired
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check second term"
argument_list|,
name|term
operator|.
name|isProhibited
argument_list|()
argument_list|)
expr_stmt|;
name|term
operator|=
operator|(
name|CompoundQueryTerm
operator|)
name|i
operator|.
name|next
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check third term"
argument_list|,
literal|"field3"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"Check third term"
argument_list|,
literal|"value3"
argument_list|,
name|getQuery
argument_list|(
name|term
argument_list|)
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|assertFalse
argument_list|(
literal|"Check third term"
argument_list|,
name|term
operator|.
name|isRequired
argument_list|()
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Check third term"
argument_list|,
name|term
operator|.
name|isProhibited
argument_list|()
argument_list|)
expr_stmt|;
block|}
specifier|private
specifier|static
name|SingleTermQuery
name|getQuery
parameter_list|(
name|CompoundQueryTerm
name|term
parameter_list|)
block|{
return|return
operator|(
name|SingleTermQuery
operator|)
name|term
operator|.
name|getQuery
argument_list|()
return|;
block|}
block|}
end_class

end_unit

