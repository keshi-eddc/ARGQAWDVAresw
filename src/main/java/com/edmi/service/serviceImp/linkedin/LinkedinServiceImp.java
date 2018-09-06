package com.edmi.service.serviceImp.linkedin;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.edmi.dao.linkedin.*;
import com.edmi.dto.linkedin.ICO_Linkedin_MemberDto;
import com.edmi.dto.linkedin.ICO_Linkedin_MembereducationexperienceDto;
import com.edmi.dto.linkedin.ICO_Linkedin_MemberselectionskillsDto;
import com.edmi.dto.linkedin.ICO_Linkedin_MemberworkexperienceDto;
import com.edmi.entity.linkedin.*;
import com.edmi.service.service.LinkedinService;
import com.edmi.utils.ExcelUtilForPOI;
import org.apache.commons.configuration.Configuration;
import org.apache.commons.configuration.ConfigurationException;
import org.apache.commons.configuration.PropertiesConfiguration;
import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.List;

@Service("linkService")
public class LinkedinServiceImp implements LinkedinService {

    Logger log = Logger.getLogger(LinkedinServiceImp.class);

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
    private ExcelUtilForPOI excelUtilForPOI;




    public void importLinkedInLinks(){
        List<String[]> datas = excelUtilForPOI.getData("C:\\Users\\EDDC\\Desktop\\linkedin links.xlsx");
        for(String[] data:datas){
            String link = StringUtils.trim(data[3]);
            if(!StringUtils.containsIgnoreCase(link,"linkedin.com")){
                log.info("非法的linkedin链接："+link);
            }else{
                log.info(link);
                String org_link = link;
                int start = StringUtils.indexOfIgnoreCase(link,"linkedin.com/in/")+15;//加15代表本字符串最后一个/的位置
                int end = StringUtils.indexOfIgnoreCase(link,"/",start+1);//查找start后面的/的位置
                if(end==-1){
                    link = "https://www.linkedin.com/in/" + StringUtils.substring(link,start+1) + "/";
                }else if(start<end){
                    link = "https://www.linkedin.com/in/" + StringUtils.substring(link,start+1,end) + "/";
                }else{
                    link = "illegal";
                }
                if(!StringUtils.equals("illegal",link)){
                    ICO_Linkedin_Link linkedin_link = linkDao.getICO_Linkedin_LinkBystandard_link(link);
                    if(null==linkedin_link){
                        log.info("新会员:"+link);
                        ICO_Linkedin_Link ico_linkedin_link = new ICO_Linkedin_Link();
                        ico_linkedin_link.setLink(org_link);
                        ico_linkedin_link.setStandard_link(link);
                        ico_linkedin_link.setLink_status("ini");
                        ico_linkedin_link.setMember_info_status("ini");
                        ico_linkedin_link.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                        ICO_Linkedin_Link flag = linkDao.save(ico_linkedin_link);
                        if(null!=flag){
                           log.info("新增会员成功："+link);
                        }else{
                            log.info("新增会员失败："+link);
                        }
                    }else{
                        log.info("老会员:"+link);
                    }
                }else{
                   log.info("Linkedin链接不合法！");
                }
            }

        }
    }

