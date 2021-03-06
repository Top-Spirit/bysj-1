package com.newview.bysj.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.newview.bysj.dao.ProvinceExcellentProjectDao;
import com.newview.bysj.domain.ProvinceExcellentProject;
import com.newview.bysj.helper.CommonHelper;
import com.newview.bysj.jpaRepository.MyRepository;
import com.newview.bysj.myAnnotation.MethodDescription;

@Service("provinceExcellentProjectService")
public class ProvinceExcellentProjectService extends BasicService<ProvinceExcellentProject, Integer> {

    ProvinceExcellentProjectDao provinceExcellentProjectDao;

    @Override
    @Autowired
    public void setDasciDao(
            MyRepository<ProvinceExcellentProject, Integer> basicDao) {
        // TODO Auto-generated method stub
        this.basicDao = basicDao;
        provinceExcellentProjectDao = (ProvinceExcellentProjectDao) basicDao;
    }

    @MethodDescription("获取被推荐为省优的课题")
    public Page<ProvinceExcellentProject> getPagesProvinceExcellentProjectsBySchoolAdmin(String title, String tutorName, Integer pageNo, Integer pageSize) {
        pageNo = CommonHelper.getPageNo(pageNo, pageSize);
        pageSize = CommonHelper.getPageSize(pageSize);
        Page<ProvinceExcellentProject> result = provinceExcellentProjectDao.findAll(new Specification<ProvinceExcellentProject>() {
            @Override
            public Predicate toPredicate(Root<ProvinceExcellentProject> root, CriteriaQuery<?> query, CriteriaBuilder cb) {
                // TODO Auto-generated method stub
                List<Predicate> predicates = new ArrayList<Predicate>();
                if (title != null || title != "") {
                    predicates.add(cb.like(root.get("graudateProject").get("title").as(String.class), "%" + title + "%"));
                }
                if (tutorName != null || tutorName != "") {
                    predicates.add(cb.like(root.get("graudateProject").get("proposer").get("name").as(String.class), "%" + tutorName + "%"));
                }
                predicates.add(cb.equal(root.get("graudateProject").get("year").as(Integer.class), CommonHelper.getYear()));
                Predicate[] p = new Predicate[predicates.size()];
                return cb.and(predicates.toArray(p));
            }

        }, new PageRequest(pageNo, pageSize, new Sort(Direction.DESC, "id")));
        return result;

    }

}
