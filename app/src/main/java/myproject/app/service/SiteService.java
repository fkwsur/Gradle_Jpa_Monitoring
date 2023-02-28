package myproject.app.service;


import java.util.List;
import java.util.Map;
import java.util.List;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import myproject.app.models.Site;
import myproject.app.models.User;
import myproject.app.models.SiteCctv;
import myproject.app.repository.SiteRepository;
import myproject.app.repository.SiteCctvRepository;
import myproject.app.repository.SiteRepositoryImpl;
import myproject.app.repository.SiteCctvRepositoryImpl;

@Service
@Transactional(readOnly = true)
public class SiteService {
    private final SiteRepository siteRepository;
    private final SiteRepositoryImpl siteRepositoryImpl;
    private final SiteCctvRepository siteCctvRepository;
    private final SiteCctvRepositoryImpl siteCctvRepositoryImpl;

    @Autowired
    public SiteService(SiteRepository siteRepository, SiteRepositoryImpl siteRepositoryImpl,SiteCctvRepository siteCctvRepository,SiteCctvRepositoryImpl siteCctvRepositoryImpl) {
        this.siteRepository = siteRepository;
        this.siteRepositoryImpl = siteRepositoryImpl;
        this.siteCctvRepository = siteCctvRepository;
        this.siteCctvRepositoryImpl = siteCctvRepositoryImpl;
    }

    public List<Site> FindByIdIn(List<String> id) throws Exception {
        try {
            return siteRepository.findByIdIn(id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Map<String, Object>> GetSiteList(Integer offset) throws Exception {
        try {
            List<Map<String, Object>> list = siteRepository.GetSiteList(offset);
            return list;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Site> GetSiteListNopage() throws Exception {
        try {
            List<Site> list = siteRepository.findAllByOrderBySiteName();
            return list;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public Boolean AddSite(Site site) throws Exception {
        try {
            siteRepository.save(site);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            throw new Exception(e);
        }
    }

    @Transactional
    public Site UpdateSite(Site req) throws Exception {
        try {
            Site site = siteRepository.findOneById(req.getId());
            if (site == null) {
                return null;
            } else {
                site.setSiteName(req.getSiteName());
                site.setNumber(req.getNumber());
                site.setCompany(req.getCompany());
                site.setAddress(req.getAddress());
                siteRepository.save(site);
                return site;
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public void DeleteSite(Site req) throws Exception {
        try {
            siteRepository.deleteById(req.getId());
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public int UpdateUserSite(String id) throws Exception {
        try {
            String reId = ','+ id + '|' + id + ',' + '|' + id;
            String likeId = '%' + id + '%';
            siteRepository.UpdateUserSite(reId,likeId);
            return siteRepository.UpdateGrade("승인대기","","관리자");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Map<String, Object>> SearchSiteName(String keywords, Integer offset) throws Exception {
        try {
            String likeKeywords = '%' + keywords + '%';
            List<Map<String, Object>> list =  siteRepository.SearchSiteName(likeKeywords,offset);
            return list;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public int UpdateUserGrade() throws Exception {
        try {
            return siteRepository.UpdateGrade("승인대기","","관리자");
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public Boolean AddCCTV(SiteCctv req) throws Exception {
        try {
            siteCctvRepository.save(req);
            return true;
        } catch (Exception e) {
            // TODO: handle exception
            throw new Exception(e);
        }
    }


    @Transactional
    public SiteCctv UpdateCCTV(SiteCctv req) throws Exception {
        try {
            SiteCctv siteCctv = siteCctvRepository.findOneById(req.getId());
            if (siteCctv == null) {
                return null;
            } else {
                siteCctv.setCctvUrl(req.getCctvUrl());
                siteCctv.setCctvNickname(req.getCctvNickname());
                siteCctv.setHlsUrlRealtime(req.getHlsUrlRealtime());
                siteCctv.setHlsUrlStreaming(req.getHlsUrlStreaming());
                return siteCctv;
            }
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<SiteCctv> GetCCTVList(String site_id) throws Exception {
        try {
            return siteCctvRepository.findBySiteId(site_id);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }


    @Transactional
    public void DeleteCctv(SiteCctv req) throws Exception {
        try {
            siteCctvRepository.delete(req);
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    @Transactional
    public int DeleteCctvBySiteId(String id) throws Exception {
        try {
            int list = siteCctvRepository.deleteAllBySiteId(id);
            return list;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    public List<Map<String, Object>> findAndCountAll(String id, Integer offset) throws Exception {
        try {
            List<Map<String, Object>> list =  siteRepository.findAndCountAll(id,offset);
            return list;
        } catch (Exception e) {
            throw new Exception(e);
        }
    }

    
}
