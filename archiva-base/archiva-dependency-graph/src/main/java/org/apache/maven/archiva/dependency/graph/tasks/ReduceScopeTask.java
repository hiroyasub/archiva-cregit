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
operator|.
name|tasks
package|;
end_package

begin_import
import|import
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
operator|.
name|DependencyGraph
import|;
end_import

begin_import
import|import
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
operator|.
name|GraphTask
import|;
end_import

begin_import
import|import
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
operator|.
name|walk
operator|.
name|DependencyGraphWalker
import|;
end_import

begin_import
import|import
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
operator|.
name|walk
operator|.
name|WalkDepthFirstSearch
import|;
end_import

begin_comment
comment|/**  * ReduceScopeTask   *  * @author<a href="mailto:joakime@apache.org">Joakim Erdfelt</a>  * @version $Id$  *   * @plexus.component   *      role="org.apache.maven.archiva.dependency.graph.GraphTask"  *      role-hint="reduce-scope"  *      instantiation-strategy="per-lookup"  */
end_comment

begin_class
specifier|public
class|class
name|ReduceScopeTask
implements|implements
name|GraphTask
block|{
specifier|private
name|String
name|scope
decl_stmt|;
specifier|public
name|ReduceScopeTask
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
specifier|public
name|void
name|executeTask
parameter_list|(
name|DependencyGraph
name|graph
parameter_list|)
block|{
name|DependencyGraphWalker
name|walker
init|=
operator|new
name|WalkDepthFirstSearch
argument_list|()
decl_stmt|;
name|ReduceScopeVisitor
name|reduceScopeResolver
init|=
operator|new
name|ReduceScopeVisitor
argument_list|(
name|this
operator|.
name|scope
argument_list|)
decl_stmt|;
name|walker
operator|.
name|visit
argument_list|(
name|graph
argument_list|,
name|reduceScopeResolver
argument_list|)
expr_stmt|;
block|}
specifier|public
name|String
name|getScope
parameter_list|()
block|{
return|return
name|scope
return|;
block|}
specifier|public
name|void
name|setScope
parameter_list|(
name|String
name|scope
parameter_list|)
block|{
name|this
operator|.
name|scope
operator|=
name|scope
expr_stmt|;
block|}
specifier|public
name|String
name|getTaskId
parameter_list|()
block|{
return|return
literal|"reduce-scope"
return|;
block|}
block|}
end_class

end_unit

