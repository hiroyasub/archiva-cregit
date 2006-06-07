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
operator|.
name|query
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
comment|/**  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|QueryTest
extends|extends
name|TestCase
block|{
specifier|public
name|void
name|testSinglePhraseQueryObject
parameter_list|()
block|{
name|SinglePhraseQuery
name|query
init|=
operator|new
name|SinglePhraseQuery
argument_list|(
literal|"Field"
argument_list|,
literal|"Value"
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
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
name|testCompoundQueries
parameter_list|()
block|{
name|CompoundQuery
name|rQuery
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|rQuery
operator|.
name|and
argument_list|(
operator|new
name|SinglePhraseQuery
argument_list|(
literal|"r1Field"
argument_list|,
literal|"r1Value"
argument_list|)
argument_list|)
expr_stmt|;
name|rQuery
operator|.
name|and
argument_list|(
operator|new
name|SinglePhraseQuery
argument_list|(
literal|"r2Field"
argument_list|,
literal|"r2Value"
argument_list|)
argument_list|)
expr_stmt|;
name|CompoundQuery
name|oQuery
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|oQuery
operator|.
name|or
argument_list|(
operator|new
name|SinglePhraseQuery
argument_list|(
literal|"oField"
argument_list|,
literal|"oValue"
argument_list|)
argument_list|)
expr_stmt|;
name|CompoundQuery
name|all
init|=
operator|new
name|CompoundQuery
argument_list|()
decl_stmt|;
name|all
operator|.
name|and
argument_list|(
name|rQuery
argument_list|)
expr_stmt|;
name|all
operator|.
name|or
argument_list|(
name|oQuery
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|all
operator|.
name|getQueries
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|CompoundQueryTerm
name|queryTerm
init|=
operator|(
name|CompoundQueryTerm
operator|)
name|all
operator|.
name|getQueries
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertTrue
argument_list|(
name|queryTerm
operator|.
name|getQuery
argument_list|()
operator|instanceof
name|CompoundQuery
argument_list|)
expr_stmt|;
name|rQuery
operator|=
operator|(
name|CompoundQuery
operator|)
name|queryTerm
operator|.
name|getQuery
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|rQuery
operator|.
name|getQueries
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|queryTerm
operator|=
operator|(
name|CompoundQueryTerm
operator|)
name|rQuery
operator|.
name|getQueries
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queryTerm
operator|.
name|getQuery
argument_list|()
operator|instanceof
name|SinglePhraseQuery
argument_list|)
expr_stmt|;
name|SinglePhraseQuery
name|sQuery
init|=
operator|(
name|SinglePhraseQuery
operator|)
name|queryTerm
operator|.
name|getQuery
argument_list|()
decl_stmt|;
name|assertEquals
argument_list|(
literal|"r1Field"
argument_list|,
name|sQuery
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"r1Value"
argument_list|,
name|sQuery
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|queryTerm
operator|=
operator|(
name|CompoundQueryTerm
operator|)
name|rQuery
operator|.
name|getQueries
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queryTerm
operator|.
name|getQuery
argument_list|()
operator|instanceof
name|SinglePhraseQuery
argument_list|)
expr_stmt|;
name|sQuery
operator|=
operator|(
name|SinglePhraseQuery
operator|)
name|queryTerm
operator|.
name|getQuery
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"r2Field"
argument_list|,
name|sQuery
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"r2Value"
argument_list|,
name|sQuery
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
name|queryTerm
operator|=
operator|(
name|CompoundQueryTerm
operator|)
name|all
operator|.
name|getQueries
argument_list|()
operator|.
name|get
argument_list|(
literal|1
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queryTerm
operator|.
name|getQuery
argument_list|()
operator|instanceof
name|CompoundQuery
argument_list|)
expr_stmt|;
name|rQuery
operator|=
operator|(
name|CompoundQuery
operator|)
name|queryTerm
operator|.
name|getQuery
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|rQuery
operator|.
name|getQueries
argument_list|()
operator|.
name|size
argument_list|()
argument_list|)
expr_stmt|;
name|queryTerm
operator|=
operator|(
name|CompoundQueryTerm
operator|)
name|rQuery
operator|.
name|getQueries
argument_list|()
operator|.
name|get
argument_list|(
literal|0
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
name|queryTerm
operator|.
name|getQuery
argument_list|()
operator|instanceof
name|SinglePhraseQuery
argument_list|)
expr_stmt|;
name|sQuery
operator|=
operator|(
name|SinglePhraseQuery
operator|)
name|queryTerm
operator|.
name|getQuery
argument_list|()
expr_stmt|;
name|assertEquals
argument_list|(
literal|"oField"
argument_list|,
name|sQuery
operator|.
name|getField
argument_list|()
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|"oValue"
argument_list|,
name|sQuery
operator|.
name|getValue
argument_list|()
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

