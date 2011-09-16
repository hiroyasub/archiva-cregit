begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|common
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Arrays
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
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_class
specifier|public
class|class
name|FileTypeUtils
block|{
comment|/**      * Default exclusions from artifact consumers that are using the file types. Note that this is simplistic in the      * case of the support files (based on extension) as it is elsewhere - it may be better to match these to actual      * artifacts and exclude later during scanning.      */
specifier|public
specifier|static
specifier|final
name|List
argument_list|<
name|String
argument_list|>
name|DEFAULT_EXCLUSIONS
init|=
name|Arrays
operator|.
name|asList
argument_list|(
literal|"**/maven-metadata.xml"
argument_list|,
literal|"**/maven-metadata-*.xml"
argument_list|,
literal|"**/*.sha1"
argument_list|,
literal|"**/*.asc"
argument_list|,
literal|"**/*.md5"
argument_list|,
literal|"**/*.pgp"
argument_list|,
literal|"**/.index/**"
argument_list|,
literal|"**/.indexer/**"
argument_list|)
decl_stmt|;
block|}
end_class

end_unit

