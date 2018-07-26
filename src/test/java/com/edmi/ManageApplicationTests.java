package com.edmi;

import com.edmi.configs.StateCodeConfig;
import com.edmi.configs.TaskExecutePoolTest;
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
import com.edmi.entity.feixiaohao.ICO_Feixiaohao_Exchange_Details;
import com.edmi.entity.github.ICO_Github_Organization;
import com.edmi.entity.github.ICO_Github_Repositories;
import com.edmi.entity.linkedin.ICO_Linkedin_Link;
import com.edmi.entity.linkedin.ICO_Linkedin_Member;
import com.edmi.entity.linkedin.ICO_Linkedin_Memberselectionskills;
import com.edmi.service.service.*;
import com.edmi.service.serviceImp.WebDriverService;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.log4j.Logger;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.sql.Timestamp;
import java.util.*;

@RunWith(SpringRunner.class)
@SpringBootTest
public class ManageApplicationTests {

	Logger log = Logger.getLogger(ManageApplicationTests.class);

	@Autowired
	private Edmi_userRepository userDao;

	@Autowired
	private LinkedinService linkService;

	@Autowired
	private GithubService githubService;

	@Autowired
	private FeixiaohaoService feixiaohaoService;

    @Autowired
    private EtherscanService etherscanService;


	@Autowired
	private WebDriverService webDriverService;
	@Autowired
	private Linkedin_linkRepository linkDao;
	@Autowired
	private Linkedin_memberRepository memberDao;
	@Autowired
	private Linkedin_membereducationexperienceRepository educationDao;
	@Autowired
	private Linkedin_memberselectionskillsRepository skillsDao;
	@Autowired
	private Linkedin_memberworkexperienceRepository workDao;
	@Autowired
	private ICO_Github_OrganizationRepository orgDao;
	@Autowired
	private ICO_Github_RepositoriesRepository igrDao;
	@Autowired
	private ICO_Feixiaohao_ExchangeRepository exchangeDao;
	@Autowired
	private ICO_Feixiaohao_Exchange_DetailsRepository exchange_detailsDao;

	@Autowired
	private ICO_Feixiaohao_Exchange_CurrenciesRepository currenciesDao;
	@Autowired
	private ICO_Etherscan_IO_BlocksRepository blocksDao;

	@Autowired
	private ICO_Etherscan_IO_Blocks_TxsRepository txsDao;


	@Autowired
	private ICO_Etherscan_IO_Blocks_Txs_Page_ListRepository txs_page_listDao;

