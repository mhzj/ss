package cs.controller;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * 建议反馈控制层
 * @author 常祥
 * @serialData 2017/4/10
 *
 */
@Controller
@RequestMapping(name = "Suggestfeedback", path = "suggestfeedback")
public class SuggestfeedbackController {
	private String ctrlName = "suggestfeedback";
		
		/**
		 * 进入建议反馈页面
		 * @author 常祥
		 * @serialData 2017/4/10
		 * @return 
		 */
		@RequiresPermissions("suggestfeedback#html/list#get")
		@RequestMapping(name = "SuggestfeedbackList", path = "html/list", method = RequestMethod.GET)
		public String list() {
			return ctrlName + "/list";
		}

}
