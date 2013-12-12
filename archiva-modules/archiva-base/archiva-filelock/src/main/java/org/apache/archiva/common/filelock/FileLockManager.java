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
operator|.
name|filelock
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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_interface
specifier|public
interface|interface
name|FileLockManager
block|{
name|Lock
name|writeFileLock
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileLockException
function_decl|;
name|Lock
name|readFileLock
parameter_list|(
name|File
name|file
parameter_list|)
throws|throws
name|FileLockException
function_decl|;
name|void
name|release
parameter_list|(
name|Lock
name|lock
parameter_list|)
throws|throws
name|FileLockException
function_decl|;
block|}
end_interface

end_unit

