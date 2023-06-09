begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|admin
operator|.
name|model
operator|.
name|beans
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|Serializable
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Collections
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
name|Locale
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
name|stream
operator|.
name|Collectors
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M1  */
end_comment

begin_class
specifier|public
class|class
name|AbstractRepository
implements|implements
name|Serializable
block|{
specifier|private
name|Locale
name|defaultLocale
init|=
name|Locale
operator|.
name|getDefault
argument_list|()
decl_stmt|;
specifier|private
name|String
name|type
decl_stmt|;
specifier|private
name|String
name|id
decl_stmt|;
comment|/*      * @since 3.0.0 as Map      */
specifier|private
name|Map
argument_list|<
name|Locale
argument_list|,
name|String
argument_list|>
name|name
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(  )
decl_stmt|;
comment|/*      * @since 3.0.0 as Map      */
specifier|private
name|Map
argument_list|<
name|Locale
argument_list|,
name|String
argument_list|>
name|description
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|(  )
decl_stmt|;
specifier|private
name|String
name|layout
init|=
literal|"default"
decl_stmt|;
specifier|private
name|String
name|indexDirectory
decl_stmt|;
comment|/*      * @since 3.0.0      */
specifier|private
name|String
name|packedIndexDirectory
decl_stmt|;
specifier|private
name|String
name|toStringCache
init|=
literal|null
decl_stmt|;
specifier|public
name|AbstractRepository
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|AbstractRepository
parameter_list|(
name|Locale
name|defaultLocale
parameter_list|)
block|{
name|this
operator|.
name|defaultLocale
operator|=
name|defaultLocale
expr_stmt|;
block|}
specifier|public
name|AbstractRepository
parameter_list|(
name|Locale
name|defaultLocale
parameter_list|,
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|layout
parameter_list|)
block|{
name|this
operator|.
name|defaultLocale
operator|=
name|defaultLocale
expr_stmt|;
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
block|}
specifier|public
name|AbstractRepository
parameter_list|(
name|String
name|id
parameter_list|,
name|String
name|name
parameter_list|,
name|String
name|layout
parameter_list|)
block|{
name|setId
argument_list|(
name|id
argument_list|)
expr_stmt|;
name|setName
argument_list|(
name|name
argument_list|)
expr_stmt|;
name|setLayout
argument_list|(
name|layout
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getId
parameter_list|()
block|{
return|return
name|id
return|;
block|}
specifier|public
name|void
name|setId
parameter_list|(
name|String
name|id
parameter_list|)
block|{
name|this
operator|.
name|toStringCache
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|id
operator|=
name|id
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
operator|.
name|get
argument_list|(
name|defaultLocale
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getNames
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|name
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
return|return
name|this
operator|.
name|name
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|toLanguageTag
argument_list|()
argument_list|,
name|Map
operator|.
name|Entry
operator|::
name|getValue
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|toStringCache
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|name
operator|.
name|put
argument_list|(
name|defaultLocale
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|languageTag
parameter_list|,
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|toStringCache
operator|=
literal|null
expr_stmt|;
specifier|final
name|Locale
name|loc
init|=
name|Locale
operator|.
name|forLanguageTag
argument_list|(
name|languageTag
argument_list|)
decl_stmt|;
name|this
operator|.
name|name
operator|.
name|put
argument_list|(
name|loc
argument_list|,
name|name
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getLayout
parameter_list|()
block|{
return|return
name|layout
return|;
block|}
specifier|public
name|void
name|setLayout
parameter_list|(
name|String
name|layout
parameter_list|)
block|{
name|this
operator|.
name|toStringCache
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|layout
operator|=
name|layout
expr_stmt|;
block|}
specifier|public
name|String
name|getIndexDirectory
parameter_list|()
block|{
return|return
name|indexDirectory
return|;
block|}
specifier|public
name|void
name|setIndexDirectory
parameter_list|(
name|String
name|indexDirectory
parameter_list|)
block|{
name|this
operator|.
name|toStringCache
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|indexDirectory
operator|=
name|indexDirectory
expr_stmt|;
block|}
specifier|public
name|String
name|getDescription
parameter_list|()
block|{
return|return
name|this
operator|.
name|description
operator|.
name|get
argument_list|(
name|defaultLocale
argument_list|)
return|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getDescriptions
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|description
operator|==
literal|null
condition|)
block|{
return|return
name|Collections
operator|.
name|emptyMap
argument_list|()
return|;
block|}
return|return
name|this
operator|.
name|description
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|filter
argument_list|(
name|e
lambda|->
name|e
operator|!=
literal|null
operator|&&
name|e
operator|.
name|getKey
argument_list|()
operator|!=
literal|null
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|e
lambda|->
name|e
operator|.
name|getKey
argument_list|()
operator|.
name|toLanguageTag
argument_list|()
argument_list|,
name|e
lambda|->
name|e
operator|.
name|getValue
argument_list|()
operator|==
literal|null
condition|?
literal|""
else|:
name|e
operator|.
name|getValue
argument_list|()
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|toStringCache
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|description
operator|.
name|put
argument_list|(
name|defaultLocale
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setDescription
parameter_list|(
name|String
name|languageTag
parameter_list|,
name|String
name|description
parameter_list|)
block|{
name|this
operator|.
name|toStringCache
operator|=
literal|null
expr_stmt|;
specifier|final
name|Locale
name|loc
init|=
name|Locale
operator|.
name|forLanguageTag
argument_list|(
name|languageTag
argument_list|)
decl_stmt|;
name|this
operator|.
name|description
operator|.
name|put
argument_list|(
name|loc
argument_list|,
name|description
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
name|int
name|result
init|=
literal|17
decl_stmt|;
name|result
operator|=
literal|37
operator|*
name|result
operator|+
operator|(
name|id
operator|!=
literal|null
condition|?
name|id
operator|.
name|hashCode
argument_list|()
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|other
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|other
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|other
operator|instanceof
name|AbstractRepository
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|AbstractRepository
name|that
init|=
operator|(
name|AbstractRepository
operator|)
name|other
decl_stmt|;
name|boolean
name|result
init|=
literal|true
decl_stmt|;
name|result
operator|=
name|result
operator|&&
operator|(
name|getId
argument_list|()
operator|==
literal|null
condition|?
name|that
operator|.
name|getId
argument_list|()
operator|==
literal|null
else|:
name|getId
argument_list|()
operator|.
name|equals
argument_list|(
name|that
operator|.
name|getId
argument_list|()
argument_list|)
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
specifier|private
name|String
name|getLocaleString
parameter_list|(
name|Map
argument_list|<
name|Locale
argument_list|,
name|String
argument_list|>
name|map
parameter_list|)
block|{
return|return
name|map
operator|.
name|entrySet
argument_list|()
operator|.
name|stream
argument_list|()
operator|.
name|map
argument_list|(
name|entry
lambda|->
name|entry
operator|.
name|getKey
argument_list|()
operator|.
name|toLanguageTag
argument_list|()
operator|+
literal|'='
operator|+
name|entry
operator|.
name|getValue
argument_list|()
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|joining
argument_list|(
literal|","
argument_list|)
argument_list|)
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|( )
block|{
return|return
name|type
return|;
block|}
specifier|public
name|void
name|setType
parameter_list|(
name|String
name|type
parameter_list|)
block|{
name|toStringCache
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
specifier|public
name|String
name|getPackedIndexDirectory
parameter_list|()
block|{
return|return
name|packedIndexDirectory
return|;
block|}
specifier|public
name|void
name|setPackedIndexDirectory
parameter_list|(
name|String
name|packedIndexDirectory
parameter_list|)
block|{
name|toStringCache
operator|=
literal|null
expr_stmt|;
name|this
operator|.
name|packedIndexDirectory
operator|=
name|packedIndexDirectory
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
if|if
condition|(
name|toStringCache
operator|!=
literal|null
condition|)
block|{
return|return
name|toStringCache
return|;
block|}
else|else
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|( )
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"AbstractRepository"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{ id=\""
argument_list|)
operator|.
name|append
argument_list|(
name|id
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", type=\""
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", name=\""
argument_list|)
operator|.
name|append
argument_list|(
name|getLocaleString
argument_list|(
name|name
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", layout=\""
argument_list|)
operator|.
name|append
argument_list|(
name|layout
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", indexDirectory=\""
argument_list|)
operator|.
name|append
argument_list|(
name|indexDirectory
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", packedIndexDirectory=\""
argument_list|)
operator|.
name|append
argument_list|(
name|packedIndexDirectory
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", description=\""
argument_list|)
operator|.
name|append
argument_list|(
name|getLocaleString
argument_list|(
name|description
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
literal|'"'
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
name|toStringCache
operator|=
name|sb
operator|.
name|toString
argument_list|( )
expr_stmt|;
return|return
name|toStringCache
return|;
block|}
block|}
block|}
end_class

end_unit

