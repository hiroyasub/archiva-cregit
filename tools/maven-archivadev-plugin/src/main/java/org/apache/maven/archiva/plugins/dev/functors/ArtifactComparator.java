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
name|plugins
operator|.
name|dev
operator|.
name|functors
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
name|artifact
operator|.
name|Artifact
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
comment|/**  * ArtifactComparator   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_class
specifier|public
class|class
name|ArtifactComparator
implements|implements
name|Comparator
block|{
specifier|public
name|int
name|compare
parameter_list|(
name|Object
name|arg0
parameter_list|,
name|Object
name|arg1
parameter_list|)
block|{
if|if
condition|(
name|arg0
operator|==
literal|null
operator|||
name|arg1
operator|==
literal|null
condition|)
block|{
return|return
operator|-
literal|1
return|;
block|}
name|Artifact
name|artifact1
init|=
operator|(
name|Artifact
operator|)
name|arg0
decl_stmt|;
name|Artifact
name|artifact2
init|=
operator|(
name|Artifact
operator|)
name|arg1
decl_stmt|;
name|int
name|diff
decl_stmt|;
name|diff
operator|=
name|artifact1
operator|.
name|getGroupId
argument_list|()
operator|.
name|compareTo
argument_list|(
name|artifact2
operator|.
name|getGroupId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|0
condition|)
block|{
return|return
name|diff
return|;
block|}
name|diff
operator|=
name|artifact1
operator|.
name|getArtifactId
argument_list|()
operator|.
name|compareTo
argument_list|(
name|artifact2
operator|.
name|getArtifactId
argument_list|()
argument_list|)
expr_stmt|;
if|if
condition|(
name|diff
operator|!=
literal|0
condition|)
block|{
return|return
name|diff
return|;
block|}
return|return
name|artifact1
operator|.
name|getVersion
argument_list|()
operator|.
name|compareTo
argument_list|(
name|artifact2
operator|.
name|getVersion
argument_list|()
argument_list|)
return|;
block|}
block|}
end_class

end_unit