	@Test
	public void importLinkedInLinks() {
		linkService.importLinkedInLinks();
	}
	@Test
	public void importICO_Github_Links() {
		String fileURL = "E:\\ICO\\github-list.xlsx";
		githubService.importICO_Github_Links(fileURL);
	}
	/*@Test
	public void analysisMembersToBase(){

		try {
			String content = FileUtils.readFileToString(new File("C:\\Users\\EDDC\\Desktop\\datas\\files\\10312.html"),"utf-8");
			Document doc = Jsoup.parse(content);
			doc.charset(Charset.forName("utf-8"));
			linkService.analysisMembersToBase(doc,000);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}*/
	@Test
    public void testSave(){
		ICO_Linkedin_Member member = new ICO_Linkedin_Member();
		member.setName("---");
		member.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
		member.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));

		/*member = memberDao.save(member);*/

		ICO_Linkedin_Memberselectionskills memberselectionskill  = new ICO_Linkedin_Memberselectionskills();
		memberselectionskill.setSkill("====");
		memberselectionskill.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
		memberselectionskill.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
		memberselectionskill.setMember(member);




		List<ICO_Linkedin_Memberselectionskills> memberselectionskills  = new ArrayList<ICO_Linkedin_Memberselectionskills>();
		memberselectionskills.add(memberselectionskill);
		/*skillsDao.saveAll(memberselectionskills);*/

	}
	@Test
    public void readLinkedinFilesToBase(){
		linkService.readLinkedinFilesToBase();
	}

	@Test
	public void doResponse() throws Exception {
		Request request = new Request("https://api.github.com/orgs/alibab", RequestMethod.GET);
		Response response = HttpClientUtil.doRequest(request);
		System.out.println(response.getResponseText()); //response text
		System.out.println(response.getCode()); //response code
		System.out.println(response.getHeader("Set-Cookie"));
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
	@Test
	public void getICO_Feixiaohao_Exchange() throws Exception {
		feixiaohaoService.getICO_Feixiaohao_Exchange();
	}
	@Test
	public void getICO_Feixiaohao_Exchange_Details() throws Exception {
		List<ICO_Feixiaohao_Exchange> exchanges = exchangeDao.getICO_Feixiaohao_ExchangeByStatus("ini");
		for(ICO_Feixiaohao_Exchange exchange:exchanges){
			feixiaohaoService.getICO_Feixiaohao_Exchange_Details(exchange);
		}
	}
	@Test
    public void getICO_Feixiaohao_Exchange_Counter_Party_Details() throws MethodNotSupportException {
		List<String> links = exchange_detailsDao.getICO_Feixiaohao_Exchange();
		for(String link:links){
			feixiaohaoService.getICO_Feixiaohao_Exchange_Counter_Party_Details(link);
		}
	}

	@Test
	public void importICO_Feixiaohao_Exchange_Currencies(){
		feixiaohaoService.importICO_Feixiaohao_Exchange_Currencies();
	}
	@Test
	public void getICO_Feixiaohao_Exchange_Currenciesdtl() throws MethodNotSupportException {
		List<ICO_Feixiaohao_Exchange_Currencies> currencies = currenciesDao.getICO_Feixiaohao_Exchange_CurrenciesByDetails_status("ini");
		int i = 0;
		for(ICO_Feixiaohao_Exchange_Currencies currency:currencies){
			feixiaohaoService.getICO_Feixiaohao_Exchange_Currenciesdtl(currency);
			i++;
			try {
			    if(i%10==0){
                    Thread.sleep(5*1000);
                }
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
    @Test
    public void getICO_Etherscan_IO_Blocks() throws Exception {
        List<String> links = etherscanService.getICO_Etherscan_IO_Blocks_TotalPageLinks();
        Long serial = Calendar.getInstance().getTime().getTime();
        if(links.size()>0){
            for(String link:links){
                etherscanService.getICO_Etherscan_IO_Blocks(link,serial,links.size());
                Thread.sleep(2*1000);
            }
        }
    }
	@Test
	public void getICO_Etherscan_IO_Blocks_Info() throws Exception {

		List<ICO_Etherscan_IO_Blocks> blocks = blocksDao.findTop50ByStatusAndServer("ini",1);

		for(ICO_Etherscan_IO_Blocks block:blocks){
			etherscanService.getICO_Etherscan_IO_Blocks_Info(block);
		}

	}
	@Test
	public void getICO_Etherscan_IO_Blocks_Txs_Page_List() throws Exception {
		List<ICO_Etherscan_IO_Blocks> blocks = blocksDao.findTop50ByPagestatusAndServer("ini",1);
		for(ICO_Etherscan_IO_Blocks block:blocks){
			etherscanService.getICO_Etherscan_IO_Blocks_TxsPages(block);
		}
		Thread.sleep(60*1000);
	}
	@Test
	public void getICO_Etherscan_IO_Blocks_Txs() throws Exception {

		List<ICO_Etherscan_IO_Blocks_Txs_Page_List> page_list = txs_page_listDao.findTop30ByStatus("ini");

        for(ICO_Etherscan_IO_Blocks_Txs_Page_List page:page_list){
        	etherscanService.getICO_Etherscan_IO_Blocks_Txs(page);
        }
	}
	@Test
	public void getICO_Etherscan_IO_Blocks_Txs_Info() throws Exception {
		List<ICO_Etherscan_IO_Blocks_Txs> txs = txsDao.findTop30ByStatus("ini");
		for(ICO_Etherscan_IO_Blocks_Txs tx:txs){
			etherscanService.getICO_Etherscan_IO_Blocks_Txs_Info(tx);
		}
	}



    @Test
	public void getStandard_link(){
		List<ICO_Linkedin_Link> links = linkDao.getICO_Linkedin_Link();
		for(ICO_Linkedin_Link link:links){
			String link_text = link.getLink();
			int start = StringUtils.indexOfIgnoreCase(link_text,"linkedin.com/in/")+15;//加15代表本字符串最后一个/的位置
			int end = StringUtils.indexOfIgnoreCase(link_text,"/",start+1);//查找start后面的/的位置
			if(end==-1){
				link_text = "https://www.linkedin.com/in/" + StringUtils.substring(link_text,start+1) + "/";
			}else if(start<end){
				link_text = "https://www.linkedin.com/in/" + StringUtils.substring(link_text,start+1,end) + "/";
			}else{
				link_text = "illegal";
			}
			link.setStandard_link(link_text);
			int i = linkDao.updateICO_Linkedin_LinkById(link.getStandard_link(),link.getId());
			System.out.println(i);
		}
	}


}
