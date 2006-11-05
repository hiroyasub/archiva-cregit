begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|plugin
operator|.
name|assembly
operator|.
name|test
package|;
end_package

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|Test
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestCase
import|;
end_import

begin_import
import|import
name|junit
operator|.
name|framework
operator|.
name|TestSuite
import|;
end_import

begin_comment
comment|/**  * Unit test for simple App.  */
end_comment

begin_class
specifier|public
class|class
name|AppTest
extends|extends
name|TestCase
block|{
comment|/**      * Create the test case      *      * @param testName name of the test case      */
specifier|public
name|AppTest
parameter_list|(
name|String
name|testName
parameter_list|)
block|{
name|super
argument_list|(
name|testName
argument_list|)
expr_stmt|;
block|}
comment|/**      * @return the suite of tests being tested      */
specifier|public
specifier|static
name|Test
name|suite
parameter_list|()
block|{
return|return
operator|new
name|TestSuite
argument_list|(
name|AppTest
operator|.
name|class
argument_list|)
return|;
block|}
comment|/**      * Rigourous Test :-)      */
specifier|public
name|void
name|testApp
parameter_list|()
block|{
name|assertTrue
argument_list|(
literal|true
argument_list|)
expr_stmt|;
block|}
block|}
end_class

end_unit

