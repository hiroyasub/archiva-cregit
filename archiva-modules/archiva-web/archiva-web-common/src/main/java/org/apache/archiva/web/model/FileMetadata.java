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
comment|/**  * @author Olivier Lamy  * @since 1.4-M3  */
end_comment

begin_class
annotation|@
name|XmlRootElement
argument_list|(
name|name
operator|=
literal|"fileMetadata"
argument_list|)
specifier|public
class|class
name|FileMetadata
implements|implements
name|Serializable
block|{
specifier|private
name|String
name|name
decl_stmt|;
specifier|private
name|String
name|serverFileName
decl_stmt|;
specifier|private
name|long
name|size
decl_stmt|;
specifier|private
name|String
name|url
decl_stmt|;
specifier|private
name|String
name|deleteUrl
decl_stmt|;
specifier|private
name|String
name|deleteType
decl_stmt|;
specifier|private
name|String
name|errorKey
decl_stmt|;
specifier|private
name|String
name|classifier
decl_stmt|;
specifier|private
name|String
name|packaging
decl_stmt|;
specifier|private
name|boolean
name|pomFile
decl_stmt|;
specifier|public
name|FileMetadata
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|FileMetadata
parameter_list|(
name|String
name|serverFileName
parameter_list|)
block|{
name|this
operator|.
name|serverFileName
operator|=
name|serverFileName
expr_stmt|;
block|}
specifier|public
name|FileMetadata
parameter_list|(
name|String
name|name
parameter_list|,
name|long
name|size
parameter_list|,
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|deleteUrl
operator|=
name|url
expr_stmt|;
name|this
operator|.
name|deleteType
operator|=
literal|"DELETE"
expr_stmt|;
block|}
specifier|public
name|String
name|getName
parameter_list|()
block|{
return|return
name|name
return|;
block|}
specifier|public
name|void
name|setName
parameter_list|(
name|String
name|name
parameter_list|)
block|{
name|this
operator|.
name|name
operator|=
name|name
expr_stmt|;
block|}
specifier|public
name|long
name|getSize
parameter_list|()
block|{
return|return
name|size
return|;
block|}
specifier|public
name|void
name|setSize
parameter_list|(
name|long
name|size
parameter_list|)
block|{
name|this
operator|.
name|size
operator|=
name|size
expr_stmt|;
block|}
specifier|public
name|String
name|getUrl
parameter_list|()
block|{
return|return
name|url
return|;
block|}
specifier|public
name|void
name|setUrl
parameter_list|(
name|String
name|url
parameter_list|)
block|{
name|this
operator|.
name|url
operator|=
name|url
expr_stmt|;
block|}
specifier|public
name|String
name|getDeleteUrl
parameter_list|()
block|{
return|return
name|deleteUrl
return|;
block|}
specifier|public
name|void
name|setDeleteUrl
parameter_list|(
name|String
name|deleteUrl
parameter_list|)
block|{
name|this
operator|.
name|deleteUrl
operator|=
name|deleteUrl
expr_stmt|;
block|}
specifier|public
name|String
name|getDeleteType
parameter_list|()
block|{
return|return
name|deleteType
return|;
block|}
specifier|public
name|void
name|setDeleteType
parameter_list|(
name|String
name|deleteType
parameter_list|)
block|{
name|this
operator|.
name|deleteType
operator|=
name|deleteType
expr_stmt|;
block|}
specifier|public
name|String
name|getErrorKey
parameter_list|()
block|{
return|return
name|errorKey
return|;
block|}
specifier|public
name|void
name|setErrorKey
parameter_list|(
name|String
name|errorKey
parameter_list|)
block|{
name|this
operator|.
name|errorKey
operator|=
name|errorKey
expr_stmt|;
block|}
specifier|public
name|String
name|getClassifier
parameter_list|()
block|{
return|return
name|classifier
return|;
block|}
specifier|public
name|void
name|setClassifier
parameter_list|(
name|String
name|classifier
parameter_list|)
block|{
name|this
operator|.
name|classifier
operator|=
name|classifier
expr_stmt|;
block|}
specifier|public
name|boolean
name|isPomFile
parameter_list|()
block|{
return|return
name|pomFile
return|;
block|}
specifier|public
name|void
name|setPomFile
parameter_list|(
name|boolean
name|pomFile
parameter_list|)
block|{
name|this
operator|.
name|pomFile
operator|=
name|pomFile
expr_stmt|;
block|}
specifier|public
name|String
name|getServerFileName
parameter_list|()
block|{
return|return
name|serverFileName
return|;
block|}
specifier|public
name|void
name|setServerFileName
parameter_list|(
name|String
name|serverFileName
parameter_list|)
block|{
name|this
operator|.
name|serverFileName
operator|=
name|serverFileName
expr_stmt|;
block|}
specifier|public
name|String
name|getPackaging
parameter_list|()
block|{
return|return
name|packaging
return|;
block|}
specifier|public
name|void
name|setPackaging
parameter_list|(
name|String
name|packaging
parameter_list|)
block|{
name|this
operator|.
name|packaging
operator|=
name|packaging
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|boolean
name|equals
parameter_list|(
name|Object
name|o
parameter_list|)
block|{
if|if
condition|(
name|this
operator|==
name|o
condition|)
block|{
return|return
literal|true
return|;
block|}
if|if
condition|(
operator|!
operator|(
name|o
operator|instanceof
name|FileMetadata
operator|)
condition|)
block|{
return|return
literal|false
return|;
block|}
name|FileMetadata
name|that
init|=
operator|(
name|FileMetadata
operator|)
name|o
decl_stmt|;
if|if
condition|(
operator|!
name|serverFileName
operator|.
name|equals
argument_list|(
name|that
operator|.
name|serverFileName
argument_list|)
condition|)
block|{
return|return
literal|false
return|;
block|}
return|return
literal|true
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|()
block|{
return|return
name|serverFileName
operator|.
name|hashCode
argument_list|()
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
argument_list|(
literal|"FileMetadata{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"name='"
argument_list|)
operator|.
name|append
argument_list|(
name|name
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
literal|", serverFileName='"
argument_list|)
operator|.
name|append
argument_list|(
name|serverFileName
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
literal|", size="
argument_list|)
operator|.
name|append
argument_list|(
name|size
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", url='"
argument_list|)
operator|.
name|append
argument_list|(
name|url
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
literal|", deleteUrl='"
argument_list|)
operator|.
name|append
argument_list|(
name|deleteUrl
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
literal|", deleteType='"
argument_list|)
operator|.
name|append
argument_list|(
name|deleteType
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
literal|", errorKey='"
argument_list|)
operator|.
name|append
argument_list|(
name|errorKey
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
literal|", classifier='"
argument_list|)
operator|.
name|append
argument_list|(
name|classifier
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
literal|", packaging='"
argument_list|)
operator|.
name|append
argument_list|(
name|packaging
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
literal|", pomFile="
argument_list|)
operator|.
name|append
argument_list|(
name|pomFile
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

