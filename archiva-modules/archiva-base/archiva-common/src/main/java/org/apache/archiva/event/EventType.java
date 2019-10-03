begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|event
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *  http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|InvalidObjectException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|ObjectStreamException
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
name|*
import|;
end_import

begin_comment
comment|/**  * Event types define a hierarchical structure of events. Each event is bound to a certain event type.  * All event types have a super type, only the root event type {@link EventType#ROOT} has no super type.  *  * Event types should be stored as static fields on the events itself.  *  * @param<T> The type class parameter allows to define the types in a type safe way and represents a event class,  *           where the type is associated to.  */
end_comment

begin_class
specifier|public
class|class
name|EventType
parameter_list|<
name|T
extends|extends
name|Event
parameter_list|>
implements|implements
name|Serializable
block|{
specifier|public
specifier|static
specifier|final
name|EventType
argument_list|<
name|Event
argument_list|>
name|ROOT
init|=
operator|new
name|EventType
argument_list|<>
argument_list|()
decl_stmt|;
specifier|private
specifier|final
name|String
name|name
decl_stmt|;
specifier|private
specifier|final
name|EventType
argument_list|<
name|?
super|super
name|T
argument_list|>
name|superType
decl_stmt|;
specifier|private
name|WeakHashMap
argument_list|<
name|EventType
argument_list|<
name|?
extends|extends
name|T
argument_list|>
argument_list|,
name|Void
argument_list|>
name|subTypes
decl_stmt|;
comment|/**      * Creates a type with the given name and the root type as parent.      * @param name the name of the new type      */
specifier|public
name|EventType
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|superType
operator|=
name|ROOT
expr_stmt|;
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
comment|/**      * Creates a event type instance with the given super type and name.      *      * @param superType The super type or<code>null</code>, if this is the root type.      * @param name      */
specifier|public
name|EventType
parameter_list|(
name|EventType
argument_list|<
name|?
super|super
name|T
argument_list|>
name|superType
parameter_list|,
name|String
name|name
parameter_list|)
block|{
if|if
condition|(
name|superType
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|NullPointerException
argument_list|(
literal|"Super Type may not be null"
argument_list|)
throw|;
block|}
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|superType
operator|=
name|superType
expr_stmt|;
name|superType
operator|.
name|register
argument_list|(
name|this
argument_list|)
expr_stmt|;
block|}
comment|/**      * Creates the root type      */
specifier|private
name|EventType
parameter_list|()
block|{
name|this
operator|.
name|name
operator|=
literal|"ROOT"
expr_stmt|;
name|this
operator|.
name|superType
operator|=
literal|null
expr_stmt|;
block|}
specifier|public
name|String
name|name
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|EventType
argument_list|<
name|?
super|super
name|T
argument_list|>
name|getSuperType
parameter_list|()
block|{
return|return
name|superType
return|;
block|}
specifier|private
name|void
name|register
parameter_list|(
name|EventType
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|subType
parameter_list|)
block|{
if|if
condition|(
name|subTypes
operator|==
literal|null
condition|)
block|{
name|subTypes
operator|=
operator|new
name|WeakHashMap
argument_list|<>
argument_list|()
expr_stmt|;
block|}
for|for
control|(
name|EventType
argument_list|<
name|?
extends|extends
name|T
argument_list|>
name|t
range|:
name|subTypes
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
operator|(
operator|(
name|t
operator|.
name|name
operator|==
literal|null
operator|&&
name|subType
operator|.
name|name
operator|==
literal|null
operator|)
operator|||
operator|(
name|t
operator|.
name|name
operator|!=
literal|null
operator|&&
name|t
operator|.
name|name
operator|.
name|equals
argument_list|(
name|subType
operator|.
name|name
argument_list|)
operator|)
operator|)
condition|)
block|{
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"EventType \""
operator|+
name|subType
operator|+
literal|"\""
operator|+
literal|"with parent \""
operator|+
name|subType
operator|.
name|getSuperType
argument_list|()
operator|+
literal|"\" already exists"
argument_list|)
throw|;
block|}
block|}
name|subTypes
operator|.
name|put
argument_list|(
name|subType
argument_list|,
literal|null
argument_list|)
expr_stmt|;
block|}
specifier|public
specifier|static
name|List
argument_list|<
name|EventType
argument_list|<
name|?
argument_list|>
argument_list|>
name|fetchSuperTypes
parameter_list|(
name|EventType
argument_list|<
name|?
argument_list|>
name|type
parameter_list|)
block|{
name|List
argument_list|<
name|EventType
argument_list|<
name|?
argument_list|>
argument_list|>
name|typeList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|()
decl_stmt|;
name|EventType
argument_list|<
name|?
argument_list|>
name|cType
init|=
name|type
decl_stmt|;
while|while
condition|(
name|cType
operator|!=
literal|null
condition|)
block|{
name|typeList
operator|.
name|add
argument_list|(
name|cType
argument_list|)
expr_stmt|;
name|cType
operator|=
name|cType
operator|.
name|getSuperType
argument_list|()
expr_stmt|;
block|}
return|return
name|typeList
return|;
block|}
specifier|public
specifier|static
name|boolean
name|isInstanceOf
parameter_list|(
name|EventType
argument_list|<
name|?
argument_list|>
name|type
parameter_list|,
name|EventType
argument_list|<
name|?
argument_list|>
name|baseType
parameter_list|)
block|{
name|EventType
argument_list|<
name|?
argument_list|>
name|cType
init|=
name|type
decl_stmt|;
while|while
condition|(
name|cType
operator|!=
literal|null
condition|)
block|{
if|if
condition|(
name|cType
operator|==
name|baseType
condition|)
block|{
return|return
literal|true
return|;
block|}
name|cType
operator|=
name|cType
operator|.
name|getSuperType
argument_list|()
expr_stmt|;
block|}
return|return
literal|false
return|;
block|}
specifier|private
name|Object
name|writeReplace
parameter_list|()
throws|throws
name|ObjectStreamException
block|{
name|Deque
argument_list|<
name|String
argument_list|>
name|path
init|=
operator|new
name|LinkedList
argument_list|<
name|String
argument_list|>
argument_list|()
decl_stmt|;
name|EventType
argument_list|<
name|?
argument_list|>
name|t
init|=
name|this
decl_stmt|;
while|while
condition|(
name|t
operator|!=
name|ROOT
condition|)
block|{
name|path
operator|.
name|addFirst
argument_list|(
name|t
operator|.
name|name
argument_list|)
expr_stmt|;
name|t
operator|=
name|t
operator|.
name|superType
expr_stmt|;
block|}
return|return
operator|new
name|EventTypeSerialization
argument_list|(
operator|new
name|ArrayList
argument_list|<>
argument_list|(
name|path
argument_list|)
argument_list|)
return|;
block|}
specifier|static
class|class
name|EventTypeSerialization
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|1841649460281865547L
decl_stmt|;
specifier|private
name|List
argument_list|<
name|String
argument_list|>
name|path
decl_stmt|;
specifier|public
name|EventTypeSerialization
parameter_list|(
name|List
argument_list|<
name|String
argument_list|>
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
specifier|private
name|Object
name|readResolve
parameter_list|()
throws|throws
name|ObjectStreamException
block|{
name|EventType
name|t
init|=
name|ROOT
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|path
operator|.
name|size
argument_list|()
condition|;
operator|++
name|i
control|)
block|{
name|String
name|p
init|=
name|path
operator|.
name|get
argument_list|(
name|i
argument_list|)
decl_stmt|;
if|if
condition|(
name|t
operator|.
name|subTypes
operator|!=
literal|null
condition|)
block|{
name|EventType
argument_list|<
name|?
argument_list|>
name|s
init|=
name|findSubType
argument_list|(
name|t
operator|.
name|subTypes
operator|.
name|keySet
argument_list|()
argument_list|,
name|p
argument_list|)
decl_stmt|;
if|if
condition|(
name|s
operator|==
literal|null
condition|)
block|{
throw|throw
operator|new
name|InvalidObjectException
argument_list|(
literal|"Cannot find event type \""
operator|+
name|p
operator|+
literal|"\" (of "
operator|+
name|t
operator|+
literal|")"
argument_list|)
throw|;
block|}
name|t
operator|=
name|s
expr_stmt|;
block|}
else|else
block|{
throw|throw
operator|new
name|InvalidObjectException
argument_list|(
literal|"Cannot find event type \""
operator|+
name|p
operator|+
literal|"\" (of "
operator|+
name|t
operator|+
literal|")"
argument_list|)
throw|;
block|}
block|}
return|return
name|t
return|;
block|}
specifier|private
name|EventType
argument_list|<
name|?
argument_list|>
name|findSubType
parameter_list|(
name|Set
argument_list|<
name|EventType
argument_list|>
name|subTypes
parameter_list|,
name|String
name|name
parameter_list|)
block|{
for|for
control|(
name|EventType
name|t
range|:
name|subTypes
control|)
block|{
if|if
condition|(
operator|(
operator|(
name|t
operator|.
name|name
operator|==
literal|null
operator|&&
name|name
operator|==
literal|null
operator|)
operator|||
operator|(
name|t
operator|.
name|name
operator|!=
literal|null
operator|&&
name|t
operator|.
name|name
operator|.
name|equals
argument_list|(
name|name
argument_list|)
operator|)
operator|)
condition|)
block|{
return|return
name|t
return|;
block|}
block|}
return|return
literal|null
return|;
block|}
block|}
block|}
end_class

end_unit

