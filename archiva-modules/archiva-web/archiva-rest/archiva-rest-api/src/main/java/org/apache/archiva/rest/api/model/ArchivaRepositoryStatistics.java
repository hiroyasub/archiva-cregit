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

begin_import
import|import
name|java
operator|.
name|util
operator|.
name|Date
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
literal|"archivaRepositoryStatistics"
argument_list|)
specifier|public
class|class
name|ArchivaRepositoryStatistics
implements|implements
name|Serializable
block|{
specifier|private
name|Date
name|scanEndTime
decl_stmt|;
specifier|private
name|Date
name|scanStartTime
decl_stmt|;
specifier|private
name|long
name|totalArtifactCount
decl_stmt|;
specifier|private
name|long
name|totalArtifactFileSize
decl_stmt|;
specifier|private
name|long
name|totalFileCount
decl_stmt|;
specifier|private
name|long
name|totalGroupCount
decl_stmt|;
specifier|private
name|long
name|totalProjectCount
decl_stmt|;
specifier|private
name|long
name|newFileCount
decl_stmt|;
specifier|private
name|long
name|duration
decl_stmt|;
specifier|private
name|String
name|lastScanDate
decl_stmt|;
specifier|public
name|ArchivaRepositoryStatistics
parameter_list|()
block|{
comment|// no op
block|}
specifier|public
name|Date
name|getScanEndTime
parameter_list|()
block|{
return|return
name|scanEndTime
return|;
block|}
specifier|public
name|void
name|setScanEndTime
parameter_list|(
name|Date
name|scanEndTime
parameter_list|)
block|{
name|this
operator|.
name|scanEndTime
operator|=
name|scanEndTime
expr_stmt|;
block|}
specifier|public
name|Date
name|getScanStartTime
parameter_list|()
block|{
return|return
name|scanStartTime
return|;
block|}
specifier|public
name|void
name|setScanStartTime
parameter_list|(
name|Date
name|scanStartTime
parameter_list|)
block|{
name|this
operator|.
name|scanStartTime
operator|=
name|scanStartTime
expr_stmt|;
block|}
specifier|public
name|long
name|getTotalArtifactCount
parameter_list|()
block|{
return|return
name|totalArtifactCount
return|;
block|}
specifier|public
name|void
name|setTotalArtifactCount
parameter_list|(
name|long
name|totalArtifactCount
parameter_list|)
block|{
name|this
operator|.
name|totalArtifactCount
operator|=
name|totalArtifactCount
expr_stmt|;
block|}
specifier|public
name|long
name|getTotalArtifactFileSize
parameter_list|()
block|{
return|return
name|totalArtifactFileSize
return|;
block|}
specifier|public
name|void
name|setTotalArtifactFileSize
parameter_list|(
name|long
name|totalArtifactFileSize
parameter_list|)
block|{
name|this
operator|.
name|totalArtifactFileSize
operator|=
name|totalArtifactFileSize
expr_stmt|;
block|}
specifier|public
name|long
name|getTotalFileCount
parameter_list|()
block|{
return|return
name|totalFileCount
return|;
block|}
specifier|public
name|void
name|setTotalFileCount
parameter_list|(
name|long
name|totalFileCount
parameter_list|)
block|{
name|this
operator|.
name|totalFileCount
operator|=
name|totalFileCount
expr_stmt|;
block|}
specifier|public
name|long
name|getTotalGroupCount
parameter_list|()
block|{
return|return
name|totalGroupCount
return|;
block|}
specifier|public
name|void
name|setTotalGroupCount
parameter_list|(
name|long
name|totalGroupCount
parameter_list|)
block|{
name|this
operator|.
name|totalGroupCount
operator|=
name|totalGroupCount
expr_stmt|;
block|}
specifier|public
name|long
name|getTotalProjectCount
parameter_list|()
block|{
return|return
name|totalProjectCount
return|;
block|}
specifier|public
name|void
name|setTotalProjectCount
parameter_list|(
name|long
name|totalProjectCount
parameter_list|)
block|{
name|this
operator|.
name|totalProjectCount
operator|=
name|totalProjectCount
expr_stmt|;
block|}
specifier|public
name|long
name|getNewFileCount
parameter_list|()
block|{
return|return
name|newFileCount
return|;
block|}
specifier|public
name|void
name|setNewFileCount
parameter_list|(
name|long
name|newFileCount
parameter_list|)
block|{
name|this
operator|.
name|newFileCount
operator|=
name|newFileCount
expr_stmt|;
block|}
specifier|public
name|void
name|setDuration
parameter_list|(
name|long
name|duration
parameter_list|)
block|{
name|this
operator|.
name|duration
operator|=
name|duration
expr_stmt|;
block|}
specifier|public
name|long
name|getDuration
parameter_list|()
block|{
return|return
name|duration
return|;
block|}
specifier|public
name|String
name|getLastScanDate
parameter_list|()
block|{
return|return
name|lastScanDate
return|;
block|}
specifier|public
name|void
name|setLastScanDate
parameter_list|(
name|String
name|lastScanDate
parameter_list|)
block|{
name|this
operator|.
name|lastScanDate
operator|=
name|lastScanDate
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
literal|"ArchivaRepositoryStatistics"
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|"{scanEndTime="
argument_list|)
operator|.
name|append
argument_list|(
name|scanEndTime
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", scanStartTime="
argument_list|)
operator|.
name|append
argument_list|(
name|scanStartTime
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalArtifactCount="
argument_list|)
operator|.
name|append
argument_list|(
name|totalArtifactCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalArtifactFileSize="
argument_list|)
operator|.
name|append
argument_list|(
name|totalArtifactFileSize
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalFileCount="
argument_list|)
operator|.
name|append
argument_list|(
name|totalFileCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalGroupCount="
argument_list|)
operator|.
name|append
argument_list|(
name|totalGroupCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", totalProjectCount="
argument_list|)
operator|.
name|append
argument_list|(
name|totalProjectCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", newFileCount="
argument_list|)
operator|.
name|append
argument_list|(
name|newFileCount
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", duration="
argument_list|)
operator|.
name|append
argument_list|(
name|duration
argument_list|)
expr_stmt|;
name|sb
operator|.
name|append
argument_list|(
literal|", lastScanDate='"
argument_list|)
operator|.
name|append
argument_list|(
name|lastScanDate
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

