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
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Test
import|;
end_import

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
name|List
import|;
end_import

begin_import
import|import static
name|org
operator|.
name|junit
operator|.
name|jupiter
operator|.
name|api
operator|.
name|Assertions
operator|.
name|*
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
specifier|public
class|class
name|EventManagerTest
block|{
specifier|private
class|class
name|TestHandler
implements|implements
name|EventHandler
argument_list|<
name|Event
argument_list|>
block|{
specifier|private
name|List
argument_list|<
name|Event
argument_list|>
name|eventList
init|=
operator|new
name|ArrayList
argument_list|<>
argument_list|( )
decl_stmt|;
annotation|@
name|Override
specifier|public
name|void
name|handle
parameter_list|(
name|Event
name|event
parameter_list|)
block|{
name|eventList
operator|.
name|add
argument_list|(
name|event
argument_list|)
expr_stmt|;
block|}
specifier|public
name|List
argument_list|<
name|Event
argument_list|>
name|getEventList
parameter_list|()
block|{
return|return
name|eventList
return|;
block|}
block|}
specifier|private
name|EventType
argument_list|<
name|Event
argument_list|>
name|testType
init|=
operator|new
name|EventType
argument_list|<>
argument_list|(
literal|"TEST"
argument_list|)
decl_stmt|;
specifier|private
name|EventType
argument_list|<
name|Event
argument_list|>
name|testTestType
init|=
operator|new
name|EventType
argument_list|<>
argument_list|(
name|testType
argument_list|,
literal|"TEST.TEST"
argument_list|)
decl_stmt|;
specifier|private
name|EventType
argument_list|<
name|Event
argument_list|>
name|otherType
init|=
operator|new
name|EventType
argument_list|(
literal|"OTHER"
argument_list|)
decl_stmt|;
annotation|@
name|Test
specifier|public
name|void
name|registerEventHandler
parameter_list|( )
block|{
name|EventManager
name|eventManager
init|=
operator|new
name|EventManager
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|TestHandler
name|handler1
init|=
operator|new
name|TestHandler
argument_list|( )
decl_stmt|;
name|TestHandler
name|handler2
init|=
operator|new
name|TestHandler
argument_list|( )
decl_stmt|;
name|TestHandler
name|handler3
init|=
operator|new
name|TestHandler
argument_list|( )
decl_stmt|;
name|TestHandler
name|handler4
init|=
operator|new
name|TestHandler
argument_list|( )
decl_stmt|;
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|Event
operator|.
name|ANY
argument_list|,
name|handler1
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|testType
argument_list|,
name|handler2
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|testTestType
argument_list|,
name|handler3
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|otherType
argument_list|,
name|handler4
argument_list|)
expr_stmt|;
name|Event
name|event1
init|=
operator|new
name|Event
argument_list|(
name|testType
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|fireEvent
argument_list|(
name|event1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|handler1
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|handler2
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler3
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler4
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|Event
name|event2
init|=
operator|new
name|Event
argument_list|(
name|testTestType
argument_list|,
name|event1
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|fireEvent
argument_list|(
name|event2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|handler1
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|handler2
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|handler3
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler4
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|Event
name|event3
init|=
operator|new
name|Event
argument_list|(
name|otherType
argument_list|,
name|event1
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|fireEvent
argument_list|(
name|event3
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|3
argument_list|,
name|handler1
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|handler2
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|handler3
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|handler4
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|unregisterEventHandler
parameter_list|( )
block|{
name|EventManager
name|eventManager
init|=
operator|new
name|EventManager
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|TestHandler
name|handler1
init|=
operator|new
name|TestHandler
argument_list|( )
decl_stmt|;
name|TestHandler
name|handler2
init|=
operator|new
name|TestHandler
argument_list|( )
decl_stmt|;
name|TestHandler
name|handler3
init|=
operator|new
name|TestHandler
argument_list|( )
decl_stmt|;
name|TestHandler
name|handler4
init|=
operator|new
name|TestHandler
argument_list|( )
decl_stmt|;
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|Event
operator|.
name|ANY
argument_list|,
name|handler1
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|testType
argument_list|,
name|handler2
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|testTestType
argument_list|,
name|handler3
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|otherType
argument_list|,
name|handler4
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|unregisterEventHandler
argument_list|(
name|Event
operator|.
name|ANY
argument_list|,
name|handler1
argument_list|)
expr_stmt|;
name|Event
name|event1
init|=
operator|new
name|Event
argument_list|(
name|testType
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|fireEvent
argument_list|(
name|event1
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler1
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|handler2
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler3
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler4
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|unregisterEventHandler
argument_list|(
name|otherType
argument_list|,
name|handler2
argument_list|)
expr_stmt|;
name|Event
name|event2
init|=
operator|new
name|Event
argument_list|(
name|testType
argument_list|,
name|this
argument_list|)
decl_stmt|;
name|eventManager
operator|.
name|fireEvent
argument_list|(
name|event2
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler1
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|2
argument_list|,
name|handler2
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler3
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|0
argument_list|,
name|handler4
operator|.
name|eventList
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Test
specifier|public
name|void
name|fireEvent
parameter_list|( )
block|{
name|Object
name|other
init|=
operator|new
name|Object
argument_list|( )
decl_stmt|;
name|EventManager
name|eventManager
init|=
operator|new
name|EventManager
argument_list|(
name|this
argument_list|)
decl_stmt|;
name|assertThrows
argument_list|(
name|NullPointerException
operator|.
name|class
argument_list|,
parameter_list|( )
lambda|->
name|eventManager
operator|.
name|fireEvent
argument_list|(
literal|null
argument_list|)
argument_list|)
expr_stmt|;
name|Event
name|event
init|=
operator|new
name|Event
argument_list|(
name|EventType
operator|.
name|ROOT
argument_list|,
name|other
argument_list|)
decl_stmt|;
name|assertEquals
argument_list|(
name|other
argument_list|,
name|event
operator|.
name|getSource
argument_list|( )
argument_list|)
expr_stmt|;
name|TestHandler
name|handler
init|=
operator|new
name|TestHandler
argument_list|( )
decl_stmt|;
name|eventManager
operator|.
name|registerEventHandler
argument_list|(
name|EventType
operator|.
name|ROOT
argument_list|,
name|handler
argument_list|)
expr_stmt|;
name|eventManager
operator|.
name|fireEvent
argument_list|(
name|event
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
literal|1
argument_list|,
name|handler
operator|.
name|getEventList
argument_list|( )
operator|.
name|size
argument_list|( )
argument_list|)
expr_stmt|;
name|Event
name|newEvent
init|=
name|handler
operator|.
name|getEventList
argument_list|( )
operator|.
name|get
argument_list|(
literal|0
argument_list|)
decl_stmt|;
name|assertNotEquals
argument_list|(
name|event
argument_list|,
name|newEvent
argument_list|)
expr_stmt|;
name|assertEquals
argument_list|(
name|this
argument_list|,
name|newEvent
operator|.
name|getSource
argument_list|( )
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

