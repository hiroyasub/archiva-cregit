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
name|services
operator|.
name|utils
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
name|archiva
operator|.
name|rest
operator|.
name|api
operator|.
name|model
operator|.
name|ArtifactContentEntry
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Comparator
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactContentEntryComparator
implements|implements
name|Comparator
argument_list|<
name|ArtifactContentEntry
argument_list|>
block|{
specifier|public
specifier|static
specifier|final
name|ArtifactContentEntryComparator
name|INSTANCE
init|=
operator|new
name|ArtifactContentEntryComparator
argument_list|()
decl_stmt|;
specifier|public
name|int
name|compare
parameter_list|(
name|ArtifactContentEntry
name|artifactContentEntry
parameter_list|,
name|ArtifactContentEntry
name|artifactContentEntry1
parameter_list|)
block|{
comment|// include depth too in comparaison ?
return|return
name|artifactContentEntry
operator|.
name|getText
argument_list|()
operator|.
name|compareTo
argument_list|(
name|artifactContentEntry1
operator|.
name|getText
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

