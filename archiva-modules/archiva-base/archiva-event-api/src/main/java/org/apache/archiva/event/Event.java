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
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|LocalDateTime
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|EventObject
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
comment|/**  * Base class for events. Events have a type and a source.  * The source is the instance that raised the event.  *  * There are different event types for a given event. The types are represented in a hierarchical structure.  *  * Events can be chained, which means a event listener can catch events and rethrow them as its own event.  *  */
end_comment

begin_class
specifier|public
class|class
name|Event
parameter_list|<
name|C
extends|extends
name|EventContext
parameter_list|>
extends|extends
name|EventObject
implements|implements
name|Cloneable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
operator|-
literal|7171846575892044990L
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|EventType
argument_list|<
name|Event
argument_list|>
name|ANY
init|=
name|EventType
operator|.
name|ROOT
decl_stmt|;
specifier|private
name|Event
name|previous
decl_stmt|;
specifier|private
specifier|final
name|EventType
argument_list|<
name|?
extends|extends
name|Event
argument_list|>
name|type
decl_stmt|;
specifier|private
specifier|final
name|LocalDateTime
name|createTime
decl_stmt|;
specifier|private
name|HashMap
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|EventContext
argument_list|>
argument_list|,
name|EventContext
argument_list|>
name|contextMap
init|=
operator|new
name|HashMap
argument_list|<>
argument_list|( )
decl_stmt|;
specifier|public
name|Event
parameter_list|(
name|EventType
argument_list|<
name|?
extends|extends
name|Event
argument_list|>
name|type
parameter_list|,
name|Object
name|originator
parameter_list|)
block|{
name|super
argument_list|(
name|originator
argument_list|)
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|type
expr_stmt|;
name|this
operator|.
name|createTime
operator|=
name|LocalDateTime
operator|.
name|now
argument_list|()
expr_stmt|;
block|}
specifier|private
name|Event
parameter_list|(
name|Event
name|previous
parameter_list|,
name|Object
name|originator
parameter_list|)
block|{
name|super
argument_list|(
name|originator
argument_list|)
expr_stmt|;
name|this
operator|.
name|previous
operator|=
name|previous
expr_stmt|;
name|this
operator|.
name|type
operator|=
name|previous
operator|.
name|getType
argument_list|()
expr_stmt|;
name|this
operator|.
name|createTime
operator|=
name|previous
operator|.
name|getCreateTime
argument_list|()
expr_stmt|;
name|this
operator|.
name|contextMap
operator|=
name|previous
operator|.
name|contextMap
expr_stmt|;
block|}
comment|/**      * Returns the event type that is associated with this event instance.      * @return the event type      */
specifier|public
name|EventType
argument_list|<
name|?
extends|extends
name|Event
argument_list|>
name|getType
parameter_list|()
block|{
return|return
name|type
return|;
block|}
empty_stmt|;
comment|/**      * Returns the time, when the event was created.      * @return      */
specifier|public
name|LocalDateTime
name|getCreateTime
parameter_list|()
block|{
return|return
name|createTime
return|;
block|}
specifier|public
parameter_list|<
name|T
extends|extends
name|EventContext
parameter_list|>
name|T
name|getContext
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|contextClazz
parameter_list|)
throws|throws
name|IllegalArgumentException
block|{
if|if
condition|(
name|contextMap
operator|.
name|containsKey
argument_list|(
name|contextClazz
argument_list|)
condition|)
block|{
return|return
operator|(
name|T
operator|)
name|contextMap
operator|.
name|get
argument_list|(
name|contextClazz
argument_list|)
return|;
block|}
else|else
block|{
name|T
name|ctx
init|=
literal|null
decl_stmt|;
for|for
control|(
name|Map
operator|.
name|Entry
argument_list|<
name|Class
argument_list|<
name|?
extends|extends
name|EventContext
argument_list|>
argument_list|,
name|EventContext
argument_list|>
name|clazzEntry
range|:
name|contextMap
operator|.
name|entrySet
argument_list|()
control|)
block|{
if|if
condition|(
name|contextClazz
operator|.
name|isAssignableFrom
argument_list|(
name|clazzEntry
operator|.
name|getKey
argument_list|()
argument_list|)
condition|)
block|{
name|ctx
operator|=
operator|(
name|T
operator|)
name|clazzEntry
operator|.
name|getValue
argument_list|( )
expr_stmt|;
break|break;
block|}
block|}
if|if
condition|(
name|ctx
operator|!=
literal|null
condition|)
block|{
name|contextMap
operator|.
name|put
argument_list|(
name|contextClazz
argument_list|,
name|ctx
argument_list|)
expr_stmt|;
return|return
name|ctx
return|;
block|}
block|}
throw|throw
operator|new
name|IllegalArgumentException
argument_list|(
literal|"No matching event context registered for "
operator|+
name|contextClazz
argument_list|)
throw|;
block|}
specifier|public
name|C
name|getContext
parameter_list|()
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|( )
throw|;
block|}
specifier|public
name|Map
argument_list|<
name|String
argument_list|,
name|String
argument_list|>
name|getContextData
parameter_list|()
block|{
return|return
name|contextMap
operator|.
name|entrySet
argument_list|( )
operator|.
name|stream
argument_list|( )
operator|.
name|flatMap
argument_list|(
name|ctx
lambda|->
name|ctx
operator|.
name|getValue
argument_list|( )
operator|.
name|getData
argument_list|( )
operator|.
name|entrySet
argument_list|( )
operator|.
name|stream
argument_list|( )
argument_list|)
operator|.
name|collect
argument_list|(
name|Collectors
operator|.
name|toMap
argument_list|(
name|Map
operator|.
name|Entry
operator|::
name|getKey
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
parameter_list|<
name|T
extends|extends
name|EventContext
parameter_list|>
name|void
name|setContext
parameter_list|(
name|Class
argument_list|<
name|T
argument_list|>
name|clazz
parameter_list|,
name|T
name|context
parameter_list|)
block|{
name|this
operator|.
name|contextMap
operator|.
name|put
argument_list|(
name|clazz
argument_list|,
name|context
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|setContext
parameter_list|(
name|C
name|context
parameter_list|)
block|{
throw|throw
operator|new
name|UnsupportedOperationException
argument_list|( )
throw|;
block|}
comment|/**      * Recreates the event with the given instance as the new source. The      * current source is stored in the previous event.      * @param newSource The new source      * @return a new event instance, where<code>this</code> is stored as previous event      */
specifier|public
name|Event
name|copyFor
parameter_list|(
name|Object
name|newSource
parameter_list|)
block|{
name|Event
name|newEvent
init|=
operator|(
name|Event
operator|)
name|this
operator|.
name|clone
argument_list|()
decl_stmt|;
name|newEvent
operator|.
name|previous
operator|=
name|this
expr_stmt|;
name|newEvent
operator|.
name|source
operator|=
name|newSource
expr_stmt|;
name|newEvent
operator|.
name|contextMap
operator|=
name|this
operator|.
name|contextMap
expr_stmt|;
return|return
name|newEvent
return|;
block|}
comment|/**      * Returns the previous event or<code>null</code>, if this is a root event.      * @return the previous event or<code>null</code>, if it does not exist      */
specifier|public
name|Event
name|getPreviousEvent
parameter_list|()
block|{
return|return
name|previous
return|;
block|}
comment|/**      * Returns<code>true</code>, if the event has a previous event.      * @return<code>true</code>, if this has a previous event, otherwise<code>false</code>      */
specifier|public
name|boolean
name|hasPreviousEvent
parameter_list|()
block|{
return|return
name|previous
operator|!=
literal|null
return|;
block|}
annotation|@
name|Override
specifier|protected
name|Object
name|clone
parameter_list|()
block|{
try|try
block|{
return|return
name|super
operator|.
name|clone
argument_list|()
return|;
block|}
catch|catch
parameter_list|(
name|CloneNotSupportedException
name|e
parameter_list|)
block|{
comment|// this should not happen
throw|throw
operator|new
name|RuntimeException
argument_list|(
literal|"Event is not clonable"
argument_list|)
throw|;
block|}
block|}
block|}
end_class

end_unit
