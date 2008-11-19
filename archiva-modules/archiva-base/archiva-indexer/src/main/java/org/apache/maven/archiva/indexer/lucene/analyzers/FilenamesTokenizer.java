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
name|lucene
operator|.
name|analyzers
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
name|CharTokenizer
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Reader
import|;
end_import

begin_comment
comment|/**  * Lucene Tokenizer for {@link BytecodeKeys#FILES} fields.   *  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|FilenamesTokenizer
extends|extends
name|CharTokenizer
block|{
specifier|public
name|FilenamesTokenizer
parameter_list|(
name|Reader
name|reader
parameter_list|)
block|{
name|super
argument_list|(
name|reader
argument_list|)
expr_stmt|;
block|}
comment|/**      * Determine Token Character.      *       * The field is a list of full filenames "/home/archiva/foo/readme.txt" seperated by      * newline characters. "\n".      *       * Identify newline "\n" and "/" as the token delimiters.      */
specifier|protected
name|boolean
name|isTokenChar
parameter_list|(
name|char
name|c
parameter_list|)
block|{
return|return
operator|(
operator|(
name|c
operator|!=
literal|'\n'
operator|)
operator|&&
operator|(
name|c
operator|!=
literal|'/'
operator|)
operator|)
return|;
block|}
block|}
end_class

end_unit

