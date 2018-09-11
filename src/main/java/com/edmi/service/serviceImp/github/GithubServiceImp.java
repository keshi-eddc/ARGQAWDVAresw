package com.edmi.service.serviceImp.github;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edmi.dao.github.ICO_Github_LinksRepository;
import com.edmi.dao.github.ICO_Github_OrganizationRepository;
import com.edmi.dao.github.ICO_Github_RepositoriesRepository;
import com.edmi.dao.github.ICO_Github_Repositories_DetailsRepository;
import com.edmi.entity.github.ICO_Github_Links;
import com.edmi.entity.github.ICO_Github_Organization;
import com.edmi.entity.github.ICO_Github_Repositories;
import com.edmi.entity.github.ICO_Github_Repositories_Details;
import com.edmi.service.service.GithubService;
import com.edmi.service.serviceImp.linkedin.LinkedinServiceImp;
import com.edmi.utils.ExcelUtilForPOI;
import com.edmi.utils.http.HttpClientUtil;
import com.edmi.utils.http.exception.MethodNotSupportException;
import com.edmi.utils.http.request.Request;
import com.edmi.utils.http.request.RequestMethod;
import com.edmi.utils.http.response.Response;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

@Service("githubService")
public class GithubServiceImp implements GithubService {

    Logger log = Logger.getLogger(LinkedinServiceImp.class);

    @Autowired
    private ExcelUtilForPOI excelUtilForPOI;
    @Autowired
    private ICO_Github_LinksRepository ico_github_linksRepository;
    @Autowired
    private ICO_Github_OrganizationRepository ico_github_organizationRepository;
    @Autowired
    private ICO_Github_RepositoriesRepository ico_github_repositoriesRepository;
    @Autowired
    private ICO_Github_Repositories_DetailsRepository ico_github_repositories_detailsRepository;

    @Override
    public void importICO_Github_Links(String file) {
        //String fileURL = "E:\\ICO\\github\\github_list.xlsx";
        List<String[]> list = excelUtilForPOI.getData(file);
        if(list != null&&list.size() != 0){
            List<ICO_Github_Links> ico_github_links = new ArrayList<>();
            for (int i = 0; i < list.size(); i++) {
                ICO_Github_Links ico_github_link = new ICO_Github_Links();
                String[] linkObject = list.get(i);
                String sourceURL = linkObject[0];
                String communityLink = linkObject[1];
                String gitCommunity = linkObject[1];
                if(StringUtils.contains(gitCommunity,"//")){
                    gitCommunity = StringUtils.substring(gitCommunity,StringUtils.indexOf(gitCommunity,"//")+2);
                }
                String[] results = StringUtils.split(gitCommunity,"/");
                if(results.length>1){
                    gitCommunity = results[1];
                }else{
                    gitCommunity = "";
                }
                ico_github_link.setSource_url(sourceURL);
                ico_github_link.setCommunity_link(communityLink);
                ico_github_link.setGithub_community(gitCommunity);
                if(StringUtils.isNotEmpty(gitCommunity)){
                    ico_github_link.setStatus("ini");
                }else{
                    ico_github_link.setStatus("illegal");
                }
                ico_github_link.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                ico_github_links.add(ico_github_link);
            }
            ico_github_linksRepository.saveAll(ico_github_links);
        }
    }