    public boolean analysisMembersToBase(Document doc, long link_id) {

        /*个人简历*/
        Elements name_eles = doc.getElementsByAttributeValueStarting("class", "pv-top-card-section__name");
        Elements headline_eles = doc.getElementsByAttributeValueStarting("class", "pv-top-card-section__headline");
        Elements company_eles = doc.getElementsByAttributeValueStarting("class", "pv-top-card-section__company");
        Elements company_eles_1 = doc.getElementsByAttributeValueStarting("class", "pv-top-card-v2-section__entity-name pv-top-card-v2-section__company-name");

        Elements school_eles = doc.getElementsByAttributeValueStarting("class", "pv-top-card-section__school");
        Elements school_eles_1 = doc.getElementsByAttributeValueStarting("class", "pv-top-card-v2-section__entity-name pv-top-card-v2-section__school-name");

        Elements location_eles = doc.getElementsByAttributeValueStarting("class", "pv-top-card-section__location");

        Elements connections_eles = doc.getElementsByAttributeValueStarting("class", "pv-top-card-section__connections pv-top-card-section__connections--with-separator");
        Elements connections_eles_1 = doc.getElementsByAttributeValueStarting("class", "pv-top-card-v2-section__entity-name pv-top-card-v2-section__connections");

        Elements summary_eles = doc.getElementsByAttributeValueStarting("class", "pv-top-card-section__summary-text");

        String name = "";//姓名
        if(null!=name_eles&&name_eles.size()>0){
            name = name_eles.first().text();
        }
        String headline = "";//职位
        if(null!=headline_eles&&headline_eles.size()>0){
            headline = headline_eles.first().text();
        }
        String company = "";//公司
        if(null!=company_eles&&company_eles.size()>0){
            company = company_eles.first().text();
        }
        if(null!=company_eles_1&&company_eles_1.size()>0){
            company = company_eles_1.first().text();
        }
        String school = "";////学校
        if(null!=school_eles&&school_eles.size()>0){
            school = school_eles.first().text();
        }
        if(null!=school_eles_1&&school_eles_1.size()>0){
            school = school_eles_1.first().text();
        }

        String location = "";//地址
        String location_nationality = "";
        String location_province = "";
        String location_city = "";
        if(null!=location_eles&&location_eles.size()>0){
            location = location_eles.first().text();
            if(StringUtils.isNotEmpty(location)){
                String[] locations = StringUtils.split(location, ",");
                if(null!=locations&&locations.length==1){
                    location_city = locations[0];
                }else if(null!=locations&&locations.length==2){
                    location_city = locations[0];
                    location_province = locations[1];
                }else if(null!=locations&&locations.length==3){
                    location_city = locations[0];
                    location_province = locations[1];
                    location_nationality = locations[2];
                }
            }
        }
        String connections = "0";//好友数
        if(null!=connections_eles&&connections_eles.size()>0){
            Elements conns = connections_eles.first().getElementsByAttributeValue("aria-hidden", "true");
            if(null!=conns&&conns.size()>0){
                connections = conns.first().text();
            }
        }
        if(null!=connections_eles_1&&connections_eles_1.size()>0){
            connections = connections_eles_1.first().text();
        }

        String summary = "";//介绍不是很全，从json里面获取
        /*String json_str = "{\"data\":{\"patentView\":"+StringUtils.substringBetween(doc.toString(),"{\"data\":{\"patentView\":","</code>");
        JSONObject json = JSONObject.parseObject(json_str);
        if(null!=json){
            JSONArray incluededs = json.getJSONArray("included");
            for(int i = 0;i<incluededs.size();i++) {
                JSONObject inclueded = incluededs.getJSONObject(i);
                String type = inclueded.getString("$type");
                if (StringUtils.equalsIgnoreCase("com.linkedin.voyager.identity.profile.Profile", type)) {
                    if(inclueded.containsKey("summary")&&null!=inclueded.getString("summary")){
                        summary = inclueded.getString("summary");
                    }
                }
            }
        }*/

        /*会员信息*/
        ICO_Linkedin_Member member = new ICO_Linkedin_Member();
        member.setName(StringUtils.defaultIfEmpty(name, ""));
        member.setPosition(StringUtils.defaultIfEmpty(headline, ""));
        member.setCompany(StringUtils.defaultIfEmpty(company, ""));
        member.setLink_id(link_id);
        member.setSchool(StringUtils.defaultIfEmpty(school, ""));
        if(StringUtils.isNotEmpty(location_province)){
            member.setProvince(StringUtils.defaultIfEmpty(location_province, ""));
        }
        if(StringUtils.isNotEmpty(location_city)){
            member.setCity(StringUtils.defaultIfEmpty(location_city, ""));
        }
        if(StringUtils.isNotEmpty(location_nationality)){
            member.setNationality(StringUtils.defaultIfEmpty(location_nationality,""));
        }
        member.setFriends_num(Integer.valueOf(StringUtils.defaultIfEmpty(StringUtils.replaceEachRepeatedly(connections,new String[]{"+","connections","connection"," "},new String[]{"","","",""}), "0")));
        member.setBrief(StringUtils.defaultIfEmpty(summary, ""));
        member.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
        member.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));


        /*工作经历*/
        Element experience_section = doc.getElementById("experience-section");
        List<ICO_Linkedin_Memberworkexperience> workexperiences = null;
        if (null != experience_section) {
            Elements experiences = new Elements();
            Elements experiences_eles_1 = experience_section.getElementsByAttributeValueStarting("class", "pv-profile-section__sortable-item pv-profile-section__section-info-item");
            Elements experiences_eles_2 = experience_section.getElementsByAttributeValueStarting("class", "pv-profile-section__card-item-v2 pv-profile-section pv-position-entity");
            experiences.addAll(experiences_eles_1);
            experiences.addAll(experiences_eles_2);

            workexperiences = new ArrayList<ICO_Linkedin_Memberworkexperience>();
            for (Element experience : experiences) {
                Elements experience_position_ele = experience.getElementsByAttributeValue("class", "pv-entity__summary-info");
                ICO_Linkedin_Memberworkexperience workexperience = new ICO_Linkedin_Memberworkexperience();
                if (null != experience_position_ele&&experience_position_ele.size()>0) {//职位
                    if (null != experience_position_ele.first().child(0)) {
                        String experience_position = StringUtils.defaultIfEmpty(experience_position_ele.first().child(0).text(), "");
                        workexperience.setPosition(experience_position);
                    }
                }
                Elements experience_company_ele = experience.getElementsContainingOwnText("Company Name");
                if (null != experience_company_ele&&experience_company_ele.size()>0) {//公司名称
                    Element experience_company_ele_next = experience_company_ele.first().nextElementSibling();
                    if (null != experience_company_ele_next) {
                        String experience_company = StringUtils.defaultIfEmpty(experience_company_ele_next.text(), "");
                        workexperience.setCompany(experience_company);
                    }
                }
                Elements experience_date_ele = experience.getElementsContainingOwnText("Dates Employed");
                if (null != experience_date_ele&&experience_date_ele.size()>0) {//入职日期
                    Element experience_date_next = experience_date_ele.first().nextElementSibling();
                    if (null != experience_date_next) {
                        String experience_date = experience_date_next.text();
                        String[] dates = StringUtils.split(experience_date, "–");
                        if (null != dates) {
                            if (dates.length == 2) {
                                String experience_date_from = dates[0];
                                String experience_date_end = dates[1];
                                workexperience.setDuration_start(experience_date_from);
                                workexperience.setDuration_end(experience_date_end);
                            } else if (dates.length == 1) {
                                String experience_date_from = dates[0];
                                workexperience.setDuration_start(experience_date_from);

                            }
                        }
                    }
                }
                Elements experience_location_ele = experience.getElementsContainingOwnText("Location");
                if (null != experience_location_ele&&experience_location_ele.size()>0&&"visually-hidden".equals(experience_location_ele.attr("class"))) {//所在地区
                    Element experience_location_next = experience_location_ele.first().nextElementSibling();
                    if (null != experience_location_next) {
                        String experience_location = StringUtils.defaultIfEmpty(experience_location_next.text(), "");
                        workexperience.setWork_address(experience_location);
                    }
                }
                Elements experience_description_eles = experience.getElementsByAttributeValueStarting("class","pv-entity__description");
                if(null!=experience_description_eles&&experience_description_eles.size()>0){
                    Elements description = experience_description_eles.first().getElementsByAttributeValueStarting("class", "pv-entity__description");

                   if(null!=description&&description.size()>0){
                       String  experience_description  = StringUtils.defaultIfEmpty(description.first().text(),"");
                       workexperience.setPosition_desc(experience_description);
                   }
                }
                workexperience.setMember(member);
                workexperience.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                workexperience.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                workexperiences.add(workexperience);
            }
        }

        /*教育经历*/
        Element education_section = doc.getElementById("education-section");
        List<ICO_Linkedin_Membereducationexperience> educationexperiences = null;
        if (null != education_section) {
            Elements educations_eles = education_section.getElementsByAttributeValueStarting("class", "pv-profile-section__sortable-item pv-profile-section__section-info-item");
            Elements educations_eles_1 = education_section.getElementsByAttributeValueStarting("class", "pv-education-entity pv-profile-section__card-item");
            educations_eles.addAll(educations_eles_1);
            educationexperiences = new ArrayList<ICO_Linkedin_Membereducationexperience>();
            for (Element education_ele : educations_eles) {
                Elements education_ele_summarys = education_ele.getElementsByAttributeValue("class", "pv-entity__summary-info");
                ICO_Linkedin_Membereducationexperience membereducationexperience = new ICO_Linkedin_Membereducationexperience();
                if (null != education_ele_summarys) {
                    Element education_ele_summary = education_ele_summarys.first();

                    Elements education_ele_degrees = education_ele_summary.getElementsByAttributeValue("class", "pv-entity__degree-info");//学位信息
                    if (null != education_ele_degrees&&education_ele_degrees.size()>0) {
                        Element education_ele_degree = education_ele_degrees.first();
                        if(null!=education_ele_degree.children()&&education_ele_degree.children().size()>0){
                            String shcool_name = StringUtils.defaultIfEmpty(education_ele_degree.children().first().text(), "");//学校名字
                            membereducationexperience.setSchool(shcool_name);
                        }
                        Elements degree_ele = education_ele_degree.getElementsContainingOwnText("Degree Name");
                        if (null != degree_ele&&degree_ele.size()>0) {//学位
                            Element degree_ele_next = degree_ele.first().nextElementSibling();;
                            if (null != degree_ele_next) {
                                String degree = StringUtils.defaultIfEmpty(degree_ele_next.text(), "");
                                membereducationexperience.setDegree(degree);
                            }
                        }
                        Elements major_ele = education_ele_degree.getElementsContainingOwnText("Field Of Study");
                        if (null != major_ele&&major_ele.size()>0) {//专业
                            Element major_ele_next = major_ele.first().nextElementSibling();
                            if (null != major_ele_next) {
                                String major = StringUtils.defaultIfEmpty(major_ele_next.text(), "");
                                membereducationexperience.setMajor(major);
                            }
                        }

                    }
                    Elements education_date_ele = education_ele_summary.getElementsContainingOwnText("Dates attended or expected graduation");
                    if (null != education_date_ele&&education_date_ele.size()>0) {//就读时间
                        Element education_date_next = education_date_ele.first().nextElementSibling();
                        if (null != education_date_next) {
                            String education_date = education_date_next.text();
                            String[] dates = StringUtils.split(education_date, "–");
                            if (null != dates) {
                                if (dates.length == 2) {
                                    String education_date_from = dates[0];
                                    String education_date_end = dates[1];
                                    membereducationexperience.setDuration_start(education_date_from);
                                    membereducationexperience.setDuration_end(education_date_end);
                                } else if (dates.length == 1) {
                                    String education_date_from = dates[0];
                                    membereducationexperience.setDuration_start(education_date_from);
                                }
                            }
                        }
                    }
                    Elements education_activities_ele = education_ele_summary.getElementsContainingOwnText("Activities and Societies:");
                    if (null != education_activities_ele&&education_activities_ele.size()>0) {//社团活动
                        Element education_activities_next = education_activities_ele.first().nextElementSibling();
                        if (null != education_activities_next) {
                            String education_activities = StringUtils.defaultIfEmpty(education_activities_next.text(), "");
                            membereducationexperience.setAssociation_activity(education_activities);
                        }
                    }
                }
                Elements education_eles_desc = education_ele.getElementsByAttributeValue("class", "pv-entity__extra-details");
                if (null != education_eles_desc&&education_eles_desc.size()>0) {
                    Elements description = education_eles_desc.first().getElementsByAttributeValueStarting("class", " pv-entity__description");
                    if (null != description&&description.size()>0) {
                        String education_desc = StringUtils.defaultIfEmpty(description.first().text(), "");
                        membereducationexperience.setDescription(education_desc);
                    }
                }
                membereducationexperience.setMember(member);
                membereducationexperience.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                membereducationexperience.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                educationexperiences.add(membereducationexperience);
            }

        }

        /*精选技能*/
        Elements skills_eles = doc.getElementsByAttributeValueStarting("class", "pv-profile-section pv-skill-categories-section");
        List<ICO_Linkedin_Memberselectionskills> memberselectionskills = null;
        if (null != skills_eles && skills_eles.size() > 0) {
            memberselectionskills = new ArrayList<ICO_Linkedin_Memberselectionskills>();
            /*第一部分技能*/
            Elements skill_eles_1 = skills_eles.first().getElementsByAttributeValueStarting("class", "pv-skill-category-entity__top-skill pv-skill-category-entity");
            /*第一部分后的技能*/
            Elements skill_eles_2 = skills_eles.first().getElementsByAttributeValueStarting("class", "pv-skill-category-entity pv-skill-category-entity--secondary");
            skill_eles_1.addAll(skill_eles_2);
            if(null!=skill_eles_1&&skill_eles_1.size()>0){
                for (Element skill_ele : skill_eles_1) {
                    ICO_Linkedin_Memberselectionskills memberselectionskill = new ICO_Linkedin_Memberselectionskills();
                    /*技能名称*/
                    Elements skill_name_ele = skill_ele.getElementsByAttributeValueStarting("class","pv-skill-category-entity__name");
                    if(null!=skill_name_ele&&skill_name_ele.size()>0){
                        String skill_name = skill_name_ele.first().text();
                        memberselectionskill.setSkill(skill_name);
                    }
                    /*认可数量*/
                    Elements skill_count_ele = skill_ele.getElementsByAttributeValueStarting("class","pv-skill-category-entity__endorsement-count");
                    if(null!=skill_count_ele&&skill_count_ele.size()>0){
                        String skill_count = StringUtils.replaceOnce(skill_count_ele.first().text(),"+","");
                        memberselectionskill.setApprove_num(Integer.parseInt(skill_count));
                    }

                    memberselectionskill.setMember(member);
                    memberselectionskill.setInsert_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    memberselectionskill.setModify_time(new Timestamp(Calendar.getInstance().getTime().getTime()));
                    memberselectionskills.add(memberselectionskill);
                }
            }

        }
        member.setEducationexperiences(educationexperiences);
        member.setSelectionskills(memberselectionskills);
        member.setWorkexperiences(workexperiences);
        ICO_Linkedin_Member m = memberDao.save(member);
        if(null!=m){
            log.info("会员信息保存成功,link_id："+m.getLink_id());
            return true;
        }else{
            log.info("会员信息保存失败,link_id："+m.getLink_id());
            return false;
        }
    }
    public void readLinkedinFilesToBase(){
        Configuration config = null;
        String filePath = "";
        String fileBackupPath = "";
        try {
            config = new PropertiesConfiguration("path.properties" );
            filePath = config.getString("linkedin.filePath");
            fileBackupPath = config.getString("linkedin.fileBackupPath");
        } catch (ConfigurationException e) {
            log.info(e.getMessage());
        }
        File f = new File(filePath);
        Collection<File> files = FileUtils.listFiles(f, new String[]{"html","htm"}, false);
        for(File file:files){
            String content = null;
            try {
                content = FileUtils.readFileToString(file,"utf-8");
            } catch (IOException e) {
                log.info(e.getMessage());
            }
            Document doc = Jsoup.parse(content);
            doc.charset(Charset.forName("utf-8"));
            String linked_id = StringUtils.substringBeforeLast(StringUtils.trim(file.getName()),".");
            log.info("正在解析文件："+file.getName());
            ICO_Linkedin_Member member = memberDao.getICO_Linkedin_MemberByLinkId(Long.valueOf(linked_id));
            if(null!=member){
                try {
                    log.info("会员已存在："+member.getLink_id());
                    String file_files = file.getParentFile().getAbsolutePath()+"\\"+StringUtils.substringBeforeLast(StringUtils.trim(file.getName()),".")+"_files";
                    FileUtils.moveFileToDirectory(file,new File(fileBackupPath),true);
                    FileUtils.moveDirectoryToDirectory(new File(file_files),new File(fileBackupPath),true);
                } catch (IOException e) {
                    log.info("文件移动失败，fileName："+file.getName()+",destDir："+fileBackupPath+e.getMessage());
                }
                continue;
            }
            boolean flag = false;
            try {
                 flag = this.analysisMembersToBase(doc,Long.valueOf(linked_id));
            }catch (Exception e){
                 log.info(file.getName()+"文件解析错误："+e.getMessage());
            }

            if(flag){
                try {
                    String file_files = file.getParentFile().getAbsolutePath()+"\\"+StringUtils.substringBeforeLast(StringUtils.trim(file.getName()),".")+"_files";
                    FileUtils.moveFileToDirectory(file,new File(fileBackupPath),true);
                    FileUtils.moveDirectoryToDirectory(new File(file_files),new File(fileBackupPath),true);
                } catch (IOException e) {
                    log.info("文件移动失败，fileName："+file.getName()+",destDir："+fileBackupPath);
                }
            }
            log.info("文件："+file.getName()+"解析完毕");
        }
    }

    @Override
    public JSONObject get_ico_linkedin_member_info(String link) {
        JSONObject json = new JSONObject();
        json.put("link",link);
        int start = StringUtils.indexOfIgnoreCase(link,"linkedin.com/in/")+15;//加15代表本字符串最后一个/的位置
        int end = StringUtils.indexOfIgnoreCase(link,"/",start+1);//查找start后面的/的位置
        if(null==link||!StringUtils.contains(link,"linkedin.com")||start==14){
            link = "illegal";
        }else if(end==-1){
            link = "https://www.linkedin.com/in/" + org.apache.commons.lang.StringUtils.substring(link,start+1) + "/";
        }else if(start<end){
            link = "https://www.linkedin.com/in/" + org.apache.commons.lang.StringUtils.substring(link,start+1,end) + "/";
        }else{
            link = "illegal";
        }
        if("illegal".equals(link)){
            json.put("standard_link","illegal");
             return json;
        }else{
            json.put("standard_link",link);
            ICO_Linkedin_Link linkedin_link = linkDao.getICO_Linkedin_LinkBystandard_link(link);
            if(null!=linkedin_link){
                ICO_Linkedin_Member member = memberDao.getICO_Linkedin_MemberByLinkId(linkedin_link.getId());

                if(null!=member){
                    ICO_Linkedin_MemberDto memberdto = new ICO_Linkedin_MemberDto();
                    BeanUtils.copyProperties(member,memberdto);
                    json.put("member", JSON.toJSON(memberdto));
                }else{
                    json.put("member", "new");
                    return json;
                }

                List<ICO_Linkedin_Membereducationexperience> educationexperiences = member.getEducationexperiences();
                if(null!=educationexperiences&&educationexperiences.size()>0){
                    List<ICO_Linkedin_MembereducationexperienceDto> educationexperiencesdto = new ArrayList<>();
                    for(ICO_Linkedin_Membereducationexperience educationexperience:educationexperiences){
                        ICO_Linkedin_MembereducationexperienceDto membereducationexperienceDto = new ICO_Linkedin_MembereducationexperienceDto();
                        BeanUtils.copyProperties(educationexperience,membereducationexperienceDto);
                        educationexperiencesdto.add(membereducationexperienceDto);
                    }
                    json.put("educationexperiences", JSON.toJSON(educationexperiencesdto));
                }

                List<ICO_Linkedin_Memberselectionskills> ico_linkedin_memberselectionskills = member.getSelectionskills();
                if(null!=ico_linkedin_memberselectionskills&&ico_linkedin_memberselectionskills.size()>0){
                    List<ICO_Linkedin_MemberselectionskillsDto> memberselectionskillsDtos = new ArrayList<>();
                    for(ICO_Linkedin_Memberselectionskills icoLinkedinMemberselectionskill:ico_linkedin_memberselectionskills){
                        ICO_Linkedin_MemberselectionskillsDto memberselectionskillsDto = new ICO_Linkedin_MemberselectionskillsDto();
                        BeanUtils.copyProperties(icoLinkedinMemberselectionskill,memberselectionskillsDto);
                        memberselectionskillsDtos.add(memberselectionskillsDto);
                    }
                    json.put("selectionskills",memberselectionskillsDtos);
                }

                List<ICO_Linkedin_Memberworkexperience> ico_linkedin_memberworkexperiences = member.getWorkexperiences();
                if(null!=ico_linkedin_memberworkexperiences&&ico_linkedin_memberworkexperiences.size()>0){
                    List<ICO_Linkedin_MemberworkexperienceDto> memberworkexperienceDtos = new ArrayList<>();
                    for(ICO_Linkedin_Memberworkexperience ico_linkedin_memberworkexperience:ico_linkedin_memberworkexperiences){
                        ICO_Linkedin_MemberworkexperienceDto memberworkexperienceDto = new ICO_Linkedin_MemberworkexperienceDto();
                        BeanUtils.copyProperties(ico_linkedin_memberworkexperience,memberworkexperienceDto);
                        memberworkexperienceDtos.add(memberworkexperienceDto);
                    }
                    json.put("workexperiences",memberworkexperienceDtos);
                }
            }else{
                json.put("member", "new");
            }
            return json;
        }
    }
}
