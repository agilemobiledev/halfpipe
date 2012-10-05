#set( $symbol_pound = '#' )
#set( $symbol_dollar = '$' )
#set( $symbol_escape = '\' )
package ${package}.view

import org.springframework.context.annotation.{Bean, ComponentScan, Configuration}
import thirtytwo.degrees.halfpipe.config.AbstractViewConfig
import org.springframework.web.servlet.config.annotation.{ViewControllerRegistry, EnableWebMvc}
import org.springframework.web.servlet.view.freemarker.{FreeMarkerViewResolver, FreeMarkerConfigurer}
import thirtytwo.degrees.halfpipe.mgmt.view.MgmtControllers

@EnableWebMvc
@Configuration
@ComponentScan(basePackageClasses = Array(classOf[ViewConfig], classOf[MgmtControllers]))
class ViewConfig extends AbstractViewConfig {

  /*@Bean
  def jspViewResolver = {
    val view = new UrlBasedViewResolver()
    view.setViewClass(classOf[JstlView])
    view.setPrefix("/WEB-INF/jsp/")
    view.setSuffix(".jsp")
    view
  }*/

  //TODO: https://github.com/sps/mustache-spring-view

  @Bean
  def freemarkerConfig = {
    val cfg = new FreeMarkerConfigurer
    cfg.setTemplateLoaderPath("/WEB-INF/freemarker")
    cfg
  }

  @Bean
  def freemarkerViewResolver = {
    val view = new FreeMarkerViewResolver
    view.setCache(true)
    view.setPrefix("")
    view.setSuffix(".ftl")
    view
  }

  override def addViewControllers(registry: ViewControllerRegistry) {
    registry.addViewController("/").setViewName("index")
  }
}