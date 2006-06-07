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
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|index
operator|.
name|Term
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
name|queryParser
operator|.
name|ParseException
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
name|queryParser
operator|.
name|QueryParser
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
name|search
operator|.
name|TermQuery
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
name|repository
operator|.
name|indexing
operator|.
name|RepositoryIndex
import|;
end_import

begin_comment
comment|/**  * Class to hold a single field search condition  *  * @author Edwin Punzalan  */
end_comment

begin_class
specifier|public
class|class
name|SinglePhraseQuery
implements|implements
name|Query
block|{
specifier|private
name|String
name|field
decl_stmt|;
specifier|private
name|String
name|value
decl_stmt|;
comment|/**      * Class constructor      *      * @param field the index field to search      * @param value the index value requirement      */
specifier|public
name|SinglePhraseQuery
parameter_list|(
name|String
name|field
parameter_list|,
name|String
name|value
parameter_list|)
block|{
name|this
operator|.
name|field
operator|=
name|field
expr_stmt|;
name|this
operator|.
name|value
operator|=
name|value
expr_stmt|;
block|}
comment|/**      * Method to retrieve the name of the index field searched      *      * @return the name of the index field      */
specifier|public
name|String
name|getField
parameter_list|()
block|{
return|return
name|field
return|;
block|}
comment|/**      * Method to retrieve the value used in searching the index field      *      * @return the value to corresspond the index field      */
specifier|public
name|String
name|getValue
parameter_list|()
block|{
return|return
name|value
return|;
block|}
specifier|public
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|Query
name|createLuceneQuery
parameter_list|(
name|RepositoryIndex
name|index
parameter_list|)
throws|throws
name|ParseException
block|{
name|org
operator|.
name|apache
operator|.
name|lucene
operator|.
name|search
operator|.
name|Query
name|qry
decl_stmt|;
if|if
condition|(
name|index
operator|.
name|isKeywordField
argument_list|(
name|this
operator|.
name|field
argument_list|)
condition|)
block|{
name|Term
name|term
init|=
operator|new
name|Term
argument_list|(
name|this
operator|.
name|field
argument_list|,
name|this
operator|.
name|value
argument_list|)
decl_stmt|;
name|qry
operator|=
operator|new
name|TermQuery
argument_list|(
name|term
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|QueryParser
name|parser
init|=
operator|new
name|QueryParser
argument_list|(
name|this
operator|.
name|field
argument_list|,
name|index
operator|.
name|getAnalyzer
argument_list|()
argument_list|)
decl_stmt|;
name|qry
operator|=
name|parser
operator|.
name|parse
argument_list|(
name|this
operator|.
name|value
argument_list|)
expr_stmt|;
block|}
return|return
name|qry
return|;
block|}
block|}
end_class

end_unit

