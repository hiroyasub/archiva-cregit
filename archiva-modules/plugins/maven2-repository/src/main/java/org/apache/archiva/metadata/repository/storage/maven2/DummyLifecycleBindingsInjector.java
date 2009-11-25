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
name|storage
operator|.
name|maven2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|model
operator|.
name|Model
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|model
operator|.
name|building
operator|.
name|ModelBuildingRequest
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|model
operator|.
name|building
operator|.
name|ModelProblemCollector
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|model
operator|.
name|plugin
operator|.
name|LifecycleBindingsInjector
import|;
end_import

begin_comment
comment|/**  * Required as plexus-spring doesn't understand the optional = true argument added to Plexus and used here.  *  * @plexus.component role="org.apache.maven.model.plugin.LifecycleBindingsInjector"  */
end_comment

begin_class
specifier|public
class|class
name|DummyLifecycleBindingsInjector
implements|implements
name|LifecycleBindingsInjector
block|{
specifier|public
name|void
name|injectLifecycleBindings
parameter_list|(
name|Model
name|model
parameter_list|,
name|ModelBuildingRequest
name|modelBuildingRequest
parameter_list|,
name|ModelProblemCollector
name|modelProblemCollector
parameter_list|)
block|{
comment|// left intentionally blank
block|}
block|}
end_class

end_unit

