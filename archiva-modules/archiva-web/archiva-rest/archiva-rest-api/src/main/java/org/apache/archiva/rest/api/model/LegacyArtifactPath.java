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
name|api
operator|.
name|model
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|javax
operator|.
name|xml
operator|.
name|bind
operator|.
name|annotation
operator|.
name|XmlRootElement
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
comment|/**  * @author Olivier Lamy  * @since 1.4  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"legacyArtifactPath"
argument_list|)
specifier|public
class|class
name|LegacyArtifactPath
implements|implements
name|Serializable
block|{
comment|/**      * The legacy path.      */
specifier|private
name|String
name|path
decl_stmt|;
comment|/**      * The artifact reference, as " [groupId] :      * [artifactId] : [version] : [classifier] : [type] ".      */
specifier|private
name|String
name|artifact
decl_stmt|;
specifier|public
name|LegacyArtifactPath
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|LegacyArtifactPath
parameter_list|(
name|String
name|path
parameter_list|,
name|String
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
block|}
specifier|public
name|String
name|getPath
parameter_list|()
block|{
return|return
name|path
return|;
block|}
specifier|public
name|void
name|setPath
parameter_list|(
name|String
name|path
parameter_list|)
block|{
name|this
operator|.
name|path
operator|=
name|path
expr_stmt|;
block|}
specifier|public
name|String
name|getArtifact
parameter_list|()
block|{
return|return
name|artifact
return|;
block|}
specifier|public
name|void
name|setArtifact
parameter_list|(
name|String
name|artifact
parameter_list|)
block|{
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
block|}
specifier|public
name|boolean
name|match
parameter_list|(
name|String
name|path
parameter_list|)
block|{
return|return
name|path
operator|.
name|equals
argument_list|(
name|this
operator|.
name|path
argument_list|)
return|;
block|}
specifier|public
name|String
name|getGroupId
parameter_list|()
block|{
return|return
name|artifact
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|0
index|]
return|;
block|}
specifier|public
name|String
name|getArtifactId
parameter_list|()
block|{
return|return
name|artifact
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|1
index|]
return|;
block|}
specifier|public
name|String
name|getVersion
parameter_list|()
block|{
return|return
name|artifact
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|2
index|]
return|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
name|String
name|classifier
init|=
name|artifact
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|3
index|]
decl_stmt|;
return|return
name|classifier
operator|.
name|length
argument_list|()
operator|>
literal|0
condition|?
name|classifier
else|:
literal|null
return|;
block|}
specifier|public
name|String
name|getType
parameter_list|()
block|{
return|return
name|artifact
operator|.
name|split
argument_list|(
literal|":"
argument_list|)
index|[
literal|4
index|]
return|;
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
literal|"LegacyArtifactPath"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{path='"
argument_list|)
operator|.
name|append
argument_list|(
name|path
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
literal|", artifact='"
argument_list|)
operator|.
name|append
argument_list|(
name|artifact
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

