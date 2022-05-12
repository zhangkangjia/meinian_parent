package com.chd.dao;

import com.chd.pojo.Member;

public interface MemberDao {
    // 添加会员
    void add(Member member);
    // 根据手机号查询会员信息（唯一）
    Member findByTelephone(String telephone);

    Integer findMemberCountBeforeDate(String regTime);

    int getTodayNewMember(String today);

    int getTotalMember();

    int getThisWeekNewMember(String weekMonday);

    int GetThisMonthNewMember(String monthFirst);
}
