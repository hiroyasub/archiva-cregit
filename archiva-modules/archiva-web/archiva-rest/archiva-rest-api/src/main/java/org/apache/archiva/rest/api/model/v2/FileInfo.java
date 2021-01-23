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
operator|.
name|v2
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  * http://www.apache.org/licenses/LICENSE-2.0  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_import
import|import
name|io
operator|.
name|swagger
operator|.
name|v3
operator|.
name|oas
operator|.
name|annotations
operator|.
name|media
operator|.
name|Schema
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

begin_import
import|import
name|java
operator|.
name|time
operator|.
name|OffsetDateTime
import|;
end_import

begin_comment
comment|/**  * @author Martin Stockhammer<martin_s@apache.org>  */
end_comment

begin_class
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"FileInfo"
argument_list|,
name|description
operator|=
literal|"Information about a file stored in the repository"
argument_list|)
specifier|public
class|class
name|FileInfo
implements|implements
name|Serializable
block|{
specifier|private
specifier|static
specifier|final
name|long
name|serialVersionUID
init|=
literal|900497784542880195L
decl_stmt|;
specifier|private
name|OffsetDateTime
name|modified
decl_stmt|;
specifier|private
name|String
name|fileName
decl_stmt|;
specifier|private
name|String
name|path
decl_stmt|;
annotation|@
name|Schema
argument_list|(
name|description
operator|=
literal|"Time when the file was last modified"
argument_list|)
specifier|public
name|OffsetDateTime
name|getModified
parameter_list|( )
block|{
return|return
name|modified
return|;
block|}
specifier|public
name|void
name|setModified
parameter_list|(
name|OffsetDateTime
name|modified
parameter_list|)
block|{
name|this
operator|.
name|modified
operator|=
name|modified
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"file_name"
argument_list|,
name|description
operator|=
literal|"Name of the file"
argument_list|)
specifier|public
name|String
name|getFileName
parameter_list|( )
block|{
return|return
name|fileName
return|;
block|}
specifier|public
name|void
name|setFileName
parameter_list|(
name|String
name|fileName
parameter_list|)
block|{
name|this
operator|.
name|fileName
operator|=
name|fileName
expr_stmt|;
block|}
annotation|@
name|Schema
argument_list|(
name|name
operator|=
literal|"path"
argument_list|,
name|description
operator|=
literal|"Path to the file relative to the repository directory"
argument_list|)
specifier|public
name|String
name|getPath
parameter_list|( )
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
return|return
literal|true
return|;
if|if
condition|(
name|o
operator|==
literal|null
operator|||
name|getClass
argument_list|( )
operator|!=
name|o
operator|.
name|getClass
argument_list|( )
condition|)
return|return
literal|false
return|;
name|FileInfo
name|fileInfo
init|=
operator|(
name|FileInfo
operator|)
name|o
decl_stmt|;
if|if
condition|(
name|modified
operator|!=
literal|null
condition|?
operator|!
name|modified
operator|.
name|equals
argument_list|(
name|fileInfo
operator|.
name|modified
argument_list|)
else|:
name|fileInfo
operator|.
name|modified
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
if|if
condition|(
name|fileName
operator|!=
literal|null
condition|?
operator|!
name|fileName
operator|.
name|equals
argument_list|(
name|fileInfo
operator|.
name|fileName
argument_list|)
else|:
name|fileInfo
operator|.
name|fileName
operator|!=
literal|null
condition|)
return|return
literal|false
return|;
return|return
name|path
operator|!=
literal|null
condition|?
name|path
operator|.
name|equals
argument_list|(
name|fileInfo
operator|.
name|path
argument_list|)
else|:
name|fileInfo
operator|.
name|path
operator|==
literal|null
return|;
block|}
annotation|@
name|Override
specifier|public
name|int
name|hashCode
parameter_list|( )
block|{
name|int
name|result
init|=
name|modified
operator|!=
literal|null
condition|?
name|modified
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
decl_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|fileName
operator|!=
literal|null
condition|?
name|fileName
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
name|result
operator|=
literal|31
operator|*
name|result
operator|+
operator|(
name|path
operator|!=
literal|null
condition|?
name|path
operator|.
name|hashCode
argument_list|( )
else|:
literal|0
operator|)
expr_stmt|;
return|return
name|result
return|;
block|}
annotation|@
name|Override
specifier|public
name|String
name|toString
parameter_list|( )
block|{
specifier|final
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|(
literal|"FileInfo{"
argument_list|)
decl_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"modified="
argument_list|)
operator|.
name|append
argument_list|(
name|modified
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", fileName='"
argument_list|)
operator|.
name|append
argument_list|(
name|fileName
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
literal|", path='"
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
literal|'}'
argument_list|)
expr_stmt|;
return|return
name|sb
operator|.
name|toString
argument_list|( )
return|;
block|}
block|}
end_class

end_unit

