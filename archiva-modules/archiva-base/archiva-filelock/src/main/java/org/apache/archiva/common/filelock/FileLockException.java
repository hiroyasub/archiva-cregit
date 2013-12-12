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

begin_comment
comment|/**  * @author Olivier Lamy  */
end_comment

begin_class
specifier|public
class|class
name|FileLockException
extends|extends
name|Exception
block|{
specifier|public
name|FileLockException
parameter_list|(
name|String
name|s
parameter_list|,
name|Throwable
name|throwable
parameter_list|)
block|{
name|super
argument_list|(
name|s
argument_list|,
name|throwable
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

