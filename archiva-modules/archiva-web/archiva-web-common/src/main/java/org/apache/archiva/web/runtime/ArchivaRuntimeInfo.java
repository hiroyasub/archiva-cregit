begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|web
operator|.
name|runtime
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
name|commons
operator|.
name|lang
operator|.
name|math
operator|.
name|NumberUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|springframework
operator|.
name|stereotype
operator|.
name|Service
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Inject
import|;
end_import

begin_import
import|import
name|javax
operator|.
name|inject
operator|.
name|Named
import|;
end_import

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Properties
import|;
end_import

begin_comment
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|Service
argument_list|(
literal|"archivaRuntimeInfo"
argument_list|)
specifier|public
class|class
name|ArchivaRuntimeInfo
block|{
specifier|private
name|String
name|version
decl_stmt|;
specifier|private
name|String
name|buildNumber
decl_stmt|;
specifier|private
name|long
name|timestamp
decl_stmt|;
specifier|private
name|boolean
name|devMode
decl_stmt|;
annotation|@
name|Inject
specifier|public
name|ArchivaRuntimeInfo
parameter_list|(
annotation|@
name|Named
argument_list|(
name|value
operator|=
literal|"archivaRuntimeProperties"
argument_list|)
name|Properties
name|archivaRuntimeProperties
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
operator|(
name|String
operator|)
name|archivaRuntimeProperties
operator|.
name|get
argument_list|(
literal|"archiva.version"
argument_list|)
expr_stmt|;
name|this
operator|.
name|buildNumber
operator|=
operator|(
name|String
operator|)
name|archivaRuntimeProperties
operator|.
name|get
argument_list|(
literal|"archiva.buildNumber"
argument_list|)
expr_stmt|;
name|this
operator|.
name|timestamp
operator|=
name|NumberUtils
operator|.
name|createLong
argument_list|(
operator|(
name|String
operator|)
name|archivaRuntimeProperties
operator|.
name|get
argument_list|(
literal|"archiva.timestamp"
argument_list|)
argument_list|)
expr_stmt|;
name|this
operator|.
name|devMode
operator|=
name|Boolean
operator|.
name|getBoolean
argument_list|(
literal|"archiva.devMode"
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|version
return|;
block|}
specifier|public
name|void
name|setVersion
parameter_list|(
name|String
name|version
parameter_list|)
block|{
name|this
operator|.
name|version
operator|=
name|version
expr_stmt|;
block|}
specifier|public
name|String
name|getBuildNumber
parameter_list|()
block|{
return|return
name|buildNumber
return|;
block|}
specifier|public
name|void
name|setBuildNumber
parameter_list|(
name|String
name|buildNumber
parameter_list|)
block|{
name|this
operator|.
name|buildNumber
operator|=
name|buildNumber
expr_stmt|;
block|}
specifier|public
name|long
name|getTimestamp
parameter_list|()
block|{
return|return
name|timestamp
return|;
block|}
specifier|public
name|void
name|setTimestamp
parameter_list|(
name|long
name|timestamp
parameter_list|)
block|{
name|this
operator|.
name|timestamp
operator|=
name|timestamp
expr_stmt|;
block|}
specifier|public
name|boolean
name|isDevMode
parameter_list|()
block|{
return|return
name|devMode
return|;
block|}
specifier|public
name|void
name|setDevMode
parameter_list|(
name|boolean
name|devMode
parameter_list|)
block|{
name|this
operator|.
name|devMode
operator|=
name|devMode
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|()
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"ArchivaRuntimeInfo"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{version='"
argument_list|)
operator|.
name|append
argument_list|(
name|version
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", buildNumber='"
argument_list|)
operator|.
name|append
argument_list|(
name|buildNumber
argument_list|)
operator|.
name|append
argument_list|(
literal|'\''
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", timestamp="
argument_list|)
operator|.
name|append
argument_list|(
name|timestamp
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", devMode="
argument_list|)
operator|.
name|append
argument_list|(
name|devMode
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|()
return|;
block|}
block|}
end_class

end_unit

