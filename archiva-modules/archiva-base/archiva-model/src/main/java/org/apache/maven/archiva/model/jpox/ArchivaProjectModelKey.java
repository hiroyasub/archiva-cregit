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
name|model
operator|.
name|jpox
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
name|maven
operator|.
name|archiva
operator|.
name|model
operator|.
name|AbstractVersionedKey
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

begin_comment
comment|/**  * ArchivaProjectModelKey - unique classid-key for JPOX.   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaProjectModelKey
extends|extends
name|AbstractVersionedKey
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|7789859208617327581L
decl_stmt|;
specifier|public
name|ArchivaProjectModelKey
parameter_list|()
block|{
block|}
specifier|public
name|ArchivaProjectModelKey
parameter_list|(
name|String
name|key
parameter_list|)
block|{
name|super
argument_list|(
name|key
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

