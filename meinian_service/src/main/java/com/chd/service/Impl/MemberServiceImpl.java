package com.chd.service.Impl;

import com.alibaba.dubbo.config.annotation.Service;
import com.chd.dao.MemberDao;
import com.chd.pojo.Member;
import com.chd.service.MemberService;
import com.chd.util.DateUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service(interfaceClass = MemberService.class)
@Transactional
public class MemberServiceImpl implements MemberService {
    @Autowired
    MemberDao memberDao;
    @Override
    public Member findByTelephone(String telephone) {
        return memberDao.findByTelephone(telephone);
    }

    @Override
    public void add(Member member) {
        memberDao.add(member);
    }

    @Override
    public List<Integer> findMemberCountByMonth(List<String> list) {
        List<Integer> MemberCountList =new ArrayList<>();
        if (list!=null&&list.size()>0){
            for (String months : list) {
                // 获取指定月份的最后一天
                String regTime =  DateUtils.getLastDayOfMonth(months);
                //  迭代过去12个月，每个月注册会员的数量，根据注册日期查询
                Integer memberCount = memberDao.findMemberCountBeforeDate(regTime);
                MemberCountList.add(memberCount);
            }
        }

        return MemberCountList;
    }
}
