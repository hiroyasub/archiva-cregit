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
name|bytecode
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
name|lucene
operator|.
name|analysis
operator|.
name|Analyzer
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
name|MultiFieldQueryParser
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
name|maven
operator|.
name|archiva
operator|.
name|indexer
operator|.
name|lucene
operator|.
name|LuceneEntryConverter
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
name|LuceneIndexHandlers
import|;
end_import

begin_comment
comment|/**  * BytecodeHandlers   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|BytecodeHandlers
implements|implements
name|LuceneIndexHandlers
block|{
specifier|private
name|BytecodeAnalyzer
name|analyzer
decl_stmt|;
specifier|private
name|BytecodeEntryConverter
name|converter
decl_stmt|;
specifier|private
name|QueryParser
name|queryParser
decl_stmt|;
specifier|public
name|BytecodeHandlers
parameter_list|()
block|{
name|converter
operator|=
operator|new
name|BytecodeEntryConverter
argument_list|()
expr_stmt|;
name|analyzer
operator|=
operator|new
name|BytecodeAnalyzer
argument_list|()
expr_stmt|;
name|queryParser
operator|=
operator|new
name|MultiFieldQueryParser
argument_list|(
operator|new
name|String
index|[]
block|{
name|BytecodeKeys
operator|.
name|GROUPID
block|,
name|BytecodeKeys
operator|.
name|ARTIFACTID
block|,
name|BytecodeKeys
operator|.
name|VERSION
block|,
name|BytecodeKeys
operator|.
name|CLASSIFIER
block|,
name|BytecodeKeys
operator|.
name|TYPE
block|,
name|BytecodeKeys
operator|.
name|CLASSES
block|,
name|BytecodeKeys
operator|.
name|FILES
block|,
name|BytecodeKeys
operator|.
name|METHODS
block|}
argument_list|,
name|analyzer
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|BytecodeKeys
operator|.
name|ID
return|;
block|}
specifier|public
name|Analyzer
name|getAnalyzer
parameter_list|()
block|{
return|return
name|analyzer
return|;
block|}
specifier|public
name|LuceneEntryConverter
name|getConverter
parameter_list|()
block|{
return|return
name|converter
return|;
block|}
specifier|public
name|QueryParser
name|getQueryParser
parameter_list|()
block|{
return|return
name|queryParser
return|;
block|}
block|}
end_class

end_unit

