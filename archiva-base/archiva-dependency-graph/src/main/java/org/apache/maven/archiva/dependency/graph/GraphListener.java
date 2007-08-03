begin_unit|revision:1.0.0;language:Java;cregit-version:0.0.1
begin_package
package|package
name|org
operator|.
name|apache
operator|.
name|maven
operator|.
name|archiva
operator|.
name|dependency
operator|.
name|graph
package|;
end_package

begin_comment
comment|/**  * GraphListener   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  */
end_comment

begin_interface
specifier|public
interface|interface
name|GraphListener
block|{
specifier|public
name|void
name|graphError
parameter_list|(
name|GraphTaskException
name|e
parameter_list|,
name|DependencyGraph
name|currentGraph
parameter_list|)
function_decl|;
specifier|public
name|void
name|graphPhaseEvent
parameter_list|(
name|GraphPhaseEvent
name|event
parameter_list|)
function_decl|;
specifier|public
name|void
name|dependencyResolutionEvent
parameter_list|(
name|DependencyResolutionEvent
name|event
parameter_list|)
function_decl|;
block|}
end_interface

end_unit

