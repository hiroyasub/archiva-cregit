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
name|org
operator|.
name|slf4j
operator|.
name|Logger
import|;
end_import

begin_import
import|import
name|org
operator|.
name|slf4j
operator|.
name|LoggerFactory
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|LinkedHashSet
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Set
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|concurrent
operator|.
name|ConcurrentHashMap
import|;
end_import

begin_comment
comment|/**  * @author Martin Schreier<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
class|class
name|AbstractEventManager
implements|implements
name|EventSource
block|{
specifier|private
specifier|static
specifier|final
name|Logger
name|log
init|=
name|LoggerFactory
operator|.
name|getLogger
argument_list|(
name|AbstractEventManager
operator|.
name|class
argument_list|)
decl_stmt|;
specifier|protected
specifier|final
name|ConcurrentHashMap
argument_list|<
name|EventType
argument_list|<
name|?
extends|extends
name|Event
argument_list|>
argument_list|,
name|Set
argument_list|<
name|EventHandler
argument_list|>
argument_list|>
name|handlerMap
init|=
operator|new
name|ConcurrentHashMap
argument_list|<>
argument_list|()
decl_stmt|;
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|Event
parameter_list|>
name|void
name|registerEventHandler
parameter_list|(
name|EventType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|EventHandler
argument_list|<
name|?
super|super
name|T
argument_list|>
name|eventHandler
parameter_list|)
block|{
name|Set
argument_list|<
name|EventHandler
argument_list|>
name|handlers
init|=
name|handlerMap
operator|.
name|computeIfAbsent
argument_list|(
name|type
argument_list|,
name|t
lambda|->
operator|new
name|LinkedHashSet
argument_list|<>
argument_list|()
argument_list|)
decl_stmt|;
if|if
condition|(
operator|!
name|handlers
operator|.
name|contains
argument_list|(
name|eventHandler
argument_list|)
condition|)
block|{
name|handlers
operator|.
name|add
argument_list|(
name|eventHandler
argument_list|)
expr_stmt|;
block|}
name|log
operator|.
name|debug
argument_list|(
literal|"Event handler registered: "
operator|+
name|eventHandler
operator|.
name|getClass
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
parameter_list|<
name|T
extends|extends
name|Event
parameter_list|>
name|void
name|unregisterEventHandler
parameter_list|(
name|EventType
argument_list|<
name|T
argument_list|>
name|type
parameter_list|,
name|EventHandler
argument_list|<
name|?
super|super
name|T
argument_list|>
name|eventHandler
parameter_list|)
block|{
if|if
condition|(
name|handlerMap
operator|.
name|containsKey
argument_list|(
name|type
argument_list|)
condition|)
block|{
name|handlerMap
operator|.
name|get
argument_list|(
name|type
argument_list|)
operator|.
name|remove
argument_list|(
name|eventHandler
argument_list|)
expr_stmt|;
name|log
operator|.
name|debug
argument_list|(
literal|"Event handler unregistered: "
operator|+
name|eventHandler
operator|.
name|getClass
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
comment|/**      * Fires the given event for the given source. If the source of the provided event does not match the<code>source</code>      * parameter the event will be chained.      *      * The event will be sent to all registered event handler. Exceptions during handling are not propagated to the      * caller.      *      * @param fireEvent the event to fire      * @param source the source object      */
specifier|public
name|void
name|fireEvent
parameter_list|(
name|Event
name|fireEvent
parameter_list|,
name|Object
name|source
parameter_list|)
block|{
specifier|final
name|EventType
argument_list|<
name|?
extends|extends
name|Event
argument_list|>
name|type
init|=
name|fireEvent
operator|.
name|getType
argument_list|()
decl_stmt|;
name|Event
name|event
decl_stmt|;
if|if
condition|(
name|fireEvent
operator|.
name|getSource
argument_list|()
operator|!=
name|source
condition|)
block|{
name|event
operator|=
name|fireEvent
operator|.
name|copyFor
argument_list|(
name|source
argument_list|)
expr_stmt|;
block|}
else|else
block|{
name|event
operator|=
name|fireEvent
expr_stmt|;
block|}
for|for
control|(
name|EventType
argument_list|<
name|?
extends|extends
name|Event
argument_list|>
name|handlerType
range|:
name|handlerMap
operator|.
name|keySet
argument_list|()
control|)
block|{
if|if
condition|(
name|EventType
operator|.
name|isInstanceOf
argument_list|(
name|type
argument_list|,
name|handlerType
argument_list|)
condition|)
block|{
for|for
control|(
name|EventHandler
name|handler
range|:
name|handlerMap
operator|.
name|get
argument_list|(
name|handlerType
argument_list|)
control|)
block|{
try|try
block|{
name|handler
operator|.
name|handle
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
catch|catch
parameter_list|(
name|Throwable
name|e
parameter_list|)
block|{
comment|// We catch all errors from handlers
name|log
operator|.
name|error
argument_list|(
literal|"An error occured during event handling: {}"
argument_list|,
name|e
operator|.
name|getMessage
argument_list|()
argument_list|,
name|e
argument_list|)
expr_stmt|;
block|}
block|}
block|}
block|}
block|}
block|}
end_class

end_unit

