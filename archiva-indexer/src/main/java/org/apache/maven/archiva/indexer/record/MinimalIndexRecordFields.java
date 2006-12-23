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
name|indexer
operator|.
name|record
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * The fields in a minimal artifact index record.  *  * @author<a href="mailto:brett@apache.org">Brett Porter</a>  * @todo should be an enum  */
end_comment

begin_class
specifier|public
class|class
name|MinimalIndexRecordFields
block|{
specifier|public
specifier|static
specifier|final
name|String
name|FILENAME
init|=
literal|"j"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|LAST_MODIFIED
init|=
literal|"d"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|FILE_SIZE
init|=
literal|"s"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|MD5
init|=
literal|"m"
decl_stmt|;
specifier|public
specifier|static
specifier|final
name|String
name|CLASSES
init|=
literal|"c"
decl_stmt|;
specifier|private
name|MinimalIndexRecordFields
parameter_list|()
block|{
comment|// No touchy!
block|}
block|}
end_class

end_unit

