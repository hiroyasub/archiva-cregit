begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|repository
operator|.
name|content
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Basic artifact types.  *<ul>  *<li>{@link #MAIN}: Standard type</li>  *<li>{@link #METADATA}: if this artifact represents a metadata file</li>  *<li>{@link #RELATED}: artifact that is related to a main artifact</li>  *<li>{@link #UNKNOWN}: Unknown type</li>  *</ul>  *  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_enum
specifier|public
enum|enum
name|BaseArtifactTypes
implements|implements
name|ArtifactType
block|{
name|MAIN
block|,
name|RELATED
block|,
name|METADATA
block|,
name|UNKNOWN
block|}
end_enum

end_unit

