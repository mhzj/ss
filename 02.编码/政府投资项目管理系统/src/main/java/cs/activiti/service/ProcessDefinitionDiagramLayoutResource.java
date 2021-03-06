package cs.activiti.service;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.databind.node.ObjectNode;

@RestController
public class ProcessDefinitionDiagramLayoutResource extends BaseProcessDefinitionDiagramLayoutResource {

	/**
	 * 页面获取已办流程图
	 * @param processDefinitionId 流程ID
	 * @return 流程图实例
	 */
  @RequestMapping(value="/process-definition/{processDefinitionId}/diagram-layout", method = RequestMethod.GET, produces = "application/json")
  public ObjectNode getDiagram(@PathVariable String processDefinitionId) {
    return getDiagramNode(null, processDefinitionId);
  }
}
