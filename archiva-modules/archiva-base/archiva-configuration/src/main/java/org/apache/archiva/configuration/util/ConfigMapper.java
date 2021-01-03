begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
operator|.
name|util
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
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
name|Arrays
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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|BiFunction
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|function
operator|.
name|Function
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * Helper class that can be used for mapping configuration keys (e.g. user configuration keys) to  * archiva configuration objects.  *  * @param<T> The class used to retrieve the attribute data  * @param<K> The class used to retrieve the data that is for prefix matching  * @author Martin Stockhammer<martin_s@apache.org>  * @since 3.0  */
end_comment

begin_class
specifier|public
class|class
name|ConfigMapper
parameter_list|<
name|T
parameter_list|,
name|K
parameter_list|>
block|{
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
argument_list|>
name|stringFunctionMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|Integer
argument_list|>
argument_list|>
name|intFunctionMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|Boolean
argument_list|>
argument_list|>
name|booleanFunctionMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
specifier|private
specifier|final
name|Map
argument_list|<
name|String
argument_list|,
name|BiFunction
argument_list|<
name|String
argument_list|,
name|K
argument_list|,
name|String
argument_list|>
argument_list|>
name|prefixStringFunctionMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
specifier|public
name|void
name|addStringMapping
parameter_list|(
name|String
name|attributeName
parameter_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|String
argument_list|>
name|mapping
parameter_list|)
block|{
name|this
operator|.
name|stringFunctionMap
operator|.
name|put
argument_list|(
name|attributeName
argument_list|,
name|mapping
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|addPrefixStringMapping
parameter_list|(
name|String
name|prefix
parameter_list|,
name|BiFunction
argument_list|<
name|String
argument_list|,
name|K
argument_list|,
name|String
argument_list|>
name|mapping
parameter_list|)
block|{
name|prefixStringFunctionMap
operator|.
name|put
argument_list|(
name|prefix
argument_list|,
name|mapping
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getString
parameter_list|(
name|String
name|attributeName
parameter_list|,
name|T
name|instance
parameter_list|)
block|{
return|return
name|stringFunctionMap
operator|.
name|get
argument_list|(
name|attributeName
argument_list|)
operator|.
name|apply
argument_list|(
name|instance
argument_list|)
return|;
block|}
specifier|public
name|String
name|getPrefixString
parameter_list|(
name|String
name|attributeName
parameter_list|,
name|K
name|instance
parameter_list|)
block|{
name|BiFunction
argument_list|<
name|String
argument_list|,
name|K
argument_list|,
name|String
argument_list|>
name|function
init|=
name|prefixStringFunctionMap
operator|.
name|entrySet
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|filter
argument_list|(
name|entry
lambda|->
name|attributeName
operator|.
name|startsWith
argument_list|(
name|entry
operator|.
name|getKey
argument_list|( )
argument_list|)
argument_list|)
operator|.
name|findFirst
argument_list|( )
operator|.
name|map
argument_list|(
name|entry
lambda|->
name|entry
operator|.
name|getValue
argument_list|( )
argument_list|)
operator|.
name|get
argument_list|( )
decl_stmt|;
return|return
name|function
operator|.
name|apply
argument_list|(
name|attributeName
argument_list|,
name|instance
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isStringMapping
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
return|return
name|stringFunctionMap
operator|.
name|containsKey
argument_list|(
name|attributeName
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isIntMapping
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
return|return
name|intFunctionMap
operator|.
name|containsKey
argument_list|(
name|attributeName
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isBooleanMapping
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
return|return
name|booleanFunctionMap
operator|.
name|containsKey
argument_list|(
name|attributeName
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isPrefixMapping
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
return|return
name|prefixStringFunctionMap
operator|.
name|keySet
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|anyMatch
argument_list|(
name|prefix
lambda|->
name|attributeName
operator|.
name|startsWith
argument_list|(
name|prefix
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|boolean
name|isMapping
parameter_list|(
name|String
name|attributeName
parameter_list|)
block|{
return|return
name|isStringMapping
argument_list|(
name|attributeName
argument_list|)
operator|||
name|isIntMapping
argument_list|(
name|attributeName
argument_list|)
operator|||
name|isBooleanMapping
argument_list|(
name|attributeName
argument_list|)
return|;
block|}
specifier|public
name|void
name|addIntMapping
parameter_list|(
name|String
name|attributeName
parameter_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|Integer
argument_list|>
name|mapping
parameter_list|)
block|{
name|this
operator|.
name|intFunctionMap
operator|.
name|put
argument_list|(
name|attributeName
argument_list|,
name|mapping
argument_list|)
expr_stmt|;
block|}
specifier|public
name|int
name|getInt
parameter_list|(
name|String
name|attributeName
parameter_list|,
name|T
name|instance
parameter_list|)
block|{
return|return
name|this
operator|.
name|intFunctionMap
operator|.
name|get
argument_list|(
name|attributeName
argument_list|)
operator|.
name|apply
argument_list|(
name|instance
argument_list|)
return|;
block|}
specifier|public
name|void
name|addBooleanMapping
parameter_list|(
name|String
name|attributeName
parameter_list|,
name|Function
argument_list|<
name|T
argument_list|,
name|Boolean
argument_list|>
name|mapping
parameter_list|)
block|{
name|this
operator|.
name|booleanFunctionMap
operator|.
name|put
argument_list|(
name|attributeName
argument_list|,
name|mapping
argument_list|)
expr_stmt|;
block|}
specifier|public
name|boolean
name|getBoolean
parameter_list|(
name|String
name|attributeName
parameter_list|,
name|T
name|instance
parameter_list|)
block|{
return|return
name|this
operator|.
name|booleanFunctionMap
operator|.
name|get
argument_list|(
name|attributeName
argument_list|)
operator|.
name|apply
argument_list|(
name|instance
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getStringAttributes
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|stringFunctionMap
operator|.
name|keySet
argument_list|( )
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getIntAttributes
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|intFunctionMap
operator|.
name|keySet
argument_list|( )
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getBooleanAttributes
parameter_list|()
block|{
return|return
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|booleanFunctionMap
operator|.
name|keySet
argument_list|( )
argument_list|)
return|;
block|}
specifier|public
name|List
argument_list|<
name|String
argument_list|>
name|getAllAttributes
parameter_list|()
block|{
return|return
name|Arrays
operator|.
name|asList
argument_list|(
name|stringFunctionMap
argument_list|,
name|intFunctionMap
argument_list|,
name|booleanFunctionMap
argument_list|)
operator|.
name|stream
argument_list|()
operator|.
name|flatMap
argument_list|(
name|map
lambda|->
name|map
operator|.
name|keySet
argument_list|()
operator|.
name|stream
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toList
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

