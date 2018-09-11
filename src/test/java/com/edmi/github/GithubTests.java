package com.edmi.github;

import com.edmi.dao.Edmi_userRepository;
import com.edmi.dao.etherscan.ICO_Etherscan_IO_BlocksRepository;
import com.edmi.dao.etherscan.ICO_Etherscan_IO_Blocks_TxsRepository;
import com.edmi.dao.etherscan.ICO_Etherscan_IO_Blocks_Txs_Page_ListRepository;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_ExchangeRepository;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_Exchange_CurrenciesRepository;
import com.edmi.dao.feixiaohao.ICO_Feixiaohao_Exchange_DetailsRepository;
import com.edmi.dao.github.ICO_Github_OrganizationRepository;
import com.edmi.dao.github.ICO_Github_RepositoriesRepository;
import com.edmi.dao.linkedin.*;
import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks;
import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Txs;
import com.edmi.entity.etherscan.ICO_Etherscan_IO_Blocks_Txs_Page_List;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange;
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Currencies;
import com.edmi.entity.github.ICO_Github_Organization;
import com.edmi.entity.github.ICO_Github_Repositories;
import com.edmi.entity.linkedin.ICO_Linkedin_Link;
import com.edmi.entity.linkedin.ICO_Linkedin_Member;
import com.edmi.entity.linkedin.ICO_Linkedin_Memberselectionskills;
import com.edmi.service.service.EtherscanService;
import com.edmi.service.service.FeixiaohaoService;
import com.edmi.service.service.GithubService;
import com.edmi.service.service.LinkedinService;
import com.edmi.service.serviceImp.WebDriverService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class GithubTests {

	Logger log = Logger.getLogger(GithubTests.class);

	@Autowired
	private GithubService githubService;
	@Autowired
	private ICO_Github_OrganizationRepository orgDao;
	@Autowired
	private ICO_Github_RepositoriesRepository igrDao;



	@Test
	public void importICO_Github_Links() {
		String fileURL = "E:\\ICO\\github\\github_list.xlsx";
		githubService.importICO_Github_Links(fileURL);
	}

	@Test
	public void getICO_Github_Organization() throws Exception {
		githubService.getICO_Github_Organization();
	}
	@Test
	public void getICO_Github_Repositories() throws Exception {
		List<ICO_Github_Organization> orgs = orgDao.getICO_Github_OrganizationByStatus("ini");
		for(ICO_Github_Organization org:orgs){
			githubService.getICO_Github_Repositories(org);
			Thread.sleep(2*1000);
		}
	}
	@Test
	public void getICO_Github_Repositories_Details() throws Exception {
		List<ICO_Github_Repositories> ogrs = igrDao.getICO_Github_RepositoriesByStatus("ini");
		for(ICO_Github_Repositories ogr:ogrs){
			githubService.getICO_Github_Repositories_Details(ogr);
		}
	}


}