    @Override
    public void getICO_Github_Organization() throws MethodNotSupportException {
        List<ICO_Github_Links> links = ico_github_linksRepository.getICO_Github_LinksByStatus("ini");
        for(ICO_Github_Links link:links){
            String url = "https://api.github.com/orgs/"+link.getGithub_community();
            Request request = new Request(url, RequestMethod.GET);
            request.addUrlParam("access_token","567c857e18b725aab9f8b73d7d23d04cd74641d4");
            Response response = HttpClientUtil.doRequest(request);

            int code = response.getCode(); //response code
            if(200 ==code){
                String content = response.getResponseText(); //response text
                if(null!=content){
                    ICO_Github_Organization organization = JSONObject.parseObject(content,ICO_Github_Organization.class);

                    organization.setIco_github_links(link);
                    organization.setStatus("ini");
                    organization.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));

                    organization = ico_github_organizationRepository.save(organization);
                    if(null!= organization){
                        log.info("组织信息保存成功："+organization.getPk_id());
                        link.setStatus(String.valueOf(code));
                        link = ico_github_linksRepository.save(link);
                        if(null!=link){
                            log.info("组织link状态更新成功："+link.getId());
                        }

                    }
                }
            }else if(404==code){
                log.info("组织信息不存在："+link.getId());
                link.setStatus(String.valueOf(code));
                link = ico_github_linksRepository.save(link);
                if(null!=link){
                    log.info("组织link状态更新成功："+link.getId());
                }
            }else{
                log.info("组织信息不存在："+link.getId());
                link.setStatus(String.valueOf(code));
                link = ico_github_linksRepository.save(link);
                if(null!=link){
                    log.info("组织link状态更新成功："+link.getId());
                }
            }
            try {
                Thread.sleep(2*1000);
            } catch (InterruptedException e) {
                log.info(e.getMessage());
            }
        }

    }

    @Override
    public void getICO_Github_Repositories(ICO_Github_Organization org) throws MethodNotSupportException {
        Request request = new Request(org.getRepos_url(), RequestMethod.GET);
        request.addUrlParam("access_token","d1bd5a365c66c3c237f0746ff747077da64bdc21");
        request.addUrlParam("page",1);
        request.addUrlParam("per_page",100);
        Response response = HttpClientUtil.doRequest(request);

        int code = response.getCode(); //response code
        if(200 ==code){
            String content = response.getResponseText(); //response text
            JSONArray repositories = new JSONArray();
            if(null!=content){
                JSONArray repositories_array = JSONObject.parseArray(content);
                repositories.addAll(repositories_array);
                while(null!=repositories_array && repositories_array.size()==100){
                    request.addUrlParam("page",Integer.parseInt(request.getUrlParams().get("page").toString())+1);
                    response = HttpClientUtil.doRequest(request);
                    code = response.getCode();
                    if(200 ==code){
                        content = response.getResponseText();
                        if(null!=content) {
                            repositories_array = JSONObject.parseArray(content);
                            repositories.addAll(repositories_array);
                        }
                    }else if(304 == code){
                        log.info("当前组织下的项目同步完毕org_pk_id："+org.getPk_id());
                        break;
                    }else if(404==code){
                        log.info("当前组织下没有项目org_pk_id："+org.getPk_id());
                        break;
                    }else if(403==code){
                        log.info("API请求次数超限，项目org_pk_id："+org.getPk_id());
                        repositories_array.clear();
                        break;
                    }
                }
                for(int i=0;i<repositories_array.size();i++){//处理license信息
                    JSONObject repository = repositories_array.getJSONObject(i);
                    if(repository.containsKey("license")&&null!=repository.getJSONObject("license")){
                        repository.put("license_key",repository.getJSONObject("license").getString("key"));
                        repository.put("license_name",repository.getJSONObject("license").getString("name"));
                    }else{
                        repository.put("license_key","");
                        repository.put("license_name","");
                    }
                }

                List<ICO_Github_Repositories> ico_github_repositories = JSONArray.parseArray(repositories_array.toJSONString(), ICO_Github_Repositories.class);
                if(null!=ico_github_repositories&&ico_github_repositories.size()>0){
                    for(ICO_Github_Repositories ico_github_repository:ico_github_repositories){
                        ico_github_repository.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        ico_github_repository.setDetail_status("ini");
                        ico_github_repository.setCode_zip_status("ini");
                        ico_github_repository.setIco_github_organization(org);
                    }
                    ico_github_repositories = ico_github_repositoriesRepository.saveAll(ico_github_repositories);
                    if(null!=ico_github_repositories){
                        log.info("当前组织下的项目保存成功，org_pk_id："+org.getPk_id());
                        org.setStatus("200");
                        ico_github_organizationRepository.save(org);
                    }else {
                        log.info("当前组织下的项目保存失败，org_pk_id："+org.getPk_id());
                        org.setStatus("ini");
                        ico_github_organizationRepository.save(org);
                    }
                }else {
                    log.info("当前组织下没有项目，org_pk_id："+org.getPk_id());
                    org.setStatus("200");
                    ico_github_organizationRepository.save(org);
                }
            }
        }else if(304 == code){
            log.info("当前组织下的项目同步完毕org_pk_id："+org.getPk_id());
            org.setStatus("200");
            ico_github_organizationRepository.save(org);
        }else if(404==code){
            log.info("当前组织下没有项目org_pk_id："+org.getPk_id());
            org.setStatus("200");
            ico_github_organizationRepository.save(org);
        }else if(403==code){
            log.info("API请求次数超限，项目org_pk_id："+org.getPk_id());
        }
    }

    @Override
    public void getICO_Github_Repositories_Details(ICO_Github_Repositories ogr) throws MethodNotSupportException {
        Request request = new Request(ogr.getUrl(), RequestMethod.GET);
        request.addUrlParam("access_token","d1bd5a365c66c3c237f0746ff747077da64bdc21");
        Response response = HttpClientUtil.doRequest(request);

        int code = response.getCode(); //response code
        if(200 ==code) {
            String content = response.getResponseText(); //response text
            if (null != content) {
                JSONObject obj = JSONObject.parseObject(content);
                if(obj.containsKey("license")&&null!=obj.getJSONObject("license")){
                    obj.put("license_key",obj.getJSONObject("license").getString("key"));
                    obj.put("license_name",obj.getJSONObject("license").getString("name"));
                    obj.put("license_url",obj.getJSONObject("license").getString("url"));
                }else{
                    obj.put("license_key","");
                    obj.put("license_name","");
                    obj.put("license_url","");
                }
                ICO_Github_Repositories_Details ico_github_repositories_details = JSONObject.parseObject(obj.toJSONString(),ICO_Github_Repositories_Details.class);
                if(null!=ico_github_repositories_details){
                    ico_github_repositories_details.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    ico_github_repositories_details.setIco_github_repositories(ogr);
                    ico_github_repositories_details = ico_github_repositories_detailsRepository.save(ico_github_repositories_details);
                    if(null!=ico_github_repositories_details){
                        log.info("Repository的详情保存成功，rpository_pk_id:"+ogr.getPk_id());
                        ogr.setDetail_status(String.valueOf(code));
                        ico_github_repositoriesRepository.save(ogr);
                    }else{
                        log.info("Repository的详情保存失败，rpository_pk_id:"+ogr.getPk_id());
                    }
                }else {
                    log.info("Repository的详情同步失败，没有详情信息，rpository_pk_id:"+ogr.getPk_id());
                }

            }
        }
    }

}
