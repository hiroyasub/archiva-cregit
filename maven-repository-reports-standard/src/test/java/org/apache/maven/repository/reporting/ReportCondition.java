begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|repository
operator|.
name|reporting
package|;
end_package

begin_comment
comment|/*  * Copyright 2001-2005 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
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

begin_comment
comment|/**  * @author<a href="mailto:jtolentino@mergere.com">John Tolentino</a>  */
end_comment

begin_class
specifier|public
class|class
name|ReportCondition
block|{
specifier|public
specifier|final
specifier|static
name|int
name|SUCCESS
init|=
literal|0
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|int
name|FAILURE
init|=
operator|-
literal|1
decl_stmt|;
specifier|public
specifier|final
specifier|static
name|int
name|WARNING
init|=
literal|1
decl_stmt|;
specifier|private
name|int
name|result
decl_stmt|;
specifier|private
name|Artifact
name|artifact
decl_stmt|;
specifier|private
name|String
name|reason
decl_stmt|;
specifier|public
name|ReportCondition
parameter_list|(
name|int
name|result
parameter_list|,
name|Artifact
name|artifact
parameter_list|,
name|String
name|reason
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
name|this
operator|.
name|artifact
operator|=
name|artifact
expr_stmt|;
name|this
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
block|}
specifier|public
name|int
name|getResult
parameter_list|()
block|{
return|return
name|result
return|;
block|}
specifier|public
name|void
name|setResult
parameter_list|(
name|int
name|result
parameter_list|)
block|{
name|this
operator|.
name|result
operator|=
name|result
expr_stmt|;
block|}
specifier|public
name|Artifact
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
name|Artifact
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
name|String
name|getReason
parameter_list|()
block|{
return|return
name|reason
return|;
block|}
specifier|public
name|void
name|setReason
parameter_list|(
name|String
name|reason
parameter_list|)
block|{
name|this
operator|.
name|reason
operator|=
name|reason
expr_stmt|;
block|}
block|}
end_class

end_unit

