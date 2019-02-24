begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|configuration
package|;
end_package

begin_comment
comment|/*  * Licensed to the Apache Software Foundation (ASF) under one  * or more contributor license agreements.  See the NOTICE file  * distributed with this work for additional information  * regarding copyright ownership.  The ASF licenses this file  * to you under the Apache License, Version 2.0 (the  * "License"); you may not use this file except in compliance  * with the License.  You may obtain a copy of the License at  *  *   http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing,  * software distributed under the License is distributed on an  * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY  * KIND, either express or implied.  See the License for the  * specific language governing permissions and limitations  * under the License.  */
end_comment

begin_comment
comment|/**  * Class RepositoryScanningConfiguration.  *   * @version $Revision$ $Date$  */
end_comment

begin_class
annotation|@
name|SuppressWarnings
argument_list|(
literal|"all"
argument_list|)
specifier|public
class|class
name|RepositoryScanningConfiguration
implements|implements
name|java
operator|.
name|io
operator|.
name|Serializable
block|{
comment|//--------------------------/
comment|//- Class/Member Variables -/
comment|//--------------------------/
comment|/**      * Field fileTypes.      */
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|FileType
argument_list|>
name|fileTypes
decl_stmt|;
comment|/**      * Field knownContentConsumers.      */
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
decl_stmt|;
comment|/**      * Field invalidContentConsumers.      */
specifier|private
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|invalidContentConsumers
decl_stmt|;
comment|//-----------/
comment|//- Methods -/
comment|//-----------/
comment|/**      * Method addFileType.      *       * @param fileType      */
specifier|public
name|void
name|addFileType
parameter_list|(
name|FileType
name|fileType
parameter_list|)
block|{
name|getFileTypes
argument_list|()
operator|.
name|add
argument_list|(
name|fileType
argument_list|)
expr_stmt|;
block|}
comment|//-- void addFileType( FileType )
comment|/**      * Method addInvalidContentConsumer.      *       * @param string      */
specifier|public
name|void
name|addInvalidContentConsumer
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getInvalidContentConsumers
argument_list|()
operator|.
name|add
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|//-- void addInvalidContentConsumer( String )
comment|/**      * Method addKnownContentConsumer.      *       * @param string      */
specifier|public
name|void
name|addKnownContentConsumer
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getKnownContentConsumers
argument_list|()
operator|.
name|add
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|//-- void addKnownContentConsumer( String )
comment|/**      * Method getFileTypes.      *       * @return List      */
specifier|public
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|FileType
argument_list|>
name|getFileTypes
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|fileTypes
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|fileTypes
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|FileType
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|fileTypes
return|;
block|}
comment|//-- java.util.List<FileType> getFileTypes()
comment|/**      * Method getInvalidContentConsumers.      *       * @return List      */
specifier|public
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|getInvalidContentConsumers
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|invalidContentConsumers
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|invalidContentConsumers
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|invalidContentConsumers
return|;
block|}
comment|//-- java.util.List<String> getInvalidContentConsumers()
comment|/**      * Method getKnownContentConsumers.      *       * @return List      */
specifier|public
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|getKnownContentConsumers
parameter_list|()
block|{
if|if
condition|(
name|this
operator|.
name|knownContentConsumers
operator|==
literal|null
condition|)
block|{
name|this
operator|.
name|knownContentConsumers
operator|=
operator|new
name|java
operator|.
name|util
operator|.
name|ArrayList
argument_list|<
name|String
argument_list|>
argument_list|()
expr_stmt|;
block|}
return|return
name|this
operator|.
name|knownContentConsumers
return|;
block|}
comment|//-- java.util.List<String> getKnownContentConsumers()
comment|/**      * Method removeFileType.      *       * @param fileType      */
specifier|public
name|void
name|removeFileType
parameter_list|(
name|FileType
name|fileType
parameter_list|)
block|{
name|getFileTypes
argument_list|()
operator|.
name|remove
argument_list|(
name|fileType
argument_list|)
expr_stmt|;
block|}
comment|//-- void removeFileType( FileType )
comment|/**      * Method removeInvalidContentConsumer.      *       * @param string      */
specifier|public
name|void
name|removeInvalidContentConsumer
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getInvalidContentConsumers
argument_list|()
operator|.
name|remove
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|//-- void removeInvalidContentConsumer( String )
comment|/**      * Method removeKnownContentConsumer.      *       * @param string      */
specifier|public
name|void
name|removeKnownContentConsumer
parameter_list|(
name|String
name|string
parameter_list|)
block|{
name|getKnownContentConsumers
argument_list|()
operator|.
name|remove
argument_list|(
name|string
argument_list|)
expr_stmt|;
block|}
comment|//-- void removeKnownContentConsumer( String )
comment|/**      * Set the FileTypes for the repository scanning configuration.      *       * @param fileTypes      */
specifier|public
name|void
name|setFileTypes
parameter_list|(
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|FileType
argument_list|>
name|fileTypes
parameter_list|)
block|{
name|this
operator|.
name|fileTypes
operator|=
name|fileTypes
expr_stmt|;
block|}
comment|//-- void setFileTypes( java.util.List )
comment|/**      * Set the list of active consumer IDs for invalid content.      *       * @param invalidContentConsumers      */
specifier|public
name|void
name|setInvalidContentConsumers
parameter_list|(
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|invalidContentConsumers
parameter_list|)
block|{
name|this
operator|.
name|invalidContentConsumers
operator|=
name|invalidContentConsumers
expr_stmt|;
block|}
comment|//-- void setInvalidContentConsumers( java.util.List )
comment|/**      * Set the list of active consumers IDs for known content.      *       * @param knownContentConsumers      */
specifier|public
name|void
name|setKnownContentConsumers
parameter_list|(
name|java
operator|.
name|util
operator|.
name|List
argument_list|<
name|String
argument_list|>
name|knownContentConsumers
parameter_list|)
block|{
name|this
operator|.
name|knownContentConsumers
operator|=
name|knownContentConsumers
expr_stmt|;
block|}
comment|//-- void setKnownContentConsumers( java.util.List )
block|}
end_class

end_unit
