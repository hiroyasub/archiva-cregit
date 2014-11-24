begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|cassandra
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|cassandra
operator|.
name|serializers
operator|.
name|LongSerializer
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|cassandra
operator|.
name|serializers
operator|.
name|SerializerTypeInferer
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|cassandra
operator|.
name|serializers
operator|.
name|StringSerializer
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|cassandra
operator|.
name|service
operator|.
name|template
operator|.
name|ColumnFamilyUpdater
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|Serializer
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|beans
operator|.
name|ColumnSlice
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|beans
operator|.
name|HColumn
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|factory
operator|.
name|HFactory
import|;
end_import

begin_import
import|import
name|me
operator|.
name|prettyprint
operator|.
name|hector
operator|.
name|api
operator|.
name|mutation
operator|.
name|Mutator
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|metadata
operator|.
name|repository
operator|.
name|cassandra
operator|.
name|model
operator|.
name|ColumnNames
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|lang
operator|.
name|StringUtils
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 2.0.0  */
end_comment

begin_class
specifier|public
class|class
name|CassandraUtils
block|{
specifier|private
specifier|static
specifier|final
name|String
name|EMPTY_VALUE
init|=
literal|""
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|SEPARATOR
init|=
literal|"->"
decl_stmt|;
specifier|public
specifier|static
name|String
name|generateKey
parameter_list|(
specifier|final
name|String
modifier|...
name|bases
parameter_list|)
block|{
specifier|final
name|StringBuilder
name|builder
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
if|if
condition|(
name|bases
operator|==
literal|null
operator|||
name|bases
operator|.
name|length
operator|==
literal|0
condition|)
block|{
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
for|for
control|(
specifier|final
name|String
name|s
range|:
name|bases
control|)
block|{
if|if
condition|(
name|s
operator|!=
literal|null
condition|)
block|{
name|builder
operator|.
name|append
argument_list|(
name|s
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|builder
operator|.
name|append
argument_list|(
name|EMPTY_VALUE
argument_list|)
expr_stmt|;
block|}
name|builder
operator|.
name|append
argument_list|(
name|SEPARATOR
argument_list|)
expr_stmt|;
block|}
if|if
condition|(
name|builder
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|)
block|{
name|builder
operator|.
name|setLength
argument_list|(
name|builder
operator|.
name|length
argument_list|()
operator|-
name|SEPARATOR
operator|.
name|length
argument_list|()
argument_list|)
expr_stmt|;
block|}
return|return
name|builder
operator|.
name|toString
argument_list|()
return|;
block|}
specifier|public
specifier|static
parameter_list|<
name|A
parameter_list|,
name|B
parameter_list|>
name|HColumn
argument_list|<
name|A
argument_list|,
name|B
argument_list|>
name|column
parameter_list|(
specifier|final
name|A
name|name
parameter_list|,
specifier|final
name|B
name|value
parameter_list|)
block|{
return|return
name|HFactory
operator|.
name|createColumn
argument_list|(
name|name
argument_list|,
comment|//
name|value
argument_list|,
comment|//
operator|(
name|Serializer
argument_list|<
name|A
argument_list|>
operator|)
name|SerializerTypeInferer
operator|.
name|getSerializer
argument_list|(
name|name
argument_list|)
argument_list|,
comment|//
operator|(
name|Serializer
argument_list|<
name|B
argument_list|>
operator|)
name|SerializerTypeInferer
operator|.
name|getSerializer
argument_list|(
name|value
argument_list|)
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getStringValue
parameter_list|(
name|ColumnSlice
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|columnSlice
parameter_list|,
name|ColumnNames
name|columnName
parameter_list|)
block|{
return|return
name|getStringValue
argument_list|(
name|columnSlice
argument_list|,
name|columnName
operator|.
name|toString
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|String
name|getStringValue
parameter_list|(
name|ColumnSlice
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|columnSlice
parameter_list|,
name|String
name|columnName
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|columnName
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|HColumn
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hColumn
init|=
name|columnSlice
operator|.
name|getColumnByName
argument_list|(
name|columnName
argument_list|)
decl_stmt|;
return|return
name|hColumn
operator|==
literal|null
condition|?
literal|null
else|:
name|hColumn
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|Long
name|getLongValue
parameter_list|(
name|ColumnSlice
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|columnSlice
parameter_list|,
name|String
name|columnName
parameter_list|)
block|{
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|columnName
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|HColumn
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|hColumn
init|=
name|columnSlice
operator|.
name|getColumnByName
argument_list|(
name|columnName
argument_list|)
decl_stmt|;
return|return
name|hColumn
operator|==
literal|null
condition|?
literal|null
else|:
name|hColumn
operator|.
name|getValue
argument_list|()
return|;
block|}
specifier|public
specifier|static
name|String
name|getAsStringValue
parameter_list|(
name|ColumnSlice
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|columnSlice
parameter_list|,
name|String
name|columnName
parameter_list|)
block|{
name|StringSerializer
name|ss
init|=
name|StringSerializer
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|columnName
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|HColumn
argument_list|<
name|String
argument_list|,
name|Long
argument_list|>
name|hColumn
init|=
name|columnSlice
operator|.
name|getColumnByName
argument_list|(
name|columnName
argument_list|)
decl_stmt|;
return|return
name|hColumn
operator|==
literal|null
condition|?
literal|null
else|:
name|ss
operator|.
name|fromByteBuffer
argument_list|(
name|hColumn
operator|.
name|getValueBytes
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|Long
name|getAsLongValue
parameter_list|(
name|ColumnSlice
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|columnSlice
parameter_list|,
name|String
name|columnName
parameter_list|)
block|{
name|LongSerializer
name|ls
init|=
name|LongSerializer
operator|.
name|get
argument_list|()
decl_stmt|;
if|if
condition|(
name|StringUtils
operator|.
name|isEmpty
argument_list|(
name|columnName
argument_list|)
condition|)
block|{
return|return
literal|null
return|;
block|}
name|HColumn
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|hColumn
init|=
name|columnSlice
operator|.
name|getColumnByName
argument_list|(
name|columnName
argument_list|)
decl_stmt|;
return|return
name|hColumn
operator|==
literal|null
condition|?
literal|null
else|:
name|ls
operator|.
name|fromByteBuffer
argument_list|(
name|hColumn
operator|.
name|getValueBytes
argument_list|()
argument_list|)
return|;
block|}
specifier|public
specifier|static
name|void
name|addInsertion
parameter_list|(
name|Mutator
argument_list|<
name|String
argument_list|>
name|mutator
parameter_list|,
name|String
name|key
parameter_list|,
name|String
name|columnFamily
parameter_list|,
name|String
name|columnName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|!=
literal|null
condition|)
block|{
name|mutator
operator|.
name|addInsertion
argument_list|(
name|key
argument_list|,
name|columnFamily
argument_list|,
name|column
argument_list|(
name|columnName
argument_list|,
name|value
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * null check on the value to prevent {@link java.lang.IllegalArgumentException}      * @param updater      * @param columnName      * @param value      */
specifier|public
specifier|static
name|void
name|addUpdateStringValue
parameter_list|(
name|ColumnFamilyUpdater
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|updater
parameter_list|,
name|String
name|columnName
parameter_list|,
name|String
name|value
parameter_list|)
block|{
if|if
condition|(
name|value
operator|==
literal|null
condition|)
block|{
return|return;
block|}
name|updater
operator|.
name|setString
argument_list|(
name|columnName
argument_list|,
name|value
argument_list|)
expr_stmt|;
block|}
specifier|private
name|CassandraUtils
parameter_list|()
block|{
comment|// no-op
block|}
block|}
end_class

end_unit

