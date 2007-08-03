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
name|database
operator|.
name|updater
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
name|commons
operator|.
name|collections
operator|.
name|CollectionUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|codehaus
operator|.
name|plexus
operator|.
name|PlexusTestCase
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

begin_comment
comment|/**  * DatabaseConsumersTest   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|DatabaseConsumersTest
extends|extends
name|PlexusTestCase
block|{
specifier|private
name|DatabaseConsumers
name|lookupDbConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|DatabaseConsumers
name|dbconsumers
init|=
operator|(
name|DatabaseConsumers
operator|)
name|lookup
argument_list|(
name|DatabaseConsumers
operator|.
name|class
argument_list|)
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"DatabaseConsumers should not be null."
argument_list|,
name|dbconsumers
argument_list|)
expr_stmt|;
return|return
name|dbconsumers
return|;
block|}
specifier|public
name|void
name|testGetAvailableCleanupConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|DatabaseConsumers
name|dbconsumers
init|=
name|lookupDbConsumers
argument_list|()
decl_stmt|;
name|List
name|available
init|=
name|dbconsumers
operator|.
name|getAvailableCleanupConsumers
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Available Cleanup Consumers should never be null."
argument_list|,
name|available
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Available Cleanup Consumers should have entries."
argument_list|,
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|available
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetAvailableUnprocessedConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|DatabaseConsumers
name|dbconsumers
init|=
name|lookupDbConsumers
argument_list|()
decl_stmt|;
name|List
name|available
init|=
name|dbconsumers
operator|.
name|getAvailableUnprocessedConsumers
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Available Unprocessed Consumers should never be null."
argument_list|,
name|available
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Available Unprocessed Consumers should have entries."
argument_list|,
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|available
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetSelectedCleanupConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|DatabaseConsumers
name|dbconsumers
init|=
name|lookupDbConsumers
argument_list|()
decl_stmt|;
name|List
name|available
init|=
name|dbconsumers
operator|.
name|getSelectedCleanupConsumers
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Selected Cleanup Consumers should never be null."
argument_list|,
name|available
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Selected Cleanup Consumers should have entries."
argument_list|,
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|available
argument_list|)
argument_list|)
expr_stmt|;
block|}
specifier|public
name|void
name|testGetSelectedUnprocessedConsumers
parameter_list|()
throws|throws
name|Exception
block|{
name|DatabaseConsumers
name|dbconsumers
init|=
name|lookupDbConsumers
argument_list|()
decl_stmt|;
name|List
name|available
init|=
name|dbconsumers
operator|.
name|getSelectedUnprocessedConsumers
argument_list|()
decl_stmt|;
name|assertNotNull
argument_list|(
literal|"Selected Unprocessed Consumers should never be null."
argument_list|,
name|available
argument_list|)
expr_stmt|;
name|assertTrue
argument_list|(
literal|"Selected Unprocessed Consumers should have entries."
argument_list|,
name|CollectionUtils
operator|.
name|isNotEmpty
argument_list|(
name|available
argument_list|)
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

