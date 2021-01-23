begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|v2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|Schema
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
name|repository
operator|.
name|ManagedRepository
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
name|repository
operator|.
name|RemoteRepository
import|;
end_import

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
name|Locale
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  * @since 3.0  */
end_comment

begin_class
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Repository data"
argument_list|)
specifier|public
class|class
name|Repository
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|4741025877287175182L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CHARACTERISTIC_MANAGED
init|=
literal|"managed"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CHARACTERISTIC_REMOTE
init|=
literal|"remote"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CHARACTERISTIC_UNKNOWN
init|=
literal|"unknown"
decl_stmt|;
specifier|protected
name|String
name|id
decl_stmt|;
specifier|protected
name|String
name|name
decl_stmt|;
specifier|protected
name|String
name|description
decl_stmt|;
specifier|protected
name|String
name|type
decl_stmt|;
specifier|protected
name|String
name|characteristic
decl_stmt|;
specifier|protected
name|String
name|location
decl_stmt|;
specifier|protected
name|boolean
name|scanned
decl_stmt|;
specifier|protected
name|String
name|schedulingDefinition
decl_stmt|;
specifier|protected
name|boolean
name|index
decl_stmt|;
specifier|protected
name|String
name|layout
decl_stmt|;
specifier|public
name|Repository
parameter_list|( )
block|{
block|}
specifier|public
specifier|static
name|Repository
name|of
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|Repository
name|repository
parameter_list|)
block|{
name|Repository
name|newRepo
init|=
operator|new
name|Repository
argument_list|( )
decl_stmt|;
name|newRepo
operator|.
name|setId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setName
argument_list|(
name|repository
operator|.
name|getName
argument_list|( )
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setDescription
argument_list|(
name|repository
operator|.
name|getDescription
argument_list|( )
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setLocation
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
operator|.
name|toASCIIString
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setIndex
argument_list|(
name|repository
operator|.
name|hasIndex
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setLayout
argument_list|(
name|repository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setType
argument_list|(
name|repository
operator|.
name|getType
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setScanned
argument_list|(
name|repository
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setSchedulingDefinition
argument_list|(
name|repository
operator|.
name|getSchedulingDefinition
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|repository
operator|instanceof
name|RemoteRepository
condition|)
block|{
name|newRepo
operator|.
name|setCharacteristic
argument_list|(
name|CHARACTERISTIC_REMOTE
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|repository
operator|instanceof
name|ManagedRepository
condition|)
block|{
name|newRepo
operator|.
name|setCharacteristic
argument_list|(
name|CHARACTERISTIC_MANAGED
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newRepo
operator|.
name|setCharacteristic
argument_list|(
name|CHARACTERISTIC_UNKNOWN
argument_list|)
expr_stmt|;
block|}
return|return
name|newRepo
return|;
block|}
specifier|public
specifier|static
name|Repository
name|of
parameter_list|(
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|Repository
name|repository
parameter_list|,
name|Locale
name|locale
parameter_list|)
block|{
name|Locale
name|myLocale
decl_stmt|;
if|if
condition|(
name|locale
operator|==
literal|null
condition|)
block|{
name|myLocale
operator|=
name|Locale
operator|.
name|getDefault
argument_list|( )
expr_stmt|;
block|}
else|else
block|{
name|myLocale
operator|=
name|locale
expr_stmt|;
block|}
name|Repository
name|newRepo
init|=
operator|new
name|Repository
argument_list|( )
decl_stmt|;
name|newRepo
operator|.
name|setId
argument_list|(
name|repository
operator|.
name|getId
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setName
argument_list|(
name|repository
operator|.
name|getName
argument_list|(
name|myLocale
argument_list|)
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setDescription
argument_list|(
name|repository
operator|.
name|getDescription
argument_list|(
name|myLocale
argument_list|)
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setLocation
argument_list|(
name|repository
operator|.
name|getLocation
argument_list|()
operator|.
name|toASCIIString
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setIndex
argument_list|(
name|repository
operator|.
name|hasIndex
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setLayout
argument_list|(
name|repository
operator|.
name|getLayout
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setType
argument_list|(
name|repository
operator|.
name|getType
argument_list|()
operator|.
name|name
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setScanned
argument_list|(
name|repository
operator|.
name|isScanned
argument_list|()
argument_list|)
expr_stmt|;
name|newRepo
operator|.
name|setSchedulingDefinition
argument_list|(
name|repository
operator|.
name|getSchedulingDefinition
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|repository
operator|instanceof
name|RemoteRepository
condition|)
block|{
name|newRepo
operator|.
name|setCharacteristic
argument_list|(
name|CHARACTERISTIC_REMOTE
argument_list|)
expr_stmt|;
block|}
if|else if
condition|(
name|repository
operator|instanceof
name|ManagedRepository
condition|)
block|{
name|newRepo
operator|.
name|setCharacteristic
argument_list|(
name|CHARACTERISTIC_MANAGED
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|newRepo
operator|.
name|setCharacteristic
argument_list|(
name|CHARACTERISTIC_UNKNOWN
argument_list|)
expr_stmt|;
block|}
return|return
name|newRepo
return|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Category of the repository. Either 'managed' or 'remote'."
argument_list|)
specifier|public
name|String
name|getCharacteristic
parameter_list|( )
block|{
return|return
name|characteristic
return|;
block|}
specifier|public
name|void
name|setCharacteristic
parameter_list|(
name|String
name|characteristic
parameter_list|)
block|{
name|this
operator|.
name|characteristic
operator|=
name|characteristic
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Unique identifier of the repository"
argument_list|)
specifier|public
name|String
name|getId
parameter_list|( )
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
name|id
operator|=
name|id
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Display name of the repository"
argument_list|)
specifier|public
name|String
name|getName
parameter_list|( )
block|{
return|return
name|name
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
name|name
operator|=
name|name
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Description of the repository"
argument_list|)
specifier|public
name|String
name|getDescription
parameter_list|( )
block|{
return|return
name|description
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
name|description
operator|=
name|description
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Repository type"
argument_list|)
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
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"The location, where the repository data can be found"
argument_list|)
specifier|public
name|String
name|getLocation
parameter_list|( )
block|{
return|return
name|location
return|;
block|}
specifier|public
name|void
name|setLocation
parameter_list|(
name|String
name|location
parameter_list|)
block|{
name|this
operator|.
name|location
operator|=
name|location
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"True, if this repository is scanned regularly"
argument_list|)
specifier|public
name|boolean
name|isScanned
parameter_list|( )
block|{
return|return
name|scanned
return|;
block|}
specifier|public
name|void
name|setScanned
parameter_list|(
name|boolean
name|scanned
parameter_list|)
block|{
name|this
operator|.
name|scanned
operator|=
name|scanned
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"scheduling_definition"
argument_list|,
name|description
operator|=
literal|"Definition of regular scheduled scan"
argument_list|)
specifier|public
name|String
name|getSchedulingDefinition
parameter_list|( )
block|{
return|return
name|schedulingDefinition
return|;
block|}
specifier|public
name|void
name|setSchedulingDefinition
parameter_list|(
name|String
name|schedulingDefinition
parameter_list|)
block|{
name|this
operator|.
name|schedulingDefinition
operator|=
name|schedulingDefinition
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"True, if this is a indexed repository"
argument_list|)
specifier|public
name|boolean
name|isIndex
parameter_list|( )
block|{
return|return
name|index
return|;
block|}
specifier|public
name|void
name|setIndex
parameter_list|(
name|boolean
name|index
parameter_list|)
block|{
name|this
operator|.
name|index
operator|=
name|index
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Layout type is implementation specific"
argument_list|)
specifier|public
name|String
name|getLayout
parameter_list|( )
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
name|layout
operator|=
name|layout
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|( )
operator|!=
name|o
operator|.
name|getClass
argument_list|( )
condition|)
return|return
literal|false
return|;
name|Repository
name|that
init|=
operator|(
name|Repository
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|scanned
operator|!=
name|that
operator|.
name|scanned
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|index
operator|!=
name|that
operator|.
name|index
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|id
operator|!=
literal|null
condition|?
operator|!
name|id
operator|.
name|equals
argument_list|(
name|that
operator|.
name|id
argument_list|)
else|:
name|that
operator|.
name|id
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|name
operator|!=
literal|null
condition|?
operator|!
name|name
operator|.
name|equals
argument_list|(
name|that
operator|.
name|name
argument_list|)
else|:
name|that
operator|.
name|name
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|description
operator|!=
literal|null
condition|?
operator|!
name|description
operator|.
name|equals
argument_list|(
name|that
operator|.
name|description
argument_list|)
else|:
name|that
operator|.
name|description
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|type
operator|!=
literal|null
condition|?
operator|!
name|type
operator|.
name|equals
argument_list|(
name|that
operator|.
name|type
argument_list|)
else|:
name|that
operator|.
name|type
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|location
operator|!=
literal|null
condition|?
operator|!
name|location
operator|.
name|equals
argument_list|(
name|that
operator|.
name|location
argument_list|)
else|:
name|that
operator|.
name|location
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|schedulingDefinition
operator|!=
literal|null
condition|?
operator|!
name|schedulingDefinition
operator|.
name|equals
argument_list|(
name|that
operator|.
name|schedulingDefinition
argument_list|)
else|:
name|that
operator|.
name|schedulingDefinition
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
return|return
name|layout
operator|!=
literal|null
condition|?
name|layout
operator|.
name|equals
argument_list|(
name|that
operator|.
name|layout
argument_list|)
else|:
name|that
operator|.
name|layout
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|( )
block|{
name|int
name|result
init|=
name|id
operator|!=
literal|null
condition|?
name|id
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|name
operator|!=
literal|null
condition|?
name|name
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|description
operator|!=
literal|null
condition|?
name|description
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|type
operator|!=
literal|null
condition|?
name|type
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|location
operator|!=
literal|null
condition|?
name|location
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|scanned
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|schedulingDefinition
operator|!=
literal|null
condition|?
name|schedulingDefinition
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|index
condition|?
literal|1
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|layout
operator|!=
literal|null
condition|?
name|layout
operator|.
name|hashCode
argument_list|( )
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
name|String
name|toString
parameter_list|( )
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"Repository{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"id='"
argument_list|)
operator|.
name|append
argument_list|(
name|id
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", name='"
argument_list|)
operator|.
name|append
argument_list|(
name|name
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", description='"
argument_list|)
operator|.
name|append
argument_list|(
name|description
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", type='"
argument_list|)
operator|.
name|append
argument_list|(
name|type
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", location='"
argument_list|)
operator|.
name|append
argument_list|(
name|location
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", scanned="
argument_list|)
operator|.
name|append
argument_list|(
name|scanned
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", schedulingDefinition='"
argument_list|)
operator|.
name|append
argument_list|(
name|schedulingDefinition
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", index="
argument_list|)
operator|.
name|append
argument_list|(
name|index
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", layout='"
argument_list|)
operator|.
name|append
argument_list|(
name|layout
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|( )
return|;
block|}
block|}
end_class

end_unit

