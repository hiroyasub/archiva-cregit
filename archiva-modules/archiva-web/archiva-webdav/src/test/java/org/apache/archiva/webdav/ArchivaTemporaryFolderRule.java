begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_comment
comment|/*  * Copyright 2014 The Apache Software Foundation.  *  * Licensed under the Apache License, Version 2.0 (the "License");  * you may not use this file except in compliance with the License.  * You may obtain a copy of the License at  *  *      http://www.apache.org/licenses/LICENSE-2.0  *  * Unless required by applicable law or agreed to in writing, software  * distributed under the License is distributed on an "AS IS" BASIS,  * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  * See the License for the specific language governing permissions and  * limitations under the License.  */
end_comment

begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|archiva
operator|.
name|webdav
package|;
end_package

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|File
import|;
end_import

begin_import
import|import
name|java
operator|.
name|io
operator|.
name|IOException
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Files
import|;
end_import

begin_import
import|import
name|java
operator|.
name|nio
operator|.
name|file
operator|.
name|Path
import|;
end_import

begin_import
import|import
name|org
operator|.
name|apache
operator|.
name|commons
operator|.
name|io
operator|.
name|FileUtils
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|rules
operator|.
name|TestRule
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runner
operator|.
name|Description
import|;
end_import

begin_import
import|import
name|org
operator|.
name|junit
operator|.
name|runners
operator|.
name|model
operator|.
name|Statement
import|;
end_import

begin_comment
comment|/**  * Rule to help creating folder for repository based on testmethod name  * @author Eric  */
end_comment

begin_class
specifier|public
class|class
name|ArchivaTemporaryFolderRule
implements|implements
name|TestRule
block|{
specifier|private
name|File
name|d
decl_stmt|;
specifier|private
name|Description
name|desc
init|=
name|Description
operator|.
name|EMPTY
decl_stmt|;
specifier|public
name|void
name|before
parameter_list|()
throws|throws
name|IOException
block|{
comment|// hard coded maven target file
name|File
name|f1
init|=
operator|new
name|File
argument_list|(
literal|"target"
operator|+
name|File
operator|.
name|separator
operator|+
literal|"archivarepo"
operator|+
name|File
operator|.
name|separator
operator|+
name|ArchivaTemporaryFolderRule
operator|.
name|resumepackage
argument_list|(
name|desc
operator|.
name|getClassName
argument_list|()
argument_list|)
operator|+
name|File
operator|.
name|separator
operator|+
name|desc
operator|.
name|getMethodName
argument_list|()
argument_list|)
decl_stmt|;
name|f1
operator|.
name|mkdirs
argument_list|()
expr_stmt|;
name|Path
name|p
init|=
name|Files
operator|.
name|createDirectories
argument_list|(
name|f1
operator|.
name|toPath
argument_list|()
argument_list|)
decl_stmt|;
name|d
operator|=
name|p
operator|.
name|toFile
argument_list|()
expr_stmt|;
block|}
specifier|public
name|File
name|getRoot
parameter_list|()
block|{
return|return
name|d
return|;
block|}
specifier|public
name|void
name|after
parameter_list|()
throws|throws
name|IOException
block|{
name|FileUtils
operator|.
name|deleteDirectory
argument_list|(
name|getRoot
argument_list|()
argument_list|)
expr_stmt|;
block|}
annotation|@
name|Override
specifier|public
name|Statement
name|apply
parameter_list|(
name|Statement
name|base
parameter_list|,
name|Description
name|description
parameter_list|)
block|{
name|desc
operator|=
name|description
expr_stmt|;
return|return
name|statement
argument_list|(
name|base
argument_list|)
return|;
block|}
specifier|private
name|Statement
name|statement
parameter_list|(
specifier|final
name|Statement
name|base
parameter_list|)
block|{
return|return
operator|new
name|Statement
argument_list|()
block|{
annotation|@
name|Override
specifier|public
name|void
name|evaluate
parameter_list|()
throws|throws
name|Throwable
block|{
name|before
argument_list|()
expr_stmt|;
try|try
block|{
name|base
operator|.
name|evaluate
argument_list|()
expr_stmt|;
block|}
finally|finally
block|{
name|after
argument_list|()
expr_stmt|;
block|}
block|}
block|}
return|;
block|}
comment|/**      * Return a filepath from FQN class name with only first char of package and classname      * @param packagename      * @return       */
specifier|public
specifier|static
name|String
name|resumepackage
parameter_list|(
name|String
name|packagename
parameter_list|)
block|{
name|StringBuilder
name|sb
init|=
operator|new
name|StringBuilder
argument_list|()
decl_stmt|;
name|String
index|[]
name|p
init|=
name|packagename
operator|.
name|split
argument_list|(
literal|"\\."
argument_list|)
decl_stmt|;
for|for
control|(
name|int
name|i
init|=
literal|0
init|;
name|i
operator|<
name|p
operator|.
name|length
operator|-
literal|2
condition|;
name|i
operator|++
control|)
block|{
name|sb
operator|.
name|append
argument_list|(
name|p
index|[
name|i
index|]
operator|.
name|charAt
argument_list|(
literal|0
argument_list|)
argument_list|)
operator|.
name|append
argument_list|(
name|File
operator|.
name|separator
argument_list|)
expr_stmt|;
block|}
name|sb
operator|.
name|append
argument_list|(
name|p
index|[
name|p
operator|.
name|length
operator|-
literal|1
index|]
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

